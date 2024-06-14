package cn.cuiot.dmp.content.controller.app;//	模板

import cn.cuiot.dmp.base.application.annotation.ResolveExtData;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.content.param.query.NoticPageQuery;
import cn.cuiot.dmp.content.param.vo.NoticeVo;
import cn.cuiot.dmp.content.service.NoticeService;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.domain.types.enums.UserTypeEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    @PostMapping("/getNoticeList")
    public IPage<NoticeVo> getAppNoticePage(@RequestBody @Valid NoticPageQuery pageQuery) {
        return noticeService.getAppNoticePage(pageQuery);
    }


    /**
     * 获取公告详情
     *
     * @param idParam
     * @return
     */
    @PostMapping("/getNoticeDetail")
    public NoticeVo getNoticeDetail(@RequestBody @Valid IdParam idParam) {
        return noticeService.queryForAppDetail(idParam.getId());
    }

    /**
     * 获取本人可看的公告
     *
     * @return
     */
    @GetMapping("/getMyNotice")
    public void getMyNotice() {
        if (!UserTypeEnum.OWNER.getValue().equals(LoginInfoHolder.getCurrentUserType())) {
            return;
        }
        if (LoginInfoHolder.getCommunityId() == null) {
            return;
        }
        noticeService.getMyNotice(LoginInfoHolder.getCommunityId());
    }
}
