package cn.cuiot.dmp.lease.mapper.charge;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import cn.cuiot.dmp.lease.entity.charge.TbChargeReceived;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface TbChargeReceivedMapper extends BaseMapper<TbChargeReceived> {

    int insertList(@Param("list")List<TbChargeReceived> list);



}