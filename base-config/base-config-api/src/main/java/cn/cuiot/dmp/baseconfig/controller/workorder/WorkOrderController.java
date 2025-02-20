package cn.cuiot.dmp.baseconfig.controller.workorder;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.baseconfig.flow.dto.StartProcessInstanceDTO;
import cn.cuiot.dmp.baseconfig.flow.dto.app.ProcessResultDto;
import cn.cuiot.dmp.baseconfig.flow.dto.app.query.UserSubmitDataDto;
import cn.cuiot.dmp.baseconfig.flow.dto.vo.HandleDataVO;
import cn.cuiot.dmp.baseconfig.flow.dto.work.*;
import cn.cuiot.dmp.baseconfig.flow.entity.CommitProcessEntity;
import cn.cuiot.dmp.baseconfig.flow.enums.WorkSourceEnums;
import cn.cuiot.dmp.baseconfig.flow.service.WorkInfoService;
import cn.cuiot.dmp.baseconfig.flow.vo.WorkInfoStatisticVO;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 工单信息
 * @author pengjian
 * @create 2024/4/25 11:27
 */
@RestController
@RequestMapping("/work")
public class WorkOrderController extends BaseController {

    @Autowired
    private WorkInfoService workInfoService;

    /**
     * 查询节点属性数据,并判断是否有权限发起该流程
     * @return
     */
    @PostMapping("queryFirstFormInfo")
    public IdmResDTO queryFirstFormInfo(@RequestBody FirstFormDto dto){

        return workInfoService.queryFirstFormInfo(dto, LoginInfoHolder.getCurrentUserId());
    }

    /**
     * 工单列表分页查询
     * @param dto
     * @return
     */
    @RequiresPermissions
    @PostMapping("queryWorkOrderInfo")
    public IdmResDTO<IPage<WorkInfoDto>> queryWorkOrderInfo(@RequestBody WorkInfoDto dto){
        return workInfoService.queryWorkOrderInfo(dto );
    }

    /**
     * 工单导出(20240601)
     * @param dto
     * @return
     */
    @PostMapping("exportWorkOrder")
    @RequiresPermissions
    public IdmResDTO exportWorkOrder(@RequestBody WorkInfoDto dto) throws Exception {
         workInfoService.exportWorkOrder(dto );
        return IdmResDTO.success();
    }
    /**
     * 客户工单列表
     * @param dto
     * @return
     */
    @RequiresPermissions
    @PostMapping("queryCustomerWorkOrderInfo")
    public IdmResDTO<IPage<WorkInfoDto>> queryCustomerWorkOrderInfo(@RequestBody WorkInfoDto dto){
        return workInfoService.queryCustomerWorkOrderInfo(dto );
    }

    /**
     * 所属组织
     * @param dto
     * @return
     */
    @PostMapping("queryDeptList")
    @RequiresPermissions
    public List<DepartmentDto> queryDeptList(@RequestBody WorkInfoDto dto){
        return workInfoService.queryDeptList(dto);
    }
    /**
     * 详情基本信息
     * @param dto
     * @return
     */
    @RequiresPermissions
    @PostMapping("queryBasicWorkOrderDetailInfo")
    public IdmResDTO<WorkInfoDto> queryBasicWorkOrderDetailInfo(@RequestBody WorkProcInstDto dto){
        return workInfoService.queryBasicWorkOrderDetailInfo(dto);
    }

    /**
     * 详情流程信息
     * @param HandleDataDTO
     * @return
     */
//    @RequiresPermissions
    @PostMapping("instanceInfo")
    public IdmResDTO<HandleDataVO>  instanceInfo(@RequestBody HandleDataDTO HandleDataDTO){

        return workInfoService.instanceInfo(HandleDataDTO);
    }

    /**
     * 获取提交的表单数据
     * @param HandleDataDTO
     * @return
     */
    @RequiresPermissions
    @PostMapping("queryDataForm")
    public IdmResDTO queryDataForm(@RequestBody HandleDataDTO HandleDataDTO){
        return workInfoService.queryDataForm(HandleDataDTO);
    }

    /**
     * 4.2.1 客户工单列表查询
     * @param req
     * @return
     */
    @RequiresPermissions
    @PostMapping("queryCustomerWorkOrder")
    public IdmResDTO<IPage<CustomerWorkOrderDto>> queryCustomerWorkOrder(@RequestBody QueryCustomerWorkOrderDto req){
        return workInfoService.queryCustomerWorkOrder(req);
    }


    /**
     * 导出客户工单信息
     * @param req
     * @return
     */
    @PostMapping("exportCustomerWorkOrder")
    @RequiresPermissions
    public IdmResDTO exportCustomerWorkOrder(@RequestBody QueryCustomerWorkOrderDto req) throws Exception {
        workInfoService.exportCustomerWorkOrder(req);
        return IdmResDTO.success();
    }
    /**
     * 4.2.1-客户工单-代录客单
     * @param startProcessInstanceDTO
     * @return
     */
    @PostMapping("proxyRecordStart")
    @RequiresPermissions
    @LogRecord(operationCode = "proxyRecordStart", operationName = "代录工单", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    public IdmResDTO proxyRecordStart(@RequestBody StartProcessInstanceDTO startProcessInstanceDTO){
        startProcessInstanceDTO.setWorkSource(WorkSourceEnums.PROXY_CUSTOMER_RECORD.getCode());
        return workInfoService.start(startProcessInstanceDTO);
    }

    /**
     * 详情-获取提交的信息
     * @param dto
     * @return
     */
    @RequiresPermissions
    @PostMapping("queryCommitProcessInfo")
    public List<CommitProcessEntity> queryCommitProcessInfo(@RequestBody @Valid QueryCommitProcessDto dto){
        return workInfoService.queryCommitProcessInfo(dto);
    }


    /**
     * 详情中查询详情
     * @param dto
     * @return
     */
    @RequiresPermissions
    @PostMapping("queryUserSubmitData")
    public IdmResDTO<ProcessResultDto> queryUserSubmitData(@RequestBody @Valid UserSubmitDataDto dto){
        return workInfoService.queryUserSubmitData(dto);
    }



    /**
     * 2.3 工单待审批数量
     * @param dto
     * @return
     */
    @PostMapping("queryAgencyHandlingNumber")
    @RequiresPermissions
    public IdmResDTO<AgencyHandlingDto> queryAgencyHandlingNumber(@RequestBody QueryAgencyDto dto){
        return workInfoService.queryAgencyHandlingNumber(dto);
    }

    /**
     * 工单统计
     * @param dto StatisInfoReqDTO
     * @return WorkInfoStatisticResDTO
     */
    @PostMapping("/queryWorkOrderStatistic")
    public IdmResDTO<WorkInfoStatisticVO> queryWorkOrderStatistic(@RequestBody StatisInfoReqDTO dto){
        return workInfoService.queryWorkOrderStatistic(dto);
    }

}
