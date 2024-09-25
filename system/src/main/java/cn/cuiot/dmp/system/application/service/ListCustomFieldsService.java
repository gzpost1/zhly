package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.param.dto.ListCustomFieldsDto;
import cn.cuiot.dmp.system.application.param.dto.ListCustomFieldsQueryDto;
import cn.cuiot.dmp.system.application.param.vo.ListCustomFieldsVO;
import cn.cuiot.dmp.system.infrastructure.entity.ListCustomFieldsEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.ListCustomFieldsMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * 列表自定义字段 业务层
 *
 * @Author: zc
 * @Date: 2024-09-03
 */
@Service
public class ListCustomFieldsService extends ServiceImpl<ListCustomFieldsMapper, ListCustomFieldsEntity> {

    public void saveOrUpdate(ListCustomFieldsDto dto) {
        //设置企业id
        dto.setCompanyId(LoginInfoHolder.getCurrentOrgId());

        //根据企业id和标识查询数据，有则修改无则新增
        LambdaQueryWrapper<ListCustomFieldsEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ListCustomFieldsEntity::getCompanyId, LoginInfoHolder.getCurrentOrgId());
        wrapper.eq(ListCustomFieldsEntity::getIdentification, dto.getIdentification());
        List<ListCustomFieldsEntity> list = list(wrapper);

        ListCustomFieldsEntity entity = new ListCustomFieldsEntity();
        BeanUtils.copyProperties(dto, entity);

        if (CollectionUtils.isNotEmpty(list)) {
            entity.setId(list.get(0).getId());
        }

        saveOrUpdate(entity);
    }

    public ListCustomFieldsVO queryByIdentification(ListCustomFieldsQueryDto dto) {
        //设置企业id
        dto.setCompanyId(LoginInfoHolder.getCurrentOrgId());

        //根据企业id和标识查询数据
        LambdaQueryWrapper<ListCustomFieldsEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ListCustomFieldsEntity::getCompanyId, dto.getCompanyId());
        wrapper.eq(ListCustomFieldsEntity::getIdentification, dto.getIdentification());
        List<ListCustomFieldsEntity> list = list(wrapper);

        ListCustomFieldsVO vo = new ListCustomFieldsVO();
        if (CollectionUtils.isNotEmpty(list)) {
            ListCustomFieldsEntity entity = list.get(0);
            BeanUtils.copyProperties(entity, vo);
        }

        return vo;
    }
}
