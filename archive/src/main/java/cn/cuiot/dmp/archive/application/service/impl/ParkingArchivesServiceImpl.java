package cn.cuiot.dmp.archive.application.service.impl;

import cn.cuiot.dmp.archive.application.param.dto.ParkingArchivesImportDto;
import cn.cuiot.dmp.archive.application.param.vo.BuildingArchivesVO;
import cn.cuiot.dmp.archive.application.param.vo.ParkingArchivesExportVo;
import cn.cuiot.dmp.archive.application.service.ParkingArchivesService;
import cn.cuiot.dmp.archive.infrastructure.entity.ParkingArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.ParkingArchivesMapper;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.common.constant.CustomConfigConstant;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.SystemOptionTypeEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 车位档案表 服务实现类
 * </p>
 *
 * @author liujianyu
 * @since 2024-05-15
 */
@Service("parkingArchivesService")
public class ParkingArchivesServiceImpl extends ServiceImpl<ParkingArchivesMapper, ParkingArchivesEntity> implements ParkingArchivesService {

    @Autowired
    private BuildingAndConfigCommonUtilService buildingAndConfigCommonUtilService;

    /**
     * 参数校验
     */
    @Override
    public void checkParams(ParkingArchivesEntity entity) {
        // 必填判断
        if (StringUtils.isBlank(entity.getCode())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "车位编号不可为空");
        }
        if (Objects.isNull(entity.getLoupanId())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "所属楼盘不可为空");
        }
        if (Objects.isNull(entity.getArea())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "所属区域不可为空");
        }
        if (Objects.isNull(entity.getUsageStatus())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "使用情况不可为空");
        }
        if (Objects.isNull(entity.getStatus())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "状态不可为空");
        }
        if (Objects.isNull(entity.getParkingType())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "车位类型不可为空");
        }

        // 规则判断
        if (entity.getCode().length() > 30) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "车位编号长度不可超过30");
        }
        if (StringUtils.isNotBlank(entity.getRemarks()) && entity.getRemarks().length() > 100){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "备注长度不可超过100");
        }
    }

    @Override
    public void checkParamsImport(ParkingArchivesImportDto entity) {
        // 必填判断
        if (StringUtils.isBlank(entity.getCode())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "车位编号不可为空");
        }
        if (StringUtils.isBlank(entity.getAreaName())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "区域不可为空");
        }
        if (StringUtils.isBlank(entity.getParkingTypeName())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "车位类型不可为空");
        }
        if (StringUtils.isBlank(entity.getUsageStatusName())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "使用情况不可为空");
        }

        // 规则判断
        if (entity.getCode().length() > 30) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "车位编号长度不可超过30");
        }
    }

    @Override
    public List<ParkingArchivesExportVo> buildExportData(List<ParkingArchivesEntity> list) {
        // 查询列表信息
        List<ParkingArchivesExportVo> res = new ArrayList<>(list.size());

        // TODO: 2024/5/16 等曹睿接口出来，就可以查询楼盘和配置
        // 查询楼盘信息-用于楼盘id转换为楼盘名称-汇总成Map
        Map<Long, String> loupanIdNameMap = buildingAndConfigCommonUtilService.getLoupanIdNameMap(list.stream().map(ParkingArchivesEntity::getLoupanId).collect(Collectors.toSet()));
        // 查询配置信息-用于配置id转换为配置名称-汇总成Map
        Set<Long> configIdList = new HashSet<>();
        list.forEach(entity -> {
            getConfigIdFromEntity(entity, configIdList);
        });
        Map<Long, String> configIdNameMap = buildingAndConfigCommonUtilService.getConfigIdNameMap(configIdList);

        // 构造导出列表
        list.forEach(entity -> {
            ParkingArchivesExportVo vo = new ParkingArchivesExportVo();
            vo.setCode(entity.getCode());
            vo.setAreaName(configIdNameMap.getOrDefault(entity.getArea(), ""));
            vo.setParkingTypeName(configIdNameMap.getOrDefault(entity.getParkingType(), ""));
            vo.setStatusName(getStatusName(entity.getStatus()));
            vo.setUsageStatusName(configIdNameMap.getOrDefault(entity.getUsageStatus(), ""));
            vo.setRemarks(entity.getRemarks());

            BuildingArchivesVO buildingArchivesVO = entity.getBuildingArchivesVO();
            String loupanName = buildingArchivesVO.getName();
            String deptName = buildingArchivesVO.getDeptName();
            vo.setLoupanName(loupanName);
            vo.setDeptName(deptName);
            res.add(vo);
        });

        return res;
    }

    @Override
    public void importDataSave(List<ParkingArchivesImportDto> dataList, Long loupanId, Long companyId) {
        // TODO: 2024/5/16 等曹睿接口出来，就可以查询楼盘和配置
        // 先查询所属楼盘，如果查不到，就报错，查到生成map-nameIdMap
        // Map<String, Long> nameIdMap = buildingAndConfigCommonUtilService.getLoupanNameIdMap(dataList.stream().map(ParkingArchivesImportDto::getLoupanName).collect(Collectors.toSet()));
        // 查询指定配置的数据，如果有配置，查询生成map-nameConfigIdMap
        Map<String, Map<String, Long>> nameConfigIdMap = buildingAndConfigCommonUtilService.getConfigNameIdMap(companyId, SystemOptionTypeEnum.PARK_ARCHIVE.getCode());

        // 构造插入列表进行保存
        List<ParkingArchivesEntity> list = new ArrayList<>();
        dataList.forEach(data -> {
            ParkingArchivesEntity entity = new ParkingArchivesEntity();
            entity.setArea(checkConfigTypeNull(nameConfigIdMap.get(CustomConfigConstant.PARKING_ARCHIVES_INIT.get(0)), data.getAreaName()));
            entity.setCode(data.getCode());
            entity.setUsageStatus(checkConfigTypeNull(nameConfigIdMap.get(CustomConfigConstant.PARKING_ARCHIVES_INIT.get(1)), data.getUsageStatusName()));
            entity.setParkingType(checkConfigTypeNull(nameConfigIdMap.get(CustomConfigConstant.PARKING_ARCHIVES_INIT.get(2)), data.getParkingTypeName()));
            entity.setLoupanId(loupanId);
            entity.setStatus(getStatusFromName(data.getStatusName()));
            // TODO: 2024/5/16 这里还需要基于不同的一级类目去查询配置
            list.add(entity);
        });

        this.saveBatch(list);
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

    public ParkingArchivesEntity queryForDetail(Long id) {
        // 查询当前id的信息
        ParkingArchivesEntity entity = getById(id);
        Set<Long> configIdList = new HashSet<>();
        addListCanNull(configIdList, entity.getArea());
        addListCanNull(configIdList, entity.getUsageStatus() );
        addListCanNull(configIdList, entity.getParkingType());

        if (StringUtils.isNotBlank(entity.getImage())) {
            entity.setImageList(Collections.singletonList(entity.getImage()));
        }

        // 查询楼盘名称
        Set<Long> loupanIdSet = new HashSet<>();
        loupanIdSet.add(entity.getLoupanId());
        Map<Long, String> loupanIdNameMap = buildingAndConfigCommonUtilService.getLoupanIdNameMap(loupanIdSet);
        entity.setLoupanIdName(loupanIdNameMap.get(entity.getLoupanId()));

        // 查询对应的配置名称，做配置名称匹配
        final Map<Long, String> configIdNameMap = buildingAndConfigCommonUtilService.getConfigIdNameMap(configIdList);
        entity.setAreaName(configIdNameMap.get(entity.getArea()));
        entity.setUsageStatusName(configIdNameMap.get(entity.getUsageStatus()));
        entity.setParkingTypeName(configIdNameMap.get(entity.getParkingType()));
        return entity;
    }

    private void addListCanNull(Set<Long> configIdList, Long configId){
        if (Objects.nonNull(configId)){
            configIdList.add(configId);
        }
    }

    private void getConfigIdFromEntity(ParkingArchivesEntity entity, Set<Long> configIdList){
        addListCanNull(configIdList, entity.getArea());
        addListCanNull(configIdList, entity.getUsageStatus() );
        addListCanNull(configIdList, entity.getParkingType());
    }

}
