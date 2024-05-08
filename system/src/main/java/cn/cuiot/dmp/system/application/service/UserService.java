package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.application.param.command.UpdateUserCommand;
import cn.cuiot.dmp.system.application.param.dto.UserDTO;
import cn.cuiot.dmp.system.infrastructure.entity.UserDataEntity;
import cn.cuiot.dmp.system.infrastructure.entity.bo.UserBo;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetUserDepartmentTreeLazyReqDto;
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
     * 新增用户
     */
    UserCsvDto insertUser(UserBo userBo);

    /**
     * 更改用户
     */
    IdmResDTO updateUser(UserBo userBo);

    /**
     * 根据userId查询用户信息
     *
     * @param userId 用户id
     */
    UserResDTO getUserById(String userId);

    /**
     * 更新用户表
     */
    int update(UpdateUserCommand updatedUser);

    /**
     * 获取user的orgId
     */
    String getOrgId(Long pkUserId);

    /**
     * 获取user的deptId
     *
     * @param pkUserId String
     * @param pkOrgId String
     * @return String
     */
    String getDeptId(String pkUserId, String pkOrgId);

    /**
     * 根据userId和orgId查询用户信息
     */
    UserResDTO getUserMenuByUserIdAndOrgId(String userId, String orgId);


    /**
     * 根据userId和orgId查询用户信息
     */
    UserResDTO getUserInfoByUserIdAndOrgId(String userId, String orgId);

    /**
     * 重置密码(不带短信会话id)
     *
     * @return userBo
     */
    SimpleStringResDTO updatePasswordByPhoneWithoutSid(UserBo userBo);

    /**
     * 用户列表筛选-分页
     */
    PageResult<UserDataResDTO> getPage(Map<String, Object> params, String sessionOrgId,
            int currentPage, int pageSize);

    /**
     * 修改密码
     */
    void updatePassword(UserDataEntity entity);

    /**
     * 修改手机号
     */
    void updatePhoneNumber(UserBo userBo);

    /**
     * 批量删除用户
     */
    int deleteUsers(UserBo userBo);

    /**
     * 虚拟用户重置密码
     */
    UserCsvDto resetPasswordWithOutSms(UserBo userBo);


    /**
     * 查询用户详情
     */
    UserDataResDTO getOne(String id, String sessionOrgId, String sessionUserId);

    /**
     * 用户管理组织树
     */
    List<GetDepartmentTreeLazyResDto> getUserDepartmentTreeLazy(
            GetUserDepartmentTreeLazyReqDto dto);

    /**
     * 获取标签类型列表
     */
    List<LabelTypeDto> getLabelTypeList(String labelType);

    /**
     * 获得用户所在组织得dGroup属性
     */
    Map<String, String> getDGroup(String orgId, String userId);

    /**
     * 根据条件查询用户（登录）
     */
    UserDTO getOneUser(String account, String safeAccount, String password);

    /**
     * 检查dept下有无用户或人员
     */
    String checkUserInDeptId(Long parkId);

    /**
     * 检查组织下是否存在用户
     */
    void checkDepartmentUser(Long orgId, String path);

}
