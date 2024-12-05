package cn.cuiot.dmp.archive.application.service;

import cn.cuiot.dmp.archive.application.param.dto.DeviceArchivesImportDto;
import cn.cuiot.dmp.archive.application.param.dto.HousesArchiveImportDto;
import cn.cuiot.dmp.archive.application.param.vo.DeviceArchivesExportVo;
import cn.cuiot.dmp.archive.application.param.vo.HousesArchiveExportVo;
import cn.cuiot.dmp.archive.infrastructure.entity.DeviceArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.entity.HousesArchivesEntity;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 设备档案表 服务类
 * </p>
 *
 * @author liujianyu
 * @since 2024-05-15
 */
public interface DeviceArchivesService extends IService<DeviceArchivesEntity> {

    /**
     * 参数校验
     */
    void checkParams(DeviceArchivesEntity entity);

    /**
     * 导入参数校验
     */
    void checkParamsImport(DeviceArchivesImportDto entity);

    /**
     * 按照id列表查询并且构造出导出用的列表
     */
    List<DeviceArchivesExportVo> buildExportData(List<DeviceArchivesEntity> list);

    /**
     * 导入数据
     */
    void importDataSave(List<DeviceArchivesImportDto> dataList, Long loupanId, Long companyId);
}
