package cn.cuiot.dmp.externalapi.service.mapper.park;



import cn.cuiot.dmp.externalapi.service.entity.park.PortraitInputEntity;
import cn.cuiot.dmp.externalapi.service.query.PortraitInputDTO;
import cn.cuiot.dmp.externalapi.service.vo.park.PortraitInputVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author pengjian
 * @since 2024-07-18
 */
public interface PortraitInputMapper extends BaseMapper<PortraitInputEntity> {

    String queryPlatfromInfo(@Param("companyId") Long companyId, @Param("platformId") Long platformId);

    Page<PortraitInputVo> queryPortraitInputInfo(Page<PortraitInputVo> portraitInputVoPage, @Param("query") PortraitInputVo para);

    /**
     * 根据companyId查询人像信息
     *
     * @param portraitInputVoPage
     * @param para
     * @return
     */
    Page<PortraitInputVo> queryPortraitInputPage(Page<PortraitInputVo> portraitInputVoPage,@Param("params") PortraitInputDTO para);

}
