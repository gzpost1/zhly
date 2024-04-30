package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.dto.AuthorizeParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.MenuDTO;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.service.MenuService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.MenuByOrgTypeIdResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.MenuQuery;
import cn.cuiot.dmp.system.infrastructure.entity.dto.MenuTreeNode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     * 根据orgTypeId查询菜单节点
     */
    @PostMapping(value = "/getMenuByOrgTypeId", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MenuByOrgTypeIdResDto> getMenuByOrgTypeId(
            @RequestParam("orgTypeId") String orgTypeId) {
        if (StringUtils.isEmpty(orgTypeId)) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL);
        }
        return menuService.getMenuByOrgTypeId(orgTypeId);
    }


    /**
     * 获取菜单列表
     */
    @PostMapping(value = "/queryForList", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MenuTreeNode> queryForList(@RequestBody MenuQuery query) {
        List<MenuTreeNode> treeList = menuService.queryForList(query);
        return treeList;
    }

    /**
     * 创建菜单项
     */
    @PostMapping("/create")
    public IdmResDTO create(@RequestBody @Valid MenuDTO menuDTO) {
        menuDTO.setCreatedBy(LoginInfoHolder.getCurrentUserId().toString());
        menuDTO.setCreatedOn(LocalDateTime.now());
        menuDTO.setUpdatedBy(LoginInfoHolder.getCurrentUserId().toString());
        menuDTO.setUpdatedOn(LocalDateTime.now());
        menuService.create(menuDTO);
        return IdmResDTO.success(null);
    }

    /**
     * 修改菜单项
     */
    @PostMapping("/update")
    public IdmResDTO update(@RequestBody @Valid MenuDTO menuDTO) {
        if (Objects.isNull(menuDTO.getId())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL);
        }
        menuDTO.setUpdatedBy(LoginInfoHolder.getCurrentUserId().toString());
        menuDTO.setUpdatedOn(LocalDateTime.now());
        menuService.create(menuDTO);
        return IdmResDTO.success(null);
    }

    /**
     * 删除菜单项
     */
    @PostMapping("/delete")
    public IdmResDTO deleteMenu(@RequestBody @Valid IdParam idParam) {
        menuService.deleteMenu(idParam.getId());
        return IdmResDTO.success(null);
    }

    /**
     * 启停用
     */
    @PostMapping("/updateStatus")
    public IdmResDTO updateStatus(@RequestBody @Valid UpdateStatusParam updateStatusParam) {
        String sessionUserId = LoginInfoHolder.getCurrentUserId().toString();
        String sessionOrgId = LoginInfoHolder.getCurrentOrgId().toString();
        menuService.updateStatus(updateStatusParam.getId(), updateStatusParam.getStatus(),
                sessionUserId, sessionOrgId);
        return IdmResDTO.success();
    }

    /**
     * 初始化授权
     */
    @PostMapping("/authorize")
    public IdmResDTO authorize(@RequestBody @Valid AuthorizeParam authorizeParam) {
        String sessionUserId = LoginInfoHolder.getCurrentUserId().toString();
        authorizeParam.setSessionUserId(sessionUserId);
        menuService.authorize(authorizeParam);
        return IdmResDTO.success();
    }

}
