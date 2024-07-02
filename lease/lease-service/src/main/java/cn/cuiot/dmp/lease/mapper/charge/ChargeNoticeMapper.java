package cn.cuiot.dmp.lease.mapper.charge;

import cn.cuiot.dmp.lease.dto.charge.ChargeNoticePageQuery;
import cn.cuiot.dmp.lease.dto.charge.ChargeNoticeSendDto;
import cn.cuiot.dmp.lease.dto.charge.ChargeNoticeSendQuery;
import cn.cuiot.dmp.lease.entity.charge.ChargeNoticeEntity;
import cn.cuiot.dmp.lease.vo.ChargeNoticePageVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 收费管理-客户账单 mapper接口
 *
 * @author zc
 */
public interface ChargeNoticeMapper extends BaseMapper<ChargeNoticeEntity> {

    /**
     * 分页
     */
    IPage<ChargeNoticePageVo> queryForPage(Page<?> page, @Param("params") ChargeNoticePageQuery dto);

    /**
     * 查询欠费信息，用于发送通知
     */
    IPage<ChargeNoticeSendDto> queryChargeNoticeInfo(Page<?> page, @Param("params") ChargeNoticeSendQuery query);
}