package cn.cuiot.dmp.externalapi.provider.controller.admin;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.entity.PersonGroupEntity;
import cn.cuiot.dmp.externalapi.service.query.PersonGroupCreateDTO;
import cn.cuiot.dmp.externalapi.service.query.PersonGroupPageQuery;
import cn.cuiot.dmp.externalapi.service.query.PersonGroupUpdateDTO;
import cn.cuiot.dmp.externalapi.service.service.PersonGroupService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 后台-人员分组
 *
 * @Author: zc
 * @Date: 2024-09-04
 */
@RestController
@RequestMapping("/personGroup")
public class PersonGroupController {

    @Autowired
    public PersonGroupService personGroupService;

    /**
     * 分页查询
     */
    @RequiresPermissions
    @PostMapping("/queryForList")
    public IdmResDTO<List<PersonGroupEntity>> queryForList(@RequestBody PersonGroupPageQuery query) {
        return IdmResDTO.success(personGroupService.queryForList(query));
    }

    /**
     * 分页查询
     */
    @RequiresPermissions
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<PersonGroupEntity>> queryForPage(@RequestBody PersonGroupPageQuery query) {
        return IdmResDTO.success(personGroupService.queryForPage(query));
    }

    /**
     * 详情
     */
    @RequiresPermissions
    @PostMapping("/queryForDetail")
    public IdmResDTO<PersonGroupEntity> queryForDetail(@RequestBody @Valid IdParam param) {
        return IdmResDTO.success(personGroupService.queryForDetail(param.getId()));
    }

    /**
     * 新增
     */
    @RequiresPermissions
    @LogRecord(operationCode = "savePersonGroup", operationName = "保存人员分组", serviceType = "personGroup", serviceTypeName = "人员分组管理")
    @PostMapping("/create")
    public IdmResDTO<?> create(@RequestBody @Valid PersonGroupCreateDTO dto) {
        personGroupService.create(dto);
        return IdmResDTO.success();
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updatePersonGroup", operationName = "更新人员分组", serviceType = "personGroup", serviceTypeName = "人员分组管理")
    @PostMapping("/update")
    public IdmResDTO<?> update(@RequestBody @Valid PersonGroupUpdateDTO dto) {
        personGroupService.update(dto);
        return IdmResDTO.success();
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deletePersonGroup", operationName = "删除人员分组", serviceType = "personGroup", serviceTypeName = "人员分组管理")
    @PostMapping("/delete")
    public IdmResDTO<?> delete(@RequestBody @Valid IdParam param) {
        personGroupService.delete(param.getId());
        return IdmResDTO.success();
    }
}
