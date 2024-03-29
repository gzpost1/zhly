package cn.cuiot.dmp.system.infrastructure.persistence.dao;


import cn.cuiot.dmp.system.infrastructure.entity.SensitivityEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xumengfan
 * @classname SensitiveDao
 * @description 敏感词Dao
 * @date 2020-12-09
 */
@Mapper
public interface SensitiveDao {

    /**
     * 查询敏感词
     * @return
     */
    List<SensitivityEntity> searchSensitiveWord();
}
