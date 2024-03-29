package cn.cuiot.dmp.system.infrastructure.persistence.dao;

import cn.cuiot.dmp.system.infrastructure.entity.UserDataEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ListOrganizationDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrgLabelDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrgTypeDto;
import cn.cuiot.dmp.system.infrastructure.entity.vo.ListOrganizationVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author shixh
 * @className OrganizationDao
 * @description
 */
@Mapper
public interface OrganizationDao {


        /**
         * 查询 企业/个人账户列表
         * @param dto
         * @return
         */
        List<ListOrganizationVO> getCommercialOrgList(ListOrganizationDto dto);

        /**
         * 查询该账户最后登录的用户
         *
         * @param orgId
         * @return
         */
        UserDataEntity getLastOnlineUser(@Param("id") String orgId);


        /**
         * 查找组织下是否挂了账户
         *
         * @param deptId
         * @return
         */
        int countOrgByDeptId(Long deptId);


        /**
         * 查询账户类型
         *
         * @return
         */
        @Select("select * from organization_type where is_show = 1 and is_delete = 0")
        List<OrgTypeDto> getOrgTypeList();

        /**
         * 查询账户类型
         * @param id
         * @return
         */
        @Select("select * from organization_type where id = #{id} and is_delete = 0")
        OrgTypeDto getOrgType(Integer id);

        /**
         * 新增账户标签信息
         *
         * @param orgLabelDto
         * @return
         */
        int insertOrgLabel(OrgLabelDto orgLabelDto);

        /**
         * 修改账户标签信息
         *
         * @param orgLabelDto
         * @return
         */
        int updateOrgLabel(OrgLabelDto orgLabelDto);

        /**
         * 根据orgId查询账户标签信息
         *
         * @param orgId 账户id
         * @return
         */
        @Select("select * from org_label where org_id = #{orgId}")
        OrgLabelDto getOrgLabelById(@Param("orgId") String orgId);

        /**
         * 根据账户id查询用户标签
         *
         * @param orgId
         * @return
         */
        Integer getUserLabelByOrg(@Param("orgId") String orgId);

        /**
         * 根据用户手机号查询租户id
         * @param safePhoneNum 加密后的手机号
         * @return
         */
        String getOrgIdByUserPhoneNumber(@Param("safePhoneNum") String safePhoneNum);

        int delOrgLab(String orgId);

        int delOrgRole(String orgId);

}

