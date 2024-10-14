package cn.cuiot.dmp.externalapi.service.service.hik;

import cn.cuiot.dmp.common.bean.external.HIKEntranceGuardBO;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.constant.HaikangDataDictConstant;
import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangDataDictEntity;
import cn.cuiot.dmp.externalapi.service.entity.hik.HikAcsDoorEventsEntity;
import cn.cuiot.dmp.externalapi.service.mapper.hik.HikAcsDoorEventsMapper;
import cn.cuiot.dmp.externalapi.service.query.hik.HikAcsDoorEventsPageQuery;
import cn.cuiot.dmp.externalapi.service.query.hik.HikIrdsResourcesByParamsQuery;
import cn.cuiot.dmp.externalapi.service.vendor.hik.HikApiFeignService;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req.HikIrdsResourcesByParamsReq;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req.HikRegionDetailReq;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikDoorEventsResp;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikIrdsResourcesByParamsResp;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikRegionDetailResp;
import cn.cuiot.dmp.externalapi.service.vendor.hik.config.HikProperties;
import cn.cuiot.dmp.externalapi.service.vendor.hik.enums.ProtocolEnum;
import cn.cuiot.dmp.externalapi.service.vo.hik.HikAcsDoorEventsPageVO;
import cn.cuiot.dmp.externalapi.service.vo.hik.HikCommonResourcesVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 海康门禁点事件 业务层
 *
 * @Author: zc
 * @Date: 2024-10-11
 */
@Service
public class HikAcsDoorEventsService extends ServiceImpl<HikAcsDoorEventsMapper, HikAcsDoorEventsEntity> {

    @Autowired
    private HaikangDataDictService haikangDataDictService;
    @Autowired
    private HikApiFeignService hikApiFeignService;
    @Autowired
    private HikCommonHandle hikCommonHandle;
    @Autowired
    private HikProperties hikProperties;

    /**
     * 区域查询默认分页大小
     */
    private final static Long DEFAULT_PAGE_SIZE = 1000L;

    /**
     * 分页
     *
     * @return IPage<HikAcsDoorEventsPageVO>
     * @Param query 参数
     */
    public IPage<HikAcsDoorEventsPageVO> queryForPage(HikAcsDoorEventsPageQuery query) {
        // 企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        // 获取当前企业的对接配置
        HIKEntranceGuardBO bo = hikCommonHandle.queryHikConfigByPlatfromInfo(companyId);

        LambdaQueryWrapper<HikAcsDoorEventsEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HikAcsDoorEventsEntity::getCompanyId, companyId);
        wrapper.like(StringUtils.isNotBlank(query.getPersonName()), HikAcsDoorEventsEntity::getPersonName, query.getPersonName());
        wrapper.like(StringUtils.isNotBlank(query.getPersonId()), HikAcsDoorEventsEntity::getPersonId, query.getPersonId());
        wrapper.like(StringUtils.isNotBlank(query.getJobNo()), HikAcsDoorEventsEntity::getJobNo, query.getJobNo());
        wrapper.like(StringUtils.isNotBlank(query.getOrgIndexCode()), HikAcsDoorEventsEntity::getOrgIndexCode, query.getOrgIndexCode());
        wrapper.like(StringUtils.isNotBlank(query.getDoorIndexCode()), HikAcsDoorEventsEntity::getDoorIndexCode, query.getDoorIndexCode());
        wrapper.like(StringUtils.isNotBlank(query.getDevIndexCode()), HikAcsDoorEventsEntity::getDevIndexCode, query.getDevIndexCode());
        wrapper.eq(StringUtils.isNotBlank(query.getDoorRegionIndexCode()), HikAcsDoorEventsEntity::getDoorRegionIndexCode, query.getDoorRegionIndexCode());
        wrapper.eq(Objects.nonNull(query.getEventType()), HikAcsDoorEventsEntity::getEventType, query.getEventType());
        wrapper.eq(Objects.nonNull(query.getInAndOutType()), HikAcsDoorEventsEntity::getInAndOutType, query.getInAndOutType());
        wrapper.eq(Objects.nonNull(query.getIsExistPicUri()), HikAcsDoorEventsEntity::getIsExistPicUri, query.getIsExistPicUri());
        if (Objects.nonNull(query.getEventBeginTime()) && Objects.nonNull(query.getEventEndTime())) {
            wrapper.between(HikAcsDoorEventsEntity::getEventTime, query.getEventBeginTime(), query.getEventEndTime());
        }
        wrapper.orderByDesc(HikAcsDoorEventsEntity::getEventTime);

        String host = hikProperties.getHost();
        IPage<HikAcsDoorEventsPageVO> iPage = page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper).convert(item -> {
            HikAcsDoorEventsPageVO vo = new HikAcsDoorEventsPageVO();
            BeanUtils.copyProperties(item, vo);
            if (StringUtils.isNotBlank(vo.getPicture())) {
                vo.setPicture(ProtocolEnum.HTTPS.getGet() + host + vo.getPicture());
            }
            return vo;
        });

        List<HikAcsDoorEventsPageVO> voList;
        if (Objects.nonNull(iPage) && CollectionUtils.isNotEmpty(voList = iPage.getRecords())) {
            // 填充事件类型名称
            patchDataDict(voList);
            // 设置门禁点区域名称
            patchRegionName(voList, bo);
        }
        return iPage;
    }

    /**
     * 填充字典名称
     */
    private void patchDataDict(List<HikAcsDoorEventsPageVO> dataList) {
        if (CollectionUtils.isNotEmpty(dataList)) {
            List<HaikangDataDictEntity> deviceChannelTypeList = haikangDataDictService.getListByDictTypeCode(
                    HaikangDataDictConstant.ACS_DOOR_EVENT_TYPE);

            dataList.forEach(item -> {
                if (Objects.nonNull(item.getEventType())) {
                    item.setEventTypeName(
                            haikangDataDictService.getNameByCode(deviceChannelTypeList,
                                    item.getEventType() + ""));
                }
            });
        }
    }

    /**
     * 填充区域名称
     */
    private void patchRegionName(List<HikAcsDoorEventsPageVO> dataList, HIKEntranceGuardBO bo) {

        List<String> doorRegionIndexCodes = dataList.stream().map(HikAcsDoorEventsPageVO::getDoorRegionIndexCode)
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(doorRegionIndexCodes)) {
            HikRegionDetailReq req = new HikRegionDetailReq();
            req.setIndexCodes(doorRegionIndexCodes);
            HikRegionDetailResp resp = hikApiFeignService.queryRegionDetail(req, bo);

            if (Objects.nonNull(resp) && CollectionUtils.isNotEmpty(resp.getList())) {
                Map<String, String> map = resp.getList().stream()
                        .collect(Collectors.toMap(HikRegionDetailResp.RegionInfo::getIndexCode, HikRegionDetailResp.RegionInfo::getName));

                dataList.forEach(item -> item.setDoorRegionName(map.getOrDefault(item.getDoorRegionIndexCode(), null)));
            }
        }
    }

    /**
     * 批量保存事件数据
     *
     * @Param list 参数
     * @Param companyId 企业id
     */
    public void batchSaveEvents(List<HikDoorEventsResp.DataItem> list, Long companyId) {
        List<HikAcsDoorEventsEntity> collect = list.stream().map(item -> {
            HikAcsDoorEventsEntity entity = new HikAcsDoorEventsEntity();
            BeanUtils.copyProperties(item, entity);
            if (StringUtils.isNotBlank(item.getEventTime())) {
                entity.setEventTime(timeFormatConvert(item.getEventTime()));
            }
            if (StringUtils.isNotBlank(item.getReceiveTime())) {
                entity.setReceiveTime(timeFormatConvert(item.getReceiveTime()));
            }
            entity.setIsExistPicUri(StringUtils.isNotBlank(entity.getPicUri()) ? EntityConstants.YES : EntityConstants.NO);
            entity.setCompanyId(companyId);
            return entity;
        }).collect(Collectors.toList());

        baseMapper.batchReplaceInsert(collect);
    }

    /**
     * ISO8601转Date
     *
     * @return 日期
     * @Param timeStr 时间字符出
     */
    private static LocalDateTime timeFormatConvert(String isoDate) {
        // 使用 ISO 8601 格式的 DateTimeFormatter
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return LocalDateTime.parse(isoDate, formatter);
    }

    /**
     * 查询控制器列表
     *
     * @return List<HikIrdsResourcesByParamsVO>
     * @Param query 参数
     */
    public List<HikCommonResourcesVO> queryAcsDeviceList(HikIrdsResourcesByParamsQuery query) {
        // 企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        // 获取当前企业的对接配置
        HIKEntranceGuardBO bo = hikCommonHandle.queryHikConfigByPlatfromInfo(companyId);

        HikIrdsResourcesByParamsReq req = new HikIrdsResourcesByParamsReq();
        req.setPageNo(Objects.nonNull(req.getPageNo()) ? req.getPageNo() : 1L);
        req.setPageSize(Objects.nonNull(req.getPageSize()) ? req.getPageSize() : DEFAULT_PAGE_SIZE);
        req.setResourceType("acsDevice");
        if (StringUtils.isNotBlank(query.getName())) {
            req.setName(query.getName());
        }
        HikIrdsResourcesByParamsResp resp = hikApiFeignService.queryIrdsResourcesByParams(req, bo);

        List<HikCommonResourcesVO> list = Lists.newArrayList();
        if (Objects.nonNull(resp) && CollectionUtils.isNotEmpty(resp.getList())) {
            list = resp.getList().stream().map(item -> {
                HikCommonResourcesVO vo = new HikCommonResourcesVO();
                vo.setId(item.getIndexCode());
                vo.setName(item.getName());
                return vo;
            }).collect(Collectors.toList());
        }
        return list;
    }
}
