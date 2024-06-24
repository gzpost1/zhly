package cn.cuiot.dmp.archive.application.service;

import cn.cuiot.dmp.archive.infrastructure.entity.CustomerMemberEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.CustomerMemberMapper;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerMemberVo;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.Sm4;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户成员信息 服务类
 * </p>
 *
 * @author wuyongchong
 * @since 2024-06-12
 */
@Service
public class CustomerMemberService extends ServiceImpl<CustomerMemberMapper, CustomerMemberEntity> {

    @Autowired
    private CustomerMemberMapper customerMemberMapper;

    /**
     *  根据客户ID查询
     * @param customerIdList
     * @return
     */
    public List<CustomerMemberVo> selectByCustomerId(List<Long> customerIdList){
        List<CustomerMemberVo> voList = customerMemberMapper
                .selectByCustomerId(customerIdList);
        if(CollectionUtils.isNotEmpty(voList)){
            voList.forEach(item->{
                if(StringUtils.isNotBlank(item.getCertificateCdoe())){
                    item.setCertificateCdoe(Sm4.decrypt(item.getCertificateCdoe()));
                }
                if(StringUtils.isNotBlank(item.getMemberPhone())){
                    item.setMemberPhone(Sm4.decrypt(item.getMemberPhone()));
                }
            });
        }
        return voList;
    }

    /**
     * 根据客户ID删除
     * @param customerId
     */
    public void deleteByCustomerId(Long customerId) {
        /*LambdaUpdateWrapper<CustomerMemberEntity> lambdaUpdate = Wrappers.lambdaUpdate();
        lambdaUpdate.set(CustomerMemberEntity::getDeleted, EntityConstants.DELETED);
        lambdaUpdate.eq(CustomerMemberEntity::getCustomerId, customerId);
        customerMemberMapper.update(null, lambdaUpdate);*/
        customerMemberMapper.deleteByCustomerId(customerId);
    }
}
