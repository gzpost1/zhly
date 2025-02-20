package cn.cuiot.dmp.content.dal.mapper;//	模板

import cn.cuiot.dmp.content.dal.entity.ContentModuleApplication;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/4 15:46
 */
public interface ContentModuleApplicationMapper extends BaseMapper<ContentModuleApplication> {

    void batchDeleteByModuleIds(@Param("list") List<Long> list);
}
