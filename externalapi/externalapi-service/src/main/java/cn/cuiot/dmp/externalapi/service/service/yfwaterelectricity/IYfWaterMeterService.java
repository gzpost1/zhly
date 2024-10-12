package cn.cuiot.dmp.externalapi.service.service.yfwaterelectricity;

import cn.cuiot.dmp.base.application.service.BaseService;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.externalapi.service.entity.yfwaterelectricity.YfWaterMeter;
import cn.cuiot.dmp.externalapi.service.query.yfwaterelectricity.YfWaterMeterDTO;
import cn.cuiot.dmp.externalapi.service.vo.watermeter.YfWaterMeterStatisticsVO;
import cn.cuiot.dmp.externalapi.service.vo.yfwaterelectricity.YfWaterMeterVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 智慧物联-宇泛水表 服务类
 * </p>
 *
 * @author xiaotao
 * @since 2024-10-10
 */
public interface IYfWaterMeterService extends BaseService<YfWaterMeter> {

    /**
     * 新增水表
     * @param waterMeterDTO YfWaterMeterDTO
     * @return Long
     */
    Long create(YfWaterMeterDTO waterMeterDTO);

    /**
     * 更新水表
     * @param waterMeterDTO YfWaterMeterDTO
     * @return Boolean
     */
    Boolean update(YfWaterMeterDTO waterMeterDTO);

    /**
     * 删除水表
     * @param idsParam IdsParam
     * @return Boolean
     */
    Boolean delete(IdsParam idsParam);

    /**
     * 查询水表分页
     * @param vo YfWaterMeterDTO
     * @return YfWaterMeterVO
     */
    IPage<YfWaterMeterVO> queryForPage(YfWaterMeterDTO vo);

    /**
     * 查询水表详情
     * @param idParam IdParam
     * @return YfWaterMeterVO
     */
    YfWaterMeterVO queryForDetail(IdParam idParam);

    /**
     * 查询水表统计数据
     * @param vo YfWaterMeterDTO
     * @return YfWaterMeterStatisticsVO
     */
    IPage<YfWaterMeterStatisticsVO> queryAmountForPage(YfWaterMeterDTO vo);
}
