package cn.cuiot.dmp.externalapi.service.service.water;

import cn.cuiot.dmp.base.application.service.ApiArchiveService;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.PlatfromInfoReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.base.infrastructure.feign.ArchiveFeignService;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.bean.external.SDKDWaterMeterBO;
import cn.cuiot.dmp.common.constant.ErrorCode;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.FootPlateInfoEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.constant.PortraitInputConstant;
import cn.cuiot.dmp.externalapi.service.entity.water.WaterManagementEntity;
import cn.cuiot.dmp.externalapi.service.enums.WaterMeterEnums;
import cn.cuiot.dmp.externalapi.service.feign.SystemApiService;
import cn.cuiot.dmp.externalapi.service.mapper.water.WaterManagementMapper;
import cn.cuiot.dmp.externalapi.service.query.AccessCommunityDto;
import cn.cuiot.dmp.externalapi.service.vendor.watermeter.bean.*;
import cn.cuiot.dmp.externalapi.service.vendor.watermeter.constant.WaterMeterConstant;
import cn.cuiot.dmp.externalapi.service.vo.watermeter.UpdateWaterManagementVO;
import cn.cuiot.dmp.externalapi.service.vo.watermeter.WaterMeterQueryVO;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.houbb.heaven.util.lang.BeanUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 水表管理 服务类
 * @author pengjian
 * @since 2024-09-06
 */
@Service
public class WaterManagementService extends ServiceImpl<WaterManagementMapper, WaterManagementEntity>{
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SystemApiService systemApiService;

    @Autowired
    private ApiArchiveService apiArchiveService;

    @Autowired
    private ArchiveFeignService archiveFeignService;


    /**
     * 获取上报数据
     *
     * @param req
     * @return
     */
    public WaterMeterPage<WaterMeterReportDataResp> queryReportData(WaterMeterReportDataQueryReq req) {
        ResponseEntity<WaterMeterPage<WaterMeterReportDataResp>> responseEntity =
                restTemplate.exchange(buildUrl(WaterMeterConstant.QUERY_REPORT_DATA_LIST, BeanUtil.beanToMap(req)), HttpMethod.POST,
                        new HttpEntity<>(null, null),
                        new ParameterizedTypeReference<WaterMeterPage<WaterMeterReportDataResp>>() {
                        });
        WaterMeterPage<WaterMeterReportDataResp> body = responseEntity.getBody();
        if (Objects.isNull(body)) {
            throw new BusinessException(ErrorCode.BUSINESS_EXCEPTION.code(), "获取上报数据返回为空");
        }
        if (!body.success()) {
            log.error("获取上报数据：" + JsonUtil.writeValueAsString(body));
            throw new BusinessException(ErrorCode.BUSINESS_EXCEPTION.code(), body.getMsg());
        }
        return body;
    }

    /**
     * 同步水表数据
     * @return
     */

    public IdmResDTO syncWaterMeter() {
        WaterMeterPage<WaterMeterReportDataResp> resp = queryReportData(new WaterMeterReportDataQueryReq());
        List<WaterMeterReportDataResp> data = resp.getData();
        if(CollectionUtils.isEmpty(data)){
            return IdmResDTO.success();
        }
        List<WaterManagementEntity> paramList = new ArrayList<>();
        List<WaterManagementEntity> waterManagementEntities = BeanMapper.mapList(data, WaterManagementEntity.class);
        waterManagementEntities.stream().forEach(item->{
            item.setUpdateUser(LoginInfoHolder.getCurrentUserId());
            item.setUpdateTime(new Date());
            item.setId(item.getWsImei()+LoginInfoHolder.getCurrentOrgId());
            item.setCompanyId(LoginInfoHolder.getCurrentOrgId());
            paramList.add(item);
            if(paramList.size()==PortraitInputConstant.MAX_BATCH_SIZE){
                getBaseMapper().insertOrUpdateBatch(paramList);
                paramList.clear();
            }
        });
        if(paramList.size()>0){
            getBaseMapper().insertOrUpdateBatch(paramList);
        }

        return IdmResDTO.success();
    }

    /**
     * 分页获取水表信息
     * @param vo
     * @return
     */
    public IdmResDTO<IPage<WaterManagementEntity>> queryForPage(WaterMeterQueryVO vo) {
        if(CollectionUtils.isEmpty(vo.getCommunityIds())){
            vo.setCommunityIdType(PortraitInputConstant.COMMUNITY_TYPE);
            //获取当前账号自己的组织及其下属组织的楼盘id
            DepartmentReqDto dto = new DepartmentReqDto();
            dto.setDeptId(LoginInfoHolder.getCurrentDeptId());
            dto.setSelfReturn(true);
            List<BuildingArchive> archives = Optional.ofNullable(apiArchiveService.lookupBuildingArchiveByDepartmentList(dto))
                    .orElse(new ArrayList<>());
            List<Long> ids = archives.stream().map(BuildingArchive::getId).collect(Collectors.toList());
            vo.setCommunityIds(ids);
        }
        vo.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        IPage<WaterManagementEntity> pages = getBaseMapper().queryForPage(new Page<>(vo.getPageNo(),vo.getPageSize()),vo);
        List<WaterManagementEntity> records = pages.getRecords();
        if(CollectionUtils.isEmpty(records)){
            return IdmResDTO.success(pages);
        }
        List<Long> communityIds = records.stream().filter(it -> Objects.nonNull(it.getCommunityId())).
                map(WaterManagementEntity::getCommunityId).collect(Collectors.toList());
        //获取楼盘名称
        Map<Long, String> propertyMap = Optional.ofNullable(getPropertyMap(communityIds)).orElse(new HashMap<>());
        records.stream().filter(item->Objects.nonNull(item.getCommunityId())).forEach(it-> {
            it.setCommunityName(propertyMap.get(it.getCommunityId()));
        });
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
     * 下发阀控指令
     *
     * @param req
     * @return
     */
    public WaterMeterCommandControlResp.RespInfo deviceCommandV2(WaterMeterCommandControlReq req) {

        ResponseEntity<WaterMeterCommandControlResp> responseEntity =
                restTemplate.exchange(buildUrl(WaterMeterConstant.CREATE_SUBJECT_URL, null), HttpMethod.POST,
                        new HttpEntity<>(req, null),
                        new ParameterizedTypeReference<WaterMeterCommandControlResp>() {
                        });
        WaterMeterCommandControlResp body = responseEntity.getBody();
        if (Objects.isNull(body)) {
            throw new BusinessException(ErrorCode.BUSINESS_EXCEPTION.code(), "下发阀控指令返回为空");
        }
        List<WaterMeterCommandControlResp.RespInfo> rows = body.getRows();
        if (CollectionUtils.isEmpty(rows) || !rows.get(0).success()) {
            log.error("下发阀控指令：" + JsonUtil.writeValueAsString(body));
            throw new BusinessException(ErrorCode.BUSINESS_EXCEPTION.code(), "物联网水表（山东科德）接口调用异常-" + rows.get(0).getMessage());
        }
        List<WaterMeterCommandControlReq.CommandControlInfo> commands = req.getRows();
        if(CollectionUtil.isNotEmpty(commands)){
            commands.stream().forEach(item->{
                String nameByCode = WaterMeterEnums.getNameByCode(item.getValve_controll());
                LambdaUpdateWrapper<WaterManagementEntity> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.set(WaterManagementEntity::getValveStatus,nameByCode).eq(WaterManagementEntity::getWsImei,item.getSteal_no());
                getBaseMapper().update(null,updateWrapper);
            });
        }
        //更新本地状态
        return rows.get(0);
    }

    /**
     * 构建请求地址
     *
     * @param method
     * @return
     */
    private String buildUrl(String method, Map<String, Object> paramsMap) {
        //获取并使用山东科德-物联网水表配置
        List<PlatfromInfoRespDTO> configInfoList = systemApiService
                .queryPlatfromInfoPage(new PlatfromInfoReqDTO(FootPlateInfoEnum.SDKD_WATER_METER.getId(), LoginInfoHolder.getCurrentOrgId()))
                .getRecords();
        if (CollectionUtils.isEmpty(configInfoList)) {
            throw new BusinessException(ResultCode.PLATFORM_NOT_CONFIG);
        }
        //json转Object
        SDKDWaterMeterBO configInfo = FootPlateInfoEnum.getObjectFromJsonById(FootPlateInfoEnum.SDKD_WATER_METER.getId(), configInfoList.get(0).getData());
        //构建url
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(configInfo.getIp()).append(method)
                .append(WaterMeterConstant.URL_LABEL).append(WaterMeterConstant.WATER_METER_HEADER_KEY_FIELD).append(WaterMeterConstant.URL_PARAMS_EQUAL_LABEL).append(configInfo.getKey());
        if(Objects.nonNull(paramsMap) && !paramsMap.isEmpty()){
            for(String key : paramsMap.keySet()){
                Object value = paramsMap.get(key);
                if(Objects.nonNull(value)){
                    stringBuilder.append(WaterMeterConstant.URL_PARAMS_SEPARATE_LABEL).append(key).append(WaterMeterConstant.URL_PARAMS_EQUAL_LABEL).append(value);
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 获取详情信息
     * @param idParam
     * @return
     */
    public IdmResDTO<WaterManagementEntity> queryWaterManagement(IdParam idParam) {
        WaterManagementEntity entity = getBaseMapper().selectById(String.valueOf(idParam.getId())+LoginInfoHolder.getCurrentOrgId());
        //获取楼盘名称
        Map<Long, String> propertyMap = Optional.ofNullable(getPropertyMap(Arrays.asList(entity.getCommunityId()))).orElse(new HashMap<>());
        entity.setCommunityName(propertyMap.get(entity.getCommunityId()));
        return IdmResDTO.success(entity);
    }

    /**
     * 更新
     * @param vo
     * @return
     */
    public IdmResDTO updateWaterManagement(UpdateWaterManagementVO vo) {
        List<String> collect = vo.getWsImeis().stream().map(item -> item + LoginInfoHolder.getCurrentOrgId()).collect(Collectors.toList());
        LambdaUpdateWrapper<WaterManagementEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(WaterManagementEntity::getWaterName,vo.getWaterName())
                .set(WaterManagementEntity::getCommunityId,vo.getCommunityId())
                .in(WaterManagementEntity::getId,collect);
        getBaseMapper().update(null,updateWrapper);

        return IdmResDTO.success();
    }

    /**
     * 删除水表信息
     * @param vo
     * @return
     */
    public IdmResDTO deleteWaterManagement(UpdateWaterManagementVO vo) {
        List<String> collect = vo.getWsImeis().stream().map(item -> item + LoginInfoHolder.getCurrentOrgId()).collect(Collectors.toList());
        getBaseMapper().deleteBatchIds(collect);
        return IdmResDTO.success();
    }




}
