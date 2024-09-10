package cn.cuiot.dmp.externalapi.service.service.gw;

import cn.cuiot.dmp.base.application.service.ApiSystemService;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParams;
import cn.cuiot.dmp.base.infrastructure.dto.req.CustomConfigDetailReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.PlatfromInfoReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.bean.external.GWEntranceGuardBO;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.FootPlateInfoEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.feign.SystemApiService;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardCreateDto;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardPageQuery;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardUpdateDto;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req.DmpDeviceCreateReq;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.DmpDeviceResp;
import cn.cuiot.dmp.externalapi.service.vendor.gw.dmp.DmpDeviceRemoteService;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwEntranceGuardPageVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    /**
     * 根据企业id获取门禁数据
     */
    public List<GwEntranceGuardEntity> queryListByCompanyId(Long companyId) {
        return list(new LambdaQueryWrapper<GwEntranceGuardEntity>()
                .eq(GwEntranceGuardEntity::getCompanyId, companyId)
                .orderByDesc(GwEntranceGuardEntity::getCreateTime));
    }

    /**
     * 分页
     */
    public IPage<GwEntranceGuardPageVo> queryForPage(GwEntranceGuardPageQuery query) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

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
     * 详情
     */
    public GwEntranceGuardEntity queryForDetail(Long id) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        List<GwEntranceGuardEntity> list = list(new LambdaQueryWrapper<GwEntranceGuardEntity>()
                .eq(GwEntranceGuardEntity::getCompanyId, companyId)
                .eq(GwEntranceGuardEntity::getId, id));
        return CollectionUtils.isNotEmpty(list) ? list.get(0) : null;
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
        GWEntranceGuardBO bo = getProductKey(companyId);

        //调用接口创建设备数据
        DmpDeviceCreateReq deviceReq = new DmpDeviceCreateReq();
        deviceReq.setProductKey(bo.getProductKey());
        deviceReq.setImei(dto.getSn());
        deviceReq.setDeviceName(dto.getName());
        deviceReq.setDescription(deviceReq.getDescription());
        DmpDeviceResp device = dmpDeviceRemoteService.createDevice(deviceReq, bo);

        //保存数据
        if (Objects.nonNull(device)) {
            GwEntranceGuardEntity entity = new GwEntranceGuardEntity();
            BeanUtils.copyProperties(dto, entity);
            entity.setCompanyId(companyId);
            entity.setDeptId(deptId);
            entity.setStatus(EntityConstants.ENABLED);
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

        List<GwEntranceGuardEntity> list = list(new LambdaQueryWrapper<GwEntranceGuardEntity>()
                .eq(GwEntranceGuardEntity::getId, dto.getId())
                .eq(GwEntranceGuardEntity::getCompanyId, companyId));
        if (CollectionUtils.isEmpty(list)) {
            throw new BusinessException(ResultCode.ERROR, "数据不存在");
        }

        GwEntranceGuardEntity entity = list.get(0);
        entity.setName(dto.getName());
        updateById(entity);
    }

    /**
     * 启停用
     */
    public void updateStatus(UpdateStatusParams param) {
        ///企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        List<GwEntranceGuardEntity> list = list(new LambdaQueryWrapper<GwEntranceGuardEntity>()
                .eq(GwEntranceGuardEntity::getCompanyId, companyId));
        if (CollectionUtils.isEmpty(list)) {
            throw new BusinessException(ResultCode.ERROR, "数据不存在");
        }

        List<Long> ids = param.getIds();
        //判断设备id列表是否都属于该企业的设备
        List<GwEntranceGuardEntity> collect = list.stream().filter(e -> !ids.contains(e.getId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            List<String> deviceNames = collect.stream().map(GwEntranceGuardEntity::getName).collect(Collectors.toList());
            throw new BusinessException(ResultCode.ERROR, "设备【" + String.join(",", deviceNames) + "】不属于该企业");
        }

        update(new LambdaUpdateWrapper<GwEntranceGuardEntity>()
                .in(GwEntranceGuardEntity::getId, ids)
                .set(GwEntranceGuardEntity::getStatus, param.getStatus()));
    }

    /**
     * 删除设备
     */
    public void delete(List<Long> ids) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        List<GwEntranceGuardEntity> list = list(new LambdaQueryWrapper<GwEntranceGuardEntity>()
                .eq(GwEntranceGuardEntity::getCompanyId, companyId));
        if (CollectionUtils.isEmpty(list)) {
            throw new BusinessException(ResultCode.ERROR, "数据不存在");
        }

        //判断设备id列表是否都属于该企业的设备
        List<GwEntranceGuardEntity> collect = list.stream().filter(e -> !ids.contains(e.getId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            List<String> deviceNames = collect.stream().map(GwEntranceGuardEntity::getName).collect(Collectors.toList());
            throw new BusinessException(ResultCode.ERROR, "设备【" + String.join(",", deviceNames) + "】不属于该企业");
        }

        removeByIds(ids);
    }

    /**
     * 获取对接配置
     *
     * @return productKey
     * @Param companyId 企业id
     */
    private GWEntranceGuardBO getProductKey(Long companyId) {
        // 构建请求DTO
        PlatfromInfoReqDTO dto = new PlatfromInfoReqDTO();
        dto.setCompanyId(companyId);
        dto.setPlatformId(FootPlateInfoEnum.GW_ENTRANCE_GUARD.getId());

        // 查询平台信息
        IPage<PlatfromInfoRespDTO> iPage = systemApiService.queryPlatfromInfoPage(dto);
        if (Objects.isNull(iPage) || Objects.isNull(iPage.getRecords()) || CollectionUtils.isEmpty(iPage.getRecords())) {
            throw new BusinessException(ResultCode.ERROR, "【格物门禁】对接参数配置为空，请先配置后再创建数据");
        }

        // 获取第一条记录
        PlatfromInfoRespDTO respDTO = iPage.getRecords().get(0);

        // 检查返回的数据是否为空
        if (StringUtils.isBlank(respDTO.getData())) {
            throw new BusinessException(ResultCode.ERROR, "【格物门禁】对接参数配置为空，请先配置后再创建数据");
        }

        // 从JSON数据中解析出GWEntranceGuardBO对象
        GWEntranceGuardBO gwEntranceGuardBO = FootPlateInfoEnum.getObjectFromJsonById(FootPlateInfoEnum.GW_ENTRANCE_GUARD.getId(), respDTO.getData());
        if (Objects.isNull(gwEntranceGuardBO)) {
            throw new BusinessException(ResultCode.ERROR, "【格物门禁】对接参数配置为空，请先配置后再创建数据");
        }

        if (StringUtils.isBlank(gwEntranceGuardBO.getAppId())) {
            throw new BusinessException(ResultCode.ERROR, "【格物门禁】对接参数【appId】配置为空，请先配置后再创建数据");
        }
        if (StringUtils.isBlank(gwEntranceGuardBO.getAppSecret())) {
            throw new BusinessException(ResultCode.ERROR, "【格物门禁】对接参数【appSecret】配置为空，请先配置后再创建数据");
        }
        if (StringUtils.isBlank(gwEntranceGuardBO.getProductKey())) {
            throw new BusinessException(ResultCode.ERROR, "【格物门禁】对接参数【productKey】配置为空，请先配置后再创建数据");
        }

        return gwEntranceGuardBO;
    }

    /**
     * 构建后台分页信息
     */
    private void buildPageVo(List<GwEntranceGuardPageVo> list) {
        // 获取各类信息的 Map
        Map<Long, String> buildingArchiveMap = getDataMap(list, GwEntranceGuardPageVo::getBuildingId, this::queryBuildingInfo, BuildingArchive::getId, BuildingArchive::getName);
        Map<Long, String> deptMap = getDataMap(list, GwEntranceGuardPageVo::getDeptId, this::queryDeptList, DepartmentDto::getId, DepartmentDto::getPathName);
        Map<Long, String> brandMap = getCustomConfigDetailsMap(list, GwEntranceGuardPageVo::getBrandId);
        Map<Long, String> modelMap = getCustomConfigDetailsMap(list, GwEntranceGuardPageVo::getModelId);

        // 设置 Vo 对象的值
        for (GwEntranceGuardPageVo vo : list) {
            vo.setBuildingName(buildingArchiveMap.getOrDefault(vo.getBuildingId(), null));
            vo.setDeptPathName(deptMap.getOrDefault(vo.getDeptId(), null));
            vo.setBrandName(brandMap.getOrDefault(vo.getBrandId(), null));
            vo.setModelName(modelMap.getOrDefault(vo.getModelId(), null));
        }
    }

    /**
     * 通用方法：根据 VO 属性获取数据 Map
     */
    private <T> Map<Long, String> getDataMap(List<GwEntranceGuardPageVo> list,
                                             Function<GwEntranceGuardPageVo, Long> idGetter,
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
    private Map<Long, String> getCustomConfigDetailsMap(List<GwEntranceGuardPageVo> list,
                                                        Function<GwEntranceGuardPageVo, Long> idGetter) {
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
}
