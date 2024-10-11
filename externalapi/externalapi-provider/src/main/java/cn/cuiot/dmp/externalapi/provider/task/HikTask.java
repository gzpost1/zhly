package cn.cuiot.dmp.externalapi.provider.task;

import cn.cuiot.dmp.base.infrastructure.dto.req.PlatfromInfoReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.common.bean.external.HIKEntranceGuardBO;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.enums.FootPlateInfoEnum;
import cn.cuiot.dmp.externalapi.service.service.hik.HikAcsDoorEventsService;
import cn.cuiot.dmp.externalapi.service.service.hik.HikCommonHandle;
import cn.cuiot.dmp.externalapi.service.service.park.PlatfromInfoService;
import cn.cuiot.dmp.externalapi.service.vendor.hik.HikApiFeignService;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req.HikAcsEventPicturesReq;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req.HikDoorEventsReq;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikDoorEventsResp;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 海康定时任务
 *
 * @Author: zc
 * @Date: 2024-10-10
 */
@Slf4j
@Component
public class HikTask {

    @Autowired
    private HikApiFeignService hikApiFeignService;
    @Autowired
    private PlatfromInfoService platfromInfoService;
    @Autowired
    private HikAcsDoorEventsService hikAcsDoorEventsService;
    @Autowired
    private HikCommonHandle hikCommonHandle;

    /**
     * 同步海康事件
     */
    @XxlJob("syncHikEvents")
    public ReturnT<String> syncHikEvents(String param) {
        log.info("开始同步海康事件......");

        AtomicLong pageNo = new AtomicLong(1);
        long pageSize = 200;
        long pages;

        LocalDateTime now = LocalDateTime.now();
        do {
            PlatfromInfoReqDTO reqDTO = new PlatfromInfoReqDTO();
            reqDTO.setPageNo(pageNo.getAndAdd(1));
            reqDTO.setPageSize(pageSize);
            reqDTO.setPlatformId(FootPlateInfoEnum.HIK_ENTRANCE_GUARD.getId());
            if (StringUtils.isNotBlank(param)) {
                reqDTO.setCompanyId(Long.parseLong(param));
            }
            IPage<PlatfromInfoRespDTO> iPage = platfromInfoService.queryForPage(reqDTO);
            // 获取总页数
            pages = iPage.getTotal();
            // 记录
            List<PlatfromInfoRespDTO> records;
            if (CollectionUtils.isNotEmpty(records = iPage.getRecords())) {
                // 同步设备信息
                records.forEach(item -> eventsHandle(item, now));
            }
        } while (pageNo.get() < pages);

        log.info("结束同步海康事件......");
        return ReturnT.SUCCESS;
    }

    /**
     * 数据同步处理
     */
    private void eventsHandle(PlatfromInfoRespDTO dto, LocalDateTime now) {
        HIKEntranceGuardBO bo = hikCommonHandle.queryHikConfigByPlatfromInfo(dto.getCompanyId());

        if (Objects.nonNull(bo) && Objects.equals(bo.getStatus(), EntityConstants.YES) &&
                StringUtils.isNotBlank(bo.getAk()) && StringUtils.isNotBlank(bo.getSk())) {

            AtomicLong pageNo = new AtomicLong(1);
            long pageSize = 200;
            long pages = 0;
            do {
                HikDoorEventsReq req = new HikDoorEventsReq();
                req.setPageNo(pageNo.getAndAdd(1));
                req.setPageSize(pageSize);
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                req.setStartTime(now.toLocalDate().atStartOfDay().format(formatter));
                req.setEndTime(now.format(formatter));

                HikDoorEventsResp resp = hikApiFeignService.queryDoorEvents(req, bo);

                List<HikDoorEventsResp.DataItem> list;
                if (Objects.nonNull(resp) && CollectionUtils.isNotEmpty(list = resp.getList())) {
                    // 设置总页数
                    pages = resp.getTotalPage();

                    // 远程调用获取图片
                    list.forEach(item -> {
                        try {
                            HikAcsEventPicturesReq picturesReq = new HikAcsEventPicturesReq();
                            picturesReq.setPicUri(item.getPicUri());
                            picturesReq.setSvrIndexCode(item.getSvrIndexCode());
                            String pictures = hikApiFeignService.queryEventPictures(picturesReq, bo);
                            item.setPicture(pictures);
                        } catch (Exception e) {
                            log.error("事件获取图片异常......eventId:{}，picUri:{}，svrIndexCode:{}", item.getEventId(),
                                    item.getPicUri(), item.getSvrIndexCode());
                            e.printStackTrace();
                        }
                    });
                    // 批量保存事件
                    hikAcsDoorEventsService.batchSaveEvents(list, dto.getCompanyId());
                }
            } while (pageNo.get() < pages);
        }
    }
}
