package cn.cuiot.dmp.externalapi.provider.task;

import cn.cuiot.dmp.common.bean.external.GWEntranceGuardBO;
import cn.cuiot.dmp.externalapi.service.constant.GwBusinessTypeConstant;
import cn.cuiot.dmp.externalapi.service.constant.GwEntranceGuardServiceKeyConstant;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardEntity;
import cn.cuiot.dmp.externalapi.service.service.gw.GwEntranceGuardConfigService;
import cn.cuiot.dmp.externalapi.service.service.gw.GwEntranceGuardService;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req.DmpDeviceReq;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req.InvokeDeviceServiceReq;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.DmpDeviceResp;
import cn.cuiot.dmp.externalapi.service.vendor.gw.dmp.DmpDeviceRemoteService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 格物门禁 定时任务
 *
 * @Author: zc
 * @Date: 2024-09-11
 */
@Slf4j
@Component
public class GwEntranceGuardTask {

    @Autowired
    private GwEntranceGuardService gwEntranceGuardService;
    @Autowired
    private GwEntranceGuardConfigService configService;
    @Autowired
    private DmpDeviceRemoteService dmpDeviceRemoteService;

    /**
     * 同步格物门禁参数
     */
    @XxlJob("syncGwEntranceGuardParams")
    public ReturnT<String> syncGwEntranceGuardParams(String param) {
        AtomicLong pageNo = new AtomicLong(1);
        long pageSize = 200;
        long pages;
        do {
            Page<GwEntranceGuardEntity> page = gwEntranceGuardService.page(new Page<>(pageNo.getAndAdd(1), pageSize));
            pages = page.getPages();

            List<GwEntranceGuardEntity> records;
            if (CollectionUtils.isNotEmpty(records = page.getRecords())) {
                //获取企业id列表
                List<Long> companyIds = records.stream().map(GwEntranceGuardEntity::getCompanyId)
                        .filter(Objects::nonNull).distinct().collect(Collectors.toList());

                if (CollectionUtils.isNotEmpty(companyIds)) {
                    List<GWEntranceGuardBO> configInfoList = configService.getConfigInfo(companyIds);
                    Map<Long, GWEntranceGuardBO> map = configInfoList.stream()
                            .collect(Collectors.toMap(GWEntranceGuardBO::getCompanyId, e -> e));

                    records.forEach(item -> {
                        try {
                            if (Objects.nonNull(item.getCompanyId()) && map.containsKey(item.getCompanyId())) {
                                String serviceKey = GwEntranceGuardServiceKeyConstant.GET_PARAM;

                                GWEntranceGuardBO bo = map.get(item.getCompanyId());
                                bo.setDeviceKey(item.getDeviceKey());
                                bo.setRequestId(GwBusinessTypeConstant.ENTRANCE_GUARD + "-" + serviceKey + "-" + item.getId() + "-" + System.currentTimeMillis());

                                InvokeDeviceServiceReq req = new InvokeDeviceServiceReq();
                                req.setIotId(item.getIotId());
                                req.setKey(serviceKey);
                                dmpDeviceRemoteService.invokeDeviceService(req, bo);
                            }
                        } catch (Exception e) {
                            log.error("同步格物门禁参数异常................");
                            e.printStackTrace();
                        }
                    });
                }
            }

        } while (pageNo.get() < pages);

        return ReturnT.SUCCESS;
    }

    /**
     * 同步门禁状态
     */
    @XxlJob("syncGwEntranceGuardStatus")
    public ReturnT<String> syncGwEntranceGuardStatus(String param) {
        AtomicLong pageNo = new AtomicLong(1);
        long pageSize = 200;
        long pages;
        do {
            Page<GwEntranceGuardEntity> page = gwEntranceGuardService.page(new Page<>(pageNo.getAndAdd(1), pageSize));
            pages = page.getPages();

            List<GwEntranceGuardEntity> records;
            if (CollectionUtils.isNotEmpty(records = page.getRecords())) {
                //获取企业id列表
                List<Long> companyIds = records.stream().map(GwEntranceGuardEntity::getCompanyId)
                        .filter(Objects::nonNull).distinct().collect(Collectors.toList());

                if (CollectionUtils.isNotEmpty(companyIds)) {
                    List<GWEntranceGuardBO> configInfoList = configService.getConfigInfo(companyIds);
                    Map<Long, GWEntranceGuardBO> map = configInfoList.stream()
                            .collect(Collectors.toMap(GWEntranceGuardBO::getCompanyId, e -> e));

                    records.forEach(item -> {
                        try {
                            if (Objects.nonNull(item.getCompanyId()) && map.containsKey(item.getCompanyId())) {
                                GWEntranceGuardBO bo = map.get(item.getCompanyId());

                                DmpDeviceReq req = new DmpDeviceReq();
                                req.setIotId(item.getIotId());
                                DmpDeviceResp resp = dmpDeviceRemoteService.getStatus(req, bo);

                                if (Objects.nonNull(resp)) {
                                    //修改设备状态
                                    item.setEquipStatus(resp.getStatus());
                                    gwEntranceGuardService.updateById(item);
                                }
                            }
                        } catch (Exception e) {
                            log.error("同步门禁状态..........");
                            e.printStackTrace();
                        }
                    });
                }
            }
        } while (pageNo.get() < pages);

        return ReturnT.SUCCESS;
    }
}
