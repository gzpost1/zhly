package cn.cuiot.dmp.system.infrastructure.persistence.mapper;


import cn.cuiot.dmp.system.infrastructure.entity.PortraitInputEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PortraitInputInfoDto;
import cn.cuiot.dmp.system.infrastructure.entity.vo.PortraitInputVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author pengjian
 * @since 2024-07-18
 */
public interface PortraitInputMapper extends BaseMapper<PortraitInputEntity> {

    PortraitInputInfoDto queryPlatfromInfo(String platformType);


    IPage<PortraitInputVo> queryPortraitInputInfo(@Param("query") PortraitInputVo para);
}
