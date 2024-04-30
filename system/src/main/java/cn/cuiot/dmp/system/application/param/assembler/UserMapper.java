package cn.cuiot.dmp.system.application.param.assembler;

import cn.cuiot.dmp.system.infrastructure.entity.UserDataEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserDataResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserResDTO;
import cn.cuiot.dmp.system.domain.entity.User;
import com.github.pagehelper.PageInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @author jiangze
 * @classname UserMapping
 * @description 用户Bean转换
 * @date 2020-06-30
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(source = "id.value", target = "id"),
            @Mapping(source = "email.encryptedValue", target = "email"),
            @Mapping(source = "phoneNumber.encryptedValue", target = "phoneNumber"),
            @Mapping(source = "password.hashEncryptValue", target = "password"),
            @Mapping(source = "userType.value", target = "userType")})
    UserResDTO doToDTO(User userEntity);

    /**
     * DataEntityList 转 DataDTOList
     *
     * @param entityPageInfo
     * @return
     */
    @Mappings({})
    PageInfo<UserDataResDTO> dataEntityListToDataDtoList(PageInfo<UserDataEntity> entityPageInfo);

    @Mappings({
            @Mapping(source = "id.value", target = "id"),
            @Mapping(source = "email.encryptedValue", target = "email"),
            @Mapping(source = "phoneNumber.encryptedValue", target = "phoneNumber"),
            @Mapping(source = "userType.value", target = "userType"),
            @Mapping(source = "contactAddress.value", target = "contactAddress")})
    UserDataResDTO doToDataDTO(User userEntity);
}
