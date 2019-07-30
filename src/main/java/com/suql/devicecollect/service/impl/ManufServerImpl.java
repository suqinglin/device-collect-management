package com.suql.devicecollect.service.impl;

import com.suql.devicecollect.mapper.ManufMapper;
import com.suql.devicecollect.model.ManufInfo;
import com.suql.devicecollect.service.ManufServer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ManufServerImpl implements ManufServer {

    @Resource
    ManufMapper manufMapper;
    @Override
    public void saveManuf(String name, String address, long createUserId) {
        long id = 0;
        if (manufMapper.getMaxId() != null) {
            id = Long.valueOf(manufMapper.getMaxId()) + 1;
        }
        ManufInfo manufInfo = new ManufInfo();
        manufInfo.setId(id);
        manufInfo.setName(name);
        manufInfo.setAddress(address);
        manufInfo.setCreateUserId(createUserId);
        manufInfo.setCreateTime(System.currentTimeMillis() / 1000);
        manufInfo.setState(1);
    }

    @Override
    public List<ManufInfo> getManufList() {
        return manufMapper.getManufList();
    }
}
