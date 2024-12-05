package cn.cuiot.dmp.archive.application.service;

import cn.cuiot.dmp.archive.application.param.dto.RoomArchivesImportDto;
import cn.cuiot.dmp.archive.application.param.vo.RoomArchivesExportVo;
import cn.cuiot.dmp.archive.infrastructure.entity.RoomArchivesEntity;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 空间档案表 服务类
 * </p>
 *
 * @author liujianyu
 * @since 2024-05-15
 */
public interface RoomArchivesService extends IService<RoomArchivesEntity> {

    /**
     * 参数校验
     */
    void checkParams(RoomArchivesEntity entity);

    /**
     * 导入参数校验
     */
    void checkParamsImport(RoomArchivesImportDto entity);

    /**
     * 构造出导出用的列表
     */
    List<RoomArchivesExportVo> buildExportData(List<RoomArchivesEntity> list);

    /**
     * 导入数据
     */
    void importDataSave(List<RoomArchivesImportDto> dataList, Long loupanId, Long companyId);
}
