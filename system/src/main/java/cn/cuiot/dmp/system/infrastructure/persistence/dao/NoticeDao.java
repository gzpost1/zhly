package cn.cuiot.dmp.system.infrastructure.persistence.dao;

import cn.cuiot.dmp.system.infrastructure.entity.Notice;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
/**
 * 公告信息Mapper接口
 * 
 * @author zhangxp207
 * @date 2022-08-05
 */
@Mapper
public interface NoticeDao
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
     * @param notice 公告信息
     * @return 结果
     */
    int insertNotice(Notice notice);

    /**
     * 修改公告信息
     * 
     * @param notice 公告信息
     * @return 结果
     */
    int updateNotice(Notice notice);

}
