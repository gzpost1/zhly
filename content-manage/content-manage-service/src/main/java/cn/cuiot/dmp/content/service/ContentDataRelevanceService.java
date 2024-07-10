package cn.cuiot.dmp.content.service;//	模板

import cn.cuiot.dmp.content.dal.entity.ContentDataRelevance;
import cn.cuiot.dmp.content.dal.entity.ContentNoticeEntity;
import cn.cuiot.dmp.content.param.dto.DepartBuildDto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/29 17:37
 */
public interface ContentDataRelevanceService extends IService<ContentDataRelevance> {
    /**
     * 批量保存内容数据关联
     * @param departments
     * @param buildings
     * @param id
     */
    void batchSaveContentDataRelevance(Byte dataType,List<Long> departments, List<Long> buildings, Long id);

    /**
     * 批量保存内容数据关联
     * @param dataType
     * @param deptId
     * @param buildings
     * @param id
     */
    void batchSaveContentDataRelevance(Byte dataType,Long deptId, List<Long> buildings, Long id);

    /**
     * 批量保存内容数据关联
     * @param dataType
     * @param departBuilds
     * @param id
     */
    void batchSaveContentDataRelevance(Byte dataType, List<DepartBuildDto> departBuilds, Long id);
}
