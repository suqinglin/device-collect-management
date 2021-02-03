package com.vians.admin.controller;

import com.vians.admin.common.RmtOperateHelper;
import com.vians.admin.model.AuthorizeInfo;
import com.vians.admin.request.*;
import com.vians.admin.response.ResponseCode;
import com.vians.admin.response.ResponseData;
import com.vians.admin.service.AuthorizeService;
import com.vians.admin.service.DeviceService;
import com.vians.admin.service.RootUserService;
import com.vians.admin.utils.CommUtil;
import com.vians.admin.utils.KeyUtil;
import com.vians.admin.web.AppBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AuthorizeController
 * @Description 授权
 * @Author su qinglin
 * @Date 2021/1/18 10:39
 * @Version 1.0
 **/

@CrossOrigin
@RestController
@RequestMapping("/vians/authorize")
public class AuthorizeController {

    @Autowired
    AppBean appBean;

//    @Autowired
//    public PropUtil propUtil;

//    @Resource
//    public RedisService redisService;

    @Resource
    private AuthorizeService authorizeService;

    @Resource
    private DeviceService deviceService;

    @Resource
    public RootUserService rootUserService;

    @PostMapping("/list")
    public ResponseData getAuthorizeList(@RequestBody RxGetAuthorizeList rx) {
        List<AuthorizeInfo> authorizeList = authorizeService.getAuthorizeList(rx.getUserId(), rx.getRoomId());
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("authorizes", authorizeList);
        return responseData;
    }

    @PostMapping("/addPassword")
    public ResponseData addPassword(@RequestBody RxAddPassword addPassword) throws InterruptedException {
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        return RmtOperateHelper.rmtOperateByRoom(
                rootId,
                rootUserService,
                appBean,
                deviceService,
                "/api/RmtAddPsw",
                addPassword.getRoomId(),
                false,
                new RmtOperateHelper.Callback() {

            @Override
            public void addParams(Map<String, Object> requestParam, String token, String psw, long nextCnt) {
                String pswEncrypt = KeyUtil.encode2HashXOR(addPassword.getPsw().getBytes(), token, psw, nextCnt);
                String userName = KeyUtil.encode2HashXOR(addPassword.getUserName().getBytes(), token, psw, nextCnt);
                requestParam.put("UserName", userName); // 加密后的用户名
                requestParam.put("Psw", pswEncrypt);
                requestParam.put("PswIdx", addPassword.getPswIdx());
                requestParam.put("PswType", addPassword.getPswType());
                requestParam.put("BeginTime", addPassword.getBeginTime());
                requestParam.put("EndTime", addPassword.getEndTime());
            }

            @Override
            public void saveToDB(long createUserId) {
                RxAuthorize authorizeInfo = new RxAuthorize();
                authorizeInfo.setContent(addPassword.getPsw());
                authorizeInfo.setTimeType(Integer.parseInt(addPassword.getPswType()));
                if (authorizeInfo.getTimeType() == 2) {
                    authorizeInfo.setEndTime(new Date(Long.parseLong(addPassword.getEndTime()) * 1000));
                    authorizeInfo.setStartTime(new Date(Long.parseLong(addPassword.getBeginTime()) * 1000));
                }
                authorizeInfo.setPosition(Integer.parseInt(addPassword.getPswIdx()));
                authorizeInfo.setUserId(addPassword.getUserId());
                authorizeInfo.setRoomId(addPassword.getRoomId());
                authorizeInfo.setType(1); // 密码授权
                authorizeInfo.setCreateUserId(createUserId);
                authorizeService.addAuthorize(authorizeInfo);
            }
        });
    }

    @PostMapping("/deletePassword")
    private ResponseData deletePassword(@RequestBody RxDeletePassword deletePassword) throws InterruptedException {
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        return RmtOperateHelper.rmtOperateByRoom(
                rootId,
                rootUserService,
                appBean,
                deviceService,
                "/api/RmtDeletePsw",
                deletePassword.getRoomId(),
                false,
                new RmtOperateHelper.Callback() {
            @Override
            public void addParams(Map<String, Object> requestParam, String token, String psw, long nextCnt) {
                String pswEncrypt = KeyUtil.encode2HashXOR(deletePassword.getPsw().getBytes(), token, psw, nextCnt);
                requestParam.put("Psw", pswEncrypt);
                requestParam.put("PswIdx", deletePassword.getPswIdx());
                requestParam.put("UserName", "None");
            }

            @Override
            public void saveToDB(long createUserId) {
                authorizeService.deleteAuthorize(deletePassword.getId());
            }
        });
    }

    @PostMapping("/deleteAllPassword")
    private ResponseData deleteAllPassword(@RequestBody RxDeletePassword deletePassword) throws InterruptedException {
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        return RmtOperateHelper.rmtOperateByRoom(
                rootId,
                rootUserService,
                appBean,
                deviceService,
                "/api/RmtDeletePsw",
                deletePassword.getRoomId(),
                false,
                new RmtOperateHelper.Callback() {
                    @Override
                    public void addParams(Map<String, Object> requestParam, String token, String psw, long nextCnt) {
                        requestParam.put("Psw", "");
                        requestParam.put("PswIdx", "");
                        requestParam.put("UserName", "AllPsw");
                    }

                    @Override
                    public void saveToDB(long createUserId) {
                        authorizeService.deleteAuthorizeByRoom(deletePassword.getRoomId(), 1);
                    }
                });
    }

    @PostMapping("/addCard")
    public ResponseData addCard(@RequestBody RxAddCard addCard) throws InterruptedException {
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        return RmtOperateHelper.rmtOperateByRoom(
                rootId,
                rootUserService,
                appBean,
                deviceService,
                "/api/RmtAddCard",
                addCard.getRoomId(),
                false,
                new RmtOperateHelper.Callback() {
            @Override
            public void addParams(Map<String, Object> requestParam, String token, String psw, long nextCnt) {
                String cardNumEncrypt = KeyUtil.encode2HashXOR(addCard.getCardNum().getBytes(), token, psw, nextCnt);
                String userName = KeyUtil.encode2HashXOR(addCard.getUserName().getBytes(), token, psw, nextCnt);
                requestParam.put("UserName", userName); // 加密后的用户名
                requestParam.put("CardNum", cardNumEncrypt);
                requestParam.put("CardIdx", addCard.getCardIdx());
                requestParam.put("CardType", addCard.getCardType());
                requestParam.put("BeginTime", addCard.getBeginTime());
                requestParam.put("EndTime", addCard.getEndTime());
            }

            @Override
            public void saveToDB(long createUserId) {
                RxAuthorize authorizeInfo = new RxAuthorize();
                authorizeInfo.setContent(addCard.getCardNum().substring(0, 8));
                authorizeInfo.setTimeType(Integer.parseInt(addCard.getCardType()));
                if (authorizeInfo.getTimeType() == 2) {
                    authorizeInfo.setEndTime(new Date(Long.parseLong(addCard.getEndTime()) * 1000));
                    authorizeInfo.setStartTime(new Date(Long.parseLong(addCard.getBeginTime()) * 1000));
                }
                authorizeInfo.setPosition(Integer.parseInt(addCard.getCardIdx()));
                authorizeInfo.setUserId(addCard.getUserId());
                authorizeInfo.setRoomId(addCard.getRoomId());
                authorizeInfo.setType(2); // 卡片授权
                authorizeInfo.setCreateUserId(createUserId);
                authorizeService.addAuthorize(authorizeInfo);
            }
        });
    }

    @PostMapping("/deleteCard")
    private ResponseData deleteCard(@RequestBody RxDeleteCard deleteCard) throws InterruptedException {
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        return RmtOperateHelper.rmtOperateByRoom(
                rootId,
                rootUserService,
                appBean,
                deviceService,
                "/api/RmtDeleteCard",
                deleteCard.getRoomId(),
                false,
                new RmtOperateHelper.Callback() {
            @Override
            public void addParams(Map<String, Object> requestParam, String token, String psw, long nextCnt) {
                String cardNumEncrypt = KeyUtil.encode2HashXOR(deleteCard.getCardNum().getBytes(), token, psw, nextCnt);
                requestParam.put("CardNum", cardNumEncrypt);
                requestParam.put("CardIdx", deleteCard.getCardIdx());
                requestParam.put("UserName", "None");
            }

            @Override
            public void saveToDB(long createUserId) {
                authorizeService.deleteAuthorize(deleteCard.getId());
            }
        });
    }

    @PostMapping("/deleteAllCard")
    private ResponseData deleteAllCard(@RequestBody RxDeleteCard deleteCard) throws InterruptedException {
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        return RmtOperateHelper.rmtOperateByRoom(
                rootId,
                rootUserService,
                appBean,
                deviceService,
                "/api/RmtDeleteCard",
                deleteCard.getRoomId(),
                false,
                new RmtOperateHelper.Callback() {
                    @Override
                    public void addParams(Map<String, Object> requestParam, String token, String psw, long nextCnt) {
                        requestParam.put("CardNum", "");
                        requestParam.put("CardIdx", "");
                        requestParam.put("UserName", "AllCard");
                    }

                    @Override
                    public void saveToDB(long createUserId) {
                        authorizeService.deleteAuthorizeByRoom(deleteCard.getRoomId(), 2);
                    }
                });
    }

    @PostMapping("/addFp")
    public ResponseData addFp(@RequestBody RxAddFp addFp) throws InterruptedException {
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        return RmtOperateHelper.rmtOperateByRoom(
                rootId,
                rootUserService,
                appBean,
                deviceService,
                "/api/RmtAddFp",
                addFp.getRoomId(),
                true,
                new RmtOperateHelper.Callback() {
            @Override
            public void addParams(Map<String, Object> requestParam, String token, String psw, long nextCnt) {
                String fpTempEncrypt = KeyUtil.encode2HashXOR(CommUtil.hexStringToBytes(addFp.getFpTemp()), token, psw, nextCnt);
                String userName = KeyUtil.encode2HashXOR(addFp.getUserName().getBytes(), token, psw, nextCnt);
                requestParam.put("UserName", userName); // 加密后的用户名
                requestParam.put("FpTemp", fpTempEncrypt);
                requestParam.put("FpIdx", addFp.getFpIdx());
                requestParam.put("FpType", addFp.getFpType());
                requestParam.put("FpFmt", addFp.getFpFmt());
                requestParam.put("BeginTime", addFp.getBeginTime());
                requestParam.put("EndTime", addFp.getEndTime());
            }

            @Override
            public void saveToDB(long createUserId) {
                RxAuthorize authorizeInfo = new RxAuthorize();
                authorizeInfo.setContent(addFp.getFeatureValue());
                authorizeInfo.setTimeType(Integer.parseInt(addFp.getFpType()));
                if (authorizeInfo.getTimeType() == 2) {
                    authorizeInfo.setEndTime(new Date(Long.parseLong(addFp.getEndTime()) * 1000));
                    authorizeInfo.setStartTime(new Date(Long.parseLong(addFp.getBeginTime()) * 1000));
                }
                authorizeInfo.setPosition(Integer.parseInt(addFp.getFpIdx()));
                authorizeInfo.setUserId(addFp.getUserId());
                authorizeInfo.setRoomId(addFp.getRoomId());
                authorizeInfo.setType(3); // 指纹授权
                authorizeInfo.setCreateUserId(createUserId);
                authorizeService.addAuthorize(authorizeInfo);
            }
        });
    }

    @PostMapping("/deleteFp")
    private ResponseData deleteFp(@RequestBody RxDeleteFp deleteFp) throws InterruptedException {
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        return RmtOperateHelper.rmtOperateByRoom(
                rootId,
                rootUserService,
                appBean,
                deviceService,
                "/api/RmtDeleteFp",
                deleteFp.getRoomId(),
                false,
                new RmtOperateHelper.Callback() {
            @Override
            public void addParams(Map<String, Object> requestParam, String token, String psw, long nextCnt) {
                requestParam.put("FpIdx", deleteFp.getFpIdx());
                requestParam.put("UserName", "None");
            }

            @Override
            public void saveToDB(long createUserId) {
                authorizeService.deleteAuthorize(deleteFp.getId());
            }
        });
    }

    @PostMapping("/deleteAllFp")
    private ResponseData deleteAllFp(@RequestBody RxDeleteFp deleteFp) throws InterruptedException {
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        return RmtOperateHelper.rmtOperateByRoom(
                rootId,
                rootUserService,
                appBean,
                deviceService,
                "/api/RmtDeleteFp",
                deleteFp.getRoomId(),
                false,
                new RmtOperateHelper.Callback() {
                    @Override
                    public void addParams(Map<String, Object> requestParam, String token, String psw, long nextCnt) {
                        requestParam.put("FpIdx", "");
                        requestParam.put("UserName", "AllFp");
                    }

                    @Override
                    public void saveToDB(long createUserId) {
                        authorizeService.deleteAuthorizeByRoom(deleteFp.getRoomId(), 3);
                    }
                });
    }
}
