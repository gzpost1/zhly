package cn.cuiot.dmp.system.domain.repository;

import cn.cuiot.dmp.domain.types.id.OrganizationId;
import cn.cuiot.dmp.query.PageResult;
import cn.cuiot.dmp.repository.Repository;
import cn.cuiot.dmp.system.domain.entity.Organization;
import cn.cuiot.dmp.system.domain.query.OrganizationCommonQuery;
import cn.cuiot.dmp.system.domain.query.OrganizationMapperQuery;
import java.util.List;
import lombok.NonNull;

/**
 * @Author 犬豪
 * @Date 2023/8/28 11:53
 * @Version V1.0
 */
public interface OrganizationRepository extends Repository<Organization, OrganizationId> {

    /**
     * 批量ID删除
     *
     * @param aggregate
     * @return
     */
    @Override
    int removeList(@NonNull List<OrganizationId> aggregate);

    /**
     * 条件查询
     */
    List<Organization> commonQuery(@NonNull OrganizationCommonQuery organizationCommonQuery);

    /**
     * 条件查询，返回一条记录
     */
    Organization commonQueryOne(@NonNull OrganizationCommonQuery organizationCommonQuery);

    /**
     * 条件查询,返回count
     */
    long commonCountQuery(@NonNull OrganizationCommonQuery organizationCommonQuery);

    /**
     * 条件查询,可查询已删除、未删除的数据
     */
    PageResult<Organization> pageQuery(@NonNull OrganizationMapperQuery organizationMapperQuery);

    /**
     * 多条件编辑
     */
    boolean updateByParams(@NonNull Organization organization, @NonNull OrganizationCommonQuery organizationCommonQuery);
}
