package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.system.application.service.SysDictDataService;
import cn.cuiot.dmp.system.application.service.SysDictTypeService;
import cn.cuiot.dmp.system.infrastructure.entity.SysDictType;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SysDictTypeParam;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SysDictTypeQuery;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统配置-数据字典管理
 *
 * @author: wuyongchong
 * @date: 2020/9/2 20:20
 */
@RestController
@RequestMapping("/sys/dict-type")
public class SysDictTypeController {

    @Autowired
    private SysDictTypeService sysDictTypeService;

    @Autowired
    private SysDictDataService sysDictDataService;

    /**
     * 字典类型列表
     */
    @PostMapping("/list")
    public IdmResDTO<IPage<SysDictType>> list(@RequestBody SysDictTypeQuery sysDictTypeQuery) {
        IPage<SysDictType> pageList = sysDictTypeService.pageList(sysDictTypeQuery);
        return IdmResDTO.success(pageList);
    }

    /**
     * 查看详情
     */
    @PostMapping("/detail")
    public IdmResDTO<SysDictType> detail(@RequestBody @Valid IdParam param) {
        SysDictType obj = Optional.ofNullable(sysDictTypeService.getById(param.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST, "找不到记录"));
        return IdmResDTO.success(obj);
    }

    /**
     * 创建字典
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteDictType", operationName = "创建字典", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/create")
    public IdmResDTO<SysDictType> create(@RequestBody @Valid SysDictTypeParam sysDictTypeParam) {
        SysDictType sysDictType = BeanUtil.copyProperties(sysDictTypeParam, SysDictType.class);
        sysDictTypeService.save(sysDictType);
        return IdmResDTO.success(sysDictType);
    }

    /**
     * 修改字典
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateDictType", operationName = "创建字典", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/update")
    public IdmResDTO<SysDictType> update(@RequestBody @Valid SysDictTypeParam sysDictTypeParam) {

        AssertUtil.notNull(sysDictTypeParam.getDictId(),
                new BusinessException(ResultCode.PARAM_NOT_NULL, "缺少参数"));

        SysDictType entity = Optional
                .ofNullable(sysDictTypeService.getById(sysDictTypeParam.getDictId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST, "找不到记录"));

        SysDictType sysDictType = BeanUtil.copyProperties(sysDictTypeParam, SysDictType.class);

        sysDictTypeService.updateById(sysDictType);

        return IdmResDTO.success(sysDictType);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteDictType", operationName = "创建字典", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/delete")
    public IdmResDTO delete(@RequestBody @Valid IdParam param) {
        SysDictType entity = Optional.ofNullable(sysDictTypeService.getById(param.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST, "找不到记录"));
        sysDictTypeService.removeById(param.getId());
        return IdmResDTO.success(null);
    }
}
