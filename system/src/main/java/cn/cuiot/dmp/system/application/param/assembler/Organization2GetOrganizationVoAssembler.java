package cn.cuiot.dmp.system.application.param.assembler;

import cn.cuiot.dmp.base.application.param.assembler.Assembler;
import cn.cuiot.dmp.system.infrastructure.entity.vo.GetOrganizationVO;
import cn.cuiot.dmp.system.user_manage.domain.entity.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @author pengcg
 * @date 2023/9/6
 */
@Mapper(componentModel = "spring")
public interface Organization2GetOrganizationVoAssembler extends
        Assembler<Organization, GetOrganizationVO> {

    @Mappings({
            @Mapping(source = "id.value", target = "id"),
            @Mapping(source = "orgTypeId.value", target = "orgTypeId"),
            @Mapping(source = "status.value", target = "status"),
    })
    GetOrganizationVO toDTO(Organization organization);



}
