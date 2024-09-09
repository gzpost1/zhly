package cn.cuiot.dmp.externalapi.service.service.park;

import cn.cuiot.dmp.base.application.service.ApiArchiveService;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.feign.ArchiveFeignService;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.ErrorCode;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.entity.park.AccessControlEntity;
import cn.cuiot.dmp.externalapi.service.mapper.park.AccessControlMapper;
import cn.cuiot.dmp.externalapi.service.query.AccessCommunityDto;
import cn.cuiot.dmp.externalapi.service.query.DeviceListDto;
import cn.cuiot.dmp.externalapi.service.query.QueryAccessCommunity;
import cn.cuiot.dmp.externalapi.service.query.UpdateAccessCommunityVO;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
                        .set(AccessControlEntity::getCompanyId,LoginInfoHolder.getCurrentOrgId())
                        .eq(AccessControlEntity::getDeviceNo,item);
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
        accessControlMapper.deleteBatchIds(communityVO.getDeviceNos());
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
            DepartmentReqDto dto = new DepartmentReqDto();
            dto.setDeptId(LoginInfoHolder.getCurrentDeptId());
            dto.setSelfReturn(true);
            List<BuildingArchive> archives = Optional.ofNullable(apiArchiveService.lookupBuildingArchiveByDepartmentList(dto))
                    .orElse(new ArrayList<>());
            List<Long> ids = archives.stream().map(BuildingArchive::getId).collect(Collectors.toList());
            queryAccessCommunity.setCommunityIds(ids);
        }

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
}
