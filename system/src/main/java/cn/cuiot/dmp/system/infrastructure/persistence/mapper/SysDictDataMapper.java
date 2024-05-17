package cn.cuiot.dmp.system.infrastructure.persistence.mapper;

import cn.cuiot.dmp.base.infrastructure.dto.DictTypeVO;
import cn.cuiot.dmp.system.infrastructure.entity.SysDictData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysDictDataMapper extends BaseMapper<SysDictData> {

    List<DictTypeVO> getDictDataList(@Param("dictCode") String dictCode);

}