package cn.cuiot.dmp.externalapi.provider.controller.admin.hik;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.entity.hik.HikPersonEntity;
import cn.cuiot.dmp.externalapi.service.query.hik.*;
import cn.cuiot.dmp.externalapi.service.service.hik.HikPersonService;
import cn.cuiot.dmp.externalapi.service.vo.hik.HikPersonAuthorizeVO;
import cn.cuiot.dmp.externalapi.service.vo.hik.HikPersonAuthorizeValidityVO;
import cn.cuiot.dmp.externalapi.service.vo.hik.HikPersonPageVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 后台-海康人员信息
 *
 * @Author: zc
 * @Date: 2024-10-10
 */
@RestController
@RequestMapping("/hik/person")
public class HikPersonController {

    @Autowired
    private HikPersonService hikPersonService;

    /**
     * 创建人员基础信息 Step 1
     *
     * @return Long 主键id
     * @Param dto 参数
     */
    @RequiresPermissions
    @LogRecord(operationCode = "createPersonInfo", operationName = "创建人员基础信息", serviceType = "hikPerson", serviceTypeName = "海康人员管理")
    @PostMapping("createPersonInfo")
    public IdmResDTO<Long> createPersonInfo(@RequestBody @Valid HikPersonInfoCreateDto dto) {
        return IdmResDTO.success(hikPersonService.createPersonInfo(dto));
    }

    /**
     * 更新用户信息
     *
     * @return Long 主键id
     * @Param dto 参数
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updatePersonInfo", operationName = "更新用户信息", serviceType = "hikPerson", serviceTypeName = "海康人员管理")
    @PostMapping("updatePersonInfo")
    public IdmResDTO<Long> updatePersonInfo(@RequestBody @Valid HikPersonInfoUpdateDto dto) {
        return IdmResDTO.success(hikPersonService.updatePersonInfo(dto));
    }

    /**
     * 编辑照片信息 Step 2
     *
     * @return 人员信息id
     * @Param dto 参数
     */
    @RequiresPermissions
    @LogRecord(operationCode = "editFaceData", operationName = "编辑照片信息", serviceType = "hikPerson", serviceTypeName = "海康人员管理")
    @PostMapping("editFaceData")
    public IdmResDTO<Long> editFaceData(@RequestBody @Valid HikPersonFaceDataDto dto) {
        return IdmResDTO.success(hikPersonService.editFaceData(dto));
    }

    /**
     * 编辑授权配置 Step 3
     *
     * @Param dto 参数
     */
    @RequiresPermissions
    @LogRecord(operationCode = "editAuthorize", operationName = "编辑授权配置", serviceType = "hikPerson", serviceTypeName = "海康人员管理")
    @PostMapping("editAuthorize")
    public IdmResDTO<?> editAuthorize(@RequestBody @Valid HikPersonAuthorizeDto dto) {
        hikPersonService.editAuthorize(dto);
        return IdmResDTO.success();
    }

    /**
     * 分页查询
     *
     * @return IPage<HikPersonEntity>
     * @Param query 参数
     */
    @PostMapping("queryForPage")
    public IdmResDTO<IPage<HikPersonPageVO>> queryForPage(@RequestBody HikPersonPageQuery query) {
        return IdmResDTO.success(hikPersonService.queryForPage(query));
    }

    /**
     * 查询详情
     *
     * @return HikPersonEntity
     * @Param id 人员id
     */
    @PostMapping("queryForDetail")
    public IdmResDTO<HikPersonEntity> queryForDetail(@RequestBody @Valid IdParam param) {
        return IdmResDTO.success(hikPersonService.queryForDetail(param.getId()));
    }

    /**
     * 根据人员id查询照片信息
     *
     * @return 照片
     * @Param id 人员id
     */
    @PostMapping("queryFaceData")
    public IdmResDTO<String> queryFaceData(@RequestBody @Valid IdParam param) {
        return IdmResDTO.success(hikPersonService.queryFaceData(param.getId()));
    }

    /**
     * 根据人员id查询授权时效
     *
     * @return PersonAuthorizeValidityVO
     * @Param id 人员id
     */
    @PostMapping("queryAuthorizeValidity")
    public IdmResDTO<HikPersonAuthorizeValidityVO> queryAuthorizeValidity(@RequestBody @Valid IdParam param) {
        return IdmResDTO.success(hikPersonService.queryAuthorizeValidity(param.getId()));
    }

    /**
     * 查询授权分页
     *
     * @return IPage<HikPersonAuthorizeVO>
     * @Param query 参数
     */
    @PostMapping("queryAuthorizeForPage")
    public IdmResDTO<IPage<HikPersonAuthorizeVO>> queryAuthorizeForPage(@RequestBody @Valid HikPersonAuthorizePageQuery query) {
        return IdmResDTO.success(hikPersonService.queryAuthorizeForPage(query));
    }

    /**
     * 批量删除
     *
     * @Param ids 列表
     */
    @RequiresPermissions
    @LogRecord(operationCode = "delete", operationName = "批量删除", serviceType = "hikPerson", serviceTypeName = "海康人员管理")
    @PostMapping("delete")
    public IdmResDTO<?> delete(@RequestBody @Valid IdsParam param) {
        hikPersonService.delete(param.getIds());
        return IdmResDTO.success();
    }
}
