package com.vians.admin.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密128位CBC模式工具类
 */
public class AESUtils {

    public static void main(String[] args) {

        byte[] key = {(byte) 0x8D, (byte) 0xA4, 0x7A, (byte) 0x83, 0x1E, (byte) 0x95, (byte) 0xC8, 0x6F, 0x70, (byte) 0xF4, 0x34, (byte) 0xF9, 0x12, 0x34, 0x56, 0x00};
        byte[] data = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        String encodeStr = bytesToHexString(encryptByAes(data, key));
        System.out.println("encode:" + encodeStr);
        String decodeStr = bytesToHexString(decryptByAes(hexStringToBytes(encodeStr), key));
        System.out.println("decode:" + decodeStr);
    }

    /**
     * AES加密
     * @param datas 待加密数据，字符串格式
     * @param key 密钥，字符串格式，必须传入16位
     * @return 加密结果，16进制字符串格式
     */
    public static String encryptByAes(String datas, String key) {
        return bytesToHexString(encryptByAes(datas.getBytes(), key.getBytes()));
    }

    private static byte[] encryptByAes(byte[] datas, byte[] key) {
        return calcByAes(Cipher.ENCRYPT_MODE, datas, key);
    }

    /**
     * AES解密
     * @param datas 密文，16进制字符串格式
     * @param key 密钥，字符串格式，必须传入16位
     * @return 解密结果，字符串格式
     */
    public static String decryptByAes(String datas, String key) {
        return new String(decryptByAes(hexStringToBytes(datas), key.getBytes()));
    }

    private static byte[] decryptByAes(byte[] datas, byte[] key) {
        return calcByAes(Cipher.DECRYPT_MODE, datas, key);
    }


    private static byte[] calcByAes(int mode, byte[] datas, byte[] key) {
        if (datas == null || key == null || key.length != 16) {
            return null;
        }
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(mode, keySpec);
            return cipher.doFinal(datas);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制字符串转换为byte数组
     *
     * @param hexString 需要转换的16进制字符串
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.trim().replaceAll("\\s*", ""); // 去除字符串中的空格

        String hexFormat = "0123456789ABCDEF";

        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (hexFormat.indexOf(hexChars[pos]) << 4 | hexFormat
                    .indexOf(hexChars[pos + 1]));
        }
        return d;
    }
}
