package cn.cuiot.dmp.content.service;//	模板

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.content.dal.entity.ContentImgTextEntity;
import cn.cuiot.dmp.content.dal.entity.ContentNoticeEntity;
import cn.cuiot.dmp.content.param.dto.ContentImgTextCreateDto;
import cn.cuiot.dmp.content.param.dto.ContentImgTextUpdateDto;
import cn.cuiot.dmp.content.param.query.ContentImgTextPageQuery;
import cn.cuiot.dmp.content.param.req.PublishReqVo;
import cn.cuiot.dmp.content.param.vo.ContentImgTextVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/28 14:42
 */
public interface ContentImgTextService extends AuditResultDealService, IService<ContentImgTextEntity> {

    /**
     * 根据id获取详情
     * @param id
     * @return
     */
    ContentImgTextVo queryForDetail(Long id);

    /**
     * 列表
     * @param pageQuery
     * @return
     */
    List<ContentImgTextVo> queryForList(ContentImgTextPageQuery pageQuery);

    /**
     * 分页列表
     * @param pageQuery
     * @return
     */
    PageResult<ContentImgTextVo> queryForPage(ContentImgTextPageQuery pageQuery);

    /**
     * 保存
     * @param createDTO
     * @return
     */
    int saveContentImgText(ContentImgTextCreateDto createDTO);

    /**
     * 更新
     * @param updateDTO
     * @return
     */
    int updateContentImgText(ContentImgTextUpdateDto updateDTO);

}
