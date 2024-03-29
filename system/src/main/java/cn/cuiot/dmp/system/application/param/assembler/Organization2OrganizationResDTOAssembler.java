package cn.cuiot.dmp.system.application.param.assembler;

import cn.cuiot.dmp.base.application.param.assembler.Assembler;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrganizationResDTO;
import cn.cuiot.dmp.system.user_manage.domain.entity.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @author pengcg
 * @date 2023/9/6
 */
@Mapper(componentModel = "spring")
public interface Organization2OrganizationResDTOAssembler extends
        Assembler<Organization, OrganizationResDTO> {

    @Mappings({
            @Mapping(source = "id.value", target = "id"),
            @Mapping(source = "orgTypeId.value", target = "orgTypeId"),
            @Mapping(source = "status.value", target = "status"),
            @Mapping(source = "parentId.value", target = "parentId"),
            @Mapping(source = "orgOwner.value", target = "orgOwner"),
            @Mapping(source = "createdByType.value", target = "createdByType"),
            @Mapping(source = "updatedByType.value", target = "updatedByType")
    })
    OrganizationResDTO toDTO(Organization organization);

}
