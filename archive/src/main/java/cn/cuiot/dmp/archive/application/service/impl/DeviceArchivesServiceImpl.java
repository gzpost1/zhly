package cn.cuiot.dmp.archive.application.service.impl;

import cn.cuiot.dmp.archive.application.param.dto.DeviceArchivesImportDto;
import cn.cuiot.dmp.archive.application.param.vo.DeviceArchivesExportVo;
import cn.cuiot.dmp.archive.application.service.BuildingArchivesService;
import cn.cuiot.dmp.archive.application.service.DeviceArchivesService;
import cn.cuiot.dmp.archive.infrastructure.entity.BuildingArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.entity.DeviceArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.BuildingArchivesMapper;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.DeviceArchivesMapper;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
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
        Map<Long, String> configIdNameMap = new HashMap<>();

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
    public void importDataSave(List<DeviceArchivesImportDto> dataList, Long loupanId) {
        // TODO: 2024/5/16 等曹睿接口出来，就可以查询楼盘和配置
        // 先查询所属楼盘，如果查不到，就报错，查到生成map-nameIdMap
        //Map<String, Long> nameIdMap = buildingAndConfigCommonUtilService.getLoupanNameIdMap(dataList.stream().map(DeviceArchivesImportDto::getLoupanName).collect(Collectors.toSet()));
        // 查询指定配置的数据，如果有配置，查询生成map-nameConfigIdMap
        Map<String, Long> nameConfigIdMap = new HashMap<>();

        // 构造插入列表进行保存
        List<DeviceArchivesEntity> list = new ArrayList<>();
        dataList.forEach(data -> {
            DeviceArchivesEntity entity = new DeviceArchivesEntity();
            entity.setDeviceName(data.getDeviceName());
            entity.setDeviceCategory(checkConfigTypeNull(nameConfigIdMap, data.getDeviceCategoryName()));
            entity.setInstallationLocation(data.getInstallationLocation());
            entity.setInstallationDate(getDate(data.getInstallationDateName()));
            entity.setLoupanId(loupanId);
            // TODO: 2024/5/16 这里还需要基于不同的一级类目去查询配置
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
        return LocalDate.parse(dateStr);
    }

    public DeviceArchivesEntity queryForDetail(Long id) {
        return getById(id);
    }

}
