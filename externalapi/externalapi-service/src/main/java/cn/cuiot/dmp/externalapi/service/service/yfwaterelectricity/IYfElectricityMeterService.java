package cn.cuiot.dmp.externalapi.service.service.yfwaterelectricity;

import cn.cuiot.dmp.base.application.service.BaseService;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.externalapi.service.entity.yfwaterelectricity.YfElectricityMeter;
import cn.cuiot.dmp.externalapi.service.query.yfwaterelectricity.YfElectricityMeterDTO;
import cn.cuiot.dmp.externalapi.service.vo.watermeter.YfElectricityMeterStatisticsVO;
import cn.cuiot.dmp.externalapi.service.vo.yfwaterelectricity.YfElectricityMeterVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 智慧物联-宇泛电表 服务类
 * </p>
 *
 * @author xiaotao
 * @since 2024-10-10
 */
public interface IYfElectricityMeterService extends BaseService<YfElectricityMeter> {


    /**
     * 新增电表
     * @param electricityMeterDTO YfElectricityMeterDTO
     * @return Long
     */
    Long create(YfElectricityMeterDTO electricityMeterDTO);

    /**
     * 电表分页查询
     * @param vo YfElectricityMeterDTO
     * @return YfElectricityMeterVO
     */
    IPage<YfElectricityMeterVO> queryForPage(YfElectricityMeterDTO vo);

    /**
     * 查询电表详情
     * @param idParam idParam
     * @return YfElectricityMeterVO
     */
    YfElectricityMeterVO queryForDetail(IdParam idParam);

    /**
     * 修改电表信息
     * @param electricityMeterDTO YfElectricityMeterDTO
     * @return Boolean
     */
    Boolean update(YfElectricityMeterDTO electricityMeterDTO);

    /**
     * 删除电表
     * @param idsParam idsParam
     * @return Boolean
     */
    Boolean delete(IdsParam idsParam);

    /**
     * 查询电表统计数据
     * @param vo YfElectricityMeterDTO
     * @return YfElectricityMeterStatisticsVO
     */
    IPage<YfElectricityMeterStatisticsVO> queryAmountForPage(YfElectricityMeterDTO vo);
}
