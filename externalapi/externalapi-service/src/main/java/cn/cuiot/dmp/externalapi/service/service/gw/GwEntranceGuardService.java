package cn.cuiot.dmp.externalapi.service.service.gw;

import cn.cuiot.dmp.base.application.dto.ExcelDownloadDto;
import cn.cuiot.dmp.base.application.service.ApiSystemService;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParams;
import cn.cuiot.dmp.base.infrastructure.dto.req.CustomConfigDetailReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.bean.external.GWEntranceGuardBO;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.NumberConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.StatusConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.constant.GwBusinessTypeConstant;
import cn.cuiot.dmp.externalapi.service.constant.GwEntranceGuardOperationStatusType;
import cn.cuiot.dmp.externalapi.service.constant.GwEntranceGuardOperationType;
import cn.cuiot.dmp.externalapi.service.constant.GwEntranceGuardServiceKeyConstant;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwDeviceRelationEntity;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardOperationRecordEntity;
import cn.cuiot.dmp.externalapi.service.enums.GwEntranceGuardEquipStatusEnums;
import cn.cuiot.dmp.externalapi.service.feign.SystemApiService;
import cn.cuiot.dmp.externalapi.service.query.EntranceGuardRecordReqDTO;
import cn.cuiot.dmp.externalapi.service.query.StatisInfoReqDTO;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardCreateDto;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardOperationDto;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardPageQuery;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardUpdateDto;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req.DmpDeviceCreateReq;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req.InvokeDeviceServiceReq;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.DmpDeviceResp;
import cn.cuiot.dmp.externalapi.service.vendor.gw.dmp.DmpDeviceRemoteService;
import cn.cuiot.dmp.externalapi.service.vo.EntranceGuardRecordVo;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwEntranceGuardAppPageVo;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwEntranceGuardPageVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardEntity;
import cn.cuiot.dmp.externalapi.service.mapper.gw.GwEntranceGuardMapper;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 格物门禁 业务层
 *
 * @Author: zc
 * @Date: 2024-09-06
 */
@Service
public class GwEntranceGuardService extends ServiceImpl<GwEntranceGuardMapper, GwEntranceGuardEntity> {

    @Autowired
    private SystemApiService systemApiService;
    @Autowired
    private DmpDeviceRemoteService dmpDeviceRemoteService;
    @Autowired
    private ApiSystemService apiSystemService;
    @Autowired
    private GwEntranceGuardConfigService gwEntranceGuardConfigService;
    @Autowired
    private GwEntranceGuardOperationRecordService operationRecordService;
    @Autowired
    private GwDeviceRelationService gwDeviceRelationService;

    @Autowired
    private ExcelExportService excelExportService;

    /**
     * 分页
     */
    public IPage<GwEntranceGuardAppPageVo> queryAppForPage(GwEntranceGuardPageQuery query) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        LambdaQueryWrapper<GwEntranceGuardEntity> wrapper = buildWrapper(query, companyId);

        IPage<GwEntranceGuardAppPageVo> page = page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper).convert(item -> {
            GwEntranceGuardAppPageVo vo = new GwEntranceGuardAppPageVo();
            BeanUtils.copyProperties(item, vo);
            return vo;
        });

        //构建数据
        if (Objects.nonNull(page) && CollectionUtils.isNotEmpty(page.getRecords())) {
            buildPageVo(page.getRecords());
        }

        return page;
    }

    /**
     * 分页
     */
    public IPage<GwEntranceGuardPageVo> queryForPage(GwEntranceGuardPageQuery query) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        LambdaQueryWrapper<GwEntranceGuardEntity> wrapper = buildWrapper(query, companyId);

        IPage<GwEntranceGuardPageVo> page = page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper).convert(item -> {
            GwEntranceGuardPageVo vo = new GwEntranceGuardPageVo();
            BeanUtils.copyProperties(item, vo);
            return vo;
        });

        //构建数据
        if (Objects.nonNull(page) && CollectionUtils.isNotEmpty(page.getRecords())) {
            buildPageVo(page.getRecords());
        }

        return page;
    }

    /**
     * 门禁导出
     * @param query
     */
    public void export(GwEntranceGuardPageQuery query){
        excelExportService.excelExport(ExcelDownloadDto.<GwEntranceGuardPageQuery>builder().loginInfo(LoginInfoHolder
                        .getCurrentLoginInfo()).query(query)
                .title("门禁导出").fileName("门禁导出(" + DateTimeUtil.dateToString(new Date(), "yyyyMMdd")+")").sheetName("门禁导出")
                .build(), GwEntranceGuardPageVo.class, this::queryExport);
    }

    /**
     * 门禁列表导出
     * @param downloadDto
     * @return
     */
    public IPage<GwEntranceGuardPageVo> queryExport(ExcelDownloadDto<GwEntranceGuardPageQuery> downloadDto){
        GwEntranceGuardPageQuery pageQuery = downloadDto.getQuery();
        IPage<GwEntranceGuardPageVo> data = this.queryForPage(pageQuery);
        List<GwEntranceGuardPageVo> records =Optional.ofNullable( data.getRecords()).orElse(new ArrayList<>());
        records.stream().forEach(item->{
            item.setStatusName(Objects.equals(item.getStatus(), NumberConst.DATA_STATUS)? StatusConst.STOP: StatusConst.ENABLE);
        });
        return data;
    }


    private LambdaQueryWrapper<GwEntranceGuardEntity> buildWrapper(GwEntranceGuardPageQuery query, Long companyId) {
        LambdaQueryWrapper<GwEntranceGuardEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GwEntranceGuardEntity::getCompanyId, companyId);
        wrapper.like(StringUtils.isNotBlank(query.getName()), GwEntranceGuardEntity::getName, query.getName());
        wrapper.eq(Objects.nonNull(query.getBuildingId()), GwEntranceGuardEntity::getBuildingId, query.getBuildingId());
        wrapper.eq(StringUtils.isNotBlank(query.getSn()), GwEntranceGuardEntity::getSn, query.getSn());
        wrapper.eq(Objects.nonNull(query.getBrandId()), GwEntranceGuardEntity::getBrandId, query.getBrandId());
        wrapper.eq(Objects.nonNull(query.getModelId()), GwEntranceGuardEntity::getModelId, query.getModelId());
        wrapper.eq(Objects.nonNull(query.getStatus()), GwEntranceGuardEntity::getStatus, query.getStatus());
        wrapper.eq(StringUtils.isNotBlank(query.getEquipStatus()), GwEntranceGuardEntity::getEquipStatus, query.getEquipStatus());
        wrapper.orderByDesc(GwEntranceGuardEntity::getCreateTime);
        return wrapper;
    }

    /**
     * 根据企业id获取门禁数据
     */
    public List<GwEntranceGuardEntity> queryListByCompanyId(Long companyId) {
        return list(new LambdaQueryWrapper<GwEntranceGuardEntity>()
                .eq(GwEntranceGuardEntity::getCompanyId, companyId)
                .orderByDesc(GwEntranceGuardEntity::getCreateTime));
    }

    /**
     * 详情
     */
    public GwEntranceGuardEntity queryForDetail(Long id) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        return baseMapper.queryForDetail(companyId, id);
    }

    /**
     * 创建
     */
    public void create(GwEntranceGuardCreateDto dto) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        //部门id
        Long deptId = LoginInfoHolder.getCurrentDeptId();

        //校验对接参数是否已填,获取productKey
        GWEntranceGuardBO bo = gwEntranceGuardConfigService.getConfigInfo(companyId);

        long id = IdWorker.getId();

        //调用接口创建设备数据
        DmpDeviceCreateReq deviceReq = new DmpDeviceCreateReq();
        deviceReq.setProductKey(bo.getProductKey());
        deviceReq.setDeviceKey(id + "");
        deviceReq.setImei(dto.getSn());
        deviceReq.setDeviceName(dto.getName());
        deviceReq.setDescription(deviceReq.getDescription());
        DmpDeviceResp device = dmpDeviceRemoteService.createDevice(deviceReq, bo);

        if (Objects.nonNull(device)) {
            //保存设备关联信息
            GwDeviceRelationEntity relation = new GwDeviceRelationEntity();
            relation.setDataId(id);
            relation.setDeviceKey(device.getDeviceKey());
            relation.setProductKey(device.getProductKey());
            relation.setBusinessType(GwBusinessTypeConstant.ENTRANCE_GUARD);
            gwDeviceRelationService.create(relation);

            //保存门禁数据
            GwEntranceGuardEntity entity = new GwEntranceGuardEntity();
            BeanUtils.copyProperties(dto, entity);
            entity.setId(id);
            entity.setCompanyId(companyId);
            entity.setDeptId(deptId);
            entity.setStatus(EntityConstants.ENABLED);
            entity.setEquipStatus(GwEntranceGuardEquipStatusEnums.NOT_ACTIVE.getCode());
            //构建外部设备信息
            entity.buildExternalDeviceInfo(entity, device);

            save(entity);
        }
    }

    /**
     * 更新
     */
    public void update(GwEntranceGuardUpdateDto dto) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        GwEntranceGuardEntity entranceGuard = Optional.ofNullable(baseMapper.queryForDetail(companyId, dto.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.ERROR, "数据不存在"));

        entranceGuard.setName(dto.getName());
        updateById(entranceGuard);
    }

    /**
     * 启停用
     */
    public void updateStatus(UpdateStatusParams param) {
        ///企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        List<GwEntranceGuardEntity> guardEntityList = baseMapper.queryForListByIds(companyId, param.getIds());
        if (CollectionUtils.isEmpty(guardEntityList)) {
            throw new BusinessException(ResultCode.ERROR, "数据不存在");
        }

        List<Long> ids = param.getIds();
        if (ids.size() != guardEntityList.size()) {
            //判断设备id列表是否都属于该企业的设备
            List<GwEntranceGuardEntity> collect = guardEntityList.stream().filter(e -> !ids.contains(e.getId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                List<String> deviceNames = collect.stream().map(GwEntranceGuardEntity::getName).collect(Collectors.toList());
                throw new BusinessException(ResultCode.ERROR, "设备【" + String.join(",", deviceNames) + "】不属于该企业");
            }
        }

        update(new LambdaUpdateWrapper<GwEntranceGuardEntity>()
                .in(GwEntranceGuardEntity::getId, ids)
                .set(GwEntranceGuardEntity::getStatus, param.getStatus()));
    }

    /**
     * 开门操作
     */
    public void openTheDoor(GwEntranceGuardOperationDto dto) {
        openOrRestartHandle(dto, GwEntranceGuardOperationType.OPEN_THE_DOOR, GwEntranceGuardServiceKeyConstant.OPEN_THE_DOOR);
    }

    /**
     * 重启操作
     */
    public void restart(GwEntranceGuardOperationDto dto) {
        openOrRestartHandle(dto, GwEntranceGuardOperationType.RESTART, GwEntranceGuardServiceKeyConstant.RESTART);
    }

    /**
     * 操作处理
     */
    private void openOrRestartHandle(GwEntranceGuardOperationDto dto, Byte recordType, String operationKey) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        //校验对接参数是否已填,获取productKey
        GWEntranceGuardBO bo = gwEntranceGuardConfigService.getConfigInfo(companyId);

        GwEntranceGuardEntity entranceGuard = Optional.ofNullable(baseMapper.queryForDetail(companyId, dto.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.ERROR, "数据不存在"));

        long id = IdWorker.getId();
        //创建记录
        GwEntranceGuardOperationRecordEntity record = new GwEntranceGuardOperationRecordEntity();
        record.setId(id);
        record.setType(recordType);
        record.setCompanyId(companyId);
        record.setExecutionStatus(GwEntranceGuardOperationStatusType.IN_PROGRESS);
        record.setEntranceGuardId(entranceGuard.getId());
        record.setDeviceSecret(dto.getDescription());
        operationRecordService.save(record);

        //请求dmp平台执行重启操作
        try {
            //requestId(业务类型-模型key-数据id-系统事件)
            bo.setRequestId(GwBusinessTypeConstant.ENTRANCE_GUARD + "-" + operationKey + "-" + id + "-" + System.currentTimeMillis());
            bo.setDeviceKey(entranceGuard.getDeviceKey());

            InvokeDeviceServiceReq req = new InvokeDeviceServiceReq();
            req.setIotId(entranceGuard.getIotId());
            req.setKey(operationKey);
            dmpDeviceRemoteService.invokeDeviceService(req, bo);
        } catch (Exception e) {

            GwEntranceGuardOperationRecordEntity entity1 = operationRecordService.getById(id);
            entity1.setExecutionStatus(GwEntranceGuardOperationStatusType.FAIL);
            entity1.setFailMsg(e.getMessage());
            operationRecordService.updateById(entity1);

            log.error("执行格物门禁操作异常......");
            e.printStackTrace();
            throw new BusinessException(ResultCode.ERROR, "操作失败【" + e.getMessage() + "】");
        }
    }

    /**
     * 删除设备
     */
    public void delete(List<Long> ids) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        List<GwEntranceGuardEntity> entranceGuardList = baseMapper.queryForListByIds(companyId, ids);
        if (CollectionUtils.isEmpty(entranceGuardList)) {
            throw new BusinessException(ResultCode.ERROR, "数据不存在");
        }

        //判断设备id列表是否都属于该企业的设备
        List<GwEntranceGuardEntity> collect = entranceGuardList.stream().filter(e -> !ids.contains(e.getId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            List<String> deviceNames = collect.stream().map(GwEntranceGuardEntity::getName).collect(Collectors.toList());
            throw new BusinessException(ResultCode.ERROR, "设备【" + String.join(",", deviceNames) + "】不属于该企业");
        }

        removeByIds(ids);
    }

    private void buildPageVo(List<?> list) {

        Object firstElement = list.get(0);

        if (firstElement instanceof GwEntranceGuardPageVo) {
            buildPageVoForSpecificType((List<GwEntranceGuardPageVo>) list,
                    GwEntranceGuardPageVo::getBuildingId, GwEntranceGuardPageVo::getDeptId,
                    GwEntranceGuardPageVo::getBrandId, GwEntranceGuardPageVo::getModelId,
                    GwEntranceGuardPageVo::setBuildingName, GwEntranceGuardPageVo::setDeptPathName,
                    GwEntranceGuardPageVo::setBrandName, GwEntranceGuardPageVo::setModelName);
        } else if (firstElement instanceof GwEntranceGuardAppPageVo) {
            buildPageVoForSpecificType((List<GwEntranceGuardAppPageVo>) list,
                    GwEntranceGuardAppPageVo::getBuildingId, null, null, null,
                    GwEntranceGuardAppPageVo::setBuildingName, null, null, null);
        } else {
            throw new IllegalArgumentException("Unsupported VO type: " + firstElement.getClass());
        }
    }

    private <V> void buildPageVoForSpecificType(List<V> list,
                                                Function<V, Long> buildingIdGetter,
                                                Function<V, Long> deptIdGetter,
                                                Function<V, Long> brandIdGetter,
                                                Function<V, Long> modelIdGetter,
                                                BiConsumer<V, String> buildingNameSetter,
                                                BiConsumer<V, String> deptPathNameSetter,
                                                BiConsumer<V, String> brandNameSetter,
                                                BiConsumer<V, String> modelNameSetter) {
        // 获取各类信息的 Map
        Map<Long, String> buildingArchiveMap = getDataMap(list, buildingIdGetter, this::queryBuildingInfo, BuildingArchive::getId, BuildingArchive::getName);
        Map<Long, String> deptMap = deptIdGetter != null ? getDataMap(list, deptIdGetter, this::queryDeptList, DepartmentDto::getId, DepartmentDto::getPathName) : null;
        Map<Long, String> brandMap = brandIdGetter != null ? getCustomConfigDetailsMap(list, brandIdGetter) : null;
        Map<Long, String> modelMap = modelIdGetter != null ? getCustomConfigDetailsMap(list, modelIdGetter) : null;

        // 设置 Vo 对象的值
        for (V vo : list) {
            if (buildingNameSetter != null) {
                buildingNameSetter.accept(vo, buildingArchiveMap.getOrDefault(buildingIdGetter.apply(vo), null));
            }
            if (deptPathNameSetter != null && deptMap != null) {
                deptPathNameSetter.accept(vo, deptMap.getOrDefault(deptIdGetter.apply(vo), null));
            }
            if (brandNameSetter != null && brandMap != null) {
                brandNameSetter.accept(vo, brandMap.getOrDefault(brandIdGetter.apply(vo), null));
            }
            if (modelNameSetter != null && modelMap != null) {
                modelNameSetter.accept(vo, modelMap.getOrDefault(modelIdGetter.apply(vo), null));
            }
        }
    }

    private <T, V> Map<Long, String> getDataMap(List<V> list,
                                                Function<V, Long> idGetter,
                                                Function<List<Long>, List<T>> queryFunction,
                                                Function<T, Long> keyMapper,
                                                Function<T, String> valueMapper) {
        List<Long> ids = list.stream().map(idGetter).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ids)) {
            return Maps.newHashMap();
        }
        return queryFunction.apply(ids).stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

    /**
     * 获取自定义配置详情的 Map
     */
    private <V> Map<Long, String> getCustomConfigDetailsMap(List<V> list, Function<V, Long> idGetter) {
        List<Long> ids = list.stream().map(idGetter).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ids)) {
            return Maps.newHashMap();
        }
        return customConfigDetails(ids);
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

    /**
     * 获取系统选项数据
     */
    public Map<Long, String> customConfigDetails(List<Long> customConfigDetailIds) {
        CustomConfigDetailReqDTO dto = new CustomConfigDetailReqDTO();
        dto.setCustomConfigDetailIdList(customConfigDetailIds);
        return apiSystemService.batchQueryCustomConfigDetailsForMap(dto);
    }


    /**
     * 获取 格物门禁 数量的统计
     */
    public Long queryEntranceGuardCount(StatisInfoReqDTO statisInfoReqDTO) {
        LambdaQueryWrapper<GwEntranceGuardEntity> queryWrapper = Wrappers.<GwEntranceGuardEntity>lambdaQuery()
                .eq(statisInfoReqDTO.getCompanyId() != null, GwEntranceGuardEntity::getCompanyId, statisInfoReqDTO.getCompanyId())
                .in(CollectionUtils.isNotEmpty(statisInfoReqDTO.getDepartmentIdList()), GwEntranceGuardEntity::getDeptId, statisInfoReqDTO.getDepartmentIdList())
                .in(CollectionUtils.isNotEmpty(statisInfoReqDTO.getLoupanIds()), GwEntranceGuardEntity::getBuildingId, statisInfoReqDTO.getLoupanIds());

        return getBaseMapper().selectCount(queryWrapper);
    }


    public IPage<EntranceGuardRecordVo> entranceGuardQueryForPage(EntranceGuardRecordReqDTO query) {
        return getBaseMapper().entranceGuardQueryForPage(new Page(query.getPageNo(), query.getPageSize()), query);
    }
}
