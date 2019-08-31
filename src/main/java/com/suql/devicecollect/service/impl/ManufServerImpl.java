package com.suql.devicecollect.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.suql.devicecollect.mapper.ManufMapper;
import com.suql.devicecollect.model.ManufInfo;
import com.suql.devicecollect.response.Page;
import com.suql.devicecollect.response.Pageable;
import com.suql.devicecollect.service.ManufServer;
import com.suql.devicecollect.utils.Md5Util;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.xml.ws.soap.Addressing;
import java.util.List;

@Service
public class ManufServerImpl implements ManufServer {

    @Resource
    ManufMapper manufMapper;
    @Override
    public void saveManuf(String name, String address, long createUserId, String password) {
        long id = 0;
        if (manufMapper.getMaxId() != null) {
            id = Long.valueOf(manufMapper.getMaxId()) + 1;
        }
        ManufInfo manufInfo = new ManufInfo();
        manufInfo.setId(id);
        manufInfo.setName(name);
        manufInfo.setAddress(address);
        manufInfo.setCreateUserId(createUserId);
        manufInfo.setPassword(Md5Util.toMd5(password));
        manufInfo.setCreateTime(System.currentTimeMillis() / 1000);
        manufInfo.setState(1);
        manufMapper.saveManufInfo(manufInfo);
    }

    @Override
    public List<ManufInfo> findAll() {
        return manufMapper.getManufList();
    }

    @Override
    public Page<ManufInfo> findByPage(Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        List<ManufInfo> manufInfoList = manufMapper.getManufList();
        PageInfo<ManufInfo> pageInfo = new PageInfo<>(manufInfoList);
        return new Page<>(manufInfoList, pageInfo.getTotal(), pageable);
    }

    @Override
    public ManufInfo findById(long id) {
        return manufMapper.findById(id);
    }

    @Override
    public void modifyPwd(long id, String newPwd) {
        ManufInfo manufInfo = new ManufInfo();
        manufInfo.setId(id);
        manufInfo.setPassword(Md5Util.toMd5(newPwd));
        manufMapper.modifyPwd(manufInfo);
    }

    @Override
    public void edit(int id, String name, String address) {
        ManufInfo manufInfo = new ManufInfo();
        manufInfo.setId(id);
        if (!StringUtils.isEmpty(name)) {
            manufInfo.setName(name);
        }
        if (!StringUtils.isEmpty(address)) {
            manufInfo.setAddress(address);
        }
        manufInfo.setCreateTime(System.currentTimeMillis());
        manufMapper.edit(manufInfo);
    }

    @Override
    public void deleteAll(List<Integer> manufIds) {
        for (int manufId: manufIds) {
            manufMapper.deleteById(manufId);
        }
    }
}
