package cn.cuiot.dmp.archive.application.service.impl;

import cn.cuiot.dmp.archive.application.param.dto.ParkingArchivesImportDto;
import cn.cuiot.dmp.archive.application.param.dto.RoomArchivesImportDto;
import cn.cuiot.dmp.archive.application.param.vo.HousesArchiveExportVo;
import cn.cuiot.dmp.archive.application.param.vo.RoomArchivesExportVo;
import cn.cuiot.dmp.archive.application.service.RoomArchivesService;
import cn.cuiot.dmp.archive.infrastructure.entity.HousesArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.entity.ParkingArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.entity.RoomArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.RoomArchivesMapper;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.common.constant.CustomConfigConstant;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.ArchiveTypeEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.DoubleValidator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 空间档案表 服务实现类
 * </p>
 *
 * @author liujianyu
 * @since 2024-05-15
 */
@Service("roomArchivesService")
public class RoomArchivesServiceImpl extends ServiceImpl<RoomArchivesMapper, RoomArchivesEntity> implements RoomArchivesService {

    @Autowired
    private BuildingAndConfigCommonUtilService buildingAndConfigCommonUtilService;

    /**
     * 参数校验
     */
    @Override
    public void checkParams(RoomArchivesEntity entity) {
        // 必填判断
        if (StringUtils.isBlank(entity.getName())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "空间名称不可为空");
        }
        if (Objects.isNull(entity.getLoupanId())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "所属楼盘不可为空");
        }
        if (Objects.isNull(entity.getSpaceCategory())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "空间分类不可为空");
        }
        if (Objects.isNull(entity.getProfessionalPurpose())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "专业用途不可为空");
        }
        if (StringUtils.isBlank(entity.getLocationDeviation())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "定位偏差不可为空");
        }
        if (Objects.isNull(entity.getStatus())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "状态不可为空");
        }
    }

    @Override
    public void checkParamsImport(RoomArchivesImportDto entity) {
        if (StringUtils.isBlank(entity.getName())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "空间名称不可为空");
        }
        if (StringUtils.isBlank(entity.getSpaceCategoryName())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "空间分类不可为空");
        }
        if (StringUtils.isBlank(entity.getProfessionalPurposeName())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "专业用途不可为空");
        }
        if (StringUtils.isBlank(entity.getLocationDeviation())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "定位偏差不可为空");
        }

        // 规则判断
        if (entity.getName().length() > 30) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "空间名称长度不可超过30");
        }
        if (entity.getLocationDeviation().length() > 3) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "定位偏差长度不可超过3");
        }
    }

    @Override
    public List<RoomArchivesExportVo> buildExportData(IdsParam param) {
        // 查询列表信息
        List<RoomArchivesEntity> list = this.listByIds(param.getIds());
        List<RoomArchivesExportVo> res = new ArrayList<>(list.size());

        // TODO: 2024/5/16 等曹睿接口出来，就可以查询楼盘和配置
        // 查询楼盘信息-用于楼盘id转换为楼盘名称-汇总成Map
        Map<Long, String> loupanIdNameMap = buildingAndConfigCommonUtilService.getLoupanIdNameMap(list.stream().map(RoomArchivesEntity::getLoupanId).collect(Collectors.toSet()));
        // 查询配置信息-用于配置id转换为配置名称-汇总成Map
        Set<Long> configIdList = new HashSet<>();
        list.forEach(entity -> {
            getConfigIdFromEntity(entity, configIdList);
        });
        Map<Long, String> configIdNameMap = buildingAndConfigCommonUtilService.getConfigIdNameMap(configIdList);

        // 构造导出列表
        list.forEach(entity -> {
            RoomArchivesExportVo vo = new RoomArchivesExportVo();
            vo.setName(entity.getName());
            vo.setSpaceCategoryName(configIdNameMap.getOrDefault(entity.getSpaceCategory(), ""));
            vo.setLocation(entity.getLocation());
            vo.setSpaceArea(getFiledForExport(entity.getSpaceArea()));
            vo.setBusinessNatureName(configIdNameMap.getOrDefault(entity.getBusinessNature(), ""));
            vo.setOwnershipUnit(entity.getOwnershipUnit());
            vo.setOwnershipAttributeName(configIdNameMap.getOrDefault(entity.getOwnershipAttribute(), ""));
            vo.setResourceTypeName(configIdNameMap.getOrDefault(entity.getResourceType(), ""));
            vo.setLocationMethodName(configIdNameMap.getOrDefault(entity.getLocationMethod(), ""));
            vo.setStatusName(getStatusName(entity.getStatus()));
            res.add(vo);
        });

        return res;
    }

    @Override
    public void importDataSave(List<RoomArchivesImportDto> dataList, Long loupanId, Long companyId) {
        // TODO: 2024/5/16 等曹睿接口出来，就可以查询楼盘和配置
        // 先查询所属楼盘，如果查不到，就报错，查到生成map-nameIdMap
        // Map<String, Long> nameIdMap = buildingAndConfigCommonUtilService.getLoupanNameIdMap(dataList.stream().map(RoomArchivesImportDto::getLoupanName).collect(Collectors.toSet()));
        // 查询指定配置的数据，如果有配置，查询生成map-nameConfigIdMap
        Map<String, Map<String, Long>> nameConfigIdMap = buildingAndConfigCommonUtilService.getConfigNameIdMap(companyId, ArchiveTypeEnum.ROOM_ARCHIVE.getCode());

        // 构造插入列表进行保存
        List<RoomArchivesEntity> list = new ArrayList<>();
        dataList.forEach(data -> {
            RoomArchivesEntity entity = new RoomArchivesEntity();
            entity.setName(data.getName());
            entity.setSpaceCategory(checkConfigTypeNull(nameConfigIdMap.get(CustomConfigConstant.ROOM_ARCHIVES_INIT.get(0)), data.getSpaceCategoryName()));
            entity.setProfessionalPurpose(checkConfigTypeNull(nameConfigIdMap.get(CustomConfigConstant.ROOM_ARCHIVES_INIT.get(1)), data.getProfessionalPurposeName()));
            entity.setLocationDeviation(data.getLocationDeviation());
            entity.setLoupanId(loupanId);
            // TODO: 2024/5/16 这里还需要基于不同的一级类目去查询配置
            list.add(entity);
        });

        this.saveBatch(list);
    }

    /**
     * 处理空对象造成空指针
     */
    private String getFiledForExport(Object value) {
        if (Objects.isNull(value)) {
            return "";
        }
        return value.toString();
    }

    /**
     * 获取状态名称
     */
    private String getStatusName(Byte status) {
        if (Objects.isNull(status)) {
            return "";
        }
        if (EntityConstants.ENABLED.equals(status)) {
            return "启用";
        }
        if (EntityConstants.DISABLED.equals(status)) {
            return "禁用";
        }
        return "";
    }

    /**
     * 从名称获取状态
     */
    private Byte getStatusFromName(String name) {
        if (StringUtils.isBlank(name) || "启用".equals(name)) {
            return EntityConstants.ENABLED;
        }
        return EntityConstants.DISABLED;
    }

    /**
     * 处理使用名称获取配置类型
     */
    private Long checkConfigTypeNull(Map<String, Long> nameConfigIdMap, String configName) {
        Long typeId = nameConfigIdMap.get(configName);
        if (Objects.isNull(typeId)) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "配置类型" + configName + "不存在");
        }
        return typeId;
    }

    public RoomArchivesEntity queryForDetail(Long id) {
        // 查询当前id的信息
        RoomArchivesEntity entity = getById(id);
        Set<Long> configIdList = new HashSet<>();
        addListCanNull(configIdList, entity.getSpaceCategory());
        addListCanNull(configIdList, entity.getProfessionalPurpose());
        addListCanNull(configIdList, entity.getBusinessNature());
        addListCanNull(configIdList, entity.getOwnershipAttribute());
        addListCanNull(configIdList, entity.getResourceType());
        addListCanNull(configIdList, entity.getPropertyServiceLevel());
        addListCanNull(configIdList, entity.getLocationMethod());

        // 查询楼盘名称
        Set<Long> loupanIdSet = new HashSet<>();
        loupanIdSet.add(entity.getLoupanId());
        Map<Long, String> loupanIdNameMap = buildingAndConfigCommonUtilService.getLoupanIdNameMap(loupanIdSet);
        entity.setLoupanIdName(loupanIdNameMap.get(entity.getLoupanId()));

        // 查询对应的配置名称，做配置名称匹配
        final Map<Long, String> configIdNameMap = buildingAndConfigCommonUtilService.getConfigIdNameMap(configIdList);
        entity.setSpaceCategoryName(configIdNameMap.get(entity.getSpaceCategory()));
        entity.setProfessionalPurposeName(configIdNameMap.get(entity.getProfessionalPurpose()));
        entity.setBusinessNatureName(configIdNameMap.get(entity.getBusinessNature()));
        entity.setOwnershipAttributeName(configIdNameMap.get(entity.getOwnershipAttribute()));
        entity.setResourceTypeName(configIdNameMap.get(entity.getResourceType()));
        entity.setPropertyServiceLevelName(configIdNameMap.get(entity.getPropertyServiceLevel()));
        entity.setLocationMethodName(configIdNameMap.get(entity.getLocationMethod()));

        return entity;
    }

    private void addListCanNull(Set<Long> configIdList, Long configId){
        if (Objects.nonNull(configId)){
            configIdList.add(configId);
        }
    }

    private void getConfigIdFromEntity(RoomArchivesEntity entity, Set<Long> configIdList){
        addListCanNull(configIdList, entity.getSpaceCategory());
        addListCanNull(configIdList, entity.getProfessionalPurpose());
        addListCanNull(configIdList, entity.getBusinessNature());
        addListCanNull(configIdList, entity.getOwnershipAttribute());
        addListCanNull(configIdList, entity.getResourceType());
        addListCanNull(configIdList, entity.getPropertyServiceLevel());
        addListCanNull(configIdList, entity.getLocationMethod());
    }

}
