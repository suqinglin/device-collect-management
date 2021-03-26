package com.vians.admin.controller;

import com.vians.admin.common.CommConstants;
import com.vians.admin.common.RmtOperateHelper;
import com.vians.admin.model.AuthorizeContentInfo;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    final Logger logger = LoggerFactory.getLogger(getClass());

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
        Long createUserId = appBean.getCurrentUserId();
        if (createUserId == null) {
            return new ResponseData(ResponseCode.ERROR_UN_AUTHORIZE_LOGIN);
        }
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        // 保存密码授权
        RxAuthorize authorizeInfo = new RxAuthorize();
        authorizeInfo.setContent(addPassword.getPsw());
        authorizeInfo.setTimeType(Integer.parseInt(addPassword.getPswType()));
        if (authorizeInfo.getTimeType() == CommConstants.TIME_TYPE_ONCE || authorizeInfo.getTimeType() == CommConstants.TIME_TYPE_PERIOD) {
            authorizeInfo.setEndTime(new Date(Long.parseLong(addPassword.getEndTime()) * 1000));
            authorizeInfo.setStartTime(new Date(Long.parseLong(addPassword.getBeginTime()) * 1000));
        }
        authorizeInfo.setUserId(addPassword.getUserId());
        authorizeInfo.setType(CommConstants.AUTHORIZE_TYPE_PASSWORD); // 密码授权
        authorizeInfo.setCreateUserId(createUserId);
        long authorizeId = authorizeService.addAuthorize(authorizeInfo);
        // 将授权信息下发到房间设备
        if (addPassword.getRoomIds().size() > 0) {
            int roomTotal = addPassword.getRoomIds().size();
            ResponseData responseData = doAddPassword(addPassword, rootId, authorizeId);
            if (!ResponseCode.SUCCESS.getKey().equals(responseData.getCode())) {
                responseData.addData("successCount", roomTotal - addPassword.getRoomIds().size() - 1);
                responseData.addData("failCount", addPassword.getRoomIds().size() + 1);
                responseData.addData("authorizeId", authorizeId);
            }
            return responseData;
        } else {
            return ResponseData.error(ResponseCode.ERROR_AUTHORIZE_NO_ROOM);
        }
    }

    /**
     * 发送失败的密码授权继续发送
     * @param addPassword
     * @return
     * @throws InterruptedException
     */
    @PostMapping("/reAddPassword")
    public ResponseData reAddPassword(@RequestBody RxAddPassword addPassword) throws InterruptedException {
        Long createUserId = appBean.getCurrentUserId();
        if (createUserId == null) {
            return new ResponseData(ResponseCode.ERROR_UN_AUTHORIZE_LOGIN);
        }
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        // 将授权信息下发到房间设备
        if (addPassword.getRoomIds().size() > 0) {
            int roomTotal = addPassword.getRoomIds().size();
            ResponseData responseData = doAddPassword(addPassword, rootId, addPassword.getAuthorizeId());
            if (!ResponseCode.SUCCESS.getKey().equals(responseData.getCode())) {
                responseData.addData("successCount", roomTotal - addPassword.getRoomIds().size() - 1);
                responseData.addData("failCount", addPassword.getRoomIds().size() + 1);
                responseData.addData("authorizeId", addPassword.getAuthorizeId());
            }
            return responseData;
        } else {
            return ResponseData.error(ResponseCode.ERROR_AUTHORIZE_NO_ROOM);
        }
    }

    private ResponseData doAddPassword(RxAddPassword addPassword, Long rootId, Long authorizeId) throws InterruptedException {
        long roomId = addPassword.getRoomIds().get(0);
        int position = authorizeService.findEmptyPosition(roomId, CommConstants.AUTHORIZE_TYPE_PASSWORD);
        // 删除掉第一个房间
        addPassword.getRoomIds().remove(0);
        return RmtOperateHelper.rmtOperateByRoom(
                rootId,
                rootUserService,
                appBean,
                deviceService,
                "/api/RmtAddPsw",
                roomId,
                false,
                new RmtOperateHelper.Callback() {

                    @Override
                    public void addParams(Map<String, Object> requestParam, String token, String psw, long nextCnt) {
                        String pswEncrypt = KeyUtil.encode2HashXOR(addPassword.getPsw().getBytes(), token, psw, nextCnt);
                        String userName = KeyUtil.encode2HashXOR(addPassword.getUserName().getBytes(), token, psw, nextCnt);
                        requestParam.put("UserName", userName); // 加密后的用户名
                        requestParam.put("Psw", pswEncrypt);
                        requestParam.put("PswIdx", String.valueOf(position));
                        requestParam.put("PswType", addPassword.getPswType());
                        requestParam.put("BeginTime", addPassword.getBeginTime());
                        requestParam.put("EndTime", addPassword.getEndTime());
                    }

                    @Override
                    public ResponseData onSuccess(ResponseData responseData) throws InterruptedException {
                        authorizeService.addAuthorizeRoom(authorizeId, roomId, position);
                        if (addPassword.getRoomIds().size() > 0) {
                            return doAddPassword(addPassword, rootId, authorizeId);
                        } else {
                            return responseData;
                        }
                    }
                });
    }

    @PostMapping("/deletePassword")
    public ResponseData deletePassword(@RequestBody RxDeletePassword deletePassword) throws InterruptedException {
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
            public ResponseData onSuccess(ResponseData responseData) {
                authorizeService.deleteAuthorize(deletePassword.getId());
                return responseData;
            }
        });
    }

    @PostMapping("/deleteAllPassword")
    public ResponseData deleteAllPassword(@RequestBody RxDeletePassword deletePassword) throws InterruptedException {
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
                    public ResponseData onSuccess(ResponseData responseData) {
                        authorizeService.deleteAuthorizeByRoom(deletePassword.getRoomId(), CommConstants.AUTHORIZE_TYPE_PASSWORD);
                        return responseData;
                    }
                });
    }

    @PostMapping("/addCard")
    public ResponseData addCard(@RequestBody RxAddCard addCard) throws InterruptedException {
        Long createUserId = appBean.getCurrentUserId();
        if (createUserId == null) {
            return new ResponseData(ResponseCode.ERROR_UN_AUTHORIZE_LOGIN);
        }
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        // 保存卡片授权
        RxAuthorize authorizeInfo = new RxAuthorize();
        authorizeInfo.setContent(addCard.getCardNum());
        authorizeInfo.setTimeType(Integer.parseInt(addCard.getCardType()));
        if (authorizeInfo.getTimeType() == CommConstants.TIME_TYPE_ONCE || authorizeInfo.getTimeType() == CommConstants.TIME_TYPE_PERIOD) {
            authorizeInfo.setEndTime(new Date(Long.parseLong(addCard.getEndTime()) * 1000));
            authorizeInfo.setStartTime(new Date(Long.parseLong(addCard.getBeginTime()) * 1000));
        }
        authorizeInfo.setUserId(addCard.getUserId());
        authorizeInfo.setType(CommConstants.AUTHORIZE_TYPE_CARD); // 卡片授权
        authorizeInfo.setCreateUserId(createUserId);
        long authorizeId = authorizeService.addAuthorize(authorizeInfo);
        int roomTotal = addCard.getRoomIds().size();
        if (roomTotal > 0) {
            ResponseData responseData = doAddCard(addCard, rootId, authorizeId);
            if (!ResponseCode.SUCCESS.getKey().equals(responseData.getCode())) {
                responseData.addData("successCount", roomTotal - addCard.getRoomIds().size() - 1);
                responseData.addData("failCount", addCard.getRoomIds().size() + 1);
                responseData.addData("authorizeId", authorizeId);
            }
            return responseData;
        } else {
            return ResponseData.error(ResponseCode.ERROR_AUTHORIZE_NO_ROOM);
        }
    }

    /**
     * 发送失败的卡片授权继续发送
     * @param addCard
     * @return
     * @throws InterruptedException
     */
    @PostMapping("/reAddCard")
    public ResponseData reAddCard(@RequestBody RxAddCard addCard) throws InterruptedException {
        Long createUserId = appBean.getCurrentUserId();
        if (createUserId == null) {
            return new ResponseData(ResponseCode.ERROR_UN_AUTHORIZE_LOGIN);
        }
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        // 将授权信息下发到房间设备
        int roomTotal = addCard.getRoomIds().size();
        if (roomTotal > 0) {
            ResponseData responseData = doAddCard(addCard, rootId, addCard.getAuthorizeId());
            if (!ResponseCode.SUCCESS.getKey().equals(responseData.getCode())) {
                responseData.addData("successCount", roomTotal - addCard.getRoomIds().size() - 1);
                responseData.addData("failCount", addCard.getRoomIds().size() + 1);
                responseData.addData("authorizeId", addCard.getAuthorizeId());
            }
            return responseData;
        } else {
            return ResponseData.error(ResponseCode.ERROR_AUTHORIZE_NO_ROOM);
        }
    }

    private ResponseData doAddCard(RxAddCard addCard, Long rootId, Long authorizeId) throws InterruptedException {
        long roomId = addCard.getRoomIds().get(0);
        addCard.getRoomIds().remove(0);
        int position = authorizeService.findEmptyPosition(roomId, CommConstants.AUTHORIZE_TYPE_CARD);
        return RmtOperateHelper.rmtOperateByRoom(
                rootId,
                rootUserService,
                appBean,
                deviceService,
                "/api/RmtAddCard",
                roomId,
                false,
                new RmtOperateHelper.Callback() {
                    @Override
                    public void addParams(Map<String, Object> requestParam, String token, String psw, long nextCnt) {
                        String cardNumEncrypt = KeyUtil.encode2HashXOR(addCard.getCardNum().getBytes(), token, psw, nextCnt);
                        String userName = KeyUtil.encode2HashXOR(addCard.getUserName().getBytes(), token, psw, nextCnt);
                        requestParam.put("UserName", userName); // 加密后的用户名
                        requestParam.put("CardNum", cardNumEncrypt);
                        requestParam.put("CardIdx", String.valueOf(position));
                        requestParam.put("CardType", addCard.getCardType());
                        requestParam.put("BeginTime", addCard.getBeginTime());
                        requestParam.put("EndTime", addCard.getEndTime());
                    }

                    @Override
                    public ResponseData onSuccess(ResponseData responseData) throws InterruptedException {
                        authorizeService.addAuthorizeRoom(authorizeId, roomId, position);
                        if (addCard.getRoomIds().size() > 0) {
                            return doAddCard(addCard, rootId, authorizeId);
                        } else {
                            return responseData;
                        }
                    }
                });
    }

    @PostMapping("/deleteCard")
    public ResponseData deleteCard(@RequestBody RxDeleteCard deleteCard) throws InterruptedException {
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
            public ResponseData onSuccess(ResponseData responseData) {
                authorizeService.deleteAuthorize(deleteCard.getId());
                return responseData;
            }
        });
    }

    @PostMapping("/deleteAllCard")
    public ResponseData deleteAllCard(@RequestBody RxDeleteCard deleteCard) throws InterruptedException {
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
                    public ResponseData onSuccess(ResponseData responseData) {
                        authorizeService.deleteAuthorizeByRoom(deleteCard.getRoomId(), CommConstants.AUTHORIZE_TYPE_CARD);
                        return responseData;
                    }
                });
    }

    @PostMapping("/addFp")
    public ResponseData addFp(@RequestBody RxAddFp addFp) throws InterruptedException {
        Long createUserId = appBean.getCurrentUserId();
        if (createUserId == null) {
            return new ResponseData(ResponseCode.ERROR_UN_AUTHORIZE_LOGIN);
        }
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        // 保存指纹授权
        AuthorizeContentInfo authorizeContent = new AuthorizeContentInfo(addFp.getFpTemp(), CommConstants.AUTHORIZE_CONTENT_TYPE_FP_TEMP);
        long authorizeContentId = authorizeService.addAuthorizeContent(authorizeContent);
        RxAuthorize authorizeInfo = new RxAuthorize();
        authorizeInfo.setContent(addFp.getFeatureValue());
        authorizeInfo.setTimeType(Integer.parseInt(addFp.getFpType()));
        if (authorizeInfo.getTimeType() == CommConstants.TIME_TYPE_ONCE || authorizeInfo.getTimeType() == CommConstants.TIME_TYPE_PERIOD) {
            authorizeInfo.setEndTime(new Date(Long.parseLong(addFp.getEndTime()) * 1000));
            authorizeInfo.setStartTime(new Date(Long.parseLong(addFp.getBeginTime()) * 1000));
        }
        authorizeInfo.setUserId(addFp.getUserId());
        authorizeInfo.setType(CommConstants.AUTHORIZE_TYPE_FP); // 指纹授权
        authorizeInfo.setCreateUserId(createUserId);
        authorizeInfo.setTempContentId(authorizeContentId);
        Long authorizeId = authorizeService.addAuthorize(authorizeInfo);
        int roomTotal = addFp.getRoomIds().size();
        if (roomTotal > 0) {
            ResponseData responseData = doAddFp(addFp, rootId, authorizeId);
            if (!ResponseCode.SUCCESS.getKey().equals(responseData.getCode())) {
                responseData.addData("successCount", roomTotal - addFp.getRoomIds().size() - 1);
                responseData.addData("failCount", addFp.getRoomIds().size() + 1);
                responseData.addData("authorizeId", authorizeId);
            }
            return responseData;
        } else {
            return ResponseData.error(ResponseCode.ERROR_AUTHORIZE_NO_ROOM);
        }
    }

    /**
     * 发送失败的指纹授权继续发送
     * @param addFp
     * @return
     * @throws InterruptedException
     */
    @PostMapping("/reAddFp")
    public ResponseData reAddFp(@RequestBody RxAddFp addFp) throws InterruptedException {
        Long createUserId = appBean.getCurrentUserId();
        if (createUserId == null) {
            return new ResponseData(ResponseCode.ERROR_UN_AUTHORIZE_LOGIN);
        }
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        // 将授权信息下发到房间设备
        int roomTotal = addFp.getRoomIds().size();
        if (roomTotal > 0) {
            ResponseData responseData = doAddFp(addFp, rootId, addFp.getAuthorizeId());
            if (!ResponseCode.SUCCESS.getKey().equals(responseData.getCode())) {
                responseData.addData("successCount", roomTotal - addFp.getRoomIds().size() - 1);
                responseData.addData("failCount", addFp.getRoomIds().size() + 1);
                responseData.addData("authorizeId", addFp.getAuthorizeId());
            }
            return responseData;
        } else {
            return ResponseData.error(ResponseCode.ERROR_AUTHORIZE_NO_ROOM);
        }
    }

    private ResponseData doAddFp(RxAddFp addFp, Long rootId, Long authorizeId) throws InterruptedException {
        long roomId = addFp.getRoomIds().get(0);
        addFp.getRoomIds().remove(0);
        int position = authorizeService.findEmptyPosition(roomId, CommConstants.AUTHORIZE_TYPE_FP);
        return RmtOperateHelper.rmtOperateByRoom(
                rootId,
                rootUserService,
                appBean,
                deviceService,
                "/api/RmtAddFp",
                roomId,
                true,
                new RmtOperateHelper.Callback() {
                    @Override
                    public void addParams(Map<String, Object> requestParam, String token, String psw, long nextCnt) {
                        String fpTempEncrypt = KeyUtil.encode2HashXOR(CommUtil.hexStringToBytes(addFp.getFpTemp()), token, psw, nextCnt);
                        String userName = KeyUtil.encode2HashXOR(addFp.getUserName().getBytes(), token, psw, nextCnt);
                        requestParam.put("UserName", userName); // 加密后的用户名
                        requestParam.put("FpTemp", fpTempEncrypt);
                        requestParam.put("FpIdx", String.valueOf(position));
                        requestParam.put("FpType", addFp.getFpType());
                        requestParam.put("FpFmt", addFp.getFpFmt());
                        requestParam.put("BeginTime", addFp.getBeginTime());
                        requestParam.put("EndTime", addFp.getEndTime());
                    }

                    @Override
                    public ResponseData onSuccess(ResponseData responseData) throws InterruptedException {
                        authorizeService.addAuthorizeRoom(authorizeId, roomId, position);
                        if (addFp.getRoomIds().size() > 0) {
                            return doAddFp(addFp, rootId, authorizeId);
                        } else {
                            return responseData;
                        }
                    }
                });
    }

    @PostMapping("/deleteFp")
    public ResponseData deleteFp(@RequestBody RxDeleteFp deleteFp) throws InterruptedException {
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
            public ResponseData onSuccess(ResponseData responseData) {
//                authorizeService.deleteAuthorizeTempContent(deleteFp.getId());
                authorizeService.deleteAuthorize(deleteFp.getId());
                return responseData;
            }
        });
    }

    @PostMapping("/deleteAllFp")
    public ResponseData deleteAllFp(@RequestBody RxDeleteFp deleteFp) throws InterruptedException {
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
                    public ResponseData onSuccess(ResponseData responseData) {
                        authorizeService.deleteAuthorizeByRoom(deleteFp.getRoomId(), CommConstants.AUTHORIZE_TYPE_FP);
                        return responseData;
                    }
                });
    }
}
