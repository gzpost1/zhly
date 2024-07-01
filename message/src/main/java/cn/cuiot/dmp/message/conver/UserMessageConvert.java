package cn.cuiot.dmp.message.conver;//	模板

import cn.cuiot.dmp.common.bean.dto.SysMsgDto;
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
public interface UserMessageConvert {
    UserMessageConvert INSTANCE = Mappers.getMapper(UserMessageConvert.class);

    @Mappings(value = {
//            @Mapping(target = "dataJson", ignore = true),
            @Mapping(target = "accepter", ignore = true)
    })
    UserMessageEntity concert(SysMsgDto sysMsgDto);
}
