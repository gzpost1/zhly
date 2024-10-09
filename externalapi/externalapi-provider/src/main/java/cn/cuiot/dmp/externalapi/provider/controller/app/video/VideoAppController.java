package cn.cuiot.dmp.externalapi.provider.controller.app.video;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.query.video.VideoPageQuery;
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
 * app监控
 *
 * @Author: zc
 * @Date: 2024-09-05
 */
@RestController
@RequestMapping("/app/video")
public class VideoAppController {

    @Autowired
    private VideoDeviceService videoDeviceService;

    /**
     * 分页查询
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<VideoPageVo>> queryForPage(@RequestBody @Valid VideoPageQuery query) {
        return IdmResDTO.success(videoDeviceService.queryForPage(query));
    }
}
