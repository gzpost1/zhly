package cn.cuiot.dmp.lease.service.charge;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.TbChargeManager;
import cn.cuiot.dmp.lease.entity.charge.TbChargePlain;
import cn.cuiot.dmp.lease.enums.ChargePlainCronType;
import cn.cuiot.dmp.lease.enums.ChargeTypeEnum;
import cn.cuiot.dmp.lease.mapper.charge.TbChargePlainMapper;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TbChargePlainService extends ServiceImpl<TbChargePlainMapper, TbChargePlain> {
    @Autowired
    private TbChargeManagerService tbChargeManagerService;

    public IPage<ChargePlainPageDto> queryForPage(ChargePlainQuery query) {
        return baseMapper.queryForPage(new Page(query.getPageNo(), query.getPageSize()), query);
    }

    /**
     * 创建数据
     *
     * @param createDto
     */
    @Transactional(rollbackFor = Exception.class)
    public void createData(ChargePlainInsertDto createDto) {
        TbChargePlain tbChargePlain = new TbChargePlain();
        BeanUtils.copyProperties(createDto, tbChargePlain);
        tbChargePlain.setId(IdWorker.getId());
        tbChargePlain.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        tbChargePlain.setStatus(EntityConstants.ENABLED);
        baseMapper.insert(tbChargePlain);
    }

    /**
     * 更新自动计划
     *
     * @param updateDto
     */
    public void updateData(ChargePlainUpdateDto updateDto) {
        //1 根据ID查询并判断
        TbChargePlain tbChargePlain = baseMapper.selectById(updateDto.getId());
        AssertUtil.notNull(tbChargePlain, "数据不存在");
        BeanUtils.copyProperties(updateDto, tbChargePlain);
        this.updateById(tbChargePlain);
    }

    /**
     * 创建计费任务
     *
     * @param list
     */
    @Transactional(rollbackFor = Exception.class)
    public void createChargePlainDayTask(List<TbChargePlain> list) {
        list = list.stream().filter(e -> {
            if (Objects.equals(e.getCronType(), ChargePlainCronType.DAILY.getCode())) {
                return true;
            } else {
                int dayOfMonth = LocalDate.now().getDayOfMonth();
                if (e.getCronAppointDate() == dayOfMonth) {
                    return true;
                }
                int endDay = DateTimeUtil.dateToLocalDate(DateUtil.endOfMonth(new Date())).getDayOfMonth();
                if (dayOfMonth == endDay && e.getCronAppointDate() > dayOfMonth) {
                    return true;
                }
                return false;
            }
        }).collect(Collectors.toList());

        getAndInsertChargeManager(list);
    }

    /**
     * 插入收费管理
     *
     * @param list
     */
    private void getAndInsertChargeManager(List<TbChargePlain> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            List<TbChargeManager> saveChargeMangeList = new ArrayList<>();

            for (TbChargePlain tbChargePlain : list) {
                ChargeManagerInsertVo createDto = new ChargeManagerInsertVo();
                createDto.setReceivableAmountRate(tbChargePlain.getReceivableAmountRate());
                createDto.setReceivableAmount(tbChargePlain.getReceivableAmount());
                createDto.setChargeItemId(tbChargePlain.getChargeItemId());
                createDto.setChargeStandard(EntityConstants.NO);
                createDto.setCompanyId(tbChargePlain.getCompanyId());
                createDto.setChargeType(ChargeTypeEnum.AUTO_GENERATE.getCode());
                if (Objects.equals(tbChargePlain.getCronType(), ChargePlainCronType.DAILY.getCode())) {
                    createDto.setOwnershipPeriodBegin(new Date());
                    createDto.setOwnershipPeriodEnd(new Date());
                    createDto.setDueDate(new Date());

                } else if (Objects.equals(tbChargePlain.getCronType(), ChargePlainCronType.MONTHLY.getCode())) {
                    //每月
                    createDto.setOwnershipPeriodBegin(DateUtil.beginOfMonth(new Date()));
                    createDto.setOwnershipPeriodEnd(DateTimeUtil.getEndTime(DateUtil.endOfMonth(new Date())));
                    createDto.setDueDateNum(tbChargePlain.getDueDateNum());

                    makeChargeByPlan(tbChargePlain, createDto, saveChargeMangeList);

                } else {
                    //指定日期
                    DateTime dateTime = DateUtil.beginOfMonth(new Date());
                    for (int i = 0; i < (Integer.valueOf(tbChargePlain.getCronEndDate() )- Integer.valueOf(tbChargePlain.getCronBeginDate())); i++) {
                        ChargeManagerInsertVo appointCreate = new ChargeManagerInsertVo();
                        BeanUtils.copyProperties(createDto, appointCreate);
                        appointCreate.setOwnershipPeriodBegin(DateUtil.beginOfMonth(DateUtil.offsetMonth(dateTime, i)));
                        appointCreate.setOwnershipPeriodEnd(DateUtil.endOfMonth(appointCreate.getOwnershipPeriodBegin()));
                        createDto.setDueDateNum(tbChargePlain.getDueDateNum());

                        makeChargeByPlan(tbChargePlain, createDto, saveChargeMangeList);
                    }
                }

            }

            for (List<TbChargeManager> chargeManagers : Lists.partition(saveChargeMangeList, 100)) {
                tbChargeManagerService.insertList(chargeManagers);
            }
        }
    }

    private void makeChargeByPlan(TbChargePlain tbChargePlain, ChargeManagerInsertVo createDto, List<TbChargeManager> saveChargeMangeList) {
        //查询房屋客户并构建收费明细
        HouseCustomerQuery houseCustomerQuery = new HouseCustomerQuery();
        houseCustomerQuery.setLoupanId(tbChargePlain.getReceivableObj());
        long pageNum = 1L;
        IPage<CustomerUserInfo> customerUserInfoIPage = null;
        do {
            houseCustomerQuery.setPageNo(pageNum);
            customerUserInfoIPage = tbChargeManagerService.queryHouseCustmerPage(houseCustomerQuery);
            if (Objects.nonNull(customerUserInfoIPage) && CollectionUtils.isNotEmpty(customerUserInfoIPage.getRecords())) {
                for (CustomerUserInfo record : customerUserInfoIPage.getRecords()) {
                    TbChargeManager tbChargeManager = tbChargeManagerService.getTbChargeManager(createDto, ChargeTypeEnum.AUTO_GENERATE.getCode(), tbChargePlain.getId());
                    tbChargeManager.setCustomerUserId(record.getCustomerUserId());
                    tbChargeManager.setCreateUser(tbChargePlain.getCreateUser());
                    tbChargeManager.setCreateTime(new Date());
                    tbChargeManager.setDeleted(EntityConstants.NO);
                    tbChargeManager.setHouseId(record.getHouseId());
                    tbChargeManager.setLoupanId(tbChargePlain.getReceivableObj());

                    //如果是指定日期需要重新计算应收日期
                    if (Objects.nonNull(createDto.getDueDateNum()) && createDto.getDueDateNum() > 0) {
                        tbChargeManagerService.setDueDate(createDto.getDueDateNum(),tbChargeManager);
                    }
                    saveChargeMangeList.add(tbChargeManager);
                }
            }
            pageNum++;
        } while (Objects.nonNull(customerUserInfoIPage) && CollectionUtils.isNotEmpty(customerUserInfoIPage.getRecords()));

        if (CollectionUtils.isNotEmpty(saveChargeMangeList) && saveChargeMangeList.size() > 500) {
            for (List<TbChargeManager> chargeManagers : Lists.partition(saveChargeMangeList, 100)) {
                tbChargeManagerService.insertList(chargeManagers);
            }
            saveChargeMangeList.clear();
        }
    }

    /**
     * 指定日期创建
     *
     * @param list
     */
    @Transactional(rollbackFor = Exception.class)
    public void createChargePlainMonthTask(List<TbChargePlain> list) {
        int month = LocalDate.now().getMonth().getValue();
        int year = LocalDate.now().getYear();
        int dayOfMonth = LocalDate.now().getDayOfMonth();
        int endDayOfMonth = DateTimeUtil.dateToLocalDate(DateUtil.endOfMonth(new Date())).getDayOfMonth();


        list = list.stream().filter(e -> {
            String[] split = StringUtils.split(e.getCronBeginDate(), "-");
            if (month == Integer.valueOf(split[1]) && year == Integer.valueOf(split[0])) {
                if (dayOfMonth == e.getCronAppointDate()) {
                    return true;
                }
                if (dayOfMonth == endDayOfMonth && e.getCronAppointDate() > dayOfMonth) {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());

        //生成收费管理
        getAndInsertChargeManager(list);
    }
}
