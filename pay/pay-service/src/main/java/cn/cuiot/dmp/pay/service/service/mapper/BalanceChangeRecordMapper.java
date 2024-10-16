package cn.cuiot.dmp.pay.service.service.mapper;


import cn.cuiot.dmp.pay.service.service.dto.BalanceChargeRecordQuery;
import cn.cuiot.dmp.pay.service.service.entity.BalanceChangeRecord;
import cn.cuiot.dmp.pay.service.service.vo.BalanceChargeRecordVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 余额变动记录 mapper接口
 */

public interface BalanceChangeRecordMapper extends BaseMapper<BalanceChangeRecord> {
    IPage<BalanceChargeRecordVO> queryChargeForPage(Page page, @Param("param") BalanceChargeRecordQuery query);
}
