package cn.cuiot.dmp.system.infrastructure.persistence.dao;

import cn.cuiot.dmp.system.infrastructure.entity.OperationLogEntity;
import cn.cuiot.dmp.system.infrastructure.entity.bean.OperationLogBean;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author suyu
 * @className OperationLogDao
 * @description
 * @date 2020-09-03 10:57:56
 */
@Mapper
public interface OperationLogDao {

    /**
     * 新增日志
     *
     * @param operationLogEntity
     * @return
     */
    int insertOperationLog(OperationLogEntity operationLogEntity);

    /**
     * 查询平台日志分页列表
     *
     * @param operationLogDto 查询参数
     * @return
     */
    List<OperationLogEntity> listLogs(OperationLogBean operationLogDto);

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    List<Map<String,String>> findById(@Param("id") Integer id);

}
