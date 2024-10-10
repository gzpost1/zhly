package cn.cuiot.dmp.externalapi.service.service.hik;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangDataDictEntity;
import cn.cuiot.dmp.externalapi.service.mapper.hik.HaikangDataDictMapper;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangDataDictQuery;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 海康数据字典 服务类
 * </p>
 *
 * @author wuyongchong
 * @since 2024-10-10
 */
@Service
public class HaikangDataDictService extends
        ServiceImpl<HaikangDataDictMapper, HaikangDataDictEntity> {

    @Autowired
    private HaikangDataDictMapper haikangDataDictMapper;

    /**
     * 根据编码获取名称
     */
    public String getNameByCode(String dictTypeCode, String dataCode) {
        LambdaQueryWrapper<HaikangDataDictEntity> queryWrapper = Wrappers.<HaikangDataDictEntity>lambdaQuery()
                .eq(HaikangDataDictEntity::getDictTypeCode, dictTypeCode)
                .eq(HaikangDataDictEntity::getDataCode, dataCode);
        List<HaikangDataDictEntity> selectList = haikangDataDictMapper.selectList(
                queryWrapper);
        if (CollectionUtils.isNotEmpty(selectList)) {
            return selectList.get(0).getDataName();
        }
        return null;
    }

    /**
     * 根据编码获取名称
     */
    public String getNameByCode(List<HaikangDataDictEntity> list, String dataCode) {
        if (CollectionUtils.isNotEmpty(list)) {
            List<HaikangDataDictEntity> selectList = list.stream()
                    .filter(ite -> ite.getDataCode().equals(dataCode)).collect(
                            Collectors.toList());
            if (CollectionUtils.isNotEmpty(selectList)) {
                return selectList.get(0).getDataName();
            }
        }
        return null;
    }

    /**
     * 根据编码获取名称列表
     */
    public List<String> getNameListByCodes(String dictTypeCode, List<String> dataCodes) {
        LambdaQueryWrapper<HaikangDataDictEntity> queryWrapper = Wrappers.<HaikangDataDictEntity>lambdaQuery()
                .eq(HaikangDataDictEntity::getDictTypeCode, dictTypeCode)
                .in(HaikangDataDictEntity::getDataCode, dataCodes);
        List<HaikangDataDictEntity> selectList = haikangDataDictMapper.selectList(
                queryWrapper);
        if (CollectionUtils.isNotEmpty(selectList)) {
            return selectList.stream().map(ite -> ite.getDataName()).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    /**
     * 根据编码获取名称列表
     */
    public List<String> getNameListByCodes(List<HaikangDataDictEntity> list,
            List<String> dataCodes) {
        if (CollectionUtils.isNotEmpty(list) && CollectionUtils.isNotEmpty(dataCodes)) {
            List<HaikangDataDictEntity> selectList = list.stream()
                    .filter(ite -> dataCodes.contains(ite.getDataCode())).collect(
                            Collectors.toList());
            if (CollectionUtils.isNotEmpty(selectList)) {
                return selectList.stream().map(ite -> ite.getDataName())
                        .collect(Collectors.toList());
            }
        }
        return Lists.newArrayList();
    }

    /**
     * 根据字典型号/子类型/子分类获取名称
     */
    public String getNameByModel(String dictTypeCode, String dataModel) {
        LambdaQueryWrapper<HaikangDataDictEntity> queryWrapper = Wrappers.<HaikangDataDictEntity>lambdaQuery()
                .eq(HaikangDataDictEntity::getDictTypeCode, dictTypeCode)
                .eq(HaikangDataDictEntity::getDataModel, dataModel);
        List<HaikangDataDictEntity> selectList = haikangDataDictMapper.selectList(
                queryWrapper);
        if (CollectionUtils.isNotEmpty(selectList)) {
            return selectList.get(0).getDataName();
        }
        return null;
    }

    /**
     * 根据字典型号/子类型/子分类获取名称
     */
    public String getNameByModel(List<HaikangDataDictEntity> list, String dataModel) {
        if (CollectionUtils.isNotEmpty(list)) {
            List<HaikangDataDictEntity> selectList = list.stream()
                    .filter(ite -> ite.getDataModel().equals(dataModel)).collect(
                            Collectors.toList());
            if (CollectionUtils.isNotEmpty(selectList)) {
                return selectList.get(0).getDataName();
            }
        }
        return null;
    }

    /**
     * 根据字典型号/子类型/子分类获取名称列表
     */
    public List<String> getNameListByModels(String dictTypeCode, List<String> dataModels) {
        LambdaQueryWrapper<HaikangDataDictEntity> queryWrapper = Wrappers.<HaikangDataDictEntity>lambdaQuery()
                .eq(HaikangDataDictEntity::getDictTypeCode, dictTypeCode)
                .in(HaikangDataDictEntity::getDataModel, dataModels);
        List<HaikangDataDictEntity> selectList = haikangDataDictMapper.selectList(
                queryWrapper);
        if (CollectionUtils.isNotEmpty(selectList)) {
            return selectList.stream().map(ite -> ite.getDataName()).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    /**
     * 根据字典型号/子类型/子分类获取名称列表
     */
    public List<String> getNameListByModels(List<HaikangDataDictEntity> list,
            List<String> dataModels) {
        if (CollectionUtils.isNotEmpty(list) && CollectionUtils.isNotEmpty(dataModels)) {
            List<HaikangDataDictEntity> selectList = list.stream()
                    .filter(ite -> dataModels.contains(ite.getDataModel())).collect(
                            Collectors.toList());
            if (CollectionUtils.isNotEmpty(selectList)) {
                return selectList.stream().map(ite -> ite.getDataName())
                        .collect(Collectors.toList());
            }
        }
        return Lists.newArrayList();
    }

    /**
     * 根据编码和字典型号/子类型/子分类获取名称
     */
    public String getNameByModelAndCode(List<HaikangDataDictEntity> list, String dataModel,
            String dataCode) {
        if (CollectionUtils.isNotEmpty(list)) {
            List<HaikangDataDictEntity> selectList = list.stream()
                    .filter(ite -> ite.getDataModel().equals(dataModel) && ite.getDataCode()
                            .equals(dataCode)).collect(
                            Collectors.toList());
            if (CollectionUtils.isNotEmpty(selectList)) {
                return selectList.get(0).getDataName();
            }
        }
        return null;
    }

    /**
     * 根据编码和字典型号/子类型/子分类获取名称
     */
    public String getNameByModelAndCode(String dictTypeCode, String dataModel, String dataCode) {
        LambdaQueryWrapper<HaikangDataDictEntity> queryWrapper = Wrappers.<HaikangDataDictEntity>lambdaQuery()
                .eq(HaikangDataDictEntity::getDictTypeCode, dictTypeCode)
                .eq(HaikangDataDictEntity::getDataModel, dataModel)
                .eq(HaikangDataDictEntity::getDataCode, dataCode);
        List<HaikangDataDictEntity> selectList = haikangDataDictMapper.selectList(
                queryWrapper);
        if (CollectionUtils.isNotEmpty(selectList)) {
            return selectList.get(0).getDataName();
        }
        return null;
    }


    /**
     * 根据字典型号/子类型/子分类获取字典列表
     */
    public List<HaikangDataDictEntity> getListByModel(String dictTypeCode, String dataModel) {
        LambdaQueryWrapper<HaikangDataDictEntity> queryWrapper = Wrappers.<HaikangDataDictEntity>lambdaQuery()
                .eq(HaikangDataDictEntity::getDictTypeCode, dictTypeCode)
                .eq(HaikangDataDictEntity::getDataModel, dataModel)
                .eq(HaikangDataDictEntity::getStatus, EntityConstants.ENABLED);
        queryWrapper.orderByAsc(HaikangDataDictEntity::getSort);
        List<HaikangDataDictEntity> selectList = haikangDataDictMapper.selectList(
                queryWrapper);
        return Optional.ofNullable(selectList).orElse(Lists.newArrayList());
    }

    /**
     * 根据字典类型获取字典列表
     */
    public List<HaikangDataDictEntity> getListByDictTypeCode(String dictTypeCode) {
        LambdaQueryWrapper<HaikangDataDictEntity> queryWrapper = Wrappers.<HaikangDataDictEntity>lambdaQuery()
                .eq(HaikangDataDictEntity::getDictTypeCode, dictTypeCode)
                .eq(HaikangDataDictEntity::getStatus, EntityConstants.ENABLED);
        queryWrapper.orderByAsc(HaikangDataDictEntity::getSort);
        List<HaikangDataDictEntity> selectList = haikangDataDictMapper.selectList(
                queryWrapper);
        return Optional.ofNullable(selectList).orElse(Lists.newArrayList());
    }

    /**
     * 根据条件获取字典列表
     */
    public List<HaikangDataDictEntity> selectListByQuery(HaikangDataDictQuery query) {
        LambdaQueryWrapper<HaikangDataDictEntity> queryWrapper = Wrappers.<HaikangDataDictEntity>lambdaQuery()
                .eq(HaikangDataDictEntity::getDictTypeCode, query.getDictTypeCode())
                .eq(HaikangDataDictEntity::getStatus, EntityConstants.ENABLED);
        if(StringUtils.isNotBlank(query.getDataModel())){
            queryWrapper.eq(HaikangDataDictEntity::getDataModel, query.getDataModel());
        }
        queryWrapper.orderByAsc(HaikangDataDictEntity::getSort);
        List<HaikangDataDictEntity> selectList = haikangDataDictMapper.selectList(
                queryWrapper);
        return Optional.ofNullable(selectList).orElse(Lists.newArrayList());
    }


}
