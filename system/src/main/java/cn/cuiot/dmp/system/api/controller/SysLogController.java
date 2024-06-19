package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.service.SysLogService;
import cn.cuiot.dmp.system.application.param.dto.SysLogQuery;
import cn.cuiot.dmp.system.infrastructure.entity.OperateLogEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 【PC】系统日志
 *
 * @author: wuyongchong
 * @date: 2024/6/18 10:15
 */
@Slf4j
@RestController
@RequestMapping("/sys/log")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    /**
     * 分页查询
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<OperateLogEntity>> queryForPage(@RequestBody SysLogQuery query) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        IPage<OperateLogEntity> pageData = sysLogService.queryForPage(query);
        return IdmResDTO.success(pageData);
    }

}
