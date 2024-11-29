package cn.cuiot.dmp.archive.application.service;

import cn.cuiot.dmp.archive.application.param.dto.ArchiveBatchUpdateDTO;
import cn.cuiot.dmp.archive.application.param.dto.HouseTreeQueryDto;
import cn.cuiot.dmp.archive.application.param.dto.HousesArchiveImportDto;
import cn.cuiot.dmp.archive.application.param.vo.HousesArchiveExportVo;
import cn.cuiot.dmp.archive.infrastructure.entity.HousesArchivesEntity;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.IdsReq;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.DepartmentTreeRspDTO;
import cn.cuiot.dmp.base.infrastructure.model.HousesArchivesVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

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
    List<HousesArchiveExportVo> buildExportData(List<HousesArchivesEntity> list);

    /**
     * 导入数据
     */
    void importDataSave(List<HousesArchiveImportDto> dataList, Long loupanId, Long companyId);


    /**
     * 根据房屋ids查询房屋信息
     *
     * @param ids
     * @return
     */
    List<HousesArchivesVo> queryHousesList(IdsReq ids);

    /**
     * 获取组织楼盘房屋树
     */
    List<DepartmentTreeRspDTO> getDepartmentBuildingHouseTree(HouseTreeQueryDto houseTreeQueryDto);

    void fullContractInfo(List<HousesArchivesEntity> records);

    void fillBuildingName(List<HousesArchivesEntity> records);

}
