package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.system.infrastructure.entity.dto.GetSysParamResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SysParamDto;

/**
 * @Description SysParamService
 * @Author shixh
 * @Data 2022/11/24
 */
public interface SysParamService {

    /**
     * 保存系统配置
     *
     * @param dto SysParamDto
     * @return
     * @Author wen
     * @Description 保存
     * @Date 2021/8/18 17:06
     **/
    int addOrUpdate(SysParamDto dto);

    /**
     * 查找系统配置
     * @return
     */
    GetSysParamResDto getByPath(Long sessionOrgId);

}
