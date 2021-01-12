package com.vians.admin.utils;

import org.apache.commons.codec.digest.DigestUtils;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * 对与设备通信的数据（payload）加解密工具
 */
public class PayloadEnDeUtil {

    private static final int CRC_BASE = 0x5555;

    /**
     * 对将要发送给设备端的数据进行加密
     *
     * @param uuid 设备的唯一UUID
     * @param reqHex 原始16进制字符串
     * @return 最终发送给设备的payload数据
     */
    public static String encryptByUUID(String uuid, String reqHex) {
        byte[] resBuf = CommUtil.hexStringToBytes(reqHex);
        int crc = crc16(CRC_BASE, resBuf);
        // 需要加密的原始字节数组
        byte[] originBuf = ByteBuffer.allocate(resBuf.length + 2).put(resBuf).putShort((short) crc).array();
        byte[] key = getKey(uuid, originBuf.length);
        byte[] oxrBuf = bytesOxrBytes(key, originBuf);
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodeBuf = encoder.encode(oxrBuf);
        return new String(encodeBuf);
    }

    private static byte[] bytesOxrBytes(byte[] bytes1, byte[] bytes2) {
        for (int i = 0; i < bytes1.length; i++) {
            bytes1[i] = (byte) ((int) bytes1[i] ^ (int) bytes2[i]);
        }
        return bytes1;
    }

    /**
     * 对设备端返回的base64字符串进行解密
     * @param uuid 设备的唯一UUID
     * @param respStr 设备端返回的base64字符串
     * @return 解密出的16进制字符串
     */
    public static String decryptByUUID(String uuid, String respStr) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] repArr = decoder.decode(respStr.getBytes());
        byte[] key = getKey(uuid, repArr.length);
        return CommUtil.bytes2HexString(bytesOxrBytes(key, repArr));
    }

    /**
     * 检查最末2位字节是否为正确得crc
     * @param allData
     * @return
     */
    public static boolean checkCrc(String allData) {
        String crcStr = allData.substring(allData.length() - 4);
        String dataStr = allData.substring(0, allData.length() - 4);
        int crc = crc16(CRC_BASE, CommUtil.hexStringToBytes(dataStr));
        return Integer.valueOf(crcStr, 16) == crc;
    }

    public static void main(String[] args) {
        // 98B669F8C8AAD47E
        System.out.println("encryptByUUID -> " + encryptByUUID("98B669F8C8AAD47E", "0923013870F55C0000"));
        System.out.println("decryptByUUID ->" + decryptByUUID("98B669F8C8AAD47E", "kGp5/oteD0hfow=="));
    }

    /**
     * 对翻转后的uuid反复取md5得到指定长度的密钥
     * @param uuid 设备的唯一UUID
     * @param len 最终要达到的密钥长度
     * @return
     */
    private static byte[] getKey(String uuid, int len) {
        // 经过翻转之后的UUID数组
        byte[] reverseHexStrBuf = CommUtil.hexStringToBytes(reverseHexStr(uuid));
        // 每一段16位，cnt为要计算的段数
        int cnt = len/16 + (len % 16 > 0 ? 1 : 0);
        byte[] md5uuid1 = DigestUtils.md5(reverseHexStrBuf);
        // 用于拼接一次次的md5，用于最终异或运算
        ByteBuffer keyBB = ByteBuffer.allocate(len);
        // 用于存放已经被加密过的md5段，以便于下次需要反复加密时直接取出
        List<byte[]> keyList = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            byte[] keyI;
            // 第一段用原md5计算，后面的用上一段计算
            if (i == 0) {
                keyI =DigestUtils.md5(md5uuid1);
            } else {
                keyI = DigestUtils.md5(keyList.get(i - 1));
            }
            keyList.add(keyI);
            // 最后一段超过len长度的不要
            if (i == cnt - 1) {
                keyBB.put(Arrays.copyOfRange(keyI, 0, len % 16));
            } else {
                keyBB.put(keyI);
            }
        }
        return keyBB.array();
    }

    /**
     * crc16 多项式校验方法
     *
     * @param buffer
     * @return
     */
    private static int crc16(int base, final byte[] buffer) {
        int crc = base;
        for (byte buf : buffer) {
            int i = buf;
            i &= 0xFF;
            crc = (((crc >> 8) & 0xFF) | (crc << 8));
            crc ^= i;
            crc ^= ((crc & 0xFF) >> 4) & 0xFF;
            crc ^= (crc << 8) << 4;
            crc ^= ((crc & 0xFF) << 4) << 1;
            crc &= 0xffff;
        }
        crc &= 0xffff;
        return crc;
    }

    /**
     * 对16进制字符串按字节翻转
     * @param value 原始数据
     * @return 翻转之后的数据
     */
    private static String reverseHexStr(String value) {
        StringBuilder result = new StringBuilder();
        for (int i = value.length(); i >= 2; i = i - 2) {
            result.append(value, i - 2, i);
        }
        return result.toString();
    }
}
