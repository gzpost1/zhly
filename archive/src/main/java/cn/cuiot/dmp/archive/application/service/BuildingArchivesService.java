package cn.cuiot.dmp.archive.application.service;

import cn.cuiot.dmp.archive.application.param.dto.BatchBuildingArchivesDTO;
import cn.cuiot.dmp.archive.application.param.dto.BuildingArchiveImportDTO;
import cn.cuiot.dmp.archive.application.param.dto.BuildingArchivesCreateDTO;
import cn.cuiot.dmp.archive.application.param.dto.BuildingArchivesUpdateDTO;
import cn.cuiot.dmp.archive.application.param.vo.BuildingArchivesExportVO;
import cn.cuiot.dmp.archive.application.param.vo.BuildingArchivesVO;
import cn.cuiot.dmp.archive.domain.aggregate.BuildingArchives;
import cn.cuiot.dmp.archive.domain.aggregate.BuildingArchivesPageQuery;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.req.CustomerUseReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CustomerUserRspDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.DepartmentTreeRspDTO;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.PageResult;

import java.util.List;

/**
 * @author caorui
 * @date 2024/5/21
 */
public interface BuildingArchivesService {

    /**
     * 根据id获取楼盘档案详情
     */
    BuildingArchivesVO queryForDetail(Long id);

    /**
     * 根据条件获取楼盘档案详情列表
     */
    List<BuildingArchivesVO> queryForList(BuildingArchivesPageQuery pageQuery);

    /**
     * 根据条件获取楼盘档案导出详情列表
     */
    List<BuildingArchivesExportVO> queryForExportList(BuildingArchivesPageQuery pageQuery);

    /**
     * 根据条件获取楼盘档案详情分页列表
     */
    PageResult<BuildingArchivesVO> queryForPage(BuildingArchivesPageQuery pageQuery);

    /**
     * 保存
     */
    int saveBuildingArchives(BuildingArchivesCreateDTO createDTO);

    /**
     * 更新
     */
    int updateBuildingArchives(BuildingArchivesUpdateDTO updateDTO);

    /**
     * 删除
     */
    int deleteBuildingArchives(Long id);

    /**
     * 批量更新
     */
    int batchUpdateBuildingArchives(BatchBuildingArchivesDTO batchBuildingArchivesDTO);

    /**
     * 批量删除
     */
    int batchDeleteBuildingArchives(List<Long> idList);

    /**
     * 导入
     */
    void importBuildingArchives(List<BuildingArchiveImportDTO> buildingArchiveImportDTOList, Long companyId,
                                Long departmentId, Long userId);

    /**
     * 获取组织楼盘树
     */
    List<DepartmentTreeRspDTO> getDepartmentBuildingTree(Long orgId, Long userId,String keyWords);

    List<BuildingArchive> apiQueryForList(BuildingArchiveReq buildingArchiveReq);

    /**
     * 根据ID获取楼盘信息
     * @param id
     * @return
     */
    BuildingArchive lookupBuildingArchiveInfo(Long id);

    /**
     * 查询当前组织及下级组织下的楼盘列表
     */
    List<BuildingArchive> lookupBuildingArchiveByDepartmentList(DepartmentReqDto reqDto);

}
