package cn.cuiot.dmp.content.service.impl;//	模板

import cn.cuiot.dmp.content.dal.entity.ImgTextType;
import cn.cuiot.dmp.content.dal.mapper.ImgTextTypeMapper;
import cn.cuiot.dmp.content.service.ImgTextTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/3 10:29
 */
@Service
public class ImgTextTypeServiceImpl extends ServiceImpl<ImgTextTypeMapper, ImgTextType> implements ImgTextTypeService {
    @Override
    public List<ImgTextType> queryForList(String orgId) {
        LambdaQueryWrapper<ImgTextType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ImgTextType::getCompanyId, orgId);
        return list(queryWrapper);
    }
}
