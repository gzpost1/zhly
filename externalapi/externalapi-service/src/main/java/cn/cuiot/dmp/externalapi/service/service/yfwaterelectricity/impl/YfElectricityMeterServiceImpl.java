package cn.cuiot.dmp.externalapi.service.service.yfwaterelectricity.impl;


import cn.cuiot.dmp.base.application.mybatis.service.BaseMybatisServiceImpl;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.StatusEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.entity.yfwaterelectricity.YfElectricityMeter;
import cn.cuiot.dmp.externalapi.service.entity.yfwaterelectricity.YfElectricityMeterStatisticsDay;
import cn.cuiot.dmp.externalapi.service.entity.yfwaterelectricity.YfElectricityMeterStatisticsReal;
import cn.cuiot.dmp.externalapi.service.feign.SystemApiService;
import cn.cuiot.dmp.externalapi.service.mapper.yfwaterelectricity.YfElectricityMeterMapper;
import cn.cuiot.dmp.externalapi.service.query.yfwaterelectricity.YfElectricityMeterDTO;
import cn.cuiot.dmp.externalapi.service.service.yfwaterelectricity.IYfElectricityMeterService;
import cn.cuiot.dmp.externalapi.service.service.yfwaterelectricity.IYfElectricityMeterStatisticsDayService;
import cn.cuiot.dmp.externalapi.service.service.yfwaterelectricity.IYfElectricityMeterStatisticsRealService;
import cn.cuiot.dmp.externalapi.service.vendor.yfwaterelectricity.bean.ElectricityTemplate;
import cn.cuiot.dmp.externalapi.service.vendor.yfwaterelectricity.bean.EquipmentCommandControllerReq;
import cn.cuiot.dmp.externalapi.service.vendor.yfwaterelectricity.bean.EquipmentControllerReq;
import cn.cuiot.dmp.externalapi.service.vendor.yfwaterelectricity.bean.MeterCommandControlResp;
import cn.cuiot.dmp.externalapi.service.vendor.yfwaterelectricity.feign.YuFanFeignService;
import cn.cuiot.dmp.externalapi.service.vo.watermeter.YfElectricityMeterStatisticsVO;
import cn.cuiot.dmp.externalapi.service.vo.yfwaterelectricity.YfElectricityMeterVO;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 智慧物联-宇泛电表 服务实现类
 * </p>
 *
 * @author xiaotao
 * @since 2024-10-10
 */
@Service
public class YfElectricityMeterServiceImpl extends BaseMybatisServiceImpl<YfElectricityMeterMapper, YfElectricityMeter> implements IYfElectricityMeterService {

    @Autowired
    private YuFanFeignService yuFanFeignService;

    @Autowired
    private SystemApiService systemApiService;

    @Autowired
    private IYfElectricityMeterStatisticsRealService realService;

    @Autowired
    private IYfElectricityMeterStatisticsDayService dayService;


    @Override
    public Long create(YfElectricityMeterDTO electricityMeterDTO) {

        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();

        // 查看是否存在
        LambdaQueryWrapper<YfElectricityMeter> queryWrapper = Wrappers.<YfElectricityMeter>lambdaQuery()
                .eq(YfElectricityMeter::getDeviceNo, electricityMeterDTO.getDeviceNo())
                .eq(YfElectricityMeter::getCompanyId, currentOrgId);

        YfElectricityMeter yfElectricityMeter = getBaseMapper().selectOne(queryWrapper);
        if (yfElectricityMeter != null) {
            throw new BusinessException(ResultCode.INVALID_PARAM_TYPE, "设备序列号重复");
        }

        ElectricityTemplate req = new ElectricityTemplate();
        req.setName(electricityMeterDTO.getName());
        req.setDeviceNo(electricityMeterDTO.getDeviceNo());

        ElectricityTemplate.Addition addition = new ElectricityTemplate.Addition();
        addition.setIp(electricityMeterDTO.getIp());

        // 从电参数模板，复制默认值
        EquipmentControllerReq eqReq = BeanMapper.map(req, EquipmentControllerReq.class);
        EquipmentControllerReq.Addition additionReq = BeanMapper.map(addition, EquipmentControllerReq.Addition.class);
        eqReq.setAddition(additionReq);


        MeterCommandControlResp<?> resp = yuFanFeignService.addMeter(eqReq);
        if (true) {
            // 如果添加成功
            YfElectricityMeter electricityMeter = BeanMapper.map(electricityMeterDTO, YfElectricityMeter.class);
            electricityMeter.setCompanyId(currentOrgId);
            save(electricityMeter);
            return electricityMeter.getId();
        } else {
            throw new BusinessException(ResultCode.INVALID_PARAM_TYPE, "调用宇泛接口出错，添加失败");
        }
    }

    @Override
    public IPage<YfElectricityMeterVO> queryForPage(YfElectricityMeterDTO vo) {

        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        LambdaQueryWrapper<YfElectricityMeter> wrapper = Wrappers.<YfElectricityMeter>lambdaQuery()
                .eq(YfElectricityMeter::getCompanyId, currentOrgId)
                .eq(vo.getBuildingId() != null, YfElectricityMeter::getBuildingId, vo.getBuildingId())
                .like(StringUtils.isNotBlank(vo.getName()), YfElectricityMeter::getName, vo.getName())
                .like(StringUtils.isNotBlank(vo.getDeviceNo()), YfElectricityMeter::getDeviceNo, vo.getDeviceNo())
                .orderByDesc(YfElectricityMeter::getCreateTime);

        IPage<YfElectricityMeter> page = new Page<>(vo.getPageNo(), vo.getPageSize());
        IPage<YfElectricityMeter> selectPage = getBaseMapper().selectPage(page, wrapper);

        List<YfElectricityMeter> records = selectPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new Page<>();
        }

        List<Long> builds = records.stream().map(YfElectricityMeter::getBuildingId).collect(Collectors.toList());

        BuildingArchiveReq buildingArchiveReq = new BuildingArchiveReq();
        buildingArchiveReq.setIdList(builds);

        List<BuildingArchive> buildingArchives = systemApiService.buildingArchiveQueryForList(buildingArchiveReq);

        Map<Long, BuildingArchive> archiveMap = buildingArchives.stream().collect(Collectors.toMap(BuildingArchive::getId, Function.identity()));


        List<YfElectricityMeterVO> meterVOList = records.stream().map(yfElectricityMeter -> {
            YfElectricityMeterVO meterVO = BeanMapper.map(yfElectricityMeter, YfElectricityMeterVO.class);
            BuildingArchive buildingArchive = archiveMap.get(yfElectricityMeter.getBuildingId());
            if (buildingArchive != null) {
                meterVO.setBuildingName(buildingArchive.getName());
            }
            return meterVO;
        }).collect(Collectors.toList());

        IPage<YfElectricityMeterVO> pageVo = new Page<>(selectPage.getCurrent(), selectPage.getSize(), selectPage.getTotal());
        pageVo.setRecords(meterVOList);
        return pageVo;
    }

    @Override
    public YfElectricityMeterVO queryForDetail(IdParam idParam) {

        YfElectricityMeter yfElectricityMeter = getById(idParam.getId());
        YfElectricityMeterVO meterVO = BeanMapper.map(yfElectricityMeter, YfElectricityMeterVO.class);

        BuildingArchiveReq buildingArchiveReq = new BuildingArchiveReq();
        buildingArchiveReq.setIdList(CollectionUtil.newArrayList(yfElectricityMeter.getBuildingId()));
        List<BuildingArchive> buildingArchives = systemApiService.buildingArchiveQueryForList(buildingArchiveReq);
        if (CollectionUtils.isNotEmpty(buildingArchives)) {
            meterVO.setBuildingName(buildingArchives.get(0).getName());
        }
        return meterVO;
    }

    @Override
    public Boolean update(YfElectricityMeterDTO electricityMeterDTO) {

        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();

        UpdateWrapper<YfElectricityMeter> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(YfElectricityMeter::getId, electricityMeterDTO.getId())
                .eq(YfElectricityMeter::getCompanyId, currentOrgId)
                .set(YfElectricityMeter::getName, electricityMeterDTO.getName())
                .set(YfElectricityMeter::getBuildingId, electricityMeterDTO.getBuildingId())
                .set(YfElectricityMeter::getIp, electricityMeterDTO.getIp());

        return update(updateWrapper);
    }

    @Override
    public Boolean delete(IdsParam idsParam) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        List<Long> ids = idsParam.getIds();
        LambdaQueryWrapper<YfElectricityMeter> wrapper = Wrappers.<YfElectricityMeter>lambdaQuery()
                .in(YfElectricityMeter::getId, ids)
                .eq(YfElectricityMeter::getCompanyId, currentOrgId);
        List<YfElectricityMeter> meterList = getBaseMapper().selectList(wrapper);
        if(CollectionUtils.isEmpty(meterList)){
            throw new BusinessException(ResultCode.INVALID_PARAM_TYPE, "未查询到设备，删除失败");
        }

        for (YfElectricityMeter yfElectricityMeter : meterList) {
            EquipmentCommandControllerReq req = new EquipmentCommandControllerReq(yfElectricityMeter.getDeviceNo(), yfElectricityMeter.getSource());
            MeterCommandControlResp<?> resp = yuFanFeignService.deleteMeter(req);
            if (true) {
                // 删除设备
                removeById(yfElectricityMeter.getId());
            }
        }
        return true;
    }

    @Override
    public IPage<YfElectricityMeterStatisticsVO> queryAmountForPage(YfElectricityMeterDTO vo) {
        IPage<YfElectricityMeterVO> yfElectricityMeterVOIPage = queryForPage(vo);
        List<YfElectricityMeterVO> records = yfElectricityMeterVOIPage.getRecords();

        // 获取设备id
        List<Long> meterIds = records.stream().map(YfElectricityMeterVO::getId).collect(Collectors.toList());

        // 获取设备实时数据
        List<YfElectricityMeterStatisticsReal> realList = realService.queryRealAmount(meterIds);

        Map<Long, YfElectricityMeterStatisticsReal> realMap = realList.stream().collect(Collectors.toMap(YfElectricityMeterStatisticsReal::getMeterId, Function.identity()));


        // 获取设备天统计数据
        if (vo.getRecordBeginTime() == null) {
            vo.setRecordBeginTime(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), 1, 0, 0, 0));
            vo.setRecordEndTime(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), LocalDateTime.now().getDayOfMonth(), 23, 59, 59));
        }

        List<YfElectricityMeterStatisticsDay> dayList = dayService.queryDayAmount(vo);
        Map<Long, YfElectricityMeterStatisticsDay> dayMap = dayList.stream().collect(Collectors.toMap(YfElectricityMeterStatisticsDay::getMeterId, Function.identity()));


        List<YfElectricityMeterStatisticsVO> statisticsVOS = records.stream().map(yfElectricityMeterVO -> {

            YfElectricityMeterStatisticsVO statisticsVO = BeanMapper.map(yfElectricityMeterVO, YfElectricityMeterStatisticsVO.class);
            statisticsVO.setMeterId(yfElectricityMeterVO.getId());
            statisticsVO.setDeviceName(yfElectricityMeterVO.getName());

            // 设置实时统计量
            YfElectricityMeterStatisticsReal statisticsReal = realMap.get(yfElectricityMeterVO.getId());
            if (statisticsReal != null) {
                statisticsVO.setAmount(statisticsReal.getAmount());
            }
            // 设置天的统计量
            YfElectricityMeterStatisticsDay statisticsDay = dayMap.get(yfElectricityMeterVO.getId());
            if (statisticsDay != null) {
                statisticsVO.setAmountTotal(statisticsDay.getAmount());
            }

            return statisticsVO;
        }).collect(Collectors.toList());

        IPage<YfElectricityMeterStatisticsVO> pageVo = new Page<>(yfElectricityMeterVOIPage.getCurrent(), yfElectricityMeterVOIPage.getSize(), yfElectricityMeterVOIPage.getTotal());
        pageVo.setRecords(statisticsVOS);
        return pageVo;
    }
}
