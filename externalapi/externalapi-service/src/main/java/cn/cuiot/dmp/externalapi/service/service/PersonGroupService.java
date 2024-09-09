package cn.cuiot.dmp.externalapi.service.service;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.query.PersonGroupCreateDTO;
import cn.cuiot.dmp.externalapi.service.query.PersonGroupPageQuery;
import cn.cuiot.dmp.externalapi.service.query.PersonGroupUpdateDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuiot.dmp.externalapi.service.mapper.PersonGroupMapper;
import cn.cuiot.dmp.externalapi.service.entity.PersonGroupEntity;

import java.util.List;

/**
 * 人员分组 业务层
 *
 * @Author: zc
 * @Date: 2024-09-04
 */
@Service
public class PersonGroupService extends ServiceImpl<PersonGroupMapper, PersonGroupEntity> {

    /**
     * 分页查询
     *
     * @return IPage
     * @Param query 参数
     */
    public IPage<PersonGroupEntity> queryForPage(PersonGroupPageQuery query) {
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        LambdaQueryWrapper<PersonGroupEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(query.getName()), PersonGroupEntity::getName, query.getName());
        wrapper.eq(PersonGroupEntity::getCompanyId, companyId);
        wrapper.orderByDesc(PersonGroupEntity::getCreateTime);

        return page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper);
    }

    /**
     * 分页查询
     *
     * @return List
     * @Param query 参数
     */
    public List<PersonGroupEntity> queryForList(PersonGroupPageQuery query) {
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        LambdaQueryWrapper<PersonGroupEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(query.getName()), PersonGroupEntity::getName, query.getName());
        wrapper.eq(PersonGroupEntity::getCompanyId, companyId);
        wrapper.orderByDesc(PersonGroupEntity::getCreateTime);

        return list(wrapper);
    }

    /**
     * 根据id获取分组名称
     * @param ids id列表
     * @return list
     */
    public List<PersonGroupEntity> queryPersonGroupByIds(List<Long> ids){
       return getBaseMapper().selectBatchIds(ids);
    }
    /**
     * 详情
     *
     * @return PersonGroupEntity
     * @Param id 数据id
     */
    public PersonGroupEntity queryForDetail(Long id) {
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        List<PersonGroupEntity> entityList = list(
                new LambdaQueryWrapper<PersonGroupEntity>()
                        .eq(PersonGroupEntity::getCompanyId, companyId)
                        .eq(PersonGroupEntity::getId, id));

        return CollectionUtils.isNotEmpty(entityList) ? entityList.get(0) : null;
    }

    /**
     * 创建
     *
     * @Param dto 参数
     */
    public void create(PersonGroupCreateDTO dto) {
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        List<PersonGroupEntity> entityList = list(
                new LambdaQueryWrapper<PersonGroupEntity>()
                        .eq(PersonGroupEntity::getName, dto.getName()));

        if (CollectionUtils.isNotEmpty(entityList)) {
            throw new BusinessException(ResultCode.ERROR, "分组名称已存在，请重新输入");
        }

        PersonGroupEntity groupEntity = new PersonGroupEntity();
        groupEntity.setCompanyId(companyId);
        groupEntity.setName(dto.getName());
        save(groupEntity);
    }

    /**
     * 更新
     *
     * @Param dto 参数
     */
    public void update(PersonGroupUpdateDTO dto) {
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        List<PersonGroupEntity> entityList = list(
                new LambdaQueryWrapper<PersonGroupEntity>()
                        .eq(PersonGroupEntity::getCompanyId, companyId)
                        .eq(PersonGroupEntity::getId, dto.getId()));

        if (CollectionUtils.isEmpty(entityList)) {
            throw new BusinessException(ResultCode.ERROR, "数据不存在");
        }

        List<PersonGroupEntity> entityList1 = list(
                new LambdaQueryWrapper<PersonGroupEntity>()
                        .ne(PersonGroupEntity::getId, dto.getId())
                        .eq(PersonGroupEntity::getName, dto.getName()));

        if (CollectionUtils.isNotEmpty(entityList1)) {
            throw new BusinessException(ResultCode.ERROR, "分组名称已存在，请重新输入");
        }

        PersonGroupEntity entity = entityList.get(0);
        entity.setName(dto.getName());
        updateById(entity);
    }

    /**
     * 删除
     *
     * @Param 数据id
     */
    public void delete(Long id) {
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        List<PersonGroupEntity> entityList = list(
                new LambdaQueryWrapper<PersonGroupEntity>()
                        .eq(PersonGroupEntity::getCompanyId, companyId)
                        .eq(PersonGroupEntity::getId, id));

        if (CollectionUtils.isEmpty(entityList)) {
            throw new BusinessException(ResultCode.ERROR, "数据不存在");
        }

        removeById(id);
    }
}
