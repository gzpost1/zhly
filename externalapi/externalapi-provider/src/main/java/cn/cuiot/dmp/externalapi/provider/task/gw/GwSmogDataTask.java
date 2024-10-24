package cn.cuiot.dmp.externalapi.provider.task.gw;

import cn.cuiot.dmp.common.bean.external.GWCurrencyBO;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.enums.FootPlateInfoEnum;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwSmogDataEntity;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwSmogEntity;
import cn.cuiot.dmp.externalapi.service.service.gw.GwEntranceGuardConfigService;
import cn.cuiot.dmp.externalapi.service.service.gw.GwSmogDataService;
import cn.cuiot.dmp.externalapi.service.service.gw.GwSmogService;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req.DmpDeviceBatchStatusReq;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req.DmpDeviceReq;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.DmpDevicePropertyListResp;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.DmpDeviceStatusBatchResp;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.DmpDeviceStatusResp;
import cn.cuiot.dmp.externalapi.service.vendor.gw.dmp.DmpDeviceRemoteService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xxl.job.core.biz.model.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 格物烟雾报警器拉取最新的设备信息
 * @author pengjian
 * @create 2024/9/9 17:37
 */
@Slf4j
@Component
public class GwSmogDataTask {
    @Autowired
    private GwSmogService gwSmogService;
    @Autowired
    private GwEntranceGuardConfigService gwEntranceGuardConfigService;

    @Autowired
    private DmpDeviceRemoteService dmpDeviceRemoteService;

    @Autowired
    private GwSmogDataService gwSmogDataService;

    /**
     * 同步格物烟雾报警器拉取最新的设备信息
     * @return
     */
    public ReturnT<String>  syncSmogPropertyLatestValues(){
        log.info("同步格物烟雾报警器拉取最新的设备信息开始");
        List<GwSmogEntity> list = gwSmogService.list();
        if(CollectionUtils.isEmpty(list)){
            log.info("没有可同步的设备");
            return ReturnT.SUCCESS;
        }

        List<GWCurrencyBO> configInfos = gwEntranceGuardConfigService.getConfigInfoByPlatformId(FootPlateInfoEnum.GW_SMOG_ALARM.getId());
        Map<Long, GWCurrencyBO> configInfoMap = configInfos.stream().collect(Collectors.toMap(GWCurrencyBO::getCompanyId, vo -> vo));
        for(GwSmogEntity entity : list){
            GWCurrencyBO bo = configInfoMap.get(entity.getCompanyId());
            if(Objects.isNull(bo)){
                log.info("该企业没有设置对接配置{}", JsonUtil.writeValueAsString(entity));
                break;
            }
            DmpDeviceReq deviceReq = new DmpDeviceReq();
            deviceReq.setProductKey(bo.getProductKey());
            deviceReq.setDeviceKey(entity.getDeviceKey());
            deviceReq.setIotId(entity.getIotId());
            DmpDevicePropertyListResp propertyLatestValues =null;
            try {
                propertyLatestValues = dmpDeviceRemoteService.getPropertyLatestValues(deviceReq, bo);
            }catch (Exception e){
                e.printStackTrace();
                log.error("烟雾报警器查询最新属性报错",e);
            }
            if(Objects.nonNull(propertyLatestValues)){
                GwSmogDataEntity gwSmogDataEntity = new GwSmogDataEntity();
                gwSmogDataEntity.setDeviceData(propertyLatestValues.getList());
                gwSmogDataEntity.setDeviceId(entity.getId());
                gwSmogDataService.save(gwSmogDataEntity);
            }
        }
        log.info("同步格物烟雾报警器拉取最新的设备信息完成");
        return ReturnT.SUCCESS;
    }

    /**
     * 同步格物烟雾报警器设备状态
     * @return
     */
    public ReturnT<String>  syncSmogStatus(){
        log.info("同步格物烟雾报警器设备状态信息开始");
        List<GwSmogEntity> list = gwSmogService.list();
        if(CollectionUtils.isEmpty(list)){
            log.info("没有可同步的设备");
            return ReturnT.SUCCESS;
        }
        //获得烟雾报警器配置
        List<GWCurrencyBO> configInfos = gwEntranceGuardConfigService.getConfigInfoByPlatformId(FootPlateInfoEnum.GW_SMOG_ALARM.getId());
        Map<Long, GWCurrencyBO> configInfoMap = configInfos.stream().collect(Collectors.toMap(GWCurrencyBO::getCompanyId, vo -> vo));
        //根据企业分组  然后批量查询
        Map<Long, List<GwSmogEntity>> smogMap = list.stream().collect(Collectors.groupingBy(GwSmogEntity::getCompanyId));
        for(Map.Entry<Long, List<GwSmogEntity>> entry : smogMap.entrySet()){

            GWCurrencyBO bo = configInfoMap.get(entry.getKey());
            if(Objects.isNull(bo)){
                log.info("该企业没有设置对接配置{}", JsonUtil.writeValueAsString(entry));
                break;
            }
            List<GwSmogEntity> values = entry.getValue();
            DmpDeviceBatchStatusReq deviceReq = new DmpDeviceBatchStatusReq();
            deviceReq.setProductKey(bo.getProductKey());
            deviceReq.setDeviceKey(values.stream().map(vo->vo.getDeviceKey()).collect(Collectors.toList()));
            deviceReq.setIotId(values.stream().map(vo->vo.getIotId()).collect(Collectors.toList()));
            DmpDeviceStatusBatchResp dmpDeviceStatusBatchResp =null;
            try {
                dmpDeviceStatusBatchResp = dmpDeviceRemoteService.batchGetStatus(deviceReq, bo);
            }catch (Exception e){
                e.printStackTrace();
                log.error("烟雾报警器查询状态报错",e);
            }
            if(Objects.nonNull(dmpDeviceStatusBatchResp) && CollectionUtils.isNotEmpty(dmpDeviceStatusBatchResp.getDeviceStatusList())){
                for(DmpDeviceStatusResp resp :dmpDeviceStatusBatchResp.getDeviceStatusList()){
                    LambdaUpdateWrapper<GwSmogEntity> updateWrapper = new LambdaUpdateWrapper<>();
                    updateWrapper.eq(GwSmogEntity::getIotId,resp.getIotId());
                    updateWrapper.eq(GwSmogEntity::getProductKey,resp.getProductId());
                    updateWrapper.eq(GwSmogEntity::getDeviceKey,resp.getDeviceKey());
                    updateWrapper.set(GwSmogEntity::getEquipStatus,resp.getStatus());
                    updateWrapper.set(GwSmogEntity::getStatus,resp.getEnabled()? EntityConstants.ENABLED:EntityConstants.DISABLED);
                    gwSmogService.update(new GwSmogEntity(),updateWrapper);
                }
            }
        }
        log.info("同步格物烟雾报警器设备状态完成");
        return ReturnT.SUCCESS;
    }

}
