package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.system.application.service.MenuService;
import cn.cuiot.dmp.system.infrastructure.entity.MenuEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetMenuRootByOrgTypeIdResDto;
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
     * @param description
     * @return
     */
    @PostMapping(value = "/getAllMenu", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MenuEntity> getMenu(@RequestParam(value = "description" ,required = false) String description) {
        log.info("getMenu, 参数为{}", description);
        String orgId = getOrgId();
        String userId = getUserId();
        return menuService.getAllMenu(description, orgId, userId);
    }

    /**
     * 根据orgTypeId查询菜单根节点
     * @param orgTypeId
     * @return
     */
    @PostMapping(value = "/getMenuRootByOrgTypeId", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GetMenuRootByOrgTypeIdResDto> getMenuRootByOrgTypeId(@RequestParam("orgTypeId") String orgTypeId) {
        getOrgId();
        if (StringUtils.isEmpty(orgTypeId)) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL);
        }
        return menuService.getMenuRootByOrgTypeId(orgTypeId);
    }

}
