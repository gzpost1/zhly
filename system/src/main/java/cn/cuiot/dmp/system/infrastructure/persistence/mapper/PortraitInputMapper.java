package cn.cuiot.dmp.system.infrastructure.persistence.mapper;


import cn.cuiot.dmp.system.infrastructure.entity.PortraitInputEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PortraitInputDTO;
import cn.cuiot.dmp.system.infrastructure.entity.vo.PortraitInputVo;
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

    Page<PortraitInputVo> queryPortraitInputInfo(Page<PortraitInputVo> portraitInputVoPage,@Param("query") PortraitInputVo para);

    /**
     * 根据companyId查询人像信息
     *
     * @param portraitInputVoPage
     * @param para
     * @return
     */
    Page<PortraitInputVo> queryPortraitInputPage(Page<PortraitInputVo> portraitInputVoPage,@Param("params") PortraitInputDTO para);

}
