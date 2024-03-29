package cn.cuiot.dmp.system.infrastructure.domain.converter;

import cn.cuiot.dmp.base.infrastructure.domain.converter.Converter;
import cn.cuiot.dmp.domain.types.Email;
import cn.cuiot.dmp.domain.types.EncryptedValue;
import cn.cuiot.dmp.domain.types.enums.OperateByTypeEnum;
import cn.cuiot.dmp.domain.types.id.OrganizationId;
import cn.cuiot.dmp.domain.types.id.UserId;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.OrganizationEntity;
import cn.cuiot.dmp.system.user_manage.domain.entity.Organization;
import cn.cuiot.dmp.system.user_manage.domain.types.enums.OrgSourceEnum;
import cn.cuiot.dmp.system.user_manage.domain.types.enums.OrgStatusEnum;
import cn.cuiot.dmp.system.user_manage.domain.types.enums.OrgTypeEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description 领域模型和数据库模型之间的转换
 * @Author 犬豪
 * @Date 2023/8/29 14:15
 * @Version V1.0
 */
public class OrganizatioConverter implements Converter<Organization, OrganizationEntity> {
    private OrganizatioConverter() {
    }

    public static final OrganizatioConverter INSTANCE = new OrganizatioConverter();

    @Override
    public Organization toDomain(OrganizationEntity organizationEntity) {
        if (organizationEntity == null) {
            return null;
        }
        Organization organization = Organization.builder().build();
        organization.setId(organizationEntity.getId() != null ? new OrganizationId(organizationEntity.getId()) : null);
        organization.setOrgKey(organizationEntity.getOrgKey());
        organization.setOrgId(organizationEntity.getOrgId());
        organization.setOrgName(organizationEntity.getOrgName());
        organization.setOrgTypeId(OrgTypeEnum.valueOf(organizationEntity.getOrgTypeId()));
        organization.setStatus(OrgStatusEnum.valueOf(organizationEntity.getStatus()));
        organization.setParentId(
                organizationEntity.getParentId() != null ? new OrganizationId(organizationEntity.getParentId()) : null);
        organization.setOrgOwner(
                organizationEntity.getOrgOwner() != null ? new UserId(organizationEntity.getOrgOwner()) : null);
        organization.setEmail(StringUtils.isNotBlank(organizationEntity.getEmail()) ? new Email(new EncryptedValue(organizationEntity.getEmail())) : null);
        organization.setSocialCreditCode(organizationEntity.getSocialCreditCode());
        organization.setCompanyName(organizationEntity.getCompanyName());
        organization.setDescription(organizationEntity.getDescription());
        organization.setCreatedOn(organizationEntity.getCreatedOn());
        organization.setCreatedBy(organizationEntity.getCreatedBy());
        organization.setCreatedByType(OperateByTypeEnum.valueOf(organizationEntity.getCreatedByType()));
        organization.setUpdatedOn(organizationEntity.getUpdatedOn());
        organization.setUpdatedBy(organizationEntity.getUpdatedBy());
        organization.setUpdatedByType(OperateByTypeEnum.valueOf(organizationEntity.getUpdatedByType()));
        organization.setDeletedFlag(organizationEntity.getDeletedFlag());
        organization.setMaxDeptHigh(organizationEntity.getMaxDeptHigh());
        organization.setSource(OrgSourceEnum.valueOf(organizationEntity.getSource()));

        return organization;
    }

    @Override
    public OrganizationEntity toEntity(Organization organization) {
        if (organization == null) {
            return null;
        }
        OrganizationEntity entity = new OrganizationEntity();
        entity.setId(organization.getId() != null ? organization.getId().getValue() : null);
        entity.setOrgKey(organization.getOrgKey());
        entity.setOrgId(organization.getOrgId());
        entity.setOrgName(organization.getOrgName());
        entity.setOrgTypeId(organization.getOrgTypeId() != null ? organization.getOrgTypeId().getValue() : null);
        entity.setStatus(organization.getStatus() != null ? organization.getStatus().getValue() : null);
        entity.setParentId(organization.getParentId() != null ? organization.getParentId().getValue() : null);
        entity.setOrgOwner(organization.getOrgOwner() != null ? organization.getOrgOwner().getValue() : null);
        entity.setEmail(organization.getEmail() != null ? organization.getEmail().getEncryptedValue() : null);
        entity.setSocialCreditCode(organization.getSocialCreditCode());
        entity.setCompanyName(organization.getCompanyName());
        entity.setDescription(organization.getDescription());
        entity.setCreatedOn(organization.getCreatedOn());
        entity.setCreatedBy(organization.getCreatedBy());
        entity.setCreatedByType(
                organization.getCreatedByType() != null ? organization.getCreatedByType().getValue() : null);
        entity.setUpdatedOn(organization.getUpdatedOn());
        entity.setUpdatedBy(organization.getUpdatedBy());
        entity.setUpdatedByType(
                organization.getUpdatedByType() != null ? organization.getUpdatedByType().getValue() : null);
        entity.setDeletedFlag(organization.getDeletedFlag());
        entity.setMaxDeptHigh(organization.getMaxDeptHigh());
        entity.setSource(organization.getSource() != null ? organization.getSource().getValue() : null);
        entity.setDeletedOn(organization.getDeletedOn());
        return entity;
    }
}
