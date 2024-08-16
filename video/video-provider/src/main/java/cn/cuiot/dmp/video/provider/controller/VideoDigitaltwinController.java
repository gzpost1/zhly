package cn.cuiot.dmp.video.provider.controller;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.video.base.auth.ThirdRequestNeedAuth;
import cn.cuiot.dmp.video.service.entity.VideoPlayEntity;
import cn.cuiot.dmp.video.service.entity.query.VideoScreenQuery;
import cn.cuiot.dmp.video.service.entity.vo.VideoScreenVo;
import cn.cuiot.dmp.video.service.service.VideoAIAlarmService;
import cn.cuiot.dmp.video.service.service.VideoPlayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 监控-提供给数字孪生api接口
 *
 * @Author: zc
 * @Date: 2024-08-14
 */
@Slf4j
@RestController
@RequestMapping("/video/digitaltwin")
public class VideoDigitaltwinController {

    @Autowired
    private VideoPlayService videoPlayService;
    @Autowired
    private VideoAIAlarmService videoAIAlarmService;

    /**
     * 根据设备id查询
     *
     * @return IdmResDTO
     * @Param query 参数
     */
    @PostMapping("/queryByDeviceId")
    @ThirdRequestNeedAuth
    public IdmResDTO<VideoPlayEntity> queryByDeviceId(@RequestBody VideoScreenQuery query) {
        return IdmResDTO.success(videoPlayService.queryByDeviceId(query));
    }

    /**
     * 查询AI报警列表（近7天数据）
     *
     * @return IdmResDTO
     * @Param query 参数
     */
    @PostMapping("/queryVideoAIList")
    @ThirdRequestNeedAuth
    public IdmResDTO<List<VideoScreenVo>> queryVideoAIList(@RequestBody VideoScreenQuery query) {
        return IdmResDTO.success(videoAIAlarmService.queryVideoAIList(query));
    }
}
