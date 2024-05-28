package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.system.application.service.SysDictDataService;
import cn.cuiot.dmp.system.infrastructure.entity.SysDictData;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SysDictDataParam;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SysDictDataQuery;
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
 * 系统配置-数据字典项管理
 *
 * @author: wuyongchong
 * @date: 2020/9/2 20:20
 */
@RestController
@RequestMapping("/sys/dict-data")
public class SysDictDataController {

    @Autowired
    private SysDictDataService sysDictDataService;

    /**
     * 字典项列表
     */
    @PostMapping("/list")
    public IdmResDTO<IPage<SysDictData>> list(
            @RequestBody @Valid SysDictDataQuery sysDictDataQuery) {
        IPage<SysDictData> page = sysDictDataService.pageList(sysDictDataQuery);
        return IdmResDTO.success(page);
    }

    /**
     * 查看详情
     */
    @PostMapping("/detail")
    public IdmResDTO<SysDictData> detail(@RequestBody @Valid IdParam param) {
        SysDictData obj = Optional.ofNullable(sysDictDataService.getById(param.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST, "找不到记录"));
        return IdmResDTO.success(obj);
    }

    /**
     * 创建字典项
     */
    @RequiresPermissions
    @LogRecord(operationCode = "createDictData", operationName = "创建字典项", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/create")
    public IdmResDTO create(@RequestBody @Valid SysDictDataParam sysDictDataParam) {
        AssertUtil
                .isFalse(sysDictDataService
                                .valueExists(sysDictDataParam.getDictId(), sysDictDataParam.getDataValue(),
                                        null),
                        "字典值已经存在");
        SysDictData entity = BeanMapper.map(sysDictDataParam, SysDictData.class);
        entity.setStatus(EntityConstants.ENABLED);
        sysDictDataService.save(entity);
        return IdmResDTO.success(null);
    }

    /**
     * 修改字典项
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateDictData", operationName = "修改字典项", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/update")
    public IdmResDTO update(@RequestBody @Valid SysDictDataParam sysDictDataParam) {
        AssertUtil
                .isFalse(sysDictDataService
                        .valueExists(sysDictDataParam.getDictId(), sysDictDataParam.getDataValue(),
                                sysDictDataParam.getDataId()), "字典值已经存在");
        SysDictData entity = BeanMapper.map(sysDictDataParam, SysDictData.class);
        sysDictDataService.updateById(entity);
        return IdmResDTO.success(null);
    }

    /**
     * 启用
     */
    @RequiresPermissions
    @LogRecord(operationCode = "enableDictData", operationName = "启用字典项", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/enable")
    public IdmResDTO enable(@RequestParam("id") Long id) {
        SysDictData entity = Optional.ofNullable(sysDictDataService.getById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST, "找不到记录"));
        entity.setStatus(EntityConstants.ENABLED);
        sysDictDataService.updateById(entity);
        return IdmResDTO.success(null);
    }

    /**
     * 禁用
     */
    @RequiresPermissions
    @LogRecord(operationCode = "disableDictData", operationName = "禁用字典项", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/disable")
    public IdmResDTO disable(@RequestParam("id") Long id) {
        SysDictData entity = Optional.ofNullable(sysDictDataService.getById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST, "找不到记录"));
        entity.setStatus(EntityConstants.DISABLED);
        sysDictDataService.updateById(entity);
        return IdmResDTO.success(null);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteDictData", operationName = "删除字典项", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/delete")
    public IdmResDTO delete(@RequestBody @Valid IdParam param) {
        SysDictData entity = Optional.ofNullable(sysDictDataService.getById(param.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST, "找不到记录"));
        sysDictDataService.removeById(param.getId());
        return IdmResDTO.success(null);
    }

}
