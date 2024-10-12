package cn.cuiot.dmp.externalapi.service.service.park;

import cn.cuiot.dmp.base.application.dto.AuthDaHuaResp;
import cn.cuiot.dmp.base.application.service.ApiArchiveService;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.feign.ArchiveFeignService;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.ErrorCode;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.common.utils.KtSignUtils;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.constant.KeTuoPlatformConstant;
import cn.cuiot.dmp.externalapi.service.entity.park.ParkInfoEntity;
import cn.cuiot.dmp.externalapi.service.mapper.park.ParkInfoMapper;
import cn.cuiot.dmp.externalapi.service.query.AccessCommunityDto;
import cn.cuiot.dmp.externalapi.service.vendor.park.config.ParkProperties;
import cn.cuiot.dmp.externalapi.service.vendor.park.query.ParkInfoQuery;
import cn.cuiot.dmp.externalapi.service.vendor.park.query.ParkingLotQuery;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.FreeSpaceNumVO;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.ParkResultVO;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.ParkingLotVO;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.ParkingPage;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @since 2024-09-03
 */
@Service
public class ParkInfoService extends ServiceImpl<ParkInfoMapper, ParkInfoEntity>{

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ParkProperties parkProperties;

    @Autowired
    private ApiArchiveService apiArchiveService;

    @Autowired
    private ArchiveFeignService archiveFeignService;
    /**
     * 同步停车场数据
     * @return
     */
    @Async
    public void syncParkInfo() {
        GetParkingLotList();

    }

    /**
     * 获取停车场列表数据
     */
    public void GetParkingLotList(){

        int pageIndex =1;
        int pageSize =20;
        while(true){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("appId",parkProperties.getAppId());
            jsonObject.put("serviceCode", KeTuoPlatformConstant.PARK_LOT_SERVICE_CODE);
            jsonObject.put("ts",new Date().getTime());
            jsonObject.put("reqId", UuidUtil.getTimeBasedUuid());
            jsonObject.put("pageIndex", pageIndex);
            jsonObject.put("pageSize", pageSize);
            String key = KtSignUtils.paramsSign(jsonObject, parkProperties.getAppSercert());
            jsonObject.put("key", key);
            //接口获取列的停车场信息
            HttpHeaders headers = getHttpHeaders();
            ResponseEntity<JSONObject> responseEntity =
                    restTemplate.exchange(parkProperties.getUrl()+ KeTuoPlatformConstant.GET_PARKING_LOT_LIST, HttpMethod.POST,
                            new HttpEntity<>(jsonObject,headers),
                            new ParameterizedTypeReference<JSONObject>() {
                            });

            JSONObject body = responseEntity.getBody();
            ParkResultVO parkResultVO = JsonUtil.readValue(body.toJSONString(), ParkResultVO.class);
            if(!StringUtils.equals(parkResultVO.getResCode(), KeTuoPlatformConstant.SUCCESS_CODE)){
                throw new RuntimeException("同步停车场列表数据失败"+parkResultVO.getResMsg());
            }
            List<ParkingLotVO> parkingList = JSONObject.parseObject(JSONObject.toJSONString(body.getJSONObject("data").getJSONArray("parkingList")), new TypeReference<List<ParkingLotVO>>() {
            });
            pageIndex++;
            if(CollectionUtil.isEmpty(parkingList)){
                break;
            }
            //查询获取车场的在场车辆总数与剩余车位总数
            List<ParkInfoEntity> lists = new ArrayList<>();
            for(ParkingLotVO lot : parkingList){
                JSONObject lotObject = new JSONObject();
                lotObject.put("appId",parkProperties.getAppId());
                lotObject.put("serviceCode", KeTuoPlatformConstant.FREE_SPACE_NUM);
                lotObject.put("ts",new Date().getTime());
                lotObject.put("reqId", UuidUtil.getTimeBasedUuid());
                lotObject.put("parkId",lot.getParkId());
                String lotKey = KtSignUtils.paramsSign(lotObject, parkProperties.getAppSercert());
                lotObject.put("key", lotKey);
                System.out.println(lotObject.toJSONString());
                HttpHeaders lotHeaders = getHttpHeaders();
                ResponseEntity<JSONObject> lotResponseEntity =
                        restTemplate.exchange(parkProperties.getUrl()+ KeTuoPlatformConstant.GET_FREE_SPACE_NUM, HttpMethod.POST,
                                new HttpEntity<>(lotObject,lotHeaders),
                                new ParameterizedTypeReference<JSONObject>() {
                                });
                JSONObject lotBody = lotResponseEntity.getBody();
                ParkResultVO lotVO = JsonUtil.readValue(body.toJSONString(), ParkResultVO.class);
                if(!StringUtils.equals(lotVO.getResCode(), KeTuoPlatformConstant.SUCCESS_CODE)){
                    throw new RuntimeException("剩余车位查询失败"+lotVO.getResMsg());
                }
                FreeSpaceNumVO free = JSONObject.parseObject(JSONObject.toJSONString(lotBody.getJSONObject("data")), new TypeReference<FreeSpaceNumVO>() {
                });
                ParkInfoEntity entity = new ParkInfoEntity();
                entity.setParkId(lot.getParkId());
                entity.setId(String.valueOf(lot.getParkId())+LoginInfoHolder.getCurrentOrgId());
                entity.setParkName(lot.getParkName());
                entity.setTotalSpaceNum(free.getTotalNum());
                entity.setFreeSpaceNum(free.getFreeSpaceNum());
                entity.setUsedSpaceNum(free.getTotalNum()-free.getFreeSpaceNum());
                entity.setUpdateUser(LoginInfoHolder.getCurrentUserId());
                entity.setUpdateTime(new Date());
                entity.setCompanyId(LoginInfoHolder.getCurrentOrgId());
                lists.add(entity);
            }
            if(CollectionUtil.isNotEmpty(lists)){
                getBaseMapper().insertOrUpdateBatch(lists);
            }

        }
    }


    /**
     * 设置请求头
     * @return
     */
    public HttpHeaders getHttpHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.set(KeTuoPlatformConstant.PARK_HEADER_VERSION, KeTuoPlatformConstant.PARK_VERSION);
        headers.set(KeTuoPlatformConstant.PARK_HEADER_ACCEPT, KeTuoPlatformConstant.PARK_ACCEPT);
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return headers;
    }


    /**
     * 分页查询
     * @param page
     * @param query
     * @return
     */
    public IPage<ParkInfoEntity> queryForPage(Page<ParkInfoEntity> page, ParkInfoQuery query) {
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
        LambdaQueryWrapper<ParkInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(query.getParkName()),ParkInfoEntity::getParkName,query.getParkName());
        queryWrapper.like(StringUtils.isNotBlank(query.getParkId()),ParkInfoEntity::getParkId,query.getParkId())
                .eq(ParkInfoEntity::getCompanyId,query.getCompanyId())
                .in(ParkInfoEntity::getCommunityId,query.getCommunityIds()).or().isNull(ParkInfoEntity::getCommunityId);

        Page<ParkInfoEntity> parkInfoEntityPage = getBaseMapper().selectPage(page, queryWrapper);
        List<ParkInfoEntity> records = parkInfoEntityPage.getRecords();
        if(CollectionUtil.isEmpty(records)){
            return parkInfoEntityPage;
        }
        List<Long> communityIds = records.stream().filter(it -> Objects.nonNull(it.getCommunityId())).
                map(ParkInfoEntity::getCommunityId).collect(Collectors.toList());

        Map<Long, String> propertyMap = Optional.ofNullable(getPropertyMap(communityIds)).orElse(new HashMap<>());

        records.stream().filter(it->Objects.nonNull(it.getCommunityId())).forEach(item->{
            item.setCommunityName(propertyMap.get(item.getCommunityId()));
        });
        return parkInfoEntityPage;
    }

    /**
     * 获取所有的车场id
     * @return
     */
    public List<Integer>  queryParkIds(){
        List<Integer> ids = getBaseMapper().queryParkIds();
        return ids;
    }

    /**
     * 获取详情信息
     * @param id
     * @return
     */
    public IdmResDTO<ParkInfoEntity> queryForDetail(Long id) {

        ParkInfoEntity entity = getBaseMapper().selectById(String.valueOf(id)+LoginInfoHolder.getCurrentOrgId());
        //楼盘id不为空，获取楼盘信息
        if(Objects.nonNull(entity.getCommunityId())){
            Map<Long, String> propertyMap = Optional.ofNullable(getPropertyMap(Arrays.asList(entity.getCommunityId()))).orElse(new HashMap<>());
            entity.setCommunityName(propertyMap.get(entity.getCommunityId()));
        }
        return IdmResDTO.success(entity);
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
