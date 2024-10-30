package cn.cuiot.dmp.lease.controller;


import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseCurdController;
import cn.cuiot.dmp.base.application.enums.ContractEnum;
import cn.cuiot.dmp.base.application.service.ApiArchiveService;
import cn.cuiot.dmp.base.infrastructure.dto.BaseVO;
import cn.cuiot.dmp.base.infrastructure.dto.req.CustomConfigDetailReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CustomConfigDetailRspDTO;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.lease.dto.contract.*;
import cn.cuiot.dmp.lease.entity.TbContractCancelEntity;
import cn.cuiot.dmp.lease.entity.TbContractIntentionEntity;
import cn.cuiot.dmp.lease.entity.TbContractLeaseEntity;
import cn.cuiot.dmp.lease.service.*;
import cn.cuiot.dmp.lease.vo.ContractBoardInfoVo;
import cn.cuiot.dmp.lease.vo.ContractBoardInfoVoResult;
import cn.cuiot.dmp.lease.vo.ContractBoardVo;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cn.cuiot.dmp.common.constant.AuditContractConstant.*;
import static cn.cuiot.dmp.lease.service.BaseContractService.RELATE_INTENTION_TYPE;


/**
 * 租赁看板
 *
 * @author Mujun
 * @since 2024-10-22
 */
@Slf4j
@RestController
@RequestMapping("/contract")
public class ContractBoardController extends BaseCurdController<TbContractIntentionService, TbContractIntentionEntity, TbContractIntentionParam> {

    @Autowired
    ApiArchiveService apiArchiveService;
    @Autowired
    ContractBoardService contractBoardService;


    /**
     * 租赁看板
     *
     * @return
     */
    @RequiresPermissions
    @PostMapping("/board")
    public ContractBoardInfoVoResult board() {
        ContractBoardInfoVoResult contractBoardInfoVo = contractBoardService.queryContractBoardInfo();
        return contractBoardInfoVo;
    }

}
