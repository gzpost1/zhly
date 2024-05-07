package cn.cuiot.dmp.baseconfig.controller.workorder;

import cn.cuiot.dmp.baseconfig.flow.dto.approval.MyApprovalResultDto;
import cn.cuiot.dmp.baseconfig.flow.dto.approval.QueryMyApprovalDto;
import cn.cuiot.dmp.baseconfig.flow.entity.WorkInfoEntity;
import cn.cuiot.dmp.baseconfig.flow.service.WorkInfoService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
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
    public IdmResDTO<IPage<MyApprovalResultDto>> queryMyNotApproval(@RequestBody QueryMyApprovalDto dto){
        return workInfoService.queryMyNotApproval(dto);
    }


    /**
     * 已审批列表
     * @param dto
     * @return
     */
    @PostMapping("queryMyApproval")
    public IdmResDTO<IPage<MyApprovalResultDto>> queryMyApproval(@RequestBody QueryMyApprovalDto dto){
        return workInfoService.queryMyApproval(dto);
    }

    /**
     * 抄送列表
     * @param dto
     * @return
     */
    @PostMapping("queryMakeApproval")
    public  IdmResDTO<IPage<MyApprovalResultDto>> queryMakeApproval(@RequestBody QueryMyApprovalDto dto){
        return workInfoService.queryMakeApproval(dto);
    }

    /**
     * 获取我提交的
     * @param dto
     * @return
     */
    @PostMapping("queryMySubmitWorkInfo")
    public IdmResDTO<IPage<WorkInfoEntity>> queryMySubmitWorkInfo(@RequestBody QueryMyApprovalDto dto){
        return workInfoService.queryMySubmitWorkInfo(dto);
    }
}
