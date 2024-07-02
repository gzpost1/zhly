package cn.cuiot.dmp.lease.controller.charge;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.DeleteParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.TbChargePlain;
import cn.cuiot.dmp.lease.feign.SystemToFlowService;
import cn.cuiot.dmp.lease.service.charge.ChargeInfoFillService;
import cn.cuiot.dmp.lease.service.charge.TbChargePlainService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 收费管理-应收管理-自动生成计划
 *
 * @Description 自动生成计划
 * @Date 2024/6/20 10:38
 * @Created by libo
 */
@RestController
@RequestMapping("/chargeplain")
public class ChargePlainController {
    @Autowired
    private TbChargePlainService chargePlainService;
    @Autowired
    private SystemToFlowService systemToFlowService;
    @Autowired
    private ChargeInfoFillService chargeInfoFillService;

    /**
     * 获取分页
     *
     * @param query
     * @return
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<ChargePlainPageDto>> queryForPage(@RequestBody ChargePlainQuery query) {
        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());

        IPage<ChargePlainPageDto> tbFlowPageDtoIPage = chargePlainService.queryForPage(query);
        //填充操作用户名称
        if (Objects.nonNull(tbFlowPageDtoIPage) && CollectionUtils.isNotEmpty(tbFlowPageDtoIPage.getRecords())) {
            BaseUserReqDto baseUserReqDto = new BaseUserReqDto();
            List<Long> createUserIds = tbFlowPageDtoIPage.getRecords().stream().map(ChargePlainPageDto::getCreateUser).distinct().collect(Collectors.toList());
            baseUserReqDto.setUserIdList(createUserIds);

            List<BaseUserDto> baseUserDtos = systemToFlowService.lookUpUserList(baseUserReqDto);
            if (CollectionUtils.isNotEmpty(baseUserDtos)) {
                tbFlowPageDtoIPage.getRecords().forEach(e -> {
                    baseUserDtos.stream().filter(baseUserDto -> Objects.equals(e.getCreateUser(), baseUserDto.getId())).findFirst().ifPresent(baseUserDto -> e.setCreateUserName(baseUserDto.getName()));
                });
            }

            chargeInfoFillService.fillinfo(tbFlowPageDtoIPage.getRecords(), ChargePlainPageDto.class);

        }
        return IdmResDTO.success().body(tbFlowPageDtoIPage);
    }


    /**
     * 创建
     *
     * @param createDto
     * @return
     */
//    @RequiresPermissions
    @PostMapping("/create")
    @LogRecord(operationCode = "create", operationName = "自动生成计划-创建", serviceType = ServiceTypeConst.CHARGEPLAIN)
    public IdmResDTO create(@RequestBody @Valid ChargePlainInsertDto createDto) {

        chargePlainService.createData(createDto);

        return IdmResDTO.success();
    }

    /**
     * 更新
     *
     * @param updateDto
     * @return
     */
//    @RequiresPermissions
    @PostMapping("/update")
    @LogRecord(operationCode = "update", operationName = "自动生成计划-修改", serviceType = ServiceTypeConst.CHARGEPLAIN)
    public IdmResDTO update(@RequestBody @Valid ChargePlainUpdateDto updateDto) {

        chargePlainService.updateData(updateDto);

        return IdmResDTO.success();
    }

    /**
     * 更新状态
     *
     * @param updateStatusParam
     * @return
     */
//    @RequiresPermissions
    @PostMapping("/updateStatus")
    @LogRecord(operationCode = "updateStatus", operationName = "自动生成计划-更新状态", serviceType = ServiceTypeConst.CHARGEPLAIN)
    public IdmResDTO updateStatus(@RequestBody @Valid UpdateStatusParam updateStatusParam) {
        TbChargePlain entity = chargePlainService.getById(updateStatusParam.getId());
        AssertUtil.notNull(entity, "数据不存在");
        entity.setStatus(updateStatusParam.getStatus());
        chargePlainService.updateById(entity);
        return IdmResDTO.success();
    }

    /**
     * 删除
     *
     * @param deleteParam
     * @return
     */
//    @RequiresPermissions
    @PostMapping("/delete")
    @LogRecord(operationCode = "delete", operationName = "自动生成计划-删除", serviceType = ServiceTypeConst.CHARGEPLAIN)
    public IdmResDTO delete(@RequestBody @Valid DeleteParam deleteParam) {
        chargePlainService.removeById(deleteParam.getId());
        return IdmResDTO.success();
    }
}
