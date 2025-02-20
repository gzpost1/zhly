package cn.cuiot.dmp.externalapi.service.service.gw;

import cn.cuiot.dmp.base.application.dto.ExcelDownloadDto;
import cn.cuiot.dmp.base.application.service.ApiArchiveService;
import cn.cuiot.dmp.base.application.service.ApiSystemService;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParams;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.bean.external.GWCurrencyBO;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.NumberConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.StatusConst;
import cn.cuiot.dmp.common.enums.FootPlateInfoEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.constant.GwBusinessTypeConstant;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwDeviceRelationEntity;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwSmogDataEntity;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwSmogEntity;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwSmogEventEntity;
import cn.cuiot.dmp.externalapi.service.enums.GwEntranceGuardEquipStatusEnums;
import cn.cuiot.dmp.externalapi.service.enums.GwSmogBatteryEnums;
import cn.cuiot.dmp.externalapi.service.enums.GwSmogConnectivityEnums;
import cn.cuiot.dmp.externalapi.service.enums.GwSmogPropertyEnums;
import cn.cuiot.dmp.externalapi.service.feign.SystemApiService;
import cn.cuiot.dmp.externalapi.service.mapper.gw.GwSmogMapper;
import cn.cuiot.dmp.externalapi.service.query.gw.GwSmogBatchUpdateDto;
import cn.cuiot.dmp.externalapi.service.query.gw.GwSmogCreateDto;
import cn.cuiot.dmp.externalapi.service.query.gw.GwSmogQuery;
import cn.cuiot.dmp.externalapi.service.query.gw.GwSmogUpdateDto;
import cn.cuiot.dmp.externalapi.service.query.gw.push.DeviceEventSmogParams;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req.*;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.DmpDevicePropertyResp;
import cn.cuiot.dmp.externalapi.service.vendor.gw.dmp.DmpDeviceRemoteService;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwCommonPropertyVo;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwSmogDetailVo;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwSmogPageVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 格物烟雾报警器 服务类
 * </p>
 *
 * @author wuyongchong
 * @since 2024-10-23
 */
@Service
public class GwSmogService extends ServiceImpl<GwSmogMapper, GwSmogEntity> {

    @Autowired
    private GwEntranceGuardConfigService gwEntranceGuardConfigService;

    @Autowired
    private DmpDeviceRemoteService dmpDeviceRemoteService;

    @Autowired
    private GwDeviceRelationService gwDeviceRelationService;

    @Autowired
    private SystemApiService systemApiService;
    @Autowired
    private ApiSystemService apiSystemService;
    @Autowired
    private ExcelExportService excelExportService;
    @Autowired
    private GwSmogDataService gwSmogDataService;
    @Autowired
    private ApiArchiveService apiArchiveService;

    @Autowired
    private GwSmogEventService gwSmogEventService;
    /**
     * 分页
     */
    public IPage<GwSmogPageVo> queryForPage(GwSmogQuery query) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        GWCurrencyBO bo = gwEntranceGuardConfigService.getConfigInfo(companyId, FootPlateInfoEnum.GW_SMOG_ALARM.getId());
        AssertUtil.isFalse(!Objects.equals(bo.getStatus(),EntityConstants.ENABLED),"功能未开通，请联系管理员");

        List<Long> buildingIdsQuery = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(query.getBuildingIds())) {
            buildingIdsQuery.addAll(query.getBuildingIds());
        } else {
            DepartmentReqDto dto = new DepartmentReqDto();
            dto.setDeptId(Objects.nonNull(query.getDeptId()) ? query.getDeptId() : LoginInfoHolder.getCurrentDeptId());
            dto.setSelfReturn(true);
            List<BuildingArchive> archives = apiArchiveService.lookupBuildingArchiveByDepartmentList(dto);
            if (CollectionUtils.isNotEmpty(archives)) {
                List<Long> collect = archives.stream().map(BuildingArchive::getId).collect(Collectors.toList());
                buildingIdsQuery.addAll(collect);
            }else {
                buildingIdsQuery.add(-999L);
            }
        }
        query.setBuildingIds(buildingIdsQuery);
        LambdaQueryWrapper<GwSmogEntity> wrapper = buildWrapper(query, companyId);

        IPage<GwSmogPageVo> page = page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper).convert(item -> {
            GwSmogPageVo vo = new GwSmogPageVo();
            BeanUtils.copyProperties(item, vo);
            return vo;
        });
        List<GwSmogPageVo> records = page.getRecords();
        if(CollectionUtils.isEmpty(records)){
            return page;
        }
        //转译楼盘
        List<Long> buildingIds = records.stream().map(vo -> vo.getBuildingId()).distinct().collect(Collectors.toList());
        Map<Long, String> buildingMap = Optional.ofNullable(queryBuildingInfo(buildingIds)).orElse(Lists.newArrayList())
                .stream().collect(Collectors.toMap(BuildingArchive::getId, BuildingArchive::getName));
        //转译部门
        List<Long> deptIds = records.stream().map(vo -> vo.getDeptId()).distinct().collect(Collectors.toList());
        Map<Long, String> deptMap = Optional.ofNullable(queryDeptList(deptIds)).orElse(Lists.newArrayList())
                .stream().collect(Collectors.toMap(DepartmentDto::getId, DepartmentDto::getName));

        for(GwSmogPageVo vo : records){
            vo.setBuildingName(buildingMap.get(vo.getBuildingId()));
            vo.setDeptName(deptMap.get(vo.getDeptId()));
            vo.setStatusName(Objects.equals(vo.getStatus(), NumberConst.DATA_STATUS)? StatusConst.STOP: StatusConst.ENABLE);
            vo.setEquipStatusName(GwEntranceGuardEquipStatusEnums.queryNameByCode(vo.getEquipStatus()));
        }
        return page;
    }

    private LambdaQueryWrapper<GwSmogEntity> buildWrapper(GwSmogQuery query, Long companyId) {
        LambdaQueryWrapper<GwSmogEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GwSmogEntity::getCompanyId, companyId);
        wrapper.like(StringUtils.isNotBlank(query.getName()), GwSmogEntity::getName, query.getName());
        wrapper.like(StringUtils.isNotBlank(query.getImei()), GwSmogEntity::getImei, query.getImei());
        wrapper.eq(Objects.nonNull(query.getStatus()), GwSmogEntity::getStatus, query.getStatus());
        wrapper.eq(StringUtils.isNotBlank(query.getEquipStatus()), GwSmogEntity::getEquipStatus, query.getEquipStatus());
        //查询所属组织下的楼盘以及未设置楼盘的数据
        wrapper.in(CollectionUtils.isNotEmpty(query.getBuildingIds()), GwSmogEntity::getBuildingId, query.getBuildingIds());
        wrapper.orderByDesc(GwSmogEntity::getCreateTime);
        return wrapper;
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
     * 烟雾报警器设备导出
     * @param query
     */
    public void export(GwSmogQuery query){
        excelExportService.excelExport(ExcelDownloadDto.<GwSmogQuery>builder().loginInfo(LoginInfoHolder
                        .getCurrentLoginInfo()).query(query)
                .title("烟雾报警器导出").fileName("烟雾报警器导出(" + DateTimeUtil.dateToString(new Date(), "yyyyMMdd")+")").sheetName("烟雾报警器导出")
                .build(), GwSmogPageVo.class, this::queryExport);

    }

    /**
     * 设备列表导出
     * @param downloadDto
     * @return
     */
    public IPage<GwSmogPageVo> queryExport(ExcelDownloadDto<GwSmogQuery> downloadDto){
        GwSmogQuery pageQuery = downloadDto.getQuery();
        IPage<GwSmogPageVo> data = this.queryForPage(pageQuery);
        return data;
    }

    /**
     * 详情
     */
    public GwSmogEntity queryForDetail(Long id,Long companyId) {
        LambdaQueryWrapper<GwSmogEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GwSmogEntity::getId, id);
        queryWrapper.eq(GwSmogEntity::getCompanyId, companyId);
        GwSmogEntity gwSmogEntity = baseMapper.selectOne(queryWrapper);
        return gwSmogEntity;
    }
    /**
     * 详情(包含可修改属性)
     */
    public GwSmogDetailVo queryForPropertyDetail(Long id, Long companyId) {
        LambdaQueryWrapper<GwSmogEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GwSmogEntity::getId,id);
        queryWrapper.eq(GwSmogEntity::getCompanyId,companyId);
        GwSmogEntity gwSmogEntity = baseMapper.selectOne(queryWrapper);
        GwSmogDetailVo vo = BeanMapper.map(gwSmogEntity, GwSmogDetailVo.class);
        //烟雾报警器属性
        GwSmogDataEntity gwSmogDataEntity = gwSmogDataService.queryLatestData(id);
        if(Objects.nonNull(gwSmogDataEntity) && CollectionUtils.isNotEmpty(gwSmogDataEntity.getDeviceData())){
            Map<String, DmpDevicePropertyResp> propertyMap = gwSmogDataEntity.getDeviceData().stream().collect(Collectors.toMap(v -> v.getKey(), v -> v));
            //灵敏度
            Object sensitivity = propertyMap.get(GwSmogPropertyEnums.SENSITIVITY.getKey()).getValue();
            vo.setSensitivity(Objects.isNull(sensitivity)?null:String.valueOf(sensitivity));
            //省电模式
            Object powerSavingMode = propertyMap.get(GwSmogPropertyEnums.POWER_SAVING_MODE.getKey()).getValue();
            vo.setPowerSavingMode(Objects.isNull(powerSavingMode)?null:String.valueOf(powerSavingMode));
            //温度报警阈值
            Object tempLimit = propertyMap.get(GwSmogPropertyEnums.TEMP_LIMIT.getKey()).getValue();
            vo.setTempLimit(Objects.isNull(powerSavingMode)?null:Double.valueOf(tempLimit.toString()));
            //温度报警阈值
            Object dbmLimit = propertyMap.get(GwSmogPropertyEnums.DBM_LIMIT.getKey()).getValue();
            vo.setDbmLimit(Objects.isNull(dbmLimit)?null:Double.valueOf(dbmLimit.toString()));
            //烟雾传感器污染度
            Object smokeDirt = propertyMap.get(GwSmogPropertyEnums.SMOKE_DIRT.getKey()).getValue();
            vo.setSmokeDirt(Objects.isNull(smokeDirt)?null:Double.valueOf(smokeDirt.toString()));
        }
        return vo;
    }

    /**
     * 创建
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(GwSmogCreateDto dto) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        //部门id
        Long deptId = LoginInfoHolder.getCurrentDeptId();

        //校验对接参数是否已填,获取productKey
        GWCurrencyBO bo = gwEntranceGuardConfigService.getConfigInfo(companyId, FootPlateInfoEnum.GW_SMOG_ALARM.getId());
        AssertUtil.isFalse(Objects.isNull(bo),"对接配置为空");
        long count = this.count(new LambdaQueryWrapper<GwSmogEntity>().eq(GwSmogEntity::getImei, dto.getImei()));
        AssertUtil.isFalse(count>1,"设备IMEI已被使用");

        long id = IdWorker.getId();

        //调用接口创建设备数据
        DmpDeviceCreateReq deviceReq = new DmpDeviceCreateReq();
        deviceReq.setProductKey(bo.getProductKey());
        deviceReq.setDeviceKey(id + "");
        deviceReq.setImei(dto.getImei());
        deviceReq.setDeviceName(dto.getName());
        deviceReq.setDescription(deviceReq.getDescription());
 /*       DmpDeviceCreateResp device = dmpDeviceRemoteService.createDevice(deviceReq, bo);;

        if (Objects.nonNull(device)) {
            //保存设备关联信息
            GwDeviceRelationEntity relation = new GwDeviceRelationEntity();
            relation.setDataId(id);
            relation.setDeviceKey(device.getDeviceKey());
            relation.setProductKey(device.getProductKey());
            relation.setBusinessType(GwBusinessTypeConstant.SMOG_ALARM);
            gwDeviceRelationService.create(relation);

            //保存门禁数据
            GwSmogEntity entity = new GwSmogEntity();
            BeanUtils.copyProperties(dto, entity);
            entity.setId(id);
            entity.setCompanyId(companyId);
            entity.setDeptId(deptId);
            entity.setStatus(EntityConstants.ENABLED);
            entity.setEquipStatus(GwEntranceGuardEquipStatusEnums.NOT_ACTIVE.getCode());
            //构建外部设备信息
            entity.buildExternalDeviceInfo(entity, device);

            save(entity);
        }*/

        GwDeviceRelationEntity relation = new GwDeviceRelationEntity();
        relation.setDataId(id);
        relation.setDeviceKey("1");
        relation.setProductKey("1");
        relation.setBusinessType(GwBusinessTypeConstant.SMOG_ALARM);
        gwDeviceRelationService.create(relation);

        GwSmogEntity entity = new GwSmogEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        entity.setCompanyId(companyId);
        entity.setDeptId(deptId);
        entity.setStatus(EntityConstants.ENABLED);
        entity.setEquipStatus(GwEntranceGuardEquipStatusEnums.NOT_ACTIVE.getCode());
        //构建外部设备信息

        save(entity);
    }


    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(GwSmogUpdateDto dto) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        GwSmogEntity entity = Optional.ofNullable(queryForDetail(dto.getId(),companyId))
                .orElseThrow(() -> new BusinessException(ResultCode.ERROR, "数据不存在"));

        //更新数据库
        entity.setName(dto.getName());
        entity.setBuildingId(dto.getBuildingId());
        entity.setAddress(dto.getAddress());
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());
        entity.setRemark(dto.getRemark());
        updateById(entity);
        //更新属性表
        gwSmogDataService.updateProperty(dto);

        //更新格物平台设备属性
        GWCurrencyBO bo = gwEntranceGuardConfigService.getConfigInfo(companyId, FootPlateInfoEnum.GW_SMOG_ALARM.getId());
        AssertUtil.isFalse(Objects.isNull(bo),"对接配置为空");

        //调用接口创建设备数据
        DmpDevicePropertyReq deviceReq = new DmpDevicePropertyReq();
        deviceReq.setProductKey(bo.getProductKey());
        deviceReq.setDeviceKey(entity.getDeviceKey());
        deviceReq.setIotId(entity.getIotId());
        deviceReq.setPriorityCommand(Boolean.FALSE);
        //需要修改的属性
        HashMap<String, Object> itemMap = Maps.newHashMap();
        itemMap.put(GwSmogPropertyEnums.SENSITIVITY.getKey(), dto.getSensitivity());
        itemMap.put(GwSmogPropertyEnums.POWER_SAVING_MODE.getKey(),dto.getPowerSavingMode());
        itemMap.put(GwSmogPropertyEnums.TEMP_LIMIT.getKey(),dto.getTempLimit());
        itemMap.put(GwSmogPropertyEnums.DBM_LIMIT.getKey(),dto.getDbmLimit());
        itemMap.put(GwSmogPropertyEnums.SMOKE_DIRT.getKey(),dto.getSmokeDirt());
        deviceReq.setItems(JsonUtil.writeValueAsString(itemMap));
        //dmpDeviceRemoteService.setDeviceProperty(deviceReq, bo);
    }


    /**
     * 批量更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdate(GwSmogBatchUpdateDto dto) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        LambdaQueryWrapper<GwSmogEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(GwSmogEntity::getId,dto.getId());
        queryWrapper.eq(GwSmogEntity::getCompanyId,companyId);
        List<GwSmogEntity> gwSmogEntities = baseMapper.selectList(queryWrapper);
        AssertUtil.isFalse(CollectionUtils.isEmpty(gwSmogEntities),"数据不存在");
        AssertUtil.isFalse(!Objects.equals(dto.getId().size(),gwSmogEntities.size()),"部分数据不存在");

        //更新数据库
        if(Objects.nonNull(dto.getBuildingId())){
            LambdaUpdateWrapper<GwSmogEntity> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(GwSmogEntity::getBuildingId,dto.getBuildingId());
            updateWrapper.in(GwSmogEntity::getId,dto.getId());
            baseMapper.update(new GwSmogEntity(),updateWrapper);
            //更新属性表
            gwSmogDataService.batchUpdateProperty(dto);
        }

        //更新格物平台设备属性
        GWCurrencyBO bo = gwEntranceGuardConfigService.getConfigInfo(companyId, FootPlateInfoEnum.GW_SMOG_ALARM.getId());
        AssertUtil.isFalse(Objects.isNull(bo),"对接配置为空");

        //调用接口创建设备数据
        Boolean f =StringUtils.isNotBlank(dto.getSensitivity()) && StringUtils.isNotBlank(dto.getPowerSavingMode()) &&Objects.nonNull(dto.getTempLimit())
                &&Objects.nonNull(dto.getDbmLimit())&&Objects.nonNull(dto.getSmokeDirt());
        //如果不存在有属性修改
        if(f){
            return;
        }
        DmpDeviceBatchPropertyReq deviceReq = new DmpDeviceBatchPropertyReq();
        deviceReq.setProductKey(bo.getProductKey());
        deviceReq.setDeviceKey(gwSmogEntities.stream().map(vo->vo.getDeviceKey()).collect(Collectors.toList()));
        deviceReq.setIotId(gwSmogEntities.stream().map(vo->vo.getIotId()).collect(Collectors.toList()));
        deviceReq.setPriorityCommand(Boolean.FALSE);
        //需要修改的属性
        HashMap<String, Object> itemMap = Maps.newHashMap();
        if(StringUtils.isNotBlank(dto.getSensitivity())){
            itemMap.put(GwSmogPropertyEnums.SENSITIVITY.getKey(),dto.getSensitivity());
        }
        if(StringUtils.isNotBlank(dto.getPowerSavingMode())){
            itemMap.put(GwSmogPropertyEnums.POWER_SAVING_MODE.getKey(),dto.getPowerSavingMode());
        }
        if(Objects.nonNull(dto.getTempLimit())){
            itemMap.put(GwSmogPropertyEnums.TEMP_LIMIT.getKey(),dto.getTempLimit());
        }

        if(Objects.nonNull(dto.getDbmLimit())){
            itemMap.put(GwSmogPropertyEnums.DBM_LIMIT.getKey(),dto.getDbmLimit());
        }

        if(Objects.nonNull(dto.getSmokeDirt())){
            itemMap.put(GwSmogPropertyEnums.SMOKE_DIRT.getKey(),dto.getSmokeDirt());
        }
        deviceReq.setItems(JsonUtil.writeValueAsString(itemMap));
        //dmpDeviceRemoteService.batchSetDeviceProperty(deviceReq, bo);
    }


    /**
     * 批量启停用
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(UpdateStatusParams param) {
        ///企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        LambdaQueryWrapper<GwSmogEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(GwSmogEntity::getId,param.getIds());
        queryWrapper.eq(GwSmogEntity::getCompanyId,companyId);
        List<GwSmogEntity> gwSmogEntities = baseMapper.selectList(queryWrapper);
        AssertUtil.isFalse(CollectionUtils.isEmpty(gwSmogEntities),"数据不存在");
        AssertUtil.isFalse(!Objects.equals(param.getIds().size(),gwSmogEntities.size()),"部分数据不存在");

        //更新数据库
        update(new LambdaUpdateWrapper<GwSmogEntity>()
                .in(GwSmogEntity::getId, param.getIds())
                .set(GwSmogEntity::getStatus, param.getStatus()));

        //更新格物平台设备状态
        GWCurrencyBO bo = gwEntranceGuardConfigService.getConfigInfo(companyId, FootPlateInfoEnum.GW_SMOG_ALARM.getId());
        AssertUtil.isFalse(Objects.isNull(bo),"对接配置为空");

        if(Objects.equals(param.getStatus(),EntityConstants.ENABLED)){
            DmpDeviceBatchEnableReq deviceReq = new DmpDeviceBatchEnableReq();
            deviceReq.setProductKey(bo.getProductKey());
            deviceReq.setDeviceKey(gwSmogEntities.stream().map(vo->vo.getDeviceKey()).collect(Collectors.toList()));
            deviceReq.setIotId(gwSmogEntities.stream().map(vo->vo.getIotId()).collect(Collectors.toList()));
            //dmpDeviceRemoteService.batchEnableDevice(deviceReq, bo);
        }else {
            DmpDeviceBatchDisableReq deviceReq = new DmpDeviceBatchDisableReq();
            deviceReq.setProductKey(bo.getProductKey());
            deviceReq.setDeviceKey(gwSmogEntities.stream().map(vo->vo.getDeviceKey()).collect(Collectors.toList()));
            deviceReq.setIotId(gwSmogEntities.stream().map(vo->vo.getIotId()).collect(Collectors.toList()));
            //dmpDeviceRemoteService.batchDisableDevice(deviceReq, bo);
        }
    }

    /**
     * 删除设备
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ///企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        LambdaQueryWrapper<GwSmogEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(GwSmogEntity::getId,ids);
        queryWrapper.eq(GwSmogEntity::getCompanyId,companyId);
        List<GwSmogEntity> gwSmogEntities = baseMapper.selectList(queryWrapper);
        AssertUtil.isFalse(CollectionUtils.isEmpty(gwSmogEntities),"数据不存在");
        AssertUtil.isFalse(!Objects.equals(ids.size(),gwSmogEntities.size()),"部分数据不存在");

        //更新数据库
        removeByIds(ids);

        //更新格物平台设备状态
        GWCurrencyBO bo = gwEntranceGuardConfigService.getConfigInfo(companyId, FootPlateInfoEnum.GW_SMOG_ALARM.getId());
        AssertUtil.isFalse(Objects.isNull(bo),"对接配置为空");
        DmpDeviceBatchDeleteReq deviceReq = new DmpDeviceBatchDeleteReq();
        deviceReq.setProductKey(bo.getProductKey());
        deviceReq.setDeviceKey(gwSmogEntities.stream().map(vo->vo.getDeviceKey()).collect(Collectors.toList()));
        deviceReq.setIotId(gwSmogEntities.stream().map(vo->vo.getIotId()).collect(Collectors.toList()));
        //dmpDeviceRemoteService.batchDeleteDevice(deviceReq, bo);
    }


    public List<List<GwCommonPropertyVo>> queryForDeviceDetailLaster(Long deviceId, Long companyId){
        LambdaQueryWrapper<GwSmogEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GwSmogEntity::getId,deviceId);
        queryWrapper.eq(GwSmogEntity::getCompanyId,companyId);
        GwSmogEntity gwSmogEntity = this.getOne(queryWrapper);
        AssertUtil.isFalse(Objects.isNull(gwSmogEntity),"该企业下不存在该设备");


        ArrayList<List<GwCommonPropertyVo>> result = Lists.newArrayList();

        //设备数据
        ArrayList<GwCommonPropertyVo> properList = Lists.newArrayList();
        GwSmogDataEntity gwSmogDataEntity = gwSmogDataService.queryLatestData(deviceId);
        List<DmpDevicePropertyResp> dmpDevicePropertyResps = Optional.ofNullable(gwSmogDataEntity)
                .map(vo -> vo.getDeviceData()).orElse(Lists.newArrayList());
        List<GwCommonPropertyVo> gwSmogropertyVos = BeanMapper.mapList(dmpDevicePropertyResps, GwCommonPropertyVo.class);
        Map<String, GwCommonPropertyVo> properMap = gwSmogropertyVos.stream().collect(Collectors.toMap(GwCommonPropertyVo::getKey, vo -> vo));

        for(GwSmogPropertyEnums statusEnums : GwSmogPropertyEnums.values()){
            GwCommonPropertyVo vo = properMap.get(statusEnums.getKey());
            if(Objects.nonNull(vo)){
                vo.setKeyName(statusEnums.getName());
                if(Objects.nonNull(statusEnums)){
                    Class enumsClzz = statusEnums.getEnumsClzz();
                    if(Objects.nonNull(enumsClzz)){
                        try {
                            Method method = enumsClzz.getMethod("queryNameByValue", String.class);
                            Object invoke = method.invoke(null, String.valueOf(vo.getValue()));
                            vo.setValue(invoke);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }else {
                vo = new GwCommonPropertyVo(statusEnums.getKey(),statusEnums.getName());
            }
            properList.add(vo);
        }
        result.add(properList);

        //事件数据  包括电池电量 连接服务
        GwSmogEventEntity gwSmogEventEntity = Optional.ofNullable(gwSmogEventService.getBaseMapper().queryLatestData(deviceId))
                .orElse(new GwSmogEventEntity());

        long time = Optional.ofNullable(gwSmogEventEntity.getCreateTime()).orElse(new Date()).getTime();

        DeviceEventSmogParams.BatteryInfo batteryInfo = Optional.ofNullable(gwSmogEventEntity.getBattery()).orElse(new DeviceEventSmogParams.BatteryInfo());
        ArrayList<GwCommonPropertyVo> batteryList = Lists.newArrayList();
        for(GwSmogBatteryEnums statusEnums : GwSmogBatteryEnums.values()){
            Object value =null;
            try {
                Class<? extends DeviceEventSmogParams.BatteryInfo> clazz = batteryInfo.getClass();
                Field field = clazz.getDeclaredField(statusEnums.getKey());
                field.setAccessible(true);
                value = field.get(batteryInfo);
            }catch (Exception e){
                e.printStackTrace();
            }
            GwCommonPropertyVo gwCommonPropertyVo = new GwCommonPropertyVo();
            gwCommonPropertyVo.setKeyName(statusEnums.getName());
            gwCommonPropertyVo.setKey(statusEnums.getKey());
            gwCommonPropertyVo.setValue(value);
            gwCommonPropertyVo.setUnit(statusEnums.getUnit());
            gwCommonPropertyVo.setTs(time);
            batteryList.add(gwCommonPropertyVo);
        }
        result.add(batteryList);


        //连接服务上报
        DeviceEventSmogParams.ConnectivityInfo connectivityInfo = Optional.ofNullable(gwSmogEventEntity.getConnectivity()).orElse(new DeviceEventSmogParams.ConnectivityInfo());
        ArrayList<GwCommonPropertyVo> connectivityList = Lists.newArrayList();
        for(GwSmogConnectivityEnums statusEnums : GwSmogConnectivityEnums.values()){
            Object value =null;
            try {
                Class<? extends DeviceEventSmogParams.ConnectivityInfo> clazz = connectivityInfo.getClass();
                Field field = clazz.getDeclaredField(statusEnums.getKey());
                field.setAccessible(true);
                value = field.get(connectivityInfo);
            }catch (Exception e){
                e.printStackTrace();
            }
            GwCommonPropertyVo gwCommonPropertyVo = new GwCommonPropertyVo();
            gwCommonPropertyVo.setKeyName(statusEnums.getName());
            gwCommonPropertyVo.setKey(statusEnums.getKey());
            gwCommonPropertyVo.setValue(value);
            gwCommonPropertyVo.setUnit(statusEnums.getUnit());
            gwCommonPropertyVo.setTs(time);
            connectivityList.add(gwCommonPropertyVo);
        }

        result.add(connectivityList);
        return result;
    }

}
