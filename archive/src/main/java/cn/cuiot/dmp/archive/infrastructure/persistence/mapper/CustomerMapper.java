package cn.cuiot.dmp.archive.infrastructure.persistence.mapper;

import cn.cuiot.dmp.archive.domain.aggregate.CustomerCriteriaQuery;
import cn.cuiot.dmp.archive.infrastructure.entity.CustomerEntity;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerVo;
import cn.cuiot.dmp.base.infrastructure.dto.req.CustomerUseReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CustomerUserRspDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 客户信息 Mapper 接口
 * </p>
 *
 * @author wuyongchong
 * @since 2024-06-12
 */
public interface CustomerMapper extends BaseMapper<CustomerEntity> {

    IPage<CustomerVo> queryForList(Page<CustomerVo> page, @Param("param") CustomerCriteriaQuery criteriaQuery);

    List<CustomerVo> queryForList(@Param("param") CustomerCriteriaQuery criteriaQuery);

    List<CustomerUserRspDto> lookupCustomerUsers(@Param("param") CustomerUseReqDto reqDto);

}
