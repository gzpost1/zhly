package cn.cuiot.dmp.externalapi.service.service.gw;

import cn.cuiot.dmp.common.bean.external.GWCurrencyBO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.constant.GwBusinessTypeConstant;
import cn.cuiot.dmp.externalapi.service.constant.GwEntranceGuardServiceKeyConstant;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardEntity;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardParamDto;
import cn.cuiot.dmp.externalapi.service.query.gw.push.DeviceParametersParams;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req.InvokeDeviceServiceReq;
import cn.cuiot.dmp.externalapi.service.vendor.gw.dmp.DmpDeviceRemoteService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardParamEntity;
import cn.cuiot.dmp.externalapi.service.mapper.gw.GwEntranceGuardParamMapper;

import java.util.List;
import java.util.Optional;

/**
 * 格物门禁-设备参数 业务层
 *
 * @Author: zc
 * @Date: 2024-09-11
 */
@Service
public class GwEntranceGuardParamService extends ServiceImpl<GwEntranceGuardParamMapper, GwEntranceGuardParamEntity> {

    @Autowired
    private GwEntranceGuardService gwEntranceGuardService;
    @Autowired
    private DmpDeviceRemoteService dmpDeviceRemoteService;
    @Autowired
    private GwEntranceGuardConfigService configService;

    /**
     * 查询参数
     */
    public GwEntranceGuardParamEntity queryForParam(Long entranceGuardId) {
        List<GwEntranceGuardParamEntity> list = list(
                new LambdaQueryWrapper<GwEntranceGuardParamEntity>()
                        .eq(GwEntranceGuardParamEntity::getEntranceGuardId, entranceGuardId));
        return CollectionUtils.isNotEmpty(list) ? list.get(0) : new GwEntranceGuardParamEntity();
    }

    public void updateParam(GwEntranceGuardParamDto dto) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        GWCurrencyBO bo = configService.getConfigInfo(companyId);

        //门禁数据
        GwEntranceGuardEntity entranceGuard = gwEntranceGuardService.getBaseMapper().queryForDetail(companyId, dto.getEntranceGuardId());

        GwEntranceGuardParamEntity entity = Optional.ofNullable(getById(dto.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.ERROR, "数据不存在"));
        BeanUtils.copyProperties(dto, entity);


        //远程请求修改参数
        InvokeDeviceServiceReq req = new InvokeDeviceServiceReq();

        //服务调用key
        String serviceKey = GwEntranceGuardServiceKeyConstant.UPDATE_PARAM;
        req.setKey(serviceKey);

        DeviceParametersParams params = new DeviceParametersParams();
        BeanUtils.copyProperties(entity, params);
        req.setArguments(JsonUtil.writeValueAsString(params));

        req.setIotId(entranceGuard.getIotId());

        //设置请求id
        bo.setRequestId(GwBusinessTypeConstant.ENTRANCE_GUARD + "-" + serviceKey + "_" + entranceGuard.getId() + "-" + System.currentTimeMillis());
        dmpDeviceRemoteService.invokeDeviceService(req, bo);

        updateById(entity);
    }
}
