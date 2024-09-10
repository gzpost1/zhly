package cn.cuiot.dmp.externalapi.service.service.gw;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.constant.PersonGroupRelationConstant;
import cn.cuiot.dmp.externalapi.service.entity.PersonGroupRelationEntity;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardPersonEntity;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardPersonCreateDto;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardPersonUpdateDto;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardPersonPageQuery;
import cn.cuiot.dmp.externalapi.service.service.TbPersonGroupRelationService;
import cn.cuiot.dmp.externalapi.service.mapper.gw.GwEntranceGuardPersonMapper;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 联通格物门禁-人员管理 业务层
 *
 * @Author: zc
 * @Date: 2024-09-07
 */
@Service
public class GwEntranceGuardPersonService extends ServiceImpl<GwEntranceGuardPersonMapper, GwEntranceGuardPersonEntity> {

    @Autowired
    private TbPersonGroupRelationService tbPersonGroupRelationService;

    /**
     * 查询分页
     */
    public IPage<GwEntranceGuardPersonEntity> queryForPage(GwEntranceGuardPersonPageQuery query) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        LambdaQueryWrapper<GwEntranceGuardPersonEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GwEntranceGuardPersonEntity::getCompanyId, companyId);
        wrapper.like(StringUtils.isNotBlank(query.getName()), GwEntranceGuardPersonEntity::getName, query.getName());
        wrapper.eq(Objects.nonNull(query.getPersonGroupId()), GwEntranceGuardPersonEntity::getPersonGroupId, query.getPersonGroupId());
        wrapper.eq(Objects.nonNull(query.getAuthorize()), GwEntranceGuardPersonEntity::getAuthorize, query.getAuthorize());
        wrapper.orderByDesc(GwEntranceGuardPersonEntity::getCreateTime);

        return page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper);
    }

    /**
     * 详情
     */
    public GwEntranceGuardPersonEntity queryForDetail(Long id) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        LambdaQueryWrapper<GwEntranceGuardPersonEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GwEntranceGuardPersonEntity::getCompanyId, companyId);
        wrapper.eq(GwEntranceGuardPersonEntity::getId, id);
        List<GwEntranceGuardPersonEntity> list = list(wrapper);

        return CollectionUtils.isNotEmpty(list) ? list.get(0) : null;
    }

    /**
     * 创建
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(GwEntranceGuardPersonCreateDto dto) {
        //参数校验
        checkData(dto, null);

        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        //部门id
        Long deptId = LoginInfoHolder.getCurrentDeptId();

        long id = IdWorker.getId();
        GwEntranceGuardPersonEntity personEntity = new GwEntranceGuardPersonEntity();
        BeanUtils.copyProperties(dto, personEntity);
        personEntity.setId(id);
        personEntity.setCompanyId(companyId);
        personEntity.setDeptId(deptId);
        save(personEntity);

        //设置分组关联信息
        setPersonGroupRelation(personEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(GwEntranceGuardPersonUpdateDto dto) {
        //参数校验
        checkData(dto, dto.getId());

        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();

        List<GwEntranceGuardPersonEntity> list = list(
                new LambdaQueryWrapper<GwEntranceGuardPersonEntity>()
                        .eq(GwEntranceGuardPersonEntity::getCompanyId, companyId)
                        .eq(GwEntranceGuardPersonEntity::getId, dto.getId()));
        if (CollectionUtils.isEmpty(list)) {
            throw new BusinessException(ResultCode.ERROR, "数据不存在");
        }

        GwEntranceGuardPersonEntity personEntity = list.get(0);
        BeanUtils.copyProperties(dto, personEntity);
        updateById(personEntity);

        //设置分组信息
        if (!Objects.equals(dto.getPersonGroupId(), personEntity.getPersonGroupId())) {
            setPersonGroupRelation(personEntity);
        }
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> ids) {
        //企业id
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        List<GwEntranceGuardPersonEntity> gwEntranceGuardPersonList = checkPerson(ids, companyId);

        removeByIds(ids);

        //删除关联信息
        gwEntranceGuardPersonList.forEach(this::deletePersonGroupRelation);
    }

    /**
     * 人员校验
     */
    public List<GwEntranceGuardPersonEntity> checkPerson(List<Long> ids, Long companyId) {
        List<GwEntranceGuardPersonEntity> list = list(
                new LambdaQueryWrapper<GwEntranceGuardPersonEntity>()
                        .eq(GwEntranceGuardPersonEntity::getCompanyId, companyId));
        if (org.apache.commons.collections.CollectionUtils.isEmpty(list)) {
            throw new BusinessException(ResultCode.ERROR, "数据不存在");
        }

        //判断人员id列表是否都属于该企业的设备
        List<GwEntranceGuardPersonEntity> collect = list.stream().filter(e -> !ids.contains(e.getId())).collect(Collectors.toList());
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(collect)) {
            List<String> deviceNames = collect.stream().map(GwEntranceGuardPersonEntity::getName).collect(Collectors.toList());
            throw new BusinessException(ResultCode.ERROR, "人员【" + String.join(",", deviceNames) + "】不属于该企业");
        }
        return list;
    }

    /**
     * 设置人员分组关联信息
     */
    private void setPersonGroupRelation(GwEntranceGuardPersonEntity personEntity) {
        PersonGroupRelationEntity relation = buildEntity(personEntity);
        tbPersonGroupRelationService.createOrUpdate(relation);
    }

    /**
     * 删除人员分组关联信息
     */
    private void deletePersonGroupRelation(GwEntranceGuardPersonEntity personEntity) {
        PersonGroupRelationEntity relation = buildEntity(personEntity);
        tbPersonGroupRelationService.delete(relation);
    }

    /**
     * 构建关联数据
     */
    private PersonGroupRelationEntity buildEntity(GwEntranceGuardPersonEntity personEntity) {
        PersonGroupRelationEntity relation = new PersonGroupRelationEntity();
        relation.setBusinessType(PersonGroupRelationConstant.GW_ENTRANCE_GUARD);
        relation.setDataId(personEntity.getId());
        relation.setPersonGroupId(personEntity.getPersonGroupId());
        return relation;
    }

    /**
     * 保存参数校验
     */
    private void checkData(GwEntranceGuardPersonCreateDto dto, Long id) {
        //时效校验
        if (Objects.equals(dto.getPrescriptionType(), EntityConstants.YES)) {
            if (Objects.isNull(dto.getPrescriptionBeginDate()) || Objects.isNull(dto.getPrescriptionEndDate())) {
                throw new BusinessException(ResultCode.ERROR, "请选择有效期起止时间");
            }
            if (dto.getPrescriptionEndDate().before(dto.getPrescriptionBeginDate())) {
                throw new BusinessException(ResultCode.ERROR, "时效结束日期必须大于开始日期");
            }
        }
        //身份证号校验
        if (StringUtils.isNotBlank(dto.getIdCardNo())) {
            boolean validCard = IdcardUtil.isValidCard(dto.getIdCardNo());
            if (!validCard) {
                throw new BusinessException(ResultCode.ERROR, "请输入正确的身份证号");
            }
        }
        //ic、密码、人员照片校验
        if (StringUtils.isBlank(dto.getIdCardNo()) && StringUtils.isBlank(dto.getIcCardNo()) && StringUtils.isBlank(dto.getImage())) {
            throw new BusinessException(ResultCode.ERROR, "IC卡号、密码、人员照片三项至少填写一项");
        }
        //ic卡号校验
        if (StringUtils.isNotBlank(dto.getIcCardNo())) {
            boolean numeric = StrUtil.isNumeric(dto.getIcCardNo());
            if (!numeric) {
                throw new BusinessException(ResultCode.ERROR, "密码必须是纯数字");
            }
            //校验数据库是否已存在
            Boolean existsIcCardNo = baseMapper.isExistsIcCardNo(dto.getIcCardNo(), id);
            if (existsIcCardNo) {
                throw new BusinessException(ResultCode.ERROR, "IC卡号已存在");
            }
        }
    }
}
