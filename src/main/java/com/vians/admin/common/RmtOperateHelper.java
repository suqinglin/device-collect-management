package com.vians.admin.common;

import com.alibaba.fastjson.JSONObject;
import com.vians.admin.model.DeviceBaseInfo;
import com.vians.admin.model.RootUserInfo;
import com.vians.admin.response.ResponseCode;
import com.vians.admin.response.ResponseData;
import com.vians.admin.service.DeviceService;
import com.vians.admin.service.RootUserService;
import com.vians.admin.utils.HttpUtil;
import com.vians.admin.utils.KeyUtil;
import com.vians.admin.web.AppBean;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName RmtOperateHelper
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/21 20:58
 * @Version 1.0
 **/
public class RmtOperateHelper {

    public static ResponseData checkAck(
//            PropUtil propUtil,
//            RedisService redisService,
            RootUserInfo rootUserInfo,
            RootUserService rootUserService,
            String userId,
            String token,
            String cnt,
            String mac,
            String crc,
            boolean isAddFp) throws InterruptedException {
        int nextCnt = Integer.parseInt(cnt, 16) + 1;
        ResponseData responseData = new ResponseData();
        int timeout = isAddFp ? 4 : 2;
        while (timeout > 0) {
            Thread.sleep(2000);
            // 1. 组装请求参数
            String key = KeyUtil.getAppKey(token, rootUserInfo.getPassword(), nextCnt);
            Map<String, Object> requestParam = new HashMap<>();
            requestParam.put("UserID", userId); // 后台分配的用户号
            requestParam.put("Key", key); // 应用密钥 AppKey
            requestParam.put("MAC", mac); // 锁具 MAC 地址
            requestParam.put("CRC16", crc);
            // 3. 发送请求
            JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, CommConstants.C_API_URL + "/api/CheckFileAck");
            int result = (int) respObj.get("Result");
            // result == 0：成功->返回成功
            // result == 1011：服务器与设备正在通讯->继续查询
            // result == 其他：异常->返回错误
            String respCnt = respObj.get("CNT").toString();
            if (!StringUtils.isEmpty(respCnt)) {
                // 5. 保存cnt到DB
                rootUserInfo.setCnt(Long.parseLong(respCnt, 16));
                rootUserService.updateRootUser(rootUserInfo);
            }
            if (result == 0) {
                return responseData.setResponseCode(ResponseCode.SUCCESS);
            } else if (result == 1011){
                nextCnt = Integer.parseInt(respCnt, 16) + 1;
                timeout --;
            } else {
                return responseData.setResponseCode(HttpUtil.getInstance().getResponseCode(result));
            }
        }
        return responseData.setResponseCode(ResponseCode.ERROR_API_ACK_TIMEOUT);
    }

    public static ResponseData rmtOperateByMac(
//            PropUtil propUtil,
//            RedisService redisService,
            long rootId,
            RootUserService rootUserService,
            AppBean appBean,
            String url, String mac, String idx, boolean isAddFp, Callback callback) throws InterruptedException {
        RootUserInfo rootUserInfo = rootUserService.getRootUserById(rootId);
        if (rootUserInfo == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        System.out.println("rootUserInfo->" + rootUserInfo.toString());
        String token = rootUserInfo.getToken();
        String userId = String.valueOf(rootUserInfo.getUserId());
        // 组装请求参数
        Map<String, Object> requestParam = new HashMap<>();
        // cnt加1
        long nextCnt = rootUserInfo.getCnt() + 1;
        String appKey = KeyUtil.getAppKey(token, rootUserInfo.getPassword(), nextCnt);
        requestParam.put("UserID", userId); // 后台分配的用户号
        requestParam.put("Key", appKey); // 应用密钥 AppKey
        requestParam.put("Index", idx); // 锁序号
        requestParam.put("MAC", mac); // 锁具 MAC 地址
        callback.addParams(requestParam, token, rootUserInfo.getPassword(), nextCnt);
        // 发送请求
        JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, CommConstants.C_API_URL + url);
        int result = (int) respObj.get("Result");
        if (result == 0) {
            String crc = respObj.get("CRC16").toString();
            String respCnt = respObj.get("CNT").toString();
            // 5. 保存cnt到DB
            rootUserInfo.setCnt(Long.parseLong(respCnt, 16));
            rootUserService.updateRootUser(rootUserInfo);
            ResponseData responseData = RmtOperateHelper.checkAck(
                    rootUserInfo, rootUserService, userId, token, respCnt, mac, crc, isAddFp);
            // 如果成功，便保存到数据库，否则，直接将异常返回给客户端
            if (ResponseCode.SUCCESS.getKey().equals(responseData.getCode())) {
                if (appBean != null) {
                    Long createUserId = appBean.getCurrentUserId();
                    if (createUserId == null) {
                        return new ResponseData(ResponseCode.ERROR_UN_AUTHORIZE_LOGIN);
                    }
                    callback.saveToDB(createUserId);
                }
            }
            return responseData;
        } else if (result == 1002) {
            return HttpUtil.getInstance().reLogin(
                    rootUserInfo.getPhone(),
                    rootUserInfo.getPassword(),
                    rootUserService,
                    new HttpUtil.ReLoginCallback() {
                @Override
                public ResponseData success() {
                    try {
                        return rmtOperateByMac(rootId, rootUserService, appBean, url, mac, idx, isAddFp, callback);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return ResponseData.error();
                    }
                }

                @Override
                public ResponseData failure(int result) {
                    return HttpUtil.getInstance().doResult(result);
                }
            });
        } else {
            return HttpUtil.getInstance().doResult(result);
        }
    }

    public static ResponseData rmtOperateByRoom(
            long rootId,
            RootUserService rootUserService,
            AppBean appBean,
            DeviceService deviceService,
            String url, long roomId, boolean isAddFp, Callback callback) throws InterruptedException {
        // 根据房间ID获取门锁信息
        DeviceBaseInfo deviceInfo = deviceService.getLockFromRoom(roomId);
        if (deviceInfo == null) {
            return new ResponseData(ResponseCode.ERROR_DEVICE_NOT_EXIST);
        }
        return rmtOperateByMac(rootId, rootUserService, appBean, url, deviceInfo.getMAC(), deviceInfo.getIdx(), isAddFp, callback);
    }

    public interface Callback {
        void addParams(Map<String, Object> requestParam, String token, String psw, long nextCnt);
        void saveToDB(long createUserId);
    }
}
