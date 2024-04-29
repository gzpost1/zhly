package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.system.application.service.MenuService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.MenuByOrgTypeIdResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.MenuTreeNode;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜单管理
 *
 * @author Gengyu
 * @Date 2020/9/21
 */
@Slf4j
@RestController
@RequestMapping(value = "/menuManagement")
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;

    /**
     * 获取菜单目录
     */
    @PostMapping(value = "/getAllMenu", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MenuTreeNode> getMenu() {
        String orgId = getOrgId();
        String userId = getUserId();
        return menuService.getAllMenu(orgId, userId);
    }

    /**
     * 根据orgTypeId查询菜单根节点
     */
    @PostMapping(value = "/getMenuRootByOrgTypeId", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MenuByOrgTypeIdResDto> getMenuRootByOrgTypeId(
            @RequestParam("orgTypeId") String orgTypeId) {
        if (StringUtils.isEmpty(orgTypeId)) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL);
        }
        return menuService.getMenuByOrgTypeId(orgTypeId);
    }

}
