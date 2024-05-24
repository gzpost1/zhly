package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.system.application.param.dto.ArchiveTypeQueryDTO;
import cn.cuiot.dmp.system.application.param.vo.ArchiveTypeTreeNodeVO;
import cn.cuiot.dmp.system.application.param.vo.ArchiveTypeVO;
import cn.cuiot.dmp.system.application.service.ArchiveTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 系统配置-档案类型分类
 *
 * @author caorui
 * @date 2024/5/22
 */
@RestController
@RequestMapping("/archiveType")
public class ArchiveTypeController extends BaseController {

    @Autowired
    private ArchiveTypeService archiveTypeService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public ArchiveTypeVO queryForDetail(@RequestBody @Valid IdParam idParam) {
        return archiveTypeService.queryForDetail(idParam.getId());
    }

    /**
     * 根据条件获取列表
     */
    @PostMapping("/queryForList")
    public List<ArchiveTypeVO> queryForList(@RequestBody @Valid ArchiveTypeQueryDTO queryDTO) {
        String orgId = getOrgId();
        String userId = getUserId();
        queryDTO.setCompanyId(Long.valueOf(orgId));
        queryDTO.setUserId(userId);
        return archiveTypeService.queryForList(queryDTO);
    }

    /**
     * 根据条件获取档案树
     */
    @PostMapping("/queryForTree")
    public List<ArchiveTypeTreeNodeVO> queryForTree(@RequestBody @Valid ArchiveTypeQueryDTO queryDTO) {
        String orgId = getOrgId();
        String userId = getUserId();
        queryDTO.setCompanyId(Long.valueOf(orgId));
        queryDTO.setUserId(userId);
        return archiveTypeService.queryForTree(queryDTO);
    }

}
