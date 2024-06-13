package cn.cuiot.dmp.baseconfig.flow.mapper;

import cn.cuiot.dmp.baseconfig.flow.dto.app.AppWorkInfoDto;
import cn.cuiot.dmp.baseconfig.flow.dto.app.BaseDto;
import cn.cuiot.dmp.baseconfig.flow.dto.app.RepairReportDto;
import cn.cuiot.dmp.baseconfig.flow.dto.app.query.PendingProcessQuery;
import cn.cuiot.dmp.baseconfig.flow.dto.app.query.RepairReportQuery;
import cn.cuiot.dmp.baseconfig.flow.dto.app.query.WorkOrderSuperQuery;
import cn.cuiot.dmp.baseconfig.flow.dto.approval.MyApprovalResultDto;
import cn.cuiot.dmp.baseconfig.flow.dto.approval.QueryMyApprovalDto;
import cn.cuiot.dmp.baseconfig.flow.dto.work.*;
import cn.cuiot.dmp.baseconfig.flow.entity.WorkInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author pengjian
 * @since 2024-04-23
 */
public interface WorkInfoMapper extends BaseMapper<WorkInfoEntity> {

    Page<WorkInfoDto> queryWorkOrderInfo(Page<WorkInfoDto> page, @Param("query") WorkInfoDto dto);

    Page<WorkInfoDto> queryCustomerWorkOrderInfo(Page<WorkInfoDto> page, @Param("query") WorkInfoDto dto);

    WorkInfoDto queryWorkOrderDetailInfo(@Param("query") WorkProcInstDto dto);

    Page<MyApprovalResultDto> queryMyNotApproval(Page<MyApprovalResultDto> myApprovalResultDtoPage,@Param("query") QueryMyApprovalDto dto);

    BaseDto queryMyNotApprocalCount(@Param("query") PendingProcessQuery query);

    Page<MyApprovalResultDto> queryMyApproval(Page<MyApprovalResultDto> page,@Param("query") QueryMyApprovalDto dto);

    Page<MyApprovalResultDto> queryMakeApproval(Page<MyApprovalResultDto> page,@Param("query") QueryMyApprovalDto dto);

    Page<WorkInfoEntity> queryMySubmitWorkInfo(Page<WorkInfoEntity> workInfoEntityPage, @Param("query") QueryMyApprovalDto dto);

    List<BaseDto> queryPendProcessList(@Param("query") PendingProcessQuery query);

    List<TaskUserInfoDto> queryTaskUserInfo(@Param("query") QueryTaskUserInfoDto dto);


    Page<AppWorkInfoDto> queryAppMySubmitWorkInfo(Page<AppWorkInfoDto> workInfoEntityPage , @Param("query") QueryMyApprovalDto dto);

    IPage<AppWorkInfoDto> queryWorkOrderSuper(Page<AppWorkInfoDto> objectPage, @Param("query") WorkOrderSuperQuery query);

    List<String> queryNodeType(@Param("query") PendingProcessQuery query);

    Page<AppWorkInfoDto> queryMyHandleInfo(Page<AppWorkInfoDto> page, @Param("query") WorkOrderSuperQuery query);

    Page<AppWorkInfoDto> queryMyApprove(Page<AppWorkInfoDto> page, @Param("query") WorkOrderSuperQuery query);

    Page<AppWorkInfoDto> queryMyMake(Page<AppWorkInfoDto> page, WorkOrderSuperQuery query);

    Page<RepairReportDto> queryReportRepairs(Page<RepairReportDto> page, RepairReportQuery query);

    Page<CustomerWorkOrderDto> queryCustomerWorkOrder(Page<CustomerWorkOrderDto> page, @Param("query") QueryCustomerWorkOrderDto req);
}
