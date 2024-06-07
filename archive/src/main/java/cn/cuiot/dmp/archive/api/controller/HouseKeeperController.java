package cn.cuiot.dmp.archive.api.controller;

import cn.cuiot.dmp.archive.application.param.dto.HouseKeeperDto;
import cn.cuiot.dmp.archive.application.param.query.HouseKeeperQuery;
import cn.cuiot.dmp.archive.application.service.HouseKeeperService;
import cn.cuiot.dmp.archive.infrastructure.entity.HouseKeeperEntity;
import cn.cuiot.dmp.base.application.service.ApiSystemService;
import cn.cuiot.dmp.base.infrastructure.dto.DeleteParam;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 【PC】管家管理
 * @author wuyongchong
 * @since 2024-06-07
 */
@RestController
@RequestMapping("/house-keeper")
public class HouseKeeperController {

    @Autowired
    private HouseKeeperService houseKeeperService;

    @Autowired
    private ApiSystemService apiSystemService;

    /**
     * 分页查询
     * @param query
     * @return
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<HouseKeeperEntity>> queryForPage(@RequestBody HouseKeeperQuery query) {
        Long currentDeptId = LoginInfoHolder.getCurrentDeptId();
        DepartmentDto departmentDto = apiSystemService.lookUpDepartmentInfo(currentDeptId,null,null);
        if(Objects.isNull(departmentDto)){
          throw  new BusinessException(ResultCode.OBJECT_NOT_EXIST, "当前登录人员所属组织信息获取失败");
        }
        query.setDeptPath(departmentDto.getPath());
        IPage<HouseKeeperEntity> pageData = houseKeeperService.queryForPage(query);
        return IdmResDTO.success(pageData);
    }

    @PostMapping("/queryForDetail")
    public IdmResDTO<HouseKeeperEntity> queryForDetail(@RequestBody @Valid IdParam idParam) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        HouseKeeperEntity data = houseKeeperService.queryForDetail(idParam.getId(),currentOrgId);
        return IdmResDTO.success(data);
    }


    @PostMapping("/create")
    public IdmResDTO create(@RequestBody @Valid HouseKeeperDto createDto) {

        return IdmResDTO.success();
    }

    @PostMapping("/update")
    public IdmResDTO update(@RequestBody @Valid HouseKeeperDto updateDto) {


        return IdmResDTO.success();
    }

    @PostMapping("/delete")
    public IdmResDTO delete(@RequestBody @Valid DeleteParam deleteParam) {

        return IdmResDTO.success();
    }

    @PostMapping("/updateStatus")
    public IdmResDTO updateStatus(@RequestBody @Valid UpdateStatusParam updateStatusParam) {

        return IdmResDTO.success();
    }

}
