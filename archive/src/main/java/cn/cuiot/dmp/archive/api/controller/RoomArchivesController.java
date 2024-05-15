package cn.cuiot.dmp.archive.api.controller;

import cn.cuiot.dmp.archive.application.param.dto.ArchiveBatchUpdateDTO;
import cn.cuiot.dmp.archive.application.param.query.RoomArchivesQuery;
import cn.cuiot.dmp.archive.application.service.RoomArchivesService;
import cn.cuiot.dmp.archive.infrastructure.entity.RoomArchivesEntity;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * @author liujianyu
 * @description 空间档案
 * @since 2024-05-15 10:30
 */
@RestController
@RequestMapping("/room")
public class RoomArchivesController {

    @Autowired
    private RoomArchivesService roomArchivesService;

    /**
     * 分页列表
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<RoomArchivesEntity>> queryForPage(@RequestBody @Valid RoomArchivesQuery query) {
        LambdaQueryWrapper<RoomArchivesEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoomArchivesEntity::getLoupanId, query.getLoupanId());
        wrapper.like(StringUtils.isNotBlank(query.getName()), RoomArchivesEntity::getName, query.getName());
        wrapper.like(StringUtils.isNotBlank(query.getOwnershipUnit()), RoomArchivesEntity::getOwnershipUnit, query.getOwnershipUnit());
        wrapper.eq(Objects.nonNull(query.getResourceType()), RoomArchivesEntity::getResourceType, query.getResourceType());
        wrapper.eq(Objects.nonNull(query.getStatus()), RoomArchivesEntity::getStatus, query.getStatus());
        wrapper.eq(Objects.nonNull(query.getProfessionalPurpose()), RoomArchivesEntity::getProfessionalPurpose, query.getProfessionalPurpose());
        wrapper.eq(Objects.nonNull(query.getLocationMethod()), RoomArchivesEntity::getLocationMethod, query.getLocationMethod());
        wrapper.eq(Objects.nonNull(query.getSpaceCategory()), RoomArchivesEntity::getSpaceCategory, query.getSpaceCategory());
        wrapper.eq(Objects.nonNull(query.getBusinessNature()), RoomArchivesEntity::getBusinessNature, query.getBusinessNature());
        wrapper.eq(Objects.nonNull(query.getOwnershipAttribute()), RoomArchivesEntity::getOwnershipAttribute, query.getOwnershipAttribute());
        IPage<RoomArchivesEntity> res = roomArchivesService.page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper);
        return IdmResDTO.success(res);
    }

    /**
     * 创建
     */
    @RequiresPermissions
    @PostMapping("/create")
    public IdmResDTO create(@RequestBody RoomArchivesEntity entity) {
        // 校验参数合法性，写在service层，用于导入的时候使用
        roomArchivesService.checkParams(entity);
        // 保存数据
        roomArchivesService.save(entity);
        return IdmResDTO.success();
    }

    /**
     * 修改
     */
    @RequiresPermissions
    @PostMapping("/update")
    public IdmResDTO update(@RequestBody RoomArchivesEntity entity) {
        roomArchivesService.updateById(entity);
        return IdmResDTO.success();
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @PostMapping("/delete")
    public IdmResDTO delete(@RequestBody @Valid IdParam idParam) {
        roomArchivesService.removeById(idParam.getId());
        return IdmResDTO.success();
    }

    /**
     * 批量修改
     */
    @RequiresPermissions
    @PostMapping("/updateByIds")
    public IdmResDTO updateByIds(@RequestBody @Valid ArchiveBatchUpdateDTO param) {
        LambdaQueryWrapper<RoomArchivesEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(RoomArchivesEntity::getId, param.getIds());

        RoomArchivesEntity entity = new RoomArchivesEntity();
        entity.setLoupanId(param.getLoupanId());
        roomArchivesService.update(entity, wrapper);
        return IdmResDTO.success();
    }
}
