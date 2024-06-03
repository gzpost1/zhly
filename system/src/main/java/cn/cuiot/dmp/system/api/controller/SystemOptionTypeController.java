package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.system.application.param.dto.SystemOptionTypeQueryDTO;
import cn.cuiot.dmp.system.application.param.vo.SystemOptionTypeTreeNodeVO;
import cn.cuiot.dmp.system.application.param.vo.SystemOptionTypeVO;
import cn.cuiot.dmp.system.application.service.SystemOptionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 系统配置-初始化配置-常用选项-系统选项
 *
 * @author caorui
 * @date 2024/5/22
 */
@RestController
@RequestMapping("/systemOptionType")
public class SystemOptionTypeController extends BaseController {

    @Autowired
    private SystemOptionTypeService systemOptionTypeService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public SystemOptionTypeVO queryForDetail(@RequestBody @Valid IdParam idParam) {
        return systemOptionTypeService.queryForDetail(idParam.getId());
    }

    /**
     * 根据条件获取列表
     */
    @PostMapping("/queryForList")
    public List<SystemOptionTypeVO> queryForList(@RequestBody @Valid SystemOptionTypeQueryDTO queryDTO) {
        String orgId = getOrgId();
        String userId = getUserId();
        queryDTO.setCompanyId(Long.valueOf(orgId));
        queryDTO.setUserId(userId);
        return systemOptionTypeService.queryForList(queryDTO);
    }

    /**
     * 根据条件获取档案树
     */
    @PostMapping("/queryForTree")
    public List<SystemOptionTypeTreeNodeVO> queryForTree(@RequestBody @Valid SystemOptionTypeQueryDTO queryDTO) {
        String orgId = getOrgId();
        String userId = getUserId();
        queryDTO.setCompanyId(Long.valueOf(orgId));
        queryDTO.setUserId(userId);
        return systemOptionTypeService.queryForTree(queryDTO);
    }

}
