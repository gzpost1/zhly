package cn.cuiot.dmp.archive.infrastructure.persistence.mapper;

import cn.cuiot.dmp.archive.application.param.query.CustomerQuery;
import cn.cuiot.dmp.archive.infrastructure.entity.CustomerHouseEntity;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerHouseVo;
import cn.cuiot.dmp.archive.infrastructure.vo.HouseCustomerVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 客户房屋信息 Mapper 接口
 * </p>
 *
 * @author wuyongchong
 * @since 2024-06-12
 */
public interface CustomerHouseMapper extends BaseMapper<CustomerHouseEntity> {

    List<CustomerHouseVo> selectByCustomerId(@Param("customerIdList") List<Long> customerIdList);

    void deleteByCustomerId(@Param("customerId") Long customerId);

    IPage<HouseCustomerVo> queryHouseCustomerList(Page<HouseCustomerVo> page,@Param("param")  CustomerQuery query);

}
