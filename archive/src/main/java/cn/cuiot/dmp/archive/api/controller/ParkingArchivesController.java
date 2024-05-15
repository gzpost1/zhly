package cn.cuiot.dmp.archive.api.controller;

import cn.cuiot.dmp.archive.application.param.dto.ArchiveBatchUpdateDTO;
import cn.cuiot.dmp.archive.application.param.query.ParkingArchivesQuery;
import cn.cuiot.dmp.archive.application.service.ParkingArchivesService;
import cn.cuiot.dmp.archive.infrastructure.entity.ParkingArchivesEntity;
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
 * @description 车位档案
 * @since 2024-05-15 10:30
 */
@RestController
@RequestMapping("/parking")
public class ParkingArchivesController {

    @Autowired
    private ParkingArchivesService parkingArchivesService;

    /**
     * 分页列表
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<ParkingArchivesEntity>> queryForPage(@RequestBody @Valid ParkingArchivesQuery query) {
        LambdaQueryWrapper<ParkingArchivesEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ParkingArchivesEntity::getLoupanId, query.getLoupanId());
        wrapper.eq(StringUtils.isNotBlank(query.getCode()),ParkingArchivesEntity::getCode, query.getCode());
        wrapper.eq(Objects.nonNull(query.getStatus()), ParkingArchivesEntity::getStatus, query.getStatus());
        wrapper.eq(Objects.nonNull(query.getUsageStatus()), ParkingArchivesEntity::getUsageStatus, query.getUsageStatus());
        wrapper.eq(Objects.nonNull(query.getParkingType()), ParkingArchivesEntity::getParkingType, query.getParkingType());
        IPage<ParkingArchivesEntity> res = parkingArchivesService.page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper);
        return IdmResDTO.success(res);
    }

    /**
     * 创建
     */
    @RequiresPermissions
    @PostMapping("/create")
    public IdmResDTO create(@RequestBody ParkingArchivesEntity entity) {
        // 校验参数合法性，写在service层，用于导入的时候使用
        parkingArchivesService.checkParams(entity);
        // 保存数据
        parkingArchivesService.save(entity);
        return IdmResDTO.success(null);
    }

    /**
     * 修改
     */
    @RequiresPermissions
    @PostMapping("/update")
    public IdmResDTO update(@RequestBody ParkingArchivesEntity entity) {
        parkingArchivesService.updateById(entity);
        return IdmResDTO.success(null);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @PostMapping("/delete")
    public IdmResDTO delete(@RequestBody @Valid IdParam idParam) {
        parkingArchivesService.removeById(idParam.getId());
        return IdmResDTO.success();
    }

    /**
     * 批量修改
     */
    @RequiresPermissions
    @PostMapping("/updateByIds")
    public IdmResDTO updateByIds(@RequestBody @Valid ArchiveBatchUpdateDTO param) {
        LambdaQueryWrapper<ParkingArchivesEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ParkingArchivesEntity::getId, param.getIds());

        ParkingArchivesEntity entity = new ParkingArchivesEntity();
        entity.setLoupanId(param.getLoupanId());
        parkingArchivesService.update(entity, wrapper);
        return IdmResDTO.success();
    }
}
