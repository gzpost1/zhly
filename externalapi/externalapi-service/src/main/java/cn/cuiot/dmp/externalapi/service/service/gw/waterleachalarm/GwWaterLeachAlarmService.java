package cn.cuiot.dmp.externalapi.service.service.gw.waterleachalarm;

import cn.cuiot.dmp.base.application.service.ApiArchiveService;
import cn.cuiot.dmp.base.application.service.ApiSystemService;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.bean.external.GWCurrencyBO;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.FootPlateInfoEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.constant.GwBusinessTypeConstant;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwDeviceRelationEntity;
import cn.cuiot.dmp.externalapi.service.entity.gw.gasalarm.GwGasAlarmPropertyEntity;
import cn.cuiot.dmp.externalapi.service.entity.gw.waterleachalarm.GwWaterLeachAlarmPropertyEntity;
import cn.cuiot.dmp.externalapi.service.enums.GwEntranceGuardEquipStatusEnums;
import cn.cuiot.dmp.externalapi.service.enums.GwGasAlarmPropertyEnums;
import cn.cuiot.dmp.externalapi.service.feign.SystemApiService;
import cn.cuiot.dmp.externalapi.service.mapper.gw.waterleachalarm.GwWaterLeachAlarmPropertyMapper;
import cn.cuiot.dmp.externalapi.service.query.gw.waterleachalarm.*;
import cn.cuiot.dmp.externalapi.service.service.gw.GwDeviceRelationService;
import cn.cuiot.dmp.externalapi.service.service.gw.GwEntranceGuardConfigService;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req.*;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.DmpDeviceBatchPropertyResp;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.DmpDeviceCreateResp;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.DmpDevicePropertyResp;
import cn.cuiot.dmp.externalapi.service.vendor.gw.dmp.DmpDeviceRemoteService;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwCommonPropertyVo;
import cn.cuiot.dmp.externalapi.service.vo.gw.waterleachalarm.GwWaterLeachAlarmDetailVO;
import cn.cuiot.dmp.externalapi.service.vo.gw.waterleachalarm.GwWaterLeachAlarmPageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuiot.dmp.externalapi.service.mapper.gw.waterleachalarm.GwWaterLeachAlarmMapper;
import cn.cuiot.dmp.externalapi.service.entity.gw.waterleachalarm.GwWaterLeachAlarmEntity;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 格物-水浸报警器
 *
 * @Author: zc
 * @Date: 2024-10-22
 */
@Service
public class GwWaterLeachAlarmService extends ServiceImpl<GwWaterLeachAlarmMapper, GwWaterLeachAlarmEntity> {

    @Autowired
    private DmpDeviceRemoteService dmpDeviceRemoteService;
    @Autowired
    private GwEntranceGuardConfigService gwEntranceGuardConfigService;
    @Autowired
    private GwDeviceRelationService gwDeviceRelationService;
    @Autowired
    private GwWaterLeachAlarmPropertyMapper gwWaterLeachAlarmPropertyMapper;
    @Autowired
    private ApiArchiveService apiArchiveService;
    @Autowired
    private GwWaterLeachAlarmTaskHandle gwWaterLeachAlarmTaskHandle;
    @Autowired
    private ApiSystemService apiSystemService;
    @Autowired
    private SystemApiService systemApiService;


    /**
     * 省电模式
     */
    public static final String POWER_SAVING_MODE = "powerSavingMode";

    /**
     * 批量操作设备最大数
     */
    public static final Integer BATCH_MAX_NUM = 200;

    /**
     * 分页
     *
     * @return IPage
     * @Param query 参数
     */
    public IPage<GwWaterLeachAlarmPageVO> queryForPage(GwWaterLeachAlarmPageQuery query) {

        // 企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        List<Long> buildingIds = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(query.getBuildingIds())) {
            buildingIds.addAll(query.getBuildingIds());
        } else {
            DepartmentReqDto dto = new DepartmentReqDto();
            dto.setDeptId(Objects.nonNull(query.getDeptId()) ? query.getDeptId() : LoginInfoHolder.getCurrentDeptId());
            dto.setSelfReturn(true);
            List<BuildingArchive> archives = apiArchiveService.lookupBuildingArchiveByDepartmentList(dto);
            if (CollectionUtils.isNotEmpty(archives)) {
                List<Long> collect = archives.stream().map(BuildingArchive::getId).collect(Collectors.toList());
                buildingIds.addAll(collect);
            }
        }

        LambdaQueryWrapper<GwWaterLeachAlarmEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GwWaterLeachAlarmEntity::getCompanyId, companyId);
        wrapper.like(StringUtils.isNotBlank(query.getDeviceName()), GwWaterLeachAlarmEntity::getDeviceName, query.getDeviceName());
        wrapper.like(StringUtils.isNotBlank(query.getImei()), GwWaterLeachAlarmEntity::getImei, query.getImei());
        wrapper.eq(Objects.nonNull(query.getStatus()), GwWaterLeachAlarmEntity::getStatus, query.getStatus());
        wrapper.eq(StringUtils.isNotBlank(query.getEquipStatus()), GwWaterLeachAlarmEntity::getEquipStatus, query.getEquipStatus());
        //查询所属组织下的楼盘以及未设置楼盘的数据
        wrapper.in(CollectionUtils.isNotEmpty(buildingIds), GwWaterLeachAlarmEntity::getBuildingId, buildingIds);
        //排序
        wrapper.orderByDesc(GwWaterLeachAlarmEntity::getCreateTime);

        //分页查询设备信息
        IPage<GwWaterLeachAlarmPageVO> iPage = page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper).convert(item -> {
            GwWaterLeachAlarmPageVO vo = new GwWaterLeachAlarmPageVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        });

        if (Objects.nonNull(iPage) && CollectionUtils.isNotEmpty(iPage.getRecords())) {
            buildPageVo(iPage.getRecords());
        }

        return iPage;
    }

    /**
     * 创建
     *
     * @Param dto 参数
     */
    public void create(GwWaterLeachAlarmCreateDto dto) {
        // 数据校验
        checkDate(dto, null);
        // 企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        // 部门id
        Long deptId = LoginInfoHolder.getCurrentDeptId();
        // 获取企业对接配置
        GWCurrencyBO bo = gwEntranceGuardConfigService.getConfigInfo(companyId, FootPlateInfoEnum.GW_WATER_LEACH_ALARM.getId());

        long id = IdWorker.getId();

        //调用接口创建设备数据
        DmpDeviceCreateReq deviceReq = new DmpDeviceCreateReq();
        deviceReq.setProductKey(bo.getProductKey());
        deviceReq.setDeviceKey(id + "");
        deviceReq.setImei(dto.getImei());
        deviceReq.setDeviceName(dto.getDeviceName());
        deviceReq.setDescription(dto.getRemark());
        DmpDeviceCreateResp device = dmpDeviceRemoteService.createDevice(deviceReq, bo);

        if (Objects.nonNull(device)) {
            //保存设备关联信息
            GwDeviceRelationEntity relation = new GwDeviceRelationEntity();
            relation.setDataId(id);
            relation.setDeviceKey(device.getDeviceKey());
            relation.setProductKey(device.getProductKey());
            relation.setBusinessType(GwBusinessTypeConstant.WATER_LEACH_ALARM);
            gwDeviceRelationService.create(relation);

            // 保存水浸报警器
            GwWaterLeachAlarmEntity entity = new GwWaterLeachAlarmEntity();
            BeanUtils.copyProperties(dto, entity);
            entity.setId(id);
            entity.setCompanyId(companyId);
            entity.setStatus(EntityConstants.ENABLED);
            entity.setEquipStatus(GwEntranceGuardEquipStatusEnums.NOT_ACTIVE.getCode());
            entity.setDeptId(deptId);
            //构建外部设备信息
            entity.buildExternalDeviceInfo(entity, device);

            save(entity);
        }
    }

    /**
     * 修改
     *
     * @Param dto 参数
     */
    public void update(GwWaterLeachAlarmUpdateDto dto) {
        checkDate(dto, dto.getId());

        // 企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        // 获取企业对接配置
        GWCurrencyBO bo = gwEntranceGuardConfigService.getConfigInfo(companyId, FootPlateInfoEnum.GW_WATER_LEACH_ALARM.getId());

        GwWaterLeachAlarmEntity entity = queryEntity(dto.getId(), companyId);

        if (!Objects.equals(dto.getImei(), entity.getImei())) {
            throw new BusinessException(ResultCode.ERROR, "设备IMEI不可编辑");
        }

        // 修改设备信息
        if (!StringUtils.equals(entity.getDeviceName(), dto.getDeviceName()) ||
                !StringUtils.equals(entity.getRemark(), dto.getRemark())) {

            DmpDeviceEditReq req = new DmpDeviceEditReq();
            req.setProductKey(bo.getProductKey());
            req.setDeviceKey(bo.getDeviceKey());
            req.setDeviceName(dto.getDeviceName());
            dmpDeviceRemoteService.editDevice(req, bo);
        }

        // 修改设备属性
        if (StringUtils.isNotBlank(dto.getPowerSavingMode())) {
            DmpDevicePropertyReq req = new DmpDevicePropertyReq();
            req.setProductKey(bo.getProductKey());
            req.setDeviceKey(bo.getDeviceKey());

            Map<String, Object> map = Maps.newHashMap();
            map.put(GwWaterLeachAlarmPropertyEntity.POWER_SAVING_MODE, dto.getPowerSavingMode());
            req.setItems(JsonUtil.writeValueAsString(map));
            dmpDeviceRemoteService.setDeviceProperty(req, bo);
        }

        // 修改设备信息
        BeanUtils.copyProperties(dto, entity);
        updateById(entity);
    }

    /**
     * 详情
     *
     * @return GwWaterLeachAlarmDetailVO
     * @Param id 数据id
     */
    public GwWaterLeachAlarmDetailVO queryForDetail(Long id) {
        // 企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        GwWaterLeachAlarmEntity waterLeachAlarm = Optional.ofNullable(queryEntity(id, companyId))
                .orElseThrow(() -> new BusinessException(ResultCode.ERROR, "数据不存在"));
        GwWaterLeachAlarmDetailVO vo = new GwWaterLeachAlarmDetailVO();
        BeanUtils.copyProperties(waterLeachAlarm, vo);

        // 设置省电模式
        GwWaterLeachAlarmPropertyEntity propertyEntity = getPropertyEntity(id);
        if (Objects.nonNull(propertyEntity) && StringUtils.isNotBlank(propertyEntity.getDeviceData())) {

            List<DmpDevicePropertyResp> propertyResp = JsonUtil.readValue(propertyEntity.getDeviceData(),
                    new TypeReference<List<DmpDevicePropertyResp>>() {
                    });

            if (CollectionUtils.isNotEmpty(propertyResp)) {
                propertyResp.forEach(item -> {
                    if (Objects.equals(item.getKey(), GwGasAlarmPropertyEntity.POWER_SAVING_MODE)) {
                        vo.setPowerSavingMode(GwGasAlarmPropertyEnums.PowerSavingMode.getNameByCode(item.getKey()));
                    }
                });
            }
        }
        return vo;
    }

    /**
     * 获取设备属性
     *
     * @return GwWaterLeachAlarmPropertyEntity 属性详情
     * @Param id 数据id
     */
    public List<GwCommonPropertyVo> getProperty(Long id) {
        GwWaterLeachAlarmPropertyEntity propertyEntity = getPropertyEntity(id);
        return GwWaterLeachAlarmPropertyEntity.buildGwCommonPropertyVo(propertyEntity);
    }

    /**
     * 批量设置数据
     *
     * @Param dto 参数
     */
    public void batchSetProperty(GwWaterLeachAlarmPropertyDto dto) {
        if (dto.getIds().size() > BATCH_MAX_NUM) {
            throw new BusinessException(ResultCode.ERROR, "设备数超过限制");
        }

        if (Objects.isNull(dto.getBuildingId()) && StringUtils.isBlank(dto.getPowerSavingMode())) {
            return;
        }

        // 获取当前企业ID
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        // 根据企业ID和数据id列表获取警报实体列表
        List<GwWaterLeachAlarmEntity> entities = list(new LambdaQueryWrapper<GwWaterLeachAlarmEntity>()
                .eq(GwWaterLeachAlarmEntity::getCompanyId, companyId)
                .in(GwWaterLeachAlarmEntity::getId, dto.getIds()));

        // 如果没有找到任何数据，抛出异常
        if (CollectionUtils.isEmpty(entities)) {
            throw new BusinessException(ResultCode.ERROR, "数据不存在");
        }

        // 批量修改楼盘
        if (Objects.nonNull(dto.getBuildingId())) {
            entities.forEach(item -> item.setBuildingId(dto.getBuildingId()));
            updateBatchById(entities);
        }

        // 批量修改属性
        if (StringUtils.isNotBlank(dto.getPowerSavingMode())) {
            // 提取唯一的设备ID
            List<String> iotIds = entities.stream()
                    .map(GwWaterLeachAlarmEntity::getIotId)
                    .distinct()
                    .collect(Collectors.toList());

            // 验证设备ID是否有效
            if (CollectionUtils.isEmpty(iotIds)) {
                throw new BusinessException(ResultCode.ERROR, "设备唯一识别码不存在");
            }

            // 获取企业的配置
            GWCurrencyBO bo = gwEntranceGuardConfigService.getConfigInfo(companyId, FootPlateInfoEnum.GW_WATER_LEACH_ALARM.getId());

            // 准备批量请求的参数
            Map<String, Object> map = Maps.newHashMap();
            map.put(POWER_SAVING_MODE, dto.getPowerSavingMode());

            // 创建请求对象并发送请求
            DmpDeviceBatchPropertyReq req = new DmpDeviceBatchPropertyReq();
            req.setItems(JsonUtil.writeValueAsString(map));
            req.setIotId(iotIds);
            DmpDeviceBatchPropertyResp resp = dmpDeviceRemoteService.batchSetDeviceProperty(req, bo);

            // 处理成功的数据
            handleSuccess(resp.getSuccessList(), companyId);

            // 处理失败的数据
            handleFail(resp.getFailList(), entities);
        }
    }

    /**
     * 批量设置数据-批量处理成功的数据
     *
     * @Param list 参数
     * @Param companyId 企业id
     * @Param powerSavingMode 省电模式
     */
    private void handleSuccess(List<DmpDeviceBatchPropertyResp.SuccessDataItem> successList, Long companyId) {

        if (CollectionUtils.isEmpty(successList)) {
            return;
        }
        // 处理成功的设备更新
        List<String> successIotIds = successList.stream()
                .map(DmpDeviceBatchPropertyResp.SuccessDataItem::getIotId)
                .collect(Collectors.toList());

        // 更新数据库中成功的设备属性
        if (CollectionUtils.isNotEmpty(successIotIds)) {
            GwWaterLeachAlarmSyncQuery query = new GwWaterLeachAlarmSyncQuery();
            query.setCompanyId(companyId);
            query.setIotId(successIotIds);
            gwWaterLeachAlarmTaskHandle.syncGwWaterLeachAlarmPropertyHandle(JsonUtil.writeValueAsString(query));
        }
    }

    /**
     * 批量设置数据-批量处理失败的数据
     *
     * @Param list 参数
     * @Param companyId 企业id
     * @Param powerSavingMode 省电模式
     */
    private void handleFail(List<DmpDeviceBatchPropertyResp.FailDataItem> failList, List<GwWaterLeachAlarmEntity> entities) {

        if (CollectionUtils.isEmpty(failList)) {
            return;
        }

        // 处理失败的设备更新
        List<String> failIotIds = failList.stream()
                .map(DmpDeviceBatchPropertyResp.FailDataItem::getIotId)
                .collect(Collectors.toList());

        // 如果有设备更新失败，抛出异常并包含设备名称
        if (CollectionUtils.isNotEmpty(failIotIds)) {

            Map<String, GwWaterLeachAlarmEntity> entityMap = entities.stream().collect(Collectors.toMap(GwWaterLeachAlarmEntity::getIotId, e -> e));

            List<String> failDeviceNames = failIotIds.stream()
                    .filter(entityMap::containsKey)
                    .map(e -> entityMap.get(e).getDeviceName())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            // 如果找到了失败的设备，收集设备名称并抛出异常
            if (CollectionUtils.isNotEmpty(failDeviceNames)) {
                throw new BusinessException(ResultCode.ERROR, "设备【" + String.join(",", failDeviceNames) + "】修改属性失败");
            }
        }
    }


    /**
     * 批量删除
     *
     * @Param ids 数据id列表
     */
    public void batchDelete(List<Long> ids) {
        batchHandle(ids, EntityConstants.EXPIRE);

        // 批量删除平台设备
        removeByIds(ids);

        // 批量删除设备关联数据
        gwDeviceRelationService.batchDeleteByDataIds(ids);
    }

    /**
     * 批量启用
     *
     * @Param ids 数据id列表
     */
    public void batchEnable(List<Long> ids) {
        batchHandle(ids, EntityConstants.ENABLED);
    }

    /**
     * 批量禁用
     *
     * @Param ids 数据id列表
     */
    public void batchDisable(List<Long> ids) {
        batchHandle(ids, EntityConstants.DISABLED);
    }

    /**
     * 批量处理设备
     *
     * @Param ids 数据id列表
     * @Param status 1启用 0停用 2删除
     */
    private void batchHandle(List<Long> ids, Byte status) {
        // 检查设备ID数量是否超过限制
        if (ids.size() > BATCH_MAX_NUM) {
            throw new BusinessException(ResultCode.ERROR, "设备数超过限制");
        }

        // 获取当前企业ID
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        // 查询该企业下的水浸报警实体
        List<GwWaterLeachAlarmEntity> waterLeachAlarmList = list(
                new LambdaQueryWrapper<GwWaterLeachAlarmEntity>()
                        .eq(GwWaterLeachAlarmEntity::getCompanyId, companyId)
                        .in(GwWaterLeachAlarmEntity::getId, ids));

        // 如果查询到的设备列表不为空
        if (CollectionUtils.isNotEmpty(waterLeachAlarmList)) {
            // 判断传入的设备ID是否都属于该企业
            if (ids.size() != waterLeachAlarmList.size()) {
                List<GwWaterLeachAlarmEntity> invalidDevices = waterLeachAlarmList.stream()
                        .filter(e -> !ids.contains(e.getId()))
                        .collect(Collectors.toList());

                // 如果存在不属于该企业的设备，抛出异常
                if (CollectionUtils.isNotEmpty(invalidDevices)) {
                    List<String> deviceNames = invalidDevices.stream()
                            .map(GwWaterLeachAlarmEntity::getDeviceName)
                            .collect(Collectors.toList());
                    throw new BusinessException(ResultCode.ERROR, "设备【" + String.join(",", deviceNames) + "】不属于该企业");
                }
            }
        }

        // 提取设备的唯一识别码
        List<String> iotIds = waterLeachAlarmList.stream()
                .map(GwWaterLeachAlarmEntity::getIotId)
                .distinct()
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(iotIds)) {
            // 获取企业对接配置
            GWCurrencyBO bo = gwEntranceGuardConfigService.getConfigInfo(companyId, FootPlateInfoEnum.GW_WATER_LEACH_ALARM.getId());

            // 根据状态执行不同的批处理请求
            if (Objects.equals(status, EntityConstants.ENABLED)) {
                // 批量启用设备
                DmpDeviceBatchEnableReq req = new DmpDeviceBatchEnableReq();
                req.setIotId(iotIds);
                dmpDeviceRemoteService.batchEnableDevice(req, bo);

                // 同步设备信息
                syncDeviceInfo(companyId, iotIds);

            } else if (Objects.equals(status, EntityConstants.DISABLED)) {
                // 批量禁用设备
                DmpDeviceBatchDisableReq req = new DmpDeviceBatchDisableReq();
                req.setIotId(iotIds);
                dmpDeviceRemoteService.batchDisableDevice(req, bo);

                // 同步设备信息
                syncDeviceInfo(companyId, iotIds);

            } else if (Objects.equals(status, EntityConstants.EXPIRE)) {
                // 批量删除设备
                DmpDeviceBatchDeleteReq req = new DmpDeviceBatchDeleteReq();
                req.setIotId(iotIds);
                dmpDeviceRemoteService.batchDeleteDevice(req, bo);
            }
        }
    }

    /**
     * 辅助方法：同步设备信息
     */
    private void syncDeviceInfo(Long companyId, List<String> iotIds) {
        GwWaterLeachAlarmSyncQuery syncQuery = new GwWaterLeachAlarmSyncQuery();
        syncQuery.setCompanyId(companyId);
        syncQuery.setIotId(iotIds);
        gwWaterLeachAlarmTaskHandle.syncGwWaterLeachAlarmInfoHandle(JsonUtil.writeValueAsString(syncQuery));
    }


    /**
     * 获取属性
     *
     * @return GwWaterLeachAlarmPropertyEntity 属性数据
     * @Param id 水浸报警器id
     */
    private GwWaterLeachAlarmPropertyEntity getPropertyEntity(Long id) {
        return gwWaterLeachAlarmPropertyMapper.selectOne(
                new LambdaQueryWrapper<GwWaterLeachAlarmPropertyEntity>()
                        .eq(GwWaterLeachAlarmPropertyEntity::getDeviceId, id).last(" LIMIT 1 "));
    }

    /**
     * 根据数据id和企业id查询详情
     *
     * @return GwWaterLeachAlarmEntity
     * @Param id 数据id
     * @Param companyId 企业id
     */
    private GwWaterLeachAlarmEntity queryEntity(Long id, Long companyId) {
        return getOne(new LambdaQueryWrapper<GwWaterLeachAlarmEntity>()
                .eq(GwWaterLeachAlarmEntity::getId, id)
                .eq(GwWaterLeachAlarmEntity::getCompanyId, companyId)
                .last(" LIMIT 1 ")
        );
    }

    /**
     * 数据校验
     *
     * @Param dto 参数
     * @Param id 数据id
     */
    private void checkDate(GwWaterLeachAlarmCreateDto dto, Long id) {
        long count = count(new LambdaQueryWrapper<GwWaterLeachAlarmEntity>()
                .ne(Objects.nonNull(id), GwWaterLeachAlarmEntity::getId, id)
                .eq(GwWaterLeachAlarmEntity::getImei, dto.getImei()));
        if (count > 0) {
            throw new BusinessException(ResultCode.ERROR, "设备IMEI已存在");
        }
    }

    private void buildPageVo(List<GwWaterLeachAlarmPageVO> list) {
        try {
            // 获取各类信息的 Map
            Map<Long, Object> buildingArchiveMap = getDataMap(list, GwWaterLeachAlarmPageVO::getBuildingId, this::queryBuildingInfo, BuildingArchive::getId, BuildingArchive::getName);
            Map<Long, Object> deptMap = getDataMap(list, GwWaterLeachAlarmPageVO::getDeptId, this::queryDeptList, DepartmentDto::getId, DepartmentDto::getPathName);

            // 设置 Vo 对象的值
            for (GwWaterLeachAlarmPageVO vo : list) {
                vo.setBuildingName(buildingArchiveMap.getOrDefault(vo.getBuildingId(), null) + "");
                vo.setDeptPathName(deptMap.getOrDefault(vo.getDeptId(), null) + "");
            }
        } catch (Exception e) {
            log.error("构建参数名称异常....");
            e.printStackTrace();
        }
    }

    /**
     * 通用方法：根据 VO 属性获取数据 Map
     */
    private <T> Map<Long, Object> getDataMap(List<GwWaterLeachAlarmPageVO> list,
                                             Function<GwWaterLeachAlarmPageVO, Long> idGetter,
                                             Function<List<Long>, List<T>> queryFunction,
                                             Function<T, Long> keyMapper,
                                             Function<T, Object> valueMapper) {
        List<Long> ids = list.stream().map(idGetter).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ids)) {
            return Maps.newHashMap();
        }
        return queryFunction.apply(ids).stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

    /**
     * 查询楼盘信息
     */
    private List<BuildingArchive> queryBuildingInfo(List<Long> buildingIds) {
        BuildingArchiveReq req = new BuildingArchiveReq();
        req.setIdList(buildingIds);
        return systemApiService.buildingArchiveQueryForList(req);
    }

    /**
     * 获取组织信息
     */
    private List<DepartmentDto> queryDeptList(List<Long> deptIds) {
        DepartmentReqDto dto = new DepartmentReqDto();
        dto.setDeptIdList(deptIds);
        return apiSystemService.lookUpDepartmentList(dto);
    }
}
