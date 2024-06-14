package cn.cuiot.dmp.lease.service.charge;

import cn.cuiot.dmp.lease.dto.charge.SecuritydepositManagerPageDto;
import cn.cuiot.dmp.lease.dto.charge.SecuritydepositManagerQuery;
import cn.cuiot.dmp.lease.enums.ChargeAbrogateEnum;
import cn.cuiot.dmp.lease.enums.ChargeAbrogateTypeEnum;
import cn.cuiot.dmp.lease.enums.SecurityDepositStatusEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuiot.dmp.lease.mapper.charge.TbSecuritydepositManagerMapper;
import cn.cuiot.dmp.lease.entity.charge.TbSecuritydepositManager;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TbSecuritydepositManagerService extends ServiceImpl<TbSecuritydepositManagerMapper, TbSecuritydepositManager> {
    @Autowired
    private TbChargeAbrogateService tbChargeAbrogateService;

    public IPage<SecuritydepositManagerPageDto> queryForPage(SecuritydepositManagerQuery query) {
        return baseMapper.queryForPage(new Page(query.getPageNo(), query.getPageSize()), query);
    }

    /**
     * 作废
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
}
