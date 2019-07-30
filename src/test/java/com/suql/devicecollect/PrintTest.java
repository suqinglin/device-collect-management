package com.suql.devicecollect;

import com.suql.devicecollect.print.PrintServiceImpl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintTest {

    private PrintServiceImpl printService;
    static Logger logger = LoggerFactory.getLogger(RandomTest.class);

    @Test
    public void printQR() {
        printService = new PrintServiceImpl();
//        String path = printService.createQrCodeByMac("FRM_", "FFFFDC2C28000048", "000000072");
        String path = printService.createTestQrCode("GWF_#76A47AD99493", "GWF_#76A47AD99493");
        printService.print(path);
    }

    @Test
    public void createQR() {
        printService = new PrintServiceImpl();
        int startSN = 119200010;
        for (int i = 0; i < 21; i++) {
            String sn = String.format("%010d", startSN + i);
            logger.info("sn {}", sn);
            String path = printService.createTestQrCode("SN:" + sn, "SN:" + sn);
            logger.info("path {}", path);
        }
    }
}
