package cn.cuiot.dmp.content.service;//	模板


import cn.cuiot.dmp.content.dal.entity.ContentNoticeEntity;
import cn.cuiot.dmp.content.param.dto.NoticeCreateDto;
import cn.cuiot.dmp.content.param.dto.NoticeUpdateDto;
import cn.cuiot.dmp.content.param.query.NoticPageQuery;
import cn.cuiot.dmp.content.param.req.AuditReqVo;
import cn.cuiot.dmp.content.param.req.PublishReqVo;
import cn.cuiot.dmp.content.param.vo.NoticeVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/29 14:34
 */
public interface NoticeService extends IService<ContentNoticeEntity>, AuditResultDealService {
    /**
     * 查询公告详情
     *
     * @param id
     * @return
     */
    NoticeVo queryForDetail(Long id);

    /**
     * 保存公告
     *
     * @param createDTO
     * @return
     */
    int saveNotice(NoticeCreateDto createDTO);

    /**
     * 更新公告
     *
     * @param updateDTO
     * @return
     */
    int updateNotice(NoticeUpdateDto updateDTO);

    /**
     * 列表查询
     *
     * @param pageQuery
     * @return
     */
    List<NoticeVo> queryForList(NoticPageQuery pageQuery);

    /**
     * 分页查询
     *
     * @param pageQuery
     * @return
     */
    IPage<NoticeVo> queryForPage(NoticPageQuery pageQuery);

    /**
     * 发布
     * @param publishReqVo
     * @return
     */
    Boolean publish(PublishReqVo publishReqVo);
}
