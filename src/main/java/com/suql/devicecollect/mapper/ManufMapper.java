package com.suql.devicecollect.mapper;

import com.suql.devicecollect.model.ManufInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ManufMapper {

    void saveManufInfo(ManufInfo manufInfo);

    String getMaxId();

    List<ManufInfo> getManufList();

    ManufInfo findById(long id);

    void edit(ManufInfo manufInfo);

    void modifyPwd(ManufInfo manufInfo);

    void deleteById(long id);
}
