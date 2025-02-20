package cn.cuiot.dmp.content.dal.mapper;//	模板

import cn.cuiot.dmp.content.dal.entity.ContentModule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/4 9:20
 */
public interface ContentModuleMapper extends BaseMapper<ContentModule> {

    void batchDeleteByIds(@Param("list") List<Long> list);
}
