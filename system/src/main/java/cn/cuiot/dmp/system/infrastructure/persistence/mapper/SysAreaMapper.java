package cn.cuiot.dmp.system.infrastructure.persistence.mapper;

import cn.cuiot.dmp.system.domain.aggregate.AreaTreeNode;
import cn.cuiot.dmp.system.domain.aggregate.SysArea;
import cn.cuiot.dmp.system.infrastructure.entity.DictAreaEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author caorui
 * @date 2024/5/14
 */
public interface SysAreaMapper extends BaseMapper<DictAreaEntity> {

    List<AreaTreeNode> getAreaTree();

    SysArea getAreaInfoByAreaCode(@Param("areaCode") String areaCode);

    List<SysArea> getAreaInfoByAreaCodeList(@Param("areaCodeList") List<String> areaCodeList);

}
