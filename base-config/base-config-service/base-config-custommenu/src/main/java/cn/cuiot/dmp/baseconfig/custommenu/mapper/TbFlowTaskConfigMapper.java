package cn.cuiot.dmp.baseconfig.custommenu.mapper;

import cn.cuiot.dmp.baseconfig.custommenu.dto.FlowTaskInfoPageDto;
import cn.cuiot.dmp.baseconfig.custommenu.dto.TbFlowTaskInfoQuery;
import cn.cuiot.dmp.baseconfig.custommenu.entity.TbFlowTaskConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbFlowTaskConfigMapper extends BaseMapper<TbFlowTaskConfig> {
    IPage<FlowTaskInfoPageDto> queryForPage(Page page, @Param("query") TbFlowTaskInfoQuery query);

    void batchDeleteByIds(@Param("list") List<Long> list);

}