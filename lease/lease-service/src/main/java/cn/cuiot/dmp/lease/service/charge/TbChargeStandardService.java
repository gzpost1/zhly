package cn.cuiot.dmp.lease.service.charge;

import cn.cuiot.dmp.lease.dto.charge.TbChargeStandardQuery;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuiot.dmp.lease.entity.charge.TbChargeStandard;
import cn.cuiot.dmp.lease.mapper.charge.TbChargeStandardMapper;
@Service
public class TbChargeStandardService extends ServiceImpl<TbChargeStandardMapper, TbChargeStandard> {

    public IPage<TbChargeStandard> queryForPage(TbChargeStandardQuery query) {
        return baseMapper.queryForPage(new Page(query.getPageNo(), query.getPageSize()), query);
    }

    public List<TbChargeStandard> queryForList(TbChargeStandardQuery query) {
        return baseMapper.queryForList(query);
    }
}
