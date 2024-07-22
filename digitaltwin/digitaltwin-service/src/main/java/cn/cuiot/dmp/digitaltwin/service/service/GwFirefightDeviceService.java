package cn.cuiot.dmp.digitaltwin.service.service;

import cn.cuiot.dmp.digitaltwin.service.dto.*;
import cn.cuiot.dmp.digitaltwin.service.entity.GwFirefightDeviceArchitecturalEntity;
import cn.cuiot.dmp.digitaltwin.service.entity.GwFirefightDeviceEntity;
import cn.cuiot.dmp.digitaltwin.service.entity.GwFirefightDeviceNotifierEntity;
import cn.cuiot.dmp.digitaltwin.service.entity.GwFirefightDeviceUnitEntity;
import cn.cuiot.dmp.digitaltwin.service.mapper.GwFirefightDeviceArchitecturalMapper;
import cn.cuiot.dmp.digitaltwin.service.mapper.GwFirefightDeviceMapper;
import cn.cuiot.dmp.digitaltwin.service.mapper.GwFirefightDeviceNotifierMapper;
import cn.cuiot.dmp.digitaltwin.service.mapper.GwFirefightDeviceUnitMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;

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
        GwFirefightDeviceEntity deviceEntity = Optional.ofNullable(baseMapper.getDeviceByDeviceId(dto.getId()))
                .orElseGet(GwFirefightDeviceEntity::new);
        BeanUtils.copyProperties(dto, deviceEntity);

        if (Objects.nonNull(deviceEntity.getId())) {
            //保存设备信息
            updateById(deviceEntity);
            //删除历史数据
            baseMapper.deleteByDeviceId(dto.getId());
            //删除数据库数据
            unitMapper.deleteByParentId(deviceEntity.getId());
            //删除联系人
            notifierMapper.deleteByParentId(deviceEntity.getId());
        }else {
            deviceEntity.setId(IdWorker.getId());
            save(deviceEntity);
        }

        //保存单位信息
        GwFirefightDeviceUnitDto unitDto = dto.getUnitDto();
        if (Objects.nonNull(unitDto)) {
            GwFirefightDeviceUnitEntity unitEntity = new GwFirefightDeviceUnitEntity();
            BeanUtils.copyProperties(unitDto, unitEntity);
            unitEntity.setParentId(deviceEntity.getId());
            unitEntity.setUnitId(unitDto.getId());
            unitMapper.insert(unitEntity);
        }

        //保存建筑信息
        GwFirefightDeviceArchitecturalDto architecturalDto = dto.getArchitecturalDto();
        if (Objects.nonNull(architecturalDto)) {
            GwFirefightDeviceArchitecturalEntity architecturalEntity = new GwFirefightDeviceArchitecturalEntity();
            BeanUtils.copyProperties(architecturalDto, architecturalEntity);
            architecturalEntity.setParentId(deviceEntity.getId());
            architecturalEntity.setArchitecturalId(architecturalDto.getId());
            architecturalMapper.insert(architecturalEntity);
        }

        //保存联系人
        List<GwFirefightDeviceNotifierDto> notifierList = dto.getNotifierList();
        if (CollectionUtils.isNotEmpty(notifierList)) {
            List<GwFirefightDeviceNotifierEntity> collect = notifierList.stream().map(item -> {
                GwFirefightDeviceNotifierEntity notifierEntity = new GwFirefightDeviceNotifierEntity();
                BeanUtils.copyProperties(item, notifierEntity);
                notifierEntity.setParentId(deviceEntity.getId());
                return notifierEntity;
            }).collect(Collectors.toList());
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
        GwFirefightDeviceEntity deviceEntity = list.get(0);
        deviceEntity.setStatus(deviceEntity.getStatus());
        updateById(deviceEntity);
    }
}
