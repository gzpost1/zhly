package cn.cuiot.dmp.archive.application.service;

import cn.cuiot.dmp.archive.infrastructure.entity.CustomerVehicleEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.CustomerVehicleMapper;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerVehicleVo;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.Sm4;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户车辆信息 服务类
 * </p>
 *
 * @author wuyongchong
 * @since 2024-06-12
 */
@Service
public class CustomerVehicleService extends
        ServiceImpl<CustomerVehicleMapper, CustomerVehicleEntity> {

    @Autowired
    private CustomerVehicleMapper customerVehicleMapper;

    /**
     * 根据客户ID查询
     */
    public List<CustomerVehicleVo> selectByCustomerId(List<Long> customerIdList) {
        List<CustomerVehicleVo> voList = customerVehicleMapper
                .selectByCustomerId(customerIdList);
        if (CollectionUtils.isNotEmpty(voList)) {
            voList.forEach(item -> {
                if (StringUtils.isNotBlank(item.getCertificateCdoe())) {
                    item.setCertificateCdoe(Sm4.decrypt(item.getCertificateCdoe()));
                }
                if (StringUtils.isNotBlank(item.getPhone())) {
                    item.setPhone(Sm4.decrypt(item.getPhone()));
                }
            });
        }
        return voList;
    }

    /**
     * 根据客户ID删除
     */
    public void deleteByCustomerId(Long customerId) {
        /*LambdaUpdateWrapper<CustomerVehicleEntity> lambdaUpdate = Wrappers.lambdaUpdate();
        lambdaUpdate.set(CustomerVehicleEntity::getDeleted, EntityConstants.DELETED);
        lambdaUpdate.eq(CustomerVehicleEntity::getCustomerId, customerId);
        customerVehicleMapper.update(null, lambdaUpdate);*/
        customerVehicleMapper.deleteByCustomerId(customerId);
    }
}
