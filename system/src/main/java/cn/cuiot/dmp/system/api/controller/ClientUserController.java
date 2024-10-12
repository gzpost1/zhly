package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.service.ClientUserService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ClientUserQuery;
import cn.cuiot.dmp.system.infrastructure.entity.vo.ClientUserVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * C端用户管理
 *
 * @author wuyongchong
 * @date 2024-06-14
 */
@Slf4j
@RestController
@RequestMapping("/client-user")
public class ClientUserController {

    @Autowired
    private ClientUserService clientUserService;

    /**
     * 分页查询
     */
    @RequiresPermissions
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<ClientUserVo>> queryForPage(@RequestBody ClientUserQuery query) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        query.setCompanyId(currentOrgId);
        IPage<ClientUserVo> pageData = clientUserService.queryForPage(query);
        return IdmResDTO.success(pageData);
    }

    /**
     * 导出
     *
     * @param pageQuery
     * @return
     * @throws Exception
     */
    @PostMapping("export")
    public IdmResDTO export(@RequestBody @Valid ClientUserQuery pageQuery) throws Exception {
        clientUserService.export(pageQuery);
        return IdmResDTO.success();
    }
}
