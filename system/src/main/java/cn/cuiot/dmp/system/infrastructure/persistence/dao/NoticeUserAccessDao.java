package cn.cuiot.dmp.system.infrastructure.persistence.dao;

import cn.cuiot.dmp.system.infrastructure.entity.NoticeUserAccess;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * 公告用户中间Mapper接口
 * 
 * @author zhangxp207
 * @date 2022-08-05
 */
@Mapper
public interface NoticeUserAccessDao
{
    /**
     * 查询公告用户中间列表
     * 
     * @param noticeUserAccess 公告用户中间
     * @return 公告用户中间集合
     */
    List<NoticeUserAccess> selectNoticeUserAccessList(NoticeUserAccess noticeUserAccess);

    /**
     * 新增公告用户中间
     * 
     * @param noticeUserAccess 公告用户中间
     * @return 结果
     */
    int insertNoticeUserAccess(NoticeUserAccess noticeUserAccess);

}
