package cn.cuiot.dmp.digitaltwin.service.service;

import cn.cuiot.dmp.digitaltwin.service.entity.GwFirefightDeviceArchitecturalEntity;
import cn.cuiot.dmp.digitaltwin.service.entity.GwFirefightDeviceEntity;
import cn.cuiot.dmp.digitaltwin.service.entity.GwFirefightDeviceNotifierEntity;
import cn.cuiot.dmp.digitaltwin.service.entity.GwFirefightDeviceUnitEntity;
import cn.cuiot.dmp.digitaltwin.service.entity.dto.*;
import cn.cuiot.dmp.digitaltwin.service.enums.GwFirefightDeviceEnum;
import cn.cuiot.dmp.digitaltwin.service.mapper.GwFirefightDeviceArchitecturalMapper;
import cn.cuiot.dmp.digitaltwin.service.mapper.GwFirefightDeviceMapper;
import cn.cuiot.dmp.digitaltwin.service.mapper.GwFirefightDeviceNotifierMapper;
import cn.cuiot.dmp.digitaltwin.service.mapper.GwFirefightDeviceUnitMapper;
import cn.cuiot.dmp.digitaltwin.service.entity.query.GwFirefightDeviceQuery;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 格物消防-设备信息 业务层
 *
 * @Author: zc
 * @Date: 2024-06-16
 */
@Slf4j
@Service
public class GwFirefightDeviceService extends ServiceImpl<GwFirefightDeviceMapper, GwFirefightDeviceEntity> {

    @Autowired
    private GwFirefightDeviceUnitMapper unitMapper;
    @Autowired
    private GwFirefightDeviceArchitecturalMapper architecturalMapper;
    @Autowired
    private GwFirefightDeviceNotifierMapper notifierMapper;

    /**
     * 保存
     *
     * @Param dto 参数
     */
    public void save(GwFirefightDeviceDto dto) {
        // 初始化设备实体和父ID
        GwFirefightDeviceEntity deviceEntity = Optional.ofNullable(dto.getDeviceId())
                .map(deviceId -> baseMapper.getDeviceByDeviceId(deviceId))
                .orElseGet(GwFirefightDeviceEntity::new);

        // 复制属性值
        BeanUtils.copyProperties(dto, deviceEntity);

        if (Objects.isNull(deviceEntity.getId())) {
            // 新增设备
            deviceEntity.setId(IdWorker.getId());
            save(deviceEntity);
        } else {
            // 更新设备
            deviceEntity.setUpdateTime(new Date());
            baseMapper.updateEntity(deviceEntity);
        }

        // 操作设备关联信息
        saveDeviceRelation(dto, deviceEntity.getId());
    }

    /**
     * 保存设备关联信息
     *
     * @Param dto
     * @Param parentId 设备id
     */
    private void saveDeviceRelation(GwFirefightDeviceDto dto, Long parentId) {
        //删除数据库单位数据
        unitMapper.deleteByParentId(parentId);
        //保存单位信息
        GwFirefightDeviceUnitDto unitDto = dto.getUnitDto();
        if (Objects.nonNull(unitDto)) {
            unitMapper.insert(GwFirefightDeviceUnitEntity.dtoToEntity(unitDto, parentId));
        }

        //删除数据库建筑对象数据
        architecturalMapper.deleteByParentId(parentId);
        //保存建筑信息
        GwFirefightDeviceArchitecturalDto architecturalDto = dto.getArchitecturalDto();
        if (Objects.nonNull(architecturalDto)) {
            architecturalMapper.insert(GwFirefightDeviceArchitecturalEntity.dtoToEntity(architecturalDto, parentId));
        }

        //删除数据库联系人数据
        notifierMapper.deleteByParentId(parentId);
        //保存联系人
        List<GwFirefightDeviceNotifierDto> notifierList = dto.getNotifiers();
        if (CollectionUtils.isNotEmpty(notifierList)) {
            List<GwFirefightDeviceNotifierEntity> collect = notifierList.stream()
                    .map(item -> GwFirefightDeviceNotifierEntity.dtoToEntity(item, parentId))
                    .collect(Collectors.toList());
            notifierMapper.batchInsert(collect);
        }
    }

    /**
     * 更新设备状态
     *
     * @Param dto 参数
     */
    public void updateStatus(GwFirefightDeviceStatusDto dto) {
        if (StringUtils.isBlank(dto.getDeviceId())) {
            log.error("格物消防设备状态更新失败，设备id值为空");
            return;
        }
        if (StringUtils.isBlank(dto.getStatus())) {
            log.error("格物消防设备状态更新失败，状态值为空");
            return;
        }
        List<GwFirefightDeviceEntity> list = list(new LambdaQueryWrapper<GwFirefightDeviceEntity>()
                .eq(GwFirefightDeviceEntity::getDeviceId, dto.getDeviceId()));
        if (CollectionUtils.isEmpty(list)) {
            log.error("格物消防设备状态更新失败，不存在该设备【{}】", dto.getDeviceId());
            return;
        }
        baseMapper.updateStatus(list.get(0).getId(), dto.getStatus());
    }

    /**
     * 查询最新设备的状态
     * @Param
     * @return String 设备状态
     */
    public String queryDeviceStatus(GwFirefightDeviceQuery query) {
        if (StringUtils.isBlank(query.getDeviceId())) {
            return GwFirefightDeviceEnum.OFF_LINE.getCode();
        }
        GwFirefightDeviceEntity device = getOne(new LambdaQueryWrapper<GwFirefightDeviceEntity>()
                .eq(GwFirefightDeviceEntity::getDeviceId, query.getDeviceId())
                .last(" LIMIT 1"));
        return Objects.nonNull(device) ? device.getStatus() : GwFirefightDeviceEnum.OFF_LINE.getCode();
    }
}
