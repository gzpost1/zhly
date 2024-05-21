package cn.cuiot.dmp.archive.application.service.impl;

import cn.cuiot.dmp.archive.application.param.dto.BatchBuildingArchivesDTO;
import cn.cuiot.dmp.archive.application.param.dto.BuildingArchiveImportDTO;
import cn.cuiot.dmp.archive.application.param.dto.BuildingArchivesCreateDTO;
import cn.cuiot.dmp.archive.application.param.dto.BuildingArchivesUpdateDTO;
import cn.cuiot.dmp.archive.application.param.vo.BuildingArchivesExportVO;
import cn.cuiot.dmp.archive.application.param.vo.BuildingArchivesVO;
import cn.cuiot.dmp.archive.application.service.BuildingArchivesService;
import cn.cuiot.dmp.archive.domain.aggregate.BuildingArchives;
import cn.cuiot.dmp.archive.domain.aggregate.BuildingArchivesPageQuery;
import cn.cuiot.dmp.archive.domain.repository.BuildingArchivesRepository;
import cn.cuiot.dmp.base.application.service.ApiSystemService;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.utils.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/5/21
 */
@Slf4j
@Service
public class BuildingArchivesServiceImpl implements BuildingArchivesService {

    @Autowired
    private BuildingArchivesRepository buildingArchivesRepository;

    @Autowired
    private ApiSystemService apiSystemService;

    @Override
    public BuildingArchivesVO queryForDetail(Long id) {
        BuildingArchives buildingArchives = buildingArchivesRepository.queryForDetail(id);
        BuildingArchivesVO buildingArchivesVO = new BuildingArchivesVO();
        BeanUtils.copyProperties(buildingArchives, buildingArchivesVO);
        return buildingArchivesVO;
    }

    @Override
    public List<BuildingArchivesVO> queryForList(BuildingArchivesPageQuery pageQuery) {
        List<BuildingArchives> buildingArchivesList = buildingArchivesRepository.queryForList(pageQuery);
        if (CollectionUtils.isEmpty(buildingArchivesList)) {
            return new ArrayList<>();
        }
        return buildingArchivesList.stream()
                .map(o -> {
                    BuildingArchivesVO buildingArchivesVO = new BuildingArchivesVO();
                    BeanUtils.copyProperties(o, buildingArchivesVO);
                    return buildingArchivesVO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<BuildingArchivesExportVO> queryForExportList(BuildingArchivesPageQuery pageQuery) {
        AssertUtil.notNull(pageQuery.getDepartmentId(), "部门id不能为空");
        List<BuildingArchives> buildingArchivesList = buildingArchivesRepository.queryForList(pageQuery);
        if (CollectionUtils.isEmpty(buildingArchivesList)) {
            return new ArrayList<>();
        }
        DepartmentDto departmentDto = apiSystemService.lookUpDepartmentInfo(pageQuery.getDepartmentId(), null, null);
        AssertUtil.notNull(departmentDto, "部门不存在");
        String departmentName = departmentDto.getPathName();
        return buildingArchivesList.stream()
                .map(o -> {
                    BuildingArchivesExportVO buildingArchivesExportVO = new BuildingArchivesExportVO();
                    buildingArchivesExportVO.setId(o.getId().toString());
                    buildingArchivesExportVO.setName(o.getName());
                    buildingArchivesExportVO.setAreaName(o.getAreaName() + o.getAreaDetail());
                    buildingArchivesExportVO.setDepartmentName(departmentName);
                    buildingArchivesExportVO.setBuildingNum(o.getBuildingNum());
                    if (Objects.nonNull(o.getHouseNum())) {
                        buildingArchivesExportVO.setHouseNum(o.getHouseNum());
                    }
                    if (Objects.nonNull(o.getParkNum())) {
                        buildingArchivesExportVO.setParkNum(o.getParkNum());
                    }
                    if (Objects.nonNull(o.getStaffPhone())) {
                        buildingArchivesExportVO.setStaffPhone(o.getStaffPhone());
                    }
                    return buildingArchivesExportVO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<BuildingArchivesVO> queryForPage(BuildingArchivesPageQuery pageQuery) {
        PageResult<BuildingArchives> buildingArchivesPageResult = buildingArchivesRepository.queryForPage(pageQuery);
        if (CollectionUtils.isEmpty(buildingArchivesPageResult.getList())) {
            return new PageResult<>();
        }
        PageResult<BuildingArchivesVO> buildingArchivesVOPageResult = new PageResult<>();
        List<BuildingArchivesVO> buildingArchivesVOS = buildingArchivesPageResult.getList().stream()
                .map(o -> {
                    BuildingArchivesVO buildingArchivesVO = new BuildingArchivesVO();
                    BeanUtils.copyProperties(o, buildingArchivesVO);
                    return buildingArchivesVO;
                })
                .collect(Collectors.toList());
        BeanUtils.copyProperties(buildingArchivesPageResult, buildingArchivesVOPageResult);
        buildingArchivesVOPageResult.setList(buildingArchivesVOS);
        return buildingArchivesVOPageResult;
    }

    @Override
    public int saveBuildingArchives(BuildingArchivesCreateDTO createDTO) {
        BuildingArchives buildingArchives = new BuildingArchives();
        BeanUtils.copyProperties(createDTO, buildingArchives);
        return buildingArchivesRepository.saveBuildingArchives(buildingArchives);
    }

    @Override
    public int updateBuildingArchives(BuildingArchivesUpdateDTO updateDTO) {
        BuildingArchives buildingArchives = new BuildingArchives();
        BeanUtils.copyProperties(updateDTO, buildingArchives);
        return buildingArchivesRepository.updateBuildingArchives(buildingArchives);
    }

    @Override
    public int deleteBuildingArchives(Long id) {
        return buildingArchivesRepository.deleteBuildingArchives(id);
    }

    @Override
    public int batchUpdateBuildingArchives(BatchBuildingArchivesDTO batchBuildingArchivesDTO) {
        AssertUtil.notNull(batchBuildingArchivesDTO.getDepartmentId(), "所属部门不能为空");
        return buildingArchivesRepository.batchUpdateBuildingArchives(batchBuildingArchivesDTO.getDepartmentId(),
                batchBuildingArchivesDTO.getIdList());
    }

    @Override
    public int batchDeleteBuildingArchives(List<Long> idList) {
        return buildingArchivesRepository.batchDeleteBuildingArchives(idList);
    }

    @Override
    public void importBuildingArchives(List<BuildingArchiveImportDTO> buildingArchiveImportDTOList, Long companyId,
                                       Long departmentId) {

    }
}
