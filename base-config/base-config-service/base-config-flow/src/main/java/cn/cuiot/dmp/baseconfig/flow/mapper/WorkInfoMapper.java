package cn.cuiot.dmp.baseconfig.flow.mapper;

import cn.cuiot.dmp.baseconfig.flow.dto.app.BaseDto;
import cn.cuiot.dmp.baseconfig.flow.dto.app.query.PendingProcessQuery;
import cn.cuiot.dmp.baseconfig.flow.dto.approval.MyApprovalResultDto;
import cn.cuiot.dmp.baseconfig.flow.dto.approval.QueryMyApprovalDto;
import cn.cuiot.dmp.baseconfig.flow.dto.work.WorkInfoDto;
import cn.cuiot.dmp.baseconfig.flow.dto.work.WorkProcInstDto;
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
}
