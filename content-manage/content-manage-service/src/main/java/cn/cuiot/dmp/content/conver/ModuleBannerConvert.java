package cn.cuiot.dmp.content.conver;//	模板

import cn.cuiot.dmp.content.dal.entity.ContentModuleBanner;
import cn.cuiot.dmp.content.param.dto.ModuleBannerCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/4 14:40
 */

@Mapper
public interface ModuleBannerConvert {

    ModuleBannerConvert INSTANCE = Mappers.getMapper(ModuleBannerConvert.class);


    ContentModuleBanner convert(ModuleBannerCreateDto moduleBannerCreateDto);
}
