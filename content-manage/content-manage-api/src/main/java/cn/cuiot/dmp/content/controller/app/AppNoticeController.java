package cn.cuiot.dmp.content.controller.app;//	模板

import cn.cuiot.dmp.base.application.annotation.ResolveExtData;
import cn.cuiot.dmp.content.param.query.NoticPageQuery;
import cn.cuiot.dmp.content.param.vo.NoticeVo;
import cn.cuiot.dmp.content.service.NoticeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * app-公告
 *
 * @author hantingyao
 * @Description
 * @data 2024/6/7 15:41
 */
@RestController
@RequestMapping("/app/notice")
@ResolveExtData
public class AppNoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 获取公告列表
     *
     * @return
     */
    @GetMapping("/getNoticeList")
    public IPage<NoticeVo> getAppNoticePage(@RequestBody @Valid NoticPageQuery pageQuery) {
        return noticeService.getAppNoticePage(pageQuery);
    }

}
