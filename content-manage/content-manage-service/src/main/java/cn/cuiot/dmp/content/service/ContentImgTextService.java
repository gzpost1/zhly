package cn.cuiot.dmp.content.service;//	模板

import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.content.dal.entity.ContentImgTextEntity;
import cn.cuiot.dmp.content.param.dto.ContentImgTextCreateDto;
import cn.cuiot.dmp.content.param.dto.ContentImgTextUpdateDto;
import cn.cuiot.dmp.content.param.query.ContentImgTextPageQuery;
import cn.cuiot.dmp.content.param.vo.AuditStatusNumVo;
import cn.cuiot.dmp.content.param.vo.ImgTextVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
     *
     * @param id
     * @return
     */
    ImgTextVo queryForDetail(Long id);

    /**
     * 列表
     *
     * @param pageQuery
     * @return
     */
    List<ImgTextVo> queryForList(ContentImgTextPageQuery pageQuery);

    /**
     * 分页列表
     *
     * @param pageQuery
     * @return
     */
    IPage<ImgTextVo> queryForPage(ContentImgTextPageQuery pageQuery);

    /**
     * 保存
     *
     * @param createDTO
     * @return
     */
    int saveContentImgText(ContentImgTextCreateDto createDTO);

    /**
     * 更新
     *
     * @param updateDTO
     * @return
     */
    int updateContentImgText(ContentImgTextUpdateDto updateDTO);

    /**
     * 停启用
     *
     * @param updateStatusParam
     * @return
     */
    Boolean updateStatus(UpdateStatusParam updateStatusParam);

    /**
     * 根据类型id获取
     *
     * @param id
     * @return
     */
    List<ContentImgTextEntity> getByTypeId(Long id);

    /**
     * 获取审核状态数量
     *
     * @return
     */
    List<AuditStatusNumVo> getAuditStatusNum(Long typeId);

    void export(ContentImgTextPageQuery pageQuery);
}
