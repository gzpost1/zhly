package cn.cuiot.dmp.externalapi.provider.controller.admin.video;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
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
    @RequiresPermissions
    @RequestMapping("/queryForPage")
    public IdmResDTO<IPage<VideoPageVo>> queryForPage(@RequestBody VideoPageQuery query) {
        return IdmResDTO.success(videoDeviceService.queryForPage(query));
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateVideoDevice", operationName = "修改监控设备", serviceType = "videoDevice", serviceTypeName = "监控设备管理")
    @PostMapping("/update")
    public IdmResDTO<?> update(@RequestBody @Valid VideoUpdateDTO dto) {
        videoDeviceService.update(dto);
        return IdmResDTO.success();
    }

    /**
     * 删除
     *
     * @return IPage
     * @Param
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteVideoDevice", operationName = "删除监控设备", serviceType = "videoDevice", serviceTypeName = "监控设备管理")
    @PostMapping("/delete")
    public IdmResDTO<?> delete(@RequestBody @Valid IdsParam param) {
        videoDeviceService.delete(param.getIds());
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
    @RequiresPermissions
    @PostMapping("/syncVideoPlay")
    public IdmResDTO<?> syncVideoPlay() {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        //同步设备
        videoTask.syncVideoDevice(companyId + "");
        //同步渠道
        videoTask.syncVideoChannel(companyId + "");
        //同步播放信息
        videoTask.syncVideoPlay(companyId + "");

        return IdmResDTO.success();
    }
}
