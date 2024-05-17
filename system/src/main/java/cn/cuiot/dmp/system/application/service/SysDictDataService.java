package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.DictDataVO;
import cn.cuiot.dmp.base.infrastructure.dto.DictTypeVO;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.system.infrastructure.entity.SysDictData;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SysDictDataQuery;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.SysDictDataMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

    public List<SysDictData> list(SysDictDataQuery sysDictDataQuery) {
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_id", Long.valueOf(sysDictDataQuery.getDictId()));
        if (StringUtils.isNotBlank(sysDictDataQuery.getDataName())) {
            queryWrapper.like("data_name", sysDictDataQuery.getDataName().trim());
        }
        queryWrapper.orderByAsc("sort");
        List<SysDictData> list = sysDictDataMapper.selectList(queryWrapper);
        return list;
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
