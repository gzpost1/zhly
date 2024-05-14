package cn.cuiot.dmp.baseconfig.controller.workorder;

import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.baseconfig.flow.constants.WorkOrderConstants;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.Properties;
import cn.cuiot.dmp.baseconfig.flow.dto.vo.HandleDataVO;
import cn.cuiot.dmp.baseconfig.flow.dto.work.FirstFormDto;
import cn.cuiot.dmp.baseconfig.flow.dto.work.HandleDataDTO;
import cn.cuiot.dmp.baseconfig.flow.dto.work.WorkInfoDto;
import cn.cuiot.dmp.baseconfig.flow.dto.work.WorkProcInstDto;
import cn.cuiot.dmp.baseconfig.flow.service.WorkInfoService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public IdmResDTO<Properties> queryFirstFormInfo(@RequestBody FirstFormDto dto){

        return workInfoService.queryFirstFormInfo(dto, LoginInfoHolder.getCurrentUserId());
    }

    /**
     * 工单列表分页查询
     * @param dto
     * @return
     */
    @PostMapping("queryWorkOrderInfo")
    public IdmResDTO<IPage<WorkInfoDto>> queryWorkOrderInfo(@RequestBody WorkInfoDto dto){

        return workInfoService.queryWorkOrderInfo(dto );
    }

    /**
     * 所属组织
     * @param dto
     * @return
     */
    @PostMapping("queryDeptList")
    public List<DepartmentDto> queryDeptList(@RequestBody WorkInfoDto dto){
        return workInfoService.queryDeptList(dto);
    }
    /**
     * 详情基本信息
     * @param dto
     * @return
     */
    @PostMapping("queryBasicWorkOrderDetailInfo")
    public IdmResDTO<WorkInfoDto> queryBasicWorkOrderDetailInfo(@RequestBody WorkProcInstDto dto){
        return workInfoService.queryBasicWorkOrderDetailInfo(dto);
    }

    /**
     * 详情流程信息
     * @param HandleDataDTO
     * @return
     */
    @PostMapping("instanceInfo")
    public IdmResDTO<HandleDataVO>  instanceInfo(@RequestBody HandleDataDTO HandleDataDTO){

        return workInfoService.instanceInfo(HandleDataDTO);
    }
}
