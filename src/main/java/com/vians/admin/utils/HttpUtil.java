package com.vians.admin.utils;

import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vians.admin.common.CommConstants;
import com.vians.admin.model.RootUserInfo;
import com.vians.admin.response.ResponseCode;
import com.vians.admin.response.ResponseData;
import com.vians.admin.service.RootUserService;

import java.util.HashMap;
import java.util.Map;

public class HttpUtil {

    private static HttpUtil instance;

    private HttpUtil() {
    }

    public static HttpUtil getInstance() {
        if (instance == null) {
            instance = new HttpUtil();
        }
        return instance;
    }

    public JSONObject doRequest(Map<String, Object> requestParam, String url) {
        System.out.println("reqUrl->" + url);
        System.out.println("reqStr->" + JSON.toJSONString(requestParam));
        String respStr;
        try {
            respStr = HttpRequest
                    .post(url)
                    .body(JSON.toJSONString(requestParam))
                    .header("Content-Type","application/json")
                    .execute()
                    .body();
            System.out.println("respStr->" + respStr);
            // 成功时Result为0
            return JSONObject.parseObject(respStr);
        } catch (HttpException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseCode getResponseCode(int result) {
        switch (result) {
            case 1000:
                return ResponseCode.ERROR_API_INVALID_GATEWAY;
            case 1001:
                return ResponseCode.ERROR_API_INVALID_UUID;
            case 1002:
                return ResponseCode.ERROR_API_INVALID_USER;
            case 1003:
                return ResponseCode.ERROR_API_INVALID_KEY;
            case 1004:
                return ResponseCode.ERROR_API_BASE64_ERROR;
            case 1005:
                return ResponseCode.ERROR_API_SEND_TOPIC_FAIL;
            case 1006:
                return ResponseCode.ERROR_API_CREATE_FILE_ERROR;
            case 1007:
                return ResponseCode.ERROR_API_JSON_FORMAT_ERROR;
            case 1008:
                return ResponseCode.ERROR_API_ILLEGAL_DEVICE;
            case 1009:
                return ResponseCode.ERROR_API_FILE_TRANSFER_FAIL;
            case 1010:
                return ResponseCode.ERROR_API_NO_OPERATE_PERMISSION;
            case 1011:
                return ResponseCode.ERROR_API_COMMUNICATING;
            default:
                return ResponseCode.ERROR;
        }
    }


    public ResponseData doResult(int result) {
        return new ResponseData(getResponseCode(result));
    }

    public Map<String, Object> getRmtOperateCommParams(RootUserInfo rootUserInfo, String index, String mac, String userName) {

        String token = rootUserInfo.getToken();
        String userId = String.valueOf(rootUserInfo.getUserId());
        Map<String, Object> requestParam = new HashMap<>();
        // cnt加1
        long nextCnt = rootUserInfo.getCnt() + 1;
        String appKey = KeyUtil.getAppKey(token, rootUserInfo.getPassword(), nextCnt);
//        String userNameEncrypt = KeyUtil.encode2HashXOR(userName.getBytes(), token, propUtil.physicPsw, nextCnt);
        requestParam.put("UserID", userId); // 后台分配的用户号
        requestParam.put("Key", appKey); // 应用密钥 AppKey
        requestParam.put("Index", index); // 锁序号
        requestParam.put("MAC", mac); // 锁具 MAC 地址
        requestParam.put("UserName", userName); // 加密后的用户名?哪些情况要加密？哪些情况不需要加密？
        return requestParam;
    }

    public String getAppKey(RootUserInfo rootUserInfo) {

        String token = rootUserInfo.getToken();
        long cnt = rootUserInfo.getCnt();
        // cnt加1
        long nextCnt = cnt + 1;
        return KeyUtil.getAppKey(token, rootUserInfo.getPassword(), nextCnt);
    }

    /**
     * 当登录过期，即result为1002时，重新登录
     * @param phone
     * @param password
     * @param rootUserService
     * @param callback
     */
    public ResponseData reLogin(String phone, String password, RootUserService rootUserService, ReLoginCallback callback) {
        String tokenStr;
        // 获取Token
        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("UserName", phone);
        JSONObject getTokenRsp = HttpUtil.getInstance().doRequest(requestParam, CommConstants.C_API_URL + "/api/GetToken");
        if (getTokenRsp != null) {
            int tokenResult = (int) getTokenRsp.get("Result");
            if (tokenResult == 0) {
                tokenStr = getTokenRsp.get("Token").toString();
                // 登录
                String keyL = KeyUtil.getKeyL(tokenStr, password);
                requestParam.put("KeyL", keyL);
                JSONObject loginRsp = HttpUtil.getInstance().doRequest(requestParam, CommConstants.C_API_URL + "/api/Login");
                if (loginRsp != null) {
                    int loginResult = (int) loginRsp.get("Result");
                    if (loginResult == 0) {
                        long userId = Long.parseLong(loginRsp.get("UserID").toString());
                        String cnt = loginRsp.get("CNT").toString();
                        // 保存到数据库
                        RootUserInfo rootUserInfo = new RootUserInfo();
                        rootUserInfo.setUserId(userId);
                        rootUserInfo.setCnt(Long.parseLong(cnt, 16));
                        rootUserInfo.setPhone(phone);
                        rootUserInfo.setToken(tokenStr);
                        rootUserInfo.setPassword(password);
                        rootUserInfo.setToken(tokenStr);
                        rootUserService.updateRootUser(rootUserInfo);
                        return callback.success();
                    } else {
                        return callback.failure(loginResult);
                    }
                }
            } else {
                return callback.failure(tokenResult);
            }
        }
        return ResponseData.error();
    }

    public interface ReLoginCallback {
        ResponseData success();
        ResponseData failure(int result);
    }
}
