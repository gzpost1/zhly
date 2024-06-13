package cn.cuiot.dmp.lease.service.charge;

import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.charge.ChargeHangupQueryDto;
import cn.cuiot.dmp.lease.entity.charge.TbChargeHangup;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuiot.dmp.lease.mapper.charge.TbChargeAbrogateMapper;
import cn.cuiot.dmp.lease.entity.charge.TbChargeAbrogate;

import java.util.Date;

@Service
public class TbChargeAbrogateService extends ServiceImpl<TbChargeAbrogateMapper, TbChargeAbrogate> {

    /**
     * 保存数据
     * @param id
     * @param code
     * @param abrogateDesc
     */
    public void saveData(Long id, Byte code, String abrogateDesc) {
        TbChargeAbrogate tbChargeAbrogate = new TbChargeAbrogate();
        tbChargeAbrogate.setDataId(id);
        tbChargeAbrogate.setDataType(code);
        tbChargeAbrogate.setAbrogateDesc(abrogateDesc);
        tbChargeAbrogate.setCreateUser(LoginInfoHolder.getCurrentUserId());
        tbChargeAbrogate.setAbrogateTime(new Date());
        save(tbChargeAbrogate);
    }

    /**
     * 分页查询
     * @param query
     * @return
     */
    public IPage<TbChargeAbrogate> queryForPage(ChargeHangupQueryDto query) {
        LambdaQueryWrapper<TbChargeAbrogate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TbChargeAbrogate::getDataId, query.getChargeId());
        wrapper.orderByDesc(TbChargeAbrogate::getAbrogateTime);
        return this.page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper);
    }
}
