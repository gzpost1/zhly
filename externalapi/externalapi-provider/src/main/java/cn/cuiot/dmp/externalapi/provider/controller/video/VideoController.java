package cn.cuiot.dmp.externalapi.provider.controller.video;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.provider.task.VideoTask;
import cn.cuiot.dmp.externalapi.service.query.video.VideoBatchSetBuildingIdQuery;
import cn.cuiot.dmp.externalapi.service.query.video.VideoPageQuery;
import cn.cuiot.dmp.externalapi.service.query.video.VideoUpdateDTO;
import cn.cuiot.dmp.externalapi.service.service.video.VideoDeviceService;
import cn.cuiot.dmp.externalapi.service.vo.video.VideoPageVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 后台-监控 接口
 *
 * @Author: zc
 * @Date: 2024-08-22
 */
@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoDeviceService videoDeviceService;
    @Autowired
    private VideoTask videoTask;

    /**
     * 分页
     *
     * @return IPage
     * @Param
     */
    @RequestMapping("/queryForPage")
    public IdmResDTO<IPage<VideoPageVo>> queryForPage(@RequestBody VideoPageQuery query) {
        return IdmResDTO.success(videoDeviceService.queryForPage(query));
    }

    /**
     * 更新
     *
     * @return IPage
     * @Param
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateVideoDevice", operationName = "修改监控设备", serviceType = "videoDevice", serviceTypeName = "监控设备管理")
    @PostMapping("/update")
    public IdmResDTO<?> update(@RequestBody @Valid VideoUpdateDTO dto) {
        videoDeviceService.update(dto);
        return IdmResDTO.success();
    }

    /**
     * 批量设置楼盘
     */
    @RequiresPermissions
    @LogRecord(operationCode = "VideoDeviceBatchSetBuildingId", operationName = "监控设备批量设置楼盘", serviceType = "videoDevice", serviceTypeName = "监控设备管理")
    @PostMapping("/batchSetBuildingId")
    public IdmResDTO<?> batchSetBuildingId(@RequestBody @Valid VideoBatchSetBuildingIdQuery query) {
        videoDeviceService.batchSetBuildingId(query);
        return IdmResDTO.success();
    }

    /**
     * 同步监控数据
     */
    @PostMapping("/syncVideoPlay")
    public IdmResDTO<?> syncVideoPlay() {
        videoTask.syncVideoPlay(null);
        return IdmResDTO.success();
    }
}
