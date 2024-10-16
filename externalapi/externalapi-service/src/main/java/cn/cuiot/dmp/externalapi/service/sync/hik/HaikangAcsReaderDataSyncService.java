package cn.cuiot.dmp.externalapi.service.sync.hik;

import cn.cuiot.dmp.common.bean.external.HIKEntranceGuardBO;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangAcsReaderEntity;
import cn.cuiot.dmp.externalapi.service.service.hik.HaikangAcsReaderService;
import cn.cuiot.dmp.externalapi.service.service.hik.HikCommonHandle;
import cn.cuiot.dmp.externalapi.service.vendor.hik.HikApiFeignService;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req.HikReaderReq;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req.HikReaderStatesReq;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikReaderResp;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikReaderStatesResp;
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
 * 海康门禁设读卡器数据同步服务
 *
 * @author: wuyongchong
 * @date: 2024/10/11 9:48
 */
@Slf4j
@Component
public class HaikangAcsReaderDataSyncService {

    @Autowired
    private HikApiFeignService hikApiFeignService;

    @Autowired
    private HikCommonHandle hikCommonHandle;

    @Autowired
    private HaikangAcsReaderService haikangAcsReaderService;

    /**
     * 转换数据
     */
    public HaikangAcsReaderEntity convertDataItemToEntity(HikReaderResp.Resource item) {
        HaikangAcsReaderEntity haikangAcsReaderEntity = new HaikangAcsReaderEntity();

        haikangAcsReaderEntity.setResourceType(item.getResourceType());
        haikangAcsReaderEntity.setIndexCode(item.getIndexCode());
        haikangAcsReaderEntity.setName(item.getName());
        haikangAcsReaderEntity.setRegionIndexCode(item.getRegionIndexCode());
        haikangAcsReaderEntity.setRegionPath(item.getRegionPath());
        haikangAcsReaderEntity.setIp(item.getIp());
        haikangAcsReaderEntity.setPort(item.getPort());
        haikangAcsReaderEntity.setDeviceCode(item.getDeviceCode());
        haikangAcsReaderEntity.setDeviceKey(item.getDeviceKey());
        haikangAcsReaderEntity.setDeviceModel(item.getDeviceModel());
        haikangAcsReaderEntity.setDeviceType(item.getDeviceType());
        haikangAcsReaderEntity.setDataVersion(item.getDataVersion());
        haikangAcsReaderEntity.setNetZoneId(item.getNetZoneId());
        haikangAcsReaderEntity.setDeployId(item.getDeployId());
        haikangAcsReaderEntity.setCommunicationMode(item.getCommunicationMode());
        haikangAcsReaderEntity.setParentIndexCode(item.getParentIndexCode());
        haikangAcsReaderEntity.setChannelIndexCode(item.getChannelIndexCode());
        haikangAcsReaderEntity.setSort(item.getSort());
        haikangAcsReaderEntity.setCapability(item.getCapability());
        haikangAcsReaderEntity.setComId(item.getComId());
        haikangAcsReaderEntity.setDoorNo(item.getDoorNo());
        haikangAcsReaderEntity.setAcsReaderCardCapacity(
                StringUtils.isNotBlank(item.getAcsReaderCardCapacity()) ? Integer.valueOf(
                        item.getAcsReaderCardCapacity()) : null);
        haikangAcsReaderEntity.setAcsReaderFingerCapacity(
                StringUtils.isNotBlank(item.getAcsReaderFingerCapacity()) ? Integer.valueOf(
                        item.getAcsReaderFingerCapacity()) : null);
        haikangAcsReaderEntity.setAcsReaderFaceCapacity(
                StringUtils.isNotBlank(item.getAcsReaderFaceCapacity()) ? Integer.valueOf(
                        item.getAcsReaderFaceCapacity()) : null);
        haikangAcsReaderEntity.setRegionPathName(item.getRegionPathName());
        haikangAcsReaderEntity.setRegionName(item.getRegionName());
        haikangAcsReaderEntity.setDescription(item.getDescription());

        if (StringUtils.isNotBlank(item.getCreateTime())) {
            haikangAcsReaderEntity.setCreateTime(DateTimeUtil.localDateTimeToDate(
                    LocalDateTime.parse(item.getCreateTime(), DateTimeFormatter.ISO_DATE_TIME)));
        }
        if (StringUtils.isNotBlank(item.getUpdateTime())) {
            haikangAcsReaderEntity.setUpdateTime(DateTimeUtil.localDateTimeToDate(
                    LocalDateTime.parse(item.getUpdateTime(),
                            DateTimeFormatter.ISO_DATE_TIME)));
        }

        //haikangAcsReaderEntity.setStatus(EntityConstants.NO);
        //haikangAcsReaderEntity.setCollectTime();
        //haikangAcsReaderEntity.setDeleted();
        //haikangAcsReaderEntity.setDataTime();

        return haikangAcsReaderEntity;
    }

    /**
     * 门禁读卡器数据全量同步
     */
    public void haikangAcsReaderFullDataSync(Long companyId) {
        HIKEntranceGuardBO hikEntranceGuardBO = hikCommonHandle.queryHikConfigByPlatfromInfo(
                companyId);
        AtomicLong pageNo = new AtomicLong(1);
        Integer size = 0;
        do {
            HikReaderReq req = new HikReaderReq();
            req.setPageNo(pageNo.getAndAdd(1));
            req.setPageSize(1000L);
            HikReaderResp resp = hikApiFeignService.queryReaderSearch(req,
                    hikEntranceGuardBO);
            List<HikReaderResp.Resource> list = resp.getList();
            size = list.size();
            if (CollectionUtils.isNotEmpty(list)) {
                List<HaikangAcsReaderEntity> entityList = Lists.newArrayList();
                for (HikReaderResp.Resource item : list) {
                    HaikangAcsReaderEntity entity = convertDataItemToEntity(item);
                    entity.setOrgId(companyId);
                    entity.setDataTime(new Date());
                    entityList.add(entity);
                }
                if (CollectionUtils.isNotEmpty(entityList)) {
                    haikangAcsReaderService.saveToDB(entityList);
                }
            }
        } while (size > 0);
    }

    /**
     * 门禁读卡器数据增量同步
     */
    public void hikAcsReaderIncrementDataSync(Long companyId) {
        HIKEntranceGuardBO hikEntranceGuardBO = hikCommonHandle.queryHikConfigByPlatfromInfo(
                companyId);
        AtomicLong pageNo = new AtomicLong(1);
        Integer size = 0;
        do {
            HikReaderReq req = new HikReaderReq();
            req.setPageNo(pageNo.getAndAdd(1));
            req.setPageSize(1000L);
            req.setStartTime(
                    LocalDateTime.now().minusHours(47).format(DateTimeFormatter.ISO_DATE_TIME));

            HikReaderResp resp = hikApiFeignService.queryReaderByTimeRange(req,
                    hikEntranceGuardBO);
            List<HikReaderResp.Resource> list = resp.getList();
            size = list.size();
            if (CollectionUtils.isNotEmpty(list)) {
                List<HaikangAcsReaderEntity> entityList = Lists.newArrayList();
                for (HikReaderResp.Resource item : list) {
                    HaikangAcsReaderEntity entity = convertDataItemToEntity(item);
                    entity.setOrgId(companyId);
                    entity.setDataTime(new Date());
                    entityList.add(entity);
                }
                if (CollectionUtils.isNotEmpty(entityList)) {
                    haikangAcsReaderService.saveToDB(entityList);
                }
            }
        } while (size > 0);
    }

    /**
     * 门禁读卡器在线状态同步
     */
    public void hikAcsReaderOnlineStatusSync(Long companyId) {
        HIKEntranceGuardBO hikEntranceGuardBO = hikCommonHandle.queryHikConfigByPlatfromInfo(
                companyId);
        AtomicLong pageNo = new AtomicLong(1);
        Integer size = 0;
        do {
            HikReaderStatesReq req = new HikReaderStatesReq();
            req.setPageNo(pageNo.getAndAdd(1));
            req.setPageSize(1000L);

            HikReaderStatesResp resp = hikApiFeignService.queryReaderStates(req,
                    hikEntranceGuardBO);
            List<HikReaderStatesResp.DtaItem> list = resp.getList();
            size = list.size();
            if (CollectionUtils.isNotEmpty(list)) {
                for (HikReaderStatesResp.DtaItem item : list) {
                    Date collectTime = null;
                    if (StringUtils.isNotBlank(item.getCollectTime())) {
                        collectTime = DateTimeUtil.localDateTimeToDate(
                                LocalDateTime.parse(item.getCollectTime(),
                                        DateTimeFormatter.ISO_DATE_TIME));
                    }
                    Byte status = null;
                    if (Objects.nonNull(item.getOnline())) {
                        status = item.getOnline().byteValue();
                    }
                    haikangAcsReaderService.updateOnlineStatus(companyId,item.getIndexCode(), collectTime,
                            status);
                }
            }
        } while (size > 0);
    }

}
