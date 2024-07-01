package cn.cuiot.dmp.lease.mapper.charge;

import cn.cuiot.dmp.lease.dto.charge.ChargeChargeItemDto;
import cn.cuiot.dmp.lease.entity.charge.ChargeNoticeItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收费管理-通知单（收费项目） mapper接口
 *
 * @author zc
 */
public interface ChargeNoticeItemMapper extends BaseMapper<ChargeNoticeItemEntity> {

    /**
     * 批量插入
     *
     * @Param list 列表
     */
    void batchInsert(@Param("list") List<ChargeNoticeItemEntity> list);

    /**
     * 根据通知单id查询收费项目名称
     *
     * @return list 收费项目列表
     * @Param list 通知单ids
     */
    List<ChargeChargeItemDto> getChargeItemNamesByChargeNoticeId(@Param("list") List<Long> list);
}