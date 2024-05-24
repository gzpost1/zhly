package cn.cuiot.dmp.archive.application.service;

import cn.cuiot.dmp.archive.application.param.dto.ArchiveBatchUpdateDTO;
import cn.cuiot.dmp.archive.application.param.dto.HousesArchiveImportDto;
import cn.cuiot.dmp.archive.application.param.vo.HousesArchiveExportVo;
import cn.cuiot.dmp.archive.infrastructure.entity.HousesArchivesEntity;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 房屋档案表 服务类
 * </p>
 *
 * @author liujianyu
 * @since 2024-05-15
 */
public interface HousesArchivesService extends IService<HousesArchivesEntity> {

    /**
     * 参数校验
     */
    void checkParams(HousesArchivesEntity entity);

    /**
     * 导入参数校验
     */
    void checkParamsImport(HousesArchiveImportDto entity);

    /**
     * 按照id列表查询并且构造出导出用的列表
     */
    List<HousesArchiveExportVo> buildExportData(IdsParam param);

    /**
     * 导入数据
     */
    void importDataSave(List<HousesArchiveImportDto> dataList, Long loupanId);

}
