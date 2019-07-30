package com.suql.devicecollect.service;

import com.suql.devicecollect.model.ManufInfo;

import java.util.List;

public interface ManufServer {

    void saveManuf(String name, String address, long createUserId);

    List<ManufInfo> getManufList();
}
