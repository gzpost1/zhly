package cn.cuiot.dmp.baseconfig.flow.mapper;

import cn.cuiot.dmp.baseconfig.flow.dto.work.WorkInfoDto;
import cn.cuiot.dmp.baseconfig.flow.entity.WorkInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author pengjian
 * @since 2024-04-23
 */
public interface WorkInfoMapper extends BaseMapper<WorkInfoEntity> {

    Page<WorkInfoDto> queryWorkOrderInfo(Page<WorkInfoDto> page, @Param("query") WorkInfoDto dto);
}
