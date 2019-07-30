package com.suql.devicecollect.print;

public interface PrintService {

    String createQrCodeByMac(String remark, String mac, String sn);

    void print(String path);
}
