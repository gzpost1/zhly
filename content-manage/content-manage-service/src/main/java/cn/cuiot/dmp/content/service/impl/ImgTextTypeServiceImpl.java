package cn.cuiot.dmp.content.service.impl;//	模板

import cn.cuiot.dmp.content.dal.entity.ImgTextType;
import cn.cuiot.dmp.content.dal.mapper.ImgTextTypeMapper;
import cn.cuiot.dmp.content.service.ImgTextTypeService;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
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

    @Override
    public ImgTextType getByName(String name) {
        LambdaQueryWrapper<ImgTextType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ImgTextType::getName, name);
        queryWrapper.eq(ImgTextType::getCompanyId, LoginInfoHolder.getCurrentOrgId());
        queryWrapper.last("limit 1");
        return getOne(queryWrapper);
    }

    @Override
    public Boolean create(ImgTextType imgTextType) {
        checkCreateOrUpdate(imgTextType);
        return save(imgTextType);
    }

    @Override
    public Boolean update(ImgTextType imgTextType) {
        checkCreateOrUpdate(imgTextType);
        return null;
    }

    @Override
    public Boolean remove(Long id) {
        return null;
    }

    @Override
    public Long getAllCount() {
        LambdaQueryWrapper<ImgTextType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ImgTextType::getCompanyId, LoginInfoHolder.getCurrentOrgId());
        return count(queryWrapper);
    }

    public void checkCreateOrUpdate(ImgTextType imgTextType) {
        Long allCount = this.getAllCount();
        if (allCount > 20) {
            throw new RuntimeException("最多可添加20个类型");
        }
        ImgTextType byName = this.getByName(imgTextType.getName());
        if (byName == null) {
            return;
        }
        if (imgTextType.getId() == null) {
            throw new RuntimeException("类型名称已存在");
        }
        if (!byName.getId().equals(imgTextType.getId())) {
            throw new RuntimeException("类型名称已存在");
        }

    }
}
