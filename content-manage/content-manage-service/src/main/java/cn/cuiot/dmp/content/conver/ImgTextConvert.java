package cn.cuiot.dmp.content.conver;//	模板

import cn.cuiot.dmp.content.dal.entity.ContentImgTextEntity;
import cn.cuiot.dmp.content.param.vo.ContentImgTextVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/29 14:29
 */
@Mapper
public interface ImgTextConvert {

    ImgTextConvert INSTANCE = Mappers.getMapper(ImgTextConvert.class);

    ContentImgTextVo convert(ContentImgTextEntity contentImgTextEntity);
}
