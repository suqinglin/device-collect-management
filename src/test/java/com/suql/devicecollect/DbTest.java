package com.suql.devicecollect;

import com.suql.devicecollect.mapper.DeviceDescribeMapper;
import com.suql.devicecollect.model.DeviceDescribeInfo;
import com.suql.devicecollect.print.PrintService;
import com.suql.devicecollect.print.PrintServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DbTest {

    final static Logger logger = LoggerFactory.getLogger(DbTest.class);

    @Resource
    private DeviceDescribeMapper deviceDescribeMapper;

//    @Resource
    private PrintServiceImpl printService;

    @Test
    public void test1() {
        DeviceDescribeInfo deviceDescribeInfo = deviceDescribeMapper.getDeviceDescribeInfoByDescribe("NL_GWF01A");
        logger.info("deviceDescribeInfo {}", deviceDescribeInfo.toString());
    }

    @Test
    public void test2() {
        printService = new PrintServiceImpl();
//        String path = printService.createQrCodeByMac("FRM_", "FFFFDC2C28000093", "0000000147");
        String path = printService.createTestQrCode("https://bll.sf-express.com/SN:0219100002", "SN:0219100002");
        printService.print(path);
    }
}
