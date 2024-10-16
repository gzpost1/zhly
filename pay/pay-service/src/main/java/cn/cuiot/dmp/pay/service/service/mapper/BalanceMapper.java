package cn.cuiot.dmp.pay.service.service.mapper;

import cn.cuiot.dmp.pay.service.service.entity.BalanceEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 余额 Mapper 接口
 */
public interface BalanceMapper extends BaseMapper<BalanceEntity> {

    @Update("update tb_house_balance set balance=balance + #{record.balance},version = version + 1,update_time=now(3),update_user=#{record.updateUser} where house_id=#{record" +
            ".houseId} and version=#{record.version}")
    int changeBalance(@Param("record") BalanceEntity record);

}
