package cn.cuiot.dmp.system.infrastructure.persistence.mapper;

import cn.cuiot.dmp.system.user_manage.query.OrganizationMapperQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 租户表 Mapper 接口
 * </p>
 *
 * @author yth
 * @since 2023-08-28
 */
public interface OrganizationEntityMapper extends BaseMapper<OrganizationEntity> {


    List<OrganizationEntity> selectListByLogicDel(@Param("param") OrganizationMapperQuery organizationMapperQuery);
}
