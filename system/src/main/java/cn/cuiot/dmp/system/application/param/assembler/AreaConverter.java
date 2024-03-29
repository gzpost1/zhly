package cn.cuiot.dmp.system.application.param.assembler;

import cn.cuiot.dmp.system.infrastructure.entity.AreaEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.AreaDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface AreaConverter {

    @Mappings({})
    List<AreaDto> entityListToDtoList(List<AreaEntity> areaEntityList);
}
