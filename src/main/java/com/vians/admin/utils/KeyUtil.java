package com.vians.admin.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 专门用于计算key的工具
 */
public class KeyUtil {

    /**
     * 1. 将pwd的UTF-16LE编码的整形得到md5
     * 2. 在以上md5前面拼接上token
     * 3. 对以上拼接的字符串进行md5便得到KeyL
     * @param token
     * @param psw
     * @return
     */
    public static String getKeyL(String token, String psw) {
        return DigestUtils.md5Hex(token + Md5Util.toMd5(psw)).toUpperCase();
    }

    /**
     * token+pwd+cnt，这里简称tpc，将pwd的UTF-16LE编码的整形得到md5、Token 和 CNT 拼接而成的字符串取前 32 位 MD5 的结果，
     * 将拼接的字符串进行MD5运算，并截取前8个字符（即4个字节），最后在前面拼接上cnt字符串得到AppKey
     * @param token
     * @param psw
     * @param cnt
     * @return
     */
    public static String getAppKey(String token, String psw, long cnt) {
        // 将cnt转成8位hex字符串
        String cntStr = String.format("%08X", cnt);
        String tpcMd5 = DigestUtils.md5Hex(token + Md5Util.toMd5(psw) + cntStr).toUpperCase();
        return cntStr + tpcMd5.substring(0, 8);
    }

    /**
     * 对传输的敏感信息进行加密
     *
     * @return
     */
//    public static String encode2HashXOR(String origin, String token, String psw, int cnt) {
//        String originHex = CommUtil.bytes2HexString(origin.getBytes());
//        // 将cnt转成hex字符串
//        String cntStr = String.format("%08X", cnt);
//        String pswMd5 = Md5Util.toMd5(psw);
//        // tpc进行两次MD5运算？为什么是两次
//        String tpcMd5 = DigestUtils.md5Hex(token + pswMd5 + cntStr).toUpperCase();
//        tpcMd5 = DigestUtils.md5Hex(tpcMd5).toUpperCase();
//        // tpcMd5取与源数据长度相同，并与源数据异或运算
//        return CommUtil.bytes2HexString(
//                CommUtil.bytesXorBytes(
//                        origin.getBytes(),
//                        CommUtil.hexStringToBytes(tpcMd5.substring(0, originHex.length())))
//        );
//    }

    /**
     * 对传输的敏感信息进行加密
     *
     * @return
     */
    public static String encode2HashXOR(byte[] origin, String token, String psw, long cnt) {
        String hash = getHashKey(token, psw, cnt, origin.length * 2);
        System.out.println("hash=" + hash);
        // tpcMd5取与源数据长度相同，并与源数据异或运算
        return CommUtil.bytes2HexString(
                CommUtil.bytesXorBytes(
                        origin,
                        CommUtil.hexStringToBytes(hash))
        );
    }

    public static void main(String[] args) {
        byte[] origin = "82DA39F4A69C3E75F3947C3B06E375A7".getBytes();
//        byte[] origin = CommUtil.hexStringToBytes("82DA39F4A69C3E75F3947C3B06E375A7");
        String token = "82DA39F4A69C3E75F3947C3B06E375A7";
        String password = "123456";
        int cnt = 0x53E492A3;
        System.out.println("encode2HashXOR1=" + encode2HashXOR(origin, token, password, cnt));
//        System.out.println(CommUtil.hexStringToBytes("AA708C4100000000").length);
//        System.out.println("encode2HashXOR1=" + encode2HashXOR(CommUtil.hexStringToBytes("AA708C4100000000"), "82DA39F4A69C3E75F3947C3B06E375A7", "123456", 0x53E492A3));
//        System.out.println("123456".length());
//        System.out.println("123456".getBytes().length);
    }

    public static String getHashKey(String token, String psw, long CNT, int len) {
        // 将CNT转成hex字符串
        String cntStr = String.format("%08X", CNT);
        String pswMd5 = Md5Util.toMd5(psw);
        String key = token + pswMd5 + cntStr;
        // 每一段16位，cnt为要计算的段数
        int cnt = len/16 + (len % 16 > 0 ? 1 : 0);
        String md5Key0 = DigestUtils.md5Hex(key).toUpperCase();
        // 用于拼接一次次的md5，用于最终异或运算
        StringBuilder keyBB = new StringBuilder();
        // 用于存放已经被加密过的md5段，以便于下次需要反复加密时直接取出
        List<String> keyList = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            String keyI;
            // 第一段用原md5计算，后面的用上一段计算
            if (i == 0) {
                keyI =DigestUtils.md5Hex(md5Key0).toUpperCase();
            } else {
                keyI = DigestUtils.md5Hex(keyList.get(i - 1)).toUpperCase();
            }
            keyList.add(keyI);
            // 最后一段超过len长度的不要
            if (i == cnt - 1 && len % 16 != 0) {
                keyBB.append(keyI, 0, len % 16);
            } else {
                keyBB.append(keyI);
            }
        }
        return keyBB.toString();
    }
}
