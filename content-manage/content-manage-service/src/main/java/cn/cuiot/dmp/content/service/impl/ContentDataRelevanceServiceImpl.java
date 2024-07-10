package cn.cuiot.dmp.content.service.impl;//	模板

import cn.cuiot.dmp.content.dal.entity.ContentDataRelevance;
import cn.cuiot.dmp.content.dal.mapper.ContentDataRelevanceMapper;
import cn.cuiot.dmp.content.param.dto.DepartBuildDto;
import cn.cuiot.dmp.content.service.ContentDataRelevanceService;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/29 17:38
 */
@Service
public class ContentDataRelevanceServiceImpl extends ServiceImpl<ContentDataRelevanceMapper, ContentDataRelevance> implements ContentDataRelevanceService {

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void batchSaveContentDataRelevance(Byte dataType, List<Long> departments, List<Long> buildings, Long id) {
        LambdaQueryWrapper<ContentDataRelevance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContentDataRelevance::getDataId, id);
        wrapper.eq(ContentDataRelevance::getDataType, dataType);
        this.baseMapper.delete(wrapper);
        if (buildings != null && !buildings.isEmpty()) {
            buildings.forEach(building -> {
                ContentDataRelevance contentDataRelevance = new ContentDataRelevance();
                contentDataRelevance.setBuildId(building);
                contentDataRelevance.setDataId(id);
                contentDataRelevance.setDataType(dataType);
                save(contentDataRelevance);
            });
        } else {
            if (departments != null && !departments.isEmpty()) {
                departments.forEach(department -> {
                    ContentDataRelevance contentDataRelevance = new ContentDataRelevance();
                    contentDataRelevance.setDepartmentId(department);
                    contentDataRelevance.setDataId(id);
                    contentDataRelevance.setDataType(dataType);
                    save(contentDataRelevance);
                });
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void batchSaveContentDataRelevance(Byte dataType, Long deptId, List<Long> buildings, Long id) {
        LambdaQueryWrapper<ContentDataRelevance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContentDataRelevance::getDataId, id);
        wrapper.eq(ContentDataRelevance::getDataType, dataType);
        this.baseMapper.delete(wrapper);
        if (buildings != null && !buildings.isEmpty()) {
            buildings.forEach(building -> {
                ContentDataRelevance contentDataRelevance = new ContentDataRelevance();
                contentDataRelevance.setBuildId(building);
                contentDataRelevance.setDataId(id);
                contentDataRelevance.setDataType(dataType);
                contentDataRelevance.setDepartmentId(deptId);
                save(contentDataRelevance);
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void batchSaveContentDataRelevance(Byte dataType, List<DepartBuildDto> departBuilds, Long id) {
        LambdaQueryWrapper<ContentDataRelevance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContentDataRelevance::getDataId, id);
        wrapper.eq(ContentDataRelevance::getDataType, dataType);
        this.baseMapper.delete(wrapper);
        if (CollUtil.isNotEmpty(departBuilds)) {
            for (DepartBuildDto departBuild : departBuilds) {
                ContentDataRelevance contentDataRelevance = new ContentDataRelevance();
                contentDataRelevance.setDepartmentId(departBuild.getDepartment());
                contentDataRelevance.setDataType(dataType);
                contentDataRelevance.setBuildId(departBuild.getBuilding());
                contentDataRelevance.setDataId(id);
                save(contentDataRelevance);
            }
        }

    }


}
