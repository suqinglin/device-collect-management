package com.suql.devicecollect.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class Md5Util {

    public static String toMd5(String origin) {
        byte[] passPhrase = DoNetUnicodeUtils.toUnicodeMC(origin);
        return DigestUtils.md5Hex(passPhrase).toUpperCase();
    }
}
