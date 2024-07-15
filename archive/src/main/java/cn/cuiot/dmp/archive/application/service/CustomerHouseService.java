package cn.cuiot.dmp.archive.application.service;

import cn.cuiot.dmp.archive.application.param.query.CustomerQuery;
import cn.cuiot.dmp.archive.infrastructure.entity.CustomerHouseEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.CustomerHouseMapper;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerHouseVo;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerVo;
import cn.cuiot.dmp.archive.infrastructure.vo.HouseCustomerVo;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.Sm4;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户房屋信息 服务类
 * </p>
 *
 * @author wuyongchong
 * @since 2024-06-12
 */
@Service
public class CustomerHouseService extends ServiceImpl<CustomerHouseMapper, CustomerHouseEntity> {

    @Autowired
    private CustomerHouseMapper customerHouseMapper;

    /**
     *  根据客户ID查询
     * @param customerIdList
     * @return
     */
    public List<CustomerHouseVo> selectByCustomerId(List<Long> customerIdList){
        List<CustomerHouseVo> voList = customerHouseMapper
                .selectByCustomerId(customerIdList);
        return Optional.ofNullable(voList).orElse(Lists.newArrayList());
    }

    /**
     * 根据客户ID删除
     * @param customerId
     */
    public void deleteByCustomerId(Long customerId) {
        /*LambdaUpdateWrapper<CustomerHouseEntity> lambdaUpdate = Wrappers.lambdaUpdate();
        lambdaUpdate.set(CustomerHouseEntity::getDeleted, EntityConstants.DELETED);
        lambdaUpdate.eq(CustomerHouseEntity::getCustomerId, customerId);
        customerHouseMapper.update(null, lambdaUpdate);*/
        customerHouseMapper.deleteByCustomerId(customerId);
    }

    /**
     * 房屋关联客户列表
     */
    public IPage<HouseCustomerVo> queryHouseCustomerList(CustomerQuery query) {
        return customerHouseMapper.queryHouseCustomerList(new Page<HouseCustomerVo>(query.getPageNo(), query.getPageSize()),query);
    }

}
