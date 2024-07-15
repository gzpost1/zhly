package cn.cuiot.dmp.archive.application.service;

import static cn.cuiot.dmp.common.utils.DateTimeUtil.DEFAULT_DATE_FORMAT;
import static cn.cuiot.dmp.common.utils.StreamUtil.distinctByKey;

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
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerExportVo;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerHouseExportVo;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerHouseVo;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerMemberVo;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerVehicleVo;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerVo;
import cn.cuiot.dmp.archive.infrastructure.vo.HouseCustomerVo;
import cn.cuiot.dmp.base.infrastructure.constants.MsgBindingNameConstants;
import cn.cuiot.dmp.base.infrastructure.constants.MsgTagConstants;
import cn.cuiot.dmp.base.infrastructure.dto.req.CustomerUseReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CustomerUserRspDto;
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
import java.util.Map;
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
                .excludeId(query.getExcludeId())
                .id(query.getId())
                .keyword(query.getKeyword())
                .customerName(query.getCustomerName())
                .contactName(query.getContactName())
                .status(query.getStatus())
                .houseId(query.getHouseId())
                .loupanId(query.getLoupanId())
                .build();
        if(StringUtils.isNotBlank(query.getKeyword())){
            criteriaQuery.setKeywordPhone(Sm4.encryption(query.getKeyword()));
        }
        if(StringUtils.isNotBlank(query.getContactPhone())){
            criteriaQuery.setContactPhone(Sm4.encryption(query.getContactPhone()));
        }
        IPage<CustomerVo> page = customerMapper
                .queryForList(new Page<CustomerVo>(query.getPageNo(), query.getPageSize()),
                        criteriaQuery);
        List<Long> customerIdList = Optional.ofNullable(page.getRecords())
                .orElse(Lists.newArrayList())
                .stream().map(ite -> ite.getId()).collect(
                        Collectors.toList());
        if (CollectionUtils.isNotEmpty(customerIdList)) {
            for (CustomerVo customerVo : page.getRecords()) {
                if(StringUtils.isNotBlank(customerVo.getContactPhone())){
                    customerVo.setContactPhone(Sm4.decrypt(customerVo.getContactPhone()));
                }
                if(StringUtils.isNotBlank(customerVo.getCertificateCdoe())){
                    customerVo.setCertificateCdoe(Sm4.decrypt(customerVo.getCertificateCdoe()));
                }
            }
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

        //下拉回显
        if(Objects.nonNull(query.getIncludeId())){
            CustomerQuery ncludeQuery = new CustomerQuery();
            ncludeQuery.setId(query.getIncludeId());
            List<CustomerVo> dataList = queryForList(query);
            if(CollectionUtils.isNotEmpty(dataList)){
                CustomerVo customerVo = dataList.get(0);
                if(!page.getRecords().stream().filter(item->item.getId().equals(customerVo.getId())).findFirst().isPresent()){
                    page.getRecords().add(0,customerVo);
                }
            }
        }

        return page;
    }

    /**
     * 列表查询
     */
    public List<CustomerVo> queryForList(CustomerQuery query) {
        CustomerCriteriaQuery criteriaQuery = CustomerCriteriaQuery.builder()
                .companyId(query.getCompanyId())
                .id(query.getId())
                .excludeId(query.getExcludeId())
                .keyword(query.getKeyword())
                .customerName(query.getCustomerName())
                .contactName(query.getContactName())
                .status(query.getStatus())
                .houseId(query.getHouseId())
                .loupanId(query.getLoupanId())
                .build();
        if(StringUtils.isNotBlank(query.getKeyword())){
            criteriaQuery.setKeywordPhone(Sm4.encryption(query.getKeyword()));
        }
        if(StringUtils.isNotBlank(query.getContactPhone())){
            criteriaQuery.setContactPhone(Sm4.encryption(query.getContactPhone()));
        }

        List<CustomerVo> selectList = customerMapper
                .queryForList(criteriaQuery);
        List<Long> customerIdList = Optional.ofNullable(selectList)
                .orElse(Lists.newArrayList())
                .stream().map(ite -> ite.getId()).collect(
                        Collectors.toList());
        if (CollectionUtils.isNotEmpty(customerIdList)) {
            for (CustomerVo customerVo : selectList) {
                if(StringUtils.isNotBlank(customerVo.getContactPhone())){
                    customerVo.setContactPhone(Sm4.decrypt(customerVo.getContactPhone()));
                }
                if(StringUtils.isNotBlank(customerVo.getCertificateCdoe())){
                    customerVo.setCertificateCdoe(Sm4.decrypt(customerVo.getCertificateCdoe()));
                }
            }
            List<CustomerHouseVo> houseList = customerHouseService
                    .selectByCustomerId(customerIdList);
            if (CollectionUtils.isNotEmpty(houseList)) {
                for (CustomerVo customerVo : selectList) {
                    customerVo.setHouseList(houseList.stream()
                            .filter(ite -> customerVo.getId().equals(ite.getCustomerId())).collect(
                                    Collectors.toList()));
                }
            }
        }
        return selectList;
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

            if(StringUtils.isNotBlank(customerVo.getContactPhone())){
                customerVo.setContactPhone(Sm4.decrypt(customerVo.getContactPhone()));
            }
            if(StringUtils.isNotBlank(customerVo.getCertificateCdoe())){
                customerVo.setCertificateCdoe(Sm4.decrypt(customerVo.getCertificateCdoe()));
            }

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
        //entity.setAttachments(dto.getAttachments());
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
        //entity.setAttachments(dto.getAttachments());
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

            if(houseList.size()!=houseList.stream().filter(distinctByKey(CustomerHouseDto::getHouseId))
                    .collect(Collectors.toList()).size()){
                throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,
                        "操作失败，一套房屋仅可被一个客户绑定一次");
            }

            Map<String, Long> houseMap = getHouseMap(
                    houseList.stream().map(ite -> ite.getHouseId()).collect(Collectors.toList()));
            customerHouseService.saveBatch(houseList.stream().map(item -> {
                CustomerHouseEntity houseEntity = new CustomerHouseEntity();
                BeanUtils.copyProperties(item, houseEntity);
                houseEntity.setCustomerId(customerId);
                houseEntity.setLoupanId(houseMap.get(item.getHouseId().toString()));
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
     * 获得房屋楼盘的对应
     * @param houseIdList
     * @return
     */
    private Map<String,Long> getHouseMap(List<Long> houseIdList){
        List<HousesArchivesEntity> selectList = housesArchivesMapper.selectList(
                Wrappers.<HousesArchivesEntity>lambdaQuery()
                        .in(HousesArchivesEntity::getId, houseIdList));
        Map<String,Long> map = Optional.ofNullable(selectList).orElse(Lists.newArrayList())
                .stream().collect(Collectors
                        .toMap(item->item.getId().toString(), HousesArchivesEntity::getLoupanId));
        return map;
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

        int row = 3;
        for (List<Object> data : dataList) {
            row = row + 1;
            CustomerDto dto = new CustomerDto();
            dto.setCompanyId(currentOrgId);
            //客户名称
            String customerName = StrUtil
                    .toStringOrNull(data.get(CustomerConstants.IDX_CUSTOMER_NAME));
            AssertUtil.notBlank(customerName,
                    "导入失败，第" + row + "行，填写的" + headers.get(CustomerConstants.IDX_CUSTOMER_NAME)
                            + "为空");
            dto.setCustomerName(customerName);

            //客户类型
            Long customerType = archiveOptionService.getArchiveOptionItemId(optionItems,
                    CustomerConstants.CONFIG_CUSTOMER_TYPE,
                    StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_CUSTOMER_TYPE)));
            AssertUtil.notNull(customerType,
                    "导入失败，第" + row + "行，填写的" + headers.get(CustomerConstants.IDX_CUSTOMER_TYPE)
                            + "为空或不存在");
            dto.setCustomerType(customerType.toString());

            //公司性质
            if (StringUtils.isNotBlank(
                    StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_COMPANY_NATURE)))) {
                Long companyNature = archiveOptionService.getArchiveOptionItemId(optionItems,
                        CustomerConstants.CONFIG_COMPANY_NATURE,
                        StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_COMPANY_NATURE)));
                AssertUtil.notNull(companyNature,
                        "导入失败，第" + row + "行，填写的" + headers.get(CustomerConstants.IDX_COMPANY_NATURE)
                                + "不存在");
                dto.setCompanyNature(companyNature.toString());
            }

            //所属行业
            if (StringUtils.isNotBlank(
                    StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_COMPANY_INDUSTRY)))) {
                Long companyIndustry = archiveOptionService.getArchiveOptionItemId(optionItems,
                        CustomerConstants.CONFIG_COMPANY_INDUSTRY,
                        StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_COMPANY_INDUSTRY)));
                AssertUtil.notNull(companyIndustry, "导入失败，第" + row + "行，填写的" + headers
                        .get(CustomerConstants.IDX_COMPANY_INDUSTRY) + "不存在");
                dto.setCompanyIndustry(companyIndustry.toString());
            }

            //证件类型
            if (StringUtils.isNotBlank(
                    StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_CERTIFICATE_TYPE)))) {
                Long certificateType = archiveOptionService.getArchiveOptionItemId(optionItems,
                        CustomerConstants.CONFIG_CERTIFICATE_TYPE,
                        StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_CERTIFICATE_TYPE)));
                AssertUtil.notNull(certificateType, "导入失败，第" + row + "行，填写的" + headers
                        .get(CustomerConstants.IDX_CERTIFICATE_TYPE) + "不存在");
                dto.setCertificateType(certificateType.toString());
            }

            //证件号码
            dto.setCertificateCdoe(
                    StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_CERTIFICATE_CDOE)));

            //联系人
            String contactName = StrUtil
                    .toStringOrNull(data.get(CustomerConstants.IDX_CONTACT_NAME));
            AssertUtil.notBlank(contactName,
                    "导入失败，第" + row + "行，填写的" + headers.get(CustomerConstants.IDX_CONTACT_NAME)
                            + "为空");
            dto.setContactName(contactName);

            //联系人手机号
            String contactPhone = StrUtil
                    .toStringOrNull(data.get(CustomerConstants.IDX_CONTACT_PHONE));
            AssertUtil.notBlank(contactPhone,
                    "导入失败，第" + row + "行，填写的" + headers.get(CustomerConstants.IDX_CONTACT_PHONE)
                            + "为空");
            dto.setContactPhone(contactPhone);

            //证件号码
            dto.setEmail(StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_EMAIL)));

            //性别
            if (StringUtils
                    .isNotBlank(StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_SEX)))) {
                Long sexId = archiveOptionService.getArchiveOptionItemId(optionItems,
                        CustomerConstants.CONFIG_SEX,
                        StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_SEX)));
                AssertUtil.notNull(sexId,
                        "导入失败，第" + row + "行，填写的" + headers.get(CustomerConstants.IDX_SEX) + "不存在");
                dto.setSex(sexId.toString());
            }

            //客户分类
            if (StringUtils.isNotBlank(
                    StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_CUSTOMER_CATE)))) {
                Long customerCate = archiveOptionService.getArchiveOptionItemId(optionItems,
                        CustomerConstants.CONFIG_CUSTOMER_CATE,
                        StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_CUSTOMER_CATE)));
                AssertUtil.notNull(customerCate,
                        "导入失败，第" + row + "行，填写的" + headers.get(CustomerConstants.IDX_CUSTOMER_CATE)
                                + "不存在");
                dto.setCustomerCate(customerCate.toString());
            }

            //客户级别
            if (StringUtils.isNotBlank(
                    StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_CUSTOMER_LEVEL)))) {
                Long customerLevel = archiveOptionService.getArchiveOptionItemId(optionItems,
                        CustomerConstants.CONFIG_CUSTOMER_LEVEL,
                        StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_CUSTOMER_LEVEL)));
                AssertUtil.notNull(customerLevel,
                        "导入失败，第" + row + "行，填写的" + headers.get(CustomerConstants.IDX_CUSTOMER_LEVEL)
                                + "不存在");
                dto.setCustomerLevel(customerLevel.toString());
            }

            //信用等级
            if (StringUtils.isNotBlank(
                    StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_CREDIT_LEVEL)))) {
                Long creditLevel = archiveOptionService.getArchiveOptionItemId(optionItems,
                        CustomerConstants.CONFIG_CREDIT_LEVEL,
                        StrUtil.toStringOrNull(data.get(CustomerConstants.IDX_CREDIT_LEVEL)));
                AssertUtil.notNull(creditLevel,
                        "导入失败，第" + row + "行，填写的" + headers.get(CustomerConstants.IDX_CREDIT_LEVEL)
                                + "不存在");
                dto.setCreditLevel(creditLevel.toString());
            }
            List<CustomerHouseDto> houseList = Lists.newArrayList();
            int houseStartColIndex = CustomerConstants.IDX_CREDIT_LEVEL + 1;
            while (true) {
                if ((houseStartColIndex + 4) >= colSize) {
                    break;
                }
                CustomerHouseDto houseDto = new CustomerHouseDto();
                //房屋ID
                Long houseId = NumberUtil
                        .parseLong(StrUtil.toStringOrNull(data.get(houseStartColIndex)), null);
                //身份
                String identityTypeName = StrUtil.toStringOrNull(data.get(houseStartColIndex + 1));

                //迁入日期
                String moveInDateStr = StrUtil.toStringOrNull(data.get(houseStartColIndex + 2));

                //迁出日期
                String moveOutDateStr = StrUtil.toStringOrNull(data.get(houseStartColIndex + 3));

                if(StringUtils.isNotBlank(identityTypeName)||StringUtils.isNotBlank(moveInDateStr)||StringUtils.isNotBlank(moveOutDateStr)){
                    AssertUtil.notNull(houseId,"导入失败，第" + row + "行，填写的" + headers.get(houseStartColIndex + 1) + "为空");
                }

                if(Objects.nonNull(houseId)){
                    HousesArchivesEntity entity = housesArchivesMapper.selectById(houseId);
                    AssertUtil.notNull(entity,
                            "导入失败，第" + row + "行，填写的" + headers.get(houseStartColIndex) + "不存在");
                    houseDto.setHouseId(houseId);

                    AssertUtil.notBlank(identityTypeName,
                            "导入失败，第" + row + "行，填写的" + headers.get(houseStartColIndex + 1) + "为空");
                    CustomerIdentityTypeEnum identityTypeEnum = CustomerIdentityTypeEnum
                            .parseByName(identityTypeName);
                    AssertUtil.notNull(identityTypeEnum,
                            "导入失败，第" + row + "行，填写的" + headers.get(houseStartColIndex + 1) + "不存在");
                    houseDto.setIdentityType(identityTypeEnum.getCode());

                    AssertUtil.notBlank(moveInDateStr,
                            "导入失败，第" + row + "行，填写的" + headers.get(houseStartColIndex + 2) + "为空");
                    try {
                        houseDto.setMoveInDate(
                                DateTimeUtil.stringToDate(moveInDateStr, DEFAULT_DATE_FORMAT));
                    } catch (Exception ex) {
                        throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,
                                "导入失败，第" + row + "行，填写的" + headers.get(houseStartColIndex + 2)
                                        + "格式有误");
                    }
                    if (StringUtils.isNotBlank(moveOutDateStr)) {
                        try {
                            houseDto.setMoveOutDate(
                                    DateTimeUtil.stringToDate(moveOutDateStr, DEFAULT_DATE_FORMAT));
                        } catch (Exception ex) {
                            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,
                                    "导入失败，第" + row + "行，填写的" + headers.get(houseStartColIndex + 3)
                                            + "格式有误");
                        }
                    }
                    houseList.add(houseDto);
                }

                houseStartColIndex = houseStartColIndex + 4;
            }
            AssertUtil.notEmpty(houseList,"导入失败，第" + row + "行，必须填写一个关联房屋信息");
            //根据房屋ID去重
            houseList = houseList.stream().filter(distinctByKey(CustomerHouseDto::getHouseId))
                    .collect(Collectors.toList());
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
        if (CollectionUtils.isNotEmpty(dtoList)) {
            for (CustomerDto dto : dtoList) {
                if (StringUtils.isNotBlank(dto.getContactPhone())) {
                    dto.setContactPhone(Sm4.encryption(dto.getContactPhone()));
                }
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
        }
    }

    /**
     * 获得客户信息选项列表
     * @return
     */
    public List<ArchiveOptionItemVo> getCustomerOptionItems(){
        List<ArchiveOptionItemVo> optionItems = archiveOptionService
                .getArchiveOptionItems(SystemOptionTypeEnum.CUSTOMER_INFO.getCode());
        return optionItems;
    }

    /**
     * 构建导出数据
     * @param dataList
     * @return
     */
    public List<CustomerExportVo> buildExportData(List<CustomerVo> dataList,List<ArchiveOptionItemVo> optionItems) {
        List<CustomerExportVo> resultList = Lists.newArrayList();
        for(CustomerVo vo:dataList){
            CustomerExportVo exportVo = new CustomerExportVo();
            exportVo.setCustomerName(vo.getCustomerName());
            exportVo.setCustomerId(vo.getId().toString());
            exportVo.setCustomerTypeName(getItemName(optionItems,parseLong(vo.getCustomerType())));
            exportVo.setCompanyNatureName(getItemName(optionItems,parseLong(vo.getCompanyNature())));
            exportVo.setCompanyIndustryName(getItemName(optionItems,parseLong(vo.getCompanyIndustry())));
            exportVo.setCertificateTypeName(getItemName(optionItems,parseLong(vo.getCertificateType())));
            exportVo.setCertificateCdoe(vo.getCertificateCdoe());
            exportVo.setContactName(vo.getContactName());
            exportVo.setContactPhone(vo.getContactPhone());
            exportVo.setEmail(vo.getEmail());
            exportVo.setSexName(getItemName(optionItems,parseLong(vo.getSex())));
            exportVo.setCustomerCateName(getItemName(optionItems,parseLong(vo.getCustomerCate())));
            exportVo.setCustomerLevelName(getItemName(optionItems,parseLong(vo.getCustomerLevel())));
            exportVo.setCreditLevelName(getItemName(optionItems,parseLong(vo.getCreditLevel())));
            resultList.add(exportVo);
        }
        return resultList;
    }

    /**
     * 转换为Long类型
     */
    private Long parseLong(String idStr){
        if(StringUtils.isBlank(idStr)){
            return null;
        }
        return Long.valueOf(idStr);
    }

    /**
     * 获得选项名
     */
    private String getItemName(List<ArchiveOptionItemVo> list, Long itemId) {
        if (CollectionUtils.isEmpty(list) || Objects.isNull(itemId)) {
            return null;
        }
        Optional<ArchiveOptionItemVo> findOptional = list.stream()
                .filter(item -> item.getItemId().equals(itemId)).findFirst();
        if (findOptional.isPresent()) {
            return findOptional.get().getItemName();
        }
        return null;
    }

    /**
     * 构建导出数据
     * @param houseList
     * @return
     */
    public List<CustomerHouseExportVo> buildHouseExportData(List<CustomerHouseVo> houseList,List<ArchiveOptionItemVo> optionItems) {
        List<CustomerHouseExportVo> resultList = Lists.newArrayList();
        for(CustomerHouseVo vo:houseList){
            CustomerHouseExportVo exportVo = new CustomerHouseExportVo();
            exportVo.setHouseId(vo.getHouseId());
            if(StringUtils.isNotBlank(vo.getIdentityType())){
                CustomerIdentityTypeEnum identityTypeEnum = CustomerIdentityTypeEnum
                        .parseByCode(vo.getIdentityType());
                if(Objects.nonNull(identityTypeEnum)){
                    exportVo.setIdentityTypeName(identityTypeEnum.getName());
                }
            }
            if(Objects.nonNull(vo.getMoveInDate())){
                exportVo.setMoveInDateStr(DateTimeUtil.dateToString(vo.getMoveInDate(),DEFAULT_DATE_FORMAT));
            }
            if(Objects.nonNull(vo.getMoveOutDate())){
                exportVo.setMoveOutDateStr(DateTimeUtil.dateToString(vo.getMoveOutDate(),DEFAULT_DATE_FORMAT));
            }
            resultList.add(exportVo);
        }
        return resultList;
    }

    /**
     * 查询客户
     */
    public List<CustomerUserRspDto> lookupCustomerUsers(CustomerUseReqDto reqDto) {
        List<CustomerUserRspDto> list = customerMapper.lookupCustomerUsers(reqDto);
        if (CollectionUtils.isNotEmpty(list)) {
            for(CustomerUserRspDto dto:list){
                if(StringUtils.isNotBlank(dto.getContactPhone())){
                    dto.setContactPhone(Sm4.decrypt(dto.getContactPhone()));
                }
            }
        }
        return list;
    }

    /**
     * 房屋关联客户列表
     */
    public IPage<HouseCustomerVo> queryHouseCustomerList(CustomerQuery query) {
        return customerHouseService.queryHouseCustomerList(query);
    }

}
