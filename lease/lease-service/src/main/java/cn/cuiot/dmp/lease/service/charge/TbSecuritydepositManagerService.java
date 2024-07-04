package cn.cuiot.dmp.lease.service.charge;

import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.TbChargeAbrogate;
import cn.cuiot.dmp.lease.entity.charge.TbSecuritydepositManager;
import cn.cuiot.dmp.lease.entity.charge.TbSecuritydepositRefund;
import cn.cuiot.dmp.lease.enums.ChargeAbrogateTypeEnum;
import cn.cuiot.dmp.lease.enums.SecurityDepositStatusEnum;
import cn.cuiot.dmp.lease.mapper.charge.TbSecuritydepositManagerMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class TbSecuritydepositManagerService extends ServiceImpl<TbSecuritydepositManagerMapper, TbSecuritydepositManager> {
    @Autowired
    private TbChargeAbrogateService tbChargeAbrogateService;
    @Autowired
    private TbSecuritydepositRefundService securitydepositRefundService;


    public IPage<SecuritydepositManagerPageDto> queryForPage(SecuritydepositManagerQuery query) {
        return baseMapper.queryForPage(new Page(query.getPageNo(), query.getPageSize()), query);
    }

    /**
     * 作废
     *
     * @param entity
     * @param abrogateDesc
     */
    @Transactional(rollbackFor = Exception.class)
    public void abrogateStatus(TbSecuritydepositManager entity, String abrogateDesc) {
        // 作废
        entity.setStatus(SecurityDepositStatusEnum.CANCELLED.getCode());
        updateById(entity);

        // 作废记录
        tbChargeAbrogateService.saveData(entity.getId(), ChargeAbrogateTypeEnum.DEPOSIT.getCode(), abrogateDesc);
    }

    /**
     * 退款
     *
     * @param dto
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    public void refund( SecuritydepositRefundDto dto) {
        // 退款
        int updateNum = baseMapper.refund(dto);
        AssertUtil.isTrue(updateNum == 1, "退款失败");

        //保存退款记录
        TbSecuritydepositRefund refund = new TbSecuritydepositRefund();
        BeanUtils.copyProperties(dto, refund);
        refund.setId(IdWorker.getId());
        refund.setRefundTime(new Date());
        securitydepositRefundService.save(refund);
    }

    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    public SecuritydepositManagerDto queryForDetail(Long id) {
        TbSecuritydepositManager entity = this.getById(id);
        AssertUtil.notNull(entity, "数据不存在");
        SecuritydepositManagerDto dto = new SecuritydepositManagerDto();
        BeanUtils.copyProperties(entity, dto);

        //查询作废明细
        LambdaQueryWrapper<TbChargeAbrogate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TbChargeAbrogate::getDataId, id);
        wrapper.eq(TbChargeAbrogate::getDataType, ChargeAbrogateTypeEnum.DEPOSIT.getCode());
        wrapper.orderByDesc(TbChargeAbrogate::getAbrogateTime);
        dto.setAbrogateList(tbChargeAbrogateService.list(wrapper));

        //查询退款明细
        LambdaQueryWrapper<TbSecuritydepositRefund> refundWrapper = new LambdaQueryWrapper<>();
        refundWrapper.eq(TbSecuritydepositRefund::getDepositId, id);
        refundWrapper.orderByDesc(TbSecuritydepositRefund::getRefundTime);
        dto.setSecuritydepositRefundList(securitydepositRefundService.list(refundWrapper));
        return dto;
    }

    public Integer getHouseReundableAmount(Long houseId) {
        return Optional.ofNullable(baseMapper.getHouseReundableAmount(houseId)).orElse(0);
    }

    /**
     * 押金保存
     * @param createDto
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveData(SecuritydepositManagerInsertDto createDto) {
        TbSecuritydepositManager entity = new TbSecuritydepositManager();
        BeanUtils.copyProperties(createDto,entity);
        entity.setId(IdWorker.getId());
        entity.setStatus(SecurityDepositStatusEnum.UNPAID.getCode());
        entity.setReceivableAmountReceived(0);
        entity.setReturnedAmount(0);
        this.save(entity);
    }
}
