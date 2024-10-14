package cn.cuiot.dmp.externalapi.service.converter.hik;

import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangAcsReaderEntity;
import cn.cuiot.dmp.externalapi.service.vo.hik.HaikangAcsReaderExportVo;
import cn.cuiot.dmp.externalapi.service.vo.hik.HaikangAcsReaderVo;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

/**
 * 门禁读卡器实体转换器
 *
 * @author: wuyongchong
 * @date: 2024/10/10 14:26
 */
@Mapper(componentModel = "spring")
public interface HaikangAcsReaderConverter {

    @Mappings({})
    HaikangAcsReaderVo entityToVo(HaikangAcsReaderEntity entity);

    @Mappings({})
    List<HaikangAcsReaderVo> entityListToVoList(List<HaikangAcsReaderEntity> entityList);

    @Mappings({})
    HaikangAcsReaderExportVo voToExportVo(HaikangAcsReaderVo vo);
}
