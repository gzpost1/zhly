package cn.cuiot.dmp.externalapi.service.service.gw.push;

import cn.cuiot.dmp.common.bean.external.GWCurrencyBO;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.externalapi.service.constant.GwBusinessTypeConstant;
import cn.cuiot.dmp.externalapi.service.constant.GwEntranceGuardServiceKeyConstant;
import cn.cuiot.dmp.externalapi.service.entity.gw.entranceguard.*;
import cn.cuiot.dmp.externalapi.service.query.gw.push.DeviceEventParams;
import cn.cuiot.dmp.externalapi.service.query.gw.push.DeviceParametersParams;
import cn.cuiot.dmp.externalapi.service.query.gw.push.base.DataItem;
import cn.cuiot.dmp.externalapi.service.query.gw.push.base.DeviceServiceOutParams;
import cn.cuiot.dmp.externalapi.service.query.gw.push.base.GwHead;
import cn.cuiot.dmp.externalapi.service.service.gw.*;
import cn.cuiot.dmp.externalapi.service.service.gw.entranceguard.*;
import cn.cuiot.dmp.externalapi.service.utils.GwPushUtil;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req.InvokeDeviceServiceReq;
import cn.cuiot.dmp.externalapi.service.vendor.gw.dmp.DmpDeviceRemoteService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 格物门禁-推送数据统一处理
 *
 * @Author: zc
 * @Date: 2024-09-12
 */
@Slf4j
@Component
public class GwEntranceGuardPushHandle implements GwBusinessStrategy {

    @Autowired
    private GwEntranceGuardOperationRecordService operationRecordService;
    @Autowired
    private GwEntranceGuardService entranceGuardService;
    @Autowired
    private GwEntranceGuardParamService entranceGuardParamService;
    @Autowired
    private GwEntranceGuardConfigService entranceGuardConfigService;
    @Autowired
    private GwEntranceGuardAuthorizeService authorizeService;
    @Autowired
    private GwEntranceGuardAccessRecordService accessRecordService;
    @Autowired
    private DmpDeviceRemoteService dmpDeviceRemoteService;

    /**
     * 响应成功
     */
    private static final String RES_CODE = "001";

    @Override
    public void serviceHandle(GwHead head, DeviceServiceOutParams<Object> obj, Long dataId) {
        // 服务调用key
        String code = head.getCode();

        //业务id
        switch (code) {
            case GwEntranceGuardServiceKeyConstant.OPEN_THE_DOOR:
                openTheDoor(dataId, head.getResCode());
                break;
            case GwEntranceGuardServiceKeyConstant.RESTART:
                restart(dataId, head.getResCode());
                break;
            case GwEntranceGuardServiceKeyConstant.UPDATE_PARAM:
                updateParam(dataId);
                break;
            case GwEntranceGuardServiceKeyConstant.GET_PARAM:
                DeviceParametersParams body = GwPushUtil.getBody(obj.getData(), DeviceParametersParams.class);
                getParam(dataId, body);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + code);
        }
    }

    @Override
    public void eventHandle(List<DataItem<Object>> params, Long dataId) {
        GwHead head = GwPushUtil.getHead(params);

        if (Objects.nonNull(head)) {
            String code = head.getCode();
            if (code.equals(GwEntranceGuardServiceKeyConstant.DEVICE_RECORD_UPLOAD)) {
                DeviceEventParams body = GwPushUtil.getBody(params, DeviceEventParams.class);
                getDeviceRecordUpload(body);
            } else {
                throw new IllegalStateException("Unexpected value: " + code);
            }
        }
    }

    /**
     * 门禁通行记录
     */
    private void getDeviceRecordUpload(DeviceEventParams body) {
        if (Objects.isNull(body)) {
            return;
        }
        GwEntranceGuardAccessRecordEntity recordEntity = new GwEntranceGuardAccessRecordEntity();
        BeanUtils.copyProperties(body, recordEntity);
        recordEntity.setQrCode(JsonUtil.writeValueAsString(body.getQrCode()));
        recordEntity.setVaccine(JsonUtil.writeValueAsString(body.getVaccine()));

        DeviceEventParams.Info info = body.getInfo();
        if (Objects.nonNull(info)) {

            // TODO 此数据是加密了的，不知道如何解密。为了走通流程，后面去掉
            info.setPersonId(null);

            recordEntity.setName(info.getName());

            if (Objects.nonNull(info.getPersonId())) {
                recordEntity.setPersonId(Long.parseLong(info.getPersonId()));

                //根据用户查询门禁id
                List<GwEntranceGuardAuthorizeEntity> list = authorizeService.list(
                        new LambdaQueryWrapper<GwEntranceGuardAuthorizeEntity>()
                                .eq(GwEntranceGuardAuthorizeEntity::getEntranceGuardPersonId, info.getPersonId()));
                if (CollectionUtils.isNotEmpty(list)) {
                    recordEntity.setEntranceGuardId(list.get(0).getEntranceGuardId());
                }
            }
        }
        accessRecordService.save(recordEntity);
    }


    /**
     * 获取参数
     *
     * @Param dataId 门禁id
     */
    private void getParam(Long dataId, DeviceParametersParams body) {
        List<GwEntranceGuardParamEntity> list = entranceGuardParamService.list(
                new LambdaQueryWrapper<GwEntranceGuardParamEntity>()
                        .eq(GwEntranceGuardParamEntity::getEntranceGuardId, dataId));

        if (CollectionUtils.isEmpty(list)) {
            GwEntranceGuardParamEntity gwEntranceGuardParamEntity = list.get(0);
            BeanUtils.copyProperties(body, gwEntranceGuardParamEntity);
            gwEntranceGuardParamEntity.setEntranceGuardId(dataId);

            LambdaUpdateWrapper<GwEntranceGuardParamEntity> wrapper = new LambdaUpdateWrapper<>();
            wrapper.setEntity(gwEntranceGuardParamEntity);
            wrapper.eq(GwEntranceGuardParamEntity::getEntranceGuardId, dataId);

            entranceGuardParamService.update(wrapper);
        } else {
            GwEntranceGuardParamEntity gwEntranceGuardParamEntity = list.get(0);
            BeanUtils.copyProperties(body, gwEntranceGuardParamEntity);
            gwEntranceGuardParamEntity.setEntranceGuardId(dataId);

            entranceGuardParamService.save(gwEntranceGuardParamEntity);
        }
    }

    /**
     * 修改参数
     *
     * @Param dataId 门禁id
     */
    private void updateParam(Long dataId) {
        GwEntranceGuardEntity entity = Optional.ofNullable(entranceGuardService.getById(dataId))
                .orElseThrow(() -> new BusinessException(ResultCode.ERROR, "格物门禁【" + dataId + "】数据不存在"));

        GWCurrencyBO configInfo = entranceGuardConfigService.getConfigInfo(entity.getCompanyId());

        //获取最新的设备参数
        InvokeDeviceServiceReq req = new InvokeDeviceServiceReq();
        req.setKey(GwEntranceGuardServiceKeyConstant.GET_PARAM);
        req.setIotId(entity.getIotId());
        dmpDeviceRemoteService.invokeDeviceService(req, configInfo);
    }

    /**
     * 开门操作
     *
     * @Param dataId 数据id
     * @Param resMsg 执行结果
     */
    private void openTheDoor(Long dataId, String resMsg) {
        if (Objects.isNull(dataId)) {
            return;
        }
        updateOperationRecord(dataId, resMsg);
    }

    /**
     * 重启操作
     *
     * @Param dataId 数据id
     * @Param resMsg 执行结果
     */
    private void restart(Long dataId, String resMsg) {
        if (Objects.isNull(dataId)) {
            return;
        }
        updateOperationRecord(dataId, resMsg);
    }

    /**
     * 修改操作记录
     *
     * @Param dataId 数据id
     * @Param resMsg 执行结果
     */
    private void updateOperationRecord(Long dataId, String resCode) {
        GwEntranceGuardOperationRecordEntity entity = Optional.ofNullable(operationRecordService.getById(dataId))
                .orElseThrow(() -> new BusinessException(ResultCode.ERROR, "开门操作异常，id【" + dataId + "】不存在"));
        entity.setExecutionStatus(Objects.equals(resCode, RES_CODE) ? EntityConstants.YES : EntityConstants.NO);
        operationRecordService.updateById(entity);
    }

    @Override
    public String getBusinessType() {
        return GwBusinessTypeConstant.ENTRANCE_GUARD;
    }
}
