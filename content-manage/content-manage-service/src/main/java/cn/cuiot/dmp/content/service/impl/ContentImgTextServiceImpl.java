package cn.cuiot.dmp.content.service.impl;//	模板


import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.content.conver.ImgTextConver;
import cn.cuiot.dmp.content.dal.entity.ContentImgTextEntity;
import cn.cuiot.dmp.content.dal.mapper.ContentImgTextMapper;
import cn.cuiot.dmp.content.param.dto.ContentImgTextCreateDto;
import cn.cuiot.dmp.content.param.dto.ContentImgTextUpdateDto;
import cn.cuiot.dmp.content.param.query.ContentImgTextPageQuery;
import cn.cuiot.dmp.content.param.vo.ContentImgTextVo;
import cn.cuiot.dmp.content.service.ContentImgTextService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/28 14:42
 */
@Service
public class ContentImgTextServiceImpl extends ServiceImpl<ContentImgTextMapper, ContentImgTextEntity> implements ContentImgTextService {


    @Override
    public ContentImgTextVo queryForDetail(Long id) {
        ContentImgTextEntity contentImgTextEntity = this.baseMapper.selectById(id);
        ContentImgTextVo contentImgTextVo = ImgTextConver.INSTANCE.conver(contentImgTextEntity);
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
    public int deleteContentImgText(Long id) {
        return 0;
    }
}
