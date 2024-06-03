package cn.cuiot.dmp.archive.application.service.impl;

import cn.cuiot.dmp.archive.application.param.dto.DeviceArchivesImportDto;
import cn.cuiot.dmp.archive.application.param.vo.DeviceArchivesExportVo;
import cn.cuiot.dmp.archive.application.service.DeviceArchivesService;
import cn.cuiot.dmp.archive.infrastructure.entity.DeviceArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.DeviceArchivesMapper;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.common.constant.CustomConfigConstant;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.SystemOptionTypeEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备档案表 服务实现类
 * </p>
 *
 * @author liujianyu
 * @since 2024-05-15
 */
@Service("deviceArchivesService")
public class DeviceArchivesServiceImpl extends ServiceImpl<DeviceArchivesMapper, DeviceArchivesEntity> implements DeviceArchivesService {

    @Autowired
    private BuildingAndConfigCommonUtilService buildingAndConfigCommonUtilService;

    /**
     * 参数校验
     */
    @Override
    public void checkParams(DeviceArchivesEntity entity) {
        // 必填判断
        if (StringUtils.isBlank(entity.getDeviceName())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "设备名称不可为空");
        }
        if (Objects.isNull(entity.getLoupanId())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "所属楼盘不可为空");
        }
        if (Objects.isNull(entity.getDeviceCategory())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "设备类别不可为空");
        }
        if (StringUtils.isBlank(entity.getInstallationLocation())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "安装位置不可为空");
        }
        if (Objects.isNull(entity.getInstallationDate())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "安装日期不可为空");
        }

        // 规则判断
        if (entity.getDeviceName().length() > 30) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "设备名称长度不可超过30");
        }
        if (entity.getInstallationLocation().length() > 30) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "安装位置长度不可超过30");
        }
        if (StringUtils.isNotBlank(entity.getDeviceSystem()) && entity.getDeviceSystem().length() > 30) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "设备系统长度不可超过30");
        }

        if (StringUtils.isNotBlank(entity.getDeviceProfessional()) && entity.getDeviceProfessional().length() > 30) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "设备专业长度不可超过30");
        }

        if (StringUtils.isNotBlank(entity.getLocationDetails()) && entity.getLocationDetails().length() > 30){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "详细位置长度不可超过30");
        }


    }

    @Override
    public void checkParamsImport(DeviceArchivesImportDto entity) {
        // 必填判断
        if (StringUtils.isBlank(entity.getDeviceName())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "设备名称不可为空");
        }
        if (StringUtils.isBlank(entity.getDeviceCategoryName())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "设备类别不可为空");
        }
        if (StringUtils.isBlank(entity.getInstallationLocation())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "安装位置不可为空");
        }
        if (StringUtils.isBlank(entity.getInstallationDateName())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "安装日期不可为空");
        }

        // 规则判断
        if (entity.getDeviceName().length() > 30) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "设备名称长度不可超过30");
        }
        if (entity.getInstallationLocation().length() > 30) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "安装位置长度不可超过30");
        }
    }

    @Override
    public List<DeviceArchivesExportVo> buildExportData(IdsParam param) {
        // 查询列表信息
        List<DeviceArchivesEntity> list = this.listByIds(param.getIds());
        List<DeviceArchivesExportVo> res = new ArrayList<>(list.size());

        // TODO: 2024/5/16 等曹睿接口出来，就可以查询楼盘和配置
        // 查询楼盘信息-用于楼盘id转换为楼盘名称-汇总成Map
        Map<Long, String> loupanIdNameMap = buildingAndConfigCommonUtilService.getLoupanIdNameMap(list.stream().map(DeviceArchivesEntity::getLoupanId).collect(Collectors.toSet()));
        // 查询配置信息-用于配置id转换为配置名称-汇总成Map
        Set<Long> configIdList = new HashSet<>();
        list.forEach(entity -> {
            getConfigIdFromEntity(entity, configIdList);
        });
        Map<Long, String> configIdNameMap = buildingAndConfigCommonUtilService.getConfigIdNameMap(configIdList);

        // 构造导出列表
        list.forEach(entity -> {
            DeviceArchivesExportVo vo = new DeviceArchivesExportVo();
            vo.setIdStr(entity.getId().toString());
            vo.setDeviceName(entity.getDeviceName());
            vo.setDeviceSystem(entity.getDeviceSystem());
            vo.setDeviceCategoryName(configIdNameMap.getOrDefault(entity.getDeviceCategory(), ""));
            vo.setDeviceProfessional(entity.getDeviceProfessional());
            vo.setInstallationLocation(entity.getInstallationLocation());
            vo.setDeviceStatusString(configIdNameMap.getOrDefault(entity.getDeviceStatus(), ""));
            res.add(vo);
        });

        return res;
    }

    @Override
    public void importDataSave(List<DeviceArchivesImportDto> dataList, Long loupanId, Long companyId) {
        // TODO: 2024/5/16 等曹睿接口出来，就可以查询楼盘和配置
        // 先查询所属楼盘，如果查不到，就报错，查到生成map-nameIdMap
        //Map<String, Long> nameIdMap = buildingAndConfigCommonUtilService.getLoupanNameIdMap(dataList.stream().map(DeviceArchivesImportDto::getLoupanName).collect(Collectors.toSet()));
        // 查询指定配置的数据，如果有配置，查询生成map-nameConfigIdMap
        Map<String, Map<String, Long>> nameConfigIdMap = buildingAndConfigCommonUtilService.getConfigNameIdMap(companyId, SystemOptionTypeEnum.DEVICE_ARCHIVE.getCode());

        // 构造插入列表进行保存
        List<DeviceArchivesEntity> list = new ArrayList<>();
        dataList.forEach(data -> {
            DeviceArchivesEntity entity = new DeviceArchivesEntity();
            entity.setDeviceName(data.getDeviceName());
            entity.setDeviceCategory(checkConfigTypeNull(nameConfigIdMap.get(CustomConfigConstant.DEVICE_ARCHIVES_INIT.get(0)), data.getDeviceCategoryName()));
            entity.setInstallationLocation(data.getInstallationLocation());
            entity.setInstallationDate(getDate(data.getInstallationDateName()));
            entity.setLoupanId(loupanId);
            list.add(entity);
        });

        this.saveBatch(list);
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

    private LocalDate getDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "安装日期不存在");
        }
        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e){
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "安装日期格式不对");
        }
    }

    public DeviceArchivesEntity queryForDetail(Long id) {
        // 查询当前id的信息
        DeviceArchivesEntity entity = getById(id);
        Set<Long> configIdList = new HashSet<>();
        addListCanNull(configIdList, entity.getDeviceCategory());
        addListCanNull(configIdList, entity.getDeviceStatus());
        addListCanNull(configIdList, entity.getPropertyServiceLevel());
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
        entity.setDeviceCategoryName(configIdNameMap.get(entity.getDeviceCategory()));
        entity.setDeviceStatusName(configIdNameMap.get(entity.getDeviceStatus()));
        entity.setPropertyServiceLevelName(configIdNameMap.get(entity.getPropertyServiceLevel()));
        return entity;
    }

    private void addListCanNull(Set<Long> configIdList, Long configId){
        if (Objects.nonNull(configId)){
            configIdList.add(configId);
        }
    }

    private void getConfigIdFromEntity(DeviceArchivesEntity entity, Set<Long> configIdList){
        addListCanNull(configIdList, entity.getDeviceCategory());
        addListCanNull(configIdList, entity.getDeviceStatus());
        addListCanNull(configIdList, entity.getPropertyServiceLevel());
    }

}
