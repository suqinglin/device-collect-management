package com.suql.devicecollect;

import com.vians.admin.utils.CommUtil;
import com.vians.admin.utils.DoNetUnicodeUtils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
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

        BigInteger macNum = new BigInteger("FFFFDC2C2801050D", 16);

        logger.info("macNum {}", macNum);
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

//        System.out.println("----------------" + String.format("%010d", Integer.valueOf("945")));
//        String str = "jauPwHy/mb9PaylAEp5Lv5qNTz9ucM1ABLc8wKoMsb/ns5nAsrMgQC4sJ0D+80/Ab/2/P+nRgMCANxC9KzEPvx4Xz8C9HZdARrZbwAgtvEACnUvAIFBqQJ+w4cDdfPO/SEkyQG9rSsBHZyJA1UW0QKrCIkCt3knAcm2PwJNtd8BOpZdApkoswDCYG0BsnQlAKKJ1wGNCw78AfSPAA7YEP+otH8BsYrI/nLAxwA4DLj/MVA/AQKUlwOx0O0AP5hnAqMO1v90KjEDlOphAwmOkvrddTT+1VpRAj4JLQP/4i77lA64/gfB/wAgEUUBSwPm/aFcGvtu6CUDeXBe/dm2fPwiqJUA4423A52CswJjrzT68mpW/kLEDwN3eLMChKKRArcVUPxblJsCJbtjAS4mXvxLI6j/eVl7Az9qMQAxN7D+8uIxAmMwWwebAkUBVOsJAEsuDQF3q1L9aZsK+kO8yv7B5Yb+j/RZA+piZwMn8gcD5nts/P9gCQJx1DEDYuwc/ZTo9wPVwAsCOAOO/liCuwFPey77Vg19A+p2jP4WRO7/ej5bAvXMPQJbnc8BghsA+RgBTwIQ/MUG6dQ/AY7rlPuMBar4IlQpAunmMP/j65r84kee+qpn2vt5dh8B2rj8/mpBQv/KmGcCo1cY/EhUYwE6CRkDW5bhAWcreQBGYhD+Bw4i/IKZ/v3uJSr6ldBrBMZ17wAoJvsAC2jdAU8ZPvxtKJUCmgwrAFGLCwDhz87/gJ4/AuzWEv5OBocDcaeO+7KIbQAzLZcDMlxTA9o2EwDZmyz4G1G4/E75yP/f4AsDrhKVAR74fv6fXUECzue6/cboHP5yC3T/I+Ty/iJgaQPhp9T8fBVjABtCDQOIWlMDC6HW/1+pfP/Wmo8C4Z9q/QUYJwMlLQMBPK+u/RMJ7wLxZUz/y0GDA5c1UQBw69D+4C2s9X/7Fv8NDzL/y+9g/okAvPsiKmEAciS5AWRdBwP4uD8AOntK/UMWPvpvsR78Mu1E/TMkSQCWUiMCY6hTA";
        String str = "jauPwHy/mb9PaylAEp5Lv5qNTz9ucM1ABLc8wKoMsb/ns5nAsrMgQC4sJ0D+80/Ab/2/P+nRgMCANxC9KzEPvx4Xz8C9HZdARrZbwAgtvEACnUvAIFBqQJ+w4cDdfPO/SEkyQG9rSsBHZyJA1UW0QKrCIkCt3knAcm2PwJNtd8BOpZdApkoswDCYG0BsnQlAKKJ1wGNCw78AfSPAA7YEP+otH8BsYrI/nLAxwA4DLj/MVA/AQKUlwOx0O0AP5hnAqMO1v90KjEDlOphAwmOkvrddTT+1VpRAj4JLQP/4i77lA64/gfB/wAgEUUBSwPm/aFcGvtu6CUDeXBe/dm2fPwiqJUA4423A52CswJjrzT68mpW/kLEDwN3eLMChKKRArcVUPxblJsCJbtjAS4mXvxLI6j/eVl7Az9qMQAxN7D+8uIxAmMwWwebAkUBVOsJAEsuDQF3q1L9aZsK+kO8yv7B5Yb+j/RZA+piZwMn8gcD5nts/P9gCQJx1DEDYuwc/ZTo9wPVwAsCOAOO/liCuwFPey77Vg19A+p2jP4WRO7/ej5bAvXMPQJbnc8BghsA+RgBTwIQ/MUG6dQ/AY7rlPuMBar4IlQpAunmMP/j65r84kee+qpn2vt5dh8B2rj8/mpBQv/KmGcCo1cY/EhUYwE6CRkDW5bhAWcreQBGYhD+Bw4i/IKZ/v3uJSr6ldBrBMZ17wAoJvsAC2jdAU8ZPvxtKJUCmgwrAFGLCwDhz87/gJ4/AuzWEv5OBocDcaeO+7KIbQAzLZcDMlxTA9o2EwDZmyz4G1G4/E75yP/f4AsDrhKVAR74fv6fXUECzue6/cboHP5yC3T/I+Ty/iJgaQPhp9T8fBVjABtCDQOIWlMDC6HW/1+pfP/Wmo8C4Z9q/QUYJwMlLQMBPK+u/RMJ7wLxZUz/y0GDA5c1UQBw69D+4C2s9X/7Fv8NDzL/y+9g/okAvPsiKmEAciS5AWRdBwP4uD8AOntK/UMWPvpvsR78Mu1E/TMkSQCWUiMCY6hTA";
        byte[] decoded = Base64.decodeBase64(str.getBytes());
        System.out.println("length=" + decoded.length + ", result=" + CommUtil.bytes2HexString(decoded));
        String hexResult = CommUtil.bytes2HexString(decoded);
        StringBuilder xresult = new StringBuilder();
        for (int i = 0; i < decoded.length; i++) {
            xresult.append(",0x").append(hexResult, i * 2, i * 2 + 2);
        }
        System.out.println(xresult.toString());
        System.out.println(new String(Base64.encodeBase64(decoded)));
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
