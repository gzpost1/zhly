package cn.cuiot.dmp.archive.application.service.impl;

import cn.cuiot.dmp.archive.application.constant.BuildingArchivesConstant;
import cn.cuiot.dmp.archive.application.param.dto.BatchBuildingArchivesDTO;
import cn.cuiot.dmp.archive.application.param.dto.BuildingArchiveImportDTO;
import cn.cuiot.dmp.archive.application.param.dto.BuildingArchivesCreateDTO;
import cn.cuiot.dmp.archive.application.param.dto.BuildingArchivesUpdateDTO;
import cn.cuiot.dmp.archive.application.param.vo.ArchivesStatisticVO;
import cn.cuiot.dmp.archive.application.param.vo.BuildingArchivesExportVO;
import cn.cuiot.dmp.archive.application.param.vo.BuildingArchivesVO;
import cn.cuiot.dmp.archive.application.service.BuildingArchivesService;
import cn.cuiot.dmp.archive.domain.aggregate.BuildingArchives;
import cn.cuiot.dmp.archive.domain.aggregate.BuildingArchivesPageQuery;
import cn.cuiot.dmp.archive.domain.repository.BuildingArchivesRepository;
import cn.cuiot.dmp.archive.infrastructure.entity.BuildingArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.entity.HousesArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.ArchivesApiMapper;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.BuildingArchivesMapper;
import cn.cuiot.dmp.base.application.service.ApiSystemService;
import cn.cuiot.dmp.base.application.utils.CalculateUtils;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.DepartmentTreeRspDTO;
import cn.cuiot.dmp.base.infrastructure.feign.ContractFeignService;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.CustomConfigConstant;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.enums.SystemOptionTypeEnum;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.math.BigDecimal;
import java.util.*;
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

    @Autowired
    private ContractFeignService contractFeignService;
    @Autowired
    private BuildingAndConfigCommonUtilService buildingAndConfigCommonUtilService;

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
        buildingArchivesVO.setDeptName(departmentDto.getName());
        return buildingArchivesVO;
    }

    @Override
    public List<BuildingArchivesVO> queryForList(BuildingArchivesPageQuery pageQuery) {

        if (CollectionUtils.isEmpty(pageQuery.getDepartmentIdList())) {
            if (Objects.nonNull(pageQuery.getDepartmentId())) {
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
                    buildingArchivesVO.setDepartmentName(o.getName());
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

    private void addListCanNull(Set<Long> configIdList, Long configId) {
        if (Objects.nonNull(configId)) {
            configIdList.add(configId);
        }
    }

    private void getConfigIdFromEntity(BuildingArchivesVO entity, Set<Long> configIdList) {
        addListCanNull(configIdList, entity.getType());
    }

    @Override
    public List<BuildingArchivesExportVO> buildExportData(List<BuildingArchivesVO> list) {
        // 查询配置信息-用于配置id转换为配置名称-汇总成Map
        Set<Long> configIdList = new HashSet<>();
        list.forEach(entity -> {
            getConfigIdFromEntity(entity, configIdList);
        });
        // 查询指定配置的数据，如果有配置，查询生成map-nameConfigIdMap
        Map<Long, String> configIdNameMap = buildingAndConfigCommonUtilService.getConfigIdNameMap(configIdList);
        List<BuildingArchivesExportVO> exportVos = list.stream()
                .map(o -> {
                    BuildingArchivesExportVO buildingArchivesExportVO = new BuildingArchivesExportVO();
                    buildingArchivesExportVO.setId(o.getId().toString());
                    buildingArchivesExportVO.setName(o.getName());
                    buildingArchivesExportVO.setAreaName(o.getAreaName() + o.getAreaDetail());
                    buildingArchivesExportVO.setDepartmentName(o.getDepartmentName());
                    buildingArchivesExportVO.setBuildingNum(o.getBuildingNum());
                    buildingArchivesExportVO.setTypeName(configIdNameMap.getOrDefault(o.getType(), ""));
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
        return exportVos;
    }

    @Override
    public PageResult<BuildingArchivesVO> queryForPage(BuildingArchivesPageQuery pageQuery) {
        PageResult<BuildingArchivesVO> buildingArchivesVOPageResult = new PageResult<>();
//        DepartmentDto departmentReqDto = apiSystemService.lookUpDepartmentInfo(pageQuery.getDepartmentId(), null, null);
        DepartmentReqDto departmentReqDto = new DepartmentReqDto();
        departmentReqDto.setSelfReturn(true);
        Long deptId = Optional.ofNullable(pageQuery.getDepartmentId()).orElse(LoginInfoHolder.getCurrentDeptId());
        departmentReqDto.setDeptId(deptId);
        List<DepartmentDto> departmentDtos = apiSystemService.lookUpDepartmentChildList(departmentReqDto);
        AssertUtil.notEmpty(departmentDtos, "部门不存在");
        List<Long> departIds = departmentDtos.stream().map(DepartmentDto::getId).collect(Collectors.toList());
        Map<Long, String> depotPathNamesMap = departmentDtos.stream().collect(Collectors.toMap(DepartmentDto::getId, DepartmentDto::getPathName));
        pageQuery.setDepartmentIdList(departIds);
        PageResult<BuildingArchives> buildingArchivesPageResult = buildingArchivesRepository.queryForPage(pageQuery);
        if (CollectionUtils.isEmpty(buildingArchivesPageResult.getList())) {
            return new PageResult<>();
        }

        List<BuildingArchivesVO> buildingArchivesVOS = buildingArchivesPageResult.getList().stream()
                .map(o -> {
                    BuildingArchivesVO buildingArchivesVO = new BuildingArchivesVO();
                    BeanUtils.copyProperties(o, buildingArchivesVO);
                    buildingArchivesVO.setDepartmentName(depotPathNamesMap.get(o.getDepartmentId()));
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
    public int batchUpdateBuildingArchivesType(BatchBuildingArchivesDTO batchBuildingArchivesDTO) {
        AssertUtil.notNull(batchBuildingArchivesDTO.getType(), "类型不能为空");
        return buildingArchivesRepository.batchUpdateBuildingArchivesType(batchBuildingArchivesDTO.getType(),
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
        Map<String, Map<String, Long>> nameConfigIdMap = buildingAndConfigCommonUtilService.getConfigNameIdMap(companyId, SystemOptionTypeEnum.BUILDING_ARCHIVE.getCode());
        List<BuildingArchives> buildingArchivesList = buildingArchiveImportDTOList.stream()
                .map(o -> {
                    BuildingArchives buildingArchives = new BuildingArchives();
                    BeanUtils.copyProperties(o, buildingArchives);
                    // 导入的楼盘默认为北京朝阳区
                    buildingArchives.setAreaCode("110105000000");
                    buildingArchives.setAreaName("北京市市辖区朝阳区");
                    buildingArchives.setCompanyId(companyId);
                    buildingArchives.setDepartmentId(departmentId);
                    Long type = nameConfigIdMap.get(CustomConfigConstant.BUIDING_ARCHIVES_INIT.get(0)).get(o.getTypeName());
                    buildingArchives.setType(type);

                    return buildingArchives;
                })
                .collect(Collectors.toList());
        buildingArchivesRepository.batchSaveBuildingArchives(buildingArchivesList, userId);
    }

    @Override
    public List<DepartmentTreeRspDTO> getDepartmentBuildingTree(Long orgId, Long userId, String keyWords) {
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
    public List<BuildingArchive> lookupBuildingArchiveByDepartmentList(Long depotId) {
        DepartmentReqDto paraDto = new DepartmentReqDto();
        Long deptId = Optional.ofNullable(depotId).orElse(LoginInfoHolder.getCurrentDeptId());
        paraDto.setDeptId(deptId);
//        if(Objects.isNull(LoginInfoHolder.getCurrentDeptId())){
//            paraDto.setDeptId(reqDto.getDeptId());
//        }

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
    public ArchivesStatisticVO queryArchiveBaseStatisticInfo(BuildingArchivesPageQuery pageQuery) {
        // 楼盘信息
        List<BuildingArchivesVO> buildingArchivesVOS = queryForList(pageQuery);
        // 楼盘数
        int parks = Optional.ofNullable(buildingArchivesVOS).orElse(new ArrayList<>()).size();
        // 楼栋数
        BigDecimal buildingNum = CalculateUtils.calculateSum(buildingArchivesVOS, BuildingArchivesVO::getBuildingNum);
        // 房间数
        BigDecimal hoursNum = CalculateUtils.calculateSum(buildingArchivesVOS, BuildingArchivesVO::getHouseNum);


        ArchivesStatisticVO statisticVO = new ArchivesStatisticVO();
        statisticVO.setParks(parks);
        statisticVO.setBuildings(buildingNum.longValue());
        statisticVO.setHouses(hoursNum.longValue());

        return statisticVO;
    }

    @Override
    public Long quertOrgIdByHouse(Long houseId) {
        return buildingArchivesRepository.quertOrgIdByHouse(houseId);
    }
}
