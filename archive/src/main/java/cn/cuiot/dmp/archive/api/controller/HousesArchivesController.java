package cn.cuiot.dmp.archive.api.controller;

import cn.cuiot.dmp.archive.application.param.dto.ArchiveBatchUpdateDTO;
import cn.cuiot.dmp.archive.application.param.query.HousesArchivesQuery;
import cn.cuiot.dmp.archive.application.service.HousesArchivesService;
import cn.cuiot.dmp.archive.infrastructure.entity.HousesArchivesEntity;
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
 * @description 房屋档案
 * @since 2024-05-15 10:30
 */
@RestController
@RequestMapping("/houses")
public class HousesArchivesController {

    @Autowired
    private HousesArchivesService housesArchivesService;

    /**
     * 分页列表
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<HousesArchivesEntity>> queryForPage(@RequestBody @Valid HousesArchivesQuery query) {
        LambdaQueryWrapper<HousesArchivesEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HousesArchivesEntity::getLoupanId, query.getLoupanId());
        wrapper.like(StringUtils.isNotBlank(query.getCode()), HousesArchivesEntity::getCode, query.getCode());
        wrapper.like(StringUtils.isNotBlank(query.getOwnershipUnit()), HousesArchivesEntity::getOwnershipUnit, query.getOwnershipUnit());
        IPage<HousesArchivesEntity> res = housesArchivesService.page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper);
        return IdmResDTO.success(res);
    }

    /**
     * 创建
     */
    @RequiresPermissions
    @PostMapping("/create")
    public IdmResDTO create(@RequestBody HousesArchivesEntity entity) {
        // 校验参数合法性，写在service层，用于导入的时候使用
        housesArchivesService.checkParams(entity);
        // 保存数据
        housesArchivesService.save(entity);
        return IdmResDTO.success();
    }

    /**
     * 修改
     */
    @RequiresPermissions
    @PostMapping("/update")
    public IdmResDTO update(@RequestBody HousesArchivesEntity entity) {
        housesArchivesService.updateById(entity);
        return IdmResDTO.success();
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @PostMapping("/delete")
    public IdmResDTO delete(@RequestBody @Valid IdParam idParam) {
        housesArchivesService.removeById(idParam.getId());
        return IdmResDTO.success();
    }

    /**
     * 批量修改
     */
    @RequiresPermissions
    @PostMapping("/updateByIds")
    public IdmResDTO updateByIds(@RequestBody @Valid ArchiveBatchUpdateDTO param) {
        LambdaQueryWrapper<HousesArchivesEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(HousesArchivesEntity::getId, param.getIds());

        HousesArchivesEntity entity = new HousesArchivesEntity();
        entity.setLoupanId(param.getLoupanId());
        housesArchivesService.update(entity, wrapper);
        return IdmResDTO.success();
    }
}
