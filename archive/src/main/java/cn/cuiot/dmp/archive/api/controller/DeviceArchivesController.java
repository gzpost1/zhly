package cn.cuiot.dmp.archive.api.controller;

import cn.cuiot.dmp.archive.application.param.dto.ArchiveBatchUpdateDTO;
import cn.cuiot.dmp.archive.application.param.query.DeviceArchivesQuery;
import cn.cuiot.dmp.archive.application.service.DeviceArchivesService;
import cn.cuiot.dmp.archive.infrastructure.entity.DeviceArchivesEntity;
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
 * @description 设备档案
 * @since 2024-05-15 10:29
 */
@RestController
@RequestMapping("/device")
public class DeviceArchivesController {

    @Autowired
    private DeviceArchivesService deviceArchivesService;

    /**
     * 分页列表
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<DeviceArchivesEntity>> queryForPage(@RequestBody @Valid DeviceArchivesQuery query) {
        LambdaQueryWrapper<DeviceArchivesEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DeviceArchivesEntity::getLoupanId, query.getLoupanId());
        wrapper.like(StringUtils.isNotBlank(query.getDeviceName()), DeviceArchivesEntity::getDeviceName, query.getDeviceName());
        wrapper.like(StringUtils.isNotBlank(query.getInstallationLocation()), DeviceArchivesEntity::getInstallationLocation, query.getInstallationLocation());
        wrapper.eq(Objects.nonNull(query.getDeviceStatus()), DeviceArchivesEntity::getDeviceStatus, query.getDeviceStatus());
        wrapper.eq(Objects.nonNull(query.getDeviceCategory()), DeviceArchivesEntity::getDeviceCategory, query.getDeviceCategory());
        IPage<DeviceArchivesEntity> res = deviceArchivesService.page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper);
        return IdmResDTO.success(res);
    }

    /**
     * 创建
     */
    @RequiresPermissions
    @PostMapping("/create")
    public IdmResDTO create(@RequestBody DeviceArchivesEntity entity) {
        // 校验参数合法性，写在service层，用于导入的时候使用
        deviceArchivesService.checkParams(entity);
        // 保存数据
        deviceArchivesService.save(entity);
        return IdmResDTO.success(null);
    }

    /**
     * 修改
     */
    @RequiresPermissions
    @PostMapping("/update")
    public IdmResDTO update(@RequestBody DeviceArchivesEntity entity) {
        deviceArchivesService.updateById(entity);
        return IdmResDTO.success(null);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @PostMapping("/delete")
    public IdmResDTO delete(@RequestBody @Valid IdParam idParam) {
        deviceArchivesService.removeById(idParam.getId());
        return IdmResDTO.success();
    }

    /**
     * 批量修改
     */
    @RequiresPermissions
    @PostMapping("/updateByIds")
    public IdmResDTO updateByIds(@RequestBody @Valid ArchiveBatchUpdateDTO param) {
        LambdaQueryWrapper<DeviceArchivesEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(DeviceArchivesEntity::getId, param.getIds());

        DeviceArchivesEntity entity = new DeviceArchivesEntity();
        entity.setLoupanId(param.getLoupanId());
        deviceArchivesService.update(entity, wrapper);
        return IdmResDTO.success();
    }
}
