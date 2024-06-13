package cn.cuiot.dmp.archive.application.service;

import cn.cuiot.dmp.archive.application.param.dto.CustomerDto;
import cn.cuiot.dmp.archive.application.param.dto.CustomerHouseDto;
import cn.cuiot.dmp.archive.application.param.dto.CustomerMemberDto;
import cn.cuiot.dmp.archive.application.param.dto.CustomerVehicleDto;
import cn.cuiot.dmp.archive.application.param.query.CustomerQuery;
import cn.cuiot.dmp.archive.domain.aggregate.CustomerCriteriaQuery;
import cn.cuiot.dmp.archive.infrastructure.entity.CustomerEntity;
import cn.cuiot.dmp.archive.infrastructure.entity.CustomerHouseEntity;
import cn.cuiot.dmp.archive.infrastructure.entity.CustomerMemberEntity;
import cn.cuiot.dmp.archive.infrastructure.entity.CustomerVehicleEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.CustomerMapper;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerHouseVo;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerMemberVo;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerVehicleVo;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerVo;
import cn.cuiot.dmp.base.infrastructure.constants.MsgBindingNameConstants;
import cn.cuiot.dmp.base.infrastructure.constants.MsgTagConstants;
import cn.cuiot.dmp.base.infrastructure.stream.StreamMessageSender;
import cn.cuiot.dmp.base.infrastructure.stream.messaging.SimpleMsg;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.Sm4;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 客户信息 服务类
 * </p>
 *
 * @author wuyongchong
 * @since 2024-06-12
 */
@Service
public class CustomerService extends ServiceImpl<CustomerMapper, CustomerEntity> {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerHouseService customerHouseService;

    @Autowired
    private CustomerMemberService customerMemberService;

    @Autowired
    private CustomerVehicleService customerVehicleService;

    @Autowired
    private StreamMessageSender streamMessageSender;

    /**
     * 分页查询
     */
    public IPage<CustomerVo> queryForPage(CustomerQuery query) {
        CustomerCriteriaQuery criteriaQuery = CustomerCriteriaQuery.builder()
                .companyId(query.getCompanyId())
                .build();
        IPage<CustomerVo> page = customerMapper
                .queryForList(new Page<CustomerVo>(query.getPageNo(), query.getPageSize()),
                        criteriaQuery);
        List<Long> customerIdList = Optional.ofNullable(page.getRecords())
                .orElse(Lists.newArrayList())
                .stream().map(ite -> ite.getId()).collect(
                        Collectors.toList());
        if (CollectionUtils.isNotEmpty(customerIdList)) {
            List<CustomerHouseVo> houseList = customerHouseService
                    .selectByCustomerId(customerIdList);
            if (CollectionUtils.isNotEmpty(houseList)) {
                for (CustomerVo customerVo : page.getRecords()) {
                    customerVo.setHouseList(houseList.stream()
                            .filter(ite -> customerVo.getId().equals(ite.getCustomerId())).collect(
                                    Collectors.toList()));
                }
            }
        }
        return page;
    }

    /**
     * 获取详情
     */
    public CustomerVo queryForDetail(Long id, Long companyId) {
        CustomerEntity entity = customerMapper.selectById(id);
        if (Objects.nonNull(entity)) {
            if (!entity.getCompanyId().equals(companyId)) {
                return null;
            }
            CustomerVo customerVo = new CustomerVo();
            BeanUtils.copyProperties(entity, customerVo);

            List<CustomerHouseVo> houseList = Optional.ofNullable(
                    customerHouseService.selectByCustomerId(Lists.newArrayList(customerVo.getId())))
                    .orElse(Lists.newArrayList());
            customerVo.setHouseList(houseList);

            List<CustomerMemberVo> memberList = Optional.ofNullable(
                    customerMemberService
                            .selectByCustomerId(Lists.newArrayList(customerVo.getId())))
                    .orElse(Lists.newArrayList());
            customerVo.setMemberList(memberList);

            List<CustomerVehicleVo> vehicleList = Optional.ofNullable(
                    customerVehicleService
                            .selectByCustomerId(Lists.newArrayList(customerVo.getId())))
                    .orElse(Lists.newArrayList());
            customerVo.setVehicleList(vehicleList);

            return customerVo;
        }
        return null;
    }

    /**
     * 创建
     */
    @Transactional(rollbackFor = Exception.class)
    public void createCustomer(CustomerDto dto) {
        dto.setContactPhone(Sm4.encryption(dto.getContactPhone()));
        if (StringUtils.isNotBlank(dto.getCertificateCdoe())) {
            dto.setCertificateCdoe(Sm4.encryption(dto.getCertificateCdoe()));
        }
        CustomerEntity entity = new CustomerEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setStatus(EntityConstants.ENABLED);
        customerMapper.insert(entity);
        Long customerId = entity.getId();
        saveRelateList(customerId, dto);
    }

    /**
     * 修改
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateCustomer(CustomerDto dto) {
        dto.setContactPhone(Sm4.encryption(dto.getContactPhone()));
        if (StringUtils.isNotBlank(dto.getCertificateCdoe())) {
            dto.setCertificateCdoe(Sm4.encryption(dto.getCertificateCdoe()));
        }
        CustomerEntity entity = new CustomerEntity();
        BeanUtils.copyProperties(dto, entity);
        customerMapper.updateById(entity);
        Long customerId = entity.getId();
        customerHouseService.deleteByCustomerId(customerId);
        customerMemberService.deleteByCustomerId(customerId);
        customerVehicleService.deleteByCustomerId(customerId);
        saveRelateList(customerId, dto);
    }

    /**
     * 保存关联信息
     */
    public void saveRelateList(Long customerId, CustomerDto dto) {
        List<CustomerHouseDto> houseList = dto.getHouseList();
        if (CollectionUtils.isNotEmpty(houseList)) {
            customerHouseService.saveBatch(houseList.stream().map(item -> {
                CustomerHouseEntity houseEntity = new CustomerHouseEntity();
                BeanUtils.copyProperties(item, houseEntity);
                houseEntity.setCustomerId(customerId);
                return houseEntity;
            }).collect(Collectors.toList()));
        }

        List<CustomerMemberDto> memberList = dto.getMemberList();
        if (CollectionUtils.isNotEmpty(memberList)) {
            customerMemberService.saveBatch(memberList.stream().map(item -> {
                if (StringUtils.isNotBlank(item.getCertificateCdoe())) {
                    item.setCertificateCdoe(Sm4.encryption(item.getCertificateCdoe()));
                }
                if (StringUtils.isNotBlank(item.getMemberPhone())) {
                    item.setMemberPhone(Sm4.encryption(item.getMemberPhone()));
                }
                CustomerMemberEntity memberEntity = new CustomerMemberEntity();
                BeanUtils.copyProperties(item, memberEntity);
                memberEntity.setCustomerId(customerId);
                return memberEntity;
            }).collect(Collectors.toList()));
        }

        List<CustomerVehicleDto> vehicleList = dto.getVehicleList();
        if (CollectionUtils.isNotEmpty(vehicleList)) {
            customerVehicleService.saveBatch(vehicleList.stream().map(item -> {
                if (StringUtils.isNotBlank(item.getCertificateCdoe())) {
                    item.setCertificateCdoe(Sm4.encryption(item.getCertificateCdoe()));
                }
                if (StringUtils.isNotBlank(item.getPhone())) {
                    item.setPhone(Sm4.encryption(item.getPhone()));
                }
                CustomerVehicleEntity vehicleEntity = new CustomerVehicleEntity();
                BeanUtils.copyProperties(item, vehicleEntity);
                vehicleEntity.setCustomerId(customerId);
                return vehicleEntity;
            }).collect(Collectors.toList()));
        }
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomer(Long id, Long companyId) {
        LambdaUpdateWrapper<CustomerEntity> lambdaUpdate = Wrappers.lambdaUpdate();
        lambdaUpdate.set(CustomerEntity::getDeleted, EntityConstants.DELETED);
        lambdaUpdate.eq(CustomerEntity::getId, id);
        lambdaUpdate.eq(CustomerEntity::getCompanyId, companyId);
        int effect = customerMapper.update(null, lambdaUpdate);
        if (effect > 0) {
            //发送MQ消息
            streamMessageSender.sendGenericMessage(
                    MsgBindingNameConstants.ARCHIVE_PRODUCER,
                    SimpleMsg.builder()
                            .delayTimeLevel(2)
                            .operateTag(MsgTagConstants.CUSTOMER_DELETE)
                            .data(null)
                            .dataId(id)
                            .info("删除客户")
                            .build());
        }
    }

    /**
     * 修改状态
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Byte status, Long companyId) {
        LambdaUpdateWrapper<CustomerEntity> lambdaUpdate = Wrappers.lambdaUpdate();
        lambdaUpdate.set(CustomerEntity::getStatus, status);
        lambdaUpdate.eq(CustomerEntity::getId, id);
        lambdaUpdate.eq(CustomerEntity::getCompanyId, companyId);
        customerMapper.update(null, lambdaUpdate);
    }

}
