package cn.cuiot.dmp.externalapi.service.service.gw;

import cn.cuiot.dmp.externalapi.service.entity.gw.GwSmogDataEntity;
import cn.cuiot.dmp.externalapi.service.enums.GwSmogPropertyEnums;
import cn.cuiot.dmp.externalapi.service.mapper.gw.GwSmogDataMapper;
import cn.cuiot.dmp.externalapi.service.query.gw.GwSmogBatchUpdateDto;
import cn.cuiot.dmp.externalapi.service.query.gw.GwSmogUpdateDto;
import cn.cuiot.dmp.externalapi.service.query.gw.push.DeviceEventSmogParams;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.DmpDevicePropertyResp;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwCommonPropertyVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 格物烟雾报警器设备属性 服务类
 * </p>
 *
 * @author wuyongchong
 * @since 2024-10-23
 */
@Service
public class GwSmogDataService extends ServiceImpl<GwSmogDataMapper, GwSmogDataEntity> {

    /**
     * 查询最新的属性数据
     *
     * @return
     */
    public GwSmogDataEntity queryLatestData(Long deviceId) {
        return baseMapper.queryLatestData(deviceId);
    }

    public void batchUpdateProperty(GwSmogBatchUpdateDto dto) {
        for(Long id :dto.getId()){
            GwSmogUpdateDto gwSmogUpdateDto = new GwSmogUpdateDto();
            BeanUtils.copyProperties(dto,gwSmogUpdateDto,"id");
            gwSmogUpdateDto.setId(id);
            updateProperty(gwSmogUpdateDto);
        }
    }

    public void updateProperty(GwSmogUpdateDto dto) {
        GwSmogDataEntity gwSmogDataEntity = queryLatestData(dto.getId());
        long time = new Date().getTime();
        if (Objects.isNull(gwSmogDataEntity)) {
            ArrayList<DmpDevicePropertyResp> result = Lists.newArrayList();
            for (GwSmogPropertyEnums statusEnums : GwSmogPropertyEnums.values()) {
                Object value = null;
                try {
                    Class<? extends GwSmogUpdateDto> clazz = dto.getClass();
                    Field field = clazz.getDeclaredField(statusEnums.getKey());
                    field.setAccessible(true);
                    value = field.get(dto);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DmpDevicePropertyResp gwCommonPropertyVo = new DmpDevicePropertyResp();
                gwCommonPropertyVo.setKey(statusEnums.getKey());
                gwCommonPropertyVo.setValue(value);
                gwCommonPropertyVo.setTs(time);
                result.add(gwCommonPropertyVo);

            }
            gwSmogDataEntity = new GwSmogDataEntity();
            gwSmogDataEntity.setDeviceId(dto.getId());
            gwSmogDataEntity.setDeviceData(result);
            this.save(gwSmogDataEntity);
        }else {
            List<DmpDevicePropertyResp> deviceData = gwSmogDataEntity.getDeviceData();
            Map<String, DmpDevicePropertyResp> map = deviceData.stream().collect(Collectors.toMap(vo -> vo.getKey(), vo -> vo));
            for (GwSmogPropertyEnums statusEnums : GwSmogPropertyEnums.values()) {
                Object value = null;
                try {
                    Class<? extends GwSmogUpdateDto> clazz = dto.getClass();
                    Field field = clazz.getDeclaredField(statusEnums.getKey());
                    field.setAccessible(true);
                    value = field.get(dto);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DmpDevicePropertyResp dmpDevicePropertyResp = map.get(statusEnums.getKey());
                if(Objects.isNull(dmpDevicePropertyResp)){
                    DmpDevicePropertyResp gwCommonPropertyVo = new DmpDevicePropertyResp();
                    gwCommonPropertyVo.setKey(statusEnums.getKey());
                    gwCommonPropertyVo.setValue(value);
                    gwCommonPropertyVo.setTs(time);
                    deviceData.add(gwCommonPropertyVo);
                }else if(Objects.nonNull(value)) {
                    dmpDevicePropertyResp.setValue(value);
                    dmpDevicePropertyResp.setTs(time);
                }
            }
            this.updateById(gwSmogDataEntity);
        }
    }


}
