package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.infrastructure.entity.dto.CompanyReqDto.CompanyDetailReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.CompanyReqDto.UpdateCompanyReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.CompanyResDto.CompanyDetailResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.InsertOrganizationDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ListOrganizationDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OperateOrganizationDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrgCsvDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrgTypeDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrganizationResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ResetUserPasswordReqDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UpdateOrganizationDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserDataReqDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserDataResDTO;
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
     *
     * @param orgId
     * @return
     */
    OrganizationResDTO getOneById(String orgId);


    /**
     * 新增账户
     *
     * @param dto
     * @return
     */
    Long insertOrganization(InsertOrganizationDto dto);

    /**
     * 编辑子账户
     *
     * @param dto
     * @return
     */
    void updateOrganization(UpdateOrganizationDto dto);

    /**
     * 账户重置密码
     * @param dto
     * @return
     */
    OrgCsvDto resetOrgPassword(UpdateOrganizationDto dto);


    /**
     * 查询选择账户详情
     *
     * @param orgId
     * @param sessionUserId
     * @param sessionOrgId
     * @return
     */
    GetOrganizationVO findOne(String orgId, String sessionUserId, String sessionOrgId);

    /**
     * 企业/个人账户列表
     * @param dto
     * @return
     * @Author xieSH
     * @Description 企业/个人账户列表
     * @Date 2021/11/25 10:47
     **/
    PageResult<ListOrganizationVO> commercialOrgList(ListOrganizationDto dto);

    /**
     * 启用/禁用 账户
     * @param dto
     * @return
     * @Author xieSH
     * @Description 启用/禁用 账户
     * @Date 2021/11/16 9:44
     **/
    Integer operateOrganization(OperateOrganizationDto dto);

    /**
     * 查询账户类型展示列表
     *
     * @return
     */
    List<OrgTypeDto> getOrgTypeList();

    /**
     * 账户删除
     * @param orgId
     * @return
     */
    int deleteAccount(String orgId);

    /**
     * 查询公司详情，仅物业用户可用，账户下用户均可查询
     * @param companyDetailReqDto 公司详情请求dto
     * @return
     */
    CompanyDetailResDto companyDetail(CompanyDetailReqDto companyDetailReqDto);

    /**
     * 编辑公司详情
     * @param updateCompanyReqDto 编辑公司请求dto
     */
    void updateCompany(UpdateCompanyReqDto updateCompanyReqDto);

    /**
     * 账户详情下的用户列表
     * @param userDataReqDTO
     * @return 用户分页列表
     */
    PageResult<UserDataResDTO> queryUserPageList(UserDataReqDTO userDataReqDTO);

    /**
     * 账户详情下的用户,重置密码
     * @param resetUserPasswordReqDTO
     */
    void resetUserPassword(ResetUserPasswordReqDTO resetUserPasswordReqDTO);

}
