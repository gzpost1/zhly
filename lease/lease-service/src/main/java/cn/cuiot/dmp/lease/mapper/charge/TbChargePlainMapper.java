package cn.cuiot.dmp.lease.mapper.charge;

import cn.cuiot.dmp.lease.dto.charge.ChargePlainPageDto;
import cn.cuiot.dmp.lease.dto.charge.ChargePlainQuery;
import cn.cuiot.dmp.lease.entity.charge.TbChargePlain;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

public interface TbChargePlainMapper extends BaseMapper<TbChargePlain> {
    IPage<ChargePlainPageDto> queryForPage(Page page, @Param("query") ChargePlainQuery query);
}