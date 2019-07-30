package com.suql.devicecollect.print;

import org.springframework.stereotype.Service;

import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class PrintServiceImpl implements PrintService {
    @Override
    public String createQrCodeByMac(String remark, String mac, String sn) {
        String realPath = "D:\\suql\\device_qr_code\\" ;
        // TODO 二维码带上APP下载链接
        String downloadUrl = "http://downapp.xeiot.com/sn/";
        String msg = String.valueOf(System.currentTimeMillis()) + ".png";

        CodeModel info = new CodeModel();
        info.setContents(downloadUrl + remark + "#" + mac + "#" + sn);
        info.setWidth(150);
        info.setHeight(150);
        info.setFontSize(18);
        info.setDesc("SN:" + sn);
        info.setDate("");
        info.setLogoFile(null);
        QR_Code code = new QR_Code();
        code.createCodeImage(info, realPath + msg);
        return realPath + msg;
    }

    public String createTestQrCode(String data, String text) {
        String realPath = "D:\\suql\\device_qr_code\\" ;
        String msg = System.currentTimeMillis() + ".png";

        CodeModel info = new CodeModel();
        info.setContents(data);
        info.setWidth(150);
        info.setHeight(150);
        info.setFontSize(18);
        info.setDesc(text);
        info.setDate("");
        info.setLogoFile(null);
        QR_Code code = new QR_Code();
        code.createCodeImage(info, realPath + msg);
        return realPath + msg;
    }

    @Override
    public void print(String path) {
        try {
            DocFlavor dof = DocFlavor.INPUT_STREAM.PNG;
            // 获取默认打印机
            javax.print.PrintService ps = PrintServiceLookup.lookupDefaultPrintService();

            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            //          pras.add(OrientationRequested.PORTRAIT);
            //          pras.add(PrintQuality.HIGH);
            pras.add(new Copies(1));
            pras.add(MediaSizeName.ISO_A10); // 设置打印的纸张

            DocAttributeSet das = new HashDocAttributeSet();
            das.add(new MediaPrintableArea(0, 0, 20, 20, MediaPrintableArea.MM));
            FileInputStream fin = new FileInputStream(path);
            Doc doc = new SimpleDoc(fin, dof, das);
            DocPrintJob job = ps.createPrintJob();

            job.print(doc, pras);
            fin.close();
        } catch (IOException | PrintException ie) {
            ie.printStackTrace();
        }
    }
}
