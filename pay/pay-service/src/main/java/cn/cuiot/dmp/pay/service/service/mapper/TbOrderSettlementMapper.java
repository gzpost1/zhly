package cn.cuiot.dmp.pay.service.service.mapper;
import org.apache.ibatis.annotations.Param;

import cn.cuiot.dmp.pay.service.service.entity.TbOrderSettlement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface TbOrderSettlementMapper extends BaseMapper<TbOrderSettlement> {
    int insertList(@Param("list")List<TbOrderSettlement> list);


}