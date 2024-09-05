package cn.cuiot.dmp.externalapi.service.service;

import cn.cuiot.dmp.common.utils.AssertUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuiot.dmp.externalapi.service.mapper.PersonGroupRelationMapper;
import cn.cuiot.dmp.externalapi.service.entity.PersonGroupRelationEntity;

import java.util.List;
import java.util.Objects;

/**
 * 人员分组关联表 业务层
 *
 * @Author: zc
 * @Date: 2024-09-05
 */
@Service
public class TbPersonGroupRelationService extends ServiceImpl<PersonGroupRelationMapper, PersonGroupRelationEntity> {

    /**
     * 保存或更新
     */
    public void createOrUpdate(PersonGroupRelationEntity entity) {
        AssertUtil.isFalse(Objects.isNull(entity.getBusinessType()), "businessType不能为空");
        AssertUtil.isFalse(Objects.isNull(entity.getDataId()), "dataId不能为空");
        AssertUtil.isFalse(Objects.isNull(entity.getPersonGroupId()), "personGroupId不能为空");

        List<PersonGroupRelationEntity> list = list(new LambdaQueryWrapper<PersonGroupRelationEntity>()
                .eq(PersonGroupRelationEntity::getBusinessType, entity.getBusinessType())
                .eq(PersonGroupRelationEntity::getDataId, entity.getDataId()));
        if (CollectionUtils.isNotEmpty(list)) {
            PersonGroupRelationEntity entity1 = list.get(0);
            entity1.setPersonGroupId(entity1.getPersonGroupId());
            updateById(entity1);
        } else {
            save(entity);
        }
    }

    /**
     * 删除
     */
    public void delete(PersonGroupRelationEntity entity) {
        AssertUtil.isFalse(Objects.isNull(entity.getBusinessType()), "businessType不能为空");
        AssertUtil.isFalse(Objects.isNull(entity.getDataId()), "dataId不能为空");

        List<PersonGroupRelationEntity> list = list(new LambdaQueryWrapper<PersonGroupRelationEntity>()
                .eq(PersonGroupRelationEntity::getBusinessType, entity.getBusinessType())
                .eq(PersonGroupRelationEntity::getDataId, entity.getDataId()));
        if (CollectionUtils.isNotEmpty(list)) {
            removeById(list.get(0).getId());
        }
    }
}
