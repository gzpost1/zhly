package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.bean.PageQuery;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.system.application.service.CompanyTemplateService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.CompanyTemplateDto;
import cn.cuiot.dmp.system.infrastructure.entity.vo.CompanyTemplateVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台-初始化管理-企业模板
 *
 * @Author: zc
 * @Date: 2024-11-05
 */
@RestController
@RequestMapping("/companyTemplate")
public class CompanyTemplateController {

    @Autowired
    private CompanyTemplateService companyTemplateService;

    /**
     * 分页
     *
     * @return IPage
     * @Param query 参数
     */
    @RequiresPermissions
    @PostMapping("queryForPage")
    public IdmResDTO<IPage<CompanyTemplateVO>> queryForPage(@RequestBody PageQuery query) {
        return IdmResDTO.success(companyTemplateService.queryForPage(query));
    }

    /**
     * 获取最后一条模板数据
     *
     * @return CompanyTemplateVO
     */
    @RequiresPermissions
    @PostMapping("queryLastTemplate")
    public IdmResDTO<CompanyTemplateVO> queryLastTemplate() {
        return IdmResDTO.success(companyTemplateService.queryLastTemplate());
    }

    /**
     * 保存
     *
     * @Param dto 参数
     */
    @RequiresPermissions
    @LogRecord(operationCode = "saveOrUpdate", operationName = "保存企业模板", serviceType = "companyTemplate", serviceTypeName = "企业模板")
    @PostMapping("saveOrUpdate")
    public IdmResDTO<?> saveOrUpdate(@RequestBody CompanyTemplateDto dto) {
        companyTemplateService.saveOrUpdate(dto);
        return IdmResDTO.success();
    }
}
