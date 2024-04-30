package cn.cuiot.dmp.system.application.param.assembler;

import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * DepartmentDtoAssembler
 * @author: wuyongchong
 * @date: 2024/4/25 15:28
 */
@Mapper(componentModel = "spring")
public interface DepartmentConverter{

    @Mappings({
            @Mapping(source = "departmentName", target = "name"),
            @Mapping(source = "pkOrgId", target = "orgId")})
    DepartmentDto entityToDTO(DepartmentEntity entity);

}
