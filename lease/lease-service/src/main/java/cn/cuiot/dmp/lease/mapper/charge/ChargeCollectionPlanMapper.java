package cn.cuiot.dmp.lease.mapper.charge;

import cn.cuiot.dmp.lease.dto.charge.ChargeCollectionPlanPageQuery;
import cn.cuiot.dmp.lease.entity.charge.ChargeCollectionPlanEntity;
import cn.cuiot.dmp.lease.vo.ChargeCollectionPlanPageVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 收费管理-催款计划 mapper接口
 *
 * @author zc
 */
public interface ChargeCollectionPlanMapper extends BaseMapper<ChargeCollectionPlanEntity> {
    /**
     * 分页
     */
    IPage<ChargeCollectionPlanPageVo> queryForPage(Page<?> page, @Param("params") ChargeCollectionPlanPageQuery planPageDto);
}