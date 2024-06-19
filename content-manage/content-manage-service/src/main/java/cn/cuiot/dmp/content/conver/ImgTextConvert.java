package cn.cuiot.dmp.content.conver;//	模板

import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.content.dal.entity.ContentImgTextEntity;
import cn.cuiot.dmp.content.param.dto.ContentImgTextCreateDto;
import cn.cuiot.dmp.content.param.vo.ImgTextVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/29 14:29
 */
@Mapper
public interface ImgTextConvert {

    ImgTextConvert INSTANCE = Mappers.getMapper(ImgTextConvert.class);

    ImgTextVo convert(ContentImgTextEntity contentImgTextEntity);

    ContentImgTextEntity convert(ContentImgTextCreateDto createDTO);

    List<ImgTextVo> convert(List<ContentImgTextEntity> imgTextEntityList);

    default IPage<ImgTextVo> convert(IPage<ContentImgTextEntity> imgTextEntityIPage, HashMap<Long, DepartmentDto> departmentMapByIds,
                                     HashMap<Long, BuildingArchive> buildingMapByIds, HashMap<Long, BaseUserDto> userMapByIds) {
        return imgTextEntityIPage.convert(imgTextEntity -> {
            ImgTextVo imgTextVo = convert(imgTextEntity);
            List<String> departmentNames = new ArrayList<>();
            Optional.ofNullable(imgTextEntity.getDepartments()).orElse(new ArrayList<>()).forEach(departmentId -> {
                departmentNames.add(Optional.ofNullable(departmentMapByIds.get(Long.parseLong(departmentId))).orElse(new DepartmentDto()).getName());
            });
            List<String> buildingNames = new ArrayList<>();
            Optional.ofNullable(imgTextEntity.getBuildings()).orElse(new ArrayList<>()).forEach(buildingId -> {
                buildingNames.add(Optional.ofNullable(buildingMapByIds.get(Long.parseLong(buildingId))).orElse(new BuildingArchive()).getName());
            });
            imgTextVo.setDepartmentNames(departmentNames);
            imgTextVo.setBuildingNames(buildingNames);
            imgTextVo.setCreatUserName(Optional.ofNullable(userMapByIds.get(imgTextEntity.getCreateUser())).orElse(new BaseUserDto()).getName());
            return imgTextVo;
        });
    }

}
