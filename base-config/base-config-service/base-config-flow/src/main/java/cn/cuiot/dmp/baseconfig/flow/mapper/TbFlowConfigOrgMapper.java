package cn.cuiot.dmp.baseconfig.flow.mapper;

import cn.cuiot.dmp.baseconfig.flow.dto.FlowConfigOrgDto;
import cn.cuiot.dmp.baseconfig.flow.entity.TbFlowConfigOrg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbFlowConfigOrgMapper extends BaseMapper<TbFlowConfigOrg> {
    int insertList(@Param("list") List<TbFlowConfigOrg> list);


    List<FlowConfigOrgDto> queryOrgNameByFlowConfigIds(@Param("list") List<Long> dataIds);
}