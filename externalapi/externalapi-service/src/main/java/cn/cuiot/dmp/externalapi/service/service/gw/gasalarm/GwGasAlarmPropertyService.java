package cn.cuiot.dmp.externalapi.service.service.gw.gasalarm;

import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.externalapi.service.entity.gw.gasalarm.GwGasAlarmFaultRecordEntity;
import cn.cuiot.dmp.externalapi.service.entity.gw.gasalarm.GwGasAlarmPropertyEntity;
import cn.cuiot.dmp.externalapi.service.mapper.gw.gasalarm.GwGasAlarmFaultRecordMapper;
import cn.cuiot.dmp.externalapi.service.mapper.gw.gasalarm.GwGasAlarmPropertyMapper;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.DmpDevicePropertyResp;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 格物-烟雾报警器属性 业务层
 *
 * @Author: zc
 * @Date: 2024-10-24
 */
@Service
public class GwGasAlarmPropertyService extends ServiceImpl<GwGasAlarmPropertyMapper, GwGasAlarmPropertyEntity> {

    @Autowired
    private GwGasAlarmFaultRecordMapper gwGasAlarmFaultRecordMapper;

    /**
     * 更新设备属性
     *
     * @Param entity 参数
     */
    public void createOrUpdate(GwGasAlarmPropertyEntity property) {
        GwGasAlarmPropertyEntity entity = getOne(
                new LambdaQueryWrapper<GwGasAlarmPropertyEntity>()
                        .eq(GwGasAlarmPropertyEntity::getDeviceId, property.getDeviceId())
                        .last(" LIMIT 1 "));
        entity.setDeviceData(property.getDeviceData());
        // 更新属性
        saveOrUpdate(entity);
        // 保存记录
        createRecord(property);
    }

    /**
     * 保存故障记录
     * @Param property 参数
     */
    private void createRecord(GwGasAlarmPropertyEntity property) {
        List<DmpDevicePropertyResp> propertyResp = JsonUtil.readValue(property.getDeviceData(),
                new TypeReference<List<DmpDevicePropertyResp>>() {
                });
        if (CollectionUtils.isNotEmpty(propertyResp)) {
            Map<String, Object> map = propertyResp.stream()
                    .collect(Collectors.toMap(DmpDevicePropertyResp::getKey, DmpDevicePropertyResp::getValue));
            if (map.containsKey(GwGasAlarmPropertyEntity.ERROR_CODE)) {
                // 同步新增故障记录
                GwGasAlarmFaultRecordEntity recordEntity = new GwGasAlarmFaultRecordEntity();
                recordEntity.setDeviceId(property.getDeviceId());
                recordEntity.setErrorCode(map.get(GwGasAlarmPropertyEntity.ERROR_CODE).toString());
                gwGasAlarmFaultRecordMapper.insert(recordEntity);
            }
        }
    }
}
