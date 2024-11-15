package cn.cuiot.dmp.baseconfig.custommenu.entity;

import cn.cuiot.dmp.baseconfig.custommenu.dto.FlowTaskOrgDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbFlowTaskOrgMapper extends BaseMapper<TbFlowTaskOrg> {

    int insertList(@Param("list") List<TbFlowTaskOrg> list);


    List<FlowTaskOrgDto> queryOrgNameByFlowConfigIds(@Param("list") List<Long> dataIds);

    void batchDeleteByTaskConfigIds(@Param("list") List<Long> list);
}