package cn.cuiot.dmp.externalapi.service.service.gw.waterleachalarm;

import cn.cuiot.dmp.common.bean.external.GWCurrencyBO;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.enums.FootPlateInfoEnum;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.externalapi.service.entity.gw.waterleachalarm.GwWaterLeachAlarmEntity;
import cn.cuiot.dmp.externalapi.service.entity.gw.waterleachalarm.GwWaterLeachAlarmPropertyEntity;
import cn.cuiot.dmp.externalapi.service.mapper.gw.waterleachalarm.GwWaterLeachAlarmMapper;
import cn.cuiot.dmp.externalapi.service.query.gw.waterleachalarm.GwWaterLeachAlarmSyncQuery;
import cn.cuiot.dmp.externalapi.service.service.gw.GwEntranceGuardConfigService;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req.DmpDeviceBatchStatusReq;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req.DmpDeviceReq;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.DmpDevicePropertyListResp;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.DmpDeviceStatusBatchResp;
import cn.cuiot.dmp.externalapi.service.vendor.gw.dmp.DmpDeviceRemoteService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 格物水浸报警器定时任务处理
 *
 * @Author: zc
 * @Date: 2024-10-24
 */
@Slf4j
@Service
public class GwWaterLeachAlarmTaskHandle {

    @Autowired
    private GwWaterLeachAlarmMapper gwWaterLeachAlarmMapper;
    @Autowired
    private GwWaterLeachAlarmPropertyService gwWaterLeachAlarmPropertyService;
    @Autowired
    private GwEntranceGuardConfigService configService;
    @Autowired
    private DmpDeviceRemoteService dmpDeviceRemoteService;


    public void syncGwWaterLeachAlarmPropertyHandle(String param) {
        GwWaterLeachAlarmSyncQuery query = null;
        // 参数不为空
        if (StringUtils.isNotBlank(param)) {
            query = JsonUtil.readValue(param, GwWaterLeachAlarmSyncQuery.class);
        }

        AtomicLong pageNo = new AtomicLong(1);
        long pageSize = 200;
        long pages;
        do {
            LambdaQueryWrapper<GwWaterLeachAlarmEntity> wrapper = new LambdaQueryWrapper<>();
            if (Objects.nonNull(query)) {
                wrapper.eq(Objects.nonNull(query.getCompanyId()), GwWaterLeachAlarmEntity::getCompanyId, query.getCompanyId());
                wrapper.in(CollectionUtils.isNotEmpty(query.getIotId()), GwWaterLeachAlarmEntity::getIotId, query.getIotId());
            }

            Page<GwWaterLeachAlarmEntity> page = gwWaterLeachAlarmMapper.selectPage(new Page<>(pageNo.getAndAdd(1), pageSize), wrapper);
            pages = page.getPages();

            List<GwWaterLeachAlarmEntity> records;
            if (CollectionUtils.isNotEmpty(records = page.getRecords())) {
                //获取企业id列表
                List<Long> companyIds = records.stream().map(GwWaterLeachAlarmEntity::getCompanyId)
                        .filter(Objects::nonNull).distinct().collect(Collectors.toList());

                if (CollectionUtils.isNotEmpty(companyIds)) {
                    List<GWCurrencyBO> configInfoList = configService.getConfigInfo(companyIds, FootPlateInfoEnum.GW_WATER_LEACH_ALARM.getId());
                    Map<Long, GWCurrencyBO> map = configInfoList.stream()
                            .collect(Collectors.toMap(GWCurrencyBO::getCompanyId, e -> e));

                    records.forEach(item -> {
                        try {
                            if (Objects.nonNull(item.getCompanyId()) && map.containsKey(item.getCompanyId())) {

                                GWCurrencyBO bo = map.get(item.getCompanyId());

                                DmpDeviceReq req = new DmpDeviceReq();
                                req.setProductKey(bo.getProductKey());
                                req.setDeviceKey(bo.getDeviceKey());
                                DmpDevicePropertyListResp resp = dmpDeviceRemoteService.getPropertyLatestValues(req, bo);

                                if (CollectionUtils.isNotEmpty(resp.getList())) {
                                    // 更新设备属性
                                    GwWaterLeachAlarmPropertyEntity propertyEntity = new GwWaterLeachAlarmPropertyEntity();
                                    propertyEntity.setDeviceId(item.getId());
                                    propertyEntity.setDeviceData(JsonUtil.writeValueAsString(resp.getList()));
                                    gwWaterLeachAlarmPropertyService.createOrUpdate(propertyEntity);
                                }
                            }
                        } catch (Exception e) {
                            log.error("同步格物水浸报警器属性异常................");
                            e.printStackTrace();
                        }
                    });
                }
            }
        } while (pageNo.get() < pages);
    }

    /**
     * 同步设备信息
     */
    public void syncGwWaterLeachAlarmInfoHandle(String param) {

        GwWaterLeachAlarmSyncQuery query = null;
        // 参数不为空
        if (StringUtils.isNotBlank(param)) {
            query = JsonUtil.readValue(param, GwWaterLeachAlarmSyncQuery.class);
        }

        AtomicLong pageNo = new AtomicLong(1);
        long pageSize = 200;
        long pages;
        do {
            LambdaQueryWrapper<GwWaterLeachAlarmEntity> wrapper = new LambdaQueryWrapper<>();
            if (Objects.nonNull(query)) {
                wrapper.eq(Objects.nonNull(query.getCompanyId()), GwWaterLeachAlarmEntity::getCompanyId, query.getCompanyId());
                wrapper.in(CollectionUtils.isNotEmpty(query.getIotId()), GwWaterLeachAlarmEntity::getIotId, query.getIotId());
            }

            Page<GwWaterLeachAlarmEntity> page = gwWaterLeachAlarmMapper.selectPage(new Page<>(pageNo.getAndAdd(1), pageSize), wrapper);
            pages = page.getPages();

            List<GwWaterLeachAlarmEntity> records;
            if (CollectionUtils.isNotEmpty(records = page.getRecords())) {
                //获取企业id列表
                List<Long> companyIds = records.stream().map(GwWaterLeachAlarmEntity::getCompanyId)
                        .filter(Objects::nonNull).distinct().collect(Collectors.toList());

                if (CollectionUtils.isNotEmpty(companyIds)) {
                    List<GWCurrencyBO> configInfoList = configService.getConfigInfo(companyIds, FootPlateInfoEnum.GW_WATER_LEACH_ALARM.getId());
                    Map<Long, GWCurrencyBO> currencyBoMap = configInfoList.stream()
                            .collect(Collectors.toMap(GWCurrencyBO::getCompanyId, e -> e));

                    Map<Long, List<String>> map = records.stream()
                            .collect(Collectors.groupingBy(GwWaterLeachAlarmEntity::getCompanyId,
                                    Collectors.mapping(GwWaterLeachAlarmEntity::getIotId, Collectors.toList())));
                    map.forEach((key, value) -> {
                        try {
                            GWCurrencyBO bo = currencyBoMap.get(key);
                            DmpDeviceBatchStatusReq req = new DmpDeviceBatchStatusReq();
                            req.setIotId(value);
                            DmpDeviceStatusBatchResp resp = dmpDeviceRemoteService.batchGetStatus(req, bo);

                            List<GwWaterLeachAlarmEntity> list = respToEntity(resp, key);
                            if (CollectionUtils.isNotEmpty(list)) {
                                list.forEach(gwWaterLeachAlarmMapper::syncUpdateStatus);
                            }

                        } catch (Exception e) {
                            log.error("同步格物水浸报警器信息异常................");
                            e.printStackTrace();
                        }
                    });
                }
            }
        } while (pageNo.get() < pages);
    }

    /**
     * 同步水浸报警器定
     *
     * @return List<GwWaterLeachAlarmEntity>
     * @Param resp 参数
     * @Param companyId 企业id
     */
    private List<GwWaterLeachAlarmEntity> respToEntity(DmpDeviceStatusBatchResp resp, Long companyId) {
        if (Objects.isNull(resp) || CollectionUtils.isEmpty(resp.getDeviceStatusList())) {
            return null;
        }
        return resp.getDeviceStatusList().stream().map(item -> {
            GwWaterLeachAlarmEntity entity = new GwWaterLeachAlarmEntity();
            entity.setIotId(item.getIotId());
            entity.setCompanyId(companyId);
            entity.setStatus(item.getEnabled() ? EntityConstants.YES : EntityConstants.NO);
            entity.setEquipStatus(item.getStatus());
            return entity;
        }).collect(Collectors.toList());
    }
}
