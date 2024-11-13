package cn.cuiot.dmp.baseconfig.flow.mapper;

import cn.cuiot.dmp.baseconfig.flow.dto.TbFlowConfigQuery;
import cn.cuiot.dmp.baseconfig.flow.dto.TbFlowPageDto;
import cn.cuiot.dmp.baseconfig.flow.entity.TbFlowConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbFlowConfigMapper extends BaseMapper<TbFlowConfig> {
    IPage<TbFlowPageDto> queryForPage(Page page, @Param("query") TbFlowConfigQuery query);

    void batchDeleteByIds(@Param("list") List<Long> list);
}