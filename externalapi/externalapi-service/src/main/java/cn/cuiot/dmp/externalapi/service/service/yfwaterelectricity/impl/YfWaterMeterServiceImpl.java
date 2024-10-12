package cn.cuiot.dmp.externalapi.service.service.yfwaterelectricity.impl;


import cn.cuiot.dmp.base.application.mybatis.service.BaseMybatisServiceImpl;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.entity.yfwaterelectricity.*;
import cn.cuiot.dmp.externalapi.service.feign.SystemApiService;
import cn.cuiot.dmp.externalapi.service.mapper.yfwaterelectricity.YfWaterMeterMapper;
import cn.cuiot.dmp.externalapi.service.query.yfwaterelectricity.YfWaterMeterDTO;
import cn.cuiot.dmp.externalapi.service.service.yfwaterelectricity.*;
import cn.cuiot.dmp.externalapi.service.vendor.yfwaterelectricity.bean.*;
import cn.cuiot.dmp.externalapi.service.vendor.yfwaterelectricity.feign.YuFanFeignService;
import cn.cuiot.dmp.externalapi.service.vo.watermeter.YfElectricityMeterStatisticsVO;
import cn.cuiot.dmp.externalapi.service.vo.watermeter.YfWaterMeterStatisticsVO;
import cn.cuiot.dmp.externalapi.service.vo.yfwaterelectricity.YfElectricityMeterVO;
import cn.cuiot.dmp.externalapi.service.vo.yfwaterelectricity.YfWaterMeterVO;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 智慧物联-宇泛水表 服务实现类
 * </p>
 *
 * @author xiaotao
 * @since 2024-10-10
 */
@Service
public class YfWaterMeterServiceImpl extends BaseMybatisServiceImpl<YfWaterMeterMapper, YfWaterMeter> implements IYfWaterMeterService {

    @Autowired
    private YuFanFeignService yuFanFeignService;

    @Autowired
    private SystemApiService systemApiService;

    @Autowired
    private IYfWaterMeterStatisticsRealService realService;

    @Autowired
    private IYfWaterMeterStatisticsDayService dayService;

    @Override
    public Long create(YfWaterMeterDTO waterMeterDTO) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();

        // 查看是否存在
        LambdaQueryWrapper<YfWaterMeter> queryWrapper = Wrappers.<YfWaterMeter>lambdaQuery()
                .eq(YfWaterMeter::getDeviceNo, waterMeterDTO.getDeviceNo())
                .eq(YfWaterMeter::getCompanyId, currentOrgId);

        YfWaterMeter yfWaterMeter = getBaseMapper().selectOne(queryWrapper);
        if (yfWaterMeter != null) {
            throw new BusinessException(ResultCode.INVALID_PARAM_TYPE, "设备序列号重复");
        }

        WaterTemplate req = new WaterTemplate();
        req.setName(waterMeterDTO.getName());
        req.setDeviceNo(waterMeterDTO.getDeviceNo());

        WaterTemplate.Addition addition = new WaterTemplate.Addition();
        addition.setIp(waterMeterDTO.getIp());

        // 从水表参数模板，复制默认值
        EquipmentControllerReq eqReq = BeanMapper.map(req, EquipmentControllerReq.class);
        EquipmentControllerReq.Addition additionReq = BeanMapper.map(addition, EquipmentControllerReq.Addition.class);
        eqReq.setAddition(additionReq);


        MeterCommandControlResp<?> resp = yuFanFeignService.addMeter(eqReq);
        if (true) {
            // 如果添加成功
            YfWaterMeter waterMeter = BeanMapper.map(waterMeterDTO, YfWaterMeter.class);
            waterMeter.setCompanyId(currentOrgId);
            save(waterMeter);
            return waterMeter.getId();
        } else {
            throw new BusinessException(ResultCode.INVALID_PARAM_TYPE, "调用宇泛接口出错，添加失败");
        }
    }

    @Override
    public Boolean update(YfWaterMeterDTO waterMeterDTO) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();

        UpdateWrapper<YfWaterMeter> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(YfWaterMeter::getId, waterMeterDTO.getId())
                .eq(YfWaterMeter::getCompanyId, currentOrgId)
                .set(YfWaterMeter::getName, waterMeterDTO.getName())
                .set(YfWaterMeter::getBuildingId, waterMeterDTO.getBuildingId())
                .set(YfWaterMeter::getIp, waterMeterDTO.getIp());

        return update(updateWrapper);
    }

    @Override
    public Boolean delete(IdsParam idsParam) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        List<Long> ids = idsParam.getIds();
        LambdaQueryWrapper<YfWaterMeter> wrapper = Wrappers.<YfWaterMeter>lambdaQuery()
                .in(YfWaterMeter::getId, ids)
                .eq(YfWaterMeter::getCompanyId, currentOrgId);
        List<YfWaterMeter> meterList = getBaseMapper().selectList(wrapper);
        if(CollectionUtils.isEmpty(meterList)){
            throw new BusinessException(ResultCode.INVALID_PARAM_TYPE, "未查询到设备，删除失败");
        }

        for (YfWaterMeter yfWaterMeter : meterList) {
            EquipmentCommandControllerReq req = new EquipmentCommandControllerReq(yfWaterMeter.getDeviceNo(), yfWaterMeter.getSource());
            MeterCommandControlResp<?> resp = yuFanFeignService.deleteMeter(req);
            if (true) {
                // 删除设备
                removeById(yfWaterMeter.getId());
            }
        }
        return true;
    }

    @Override
    public IPage<YfWaterMeterVO> queryForPage(YfWaterMeterDTO vo) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        LambdaQueryWrapper<YfWaterMeter> wrapper = Wrappers.<YfWaterMeter>lambdaQuery()
                .eq(YfWaterMeter::getCompanyId, currentOrgId)
                .eq(vo.getBuildingId() != null, YfWaterMeter::getBuildingId, vo.getBuildingId())
                .like(StringUtils.isNotBlank(vo.getName()), YfWaterMeter::getName, vo.getName())
                .like(StringUtils.isNotBlank(vo.getDeviceNo()), YfWaterMeter::getDeviceNo, vo.getDeviceNo())
                .orderByDesc(YfWaterMeter::getCreateTime);

        IPage<YfWaterMeter> page = new Page<>(vo.getPageNo(), vo.getPageSize());
        IPage<YfWaterMeter> selectPage = getBaseMapper().selectPage(page, wrapper);

        List<YfWaterMeter> records = selectPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new Page<>();
        }

        List<Long> builds = records.stream().map(YfWaterMeter::getBuildingId).collect(Collectors.toList());

        BuildingArchiveReq buildingArchiveReq = new BuildingArchiveReq();
        buildingArchiveReq.setIdList(builds);

        List<BuildingArchive> buildingArchives = systemApiService.buildingArchiveQueryForList(buildingArchiveReq);

        Map<Long, BuildingArchive> archiveMap = buildingArchives.stream().collect(Collectors.toMap(BuildingArchive::getId, Function.identity()));


        List<YfWaterMeterVO> meterVOList = records.stream().map(yfWaterMeter -> {
            YfWaterMeterVO meterVO = BeanMapper.map(yfWaterMeter, YfWaterMeterVO.class);
            BuildingArchive buildingArchive = archiveMap.get(yfWaterMeter.getBuildingId());
            if (buildingArchive != null) {
                meterVO.setBuildingName(buildingArchive.getName());
            }
            return meterVO;
        }).collect(Collectors.toList());

        IPage<YfWaterMeterVO> pageVo = new Page<>(selectPage.getCurrent(), selectPage.getSize(), selectPage.getTotal());
        pageVo.setRecords(meterVOList);
        return pageVo;
    }

    @Override
    public YfWaterMeterVO queryForDetail(IdParam idParam) {
        YfWaterMeter yfWaterMeter = getById(idParam.getId());
        YfWaterMeterVO meterVO = BeanMapper.map(yfWaterMeter, YfWaterMeterVO.class);

        BuildingArchiveReq buildingArchiveReq = new BuildingArchiveReq();
        buildingArchiveReq.setIdList(CollectionUtil.newArrayList(yfWaterMeter.getBuildingId()));
        List<BuildingArchive> buildingArchives = systemApiService.buildingArchiveQueryForList(buildingArchiveReq);
        if (CollectionUtils.isNotEmpty(buildingArchives)) {
            meterVO.setBuildingName(buildingArchives.get(0).getName());
        }
        return meterVO;
    }

    @Override
    public IPage<YfWaterMeterStatisticsVO> queryAmountForPage(YfWaterMeterDTO vo) {
        IPage<YfWaterMeterVO> yfWaterMeterVOIPage = queryForPage(vo);
        List<YfWaterMeterVO> records = yfWaterMeterVOIPage.getRecords();

        // 获取设备id
        List<Long> meterIds = records.stream().map(YfWaterMeterVO::getId).collect(Collectors.toList());

        // 获取设备实时数据
        List<YfWaterMeterStatisticsReal> realList = realService.queryRealAmount(meterIds);

        Map<Long, YfWaterMeterStatisticsReal> realMap = realList.stream().collect(Collectors.toMap(YfWaterMeterStatisticsReal::getMeterId, Function.identity()));


        // 获取设备天统计数据
        if (vo.getRecordBeginTime() == null) {
            vo.setRecordBeginTime(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), 1, 0, 0, 0));
            vo.setRecordEndTime(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), LocalDateTime.now().getDayOfMonth(), 23, 59, 59));
        }

        List<YfWaterMeterStatisticsDay> dayList = dayService.queryDayAmount(vo);
        Map<Long, YfWaterMeterStatisticsDay> dayMap = dayList.stream().collect(Collectors.toMap(YfWaterMeterStatisticsDay::getMeterId, Function.identity()));


        List<YfWaterMeterStatisticsVO> statisticsVOS = records.stream().map(yfWaterMeterVO -> {

            YfWaterMeterStatisticsVO statisticsVO = BeanMapper.map(yfWaterMeterVO, YfWaterMeterStatisticsVO.class);
            statisticsVO.setMeterId(yfWaterMeterVO.getId());
            statisticsVO.setDeviceName(yfWaterMeterVO.getName());

            // 设置实时统计量
            YfWaterMeterStatisticsReal statisticsReal = realMap.get(yfWaterMeterVO.getId());
            if (statisticsReal != null) {
                statisticsVO.setAmount(statisticsReal.getAmount());
            }
            // 设置天的统计量
            YfWaterMeterStatisticsDay statisticsDay = dayMap.get(yfWaterMeterVO.getId());
            if (statisticsDay != null) {
                statisticsVO.setAmountTotal(statisticsDay.getAmount());
            }

            return statisticsVO;
        }).collect(Collectors.toList());

        IPage<YfWaterMeterStatisticsVO> pageVo = new Page<>(yfWaterMeterVOIPage.getCurrent(), yfWaterMeterVOIPage.getSize(), yfWaterMeterVOIPage.getTotal());
        pageVo.setRecords(statisticsVOS);
        return pageVo;
    }
}
