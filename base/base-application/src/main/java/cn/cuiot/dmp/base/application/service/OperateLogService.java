package cn.cuiot.dmp.base.application.service;

import cn.cuiot.dmp.common.log.dto.OperateLogDto;

/**
 * @author wensq
 * @classname OperateLogService
 * @description 日志保存接口
 * @date 2021/12/3
 */
public interface OperateLogService {

    /**
     * 保存操作日志
     * @param operateLogDTO
     * @author <zhangxp207@chinaunicom.cn>
     * @since 2022/12/2 14:04
     * @return int
     *
     */
    Integer saveDb(OperateLogDto operateLogDTO);

}
