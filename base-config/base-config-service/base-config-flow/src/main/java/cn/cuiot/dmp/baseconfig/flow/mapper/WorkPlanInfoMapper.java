package cn.cuiot.dmp.baseconfig.flow.mapper;


import cn.cuiot.dmp.baseconfig.flow.dto.QueryWorkPlanInfoDto;
import cn.cuiot.dmp.baseconfig.flow.entity.WorkPlanInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pengjian
 * @since 2024-05-06
 */
public interface WorkPlanInfoMapper extends BaseMapper<WorkPlanInfoEntity> {

    Page<WorkPlanInfoEntity> queryWordPlanInfo(Page<WorkPlanInfoEntity> page, @Param("query") QueryWorkPlanInfoDto dto);
}
