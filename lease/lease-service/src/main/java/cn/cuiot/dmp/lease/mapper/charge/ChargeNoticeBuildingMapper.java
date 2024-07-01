package cn.cuiot.dmp.lease.mapper.charge;

import cn.cuiot.dmp.lease.dto.charge.ChargeBuildingDto;
import cn.cuiot.dmp.lease.entity.charge.ChargeNoticeBuildingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收费管理-通知单（楼盘信息） mapper接口
 *
 * @author zc
 */
public interface ChargeNoticeBuildingMapper extends BaseMapper<ChargeNoticeBuildingEntity> {

    /**
     * 批量插入
     *
     * @Param list 列表
     */
    void batchInsert(@Param("list") List<ChargeNoticeBuildingEntity> list);

    /**
     * 根据通知单id查询楼盘名称
     *
     * @return list 楼盘名称列表
     * @Param list 通知单ids
     */
    List<ChargeBuildingDto> getBuildingNamesByChargeNoticeId(@Param("list") List<Long> list);
}