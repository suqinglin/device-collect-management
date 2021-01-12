package com.vians.admin.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class Md5Util {

    public static String toMd5(String origin) {
        byte[] passPhrase = DoNetUnicodeUtils.toUnicodeMC(origin);
        return DigestUtils.md5Hex(passPhrase).toUpperCase();
    }



//    public static void main(String[] args) {
//        System.out.println(bytes2HexString(DoNetUnicodeUtils.toUnicodeMC("123456")));
//        byte[] tokenArr = hexStringToBytes("5FD178D8B7444DE292BF58B77D4A1670");
//        byte[] pswArr = hexStringToBytes(toMd5("123456"));
//        System.out.println(bytes2HexString(tokenArr));
//        System.out.println(bytes2HexString(pswArr));
//
//        byte[] message = new byte[tokenArr.length + pswArr.length ];
//        System.arraycopy(tokenArr, 0, message, 0, tokenArr.length);
//        System.arraycopy(pswArr, 0, message, tokenArr.length, pswArr.length);
//        System.out.println(bytes2HexString(message));
//        System.out.println(DigestUtils.md5Hex("5FD178D8B7444DE292BF58B77D4A1670CE0BFD15059B68D67688884D7A3D3E8C".getBytes()).toUpperCase());
////        System.out.println(DigestUtils.md5Hex(tokenArr).toUpperCase());
////        System.out.println(toMd5("82DA39F4A69C3E75F3947C3B06E375A7CE0BFD15059B68D67688884D7A3D3E8C"));
//    }
}
