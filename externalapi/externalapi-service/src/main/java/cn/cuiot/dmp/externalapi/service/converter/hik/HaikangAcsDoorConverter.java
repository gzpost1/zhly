package cn.cuiot.dmp.externalapi.service.converter.hik;

import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangAcsDoorEntity;
import cn.cuiot.dmp.externalapi.service.vo.hik.HaikangAcsDoorExportVo;
import cn.cuiot.dmp.externalapi.service.vo.hik.HaikangAcsDoorVo;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

/**
 * 门禁点实体转换器
 *
 * @author: wuyongchong
 * @date: 2024/10/10 14:26
 */
@Mapper(componentModel = "spring")
public interface HaikangAcsDoorConverter {

    @Mappings({})
    HaikangAcsDoorVo entityToVo(HaikangAcsDoorEntity entity);

    @Mappings({})
    List<HaikangAcsDoorVo> entityListToVoList(List<HaikangAcsDoorEntity> entityList);

    @Mappings({})
    HaikangAcsDoorExportVo voToExportVo(HaikangAcsDoorVo vo);
}
