package cn.cuiot.dmp.content.service.impl;//	模板


import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.content.constant.ContentConstants;
import cn.cuiot.dmp.content.conver.ImgTextConvert;
import cn.cuiot.dmp.content.dal.entity.ContentImgTextEntity;
import cn.cuiot.dmp.content.dal.entity.ContentNoticeEntity;
import cn.cuiot.dmp.content.dal.mapper.ContentImgTextMapper;
import cn.cuiot.dmp.content.param.dto.AuditResultDto;
import cn.cuiot.dmp.content.param.dto.ContentImgTextCreateDto;
import cn.cuiot.dmp.content.param.dto.ContentImgTextUpdateDto;
import cn.cuiot.dmp.content.param.query.ContentImgTextPageQuery;
import cn.cuiot.dmp.content.param.req.PublishReqVo;
import cn.cuiot.dmp.content.param.vo.ContentImgTextVo;
import cn.cuiot.dmp.content.service.ContentImgTextService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/28 14:42
 */
@Service("imgTextService")
public class ContentImgTextServiceImpl extends ServiceImpl<ContentImgTextMapper, ContentImgTextEntity> implements ContentImgTextService {


    @Override
    public ContentImgTextVo queryForDetail(Long id) {
        ContentImgTextEntity contentImgTextEntity = this.baseMapper.selectById(id);
        ContentImgTextVo contentImgTextVo = ImgTextConvert.INSTANCE.convert(contentImgTextEntity);
        return contentImgTextVo;
    }

    @Override
    public List<ContentImgTextVo> queryForList(ContentImgTextPageQuery pageQuery) {
        return null;
    }

    @Override
    public PageResult<ContentImgTextVo> queryForPage(ContentImgTextPageQuery pageQuery) {
        return null;
    }

    @Override
    public int saveContentImgText(ContentImgTextCreateDto createDTO) {
        return 0;
    }

    @Override
    public int updateContentImgText(ContentImgTextUpdateDto updateDTO) {
        return 0;
    }

    @Override
    public Boolean dealAuditResult(AuditResultDto auditResultDto) {
        ContentImgTextEntity imgTextEntity = this.baseMapper.selectById(auditResultDto.getId());
        if (imgTextEntity != null) {
            imgTextEntity.setAuditStatus(auditResultDto.getAuditStatus());
            this.baseMapper.updateById(imgTextEntity);
            return true;
        }
        return false;
    }
}
