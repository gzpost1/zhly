package cn.cuiot.dmp.externalapi.provider.controller.admin;

import cn.cuiot.dmp.base.application.annotation.InternalApi;
import cn.cuiot.dmp.base.infrastructure.dto.req.PlatfromInfoReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.externalapi.service.query.EntranceGuardRecordReqDTO;
import cn.cuiot.dmp.externalapi.service.query.StatisInfoReqDTO;
import cn.cuiot.dmp.externalapi.service.query.video.VideoStatisInfoReqDTO;
import cn.cuiot.dmp.externalapi.service.service.gw.GwEntranceGuardService;
import cn.cuiot.dmp.externalapi.service.service.park.AccessControlService;
import cn.cuiot.dmp.externalapi.service.service.park.PlatfromInfoService;
import cn.cuiot.dmp.externalapi.service.service.video.VideoDeviceService;
import cn.cuiot.dmp.externalapi.service.service.water.WaterManagementService;
import cn.cuiot.dmp.externalapi.service.vo.EntranceGuardRecordVo;
import cn.cuiot.dmp.externalapi.service.vo.IOTStatisticVo;
import cn.cuiot.dmp.externalapi.service.vo.video.VideoPageVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * 供其他服务调用接口
 *
 * @Author: zc
 * @Date: 2024-09-26
 */
@InternalApi
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private PlatfromInfoService platfromInfoService;

    @Autowired
    private VideoDeviceService videoDeviceService;

    @Autowired
    private GwEntranceGuardService gwEntranceGuardService;

    @Autowired
    private WaterManagementService waterManagementService;

    @Autowired
    private AccessControlService accessControlService;
    
    /**
     * 获取平台信息分页
     *
     * @return Page
     * @Param 企业id
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<Page<PlatfromInfoRespDTO>> queryForPage(@RequestBody PlatfromInfoReqDTO dto) {
        return IdmResDTO.success(platfromInfoService.queryForPage(dto));
    }

    /**
     * 获取平台信息列表
     *
     * @return List
     * @Param 企业id
     */
    @PostMapping("/queryForList")
    public IdmResDTO<List<PlatfromInfoRespDTO>> queryForList(@RequestBody PlatfromInfoReqDTO dto) {
        return IdmResDTO.success(platfromInfoService.queryForList(dto));
    }


    /**
     * 查看大屏 视频分页数据
     * @param query
     * @return
     */
    @RequestMapping("/video/queryForPage")
    public IdmResDTO<IPage<VideoPageVo>> queryForVideoPage(@RequestBody VideoStatisInfoReqDTO query) {
        return IdmResDTO.success(videoDeviceService.queryForVideoPage(query));
    }


    /**
     * 查询IOT统计信息
     * @return IOTStatisticVo
     */
    @RequestMapping("/queryIotStatistic")
    public IdmResDTO<IOTStatisticVo> queryIotStatistic(@RequestBody StatisInfoReqDTO reqDTO) {

        // 德科水表
        Long dekeWaterCount = waterManagementService.queryWaterMeterCount(reqDTO);
        // 格物门禁
        Long gwEntranceGuardCount = gwEntranceGuardService.queryEntranceGuardCount(reqDTO);
        // 宇泛门禁
        Long yfAccessCount = accessControlService.queryAccessCommunityCount(reqDTO);

         // 监控 数量
        Long videoCount  = videoDeviceService.queryVideoCount(reqDTO);

        IOTStatisticVo iotStatisticVo = new IOTStatisticVo();
        iotStatisticVo.setWaterMeter(Optional.ofNullable(dekeWaterCount).orElse(0L));
        iotStatisticVo.setVideoMeter(Optional.ofNullable(videoCount).orElse(0L));
        iotStatisticVo.setEntranceGuard(Optional.ofNullable(gwEntranceGuardCount).orElse(0L)
        + Optional.ofNullable(yfAccessCount).orElse(0L));


        return IdmResDTO.success(iotStatisticVo);
    }


    /**
     *  门禁 出行记录
     * @param query
     * @return
     */
    @PostMapping("/entranceGuard/queryForPage")
    public IdmResDTO<IPage<EntranceGuardRecordVo>> entranceGuardQueryForPage(@RequestBody EntranceGuardRecordReqDTO query) {
        return IdmResDTO.success(gwEntranceGuardService.entranceGuardQueryForPage(query));
    }


}
