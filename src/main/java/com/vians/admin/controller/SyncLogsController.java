package com.vians.admin.controller;



import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.vians.admin.common.CommConstants;
import com.vians.admin.model.RootUserInfo;
import com.vians.admin.request.RxSychLogs;
import com.vians.admin.response.ResponseCode;
import com.vians.admin.response.ResponseData;
import com.vians.admin.response.bean.CheckLogResponse;
import com.vians.admin.service.LogService;
import com.vians.admin.service.ProjectService;
import com.vians.admin.service.RootUserService;
import com.vians.admin.utils.CommUtil;
import com.vians.admin.utils.HttpUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @ClassName StatisticsController
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/2/1 20:49
 * @Version 1.0
 **/

/**
 * @ClassName SyncLogsController
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/6/5 14:30
 * @Version 1.0
 **/
@CrossOrigin
@RestController
@RequestMapping("/vians/syncLogs")
public class SyncLogsController {

        @Resource
        public RootUserService rootUserService;

        @Resource
        LogService logService;

        @Resource
        ProjectService projectService;


        @PostMapping("/unlockLogs")
        public ResponseData getUnlockStatistics(@RequestBody RxSychLogs rxSychLogs) {
            long rootId = projectService.getProjectById(rxSychLogs.getProjectId()).getRootId();
            return doCheckUnlockLog(rootId, rxSychLogs.getProjectId(), rxSychLogs.getStartTime(), rxSychLogs.getEndTime());
        }

        @PostMapping("/alarmLogs")
        public ResponseData getAlarmStatistics(@RequestBody RxSychLogs rxSychLogs) {
            long rootId = projectService.getProjectById(rxSychLogs.getProjectId()).getRootId();
            return doCheckAlarmLog(rootId, rxSychLogs.getProjectId(), rxSychLogs.getStartTime(), rxSychLogs.getEndTime());
        }

        private ResponseData doCheckUnlockLog(long rootId, long projectId, String startTime, String endTime) {
            RootUserInfo rootUserInfo = rootUserService.getRootUserById(rootId);
            if (rootUserInfo == null) {
                return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
            }
            Map<String, Object> requestParam = HttpUtil.getInstance().getRmtOperateCommParams(
                    rootUserInfo, "0", "", "");
            if (requestParam != null) {
                // 2. 组装特有请求参数
                requestParam.put("BeginTime", String.valueOf(DateUtil.parse(startTime, "yyyy-MM-dd HH:mm:ss").getTime() / 1000));
                requestParam.put("EndTime", String.valueOf(DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss").getTime() / 1000));
                requestParam.put("Page", "1");
                requestParam.put("MAC", "");
                requestParam.put("PageSize", "65535");
                // 3. 发送请求
                JSONObject respObj =HttpUtil.getInstance().doRequest(requestParam, CommConstants.C_API_URL + "/api/CheckLog");
                if (respObj != null) {
                    int result = (int) respObj.get("Result");
                    if (result == 0) {
                        // 4. 解析出响应数据
                        CheckLogResponse checkLogResponse = respObj.toJavaObject(CheckLogResponse.class);
                        // 5. 保存cnt到DB
                        rootUserInfo.setCnt(Long.parseLong(checkLogResponse.getCNT(), 16));
                        rootUserService.updateRootUser(rootUserInfo);
                        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
                        if (checkLogResponse.getLog() != null) {
                            // 删除
                            logService.deleteUnlockLogsByProjectId(projectId);
                            checkLogResponse.getLog().forEach(log -> {
                                log.setProjectId(projectId);
                                log.setTime(CommUtil.parseTime(Long.parseLong(log.getTime())));
                                // 添加
                                logService.addUnlockLog(log);
                            });
                            responseData.addData("count", checkLogResponse.getLog().size());
                        }
                        return responseData;
                    } else if (result == 1002) {
                        return HttpUtil.getInstance().reLogin(rootUserInfo.getPhone(), rootUserInfo.getPassword(), rootUserService, new HttpUtil.ReLoginCallback() {
                            @Override
                            public ResponseData success() {
                                return doCheckUnlockLog(rootId, projectId, startTime, endTime);
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

        private ResponseData doCheckAlarmLog(long rootId, long projectId, String startTime, String endTime) {
            RootUserInfo rootUserInfo = rootUserService.getRootUserById(rootId);
            if (rootUserInfo == null) {
                return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
            }
            Map<String, Object> requestParam = HttpUtil.getInstance().getRmtOperateCommParams(
                    rootUserInfo, "0", "", "");
            if (requestParam != null) {
                // 2. 组装特有请求参数
                requestParam.put("BeginTime", String.valueOf(DateUtil.parse(startTime, "yyyy-MM-dd HH:mm:ss").getTime() / 1000));
                requestParam.put("EndTime", String.valueOf(DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss").getTime() / 1000));
                requestParam.put("Page", "1");
                requestParam.put("MAC", "");
                requestParam.put("PageSize", "65535");
                // 3. 发送请求
                JSONObject respObj =HttpUtil.getInstance().doRequest(requestParam, CommConstants.C_API_URL + "/api/CheckAlarm");
                if (respObj != null) {
                    int result = (int) respObj.get("Result");
                    if (result == 0) {
                        // 4. 解析出响应数据
                        CheckLogResponse checkLogResponse = respObj.toJavaObject(CheckLogResponse.class);
                        // 5. 保存cnt到DB
                        rootUserInfo.setCnt(Long.parseLong(checkLogResponse.getCNT(), 16));
                        rootUserService.updateRootUser(rootUserInfo);
                        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
                        if (checkLogResponse.getLog() != null) {
                            // 删除
                            logService.deleteAlarmLogsByProjectId(projectId);
                            checkLogResponse.getLog().forEach(log -> {
                                log.setProjectId(projectId);
                                log.setTime(CommUtil.parseTime(Long.parseLong(log.getTime())));
                                // 添加
                                logService.addAlarmLog(log);
                            });
                            responseData.addData("count", checkLogResponse.getLog().size());
                        }
                        // 6. 返回数据给客户端
                        return responseData;
                    } else if (result == 1002) {
                        return HttpUtil.getInstance().reLogin(rootUserInfo.getPhone(), rootUserInfo.getPassword(), rootUserService, new HttpUtil.ReLoginCallback() {
                            @Override
                            public ResponseData success() {
                                return doCheckAlarmLog(rootId, projectId, startTime, endTime);
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
}
