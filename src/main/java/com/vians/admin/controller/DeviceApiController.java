package com.vians.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.vians.admin.common.CommConstants;
import com.vians.admin.model.DeviceBaseInfo;
import com.vians.admin.model.RootUserInfo;
import com.vians.admin.model.UnlockLog;
import com.vians.admin.request.*;
import com.vians.admin.response.ResponseCode;
import com.vians.admin.response.ResponseData;
import com.vians.admin.response.bean.*;
import com.vians.admin.service.DeviceService;
import com.vians.admin.service.LogService;
import com.vians.admin.service.RootUserService;
import com.vians.admin.utils.CommUtil;
import com.vians.admin.utils.HttpUtil;
import com.vians.admin.web.AppBean;
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

//    @Autowired
//    public PropUtil propUtil;

//    @Resource
//    public RedisService redisService;

    @Resource
    public LogService logService;

    @Resource
    public DeviceService deviceService;

    @Autowired
    public AppBean appBean;

    @Resource
    public RootUserService rootUserService;

    /**
     * 获取token
     * @return
     */
//    @PostMapping("/GetToken")
//    public ResponseData getToken() {
//
//        Map<String, Object> requestParam = new HashMap<>();
//        requestParam.put("UserName", propUtil.physicUser);
//        JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, CommConstants.C_API_URL + "/api/GetToken");
//        if (respObj != null) {
//            int result = (int) respObj.get("Result");
//            if (result == 0) {
//                TokenResponse tokenResponse = respObj.toJavaObject(TokenResponse.class);
//                String tokenStr = tokenResponse.getToken();
//                redisService.set(RedisKeyConstants.VIANS_TOKEN, tokenStr);
//                System.out.println("writeToken:" + tokenStr);
//                // 保存token值到redis服务器
//                return new ResponseData(ResponseCode.SUCCESS);
//            } else {
//                return HttpUtil.getInstance().doResult(result);
//            }
//        } else {
//            return new ResponseData(ResponseCode.ERROR);
//        }
//    }

    /**
     * 登录
     * @return
     */
//    @PostMapping("/Login")
//    public ResponseData login() {
//
//        Map<String, Object> requestParam = new HashMap<>();
//        requestParam.put("UserName", propUtil.physicUser);
//        String token = redisService.get(RedisKeyConstants.VIANS_TOKEN);
//        if (token == null) {
//            return new ResponseData(ResponseCode.ERROR);
//        }
//        // 根据token和物理psw计算出keyL，用于跟服务器验证
//        String keyL = KeyUtil.getKeyL(token, propUtil.physicPsw);
//        System.out.println("keyL:" + keyL);
//        requestParam.put("KeyL", keyL);
//        JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, CommConstants.C_API_URL + "/api/Login");
//        if (respObj != null) {
//            int result = (int) respObj.get("Result");
//            if (result == 0) {
//                LoginResponse loginResponse = respObj.toJavaObject(LoginResponse.class);
//                redisService.set(RedisKeyConstants.VIANS_USER_ID, loginResponse.getUserID() + "");
//                redisService.set(RedisKeyConstants.VIANS_CNT, loginResponse.getCNT());
//            } else {
//                return HttpUtil.getInstance().doResult(result);
//            }
//        } else {
//            return new ResponseData(ResponseCode.ERROR);
//        }
//        return new ResponseData(ResponseCode.SUCCESS);
//    }

//    @PostMapping("/Logout")
//    public ResponseData logout() {
//        Map<String, Object> requestParam = new HashMap<>();
//        String appKey = HttpUtil.getInstance().getAppKey(redisService, propUtil);
//        String userId = redisService.get(RedisKeyConstants.VIANS_USER_ID);
//        if (appKey == null || userId == null) {
//            return new ResponseData(ResponseCode.ERROR);
//        }
//        requestParam.put("UserID", userId);
//        requestParam.put("Key", appKey);
//        JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, CommConstants.C_API_URL + "/api/Logout");
//        if (respObj != null) {
//            int result = (int) respObj.get("Result");
//            if (result == 0) {
//                LogoutResponse logoutResponse = respObj.toJavaObject(LogoutResponse.class);
//                redisService.set(RedisKeyConstants.VIANS_CNT, logoutResponse.getCNT());
//            } else {
//                return HttpUtil.getInstance().doResult(result);
//            }
//        } else {
//            return new ResponseData(ResponseCode.ERROR);
//        }
//        return new ResponseData(ResponseCode.SUCCESS);
//    }

    /**
     * 远程开锁
     * @param rmtUnlock
     * @return
     */
    @PostMapping("/RmtUnlock")
    public ResponseData rmtUnlock(@Valid @RequestBody RxRmtUnlock rmtUnlock) {
        System.out.println("RmtUnlockReqParam=" + rmtUnlock.toString());
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        return doRmtUnlock(rootId, rmtUnlock);
    }

    private ResponseData doRmtUnlock(long rootId, RxRmtUnlock rmtUnlock) {
        RootUserInfo rootUserInfo = rootUserService.getRootUserById(rootId);
        if (rootUserInfo == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        Map<String, Object> requestParam = HttpUtil.getInstance().getRmtOperateCommParams(
                rootUserInfo, rmtUnlock.getIndex(), rmtUnlock.getMac(), rootUserInfo.getPhone());
        JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, CommConstants.C_API_URL + "/api/RmtUnlock");
        if (respObj != null) {
            int result = (int) respObj.get("Result");
            if (result == 0) {
                RmtUnlockResponse rmtUnlockResponse = respObj.toJavaObject(RmtUnlockResponse.class);
                // 5. 保存cnt到DB
                rootUserInfo.setCnt(Long.parseLong(rmtUnlockResponse.getCNT(), 16));
                rootUserService.updateRootUser(rootUserInfo);
            } else if (result == 1002) {
                return HttpUtil.getInstance().reLogin(rootUserInfo.getPhone(), rootUserInfo.getPassword(), rootUserService, new HttpUtil.ReLoginCallback() {
                    @Override
                    public ResponseData success() {
                        return doRmtUnlock(rootId, rmtUnlock);
                    }

                    @Override
                    public ResponseData failure(int result) {
                        return HttpUtil.getInstance().doResult(result);
                    }
                });
            } else {
                return HttpUtil.getInstance().doResult(result);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR);
        }
        return new ResponseData(ResponseCode.SUCCESS);
    }

    /**
     * 查询开锁日志
     * @param checkLog
     * @return
     */
    @PostMapping("/CheckLog")
    public ResponseData checkUnlockLog(@Valid @RequestBody RxCheckLog checkLog) {
        return checkLog(checkLog, "/api/CheckLog");
    }

    /**
     * 查询报警器日志
     * @param checkLog
     * @return
     */
    @PostMapping("/CheckAlarm")
    public ResponseData checkAlarmLog(@Valid @RequestBody RxCheckLog checkLog) {
        return checkLog(checkLog, "/api/CheckAlarm");
    }

    private ResponseData checkLog(RxCheckLog checkLog, String url) {
        DeviceBaseInfo deviceBaseInfo;
        // 由于MAC必传，如果没有传入房间，则MAC传空字符串查询
        if (checkLog.getRoomId() == 0) {
            deviceBaseInfo = new DeviceBaseInfo();
            deviceBaseInfo.setMAC("");
        } else {
            // 如果房间中没有找到设备，则直接返回空的日志表
            deviceBaseInfo = deviceService.getDeviceByRoomId(checkLog.getRoomId());
            if (deviceBaseInfo == null) {
                ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
                responseData.addData("Log", null);
                responseData.addData("Total", 0);
                return responseData;
            }
        }
        // 1. 获取到远程操作的公共请求参数
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        return doCheckLog(rootId, checkLog, url, deviceBaseInfo.getMAC());
    }

    private ResponseData doCheckLog(long rootId, RxCheckLog checkLog, String url, String mac) {
        RootUserInfo rootUserInfo = rootUserService.getRootUserById(rootId);
        if (rootUserInfo == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        Map<String, Object> requestParam = HttpUtil.getInstance().getRmtOperateCommParams(
                rootUserInfo, checkLog.getIndex(), checkLog.getMac(), checkLog.getUserName());
        if (requestParam != null) {
            // 2. 组装特有请求参数
            requestParam.put("BeginTime", checkLog.getBeginTime());
            requestParam.put("EndTime", checkLog.getEndTime());
            requestParam.put("Page", checkLog.getPage());
            requestParam.put("MAC", mac);
            requestParam.put("PageSize", checkLog.getPageSize());
            // 3. 发送请求
            JSONObject respObj =HttpUtil.getInstance().doRequest(requestParam, CommConstants.C_API_URL + url);
            if (respObj != null) {
                int result = (int) respObj.get("Result");
                if (result == 0) {
                    // 4. 解析出响应数据
                    CheckLogResponse checkLogResponse = respObj.toJavaObject(CheckLogResponse.class);
                    // 5. 保存cnt到DB
                    rootUserInfo.setCnt(Long.parseLong(checkLogResponse.getCNT(), 16));
                    rootUserService.updateRootUser(rootUserInfo);
//                    redisService.set(RedisKeyConstants.VIANS_CNT, checkLogResponse.getCNT());
                    ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
                    if (checkLogResponse.getLog() != null) {
                        checkLogResponse.getLog().sort(
                                (o1, o2) -> Long.compare(Long.parseLong(o2.getTime()), Long.parseLong(o1.getTime())));
                        checkLogResponse.getLog().forEach(log -> {
                            log.setTime(CommUtil.parseTime(Long.parseLong(log.getTime())));
                            UnlockLog logInfo;
                            if (url.contains("CheckLog")) {
                                log.setValue(log.getValue().replace("00000000", ""));
                                logInfo = logService.getUnlockLogByMacAndName(log.getName(), log.getMAC());
                            } else {
                                logInfo = logService.getUnlockLogByMacAndName("", log.getMAC());
                            }
                            if (logInfo != null) {
                                log.setCardId(logInfo.getCardId());
                                log.setRoleName(logInfo.getRoleName());
                                log.setProjectName(logInfo.getProjectName());
                                log.setCommunityName(logInfo.getCommunityName());
                                log.setBuildingName(logInfo.getBuildingName());
                                log.setUnitName(logInfo.getUnitName());
                                log.setFloorName(logInfo.getFloorName());
                                log.setRoomName(logInfo.getRoomName());
                                log.setDeviceName(logInfo.getDeviceName());
                                log.setDeviceModel(logInfo.getDeviceModel());
                            }
                        });
                    }
                    // 6. 返回数据给客户端
                    responseData.addData("Log", checkLogResponse.getLog());
                    responseData.addData("Total", checkLogResponse.getTotal());
                    return responseData;
                } else if (result == 1002) {
                    return HttpUtil.getInstance().reLogin(rootUserInfo.getPhone(), rootUserInfo.getPassword(), rootUserService, new HttpUtil.ReLoginCallback() {
                        @Override
                        public ResponseData success() {
                            return doCheckLog(rootId, checkLog, url, mac);
                        }

                        @Override
                        public ResponseData failure(int result) {
                            return HttpUtil.getInstance().doResult(result);
                        }
                    });
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
        DeviceBaseInfo deviceBaseInfo;
        if (checkBatt.getRoomId() == 0) {
            deviceBaseInfo = new DeviceBaseInfo();
            deviceBaseInfo.setMAC("");
        } else {
            deviceBaseInfo = deviceService.getDeviceByRoomId(checkBatt.getRoomId());
            if (deviceBaseInfo == null) {
                ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
                responseData.addData("Batt", null);
                responseData.addData("Total", 0);
                return responseData;
            }
        }
        // 1. 获取到远程操作的公共请求参数
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        return doCheckBatt(rootId, checkBatt, deviceBaseInfo.getMAC());
    }

    private ResponseData doCheckBatt(long rootId, RxCheckBatt checkBatt, String mac) {
        RootUserInfo rootUserInfo = rootUserService.getRootUserById(rootId);
        if (rootUserInfo == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        String appKey = HttpUtil.getInstance().getAppKey(rootUserInfo);
        // 1. 组装请求参数
        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("UserID", String.valueOf(rootUserInfo.getUserId()));
        requestParam.put("Key", appKey);
        requestParam.put("Feature", checkBatt.getFeature());
        requestParam.put("MAC", mac);
        requestParam.put("Page", checkBatt.getPage());
        requestParam.put("PageSize", checkBatt.getPageSize());
        // 3. 发送请求
        JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, CommConstants.C_API_URL + "/api/CheckBatt");
        if (respObj != null) {
            int result = (int) respObj.get("Result");
            if (result == 0) {
                // 4. 解析出响应数据
                CheckBattResponse checkBattResponse = respObj.toJavaObject(CheckBattResponse.class);
                // 5. 保存cnt到DB
                rootUserInfo.setCnt(Long.parseLong(checkBattResponse.getCNT(), 16));
                rootUserService.updateRootUser(rootUserInfo);
                ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
                if (checkBattResponse.getBatt() != null) {
                    checkBattResponse.getBatt().forEach(battery -> {
                        battery.setTime(CommUtil.parseTime(Long.parseLong(battery.getTime())));
                        UnlockLog logInfo = logService.getUnlockLogByMacAndName(null, battery.getMAC());
                        if (logInfo != null) {
                            battery.setProjectName(logInfo.getProjectName());
                            battery.setCommunityName(logInfo.getCommunityName());
                            battery.setBuildingName(logInfo.getBuildingName());
                            battery.setUnitName(logInfo.getUnitName());
                            battery.setFloorName(logInfo.getFloorName());
                            battery.setRoomName(logInfo.getRoomName());
                            battery.setDeviceModel(logInfo.getDeviceModel());
                        }
                    });
                }
                // 6. 返回数据给客户端
                responseData.addData("Batt", checkBattResponse.getBatt());
                responseData.addData("Total", checkBattResponse.getTotal());
                return responseData;
            } else if (result == 1002) {
                return HttpUtil.getInstance().reLogin(rootUserInfo.getPhone(), rootUserInfo.getPassword(),
                        rootUserService,
                        new HttpUtil.ReLoginCallback() {
                    @Override
                    public ResponseData success() {
                        return doCheckBatt(rootId, checkBatt, mac);
                    }

                    @Override
                    public ResponseData failure(int result) {
                        return HttpUtil.getInstance().doResult(result);
                    }
                });
            } else {
                return HttpUtil.getInstance().doResult(result);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR);
        }
    }

    /**
     * 获取该用户的设备列表
     * @param getDevice
     * @return
     */
    @PostMapping("/GetDevice")
    public ResponseData getDevice(@Valid @RequestBody RxGetDevice getDevice) {
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        return doGetDevice(rootId, getDevice);
    }

    private ResponseData doGetDevice(long rootId, RxGetDevice getDevice) {
        RootUserInfo rootUserInfo = rootUserService.getRootUserById(rootId);
        if (rootUserInfo == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        String appKey = HttpUtil.getInstance().getAppKey(rootUserInfo);
        // 1. 组装请求参数
        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("UserID", rootUserInfo.getUserId()); // 后台分配的用户号
        requestParam.put("Key", appKey); // 应用密钥 AppKey
        requestParam.put("MAC", getDevice.getMac()); // MAC 地址
        requestParam.put("Feature", getDevice.getFeature());
        requestParam.put("Page", getDevice.getPage());
        requestParam.put("PageSize", getDevice.getPageSize());
        // 2. 发送请求
        JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, CommConstants.C_API_URL + "/api/GetDevice");
        if (respObj != null) {
            int result = (int) respObj.get("Result");
            if (result == 0) {
                // 3. 解析出响应数据
                GetDeviceResponse getDeviceResponse = respObj.toJavaObject(GetDeviceResponse.class);
                // 5. 保存cnt到DB
                rootUserInfo.setCnt(Long.parseLong(getDeviceResponse.getCNT(), 16));
                rootUserService.updateRootUser(rootUserInfo);
                ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
                responseData.addData("Dev", getDeviceResponse.getDev());
                responseData.addData("Total", getDeviceResponse.getTotal());
                return responseData;
            } else if (result == 1002) {
                return HttpUtil.getInstance().reLogin(rootUserInfo.getPhone(), rootUserInfo.getPassword(),
                        rootUserService,
                        new HttpUtil.ReLoginCallback() {
                            @Override
                            public ResponseData success() {
                                return doGetDevice(rootId, getDevice);
                            }

                            @Override
                            public ResponseData failure(int result) {
                                return HttpUtil.getInstance().doResult(result);
                            }
                        });
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
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        return doGetLockInfo(rootId, getLockInfo);
    }

    private ResponseData doGetLockInfo(long rootId, RxGetLockInfo getLockInfo) {
        RootUserInfo rootUserInfo = rootUserService.getRootUserById(rootId);
        if (rootUserInfo == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        String appKey = HttpUtil.getInstance().getAppKey(rootUserInfo);
        // 1. 组装请求参数
        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("UserID", rootUserInfo.getUserId()); // 后台分配的用户号
        requestParam.put("Key", appKey); // 应用密钥 AppKey
        requestParam.put("MAC", getLockInfo.getMac()); // MAC 地址
        // 2. 发送请求
        JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, CommConstants.C_API_URL + "/api/GetLockInfo");
        if (respObj != null) {
            int result = (int) respObj.get("Result");
            if (result == 0) {
                // 3. 解析出响应数据
                GetLockInfoResponse getLockInfoResponse = respObj.toJavaObject(GetLockInfoResponse.class);
                // 5. 保存cnt到DB
                rootUserInfo.setCnt(Long.parseLong(getLockInfoResponse.getCNT(), 16));
                rootUserService.updateRootUser(rootUserInfo);
                ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
                responseData.addData("BlockIdx", getLockInfoResponse.getBlockIdx());
                responseData.addData("GwMAC", getLockInfoResponse.getGwMAC());
                responseData.addData("GwUUID", getLockInfoResponse.getGwUUID());
                responseData.addData("LockIdx", getLockInfoResponse.getLockIdx());
                return responseData;
            } else if (result == 1002) {
                return HttpUtil.getInstance().reLogin(rootUserInfo.getPhone(), rootUserInfo.getPassword(),
                        rootUserService,
                        new HttpUtil.ReLoginCallback() {
                            @Override
                            public ResponseData success() {
                                return doGetLockInfo(rootId, getLockInfo);
                            }

                            @Override
                            public ResponseData failure(int result) {
                                return HttpUtil.getInstance().doResult(result);
                            }
                        });
            } else {
                return HttpUtil.getInstance().doResult(result);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR);
        }
    }
}
