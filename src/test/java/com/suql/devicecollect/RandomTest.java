package com.suql.devicecollect;

import com.vians.admin.utils.DoNetUnicodeUtils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class RandomTest {

    final static Logger logger = LoggerFactory.getLogger(RandomTest.class);

    @Test
    public void test() {

        BigInteger maxMacNum = new BigInteger("18446704680940601491");
        logger.info("random {}", String.format("%016X", maxMacNum));
    }

    @Test
    public void cmdEncrypt() {
        String ss = "PrdClr";
        String result = Hex.encodeHexString(sendCommandEncrypt(ss));
        logger.info("result {}", result);
    }

    @Test
    public void test2() throws DecoderException {
//        byte[] bytes = Hex.decodeHex("4E4C5F464C4C32324100000000".replace("00", ""));
//        logger.info("model {}", new String(bytes));
//        String aa = "3a343039362c4d611f75666163747572653a3131";
        String aa = "3a343039362c4d616e75666163747572653a31310d";
//        ":4096,Ma\u001Fufacture:11"
        byte[] bb = Hex.decodeHex(aa);
        logger.info("result {}", new BigInteger("23", 16));

    }

    @Test
    public void test3() {

        String pwd = "123456";
        byte[] passPhrase = DoNetUnicodeUtils.toUnicodeMC(pwd);
        String ePwd = DigestUtils.md5Hex(pwd).toUpperCase();
        logger.info("result {}", ePwd);
    }

    @Test
    public void test4() {

        System.out.println("----------------" + String.format("%010d", Integer.valueOf("945")));
    }

    public static byte[] sendCommandEncrypt(String cmd) {
//        cmd = cmd + " ";
        byte[] buf = cmd.getBytes(StandardCharsets.US_ASCII);
        byte[] endBuf = new byte[]{0x0D};
        return ByteBuffer.allocate(buf.length + 1)
                .put(buf)
                .put(endBuf)
                .array();
    }
}
