package com.vians.admin.utils;

import org.apache.commons.codec.digest.DigestUtils;

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
    public static String getAppKey(String token, String psw, int cnt) {
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
    public static String encode2HashXOR(String origin, String token, String psw, int cnt) {
        String originHex = CommUtil.bytes2HexString(origin.getBytes());
        // 将cnt转成hex字符串
        String cntStr = String.format("%08X", cnt);
        String pswMd5 = Md5Util.toMd5(psw);
        // tpc进行两次MD5运算？为什么是两次
        String tpcMd5 = DigestUtils.md5Hex(token + pswMd5 + cntStr).toUpperCase();
        tpcMd5 = DigestUtils.md5Hex(tpcMd5).toUpperCase();
        // tpcMd5取与源数据长度相同，并与源数据异或运算
        return CommUtil.hexXOR(originHex, tpcMd5.substring(0, originHex.length()));
    }
}
