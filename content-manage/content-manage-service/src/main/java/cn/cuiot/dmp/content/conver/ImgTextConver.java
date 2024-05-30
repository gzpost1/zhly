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
public interface ImgTextConver {

    ImgTextConver INSTANCE = Mappers.getMapper(ImgTextConver.class);

    ContentImgTextVo conver(ContentImgTextEntity contentImgTextEntity);
}
