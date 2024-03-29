package cn.cuiot.dmp.system.application.param.assembler;

import cn.cuiot.dmp.system.infrastructure.entity.po.HousePo;
import cn.cuiot.dmp.system.infrastructure.entity.vo.CommercialComplexHouseDetailResVO;
import cn.cuiot.dmp.system.infrastructure.entity.vo.CommercialComplexHouseListVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @Author: 胡炜
 * @classname HouseConverter
 * @Description:
 * @Date: create in 2023/2/20 15:28
 */
@Mapper(componentModel = "spring")
public interface HouseConverter {

    /**
     * updateDto转entity
     * @param baseHousePO
     * @return
     */
    @Mappings({
            @Mapping(target = "commercialComplexId", expression = "java(baseHousePO.getCommunityId())"),
            @Mapping(target = "commercialComplexName", expression = "java(baseHousePO.getCommunityName())"),
            @Mapping(target = "commercialComplexCode", expression = "java(baseHousePO.getCommunityCode())"),
            @Mapping(target = "houseId", expression = "java(baseHousePO.getSelfDeptId())"),
            @Mapping(target = "houseNum", expression = "java(baseHousePO.getName())"),
            @Mapping(target = "usedStatus", expression = "java(baseHousePO.getUsedStatus())")
    })
    CommercialComplexHouseDetailResVO toCommercialComplexByBaseHousePO(HousePo baseHousePO);

    /**
     * updateDto转entity
     * @param baseHousePO
     * @return
     */
    @Mappings({
            @Mapping(target = "commercialComplexId", expression = "java(baseHousePO.getCommunityId())"),
            @Mapping(target = "commercialComplexName", expression = "java(baseHousePO.getCommunityName())"),
            @Mapping(target = "houseNum", expression = "java(baseHousePO.getName())")
    })
    CommercialComplexHouseListVO toCommercialComplexHouseListVOByBaseHousePO(HousePo baseHousePO);
}
