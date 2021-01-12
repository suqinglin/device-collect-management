package com.vians.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.vians.admin.common.RedisKeyConstants;
import com.vians.admin.request.*;
import com.vians.admin.response.ResponseCode;
import com.vians.admin.response.ResponseData;
import com.vians.admin.response.bean.*;
import com.vians.admin.service.RedisService;
import com.vians.admin.utils.HttpUtil;
import com.vians.admin.utils.KeyUtil;
import com.vians.admin.utils.PropUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/vians/DeviceApi")
public class DeviceApiController {

    @Autowired
    public PropUtil propUtil;

    @Resource
    public RedisService redisService;

    /**
     * 获取token
     * @return
     */
    @PostMapping("/GetToken")
    public ResponseData getToken() {

        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("UserName", propUtil.physicUser);
        JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, propUtil.url + "/api/GetToken");
        if (respObj != null) {
            int result = (int) respObj.get("Result");
            if (result == 0) {
                TokenResponse tokenResponse = respObj.toJavaObject(TokenResponse.class);
                String tokenStr = tokenResponse.getToken();
                redisService.set(RedisKeyConstants.VIANS_TOKEN, tokenStr);
                System.out.println("writeToken:" + tokenStr);
                // 保存token值到redis服务器
                return new ResponseData(ResponseCode.SUCCESS);
            } else {
                return HttpUtil.getInstance().doResult(result);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR);
        }
    }

    /**
     * 登录
     * @return
     */
    @PostMapping("/Login")
    public ResponseData login() {

        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("UserName", propUtil.physicUser);
        String token = redisService.get(RedisKeyConstants.VIANS_TOKEN);
        if (token == null) {
            return new ResponseData(ResponseCode.ERROR);
        }
        // 根据token和物理psw计算出keyL，用于跟服务器验证
        String keyL = KeyUtil.getKeyL(token, propUtil.physicPsw);
        System.out.println("keyL:" + keyL);
        requestParam.put("KeyL", keyL);
        JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, propUtil.url + "/api/Login");
        if (respObj != null) {
            int result = (int) respObj.get("Result");
            if (result == 0) {
                LoginResponse loginResponse = respObj.toJavaObject(LoginResponse.class);
                redisService.set(RedisKeyConstants.VIANS_USER_ID, loginResponse.getUserID() + "");
                redisService.set(RedisKeyConstants.VIANS_CNT, loginResponse.getCNT());
            } else {
                return HttpUtil.getInstance().doResult(result);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR);
        }
        return new ResponseData(ResponseCode.SUCCESS);
    }

    @PostMapping("/Logout")
    public ResponseData logout() {
        Map<String, Object> requestParam = new HashMap<>();
        String appKey = getAppKey(redisService, propUtil);
        String userId = redisService.get(RedisKeyConstants.VIANS_USER_ID);
        if (appKey == null || userId == null) {
            return new ResponseData(ResponseCode.ERROR);
        }
        requestParam.put("UserID", userId);
        requestParam.put("Key", appKey);
        JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, propUtil.url + "/api/Logout");
        if (respObj != null) {
            int result = (int) respObj.get("Result");
            if (result == 0) {
                LogoutResponse logoutResponse = respObj.toJavaObject(LogoutResponse.class);
                redisService.set(RedisKeyConstants.VIANS_CNT, logoutResponse.getCNT());
            } else {
                return HttpUtil.getInstance().doResult(result);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR);
        }
        return new ResponseData(ResponseCode.SUCCESS);
    }

    /**
     * 远程开锁
     * @param rmtUnlock
     * @return
     */
    @PostMapping("/RmtUnlock")
    public ResponseData rmtUnlock(@Valid @RequestBody RxRmtUnlock rmtUnlock) {
        System.out.println("RmtUnlockReqParam=" + rmtUnlock.toString());
        Map<String, Object> requestParam = getRmtOperateCommParams(propUtil, redisService, rmtUnlock.getIndex(), rmtUnlock.getMac());
        JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, propUtil.url + "/api/RmtUnlock");
        if (respObj != null) {
            int result = (int) respObj.get("Result");
            if (result == 0) {
                RmtUnlockResponse rmtUnlockResponse = respObj.toJavaObject(RmtUnlockResponse.class);
                redisService.set(RedisKeyConstants.VIANS_CNT, rmtUnlockResponse.getCNT());
            } else {
                return HttpUtil.getInstance().doResult(result);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR);
        }
        return new ResponseData(ResponseCode.SUCCESS);
    }

    /**
     * 远程下发密码
     * @param rmtAddPsw
     * @return
     */
    @PostMapping("/RmtAddPsw")
    public ResponseData rmtAddPsw(@Valid @RequestBody RxRmtAddPsw rmtAddPsw) {

        Map<String, Object> requestParam = getRmtOperateCommParams(propUtil, redisService, rmtAddPsw.getIndex(), rmtAddPsw.getMac());
        String crc16;
        if (requestParam != null) {
            requestParam.put("Psw", rmtAddPsw.getPsw());
            requestParam.put("PswIdx", rmtAddPsw.getPswIdx());
            requestParam.put("PswType", rmtAddPsw.getPswType());
            requestParam.put("BeginTime", rmtAddPsw.getBeginTime());
            requestParam.put("EndTime", rmtAddPsw.getEndTime());
            JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, propUtil.url + "/api/RmtAddPsw");
            if (respObj != null) {
                int result = (int) respObj.get("Result");
                if (result == 0) {
                    RmtAddPswResponse rmtAddPswResponse = respObj.toJavaObject(RmtAddPswResponse.class);
                    crc16 = rmtAddPswResponse.getCRC16();
                    redisService.set(RedisKeyConstants.VIANS_CNT, rmtAddPswResponse.getCNT());
                } else {
                    return HttpUtil.getInstance().doResult(result);
                }
            } else {
                return new ResponseData(ResponseCode.ERROR);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR);
        }
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("CRC16", crc16);
        return responseData;
    }

    /**
     * 远程删除密码
     * @param rmtDeletePsw
     * @return
     */
    @PostMapping("/RmtDeletePsw")
    public ResponseData rmtDeletePsw(@Valid @RequestBody RxRmtDeletePsw rmtDeletePsw) {
        // 1. 获取到远程操作的公共请求参数
        Map<String, Object> requestParam = getRmtOperateCommParams(propUtil, redisService, rmtDeletePsw.getIndex(), rmtDeletePsw.getMac());
        String crc16;
        if (requestParam != null) {
            // 2. 组装特有请求参数
            requestParam.put("Psw", rmtDeletePsw.getPsw());
            requestParam.put("PswIdx", rmtDeletePsw.getPswIdx());
            // 3. 发送请求
            JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, propUtil.url + "/api/RmtDeletePsw");
            if (respObj != null) {
                int result = (int) respObj.get("Result");
                if (result == 0) {
                    // 4. 解析出响应数据
                    RmtDeletePswResponse rmtDeletePswResponse = respObj.toJavaObject(RmtDeletePswResponse.class);
                    crc16 = rmtDeletePswResponse.getCRC16();
                    // 5. 保存cnt到redis
                    redisService.set(RedisKeyConstants.VIANS_CNT, rmtDeletePswResponse.getCNT());
                } else {
                    return HttpUtil.getInstance().doResult(result);
                }
            } else {
                return new ResponseData(ResponseCode.ERROR);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR);
        }
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        // 6. 返回crc16给客户端
        responseData.addData("CRC16", crc16);
        return responseData;
    }

    /**
     * 远程下发卡片
     * @param rmtAddCard
     * @return
     */
    @PostMapping("/RmtAddCard")
    public ResponseData rmtAddCard(@Valid @RequestBody RxRmtAddCard rmtAddCard) {
        // 1. 获取到远程操作的公共请求参数
        Map<String, Object> requestParam = getRmtOperateCommParams(propUtil, redisService, rmtAddCard.getIndex(), rmtAddCard.getMac());
        String crc16;
        if (requestParam != null) {
            // 2. 组装特有请求参数
            requestParam.put("CardNum", rmtAddCard.getCardNum());
            requestParam.put("CardIdx", rmtAddCard.getCardIdx());
            requestParam.put("CardType", rmtAddCard.getCardType());
            requestParam.put("BeginTime", rmtAddCard.getBeginTime());
            requestParam.put("EndTime", rmtAddCard.getEndTime());
            // 3. 发送请求
            JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, propUtil.url + "/api/RmtAddCard");
            if (respObj != null) {
                int result = (int) respObj.get("Result");
                if (result == 0) {
                    // 4. 解析出响应数据
                    RmtAddCardResponse rmtAddCardResponse = respObj.toJavaObject(RmtAddCardResponse.class);
                    crc16 = rmtAddCardResponse.getCRC16();
                    // 5. 保存cnt到redis
                    redisService.set(RedisKeyConstants.VIANS_CNT, rmtAddCardResponse.getCNT());
                } else {
                    return HttpUtil.getInstance().doResult(result);
                }
            } else {
                return new ResponseData(ResponseCode.ERROR);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR);
        }
        // 6. 成功，返回crc16给客户端
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("CRC16", crc16);
        return responseData;
    }

    /**
     * 远程删除卡片
     * @param rmtDeleteCard
     * @return
     */
    @PostMapping("/RmtDeleteCard")
    public ResponseData rmtDeleteCard(@Valid @RequestBody RxRmtDeleteCard rmtDeleteCard) {
        // 1. 获取到远程操作的公共请求参数
        Map<String, Object> requestParam = getRmtOperateCommParams(propUtil, redisService, rmtDeleteCard.getIndex(), rmtDeleteCard.getMac());
        String crc16;
        if (requestParam != null) {
            // 2. 组装特有请求参数
            requestParam.put("CardNum", rmtDeleteCard.getCardNum());
            requestParam.put("CardIdx", rmtDeleteCard.getCardIdx());
            // 3. 发送请求
            JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, propUtil.url + "/api/RmtDeleteCard");
            if (respObj != null) {
                int result = (int) respObj.get("Result");
                if (result == 0) {
                    // 4. 解析出响应数据
                    RmtDeleteCardResponse rmtDeleteCardResponse = respObj.toJavaObject(RmtDeleteCardResponse.class);
                    crc16 = rmtDeleteCardResponse.getCRC16();
                    // 5. 保存cnt到redis
                    redisService.set(RedisKeyConstants.VIANS_CNT, rmtDeleteCardResponse.getCNT());
                } else {
                    return HttpUtil.getInstance().doResult(result);
                }
            } else {
                return new ResponseData(ResponseCode.ERROR);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR);
        }
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        // 6. 返回crc16给客户端
        responseData.addData("CRC16", crc16);
        return responseData;
    }

    /**
     * 远程下发指纹模板
     * @param rmtAddFp
     * @return
     */
    @PostMapping("/RmtAddFp")
    public ResponseData rmtAddFp(@Valid @RequestBody RxRmtAddFp rmtAddFp) {
        // 1. 获取到远程操作的公共请求参数
        Map<String, Object> requestParam = getRmtOperateCommParams(propUtil, redisService, rmtAddFp.getIndex(), rmtAddFp.getMac());
        String crc16;
        if (requestParam != null) {
            // 2. 组装特有请求参数
            requestParam.put("FpIdx", rmtAddFp.getFpIdx());
            requestParam.put("FpType", rmtAddFp.getFpType());
            requestParam.put("FpFmt", rmtAddFp.getFpFmt());
            requestParam.put("FpTemp", rmtAddFp.getFpTemp());
            requestParam.put("BeginTime", rmtAddFp.getBeginTime());
            requestParam.put("EndTime", rmtAddFp.getEndTime());
            // 3. 发送请求
            JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, propUtil.url + "/api/RmtAddFp");
            if (respObj != null) {
                int result = (int) respObj.get("Result");
                if (result == 0) {
                    // 4. 解析出响应数据
                    RmtAddFpResponse rmtAddFpResponse = respObj.toJavaObject(RmtAddFpResponse.class);
                    crc16 = rmtAddFpResponse.getCRC16();
                    // 5. 保存cnt到redis
                    redisService.set(RedisKeyConstants.VIANS_CNT, rmtAddFpResponse.getCNT());
                } else {
                    return HttpUtil.getInstance().doResult(result);
                }
            } else {
                return new ResponseData(ResponseCode.ERROR);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR);
        }
        // 6. 成功，返回crc16给客户端
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("CRC16", crc16);
        return responseData;
    }

    /**
     * 远程删除指纹
     * @param rmtDeleteFp
     * @return
     */
    @PostMapping("/RmtDeleteFp")
    public ResponseData rmtDeleteFp(@Valid @RequestBody RxRmtDeleteFp rmtDeleteFp) {
        // 1. 获取到远程操作的公共请求参数
        Map<String, Object> requestParam = getRmtOperateCommParams(propUtil, redisService, rmtDeleteFp.getIndex(), rmtDeleteFp.getMac());
        String crc16;
        if (requestParam != null) {
            // 2. 组装特有请求参数
            requestParam.put("FpIdx", rmtDeleteFp.getFpIdx());
            // 3. 发送请求
            JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, propUtil.url + "/api/RmtDeleteFp");
            if (respObj != null) {
                int result = (int) respObj.get("Result");
                if (result == 0) {
                    // 4. 解析出响应数据
                    RmtDeleteFpResponse rmtDeleteFpResponse = respObj.toJavaObject(RmtDeleteFpResponse.class);
                    crc16 = rmtDeleteFpResponse.getCRC16();
                    // 5. 保存cnt到redis
                    redisService.set(RedisKeyConstants.VIANS_CNT, rmtDeleteFpResponse.getCNT());
                } else {
                    return HttpUtil.getInstance().doResult(result);
                }
            } else {
                return new ResponseData(ResponseCode.ERROR);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR);
        }
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        // 6. 返回crc16给客户端
        responseData.addData("CRC16", crc16);
        return responseData;
    }

    /**
     * 远程下发人脸模板
     * @param rmtAddFace
     * @return
     */
    @PostMapping("/RmtAddFace")
    public ResponseData rmtAddFace(@Valid @RequestBody RxRmtAddFace rmtAddFace) {
        // 1. 获取到远程操作的公共请求参数
        Map<String, Object> requestParam = getRmtOperateCommParams(propUtil, redisService, rmtAddFace.getIndex(), rmtAddFace.getMac());
        String crc16;
        if (requestParam != null) {
            // 2. 组装特有请求参数
            requestParam.put("FaceIdx", rmtAddFace.getFaceIdx());
            requestParam.put("FaceType", rmtAddFace.getFaceType());
            requestParam.put("FaceFmt", rmtAddFace.getFaceFmt());
            requestParam.put("FaceTemp", rmtAddFace.getFaceTemp());
            requestParam.put("BeginTime", rmtAddFace.getBeginTime());
            requestParam.put("EndTime", rmtAddFace.getEndTime());
            // 3. 发送请求
            JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, propUtil.url + "/api/RmtAddFace");
            if (respObj != null) {
                int result = (int) respObj.get("Result");
                if (result == 0) {
                    // 4. 解析出响应数据
                    RmtAddFaceResponse rmtAddFaceResponse = respObj.toJavaObject(RmtAddFaceResponse.class);
                    crc16 = rmtAddFaceResponse.getCRC16();
                    // 5. 保存cnt到redis
                    redisService.set(RedisKeyConstants.VIANS_CNT, rmtAddFaceResponse.getCNT());
                } else {
                    return HttpUtil.getInstance().doResult(result);
                }
            } else {
                return new ResponseData(ResponseCode.ERROR);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR);
        }
        // 6. 成功，返回crc16给客户端
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("CRC16", crc16);
        return responseData;
    }

    /**
     * 远程删除人脸
     * @param rmtDeleteFace
     * @return
     */
    @PostMapping("/RmtDeleteFace")
    public ResponseData rmtDeleteFace(@Valid @RequestBody RxRmtDeleteFace rmtDeleteFace) {
        // 1. 获取到远程操作的公共请求参数
        Map<String, Object> requestParam = getRmtOperateCommParams(propUtil, redisService, rmtDeleteFace.getIndex(), rmtDeleteFace.getMac());
        String crc16;
        if (requestParam != null) {
            // 2. 组装特有请求参数
            requestParam.put("FaceIdx", rmtDeleteFace.getFaceIdx());
            // 3. 发送请求
            JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, propUtil.url + "/api/RmtDeleteFace");
            if (respObj != null) {
                int result = (int) respObj.get("Result");
                if (result == 0) {
                    // 4. 解析出响应数据
                    RmtDeleteFaceResponse rmtDeleteFaceResponse = respObj.toJavaObject(RmtDeleteFaceResponse.class);
                    crc16 = rmtDeleteFaceResponse.getCRC16();
                    // 5. 保存cnt到redis
                    redisService.set(RedisKeyConstants.VIANS_CNT, rmtDeleteFaceResponse.getCNT());
                } else {
                    return HttpUtil.getInstance().doResult(result);
                }
            } else {
                return new ResponseData(ResponseCode.ERROR);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR);
        }
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        // 6. 返回crc16给客户端
        responseData.addData("CRC16", crc16);
        return responseData;
    }

    /**
     * 查询开锁日志
     * @param checkLog
     * @return
     */
    @PostMapping("/CheckLog")
    public ResponseData checkLog(@Valid @RequestBody RxCheckLog checkLog) {
        // 1. 获取到远程操作的公共请求参数
        Map<String, Object> requestParam = getRmtOperateCommParams(propUtil, redisService, checkLog.getIndex(), checkLog.getMac());
        if (requestParam != null) {
            // 2. 组装特有请求参数
            requestParam.put("BeginTime", checkLog.getBeginTime());
            requestParam.put("EndTime", checkLog.getEndTime());
            requestParam.put("Page", checkLog.getPage());
            requestParam.put("PageSize", checkLog.getPageSize());
            // 3. 发送请求
            JSONObject respObj =HttpUtil.getInstance().doRequest(requestParam, propUtil.url + "/api/CheckLog");
            if (respObj != null) {
                int result = (int) respObj.get("Result");
                if (result == 0) {
                    // 4. 解析出响应数据
                    CheckLogResponse checkLogResponse = respObj.toJavaObject(CheckLogResponse.class);
                    // 5. 保存cnt到redis
                    redisService.set(RedisKeyConstants.VIANS_CNT, checkLogResponse.getCNT());
                    ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
                    // 6. 返回数据给客户端
                    responseData.addData("Log", checkLogResponse.getLog());
                    responseData.addData("Total", checkLogResponse.getTotal());
                    return responseData;
                } else {
                    return HttpUtil.getInstance().doResult(result);
                }
            } else {
                return new ResponseData(ResponseCode.ERROR);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR);
        }
    }

    /**
     * 查询设备电量
     * @param checkBatt
     * @return
     */
    @PostMapping("/CheckBatt")
    public ResponseData checkBatt(@Valid @RequestBody RxCheckBatt checkBatt) {
        String appKey = getAppKey(redisService, propUtil);
        String userId = redisService.get(RedisKeyConstants.VIANS_USER_ID);
        if (appKey == null || userId == null) {
            return new ResponseData(ResponseCode.ERROR);
        }
        // 1. 组装请求参数
        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("UserID", userId);
        requestParam.put("Key", appKey);
        requestParam.put("Feature", checkBatt.getFeature());
        requestParam.put("MAC", checkBatt.getMac());
        requestParam.put("Page", checkBatt.getPage());
        requestParam.put("PageSize", checkBatt.getPageSize());
        // 3. 发送请求
        JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, propUtil.url + "/api/CheckBatt");
        if (respObj != null) {
            int result = (int) respObj.get("Result");
            if (result == 0) {
                // 4. 解析出响应数据
                CheckBattResponse checkBattResponse = respObj.toJavaObject(CheckBattResponse.class);
                // 5. 保存cnt到redis
                redisService.set(RedisKeyConstants.VIANS_CNT, checkBattResponse.getCNT());
                ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
                // 6. 返回数据给客户端
                responseData.addData("Batt", checkBattResponse.getBatt());
                responseData.addData("Total", checkBattResponse.getTotal());
                return responseData;
            } else {
                return HttpUtil.getInstance().doResult(result);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR);
        }
    }

    /**
     * 查询报警器日志
     * @param checkLog
     * @return
     */
    @PostMapping("/CheckAlarm")
    public ResponseData checkAlarm(@Valid @RequestBody RxCheckLog checkLog) {
        String appKey = getAppKey(redisService, propUtil);
        String userId = redisService.get(RedisKeyConstants.VIANS_USER_ID);
        if (appKey == null || userId == null) {
            return new ResponseData(ResponseCode.ERROR);
        }
        // 1. 组装请求参数
        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("UserID", userId); // 后台分配的用户号
        requestParam.put("Key", appKey); // 应用密钥 AppKey
        requestParam.put("Index", checkLog.getIndex()); // 锁序号
        requestParam.put("MAC", checkLog.getMac()); // 锁具 MAC 地址
        requestParam.put("BeginTime", checkLog.getBeginTime());
        requestParam.put("EndTime", checkLog.getEndTime());
        requestParam.put("Page", checkLog.getPage());
        requestParam.put("PageSize", checkLog.getPageSize());
        // 3. 发送请求
        JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, propUtil.url + "/api/CheckAlarm");
        if (respObj != null) {
            int result = (int) respObj.get("Result");
            if (result == 0) {
                // 4. 解析出响应数据
                CheckAlarmResponse checkAlarmResponse = respObj.toJavaObject(CheckAlarmResponse.class);
                // 5. 保存cnt到redis
                redisService.set(RedisKeyConstants.VIANS_CNT, checkAlarmResponse.getCNT());
                ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
                // 6. 返回数据给客户端
                responseData.addData("Log", checkAlarmResponse.getLog());
                responseData.addData("Total", checkAlarmResponse.getTotal());
                return responseData;
            } else {
                return HttpUtil.getInstance().doResult(result);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR);
        }
    }

    /**
     * 查询文件是否下发成功
     * @param checkFileAck
     * @return
     */
    @PostMapping("/CheckFileAck")
    public ResponseData checkFileAck(@Valid @RequestBody RxCheckFileAck checkFileAck) {

        String appKey = getAppKey(redisService, propUtil);
        String userId = redisService.get(RedisKeyConstants.VIANS_USER_ID);
        if (appKey == null || userId == null) {
            return new ResponseData(ResponseCode.ERROR);
        }
        // 1. 组装请求参数
        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("UserID", userId); // 后台分配的用户号
        requestParam.put("Key", appKey); // 应用密钥 AppKey
        requestParam.put("MAC", checkFileAck.getMac()); // 锁具 MAC 地址
        requestParam.put("CRC16", checkFileAck.getCrc16());
        // 3. 发送请求
        JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, propUtil.url + "/api/CheckFileAck");
        if (respObj != null) {
            int result = (int) respObj.get("Result");
            if (result == 0) {
                // 4. 解析出响应数据
                CheckFileAckResponse checkFileAckResponse = respObj.toJavaObject(CheckFileAckResponse.class);
                redisService.set(RedisKeyConstants.VIANS_CNT, checkFileAckResponse.getCNT());
            } else {
                return HttpUtil.getInstance().doResult(result);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR);
        }
        return new ResponseData(ResponseCode.SUCCESS);
    }

    /**
     * 获取该用户的设备列表
     * @param getDevice
     * @return
     */
    @PostMapping("/GetDevice")
    public ResponseData getDevice(@Valid @RequestBody RxGetDevice getDevice) {
        System.out.println("RmtUnlockReqParam=" + getDevice.toString());
        String appKey = getAppKey(redisService, propUtil);
        String userId = redisService.get(RedisKeyConstants.VIANS_USER_ID);
        if (appKey == null || userId == null) {
            return new ResponseData(ResponseCode.ERROR);
        }
        // 1. 组装请求参数
        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("UserID", userId); // 后台分配的用户号
        requestParam.put("Key", appKey); // 应用密钥 AppKey
        requestParam.put("MAC", getDevice.getMac()); // MAC 地址
        requestParam.put("Feature", getDevice.getFeature());
        requestParam.put("Page", getDevice.getPage());
        requestParam.put("PageSize", getDevice.getPageSize());
        // 2. 发送请求
        JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, propUtil.url + "/api/GetDevice");
        if (respObj != null) {
            int result = (int) respObj.get("Result");
            if (result == 0) {
                // 3. 解析出响应数据
                GetDeviceResponse getDeviceResponse = respObj.toJavaObject(GetDeviceResponse.class);
                redisService.set(RedisKeyConstants.VIANS_CNT, getDeviceResponse.getCNT());
                ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
                responseData.addData("Dev", getDeviceResponse.getDev());
                responseData.addData("Total", getDeviceResponse.getTotal());
                return responseData;
            } else {
                return HttpUtil.getInstance().doResult(result);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR);
        }
    }

    /**
     * 获取锁具信息
     * @param getLockInfo
     * @return
     */
    @PostMapping("/GetLockInfo")
    public ResponseData getLockInfo(@Valid @RequestBody RxGetLockInfo getLockInfo) {
        String appKey = getAppKey(redisService, propUtil);
        String userId = redisService.get(RedisKeyConstants.VIANS_USER_ID);
        if (appKey == null || userId == null) {
            return new ResponseData(ResponseCode.ERROR);
        }
        // 1. 组装请求参数
        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("UserID", userId); // 后台分配的用户号
        requestParam.put("Key", appKey); // 应用密钥 AppKey
        requestParam.put("MAC", getLockInfo.getMac()); // MAC 地址
        // 2. 发送请求
        JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, propUtil.url + "/api/GetLockInfo");
        if (respObj != null) {
            int result = (int) respObj.get("Result");
            if (result == 0) {
                // 3. 解析出响应数据
                GetLockInfoResponse getLockInfoResponse = respObj.toJavaObject(GetLockInfoResponse.class);
                redisService.set(RedisKeyConstants.VIANS_CNT, getLockInfoResponse.getCNT());
                ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
                responseData.addData("BlockIdx", getLockInfoResponse.getBlockIdx());
                responseData.addData("GwMAC", getLockInfoResponse.getGwMAC());
                responseData.addData("GwUUID", getLockInfoResponse.getGwUUID());
                responseData.addData("LockIdx", getLockInfoResponse.getLockIdx());
                return responseData;
            } else {
                return HttpUtil.getInstance().doResult(result);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR);
        }
    }

    /**
     * 远程操作公共参数封装
     * @param propUtil
     * @param redisService
     * @param index
     * @param mac
     * @return
     */
    private Map<String, Object> getRmtOperateCommParams(PropUtil propUtil, RedisService redisService, String index, String mac) {

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

    private String getAppKey(RedisService redisService, PropUtil propUtil) {

        String token = redisService.get(RedisKeyConstants.VIANS_TOKEN);
        String cnt = redisService.get(RedisKeyConstants.VIANS_CNT);
        if (token == null || cnt == null) {
            return null;
        }
        // cnt加1
        int nextCnt = Integer.parseInt(cnt, 16) + 1;
        return KeyUtil.getAppKey(token, propUtil.physicPsw, nextCnt);
    }
}
