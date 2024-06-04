package cn.cuiot.dmp.content.conver;//	模板

import cn.cuiot.dmp.content.dal.entity.ContentModuleApplication;
import cn.cuiot.dmp.content.param.dto.ModuleApplicationCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/4 19:06
 */

@Mapper
public interface ModuleApplicationConvert {

    ModuleApplicationConvert INSTANCE = Mappers.getMapper(ModuleApplicationConvert.class);

    ContentModuleApplication convert(ModuleApplicationCreateDto moduleBannerCreateDto);
}
