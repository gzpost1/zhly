package cn.cuiot.dmp.archive.infrastructure.persistence.mapper;

import cn.cuiot.dmp.archive.infrastructure.entity.CustomerMemberEntity;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerMemberVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 客户成员信息 Mapper 接口
 * </p>
 *
 * @author wuyongchong
 * @since 2024-06-12
 */
public interface CustomerMemberMapper extends BaseMapper<CustomerMemberEntity> {

    List<CustomerMemberVo> selectByCustomerId(@Param("customerIdList") List<Long> customerIdList);

    void deleteByCustomerId(@Param("customerId") Long customerId);
}
