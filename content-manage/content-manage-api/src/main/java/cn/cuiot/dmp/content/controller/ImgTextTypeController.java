package cn.cuiot.dmp.content.controller;//	模板

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.content.dal.entity.ImgTextType;
import cn.cuiot.dmp.content.service.ImgTextTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 图文类型
 *
 * @author hantingyao
 * @Description
 * @data 2024/6/3 10:31
 */
@RestController
@RequestMapping("/imgTextType")
public class ImgTextTypeController extends BaseController {

    @Autowired
    private ImgTextTypeService imgTextTypeService;

    /**
     * 保存
     */
    @RequiresPermissions
    @LogRecord(operationCode = "saveImgTextType", operationName = "保存图文类型", serviceType = "imgTextType", serviceTypeName = "图文类型管理")
    @PostMapping("/create")
    public Boolean saveImgTextType(@RequestBody @Valid ImgTextType imgTextType) {
        return imgTextTypeService.create(imgTextType);
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateImgTextType", operationName = "更新图文类型", serviceType = "imgTextType", serviceTypeName = "图文类型管理")
    @PostMapping("/update")
    public Boolean updateImgTextType(@RequestBody @Valid ImgTextType imgTextType) {
        return imgTextTypeService.update(imgTextType);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteImgTextType", operationName = "删除图文类型", serviceType = "imgTextType", serviceTypeName = "图文类型管理")
    @PostMapping("/delete")
    public Boolean deleteImgTextType(@RequestBody @Valid IdParam idParam) {
        return imgTextTypeService.remove(idParam.getId());
    }

    /**
     * 查询图文类型列表
     *
     * @return
     */
    @PostMapping("/queryForList")
    public List<ImgTextType> queryForList() {
        String orgId = getOrgId();
        return imgTextTypeService.queryForList(orgId);
    }

}
