package com.suql.devicecollect.utils;

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
}
