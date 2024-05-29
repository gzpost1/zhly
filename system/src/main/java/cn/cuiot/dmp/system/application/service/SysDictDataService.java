package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.DictDataVO;
import cn.cuiot.dmp.base.infrastructure.dto.DictTypeVO;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.system.infrastructure.entity.SysDictData;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SysDictDataQuery;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.SysDictDataMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据字典 SysDictDataService Created by wuyongchong on 2019/8/26.
 */
@Service
public class SysDictDataService {

    @Autowired
    private SysDictDataMapper sysDictDataMapper;

    public List<DictTypeVO> getDictDataList() {
        List<DictTypeVO> list = sysDictDataMapper.getDictDataList(null);
        return Optional.ofNullable(list).orElse(Lists.newArrayList()).stream()
                .collect(
                        Collectors.toList());
    }

    public List<DictDataVO> getDataListByCode(String dictCode) {
        List<DictTypeVO> list = sysDictDataMapper.getDictDataList(dictCode);
        if (null != list && list.size() > 0) {
            return Optional.ofNullable(list.get(0).getItems()).orElse(Lists.newArrayList()).stream()
                    .collect(
                            Collectors.toList());
        }
        return Lists.newArrayList();
    }

    /**
     * 列表查询
     * @param sysDictDataQuery
     * @return
     */
    public List<SysDictData> list(SysDictDataQuery sysDictDataQuery) {
        LambdaQueryWrapper<SysDictData> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(SysDictData::getDictId, Long.valueOf(sysDictDataQuery.getDictId()));
        if (StringUtils.isNotBlank(sysDictDataQuery.getDataName())) {
            lambdaQueryWrapper.like(SysDictData::getDataName, sysDictDataQuery.getDataName().trim());
        }
        lambdaQueryWrapper.orderByAsc(SysDictData::getSort);
        lambdaQueryWrapper.orderByDesc(SysDictData::getCreateTime);
        List<SysDictData> list = sysDictDataMapper.selectList(lambdaQueryWrapper);
        return list;
    }

    /**
     * 分页查询
     * @param sysDictDataQuery
     * @return
     */
    public IPage<SysDictData> pageList(SysDictDataQuery sysDictDataQuery) {
        LambdaQueryWrapper<SysDictData> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(SysDictData::getDictId, Long.valueOf(sysDictDataQuery.getDictId()));
        if (StringUtils.isNotBlank(sysDictDataQuery.getDataName())) {
            lambdaQueryWrapper.like(SysDictData::getDataName, sysDictDataQuery.getDataName().trim());
        }
        lambdaQueryWrapper.orderByAsc(SysDictData::getSort);
        lambdaQueryWrapper.orderByDesc(SysDictData::getCreateTime);
        IPage<SysDictData> page = sysDictDataMapper.selectPage(new Page<>(sysDictDataQuery.getPageNo(),sysDictDataQuery.getPageSize()),lambdaQueryWrapper);
        return page;
    }

    public boolean valueExists(Long dictId, String dataValue, Long dataId) {
        if (null == dataId) {
            Long count = sysDictDataMapper
                    .selectCount(
                            new QueryWrapper<SysDictData>().eq("dict_id", dictId)
                                    .eq("data_value", dataValue));
            return count >= 1 ? true : false;
        } else {
            Long count = sysDictDataMapper
                    .selectCount(
                            new QueryWrapper<SysDictData>().eq("dict_id", dictId)
                                    .eq("data_value", dataValue)
                                    .ne("data_id", dataId));
            return count >= 1 ? true : false;
        }
    }


    public void save(SysDictData entity) {
        sysDictDataMapper.insert(entity);
    }

    public void updateById(SysDictData entity) {
        sysDictDataMapper.updateById(entity);
    }

    public SysDictData getById(Long id) {
        return sysDictDataMapper.selectById(id);
    }

    public void removeById(Long id) {

    }
}
