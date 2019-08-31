package com.suql.devicecollect.service;

import com.suql.devicecollect.model.ManufInfo;
import com.suql.devicecollect.response.Page;
import com.suql.devicecollect.response.Pageable;

import java.util.List;

public interface ManufServer {

    void saveManuf(String name, String address, long createUserId, String password);

    List<ManufInfo> findAll();

    Page<ManufInfo> findByPage(Pageable pageable);

    ManufInfo findById(long id);

    void modifyPwd(long id, String newPwd);

    void edit(int id, String name, String address);

    void deleteAll(List<Integer> manufIds);
}
