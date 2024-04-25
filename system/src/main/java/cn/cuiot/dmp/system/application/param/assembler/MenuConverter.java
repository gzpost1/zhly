package cn.cuiot.dmp.system.application.param.assembler;

import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.MenuDTO;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.entity.MenuEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * DepartmentDtoAssembler
 * @author: wuyongchong
 * @date: 2024/4/25 15:28
 */
@Mapper(componentModel = "spring")
public interface MenuConverter {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "menuId", target = "menuId"),
            @Mapping(source = "menuName", target = "menuName"),
            @Mapping(source = "menuType", target = "menuType")})
    MenuDTO entityToDTO(MenuEntity entity);

}
