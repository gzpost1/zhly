package cn.cuiot.dmp.lease.mapper.charge;

import cn.cuiot.dmp.lease.dto.charge.TbChargeStandardQuery;
import cn.cuiot.dmp.lease.entity.charge.TbChargeStandard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbChargeStandardMapper extends BaseMapper<TbChargeStandard> {
    IPage<TbChargeStandard> queryForPage(Page page,@Param("query") TbChargeStandardQuery query);

    List<TbChargeStandard> queryForList(@Param("query")TbChargeStandardQuery query);
}