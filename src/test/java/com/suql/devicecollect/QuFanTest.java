package com.suql.devicecollect;

/**
 * @ClassName QuFanTest
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/4/7 17:41
 * @Version 1.0
 **/
public class QuFanTest {

    public static void main(String[] args) {
        String mac = "DC2C28010936";
        String javaStr = mac.substring(10); //十六进制
        byte [] bytes = parseHexStr2Byte(javaStr);
        byte temp;
        for(int i=0;i<bytes.length;i++){
            temp = bytes[i];
            bytes[i] = (byte) (~temp);
        }
        String bths =  parseByte2HexStr(bytes);
        System.err.println(bths
                + mac.substring(8, 10)
                + mac.substring(6, 8)
                + mac.substring(4, 6)
                + mac.substring(2, 4)
                + mac.substring(0, 2));
    }
    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

}
