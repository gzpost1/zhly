package cn.cuiot.dmp.message.conver;//	模板

import cn.cuiot.dmp.message.param.UserMessageAcceptDto;
import cn.cuiot.dmp.message.dal.entity.UserMessageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/24 15:04
 */
@Mapper
public interface UserMessageConver {
    UserMessageConver INSTANCE = Mappers.getMapper(UserMessageConver.class);

    @Mappings(value = {
            @Mapping(target = "dataJson", ignore = true),
    })
    UserMessageEntity concer(UserMessageAcceptDto userMessageAcceptDto);
}
