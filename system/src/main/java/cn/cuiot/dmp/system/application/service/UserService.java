package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.application.param.command.UpdateUserCommand;
import cn.cuiot.dmp.system.application.param.dto.UserDTO;
import cn.cuiot.dmp.system.infrastructure.entity.UserDataEntity;
import cn.cuiot.dmp.system.infrastructure.entity.bo.UserBo;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetUserDepartmentTreeLazyReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.InsertUserDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.LabelTypeDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SimpleStringResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserCsvDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserDataResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserResDTO;
import java.util.List;
import java.util.Map;

/**
 * @Description UserService
 * @Author shixh
 * @Data 2022/11/24
 */
public interface UserService {

    /**
     * 根据userId查询用户信息
     *
     * @param userId 用户id
     * @return
     */
    UserResDTO getUserById(String userId);

    /**
     * 更新用户表
     *
     * @param updatedUser
     * @return
     */
    int update(UpdateUserCommand updatedUser);

    /**
     * 获取user的orgId
     *
     * @param pkUserId
     * @return
     */
    String getOrgId(Long pkUserId);

    /**
     * 获取user的deptId
     *
     * @param pkUserId String
     * @param pkOrgId  String
     * @return String
     */
    String getDeptId(String pkUserId, String pkOrgId);

    /**
     * 根据userId和orgId查询用户信息
     *
     * @param userId
     * @param orgId
     * @return
     */
    UserResDTO getUserMenuByUserIdAndOrgId(String userId, String orgId);


    /**
     * 根据userId和orgId查询用户信息
     *
     * @param userId
     * @param orgId
     * @return
     */
    UserResDTO getUserInfoByUserIdAndOrgId(String userId, String orgId);

    /**
     * 重置密码(不带短信会话id)
     *
     * @param userBo
     * @return userBo
     */
    SimpleStringResDTO updatePasswordByPhoneWithoutSid(UserBo userBo);

    /**
     * 用户列表筛选-分页
     *
     * @param params
     * @param sessionOrgId
     * @param currentPage
     * @param pageSize
     * @param orgId
     * @return
     */
    PageResult<UserDataResDTO> getPage(Map<String, Object> params, String sessionOrgId, int currentPage, int pageSize, String orgId);

    /**
     * 修改密码
     *
     * @param entity
     */
    void updatePassword(UserDataEntity entity);

    /**
     * 修改手机号
     *
     * @param userBo
     */
    void updatePhoneNumber(UserBo userBo);

    /**
     * 批量删除用户
     *
     * @param userBo
     * @return
     */
    int deleteUsers(UserBo userBo);

    /**
     * 更改用户对应角色权限
     *
     * @param userBo
     * @return
     */
    IdmResDTO updatePermit(UserBo userBo);

    /**
     * 虚拟用户重置密码
     *
     * @param userBo
     * @return
     */
    UserCsvDto resetPasswordWithOutSms(UserBo userBo);

    /**
     * 新增用户
     *
     * @param userBo
     * @return
     */
    UserCsvDto insertUser(UserBo userBo);

    /**
     * 查询用户详情
     *
     * @param id
     * @param sessionOrgId
     * @param sessionUserId
     * @return
     */
    UserDataResDTO getOne(String id, String sessionOrgId, String sessionUserId);

    /**
     * 用户管理组织树
     *
     * @param dto
     * @return
     */
    List<GetDepartmentTreeLazyResDto> getUserDepartmentTreeLazy(GetUserDepartmentTreeLazyReqDto dto);

    /**
     * 获取标签类型列表
     *
     * @param labelType
     * @return
     */
    List<LabelTypeDto> getLabelTypeList(String labelType);

    /**
     * 获得用户所在组织得dGroup属性
     *
     * @param orgId
     * @param userId
     * @return
     */
    Map<String, String> getDGroup(String orgId, String userId);

    /**
     * 根据条件查询用户（登录）
     *
     * @param account
     * @param safeAccount
     * @return
     */
    UserDTO getOneUser(String account, String safeAccount, String password);

    /**
     * 检查dept下有无用户或人员
     *
     * @param parkId
     * @return
     */
    String checkUserInDeptId(Long parkId);

    /**
     * 新增用户
     *
     * @param insertUserDTO
     * @param orgId
     */
    void insertUserD(InsertUserDTO insertUserDTO, String orgId, String loginUserName);

    /**
     * 检查组织下是否存在用户
     *
     * @param orgId
     * @param path
     */
    void checkDepartmentUser(Long orgId, String path);

}
