package cn.cuiot.dmp.archive.application.service;

import static cn.cuiot.dmp.common.utils.DateTimeUtil.DEFAULT_DATE_FORMAT;

import cn.cuiot.dmp.archive.application.constant.CustomerConstants;
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
import cn.cuiot.dmp.archive.infrastructure.entity.HousesArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.CustomerMapper;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.HousesArchivesMapper;
import cn.cuiot.dmp.archive.infrastructure.vo.ArchiveOptionItemVo;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerHouseVo;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerMemberVo;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerVehicleVo;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerVo;
import cn.cuiot.dmp.base.infrastructure.constants.MsgBindingNameConstants;
import cn.cuiot.dmp.base.infrastructure.constants.MsgTagConstants;
import cn.cuiot.dmp.base.infrastructure.stream.StreamMessageSender;
import cn.cuiot.dmp.base.infrastructure.stream.messaging.SimpleMsg;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.CustomerIdentityTypeEnum;
import cn.cuiot.dmp.common.enums.SystemOptionTypeEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.common.utils.Sm4;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
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

    @Autowired
    private ArchiveOptionService archiveOptionService;

    @Autowired
    private HousesArchivesMapper housesArchivesMapper;

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


    /**
     * 解析上传数据
     */
    public List<CustomerDto> analysisImportData(Long currentOrgId, List<String> headers,
            List<List<Object>> dataList) {
        int colSize = headers.size();
        List<CustomerDto> resultList = Lists.newArrayList();
        List<ArchiveOptionItemVo> optionItems = archiveOptionService
                .getArchiveOptionItems(SystemOptionTypeEnum.CUSTOMER_INFO.getCode());
        int houseStartColIndex = CustomerConstants.IDX_CREDIT_LEVEL + 1;
        int row=3;
        for (List<Object> data : dataList) {
            row=row+1;
            CustomerDto dto = new CustomerDto();
            dto.setCompanyId(currentOrgId);
            //客户名称
            String customerName = StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_CUSTOMER_NAME));
            AssertUtil.notBlank(customerName,"导入失败，第"+row+"行，填写的"+headers.get(CustomerConstants.IDX_CUSTOMER_NAME)+"为空");
            dto.setCustomerName(customerName);

            //客户类型
            Long customerType = archiveOptionService.getArchiveOptionItemId(optionItems,
                    CustomerConstants.CONFIG_CUSTOMER_TYPE,
                    StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_CUSTOMER_TYPE)));
            AssertUtil.notNull(customerType,"导入失败，第"+row+"行，填写的"+headers.get(CustomerConstants.IDX_CUSTOMER_TYPE)+"为空或不存在");
            dto.setCustomerType(customerType.toString());

            //公司性质
            if(StringUtils.isNotBlank(StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_COMPANY_NATURE)))){
                Long companyNature = archiveOptionService.getArchiveOptionItemId(optionItems,
                        CustomerConstants.CONFIG_COMPANY_NATURE,
                        StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_COMPANY_NATURE)));
                AssertUtil.notNull(companyNature,"导入失败，第"+row+"行，填写的"+headers.get(CustomerConstants.IDX_COMPANY_NATURE)+"不存在");
                dto.setCompanyNature(companyNature.toString());
            }

            //所属行业
            if(StringUtils.isNotBlank(StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_COMPANY_INDUSTRY)))){
                Long companyIndustry = archiveOptionService.getArchiveOptionItemId(optionItems,
                        CustomerConstants.CONFIG_COMPANY_INDUSTRY,
                        StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_COMPANY_INDUSTRY)));
                AssertUtil.notNull(companyIndustry,"导入失败，第"+row+"行，填写的"+headers.get(CustomerConstants.IDX_COMPANY_INDUSTRY)+"不存在");
                dto.setCompanyIndustry(companyIndustry.toString());
            }

            //证件类型
            if(StringUtils.isNotBlank(StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_CERTIFICATE_TYPE)))){
                Long certificateType = archiveOptionService.getArchiveOptionItemId(optionItems,
                        CustomerConstants.CONFIG_CERTIFICATE_TYPE,
                        StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_CERTIFICATE_TYPE)));
                AssertUtil.notNull(certificateType,"导入失败，第"+row+"行，填写的"+headers.get(CustomerConstants.IDX_CERTIFICATE_TYPE)+"不存在");
                dto.setCertificateType(certificateType.toString());
            }

            //证件号码
            dto.setCertificateCdoe( StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_CERTIFICATE_CDOE)));

            //联系人
            String contactName = StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_CONTACT_NAME));
            AssertUtil.notBlank(contactName,"导入失败，第"+row+"行，填写的"+headers.get(CustomerConstants.IDX_CONTACT_NAME)+"为空");
            dto.setContactName(contactName);

            //联系人手机号
            String contactPhone = StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_CONTACT_PHONE));
            AssertUtil.notBlank(contactPhone,"导入失败，第"+row+"行，填写的"+headers.get(CustomerConstants.IDX_CONTACT_PHONE)+"为空");
            dto.setContactPhone(contactPhone);

            //证件号码
            dto.setEmail( StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_EMAIL)));

            //性别
            if(StringUtils.isNotBlank(StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_SEX)))){
                Long certificateType = archiveOptionService.getArchiveOptionItemId(optionItems,
                        CustomerConstants.CONFIG_SEX,
                        StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_SEX)));
                AssertUtil.notNull(certificateType,"导入失败，第"+row+"行，填写的"+headers.get(CustomerConstants.IDX_SEX)+"不存在");
                dto.setCertificateType(certificateType.toString());
            }

            //客户分类
            if(StringUtils.isNotBlank(StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_CUSTOMER_CATE)))){
                Long customerCate = archiveOptionService.getArchiveOptionItemId(optionItems,
                        CustomerConstants.CONFIG_CUSTOMER_CATE,
                        StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_CUSTOMER_CATE)));
                AssertUtil.notNull(customerCate,"导入失败，第"+row+"行，填写的"+headers.get(CustomerConstants.IDX_CUSTOMER_CATE)+"不存在");
                dto.setCustomerCate(customerCate.toString());
            }

            //客户级别
            if(StringUtils.isNotBlank(StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_CUSTOMER_LEVEL)))){
                Long customerLevel = archiveOptionService.getArchiveOptionItemId(optionItems,
                        CustomerConstants.CONFIG_CUSTOMER_LEVEL,
                        StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_CUSTOMER_LEVEL)));
                AssertUtil.notNull(customerLevel,"导入失败，第"+row+"行，填写的"+headers.get(CustomerConstants.IDX_CUSTOMER_LEVEL)+"不存在");
                dto.setCustomerLevel(customerLevel.toString());
            }

            //信用等级
            if(StringUtils.isNotBlank(StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_CREDIT_LEVEL)))){
                Long creditLevel = archiveOptionService.getArchiveOptionItemId(optionItems,
                        CustomerConstants.CONFIG_CREDIT_LEVEL,
                        StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_CREDIT_LEVEL)));
                AssertUtil.notNull(creditLevel,"导入失败，第"+row+"行，填写的"+headers.get(CustomerConstants.IDX_CREDIT_LEVEL)+"不存在");
                dto.setCreditLevel(creditLevel.toString());
            }
            List<CustomerHouseDto> houseList = Lists.newArrayList();
            while (true){
                if((houseStartColIndex+4)>=colSize){
                    break;
                }
                CustomerHouseDto houseDto = new CustomerHouseDto();
                //房屋ID
                Long houseId = NumberUtil.parseLong(StrUtil.toStringOrNull(data.get(houseStartColIndex)),null);
                AssertUtil.notNull(houseId,"导入失败，第"+row+"行，填写的"+headers.get(houseStartColIndex)+"为空");
                HousesArchivesEntity entity = housesArchivesMapper.selectById(houseId);
                AssertUtil.notNull(entity,"导入失败，第"+row+"行，填写的"+headers.get(houseStartColIndex)+"不存在");
                houseDto.setHouseId(houseId);
                //身份
                String identityTypeName = StrUtil.toStringOrNull(data.get(houseStartColIndex+1));
                AssertUtil.notBlank(identityTypeName,"导入失败，第"+row+"行，填写的"+headers.get(houseStartColIndex+1)+"为空");
                CustomerIdentityTypeEnum identityTypeEnum = CustomerIdentityTypeEnum
                        .parseByName(identityTypeName);
                AssertUtil.notNull(identityTypeEnum,"导入失败，第"+row+"行，填写的"+headers.get(houseStartColIndex+1)+"不存在");
                houseDto.setIdentityType(identityTypeEnum.getCode());

                //迁入日期
                String moveInDateStr = StrUtil.toStringOrNull(data.get(houseStartColIndex+2));
                AssertUtil.notBlank(moveInDateStr,"导入失败，第"+row+"行，填写的"+headers.get(houseStartColIndex+2)+"为空");
                try {
                    houseDto.setMoveInDate(DateTimeUtil.stringToDate(moveInDateStr, DEFAULT_DATE_FORMAT));
                }catch (Exception ex){
                    throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "导入失败，第"+row+"行，填写的"+headers.get(houseStartColIndex+2)+"格式有误");
                }

                //迁出日期
                String moveOutDateStr = StrUtil.toStringOrNull(data.get(houseStartColIndex+3));
                if(StringUtils.isNotBlank(moveOutDateStr)){
                    try {
                        houseDto.setMoveOutDate(DateTimeUtil.stringToDate(moveOutDateStr, DEFAULT_DATE_FORMAT));
                    }catch (Exception ex){
                        throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "导入失败，第"+row+"行，填写的"+headers.get(houseStartColIndex+3)+"格式有误");
                    }
                }
                houseList.add(houseDto);
                houseStartColIndex = houseStartColIndex+4;
            }
            dto.setHouseList(houseList);
            resultList.add(dto);
        }
        return resultList;
    }

    /**
     * 保存导入客户
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveImportCustomers(List<CustomerDto> dtoList) {

    }


}
