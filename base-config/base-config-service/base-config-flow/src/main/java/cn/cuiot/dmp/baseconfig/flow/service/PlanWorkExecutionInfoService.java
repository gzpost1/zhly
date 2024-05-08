package cn.cuiot.dmp.baseconfig.flow.service;


import cn.cuiot.dmp.baseconfig.flow.dto.ExecutionDateDto;
import cn.cuiot.dmp.baseconfig.flow.entity.PlanWorkExecutionInfoEntity;
import cn.cuiot.dmp.baseconfig.flow.mapper.PlanWorkExecutionInfoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @author pengjian
 * @since 2024-05-08
 */
@Service
public class PlanWorkExecutionInfoService extends ServiceImpl<PlanWorkExecutionInfoMapper, PlanWorkExecutionInfoEntity>{

    public List<ExecutionDateDto> queryExecutionTime(String startDate, String endDate) {
        return baseMapper.queryExecutionTime(startDate,endDate);
    }
}
