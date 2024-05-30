package cn.cuiot.dmp.content.conver;//	模板

import cn.cuiot.dmp.content.dal.entity.ContentNoticeEntity;
import cn.cuiot.dmp.content.param.dto.NoticeCreateDto;
import cn.cuiot.dmp.content.param.vo.NoticeVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/29 15:52
 */
@Mapper
public interface NoticeConver {

    NoticeConver INSTANCE = Mappers.getMapper(NoticeConver.class);

    NoticeVo conver(ContentNoticeEntity contentNoticeEntity);

//    @Mappings(value = {
//            @Mapping(target = "departments", ignore = true),
//            @Mapping(target = "buildings", ignore = true)
//    })
    ContentNoticeEntity conver(NoticeCreateDto createDto);
}
