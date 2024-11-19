package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.common.bean.PageQuery;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.OrgTypeEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.domain.types.id.OrganizationId;
import cn.cuiot.dmp.system.domain.entity.Organization;
import cn.cuiot.dmp.system.domain.query.OrganizationCommonQuery;
import cn.cuiot.dmp.system.domain.repository.OrganizationRepository;
import cn.cuiot.dmp.system.infrastructure.entity.CompanyTemplateEntity;
import cn.cuiot.dmp.system.infrastructure.entity.UserDataEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.CompanyTemplateDto;
import cn.cuiot.dmp.system.infrastructure.entity.vo.CompanyTemplateVO;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.UserDataDao;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.CompanyTemplateMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 后台-初始化管理-企业模板 业务层
 *
 * @Author: zc
 * @Date: 2024-11-05
 */
@Service
public class CompanyTemplateService extends ServiceImpl<CompanyTemplateMapper, CompanyTemplateEntity> {

    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private UserDataDao userDataDao;

    /**
     * 分页
     *
     * @return IPage
     * @Param query 参数
     */
    public IPage<CompanyTemplateVO> queryForPage(PageQuery query) {

        // 获取当前账户类型
        Long orgTypeId = getOrgTypeId();
        if (!Objects.equals(orgTypeId, OrgTypeEnum.PLATFORM.getValue())) {
            return new Page<>();
        }

        // 构造条件
        LambdaQueryWrapper<CompanyTemplateEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(CompanyTemplateEntity::getCreateTime);

        // 分页查询
        IPage<CompanyTemplateVO> iPage = this.page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper).convert(item -> {
            CompanyTemplateVO vo = new CompanyTemplateVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        });

        List<CompanyTemplateVO> records;
        if (Objects.nonNull(iPage) && CollectionUtils.isNotEmpty(records = iPage.getRecords())) {
            // 获取企业id列表
            List<Long> companyIds = records.stream().map(CompanyTemplateVO::getCompanyId).distinct().collect(Collectors.toList());
            Map<Long, Organization> organizationMap = getOrganizationMap(companyIds);

            // 获取创建人id列表
            List<Long> createUserId = records.stream().map(CompanyTemplateVO::getCreateUser).distinct().collect(Collectors.toList());
            Map<Long, String> userMap = getUserMap(createUserId);

            records.forEach(item -> {
                // 设置企业信息
                if (MapUtils.isNotEmpty(organizationMap) && organizationMap.containsKey(item.getCompanyId())) {
                    Organization organization = organizationMap.get(item.getCompanyId());
                    item.setCompanyCode(organization.getOrgKey());
                    item.setCompanyName(organization.getCompanyName());
                }

                // 设置用户信息
                if (MapUtils.isNotEmpty(userMap) && userMap.containsKey(item.getCreateUser())) {
                    item.setCreateUserName(userMap.get(item.getCreateUser()));
                }
            });
        }

        return iPage;
    }

    /**
     * 获取最后一条模板数据
     *
     * @return CompanyTemplateVO
     */
    public CompanyTemplateVO queryLastTemplate() {

        // 获取当前账户类型
        Long orgTypeId = getOrgTypeId();
        if (!Objects.equals(orgTypeId, OrgTypeEnum.PLATFORM.getValue())) {
            return new CompanyTemplateVO();
        }

        CompanyTemplateVO lastTemplate = baseMapper.queryLastTemplate();
        if (Objects.nonNull(lastTemplate) && Objects.nonNull(lastTemplate.getCompanyId())) {
            // 获取企业id列表
            Map<Long, Organization> organizationMap = getOrganizationMap(Collections.singletonList(lastTemplate.getCompanyId()));
            // 设置企业信息
            if (MapUtils.isNotEmpty(organizationMap) && organizationMap.containsKey(lastTemplate.getCompanyId())) {
                Organization organization = organizationMap.get(lastTemplate.getCompanyId());
                lastTemplate.setCompanyCode(organization.getOrgKey());
                lastTemplate.setCompanyName(organization.getCompanyName());
            }
        }
        return lastTemplate;
    }

    /**
     * 保存
     *
     * @Param dto 参数
     */
    public void saveOrUpdate(CompanyTemplateDto dto) {

        // 获取当前账户类型
        Long orgTypeId = getOrgTypeId();
        if (!Objects.equals(orgTypeId, OrgTypeEnum.PLATFORM.getValue())) {
            throw new BusinessException(ResultCode.ERROR, "该页面权限仅对平台开开放");
        }

        // 判断模板企业是否存在
        Organization organization = organizationRepository.find(new OrganizationId(dto.getCompanyId()));
        if (Objects.isNull(organization)) {
            throw new BusinessException(ResultCode.ERROR, "模板企业不存在");
        }

        CompanyTemplateEntity template = new CompanyTemplateEntity();
        template.setCompanyId(dto.getCompanyId());
        save(template);
    }

    /**
     * 获取账户类型
     */
    private Long getOrgTypeId() {
        Integer currentOrgTypeId = LoginInfoHolder.getCurrentOrgTypeId();
        if (Objects.isNull(currentOrgTypeId)) {
            throw new BusinessException(ResultCode.ERROR, "所属企业不存在");
        }
        return (long) currentOrgTypeId;
    }

    /**
     * 获取用户Map
     *
     * @return Map
     * @Param userIds 用户列表
     */
    private Map<Long, String> getUserMap(List<Long> userIds) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("userIdList", userIds);
        List<UserDataEntity> userList = userDataDao.lookUpUserList(params);

        if (CollectionUtils.isNotEmpty(userList)) {
            return userList.stream().collect(Collectors.toMap(UserDataEntity::getId, UserDataEntity::getName));
        }
        return null;
    }

    /**
     * 获取用户Map
     *
     * @return Map
     * @Param userIds 用户列表
     */
    private Map<Long, Organization> getOrganizationMap(List<Long> companyIds) {
        OrganizationCommonQuery.OrganizationCommonQueryBuilder builder = OrganizationCommonQuery.builder();
        builder.idList(companyIds.stream().map(OrganizationId::new).collect(Collectors.toList()));
        List<Organization> organizationList = organizationRepository.commonQuery(builder.build());

        if (CollectionUtils.isNotEmpty(organizationList)) {
            return organizationList.stream().collect(Collectors.toMap(e -> e.getId().getValue(), e -> e));
        }
        return null;
    }
}
