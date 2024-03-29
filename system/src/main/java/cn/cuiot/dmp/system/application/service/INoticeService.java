package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.system.infrastructure.entity.Notice;
import cn.cuiot.dmp.system.infrastructure.entity.NoticeUserAccess;
import cn.cuiot.dmp.system.infrastructure.entity.dto.NoticeDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.NoticeUserAccessDto;
import java.util.List;

/**
 * 公告信息Service接口
 * 
 * @author zhangxp207
 * @date 2022-08-05
 */
public interface INoticeService 
{
    /**
     * 查询公告信息列表
     * 
     * @param notice 公告信息
     * @return 公告信息集合
     */
    List<Notice> selectNoticeList(Notice notice);

    /**
     * 新增公告信息
     * 
     * @param noticeDto 公告信息
     * @return 结果
     */
    int insertNotice(NoticeDto noticeDto);

    /**
     * 修改公告信息
     * 
     * @param noticeDto 公告信息
     * @return 结果
     */
    int updateNotice(NoticeDto noticeDto);

    /**
     * 用户-公告中间表新增
     * @param noticeUserAccessDto
     * @param userId
     * @return
     */
    int insertNoticeUserAccess(NoticeUserAccessDto noticeUserAccessDto, String userId);

    /**
     * 页面弹窗信息查询
     * @param noticeUserAccess
     * @param userId
     * @return
     */
    List<Notice> remindNotice(NoticeUserAccess noticeUserAccess,String userId);
}
