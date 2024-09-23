package cn.cuiot.dmp.externalapi.provider.task;

import cn.cuiot.dmp.base.infrastructure.dto.req.PlatfromInfoReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.common.bean.external.VsuapVideoBO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.FootPlateInfoEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.common.utils.Sm4;
import cn.cuiot.dmp.externalapi.service.entity.video.VideoAIMethodEntity;
import cn.cuiot.dmp.externalapi.service.entity.video.VideoChannelEntity;
import cn.cuiot.dmp.externalapi.service.entity.video.VideoDeviceEntity;
import cn.cuiot.dmp.externalapi.service.feign.SystemApiService;
import cn.cuiot.dmp.externalapi.service.service.video.*;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap.*;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap.*;
import cn.cuiot.dmp.externalapi.service.vendor.video.enums.VsuapChannelStateEnum;
import cn.cuiot.dmp.externalapi.service.vendor.video.enums.VsuapDeviceStateEnum;
import cn.cuiot.dmp.externalapi.service.vendor.video.enums.VsuapMethodTypeEnum;
import cn.cuiot.dmp.externalapi.service.vendor.video.vsuap.VsuapAIApiService;
import cn.cuiot.dmp.externalapi.service.vendor.video.vsuap.VsuapDeviceApiService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 监控定时任务
 *
 * @Author: zc
 * @Date: 2024-08-12
 */
@Slf4j
@Component
public class VideoTask {

    @Autowired
    private VsuapDeviceApiService vsuapDeviceApiService;
    @Autowired
    private VsuapAIApiService vsuapAIApiService;
    @Autowired
    private VideoDeviceService videoDeviceService;
    @Autowired
    private VideoChannelService videoChannelService;
    @Autowired
    private VideoPlayService videoPlayService;
    @Autowired
    private VideoAIMethodService videoAiMethodService;
    @Autowired
    private VideoAIStatisticsService videoAIStatisticsService;
    @Autowired
    private VideoAIAlarmService videoAIAlarmService;
    @Autowired
    private SystemApiService systemApiService;

    /*----------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------同步设备信息------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * 同步设备信息
     */
    @XxlJob("syncVideoDevice")
    public ReturnT<String> syncVideoDevice(String param) {
        log.info("开始同步设备信息......");

        AtomicLong pageNo = new AtomicLong(1);
        long pageSize = 200;
        long pages;
        do {

            PlatfromInfoReqDTO reqDTO = new PlatfromInfoReqDTO();
            reqDTO.setPageNo(pageNo.getAndAdd(1));
            reqDTO.setPageSize(pageSize);
            reqDTO.setPlatformId(FootPlateInfoEnum.VSUAP_VIDEO.getId());
            if (StringUtils.isBlank(param)) {
                reqDTO.setCompanyId(Long.parseLong(param));
            }
            IPage<PlatfromInfoRespDTO> iPage = systemApiService.queryPlatfromInfoPage(reqDTO);
            // 获取总页数
            pages = iPage.getTotal();
            // 记录
            List<PlatfromInfoRespDTO> records;
            if (CollectionUtils.isNotEmpty(records = iPage.getRecords())) {
                // 同步设备信息
                records.forEach(this::syncDevice);
            }
        } while (pageNo.get() < pages);

        log.info("结束同步设备信息......");
        return ReturnT.SUCCESS;
    }

    private void syncDevice(PlatfromInfoRespDTO platfromInfoRespDTO) {
        AtomicLong pageNo = new AtomicLong(1);
        long pageSize = 200;
        long totalSize = 0;
        long currentSize = 0;
        // 标记是否已经停用数据
        boolean isDisabled = false;
        do {
            try {
                // 密钥req
                VsuapSecretKeyReq secretKeyReq = jsonToSecretKeyReq(platfromInfoRespDTO.getData(), platfromInfoRespDTO.getCompanyId());

                VsuapDeviceListReq req = new VsuapDeviceListReq();
                req.setIndex(pageNo.getAndAdd(1) + "");
                req.setRows(pageSize + "");
                VsuapBaseResp<List<VsuapDeviceListResp>> resp = vsuapDeviceApiService.requestDeviceList(req, secretKeyReq);
                totalSize = resp.getTotal();
                currentSize += pageSize;

                // 停用数据
                if (!isDisabled) {
                    videoDeviceService.disableDevice(null, platfromInfoRespDTO.getCompanyId());
                    isDisabled = true;
                }

                List<VsuapDeviceListResp> data = resp.getData();
                if (CollectionUtils.isNotEmpty(data)) {
                    videoDeviceService.syncDevices(data, platfromInfoRespDTO);
                }
            } catch (Exception e) {
                log.error("企业【" + platfromInfoRespDTO.getCompanyId() + "】同步设备信息异常......");
                e.printStackTrace();
            }
        } while (currentSize < totalSize);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------同步通道信息------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * 同步通道信息
     */
    @XxlJob("syncVideoChannel")
    public ReturnT<String> syncVideoChannel(String param) {
        log.info("开始同步通道信息......");
        Long companyId = null;
        if (StringUtils.isBlank(param)) {
            companyId = Long.parseLong(param);
        }
        AtomicLong pageNo = new AtomicLong(1);
        long pageSize = 200;
        long pages;
        do {
            //获取在线的设备列表
            IPage<VideoDeviceEntity> iPage = videoDeviceService
                    .queryEnableDevicePage(new Page<>(pageNo.getAndAdd(1), pageSize), VsuapDeviceStateEnum.ON_LINE.getCode(), companyId);
            pages = iPage.getPages();

            List<VideoDeviceEntity> records;
            if (CollectionUtils.isNotEmpty(records = iPage.getRecords())) {
                records.forEach(this::syncDeviceChannels);
            }
        } while (pageNo.get() < pages);

        log.info("结束同步通道信息......");
        return ReturnT.SUCCESS;
    }

    private void syncDeviceChannels(VideoDeviceEntity device) {
        AtomicLong channelPageNo = new AtomicLong(1);
        long totalSize = 0;
        long pageSize = 200;
        long currentSize = 0;
        // 标记是否已经停用数据
        boolean isDisabled = false;
        do {
            try {
                // 请求密钥
                VsuapSecretKeyReq secretKeyReq = jsonToSecretKeyReq(Sm4.decrypt(device.getSecret()), device.getCompanyId());

                VsuapChannelReq req = new VsuapChannelReq();
                req.setIndex(channelPageNo.getAndAdd(1) + "");
                req.setRows(pageSize + "");
                req.setDeviceId(device.getDeviceId());
                VsuapBaseResp<List<VsuapChannelResp>> resp = vsuapDeviceApiService.requestChannel(req, secretKeyReq);
                totalSize = resp.getTotal();
                currentSize += pageSize;

                // 停用数据
                if (!isDisabled) {
                    videoChannelService.disableChannel(device.getDeviceId(), device.getCompanyId());
                    isDisabled = true;
                }

                List<VsuapChannelResp> data;
                if (CollectionUtils.isNotEmpty(data = resp.getData())) {
                    videoChannelService.syncChannel(data, device);
                }
            } catch (Exception e) {
                log.error("企业【" + device.getCompanyId() + "】同步通道信息异常.......");
                e.printStackTrace();
            }
        } while (currentSize < totalSize);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------同步播放信息------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * 同步播放信息
     */
    @XxlJob("syncVideoPlay")
    public ReturnT<String> syncVideoPlay(String param) {
        log.info("开始同步播放信息......");
        Long companyId = null;
        if (StringUtils.isBlank(param)) {
            companyId = Long.parseLong(param);
        }

        AtomicLong pageNo = new AtomicLong(1);
        long pageSize = 200;
        long pages = 0;
        do {
            //通道查询分页数据
            IPage<VideoChannelEntity> iPage = videoChannelService
                    .queryEnableChannelPage(new Page<>(pageNo.getAndAdd(1), pageSize), VsuapChannelStateEnum.ON_LINE.getCode(), companyId);
            pages = iPage.getPages();

            List<VideoChannelEntity> records;
            if (CollectionUtils.isNotEmpty(records = iPage.getRecords())) {
                //根据通道遍历请求
                records.forEach(item -> {
                    try {
                        // 请求密钥信息
                        VsuapSecretKeyReq secretKeyReq = jsonToSecretKeyReq(Sm4.decrypt(item.getSecret()), item.getCompanyId());

                        VsuapPlayOnReq req = new VsuapPlayOnReq();
                        req.setDeviceId(item.getDeviceId());
                        req.setChannelCodeId(item.getChannelCodeId());
                        VsuapPlayOnFlvHlsResp resp = vsuapDeviceApiService.requestPlayOnFlvHls(req, secretKeyReq);

                        if (Objects.nonNull(resp)) {
                            videoPlayService.syncPlay(resp, item.getCompanyId());
                        }
                    } catch (Exception e) {
                        log.error("企业【" + item.getCompanyId() + "】同步播放信息异常.....");
                        e.printStackTrace();
                    }
                });
            }

        } while (pageNo.get() < pages);

        log.info("结束同步播放信息......");
        return ReturnT.SUCCESS;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------同步AI算法信息----------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * 同步AI算法列表
     */
    @XxlJob("syncVideoAIMethod")
    public ReturnT<String> syncVideoAIMethod(String param) {
        log.info("开始同步AI算法列表......");

        AtomicLong pageNo = new AtomicLong(1);
        long pageSize = 200;
        long pages;
        do {
            PlatfromInfoReqDTO reqDTO = new PlatfromInfoReqDTO();
            reqDTO.setPageNo(pageNo.getAndAdd(1));
            reqDTO.setPageSize(pageSize);
            reqDTO.setPlatformId(FootPlateInfoEnum.VSUAP_VIDEO.getId());
            IPage<PlatfromInfoRespDTO> iPage = systemApiService.queryPlatfromInfoPage(reqDTO);
            // 获取总页数
            pages = iPage.getTotal();
            // 记录
            List<PlatfromInfoRespDTO> records;
            if (CollectionUtils.isNotEmpty(records = iPage.getRecords())) {
                // 同步设备信息
                records.forEach(this::syncAIMethod);
            }

        } while (pageNo.get() < pages);

        log.info("结束同步AI算法列表......");
        return ReturnT.SUCCESS;
    }

    private void syncAIMethod(PlatfromInfoRespDTO platfromInfoRespDTO) {
        AtomicLong pageNo = new AtomicLong(1);
        long pageSize = 200;
        long totalSize = 0;
        long currentSize = 0;
        // 标记是否已经停用设备
        boolean isDisabled = false;
        do {
            try {
                // 请求密钥信息
                VsuapSecretKeyReq secretKeyReq = jsonToSecretKeyReq(platfromInfoRespDTO.getData(), platfromInfoRespDTO.getCompanyId());

                VsuapAIMethodListReq req = new VsuapAIMethodListReq();
                req.setIndex(pageNo.getAndAdd(1) + "");
                req.setRows(pageSize + "");
                VsuapBaseResp<List<VsuapAIMethodListResp>> resp = vsuapAIApiService.requestAIMethodList(req, secretKeyReq);
                totalSize = resp.getTotal();
                currentSize += pageSize;

                // 停用设备
                if (!isDisabled) {
                    videoAiMethodService.disableAIMethod(null, platfromInfoRespDTO.getCompanyId());
                    isDisabled = true;
                }

                List<VsuapAIMethodListResp> data;
                if (CollectionUtils.isNotEmpty(data = resp.getData())) {
                    videoAiMethodService.syncAIMethods(data, platfromInfoRespDTO);
                }
            } catch (Exception e) {
                log.error("企业【" + platfromInfoRespDTO.getCompanyId() + "】同步AI算法列表异常........");
                e.printStackTrace();
            }
        } while (currentSize < totalSize);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------同步AI警告信息----------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * 同步AI警告列表
     */
    @XxlJob("syncVideoAIAlarm")
    public ReturnT<String> syncVideoAIAlarm(String param) {
        log.info("开始同步AI警告列表......");
        Pair<Long, Long> pair = dateTransformation(param);

        AtomicLong pageNo = new AtomicLong(1);
        long pageSize = 200;
        long pages = 0;
        do {
            try {
                IPage<VideoAIMethodEntity> page = videoAiMethodService
                        .queryEnableAIMethodPage(new Page<>(pageNo.getAndAdd(1), pageSize), VsuapMethodTypeEnum.ALARM_CATEGORY.getCode());
                pages = page.getPages();

                List<VideoAIMethodEntity> records;
                if (CollectionUtils.isNotEmpty(records = page.getRecords())) {
                    records.forEach(e -> syncAIAlarm(e, pair));
                }
            } catch (Exception e) {
                log.info("同步AI统计列表异常........");
                e.printStackTrace();
            }
        } while (pageNo.get() < pages);

        log.info("结束同步AI警告列表......");
        return ReturnT.SUCCESS;
    }

    private void syncAIAlarm(VideoAIMethodEntity aiMethod, Pair<Long, Long> pair) {
        AtomicLong pageNo = new AtomicLong(1);
        long pageSize = 200;
        long totalSize = 0;
        long currentSize = 0;
        do {
            try {
                //请求密钥信息
                VsuapSecretKeyReq secretKeyReq = jsonToSecretKeyReq(Sm4.decrypt(aiMethod.getSecret()), aiMethod.getCompanyId());

                VsuapAIAlarmListReq req = new VsuapAIAlarmListReq();
                req.setMethodId(aiMethod.getMethodId());
                req.setIndex(pageNo.getAndAdd(1) + "");
                req.setRows(pageSize + "");
                req.setStartTime(pair.getLeft() + "");
                req.setEndTime(pair.getRight() + "");
                VsuapBaseResp<List<VsuapAIAlarmListResp>> resp = vsuapAIApiService.requestAIAlarmLis(req, secretKeyReq);

                totalSize = resp.getTotal();
                currentSize += pageSize;

                List<VsuapAIAlarmListResp> data;
                if (CollectionUtils.isNotEmpty(data = resp.getData())) {
                    videoAIAlarmService.syncAIAlarm(data, aiMethod.getCompanyId());
                }
            } catch (Exception e) {
                log.error("企业【" + aiMethod.getCompanyId() + "】同步监控AI警告列表异常.......");
                e.printStackTrace();
            }
        } while (currentSize < totalSize);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------同步AI统计信息----------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * 同步AI统计列表
     */
    @XxlJob("syncVideoAIStatistics")
    public ReturnT<String> syncVideoAIStatistics(String param) {
        log.info("开始同步AI统计列表........");
        Pair<Long, Long> pair = dateTransformation(param);

        AtomicLong pageNo = new AtomicLong(1);
        long pageSize = 200;
        long pages = 0;
        do {
            IPage<VideoAIMethodEntity> page = videoAiMethodService
                    .queryEnableAIMethodPage(new Page<>(pageNo.getAndAdd(1), pageSize), VsuapMethodTypeEnum.STATISTICAL_CATEGORY.getCode());
            pages = page.getPages();

            List<VideoAIMethodEntity> records;
            if (CollectionUtils.isNotEmpty(records = page.getRecords())) {
                records.forEach(item -> {
                    try {
                        // 请求密钥信息
                        VsuapSecretKeyReq secretKeyReq = jsonToSecretKeyReq(Sm4.decrypt(item.getSecret()), item.getCompanyId());

                        VsuapAIStatisticsReq req = new VsuapAIStatisticsReq();
                        req.setMethodId(item.getMethodId());
                        req.setQueryType(1);
                        req.setStartTime(pair.getLeft() + "");
                        req.setEndTime(pair.getRight() + "");
                        List<VsuapAIStatisticsResp> resp = vsuapAIApiService.requestAIStatistics(req, secretKeyReq);

                        if (CollectionUtils.isNotEmpty(resp)) {
                            videoAIStatisticsService.syncAIStatistics(resp, item.getMethodId(), item.getCompanyId());
                        }
                    } catch (Exception e) {
                        log.info("企业【" + item.getCompanyId() + "】同步AI统计列表异常........");
                        e.printStackTrace();
                    }
                });
            }
        } while (pageNo.get() < pages);

        log.info("结束同步AI统计列表........");
        return ReturnT.SUCCESS;
    }

    /**
     * json转密钥
     *
     * @return VsuapVideoBO
     * @Param json json数据
     */
    private VsuapSecretKeyReq jsonToSecretKeyReq(String json, Long companyId) {
        VsuapVideoBO bo = FootPlateInfoEnum.getObjectFromJsonById(FootPlateInfoEnum.VSUAP_VIDEO.getId(), json);

        if (StringUtils.isBlank(bo.getAccessKeyId())) {
            throw new BusinessException(ResultCode.ERROR, "企业【" + companyId + "】请求云智眼失败，AccessKey为空");
        }
        if (StringUtils.isBlank(bo.getAccessKeySecret())) {
            throw new BusinessException(ResultCode.ERROR, "企业【" + companyId + "】请求云智眼失败，SecretKey为空");
        }

        VsuapSecretKeyReq secretKeyReq = new VsuapSecretKeyReq();
        secretKeyReq.setAccessKey(bo.getAccessKeyId());
        secretKeyReq.setSecretKey(bo.getAccessKeySecret());
        secretKeyReq.setCompanyId(companyId);

        return secretKeyReq;
    }

    /**
     * 日期转换
     *
     * @return Pair<Long, Long> 开始、结束日期
     * @Param param
     */
    private Pair<Long, Long> dateTransformation(String param) {
        LocalDateTime beginTime;
        LocalDateTime endTime;
        if (StringUtils.isNotBlank(param)) {
            try {
                String[] split = param.split(",");
                beginTime = DateTimeUtil.stringToLocalDate(split[0]).atTime(LocalTime.MIN);
                endTime = DateTimeUtil.stringToLocalDate(split[1]).atTime(LocalTime.MAX);
            } catch (Exception e) {
                throw new BusinessException(ResultCode.ERROR, "参数错误，应为【yyyy-MM-dd,yyyy-MM-dd】");
            }
        } else {
            LocalDate now = LocalDate.now();
            beginTime = now.atTime(LocalTime.MIN);
            endTime = now.atTime(LocalTime.MAX);
        }
        AssertUtil.isFalse(beginTime.isAfter(endTime), "结束时间必须大于开始时间");
        AssertUtil.isFalse(ChronoUnit.DAYS.between(beginTime.toLocalDate(), endTime.toLocalDate()) > 7, "开始时间与结束时间最大间隔7天");

        //LocalDateTime转毫秒
        long beginEpochMilli = beginTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endEpochMilli = endTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        return Pair.of(beginEpochMilli, endEpochMilli);
    }
}
