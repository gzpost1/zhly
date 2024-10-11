package cn.cuiot.dmp.externalapi.service.sync.hik;

import cn.cuiot.dmp.common.bean.external.HIKEntranceGuardBO;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangAcsDoorEntity;
import cn.cuiot.dmp.externalapi.service.service.hik.HaikangAcsDoorService;
import cn.cuiot.dmp.externalapi.service.service.hik.HikCommonHandle;
import cn.cuiot.dmp.externalapi.service.vendor.hik.HikApiFeignService;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req.HikDoorReq;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req.HikDoorStatesReq;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikDoorResp;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikDoorStatesResp;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikDoorStatesResp.AuthDoor;
import com.google.common.collect.Lists;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 海康门禁点数据同步服务
 *
 * @author: wuyongchong
 * @date: 2024/10/11 9:48
 */
@Slf4j
@Component
public class HaikangAcsDoorDataSyncService {

    @Autowired
    private HikApiFeignService hikApiFeignService;

    @Autowired
    private HikCommonHandle hikCommonHandle;

    @Autowired
    private HaikangAcsDoorService haikangAcsDoorService;

    /**
     * 转换数据
     */
    private HaikangAcsDoorEntity convertDataItemToEntity(HikDoorResp.DataItem item) {
        HaikangAcsDoorEntity haikangAcsDoorEntity = new HaikangAcsDoorEntity();
        haikangAcsDoorEntity.setIndexCode(item.getIndexCode());
        haikangAcsDoorEntity.setResourceType(item.getResourceType());
        haikangAcsDoorEntity.setName(item.getName());
        haikangAcsDoorEntity.setDoorNo(item.getDoorNo());
        haikangAcsDoorEntity.setChannelNo(item.getChannelNo());
        haikangAcsDoorEntity.setParentIndexCode(item.getParentIndexCode());
        haikangAcsDoorEntity.setControlOneId(item.getControlOneId());
        haikangAcsDoorEntity.setControlTwoId(item.getControlTwoId());
        haikangAcsDoorEntity.setReaderInId(item.getReaderInId());
        haikangAcsDoorEntity.setReaderOutId(item.getReaderOutId());
        haikangAcsDoorEntity.setDoorSerial(String.valueOf(item.getDoorSerial()));
        haikangAcsDoorEntity.setTreatyType(item.getTreatyType());
        haikangAcsDoorEntity.setRegionIndexCode(item.getRegionIndexCode());
        haikangAcsDoorEntity.setRegionPath(item.getRegionPath());
        haikangAcsDoorEntity.setDescription(item.getDescription());
        haikangAcsDoorEntity.setChannelType(item.getChannelType());
        haikangAcsDoorEntity.setRegionName(item.getRegionName());
        haikangAcsDoorEntity.setRegionPathName(item.getRegionPathName());
        haikangAcsDoorEntity.setInstallLocation(item.getInstallLocation());

        if (StringUtils.isNotBlank(item.getCreateTime())) {
            haikangAcsDoorEntity.setCreateTime(DateTimeUtil.localDateTimeToDate(
                    LocalDateTime.parse(item.getCreateTime(), DateTimeFormatter.ISO_DATE_TIME)));
        }
        if (StringUtils.isNotBlank(item.getUpdateTime())) {
            haikangAcsDoorEntity.setUpdateTime(DateTimeUtil.localDateTimeToDate(
                    LocalDateTime.parse(item.getUpdateTime(),
                            DateTimeFormatter.ISO_DATE_TIME)));
        }

        //haikangAcsDoorEntity.setDoorState();
        //haikangAcsDoorEntity.setAuthState();
        //haikangAcsDoorEntity.setDeleted();
        //haikangAcsDoorEntity.setDataTime();

        return haikangAcsDoorEntity;

    }

    /**
     * 门禁点数据全量同步
     */
    public void haikangAcsDoorFullDataSync(Long companyId) {
        HIKEntranceGuardBO hikEntranceGuardBO = hikCommonHandle.queryHikConfigByPlatfromInfo(
                companyId);
        AtomicLong pageNo = new AtomicLong(1);
        Integer size = 0;
        do {
            HikDoorReq req = new HikDoorReq();
            req.setPageNo(pageNo.getAndAdd(1));
            req.setPageSize(1000L);
            HikDoorResp resp = hikApiFeignService.queryDoorSearch(req,
                    hikEntranceGuardBO);
            List<HikDoorResp.DataItem> list = resp.getList();
            size = list.size();
            if (CollectionUtils.isNotEmpty(list)) {
                List<HaikangAcsDoorEntity> entityList = Lists.newArrayList();
                for (HikDoorResp.DataItem item : list) {
                    HaikangAcsDoorEntity entity = convertDataItemToEntity(item);
                    entity.setOrgId(companyId);
                    entity.setDataTime(new Date());
                    entityList.add(entity);
                }
                if (CollectionUtils.isNotEmpty(entityList)) {
                    haikangAcsDoorService.saveToDB(entityList);
                }
            }
        } while (size > 0);
    }

    /**
     * 门禁点数据增量同步
     */
    public void hikAcsDoorIncrementDataSync(Long companyId) {
        HIKEntranceGuardBO hikEntranceGuardBO = hikCommonHandle.queryHikConfigByPlatfromInfo(
                companyId);
        AtomicLong pageNo = new AtomicLong(1);
        Integer size = 0;
        do {
            HikDoorReq req = new HikDoorReq();
            req.setPageNo(pageNo.getAndAdd(1));
            req.setPageSize(1000L);
            req.setStartTime(
                    LocalDateTime.now().minusHours(47).format(DateTimeFormatter.ISO_DATE_TIME));

            HikDoorResp resp = hikApiFeignService.queryDoorByTimeRange(req,
                    hikEntranceGuardBO);
            List<HikDoorResp.DataItem> list = resp.getList();
            size = list.size();
            if (CollectionUtils.isNotEmpty(list)) {
                List<HaikangAcsDoorEntity> entityList = Lists.newArrayList();
                for (HikDoorResp.DataItem item : list) {
                    HaikangAcsDoorEntity entity = convertDataItemToEntity(item);
                    entity.setOrgId(companyId);
                    entity.setDataTime(new Date());
                    entityList.add(entity);
                }
                if (CollectionUtils.isNotEmpty(entityList)) {
                    haikangAcsDoorService.saveToDB(entityList);
                }
            }
        } while (size > 0);
    }

    /**
     * 门禁点状态同步
     */
    public void hikAcsDoorStateSync(Long companyId) {
        HIKEntranceGuardBO hikEntranceGuardBO = hikCommonHandle.queryHikConfigByPlatfromInfo(
                companyId);

        HikDoorStatesReq req = new HikDoorStatesReq();

        HikDoorStatesResp resp = hikApiFeignService.queryDoorStates(req,
                hikEntranceGuardBO);

        List<AuthDoor> authDoorList = resp.getAuthDoorList();
        if (CollectionUtils.isNotEmpty(authDoorList)) {
            for (AuthDoor authDoor : authDoorList) {
                Byte doorState = null;
                if (Objects.nonNull(authDoor.getDoorState())) {
                    doorState = authDoor.getDoorState().byteValue();
                }
                haikangAcsDoorService.updateDoorState(authDoor.getDoorIndexCode(), doorState,
                        EntityConstants.YES);
            }
        }
        List<String> noAuthDoorIndexCodeList = resp.getNoAuthDoorIndexCodeList();
        if (CollectionUtils.isNotEmpty(noAuthDoorIndexCodeList)) {
            for (String doorIndexCode : noAuthDoorIndexCodeList) {
                haikangAcsDoorService.updateDoorState(doorIndexCode, null,
                        EntityConstants.NO);
            }
        }
    }

}
