package cn.cuiot.dmp.app.converter;

import cn.cuiot.dmp.app.dto.AppUserDto;
import cn.cuiot.dmp.app.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 *
 */
@Mapper(componentModel = "spring")
public interface AppUserConverter {

    @Mappings({
            @Mapping(source = "id", target = "id")})
    AppUserDto toAppUserDto(UserEntity userEntity);
}
