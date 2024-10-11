package cn.cuiot.dmp.externalapi.service.sync.hik;

import cn.cuiot.dmp.common.bean.external.HIKEntranceGuardBO;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangAcsDeviceEntity;
import cn.cuiot.dmp.externalapi.service.service.hik.HaikangAcsDeviceService;
import cn.cuiot.dmp.externalapi.service.service.hik.HikCommonHandle;
import cn.cuiot.dmp.externalapi.service.vendor.hik.HikApiFeignService;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req.HikAcsListReq;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req.HikAcsStatesReq;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikAcsListResp;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikAcsStatesResp;
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
 * 海康门禁设备数据同步服务
 *
 * @author: wuyongchong
 * @date: 2024/10/11 9:48
 */
@Slf4j
@Component
public class HaikangAcsDeviceDataSyncService {

    @Autowired
    private HikApiFeignService hikApiFeignService;

    @Autowired
    private HikCommonHandle hikCommonHandle;

    @Autowired
    private HaikangAcsDeviceService haikangAcsDeviceService;

    public HaikangAcsDeviceEntity convertDataItemToEntity(HikAcsListResp.DataItem item) {

        HaikangAcsDeviceEntity haikangAcsDeviceEntity = new HaikangAcsDeviceEntity();

        haikangAcsDeviceEntity.setIndexCode(item.getIndexCode());
        haikangAcsDeviceEntity.setParentIndexCode(item.getParentIndexCode());
        haikangAcsDeviceEntity.setResourceType(item.getResourceType());
        haikangAcsDeviceEntity.setName(item.getName());
        haikangAcsDeviceEntity.setDevTypeCode(item.getDevTypeCode());
        haikangAcsDeviceEntity.setDevTypeDesc(item.getDevTypeDesc());
        haikangAcsDeviceEntity.setDeviceCode(item.getDeviceCode());
        haikangAcsDeviceEntity.setDeviceModel(item.getDeviceModel());
        haikangAcsDeviceEntity.setDeviceType(item.getDeviceType());
        haikangAcsDeviceEntity.setManufacturer(item.getManufacturer());
        haikangAcsDeviceEntity.setRegionIndexCode(item.getRegionIndexCode());
        haikangAcsDeviceEntity.setRegionPath(item.getRegionPath());
        haikangAcsDeviceEntity.setTreatyType(item.getTreatyType());
        haikangAcsDeviceEntity.setCardCapacity(item.getCardCapacity());
        haikangAcsDeviceEntity.setFingerCapacity(item.getFingerCapacity());
        haikangAcsDeviceEntity.setVeinCapacity(item.getVeinCapacity());
        haikangAcsDeviceEntity.setFaceCapacity(item.getFaceCapacity());
        haikangAcsDeviceEntity.setDoorCapacity(item.getDoorCapacity());
        haikangAcsDeviceEntity.setDeployId(item.getDeployId());
        haikangAcsDeviceEntity.setNetZoneId(item.getNetZoneId());
        haikangAcsDeviceEntity.setDescription(item.getDescription());
        haikangAcsDeviceEntity.setAcsReaderVerifyModeAbility(item.getAcsReaderVerifyModeAbility());
        haikangAcsDeviceEntity.setRegionName(item.getRegionName());
        haikangAcsDeviceEntity.setRegionPathName(item.getRegionPathName());
        haikangAcsDeviceEntity.setIp(item.getIp());
        haikangAcsDeviceEntity.setPort(item.getPort());
        haikangAcsDeviceEntity.setCapability(item.getCapability());
        haikangAcsDeviceEntity.setDevSerialNum(item.getDevSerialNum());
        haikangAcsDeviceEntity.setDataVersion(item.getDataVersion());

        if (StringUtils.isNotBlank(item.getCreateTime())) {
            haikangAcsDeviceEntity.setCreateTime(DateTimeUtil.localDateTimeToDate(
                    LocalDateTime.parse(item.getCreateTime(), DateTimeFormatter.ISO_DATE_TIME)));
        }
        if (StringUtils.isNotBlank(item.getUpdateTime())) {
            haikangAcsDeviceEntity.setUpdateTime(DateTimeUtil.localDateTimeToDate(
                    LocalDateTime.parse(item.getUpdateTime(),
                            DateTimeFormatter.ISO_DATE_TIME)));
        }

        //haikangAcsDeviceEntity.setStatus(EntityConstants.NO);
        //haikangAcsDeviceEntity.setCollectTime();
        //haikangAcsDeviceEntity.setDeleted();
        //haikangAcsDeviceEntity.setDataTime();

        return haikangAcsDeviceEntity;

    }

    /**
     * 门禁设备数据全量同步
     */
    public void haikangAcsDeviceFullDataSync(Long companyId) {
        HIKEntranceGuardBO hikEntranceGuardBO = hikCommonHandle.queryHikConfigByPlatfromInfo(
                companyId);
        AtomicLong pageNo = new AtomicLong(1);
        Integer size = 0;
        do {
            HikAcsListReq req = new HikAcsListReq();
            req.setPageNo(pageNo.getAndAdd(1));
            req.setPageSize(1000L);
            HikAcsListResp resp = hikApiFeignService.queryAcsDeviceSearch(req,
                    hikEntranceGuardBO);
            List<HikAcsListResp.DataItem> list = resp.getList();
            size = list.size();
            if (CollectionUtils.isNotEmpty(list)) {
                List<HaikangAcsDeviceEntity> entityList = Lists.newArrayList();
                for (HikAcsListResp.DataItem item : list) {
                    HaikangAcsDeviceEntity entity = convertDataItemToEntity(item);
                    entity.setOrgId(companyId);
                    entity.setDataTime(new Date());
                    entityList.add(entity);
                }
                if (CollectionUtils.isNotEmpty(entityList)) {
                    haikangAcsDeviceService.saveToDB(entityList);
                }
            }
        } while (size > 0);
    }

    /**
     * 门禁设备数据增量同步
     */
    public void hikAcsDeviceIncrementDataSync(Long companyId) {
        HIKEntranceGuardBO hikEntranceGuardBO = hikCommonHandle.queryHikConfigByPlatfromInfo(
                companyId);
        AtomicLong pageNo = new AtomicLong(1);
        Integer size = 0;
        do {
            HikAcsListReq req = new HikAcsListReq();
            req.setPageNo(pageNo.getAndAdd(1));
            req.setPageSize(1000L);
            req.setStartTime(
                    LocalDateTime.now().minusHours(47).format(DateTimeFormatter.ISO_DATE_TIME));

            HikAcsListResp resp = hikApiFeignService.queryAcsDeviceByTimeRange(req,
                    hikEntranceGuardBO);
            List<HikAcsListResp.DataItem> list = resp.getList();
            size = list.size();
            if (CollectionUtils.isNotEmpty(list)) {
                List<HaikangAcsDeviceEntity> entityList = Lists.newArrayList();
                for (HikAcsListResp.DataItem item : list) {
                    HaikangAcsDeviceEntity entity = convertDataItemToEntity(item);
                    if (Objects.nonNull(item.getStatus())) {
                        //小于0则代表资源已被删除
                        if (item.getStatus() < 0) {
                            entity.setDeleted(EntityConstants.DELETED);
                        }
                    }
                    entity.setOrgId(companyId);
                    entity.setDataTime(new Date());
                    entityList.add(entity);
                }
                if (CollectionUtils.isNotEmpty(entityList)) {
                    haikangAcsDeviceService.saveToDB(entityList);
                }
            }
        } while (size > 0);
    }

    /**
     * 门禁设备在线状态同步
     */
    public void hikAcsDeviceOnlineStatusSync(Long companyId) {
        HIKEntranceGuardBO hikEntranceGuardBO = hikCommonHandle.queryHikConfigByPlatfromInfo(
                companyId);
        AtomicLong pageNo = new AtomicLong(1);
        Integer size = 0;
        do {
            HikAcsStatesReq req = new HikAcsStatesReq();
            req.setPageNo(pageNo.getAndAdd(1));
            req.setPageSize(1000L);

            HikAcsStatesResp resp = hikApiFeignService.queryAcsStatus(req,
                    hikEntranceGuardBO);
            List<HikAcsStatesResp.DataItem> list = resp.getList();
            size = list.size();
            if (CollectionUtils.isNotEmpty(list)) {
                for (HikAcsStatesResp.DataItem item : list) {
                    Date collectTime = null;
                    if(StringUtils.isNotBlank(item.getCollectTime())){
                        collectTime = DateTimeUtil.localDateTimeToDate(
                                LocalDateTime.parse(item.getCollectTime(),
                                        DateTimeFormatter.ISO_DATE_TIME));
                    }
                    Byte status = null;
                    if(Objects.nonNull(item.getOnline())){
                        status = item.getOnline().byteValue();
                    }
                    haikangAcsDeviceService.updateOnlineStatus(item.getIndexCode(),collectTime,status);
                }
            }
        } while (size > 0);
    }

}
