package cn.cuiot.dmp.archive.application.service;

import cn.cuiot.dmp.archive.application.param.dto.DeviceArchivesImportDto;
import cn.cuiot.dmp.archive.application.param.dto.ParkingArchivesImportDto;
import cn.cuiot.dmp.archive.application.param.vo.DeviceArchivesExportVo;
import cn.cuiot.dmp.archive.application.param.vo.ParkingArchivesExportVo;
import cn.cuiot.dmp.archive.infrastructure.entity.HousesArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.entity.ParkingArchivesEntity;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 车位档案表 服务类
 * </p>
 *
 * @author liujianyu
 * @since 2024-05-15
 */
public interface ParkingArchivesService extends IService<ParkingArchivesEntity> {

    /**
     * 参数校验
     */
    void checkParams(ParkingArchivesEntity entity);

    /**
     * 导入参数校验
     */
    void checkParamsImport(ParkingArchivesImportDto entity);

    /**
     *  构造出导出用的列表
     */
    List<ParkingArchivesExportVo> buildExportData(List<ParkingArchivesEntity> list);

    /**
     * 导入数据
     */
    void importDataSave(List<ParkingArchivesImportDto> dataList, Long loupanId, Long companyId);
}
