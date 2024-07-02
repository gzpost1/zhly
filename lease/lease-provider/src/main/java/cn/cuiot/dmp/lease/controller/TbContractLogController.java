package cn.cuiot.dmp.lease.controller;


import cn.cuiot.dmp.base.application.controller.BaseCurdController;
import cn.cuiot.dmp.lease.dto.contract.TbContractLogParam;
import cn.cuiot.dmp.lease.entity.TbContractLogEntity;
import cn.cuiot.dmp.lease.service.TbContractLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 合同操作日志
 *
 * @author Mujun
 * @since 2024-06-13
 */
@Slf4j
@RestController
@RequestMapping("/contractLog")
    public class TbContractLogController extends BaseCurdController<TbContractLogService,TbContractLogEntity, TbContractLogParam> {
}
