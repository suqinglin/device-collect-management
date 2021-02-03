package com.suql.devicecollect;

import com.vians.admin.mapper.NatureMapper;
import com.vians.admin.mapper.ProjectMapper;
import com.vians.admin.mapper.ViansUserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DbTest {

    final static Logger logger = LoggerFactory.getLogger(DbTest.class);

    @Resource
    private ViansUserMapper userMapper;

    @Resource
    private NatureMapper natureMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Test
    public void natureTest() {
//        List<Permission> permissions = userMapper.getNodeTree();
//        permissions.forEach(permission -> {
//            logger.info("=========== permission0 {}", permission.toString());
//            if (permission.getChildren().size() > 0) {
//                permission.getChildren().forEach(permission1 -> {
//                    logger.info("=========== permission1 {}", permission1.toString());
//                    if (permission1.getChildren().size() > 0) {
//                        permission1.getChildren().forEach(permission2 -> {
//                            logger.info("=========== permission2 {}", permission2.toString());
//                        });
//                    }
//                });
//            }
//        });
//        List<NatureInfo> ProjectNatures = natureMapper.getProjectNatures();
//        logger.info("==================ProjectNatures size = {}", ProjectNatures.get(0).getNatureName());
//        List<NatureInfo> CommunityNatures = natureMapper.getCommunityNatures(1);
//        logger.info("==================CommunityNatures size = {}", CommunityNatures.get(0).getNatureName());
//        List<NatureInfo> BuildingNatures = natureMapper.getBuildingNatures(1);
//        logger.info("==================BuildingNatures size = {}", BuildingNatures.get(0).getNatureName());
//        List<NatureInfo> UnitNatures = natureMapper.getUnitNatures(1);
//        logger.info("==================UnitNatures size = {}", UnitNatures.get(0).getNatureName());
//        List<NatureInfo> FloorNatures = natureMapper.getFloorNatures(1);
//        logger.info("==================FloorNatures size = {}", FloorNatures.get(0).getNatureName());
//        List<NatureInfo> RoomNatures = natureMapper.getRoomNatures(1);
//        logger.info("==================RoomNatures size = {}", RoomNatures.get(0).getNatureName());
    }

    @Transactional(rollbackFor = Exception.class)
    @Test
    public void projectTest() {
//         #{projectName}, #{address}, #{natureId}, #{createUserId}, #{createTime}
//        ProjectInfo projectInfo = new ProjectInfo();
//        projectInfo.setProjectName("项目2");
//        projectInfo.setAddress("广东省深圳市龙岗区杨美路31号");
//        projectInfo.setNatureId(1);
//        projectInfo.setCreateUserId(1);
//        projectInfo.setCreateTime(new Date());
//        projectMapper.addProject(projectInfo);
//        List<ProjectInfo> projectInfos = projectMapper.getProjects();
//        logger.info("============================ projects size {}", projectInfos.size());
//        for (int i = 0; i < projectInfos.size(); i++) {
//            logger.info("============================ project {}", projectInfos.get(i).toString());
//        }
    }

    @Test
    public void test3() {
//        UserInfo userInfo = userMapper.getUserByPhone("18580250664");
//        logger.info("userInfo {}", userInfo.toString());
    }


    @Test
    public void test2() {
//        printService = new PrintServiceImpl();
////        String path = printService.createQrCodeByMac("FRM_", "FFFFDC2C28000093", "0000000147");
//        String path = printService.createTestQrCode("https://bll.sf-express.com/SN:0219100002", "SN:0219100002");
//        printService.print(path);
    }
}
