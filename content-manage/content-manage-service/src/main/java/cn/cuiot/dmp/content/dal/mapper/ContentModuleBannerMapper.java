package cn.cuiot.dmp.content.dal.mapper;//	模板

import cn.cuiot.dmp.content.dal.entity.ContentModuleBanner;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/4 10:29
 */
public interface ContentModuleBannerMapper extends BaseMapper<ContentModuleBanner> {

    void batchDeleteByModuleIds(@Param("list") List<Long> list);
}
