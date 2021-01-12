package com.vians.admin.utils;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vians.admin.common.RedisKeyConstants;
import com.vians.admin.response.ResponseCode;
import com.vians.admin.response.ResponseData;
import com.vians.admin.service.RedisService;

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
        String respStr;
        try {
            respStr = HttpRequest
                    .post(url)
                    .body(JSON.toJSONString(requestParam))
                    .header("Content-Type","application/json")
                    .execute()
                    .body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("reqUrl->" + url);
        System.out.println("reqStr->" + JSON.toJSONString(requestParam));
        System.out.println("respStr->" + respStr);
        // 如果成功，返回json
//        Map<String, Object> respMap = JSONObject.parseObject(respStr);
//        int result = (int) respMap.get("Result");
        // 成功时Result为0
        return JSONObject.parseObject(respStr);



//        if (result == 0) {
//            try {
//                respObj = JSONObject.parseObject(respStr);
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println(e.getMessage());
//            }
//        } else {
//            return null;
//        }
//        return respObj;
    }

    /**
     * 远程操作公共参数封装
     * @param propUtil
     * @param redisService
     * @param index
     * @param mac
     * @return
     */
    public Map<String, Object> getRmtOperateCommParams(PropUtil propUtil, RedisService redisService, String index, String mac) {

        String token = redisService.get(RedisKeyConstants.VIANS_TOKEN);
        String cnt = redisService.get(RedisKeyConstants.VIANS_CNT);
        String userId = redisService.get(RedisKeyConstants.VIANS_USER_ID);
        if (token == null || cnt == null || userId == null) {
            return null;
        }
        Map<String, Object> requestParam = new HashMap<>();
        // cnt加1
        int nextCnt = Integer.parseInt(cnt, 16) + 1;
        String appKey = KeyUtil.getAppKey(token, propUtil.physicPsw, nextCnt);
        String userName = KeyUtil.encode2HashXOR(propUtil.physicUser, token, propUtil.physicPsw, nextCnt);
        requestParam.put("UserID", userId); // 后台分配的用户号
        requestParam.put("Key", appKey); // 应用密钥 AppKey
        requestParam.put("Index", index); // 锁序号
        requestParam.put("MAC", mac); // 锁具 MAC 地址
        requestParam.put("UserName", userName); // 加密后的用户名
        return requestParam;
    }

    public ResponseData doResult(int result) {
        switch (result) {
            case 1000:
                return new com.vians.admin.response.ResponseData(com.vians.admin.response.ResponseCode.ERROR_API_INVALID_GATEWAY);
            case 1001:
                return new com.vians.admin.response.ResponseData(com.vians.admin.response.ResponseCode.ERROR_API_INVALID_UUID);
            case 1002:
                return new com.vians.admin.response.ResponseData(com.vians.admin.response.ResponseCode.ERROR_API_INVALID_USER);
            case 1003:
                return new com.vians.admin.response.ResponseData(com.vians.admin.response.ResponseCode.ERROR_API_INVALID_KEY);
            case 1004:
                return new com.vians.admin.response.ResponseData(com.vians.admin.response.ResponseCode.ERROR_API_BASE64_ERROR);
            case 1005:
                return new com.vians.admin.response.ResponseData(com.vians.admin.response.ResponseCode.ERROR_API_SEND_TOPIC_FAIL);
            case 1006:
                return new com.vians.admin.response.ResponseData(com.vians.admin.response.ResponseCode.ERROR_API_CREATE_FILE_ERROR);
            case 1007:
                return new com.vians.admin.response.ResponseData(com.vians.admin.response.ResponseCode.ERROR_API_JSON_FORMAT_ERROR);
            case 1008:
                return new com.vians.admin.response.ResponseData(com.vians.admin.response.ResponseCode.ERROR_API_ILLEGAL_DEVICE);
            case 1009:
                return new com.vians.admin.response.ResponseData(com.vians.admin.response.ResponseCode.ERROR_API_FILE_TRANSFER_FAIL);
            case 1010:
                return new com.vians.admin.response.ResponseData(com.vians.admin.response.ResponseCode.ERROR_API_NO_OPERATE_PERMISSION);
            case 1011:
                return new com.vians.admin.response.ResponseData(com.vians.admin.response.ResponseCode.ERROR_API_COMMUNICATING);
            default:
                return new ResponseData(ResponseCode.ERROR);
        }
    }
}
