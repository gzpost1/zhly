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
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
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

        //todo 填充xxljob生成任务

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

        //todo 更新xxl-job任务
    }

    /**
     * 创建计费任务
     *
     * @param list
     */
    @Transactional(rollbackFor = Exception.class)
    public void createChargePlainDayTask(List<TbChargePlain> list) {
        list = list.stream().filter(e -> {
            if (Objects.equals(e.getCronType(), ChargePlainCronType.DAILY)) {
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
                if (Objects.equals(tbChargePlain.getCronType(), ChargePlainCronType.DAILY)) {
                    createDto.setOwnershipPeriodBegin(new Date());
                    createDto.setOwnershipPeriodEnd(new Date());
                } else {
                    createDto.setOwnershipPeriodBegin(DateUtil.beginOfMonth(new Date()));
                    createDto.setOwnershipPeriodEnd(DateUtil.endOfMonth(new Date()));
                }
                createDto.setDueDate(new Date());

                TbChargeManager tbChargeManager = tbChargeManagerService.getTbChargeManager(createDto, ChargeTypeEnum.AUTO_GENERATE.getCode(), tbChargePlain.getId());

                tbChargeManager.setCreateUser(tbChargePlain.getCreateUser());
                tbChargeManager.setCreateTime(new Date());
                tbChargeManager.setDeleted(EntityConstants.NO);
                saveChargeMangeList.add(tbChargeManager);
            }

            tbChargeManagerService.insertList(saveChargeMangeList);
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
        int dayOfMonth = LocalDate.now().getDayOfMonth();
        int endDayOfMonth = DateTimeUtil.dateToLocalDate(DateUtil.endOfMonth(new Date())).getDayOfMonth();

        list = list.stream().filter(e -> {
            if (month >= e.getCronBeginDate() && month <= e.getCronEndDate()) {
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
