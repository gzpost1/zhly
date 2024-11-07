package cn.cuiot.dmp.externalapi.service.service.park;


import cn.cuiot.dmp.base.application.service.ApiArchiveService;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.feign.ArchiveFeignService;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.ErrorCode;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.constant.PortraitInputConstant;
import cn.cuiot.dmp.externalapi.service.entity.park.AccessControlEntity;
import cn.cuiot.dmp.externalapi.service.entity.park.IdentificationRecordEntity;
import cn.cuiot.dmp.externalapi.service.mapper.park.IdentificationRecordMapper;
import cn.cuiot.dmp.externalapi.service.query.AccessCommunityDto;
import cn.cuiot.dmp.externalapi.service.query.IdentificationRecordQuery;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 识别记录上报记录 服务类
 *
 * @author pengjian
 * @since 2024-11-06
 */
@Service
public class IdentificationRecordService extends ServiceImpl<IdentificationRecordMapper, IdentificationRecordEntity>{


    @Autowired
    private AccessControlService accessControlService;

    @Autowired
    private ApiArchiveService apiArchiveService;

    @Autowired
    private ArchiveFeignService archiveFeignService;
    /**
     * 识别记录上报
     * @param params
     * @return
     */
    public IdmResDTO identificationRecordReport(Map<String, String> params) {
        String eventCode = params.get("eventCode");
        String eventGuid = params.get("eventGuid");
        String eventMsg = params.get("eventMsg");
        if(!StringUtils.equals(eventCode, PortraitInputConstant.RECORD_EVENT_CODE)){
            throw new RuntimeException("接口编码错误"+eventCode);
        }
        IdentificationRecordEntity identificationRecordEntity = JsonUtil.readValue(eventMsg, IdentificationRecordEntity.class);
        identificationRecordEntity.setCreateTime(new Date());
        identificationRecordEntity.setShowDate(new Date(Long.parseLong(identificationRecordEntity.getShowTime())));
        identificationRecordEntity.setEventGuid(eventGuid);
        //根据设备编码查询所属楼盘信息与公司信息
        LambdaQueryWrapper<AccessControlEntity> lw = new LambdaQueryWrapper<>();
        lw.eq(AccessControlEntity::getDeviceNo,identificationRecordEntity.getDeviceNo());
        List<AccessControlEntity> list = accessControlService.list(lw);
        if(CollectionUtil.isNotEmpty(list)){
            identificationRecordEntity.setCompanyId(list.get(0).getCompanyId());
            identificationRecordEntity.setCommunityId(list.get(0).getCommunityId());
        }
        this.saveOrUpdate(identificationRecordEntity);
        return IdmResDTO.success();
    }

    /**
     * 分页获取
     * @param
     * @param query
     * @return
     */
    public IPage<IdentificationRecordEntity> queryForPage( IdentificationRecordQuery query) {
        if(CollectionUtil.isEmpty(query.getCommunityIds())){
            query.setQueryType(PortraitInputConstant.COMMUNITY_TYPE);
            DepartmentReqDto dto = new DepartmentReqDto();
            dto.setDeptId(LoginInfoHolder.getCurrentDeptId());
            dto.setSelfReturn(true);
            List<BuildingArchive> archives = Optional.ofNullable(apiArchiveService.lookupBuildingArchiveByDepartmentList(dto))
                    .orElse(new ArrayList<>());
            List<Long> ids = archives.stream().map(BuildingArchive::getId).collect(Collectors.toList());
            query.setCommunityIds(ids);
        }
        IPage<IdentificationRecordEntity> recordEntityIPage =  getBaseMapper().queryForPage(new Page<IdentificationRecordEntity>(query.getPageNo(), query.getPageSize()),query);
        List<IdentificationRecordEntity> records = Optional.ofNullable(recordEntityIPage.getRecords()).orElse(new ArrayList<>());
        List<Long> communityIds = records.stream().filter(it -> Objects.nonNull(it.getCommunityId())).
                map(IdentificationRecordEntity::getCommunityId).collect(Collectors.toList());

            Map<Long, String> propertyMap = Optional.ofNullable(getPropertyMap(communityIds)).orElse(new HashMap<>());
            records.stream().filter(it->Objects.nonNull(it.getCommunityId())).forEach(item->{
                item.setCommunityName(propertyMap.get(item.getCommunityId()));
            });
        return recordEntityIPage;
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
}
