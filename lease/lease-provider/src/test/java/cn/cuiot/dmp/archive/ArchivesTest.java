package cn.cuiot.dmp.archive;

import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.IdsReq;
import cn.cuiot.dmp.base.infrastructure.feign.ArchiveFeignService;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.base.infrastructure.model.HousesArchivesVo;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.lease.LeaseApplication;
import cn.cuiot.dmp.lease.service.TbContractBindInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static cn.cuiot.dmp.lease.service.BaseContractService.CONTRACT_INTENTION_TYPE;


/**
 * @author liujianyu
 * @description 档案测试类
 * @since 2024-05-15 14:34
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LeaseApplication.class)
public class ArchivesTest {
    @Autowired
    ArchiveFeignService archiveFeignService;
    @Autowired
    TbContractBindInfoService bindInfoService;


    @Test
    public void test(){
        BuildingArchiveReq buildingArchiveReq = new BuildingArchiveReq();
        ArrayList<Long> ids = Lists.newArrayList();
//        ids.add(1793265070438080513L);
        ids.add(1793816238359584769L);

        buildingArchiveReq.setIdList(ids);
        IdsReq idsReq = new IdsReq();
        idsReq.setIds(ids);
        IdmResDTO<List<BuildingArchive>> listIdmResDTO = archiveFeignService.buildingArchiveQueryForList(buildingArchiveReq);
        IdmResDTO<List<HousesArchivesVo>> idmResDTO = archiveFeignService.queryHousesList(idsReq);

        System.out.println(listIdmResDTO);
        System.out.println(idmResDTO);

    }

    @Test
    public void test2(){
        List<HousesArchivesVo> contractBindInfo = bindInfoService.queryBindHouseInfoByContractId(1L, CONTRACT_INTENTION_TYPE);
        System.out.println(contractBindInfo);
    }

}
