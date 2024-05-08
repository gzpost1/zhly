package cn.cuiot.dmp.baseconfig.flow.mapper;


import cn.cuiot.dmp.baseconfig.flow.dto.ExecutionDateDto;
import cn.cuiot.dmp.baseconfig.flow.entity.PlanWorkExecutionInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;
import java.util.List;

/**
 * @author pengjian
 * @since 2024-05-08
 */
public interface PlanWorkExecutionInfoMapper extends BaseMapper<PlanWorkExecutionInfoEntity> {

    List<ExecutionDateDto> queryExecutionTime(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
