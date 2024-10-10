package cn.cuiot.dmp.externalapi.service.converter.hik;

import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangAcsDeviceEntity;
import cn.cuiot.dmp.externalapi.service.vo.hik.HaikangAcsDeviceVo;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

/**
 * 门禁设备转换器
 * @author: wuyongchong
 * @date: 2024/10/10 14:26
 */
@Mapper(componentModel = "spring")
public interface HaikangAcsDeviceConverter {

    @Mappings({})
    HaikangAcsDeviceVo entityToVo(HaikangAcsDeviceEntity entity);

    @Mappings({})
    List<HaikangAcsDeviceVo> entityListToVoList(List<HaikangAcsDeviceEntity> entityList);

}
