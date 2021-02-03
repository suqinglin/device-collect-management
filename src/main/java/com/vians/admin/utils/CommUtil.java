package com.vians.admin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class CommUtil {

    /**
     * 随机生成2^32以内的正整数
     * @return
     */
    public static String getRandom8Hex() {
        long n = 1L<<31; // 2^31
        Random rng = new Random();
        long bits, val;
        do {
            bits = (rng.nextLong() << 1) >>> 1;
            val = bits % n;
        } while (bits-val+(n-1) < 0L);
        return String.format("%08X",val);
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

    /**
     * 将byte数组转化为16进制字符串
     */
    public static String bytes2HexString(byte[] bytes) {
        int len = bytes.length;
        StringBuffer sb = new StringBuffer();
        char temp;
        for (int i = 0; i < len; i++) {
            temp = (char) ((bytes[i] >> 4) & 0x0F);
            sb.append((char) (temp > 9 ? (temp + 'A' - 10) : temp + '0'));
            temp = (char) (bytes[i] & 0x0F);
            sb.append((char) (temp > 9 ? (temp + 'A' - 10) : temp + '0'));
        }
        return sb.toString();
    }

//    /**
//     * hex异或计算得到hex
//     * @param hex1
//     * @param hex2
//     * @return
//     */
//    public static String hexXOR(String hex1, String hex2){
//        BigInteger i1 = new BigInteger(hex1, 16);
//        BigInteger i2 = new BigInteger(hex2, 16);
//        BigInteger res = i1.xor(i2);
//        return res.toString(16).toUpperCase();
//    }

    public static byte[] bytesXorBytes(byte[] bytes1, byte[] bytes2) {
        for (int i = 0; i < bytes1.length; i++) {
            bytes1[i] = (byte) ((int) bytes1[i] ^ (int) bytes2[i]);
        }
        return bytes1;
    }

    /**
     * 时间字符串转秒数
     * @param time
     * @return
     * @throws ParseException
     */
    public static long parseTime(String time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
        long timeMs = simpleDateFormat.parse(time).getTime();
        return timeMs/1000;
    }

    public static String parseTime(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
        return simpleDateFormat.format(new Date(time * 1000));
    }

    public static String parseTime(long time, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);//24小时制
        return simpleDateFormat.format(new Date(time * 1000));
    }
    /**
     * 获取当天的零点时间戳
     *
     * @return 当天的零点时间戳
     */
    public static long getTodayStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime();
    }


}
