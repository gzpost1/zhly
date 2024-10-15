package cn.cuiot.dmp.baseconfig.controller.workorder;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.baseconfig.flow.dto.approval.MyApprovalResultDto;
import cn.cuiot.dmp.baseconfig.flow.dto.approval.QueryMyApprovalDto;
import cn.cuiot.dmp.baseconfig.flow.dto.work.QueryCommitProcessDto;
import cn.cuiot.dmp.baseconfig.flow.entity.CommitProcessEntity;
import cn.cuiot.dmp.baseconfig.flow.entity.WorkInfoEntity;
import cn.cuiot.dmp.baseconfig.flow.service.WorkInfoService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 待审批
 *
 * @Description 查询列表
 * @author pengjian
 * @create 2024/4/29 11:19
 */
@RestController
@RequestMapping("/workorder/approval")
public class ApprovalController {
    @Autowired
    private WorkInfoService workInfoService;

    /**
     * 待审批列表
     * @param dto
     * @return
     */
    @PostMapping("queryMyNotApproval")
    @RequiresPermissions
    public IdmResDTO<IPage<MyApprovalResultDto>> queryMyNotApproval(@RequestBody QueryMyApprovalDto dto){
        return workInfoService.queryMyNotApproval(dto);
    }

    /**
     * 待审批工单导出
     * @param dto
     * @return
     */
    @PostMapping("exportMyNotApproval")
    @RequiresPermissions
    public IdmResDTO exportMyNotApproval(@RequestBody QueryMyApprovalDto dto) throws Exception {
         workInfoService.export(dto);
        return IdmResDTO.success();
    }
    /**
     * 已审批列表
     * @param dto
     * @return
     */
    @PostMapping("queryMyApproval")
    @RequiresPermissions
    public IdmResDTO<IPage<MyApprovalResultDto>> queryMyApproval(@RequestBody QueryMyApprovalDto dto){
        return workInfoService.queryMyApproval(dto);
    }

    /**
     * 导出已审批数据
     * @param dto
     * @return
     * @throws Exception
     */
    @PostMapping("exportMyApproval")
    @RequiresPermissions
    public IdmResDTO exportMyApproval(@RequestBody QueryMyApprovalDto dto) throws Exception {
        workInfoService.exportMyApproval(dto);
        return IdmResDTO.success();
    }

    /**
     * 抄送列表
     * @param dto
     * @return
     */
    @PostMapping("queryMakeApproval")
    @RequiresPermissions
    public  IdmResDTO<IPage<MyApprovalResultDto>> queryMakeApproval(@RequestBody QueryMyApprovalDto dto){
        return workInfoService.queryMakeApproval(dto);
    }

    /**
     * 导出抄送列表
     * @param dto
     * @return
     */
    @PostMapping("exportMakeApproval")
    @RequiresPermissions
    public IdmResDTO exportMakeApproval(@RequestBody QueryMyApprovalDto dto) throws Exception {
        workInfoService.exportMakeApproval(dto);
        return IdmResDTO.success();
    }

    /**
     * 获取我提交的
     * @param dto
     * @return
     */
    @PostMapping("queryMySubmitWorkInfo")
    @RequiresPermissions
    public IdmResDTO<IPage<WorkInfoEntity>> queryMySubmitWorkInfo(@RequestBody QueryMyApprovalDto dto){
        return workInfoService.queryMySubmitWorkInfo(dto);
    }

    /**
     * 导出我提交的
     * @param dto
     * @return
     * @throws Exception
     */
    @PostMapping("exportMySubmitWorkInfo")
    @RequiresPermissions
    public IdmResDTO exportMySubmitWorkInfo(@RequestBody QueryMyApprovalDto dto) throws Exception {
        workInfoService.exportMySubmitWorkInfo(dto);
       return IdmResDTO.success();
    }

    /**
     * 获取用户已提交的信息开始节点
     * @param dto
     * @return
     */
    @PostMapping("queryCommitProcess")
    @RequiresPermissions
    public IdmResDTO<CommitProcessEntity> queryCommitProcess(@RequestBody @Valid QueryCommitProcessDto dto){
        return workInfoService.queryCommitProcess(dto);
    }
}
