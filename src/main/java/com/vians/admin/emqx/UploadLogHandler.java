package com.vians.admin.emqx;

import cn.hutool.core.date.DateUtil;
import com.vians.admin.model.DeviceBatt;
import com.vians.admin.model.UnlockLog;
import com.vians.admin.service.DeviceService;
import com.vians.admin.service.LogService;
import com.vians.admin.utils.CommUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ClassName UnlockLogHandler
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/6/2 18:18
 * @Version 1.0
 **/
@Component
public class UploadLogHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private DeviceService deviceService;

    @Resource
    private LogService logService;

    /**
     * 处理数据
     * @param data
     */
    public void handleLockLogs(byte[] data) {

        // MAC
        byte[] macBuff = new byte[6];
        System.arraycopy(data, 34, macBuff, 0, 6);
        String mac = CommUtil.bytes2HexString(macBuff);
        // 时间
        byte[] timeBuff = new byte[4];
        System.arraycopy(data, 10, timeBuff, 0, 4);
        long timeLong = Long.parseLong(CommUtil.bytes2HexString(CommUtil.reverseBytes(timeBuff)), 16);
//        String time = DateUtil.format(new Date(timeLong * 1000), "yyyy-MM-dd HH:mm:ss");
        if (data[14] == (byte) 0xFD) {
            if (data[16] == 0x02) { // 告警日志
                handleLockAlarmLog(data, mac, timeLong * 1000);
            } else if (data[16] == 0x03) { // 状态日志
                handleLockStateLog(data, mac, timeLong * 1000);
            }
        } else {
            handleUnlockLog(data, mac, timeLong * 1000); // 开锁日志
        }
    }

    /**
     * 处理其他设备告警日志：通用报警、烟雾报警、燃气报警、门磁报警
     * @param data
     * @param action
     */
    public void handleOtherLogs(byte[] data, String action) {
        // MAC
        byte[] macBuff = new byte[6];
        System.arraycopy(data, 8, macBuff, 0, 6);
        String mac = CommUtil.bytes2HexString(macBuff);
        // 时间
        byte[] timeBuff = new byte[4];
        System.arraycopy(data, 3, timeBuff, 0, 4);
        long timeLong = Long.parseLong(CommUtil.bytes2HexString(CommUtil.reverseBytes(timeBuff)), 16);
        // 电量
        int battery = data[7];
        // 保存告警日志
        UnlockLog alarmLog = new UnlockLog();
        alarmLog.setAction(action);
        alarmLog.setMAC(mac);
        alarmLog.setTime(DateUtil.format(new Date(timeLong), "yyyy-MM-dd HH:mm:ss"));
        logService.addAlarmLog(alarmLog);
        // 更新设备电量
        deviceService.updateDeviceBattery(mac, battery, new Date(timeLong));
        // 保存设备电量用于数据统计
        DeviceBatt deviceBatt = new DeviceBatt();
        deviceBatt.setMac(mac);
        deviceBatt.setUpdateTime(new Date(timeLong));
        deviceBatt.setBatt(battery);
        deviceService.addDeviceBatt(deviceBatt);
    }

    /**
     * 处理告警日志
     * @param data
     */
    private void handleLockAlarmLog(byte[] data, String mac, long time) {
        String action = "";
        int value = (data[2] & 0xFF) << 8 | (data[3] & 0xFF);
        switch (value) {
            case 0x0600: // 门被撬
                action = "DOOR_BROKEN_ALARM";
                break;
            case 0x0200: // 门未上锁
                action = "DOOR_LOCK_ALARM";
                break;
            case 0x0400: // 门未关
                action = "DOOR_OPEN_ALARM";
                break;
            case 0x0001: // 门铃
                action = "DOOR_BELL_ALARM";
                break;
            case 0x8000: // 键盘输入锁定
                action = "INPUT_ERROR_ALARM";
                break;
            case 0x0800: // 锁舌未弹出
                action = "DOOR_TONGUE_ALARM";
                break;
            case 0x0002: // 低电压报警
                action = "LOW_BATTERY_ALARM";
                break;
            default:
                // 如果action不认识，则不再往下执行其他解析和保存
                return;
        }
        UnlockLog alarmLog = new UnlockLog();
        alarmLog.setAction(action);
        alarmLog.setMAC(mac);
        alarmLog.setTime(DateUtil.format(new Date(time), "yyyy-MM-dd HH:mm:ss"));
//        long projectId = deviceService.getDeviceProjectId(mac);
//        if (projectId != 0) {
//            alarmLog.setProjectId(projectId);
            logService.addAlarmLog(alarmLog);
//        } else {
//            logger.info("projectId未找到");
//        }
    }

    /**
     * 处理门锁状态日志
     * @param data
     */
    private void handleLockStateLog(byte[] data, String mac, long time) {
        int battery = data[3];
        // 更新设备电量
        deviceService.updateDeviceBattery(mac, battery, new Date(time));
        // 保存设备电量用于数据统计
        DeviceBatt deviceBatt = new DeviceBatt();
        deviceBatt.setMac(mac);
        deviceBatt.setUpdateTime(new Date(time));
        deviceBatt.setBatt(battery);
        deviceService.addDeviceBatt(deviceBatt);
    }

    /**
     * 处理开门日志
     * @param data
     */
    private void handleUnlockLog(byte[] data, String mac, long time) {
        String action = "";
        String value = "";
        String userName = "";
        switch (data[14]) {
            case 0x01: // MIFARE 卡开门
            case 0x02: // ISO14443A 卡开门
            case 0x03: // ISO15693 卡开门
            case 0x04: // ISO14443B 卡开门
            case 0x05: // 蓝牙纽扣开门
            case 0x06: // NFC 卡开门
            case 0x07: // 身份证卡开门
                // 以上均为卡片开门
                action = "CARD_UNLOCK";
                break;
            case 0x20: // 用户密码开门
            case 0x21: // 配对密码开门
            case 0x22: // 单次密码开门
            case 0x23: // 固定密码开门
            case 0x24: // PSW_ONE_REMOT
            case 0x25: // PSW_MANYFIX_REMOT
            case 0x26: // PSW_MANY_REMOT
            case 0x27: // PSW_DYNAMIC_REMOT
            case (byte) 0xA0: // 单次密码
            case (byte) 0xA1: // 时间段密码
            case (byte) 0xA2: // 多次密码
            case (byte) 0xA3: // 动态密码
            case (byte) 0xA4: // 带次数的动态密码
                // 以上均为密码开门
                action = "PSW_UNLOCK";
                break;
            case 0x31: // 机械钥匙开门
                action = "KEY_UNLOCK";
                break;
            case 0x40: // 指纹开门
                action = "FP_UNLOCK";
                break;
            case 0x41: // 人脸开门
                action = "FACE_UNLOCK";
                break;
            case 0x50: // 蓝牙开门
            case 0x51: // 蓝牙开门
                action = "BT_UNLOCK";
                break;
            case 0x52: // 远程开门
                action = "RMT_UNLOCK";
                break;
            default:
                // 如果action不认识，则不再往下执行其他解析和保存
                return;
        }
        // 值
        byte[] valueBuff = new byte[8];
        System.arraycopy(data, 2, valueBuff, 0, 8);
        value = CommUtil.bytes2HexString(valueBuff);
        // 用户名
        byte[] userNameBuff = new byte[16];
        System.arraycopy(data, 18, userNameBuff, 0, 16);
        userName = new String(userNameBuff);
        // 将以上信息生成日志对象，并保存数据库
        UnlockLog unlockLog = new UnlockLog();
        unlockLog.setAction(action);
        unlockLog.setTime(DateUtil.format(new Date(time), "yyyy-MM-dd HH:mm:ss"));
        unlockLog.setValue(value);
        unlockLog.setMAC(mac);
        unlockLog.setName(userName);
        logger.info("==========unlockLog {}", unlockLog.toString());
//        long projectId = deviceService.getDeviceProjectId(mac);
//        if (projectId != 0) {
//            unlockLog.setProjectId(projectId);
            logService.addUnlockLog(unlockLog);
//        } else {
//            logger.info("projectId未找到");
//        }
    }
}
