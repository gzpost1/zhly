package cn.cuiot.dmp.system.application.param.assembler;

import cn.cuiot.dmp.base.infrastructure.dto.BaseRoleDto;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * RoleConverter
 * @author: wuyongchong
 * @date: 2024/4/25 15:28
 */
@Mapper(componentModel = "spring")
public interface RoleConverter {

    @Mappings({})
    BaseRoleDto entityToBaseRoleDto(RoleEntity entity);

}
