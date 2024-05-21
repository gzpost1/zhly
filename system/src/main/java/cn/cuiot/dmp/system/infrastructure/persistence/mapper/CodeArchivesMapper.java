package cn.cuiot.dmp.system.infrastructure.persistence.mapper;

import cn.cuiot.dmp.system.infrastructure.entity.CodeArchivesEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author caorui
 * @date 2024/5/20
 */
public interface CodeArchivesMapper extends BaseMapper<CodeArchivesEntity> {

    /**
     * 批量保存二维码档案
     *
     * @param codeArchivesEntityList 二维码档案列表
     */
    int batchSaveCodeArchives(@Param("list") List<CodeArchivesEntity> codeArchivesEntityList);

}
