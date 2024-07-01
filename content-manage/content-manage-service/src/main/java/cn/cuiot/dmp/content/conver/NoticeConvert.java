package cn.cuiot.dmp.content.conver;//	模板

import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.content.constant.ContentConstants;
import cn.cuiot.dmp.content.dal.entity.ContentNoticeEntity;
import cn.cuiot.dmp.content.param.dto.NoticeCreateDto;
import cn.cuiot.dmp.content.param.vo.NoticeVo;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.*;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/29 15:52
 */
@Mapper
public interface NoticeConvert {

    NoticeConvert INSTANCE = Mappers.getMapper(NoticeConvert.class);

    NoticeVo convert(ContentNoticeEntity contentNoticeEntity);

    //    @Mappings(value = {
//            @Mapping(target = "departments", ignore = true),
//            @Mapping(target = "buildings", ignore = true)
//    })
    ContentNoticeEntity convert(NoticeCreateDto createDto);

    default IPage<NoticeVo> convert(IPage<ContentNoticeEntity> noticeEntityIPage, HashMap<Long, DepartmentDto> departmentMapByIds, HashMap<Long, BuildingArchive> buildingMapByIds, HashMap<Long, BaseUserDto> userMapByIds) {
        return noticeEntityIPage.convert(contentNoticeEntity -> {
            NoticeVo noticeVo = convert(contentNoticeEntity);
            List<String> departmentNames = new ArrayList<>();
            Optional.ofNullable(contentNoticeEntity.getDepartments()).orElse(new ArrayList<>()).forEach(departmentId -> {
                departmentNames.add(Optional.ofNullable(departmentMapByIds.get(Long.parseLong(departmentId))).orElse(new DepartmentDto()).getName());
            });
            List<String> buildingNames = new ArrayList<>();
            Optional.ofNullable(contentNoticeEntity.getBuildings()).orElse(new ArrayList<>()).forEach(buildingId -> {
                buildingNames.add(Optional.ofNullable(buildingMapByIds.get(Long.parseLong(buildingId))).orElse(new BuildingArchive()).getName());
            });
            noticeVo.setDepartmentNames(departmentNames);
            noticeVo.setBuildingNames(buildingNames);
            noticeVo.setCreatUserName(Optional.ofNullable(userMapByIds.get(contentNoticeEntity.getCreateUser())).orElse(new BaseUserDto()).getUsername());

            if (ContentConstants.PublishStatus.PUBLISHED.equals(contentNoticeEntity.getPublishStatus())) {
                noticeVo.setEffectiveStatus(checkEffectiveStatus(contentNoticeEntity));
            }
            return noticeVo;
        });
    }

    default Byte checkEffectiveStatus(ContentNoticeEntity contentNoticeEntity) {
        if (DateUtil.compare(new Date(), contentNoticeEntity.getEffectiveStartTime()) < 0) {
            return EntityConstants.NOT_EFFECTIVE;
        } else if (DateUtil.compare(new Date(), contentNoticeEntity.getEffectiveEndTime()) > 0) {
            return EntityConstants.EXPIRE;
        } else {
            return EntityConstants.NORMAL;
        }
    }

    List<NoticeVo> convert(List<ContentNoticeEntity> noticeEntityList);

    default IPage<NoticeVo> convert(IPage<ContentNoticeEntity> noticeEntityIPage) {
        return noticeEntityIPage.convert(this::convert);
    }
}
