package cn.cuiot.dmp.externalapi.provider.controller.video;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.query.video.VideoPageQuery;
import cn.cuiot.dmp.externalapi.service.vo.video.VideoPageVo;
import cn.cuiot.dmp.externalapi.service.service.video.VideoPlayService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private VideoPlayService videoPlayService;

    /**
     * 分页
     *
     * @return IPage
     * @Param
     */
    @RequestMapping("/queryForPage")
    public IdmResDTO<IPage<VideoPageVo>> queryForPage(@RequestBody VideoPageQuery query) {
        return IdmResDTO.success(videoPlayService.queryForPage(query));
    }
}
