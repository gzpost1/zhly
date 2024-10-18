package cn.cuiot.dmp.archive.application.service.impl;

import cn.cuiot.dmp.archive.application.constant.BuildingArchivesConstant;
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
import cn.cuiot.dmp.archive.infrastructure.entity.BuildingArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.ArchivesApiMapper;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.BuildingArchivesMapper;
import cn.cuiot.dmp.base.application.service.ApiSystemService;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.DepartmentTreeRspDTO;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.enums.SystemOptionTypeEnum;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author caorui
 * @date 2024/5/21
 */
@Slf4j
@Service("buildingArchivesService")
public class BuildingArchivesServiceImpl implements BuildingArchivesService {

    @Autowired
    private BuildingArchivesRepository buildingArchivesRepository;

    @Autowired
    private ApiSystemService apiSystemService;

    @Autowired
    private ArchivesApiMapper archivesApiMapper;
    @Autowired
    private BuildingArchivesMapper buildingArchivesMapper;

    @Override
    public BuildingArchivesVO queryForDetail(Long id) {
        BuildingArchives buildingArchives = buildingArchivesRepository.queryForDetail(id);
        BuildingArchivesVO buildingArchivesVO = new BuildingArchivesVO();
        BeanUtils.copyProperties(buildingArchives, buildingArchivesVO);
        buildingArchivesVO.setQrCodeId(archivesApiMapper.getCodeId(id, SystemOptionTypeEnum.BUILDING_ARCHIVE.getCode()));
        buildingArchivesVO.setStatusName(EntityConstants.ENABLED.equals(buildingArchives.getStatus()) ?
                "启用" : "停用");
        DepartmentDto departmentDto = apiSystemService.lookUpDepartmentInfo(buildingArchives.getDepartmentId(),
                null, null);
        AssertUtil.notNull(departmentDto, "部门不存在");
        buildingArchivesVO.setDepartmentName(departmentDto.getPathName());
        return buildingArchivesVO;
    }

    @Override
    public List<BuildingArchivesVO> queryForList(BuildingArchivesPageQuery pageQuery) {

        if(CollectionUtils.isEmpty(pageQuery.getDepartmentIdList())){
            if(Objects.nonNull(pageQuery.getDepartmentId())){
                DepartmentReqDto paraDto = new DepartmentReqDto();
                paraDto.setDeptId(pageQuery.getDepartmentId());
                paraDto.setSelfReturn(true);
                List<DepartmentDto> departmentDtoList = apiSystemService.lookUpDepartmentChildList(paraDto);
                List<Long> departmentIdList = departmentDtoList.stream()
                        .map(DepartmentDto::getId)
                        .collect(Collectors.toList());
                pageQuery.setDepartmentIdList(departmentIdList);
            }
        }

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
        List<BuildingArchives> buildingArchivesList = buildingArchivesRepository.queryForList(pageQuery);
        if (CollectionUtils.isEmpty(buildingArchivesList)) {
            return new ArrayList<>();
        }
        DepartmentDto departmentDto = apiSystemService.lookUpDepartmentInfo(buildingArchivesList.get(0).getDepartmentId(), null, null);
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
        DepartmentDto departmentDto = apiSystemService.lookUpDepartmentInfo(pageQuery.getDepartmentId(), null, null);
        AssertUtil.notNull(departmentDto, "部门不存在");
        String departmentName = departmentDto.getPathName();
        List<BuildingArchivesVO> buildingArchivesVOS = buildingArchivesPageResult.getList().stream()
                .map(o -> {
                    BuildingArchivesVO buildingArchivesVO = new BuildingArchivesVO();
                    BeanUtils.copyProperties(o, buildingArchivesVO);
                    buildingArchivesVO.setDepartmentName(departmentName);
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
        checkDelete(id);
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
                                       Long departmentId, Long userId) {
        AssertUtil.notNull(companyId, "企业ID不能为空");
        AssertUtil.notNull(departmentId, "部门ID不能为空");
        List<BuildingArchives> buildingArchivesList = buildingArchiveImportDTOList.stream()
                .map(o -> {
                    BuildingArchives buildingArchives = new BuildingArchives();
                    BeanUtils.copyProperties(o, buildingArchives);
                    // 导入的楼盘默认为北京朝阳区
                    buildingArchives.setAreaCode("110105000000");
                    buildingArchives.setAreaName("北京市市辖区朝阳区");
                    buildingArchives.setCompanyId(companyId);
                    buildingArchives.setDepartmentId(departmentId);
                    return buildingArchives;
                })
                .collect(Collectors.toList());
        buildingArchivesRepository.batchSaveBuildingArchives(buildingArchivesList, userId);
    }

    @Override
    public List<DepartmentTreeRspDTO> getDepartmentBuildingTree(Long orgId, Long userId,String keyWords) {
        AssertUtil.notNull(orgId, "企业id不能为空");
        AssertUtil.notNull(userId, "用户id不能为空");
        List<DepartmentTreeRspDTO> departmentTreeRspList = apiSystemService.lookUpDepartmentTree(orgId, userId);
        if (CollectionUtils.isEmpty(departmentTreeRspList)) {
            return new ArrayList<>();
        }
        List<Long> departmentIdList = getDepartmentIdList(departmentTreeRspList.get(0));
        BuildingArchivesPageQuery pageQuery = new BuildingArchivesPageQuery();
        pageQuery.setDepartmentIdList(departmentIdList);
        pageQuery.setName(keyWords);
        List<BuildingArchives> buildingArchivesList = buildingArchivesRepository.queryForList(pageQuery);
        fillDepartmentBuildingTree(departmentTreeRspList.get(0), buildingArchivesList);
        return departmentTreeRspList;
    }

    @Override
    public List<BuildingArchive> apiQueryForList(BuildingArchiveReq buildingArchiveReq) {
        LambdaQueryWrapper<BuildingArchivesEntity> queryWrapper = new LambdaQueryWrapper<BuildingArchivesEntity>()
                .in(CollectionUtils.isNotEmpty(buildingArchiveReq.getIdList()), BuildingArchivesEntity::getId, buildingArchiveReq.getIdList());
        List<BuildingArchivesEntity> buildingArchivesEntityList = buildingArchivesMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(buildingArchivesEntityList)) {
            return new ArrayList<>();
        }
        return buildingArchivesEntityList.stream().map(item -> {
            BuildingArchive buildingArchive = new BuildingArchive();
            BeanUtils.copyProperties(item, buildingArchive);
            return buildingArchive;
        }).collect(Collectors.toList());
    }

    private List<Long> getDepartmentIdList(DepartmentTreeRspDTO rootTreeNode) {
        List<Long> treeIdList = new ArrayList<>();
        treeIdList.add(rootTreeNode.getId());
        rootTreeNode.setType(BuildingArchivesConstant.DEPARTMENT_TYPE);
        if (CollectionUtils.isNotEmpty(rootTreeNode.getChildren())) {
            for (DepartmentTreeRspDTO child : rootTreeNode.getChildren()) {
                treeIdList.addAll(getDepartmentIdList(child));
            }
        }
        return treeIdList;
    }

    private void fillDepartmentBuildingTree(DepartmentTreeRspDTO rootTreeNode, List<BuildingArchives> buildingArchivesList) {
        for (BuildingArchives buildingArchives : buildingArchivesList) {
            if (rootTreeNode.getId().equals(buildingArchives.getDepartmentId())) {
                DepartmentTreeRspDTO departmentTreeRspDTO = new DepartmentTreeRspDTO();
                departmentTreeRspDTO.setId(buildingArchives.getId());
                departmentTreeRspDTO.setDepartmentName(buildingArchives.getName());
                departmentTreeRspDTO.setType(BuildingArchivesConstant.BUILDING_ARCHIVES_TYPE);
                departmentTreeRspDTO.setParentId(rootTreeNode.getId());
                departmentTreeRspDTO.setChildren(new ArrayList<>());
                rootTreeNode.getChildren().add(departmentTreeRspDTO);
            }
        }
        if (CollectionUtils.isNotEmpty(rootTreeNode.getChildren())) {
            for (DepartmentTreeRspDTO child : rootTreeNode.getChildren()) {
                fillDepartmentBuildingTree(child, buildingArchivesList);
            }
        }
    }

    private void checkDelete(Long buildingId) {
        List<Integer> countList = archivesApiMapper.countArchiveByBuildingId(buildingId);
        for (Integer count : countList) {
            AssertUtil.isTrue(count == 0, "楼盘档案下存在关联的房屋/空间/设备/车位档案，不能删除");
        }
    }

    /**
     * 根据ID获取楼盘信息
     *
     * @param id
     * @return
     */
    @Override
    public BuildingArchive lookupBuildingArchiveInfo(Long id) {
        BuildingArchives buildingArchives = buildingArchivesRepository.queryForDetail(id);
        if (Objects.nonNull(buildingArchives)) {
            BuildingArchive result = new BuildingArchive();
            BeanUtils.copyProperties(buildingArchives, result);
            return result;
        }
        return null;
    }

    @Override
    public List<BuildingArchive> lookupBuildingArchiveByDepartmentList(DepartmentReqDto reqDto) {
        DepartmentReqDto paraDto = new DepartmentReqDto();
        paraDto.setDeptId(reqDto.getDeptId());
        if(Objects.isNull(LoginInfoHolder.getCurrentDeptId())){
            paraDto.setDeptId(reqDto.getDeptId());
        }

        paraDto.setSelfReturn(true);
        List<DepartmentDto> departmentDtoList = apiSystemService.lookUpDepartmentChildList(paraDto);
        List<Long> departmentIdList = departmentDtoList.stream()
                .map(DepartmentDto::getId)
                .collect(Collectors.toList());
        BuildingArchivesPageQuery pageQuery = new BuildingArchivesPageQuery();
        pageQuery.setDepartmentIdList(departmentIdList);
        List<BuildingArchives> buildingArchivesList = buildingArchivesRepository.queryForList(pageQuery);
        if (CollectionUtils.isEmpty(buildingArchivesList)) {
            return new ArrayList<>();
        }
        return buildingArchivesList.stream()
                .map(o -> {
                    BuildingArchive buildingArchive = new BuildingArchive();
                    BeanUtils.copyProperties(o, buildingArchive);
                    return buildingArchive;
                })
                .collect(Collectors.toList());
    }
    @Override
    public Long quertOrgIdByHouse(Long houseId){
        return buildingArchivesRepository.quertOrgIdByHouse(houseId);
    }
}
