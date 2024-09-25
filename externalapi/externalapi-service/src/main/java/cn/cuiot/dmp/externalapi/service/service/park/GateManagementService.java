package cn.cuiot.dmp.externalapi.service.service.park;

import cn.cuiot.dmp.base.application.service.ApiArchiveService;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.feign.ArchiveFeignService;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.ErrorCode;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.KtSignUtils;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.constant.KeTuoPlatformConstant;
import cn.cuiot.dmp.externalapi.service.entity.park.GateManagementEntity;
import cn.cuiot.dmp.externalapi.service.mapper.park.GateManagementMapper;
import cn.cuiot.dmp.externalapi.service.vendor.park.config.ParkProperties;
import cn.cuiot.dmp.externalapi.service.vendor.park.query.GateManagementQuery;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.*;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 道闸管理 服务类
 * @author pengjian
 * @since 2024-09-09
 */
@Service
public class GateManagementService extends ServiceImpl<GateManagementMapper, GateManagementEntity>{

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ParkProperties parkProperties;

    @Autowired
    private ApiArchiveService apiArchiveService;

    @Autowired
    private ArchiveFeignService archiveFeignService;

    public void insertOrUpdateBatch(List<GateManagementEntity> list){
        getBaseMapper().insertOrUpdateBatch(list);
    }

    /**
     * 道闸控制
     * @param vo
     * @return
     */
    public IdmResDTO gateControl(GateControlVO vo) {
        JSONObject jsonObject =getJsonObject(KeTuoPlatformConstant.GATE_CONTROL_CODE);
        jsonObject.put("parkId",vo.getParkId());
        jsonObject.put("nodeId",vo.getNodeId());
        jsonObject.put("type",vo.getType());
        jsonObject.put("userName", LoginInfoHolder.getCurrentUserId().toString());
        String key = KtSignUtils.paramsSign(jsonObject, parkProperties.getAppSercert());
        jsonObject.put("key",key);

        //接口获取列的停车场信息
        HttpHeaders headers = getHttpHeaders();
        ResponseEntity<ParkResultVO<GateControlResultVO>> responseEntity =
                restTemplate.exchange(parkProperties.getUrl()+ KeTuoPlatformConstant.GET_PARK_NODE, HttpMethod.POST,
                        new HttpEntity<>(jsonObject,headers),
                        new ParameterizedTypeReference<ParkResultVO<GateControlResultVO>>() {
                        });
        ParkResultVO<GateControlResultVO> body = responseEntity.getBody();
        if(!StringUtils.equals(body.getResCode(), KeTuoPlatformConstant.SUCCESS_CODE)){
            throw new RuntimeException("操作失败:"+body.getResMsg());
        }
        GateControlResultVO data = body.getData();
        //更新本地数据状态
        LambdaUpdateWrapper<GateManagementEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(GateManagementEntity::getStatus,data.getStatus())
                .eq(GateManagementEntity::getParkId,vo.getParkId()).eq(GateManagementEntity::getNodeId,data.getNodeId());
        getBaseMapper().update(null,updateWrapper);
        return IdmResDTO.success();
    }


    /**
     * 设置请求头
     * @return
     */
    public HttpHeaders getHttpHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.set(KeTuoPlatformConstant.PARK_HEADER_VERSION, KeTuoPlatformConstant.PARK_VERSION);
        headers.set(KeTuoPlatformConstant.PARK_HEADER_ACCEPT, KeTuoPlatformConstant.PARK_ACCEPT);
        return headers;
    }

    /**
     * 组装请求参数
     * @param serviceCode
     * @return
     */
    public JSONObject getJsonObject(String serviceCode){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appId",parkProperties.getAppId());
        jsonObject.put("serviceCode",serviceCode);
        jsonObject.put("ts",new Date().getTime());
        jsonObject.put("reqId", UuidUtil.getTimeBasedUuid());
        return jsonObject;
    }

    /**
     * 分页查询
     * @param page
     * @param query
     * @return
     */
    public IPage<GageManagePageVO> queryForPage(Page<GageManagePageVO> page, GateManagementQuery query) {

        if(CollectionUtil.isEmpty(query.getCommunityIds())){
            DepartmentReqDto dto = new DepartmentReqDto();
            dto.setDeptId(LoginInfoHolder.getCurrentDeptId());
            dto.setSelfReturn(true);
            List<BuildingArchive> archives = Optional.ofNullable(apiArchiveService.lookupBuildingArchiveByDepartmentList(dto))
                    .orElse(new ArrayList<>());
            List<Long> ids = archives.stream().map(BuildingArchive::getId).collect(Collectors.toList());
            query.setCommunityIds(ids);
        }
        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        IPage<GageManagePageVO> pages = getBaseMapper().queryForPage(page,query);
        List<GageManagePageVO> records = pages.getRecords();
        if(CollectionUtil.isEmpty(records)){
            return pages;
        }
        List<Long> communityIds = records.stream().filter(item -> Objects.nonNull(item.getCommunityId()))
                .map(GageManagePageVO::getCommunityId).collect(Collectors.toList());
        //获取楼盘对应的楼盘名称
        Map<Long, String> propertyMap = Optional.ofNullable(getPropertyMap(communityIds)).orElse(new HashMap<>());
        records.stream().filter(it->Objects.nonNull(it.getCommunityId())).forEach(item->{
            item.setCommunityName(propertyMap.get(item.getCommunityId()));
        });
        return pages;
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
