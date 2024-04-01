package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.system.application.service.INoticeService;
import cn.cuiot.dmp.system.infrastructure.entity.Notice;
import cn.cuiot.dmp.system.infrastructure.entity.NoticeUserAccess;
import cn.cuiot.dmp.system.infrastructure.entity.dto.NoticeDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.NoticeUserAccessDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 公告信息Controller
 * 
 * @author shixh75
 * @date 2022-08-05
 */
@RestController
@RequestMapping("/notice")
public class NoticeController extends BaseController
{
    @Autowired
    private INoticeService noticeService;

    /**
     * 查询公告信息列表
     */
    @PostMapping("/latestNotice")
    public List<Notice> list()
    {
        Notice notice = new Notice();
        notice.setOrgId(getOrgId());
        return noticeService.selectNoticeList(notice);
    }

    /**
     * 新增公告信息
     */
    @PostMapping(value = "/insertNotice")
    public int add(@RequestBody NoticeDto noticeDto)
    {
        String orgId = getOrgId();
        String userId = getUserId();
        noticeDto.setOrgId(orgId);
        noticeDto.setUserId(userId);
        return (noticeService.insertNotice(noticeDto));
    }

    /**
     * 取消公告信息
     */
    @PostMapping(value = "/cancelNotice")
    public int edit(@RequestBody NoticeDto noticeDto)
    {
        String userId = getUserId();
        noticeDto.setUserId(userId);
        return (noticeService.updateNotice(noticeDto));
    }

    /**
     * 页面弹窗信息查询
     */
    @PostMapping("/remindNotice")
    public List<Notice> remindNotice()
    {
        NoticeUserAccess noticeUserAccess = new NoticeUserAccess();
        String userId = getUserId();
        return noticeService.remindNotice(noticeUserAccess,userId);
    }

    /**
     * 公告用户表新增
     */
    @PostMapping(value = "/insertAccess")
    public int add(@RequestBody NoticeUserAccessDto noticeUserAccessDto)
    {
        String userId = getUserId();
        return (noticeService.insertNoticeUserAccess(noticeUserAccessDto,userId));
    }

}
