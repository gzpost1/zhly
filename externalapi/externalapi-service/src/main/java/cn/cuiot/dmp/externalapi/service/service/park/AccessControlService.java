package cn.cuiot.dmp.externalapi.service.service.park;

import cn.cuiot.dmp.base.application.dto.ExcelDownloadDto;
import cn.cuiot.dmp.base.application.service.ApiArchiveService;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.feign.ArchiveFeignService;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.ErrorCode;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.constant.PortraitInputConstant;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardEntity;
import cn.cuiot.dmp.externalapi.service.entity.park.AccessControlEntity;
import cn.cuiot.dmp.externalapi.service.mapper.park.AccessControlMapper;
import cn.cuiot.dmp.externalapi.service.query.*;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 门禁管理 服务类
 * @author pengjian
 * @since 2024-09-04
 */
@Service
public class AccessControlService extends ServiceImpl<AccessControlMapper, AccessControlEntity>{

    @Autowired
    private AccessControlMapper accessControlMapper;
    @Autowired
    private ApiArchiveService apiArchiveService;

    @Autowired
    private ArchiveFeignService archiveFeignService;
    @Autowired
    private ExcelExportService excelExportService;

    public void insertOrUpdateBatch( List<AccessControlEntity> accessList){
        accessControlMapper.insertOrUpdateBatch(accessList);
    }

    /**
     * 更新设备关联的楼盘
     * @param communityVO
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateAccessCommunity(UpdateAccessCommunityVO communityVO) {
        List<String> deviceNos = communityVO.getDeviceNos();
        if(CollectionUtil.isNotEmpty(deviceNos)){
            deviceNos.stream().forEach(item->{
                LambdaUpdateWrapper<AccessControlEntity> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.set(AccessControlEntity::getCommunityId,communityVO.getCommunityId())
                        .eq(AccessControlEntity::getId,item+LoginInfoHolder.getCurrentOrgId());
                accessControlMapper.update(null,updateWrapper);
            });
        }
    }


    /**
     * 删除设备信息
     * @param communityVO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public IdmResDTO deleteAccessCommunity(UpdateAccessCommunityVO communityVO) {
        List<String> collect = communityVO.getDeviceNos().stream().map(item -> item + LoginInfoHolder.getCurrentOrgId()).collect(Collectors.toList());
        accessControlMapper.deleteBatchIds(collect);
        return IdmResDTO.success();
    }

    /**
     * 查询设备信息
     * @param queryAccessCommunity
     * @return
     */
    public IdmResDTO<IPage<AccessCommunityDto>> queryForPage(QueryAccessCommunity queryAccessCommunity) {

        //判断查询的楼盘信息是否为空，如果为空则查询组织下的楼盘信息
        if(CollectionUtil.isEmpty(queryAccessCommunity.getCommunityIds())){
            queryAccessCommunity.setCommunityIdType(PortraitInputConstant.COMMUNITY_TYPE);
            DepartmentReqDto dto = new DepartmentReqDto();
            dto.setDeptId(LoginInfoHolder.getCurrentDeptId());
            dto.setSelfReturn(true);
            List<BuildingArchive> archives = Optional.ofNullable(apiArchiveService.lookupBuildingArchiveByDepartmentList(dto))
                    .orElse(new ArrayList<>());
            List<Long> ids = archives.stream().map(BuildingArchive::getId).collect(Collectors.toList());

            queryAccessCommunity.setCommunityIds(ids);
        }
        queryAccessCommunity.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        IPage<AccessCommunityDto> pages =accessControlMapper.queryForPage(new Page<>(queryAccessCommunity.getPageNo(),
                queryAccessCommunity.getPageSize()),queryAccessCommunity);
        List<AccessCommunityDto> records = pages.getRecords();
        if (CollectionUtil.isNotEmpty(records)){
            List<Long> communityIds = records.stream().filter(it -> Objects.nonNull(it.getCommunityId())).
                    map(AccessCommunityDto::getCommunityId).collect(Collectors.toList());

            Map<Long, String> propertyMap = Optional.ofNullable(getPropertyMap(communityIds)).orElse(new HashMap<>());
            records.stream().filter(it->Objects.nonNull(it.getCommunityId())).forEach(item->{
                item.setCommunityName(propertyMap.get(item.getCommunityId()));
            });
        }
        return IdmResDTO.success(pages);
    }

    /**
     * 导出门禁
     * @param queryAccessCommunity
     */
    public  void export(QueryAccessCommunity queryAccessCommunity){
        excelExportService.excelExport(ExcelDownloadDto.<QueryAccessCommunity>builder().loginInfo(LoginInfoHolder.getCurrentLoginInfo()).query(queryAccessCommunity)
                .title("门禁导出").fileName("门禁导出(" + DateTimeUtil.dateToString(new Date(), "yyyyMMdd")+")").sheetName("门禁导出")
                .build(), AccessCommunityDto.class, this::queryExport);

    }
    /**
     * 门禁导出列表
     * @param downloadDto
     * @return
     */
    public IPage<AccessCommunityDto> queryExport(ExcelDownloadDto<QueryAccessCommunity> downloadDto){
        QueryAccessCommunity pageQuery = downloadDto.getQuery();
        IPage<AccessCommunityDto> data = this.queryForPage(pageQuery).getData();
        return data;
    }

    /**
     * 根据楼盘id获取楼盘信息
     * @param propertyIds
     * @return
     */
    public  Map<Long,String> getPropertyMap(List<Long> propertyIds){
        BuildingArchiveReq req = new BuildingArchiveReq();
        req.setIdList(propertyIds);
        IdmResDTO<List<BuildingArchive>> listIdmResDTO = archiveFeignService.buildingArchiveQueryForList(req);
        List<BuildingArchive> archiveList = Optional.ofNullable(listIdmResDTO.getData()).orElseThrow(()->new BusinessException(ErrorCode.NOT_FOUND.getCode(),
                ErrorCode.NOT_FOUND.getMessage()));
        return archiveList.stream().collect(Collectors.toMap(BuildingArchive::getId,BuildingArchive::getName));
    }

    /**
     * 查询授权文件数据
     * @return
     */
    public IdmResDTO<List<DeviceListDto>> queryDevices() {
        LambdaQueryWrapper<AccessControlEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AccessControlEntity::getCompanyId,LoginInfoHolder.getCurrentOrgId());//LoginInfoHolder.getCurrentOrgId()

        List<AccessControlEntity> accessControlEntities = getBaseMapper().selectList(queryWrapper);
        if(CollectionUtil.isEmpty(accessControlEntities)){
            return IdmResDTO.success();
        }
        List<DeviceListDto> deviceListDtos = BeanMapper.mapList(accessControlEntities, DeviceListDto.class);
        return IdmResDTO.success(deviceListDtos);
    }


    /**
     * 获取 宇泛门禁 数量的统计
     */
    public Long queryAccessCommunityCount(StatisInfoReqDTO statisInfoReqDTO) {
        return getBaseMapper().queryAccessCommunityCount(statisInfoReqDTO);
    }


}
