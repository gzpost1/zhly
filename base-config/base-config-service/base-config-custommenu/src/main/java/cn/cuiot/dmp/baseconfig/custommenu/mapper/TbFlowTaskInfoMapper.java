package cn.cuiot.dmp.baseconfig.custommenu.mapper;
import cn.cuiot.dmp.baseconfig.custommenu.vo.FlowTaskInfoVo;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import cn.cuiot.dmp.baseconfig.custommenu.entity.TbFlowTaskInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface TbFlowTaskInfoMapper extends BaseMapper<TbFlowTaskInfo> {
    int insertList(@Param("list")List<TbFlowTaskInfo> list);


    List<FlowTaskInfoVo> queryByTaskConfigId(Long id);

    void batchDeleteByTaskConfigIds(@Param("list") List<Long> list);
}