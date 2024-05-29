package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.system.infrastructure.entity.SysDictType;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SysDictTypeQuery;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.SysDictTypeMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据字典类型 SysDictTypeService
 * Created by wuyongchong on 2019/8/26.
 */
@Service
public class SysDictTypeService {

    @Autowired
    private SysDictTypeMapper sysDictTypeMapper;

    public boolean codeExists(String dictCode, Long dictId) {
        if (null == dictId) {
            Long count = sysDictTypeMapper
                    .selectCount(new QueryWrapper<SysDictType>().eq("dict_code", dictCode));
            return count >= 1 ? true : false;
        } else {
            Long count = sysDictTypeMapper
                    .selectCount(
                            new QueryWrapper<SysDictType>().eq("dict_code", dictCode)
                                    .ne("dict_id", dictId));
            return count >= 1 ? true : false;
        }
    }

    public List<SysDictType> list(SysDictTypeQuery sysDictTypeQuery) {
        LambdaQueryWrapper<SysDictType> queryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(sysDictTypeQuery.getKeyword())) {
            queryWrapper.like(SysDictType::getDictName,sysDictTypeQuery.getKeyword());
        }
        if (StringUtils.isNotBlank(sysDictTypeQuery.getDictName())) {
            queryWrapper.like(SysDictType::getDictName,sysDictTypeQuery.getDictName());
        }
        if (StringUtils.isNotBlank(sysDictTypeQuery.getDictCode())) {
            queryWrapper.like(SysDictType::getDictCode,sysDictTypeQuery.getDictCode());
        }
        queryWrapper.orderByAsc(SysDictType::getSort);
        List<SysDictType> list = sysDictTypeMapper.selectList(queryWrapper);
        return list;
    }

    /**
     * 分页查询
     * @param sysDictTypeQuery
     * @return
     */
    public IPage<SysDictType> pageList(SysDictTypeQuery sysDictTypeQuery) {
        LambdaQueryWrapper<SysDictType> queryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(sysDictTypeQuery.getKeyword())) {
            queryWrapper.like(SysDictType::getDictName,sysDictTypeQuery.getKeyword());
        }
        if (StringUtils.isNotBlank(sysDictTypeQuery.getDictName())) {
            queryWrapper.like(SysDictType::getDictName,sysDictTypeQuery.getDictName());
        }
        if (StringUtils.isNotBlank(sysDictTypeQuery.getDictCode())) {
            queryWrapper.like(SysDictType::getDictCode,sysDictTypeQuery.getDictCode());
        }
        queryWrapper.orderByDesc(SysDictType::getCreateTime);
        IPage<SysDictType> page = sysDictTypeMapper.selectPage(new Page<SysDictType>(sysDictTypeQuery.getPageNo(),sysDictTypeQuery.getPageSize()),queryWrapper);
        return page;
    }

    public SysDictType getById(Long id) {
        return sysDictTypeMapper.selectById(id);
    }

    public void save(SysDictType sysDictType) {
        AssertUtil
                .isFalse(codeExists(sysDictType.getDictCode(), null),
                        "字典编码已经存在");
        sysDictTypeMapper.insert(sysDictType);
    }

    public void updateById(SysDictType sysDictType) {
        AssertUtil.isFalse(codeExists(sysDictType.getDictCode(), sysDictType.getDictId()),
                "字典编码已经存在");
        sysDictTypeMapper.updateById(sysDictType);
    }

    public void removeById(Long id) {
        sysDictTypeMapper.deleteById(id);
    }
}