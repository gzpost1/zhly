package cn.cuiot.dmp.lease.service.charge;

import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.charge.ChargeHangupQueryDto;
import cn.cuiot.dmp.lease.entity.charge.TbChargeHangup;
import cn.cuiot.dmp.lease.mapper.charge.TbChargeHangupMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TbChargeHangupService extends ServiceImpl<TbChargeHangupMapper, TbChargeHangup> {

    public void saveData(Long id, Byte hangUpStatus, String abrogateDesc) {
        TbChargeHangup tbChargeHangup = new TbChargeHangup();
        tbChargeHangup.setId(IdWorker.getId());
        tbChargeHangup.setHangupTime(new Date());
        tbChargeHangup.setHangupDesc(abrogateDesc);
        tbChargeHangup.setCreateUser(LoginInfoHolder.getCurrentUserId());
        tbChargeHangup.setDataId(id);
        tbChargeHangup.setDataType(hangUpStatus);
        save(tbChargeHangup);
    }

    public IPage<TbChargeHangup> queryForPage(ChargeHangupQueryDto query) {
        LambdaQueryWrapper<TbChargeHangup> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TbChargeHangup::getDataId, query.getChargeId());
        wrapper.orderByDesc(TbChargeHangup::getHangupTime);
        return this.page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper);
    }
}
