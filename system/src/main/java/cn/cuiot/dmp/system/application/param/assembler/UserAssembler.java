package cn.cuiot.dmp.system.application.param.assembler;

import cn.cuiot.dmp.base.application.param.assembler.Assembler;
import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.system.application.param.dto.UserDTO;
import cn.cuiot.dmp.system.domain.entity.User;
import cn.cuiot.dmp.system.infrastructure.entity.UserDataEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserDataResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.vo.UserExportVo;
import com.github.pagehelper.PageInfo;
import java.util.List;
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

    /**
     * DataEntityList 转 DataDTOList
     *
     * @param list
     * @return
     */
    @Mappings({})
    List<UserDataResDTO> entityListToResDtoList(List<UserDataEntity> list);

    @Mappings({})
    List<UserExportVo> entityListToExportVoList(List<UserDataEntity> list);

    @Mappings({
            @Mapping(source = "id.value", target = "id"),
            @Mapping(source = "email.encryptedValue", target = "email"),
            @Mapping(source = "phoneNumber.encryptedValue", target = "phoneNumber"),
            @Mapping(source = "userType.value", target = "userType"),
            @Mapping(source = "contactAddress.value", target = "contactAddress")})
    UserDataResDTO doToDataDTO(User userEntity);


    /**
     * entityList 转 BaseUserDtoList
     *
     * @param entityList
     * @return
     */
    @Mappings({})
    List<BaseUserDto> dataEntityListToBaseUserDtoList(List<UserDataEntity> entityList);
}
