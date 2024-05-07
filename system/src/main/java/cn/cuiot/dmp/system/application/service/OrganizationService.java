package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.application.param.event.OrganizationActionEvent;
import cn.cuiot.dmp.system.infrastructure.entity.dto.InsertOrganizationDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ListOrganizationDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OperateOrganizationDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrgCsvDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrgTypeDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrganizationChangeDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrganizationResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ResetUserPasswordReqDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UpdateOrganizationDto;
import cn.cuiot.dmp.system.infrastructure.entity.vo.GetOrganizationVO;
import cn.cuiot.dmp.system.infrastructure.entity.vo.ListOrganizationVO;
import java.util.List;

/**
 * @author shixh
 * @classname OrganizationService
 * @description 组织service
 * @date 2022/11/24
 */
public interface OrganizationService {

    /**
     * 查询登录者所属账户详情
     */
    OrganizationResDTO getOneById(String orgId);


    /**
     * 新增账户
     */
    Long insertOrganization(InsertOrganizationDto dto);

    /**
     * 编辑子账户
     */
    void updateOrganization(UpdateOrganizationDto dto);

    /**
     * 账户重置密码
     */
    OrgCsvDto resetOrgPassword(UpdateOrganizationDto dto);


    /**
     * 查询选择账户详情
     */
    GetOrganizationVO findOne(String orgId, String sessionUserId, String sessionOrgId);

    /**
     * 企业/个人账户列表
     *
     * @Author xieSH
     * @Description 企业/个人账户列表
     * @Date 2021/11/25 10:47
     **/
    PageResult<ListOrganizationVO> commercialOrgList(ListOrganizationDto dto);

    /**
     * 查询账户类型展示列表
     */
    List<OrgTypeDto> getOrgTypeList();

    /**
     * 账户删除
     */
    int deleteAccount(String orgId);

    /**
     * 账户详情下的用户,重置密码
     */
    void resetUserPassword(ResetUserPasswordReqDTO resetUserPasswordReqDTO);

    /**
     * 启停用
     */
    void updateStatus(UpdateStatusParam updateStatusParam, String sessionUserId,
            String sessionOrgId);

    /**
     * 记录变更日志
     */
    void recordOrganizationChange(OrganizationActionEvent event);

    /**
     * 查询企业变更记录
     */
    List<OrganizationChangeDto> selectOrganizationChangeByOrgId(String orgId, String sessionUserId,
            String sessionOrgId);

    /**
     * 获得变更详情内容
     */
    OrganizationChangeDto getOrganizationChangeById(Long id, String sessionUserId,
            String sessionOrgId);
}
