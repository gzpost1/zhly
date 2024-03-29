package cn.cuiot.dmp.system.application.param.assembler;

import cn.cuiot.dmp.base.application.param.assembler.Assembler;
import cn.cuiot.dmp.system.application.param.dto.UserDTO;
import cn.cuiot.dmp.system.user_manage.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @Author 犬豪
 * @Date 2023/9/5 13:31
 * @Version V1.0
 */
@Mapper(componentModel = "spring")
public interface UserAssembler extends Assembler<User, UserDTO> {

    @Mappings({
            @Mapping(source = "id.value", target = "id"),
            @Mapping(source = "email.encryptedValue", target = "email"),
            @Mapping(source = "phoneNumber.encryptedValue", target = "phoneNumber"),
            @Mapping(source = "password.hashEncryptValue", target = "password"),
            @Mapping(source = "userType.value", target = "userType"),
            @Mapping(source = "status.value", target = "status"),
            @Mapping(source = "lastOnlineIp.value", target = "lastOnlineIp"),
            @Mapping(source = "lastOnlineAddress.value", target = "lastOnlineAddress"),
            @Mapping(source = "createdByType.value", target = "createdByType"),
            @Mapping(source = "updatedByType.value", target = "updatedByType"),
            @Mapping(source = "contactAddress.value", target = "contactAddress")})
    UserDTO toDTO(User user);

}
