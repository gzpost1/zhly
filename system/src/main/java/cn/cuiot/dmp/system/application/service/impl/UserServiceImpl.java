package cn.cuiot.dmp.system.application.service.impl;

import static cn.cuiot.dmp.common.constant.ResultCode.INNER_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.PHONE_NUMBER_ALREADY_EXIST;
import static cn.cuiot.dmp.common.constant.ResultCode.PHONE_NUMBER_EXIST;
import static cn.cuiot.dmp.common.constant.ResultCode.RESET_PASSWORD_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.SMS_CODE_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.SMS_CODE_EXPIRED_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.SMS_TEXT_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.UNAUTHORIZED_ACCESS;
import static cn.cuiot.dmp.common.constant.ResultCode.USERNAME_IS_SENSITIVE_WORD;

import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.RegexConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.enums.LogLevelEnum;
import cn.cuiot.dmp.common.enums.StatusCodeEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.common.log.dto.OperateLogDto;
import cn.cuiot.dmp.system.application.service.OperateLogService;
import cn.cuiot.dmp.common.utils.Const;
import cn.cuiot.dmp.base.application.utils.IpUtil;
import cn.cuiot.dmp.common.utils.Sm4;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorker;
import cn.cuiot.dmp.domain.types.Address;
import cn.cuiot.dmp.domain.types.Email;
import cn.cuiot.dmp.domain.types.EncryptedValue;
import cn.cuiot.dmp.domain.types.IP;
import cn.cuiot.dmp.domain.types.Password;
import cn.cuiot.dmp.domain.types.PhoneNumber;
import cn.cuiot.dmp.domain.types.enums.OperateByTypeEnum;
import cn.cuiot.dmp.domain.types.id.OrganizationId;
import cn.cuiot.dmp.domain.types.id.UserId;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.system.application.enums.DepartmentGroupEnum;
import cn.cuiot.dmp.system.application.enums.OrgLabelEnum;
import cn.cuiot.dmp.system.application.enums.ResetPasswordEnum;
import cn.cuiot.dmp.system.application.enums.RoleTypeEnum;
import cn.cuiot.dmp.system.application.param.assembler.Organization2EntityAssembler;
import cn.cuiot.dmp.system.application.param.assembler.UserAssembler;
import cn.cuiot.dmp.system.application.param.assembler.UserMapper;
import cn.cuiot.dmp.system.application.param.command.UpdateUserCommand;
import cn.cuiot.dmp.system.application.param.dto.DepartmentUserDto;
import cn.cuiot.dmp.system.application.param.dto.UserDTO;
import cn.cuiot.dmp.system.application.service.UserService;
import cn.cuiot.dmp.system.application.service.VerifyService;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.entity.MenuEntity;
import cn.cuiot.dmp.system.infrastructure.entity.OrganizationEntity;
import cn.cuiot.dmp.system.infrastructure.entity.UserDataEntity;
import cn.cuiot.dmp.system.infrastructure.entity.bo.UserBo;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetUserDepartmentTreeLazyReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.InsertUserDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.LabelTypeDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrgLabelDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ResetPasswordReqDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RoleDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SimpleStringResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserCsvDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserDataResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserLabelDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserResDTO;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.DepartmentDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.MenuDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.OrgMenuDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.OrganizationDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.RoleDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.SpaceDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.UserDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.UserDataDao;
import cn.cuiot.dmp.base.application.utils.CommonCsvUtil;
import cn.cuiot.dmp.system.infrastructure.utils.RandomPwUtils;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.system.user_manage.domain.entity.Organization;
import cn.cuiot.dmp.system.user_manage.domain.entity.User;
import cn.cuiot.dmp.system.user_manage.domain.service.UserPhoneNumberDomainService;
import cn.cuiot.dmp.system.user_manage.domain.types.enums.UserTypeEnum;
import cn.cuiot.dmp.system.user_manage.query.UserCommonQuery;
import cn.cuiot.dmp.system.user_manage.repository.OrganizationRepository;
import cn.cuiot.dmp.system.user_manage.repository.UserRepository;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.PhoneUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.core.api.strategory.StrategyPhone;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author wqd
 * @classname UserPersonGroupServiceImpl
 * @description
 * @date 2022/8/10
 */
@Slf4j
@Service
public class UserServiceImpl extends BaseController implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserPhoneNumberDomainService userPhoneNumberDomainService;
    @Autowired
    private UserDataDao userDataDao;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private Organization2EntityAssembler organization2EntityAssembler;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    OperateLogService operateLogService;

    @Autowired
    private SpaceDao spaceDao;

    @Autowired
    private VerifyService verifyService;

    @Autowired
    private OrgMenuDao orgMenuDao;

    @Autowired
    private UserAssembler userAssembler;

    private static final String ROLE_NAME_LIKE = "roleNameLike";

    private static final String INIT = "init";

    @Value("${self.debug}")
    private String debug;

    /**
     * 雪花算法生成器
     */
    private final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);

    /**
     * 应用id长度常量
     */
    private static final String ROOT_USER_ID = "1";

    /**
     * 非 常量
     */
    private static final String FALSE = "false";

    /**
     * 注入密码生成类
     */
    @Autowired
    private RandomPwUtils randomPwUtils;

    /**
     * 自动注入stringRedisTemplate
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public int update(UpdateUserCommand updatedUser) {
        if (updatedUser.getId() == null) {
            return 0;
        }
        User user = User.builder().build();
        user.setId(new UserId(updatedUser.getId()));
        if (updatedUser.getEmail() != null) {
            user.setEmail(new Email(updatedUser.getEmail()));
        }
        user.setContactPerson(updatedUser.getContactPerson());
        if (updatedUser.getContactAddress() != null) {
            user.setContactAddress(new Address(updatedUser.getContactAddress()));
        }
        if (updatedUser.getLastOnlineIp() != null) {
            user.setLastOnlineIp(new IP(updatedUser.getLastOnlineIp()));
        }
        return userRepository.save(user) ? 1 : 0;
    }

    @Override
    public UserResDTO getUserById(String userId) {
        User userEntity = userRepository.find(new UserId(userId));
        if (userEntity == null) {
            // 账号不存在
            throw new BusinessException(ResultCode.USER_ACCOUNT_NOT_EXIST);
        }
        UserResDTO userResDTO = new UserResDTO();
        copyProperties(userEntity, userResDTO);
        userResDTO.setId(userId);
        Long orgId = userDao.getOrgId(Long.parseLong(userId));
        if (orgId != null) {
            userResDTO.setOrgId(String.valueOf(orgId));
        }
        return userResDTO;
    }

    private void copyProperties(User userEntity, UserResDTO userResDTO) {
        userResDTO.setId(userEntity.getId().getStrValue());
        userResDTO.setUserId(userEntity.getUserId());
        userResDTO.setUsername(userEntity.getUsername());
        userResDTO.setPassword(
                userEntity.getPassword() != null ? userEntity.getPassword().getHashEncryptValue()
                        : null);
        userResDTO.setEmail(
                userEntity.getEmail() != null ? userEntity.getEmail().getEncryptedValue() : null);
        userResDTO.setPhoneNumber(userEntity.getPhoneNumber() != null ? userEntity.getPhoneNumber()
                .getEncryptedValue() : null);
        userResDTO.setCreatedOn(userEntity.getCreatedOn());
        userResDTO.setAvatar(userEntity.getAvatar());
        userResDTO.setUserType(userEntity.getUserType().getValue());
        userResDTO.setLastOnlineOn(userEntity.getLastOnlineOn());
    }

    /**
     * 获取user的orgId
     */
    @Override
    public String getOrgId(Long pkUserId) {
        Long pkOrgId = null;
        try {
            pkOrgId = userDao.getOrgId(pkUserId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.INNER_ERROR);
        }
        return String.valueOf(pkOrgId);
    }

    /**
     * 获取user的deptId
     *
     * @param pkUserId String
     * @param pkOrgId String
     * @return String
     */
    @Override
    public String getDeptId(String pkUserId, String pkOrgId) {
        String pkDeptId;
        try {
            pkDeptId = userDao.getDeptId(pkUserId, pkOrgId);
        } catch (Exception e) {
            log.error("getDeptId error", e);
            throw new BusinessException(ResultCode.INNER_ERROR);
        }
        return pkDeptId;
    }

    @Override
    public UserResDTO getUserMenuByUserIdAndOrgId(String userId, String orgId) {
        // 获取用户信息
        UserResDTO userResDTO = getUserInfoByUserIdAndOrgId(userId, orgId);
        List<MenuEntity> menuList = menuDao.selectMenuListByRoleId(userResDTO.getRoleId());
        List<String> banMenuIdList = orgMenuDao.getBanMenuIdList(orgId);
        menuList.removeIf(o -> banMenuIdList.contains(o.getMenuId()));
        banMenuIdList.forEach(menuId -> {
            List<String> menuIdByParentMenuId = menuDao.getMenuIdByParentMenuIdString(menuId);
            if (!CollectionUtils.isEmpty(menuIdByParentMenuId)) {
                menuList.removeIf(o -> menuIdByParentMenuId.contains(o.getMenuId()));
                menuIdByParentMenuId.forEach(sonMenuId -> {
                    List<String> sonMenuIdByParentMenuId = menuDao
                            .getMenuIdByParentMenuIdString(sonMenuId);
                    if (!CollectionUtils.isEmpty(sonMenuIdByParentMenuId)) {
                        menuList.removeIf(o -> sonMenuIdByParentMenuId.contains(o.getMenuId()));
                    }
                });
            }
        });
        userResDTO.setMenu(menuList);
        return userResDTO;
    }

    @Override
    public UserResDTO getUserInfoByUserIdAndOrgId(String userId, String orgId) {
        // 获取用户信息
        User userEntity = userRepository.find(new UserId(userId));
        if (userEntity == null) {
            // 账号不存在
            throw new BusinessException(ResultCode.USER_ACCOUNT_NOT_EXIST);
        }
        UserResDTO userResDTO = userMapper.doToDTO(userEntity);
        // 查询用户组织的类型
        DepartmentDto userDept = departmentDao.getPathByUser(userId);
        if (userDept != null) {
            userResDTO.setDGroup(userDept.getDGroup());
            userResDTO.setOrgId(orgId);
        }
        // 获取账户信息
        Organization organization = organizationRepository.find(new OrganizationId(orgId));
        if (organization == null) {
            throw new BusinessException(ResultCode.ORG_IS_NOT_EXIST);
        }
        // 获取账户标签id
        OrgLabelDto orgLabelDto = organizationDao.getOrgLabelById(orgId);
        userResDTO.setOrgLabelTypeId(orgLabelDto == null ? null : orgLabelDto.getLabelId());
        userResDTO.setOrgTypeId(Math.toIntExact(organization.getOrgTypeId().getValue()));
        userResDTO.setOrgKey(organization.getOrgKey());
        userResDTO.setOrgName(organization.getOrgName());
        userResDTO.setMaxDeptHigh(organization.getMaxDeptHigh());
        // 不需要菜单权限
        RoleDTO roleDTO = roleDao.selectRoleByUserId(Long.parseLong(orgId), Long.parseLong(userId));
        if (roleDTO != null) {
            userResDTO.setRoleId(roleDTO.getId());
            userResDTO.setRoleKey(roleDTO.getRoleKey());
            userResDTO.setRoleName(roleDTO.getRoleName());
        }
        return userResDTO;
    }

    private SimpleStringResDTO updatePasswordCommon(ResetPasswordReqDTO resetPasswordReqDTO,
            User user,
            UserBo userBo, String uuid) {
        // 获取密码
        String password = resetPasswordReqDTO.getPassword();

        //获取操作对象
        String[] op = new String[1];
        op[0] = user.getUsername();
        userBo.setOperationTarget(op);

        user.setPassword(new Password(password));
        user.updatedByPortal();
        redisUtil.del(CacheConst.USER_CACHE_KEY_PREFIX + user.getId().getStrValue());
        boolean success = userRepository.save(user);
        // 修改成功
        if (success) {
            stringRedisTemplate.delete(CacheConst.SMS_CODE_TEXT_REDIS_KEY + uuid);

            return new SimpleStringResDTO("修改成功");
        }
        // 修改失败
        else {
            throw new BusinessException(RESET_PASSWORD_ERROR);
        }
    }


    @LogRecord(operationCode = "resetPassword", operationName = "重置密码", serviceType = ServiceTypeConst.SECURITY_SETTING)
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SimpleStringResDTO updatePasswordByPhoneWithoutSid(UserBo userBo) {
        ResetPasswordReqDTO resetPasswordReqDTO = userBo.getResetPasswordReqDTO();
        User user = userRepository.find(new UserId(userBo.getUserId()));
        String phoneNumber = user.getDecryptedPhoneNumber();
        // 获取redis中的验证码文本
        String expectedSmsText = stringRedisTemplate.opsForValue()
                .get(CacheConst.SMS_CODE_TEXT_REDIS_KEY_P
                        + userBo.getUserId() + phoneNumber);
        if (FALSE.equals(debug) && StringUtils.isEmpty(expectedSmsText)) {
            // 短信验证码过期
            throw new BusinessException(SMS_CODE_EXPIRED_ERROR);
        }
        if (FALSE.equals(debug)
                && !expectedSmsText.equals(phoneNumber + resetPasswordReqDTO.getSmsCode())) {
            throw new BusinessException(SMS_CODE_ERROR);
        }

        return updatePasswordCommon(resetPasswordReqDTO, user, userBo,
                userBo.getUserId() + phoneNumber);
    }

    /**
     * 用户列表筛选-分页
     */
    @Override
    public PageResult<UserDataResDTO> getPage(Map<String, Object> params, String sessionOrgId,
            int currentPage, int pageSize, String orgId) {
        PageInfo<UserDataResDTO> resultPageInfo;
        try {
            Organization organization = organizationRepository
                    .find(new OrganizationId(sessionOrgId));
            String orgOwner = String.valueOf(organization.getOrgOwner().getValue());
            List<UserDataEntity> entities;
            PageHelper.startPage(currentPage, pageSize);
            if (params.containsKey(ROLE_NAME_LIKE)) {
                entities = userDataDao.searchListByRole(params);
            } else {
                entities = userDataDao.searchList(params);
            }
            //对entities集合里的手机号解密、脱敏--2020/12/09
            IStrategy strategyPhone = new StrategyPhone();
            for (UserDataEntity userDataEntity : entities) {
                String phoneNumber = Sm4.decrypt(userDataEntity.getPhoneNumber());
                if (StringUtils.hasLength(phoneNumber)) {
                    final String phoneSensitive = (String) strategyPhone.des(phoneNumber, null);
                    userDataEntity.setPhoneNumber(phoneSensitive);
                }
                String email = userDataEntity.getEmail();
                if (StringUtils.hasLength(email)) {
                    userDataEntity.setEmail(DesensitizedUtil.email(email));
                }
            }

            // 创建分页
            PageInfo<UserDataEntity> pageInfo = new PageInfo<>(entities);
            resultPageInfo = userMapper.dataEntityListToDataDtoList(pageInfo);
            for (UserDataResDTO userDataResDTO : resultPageInfo.getList()) {
                //找到该用户对应的角色
                String pkUserId = userDataResDTO.getId();
                String roleId = userDao.getRoleId(pkUserId, sessionOrgId);
                userDataResDTO.setRoleId(roleId);
                // 找到角色名称
                String roleName = userDao.getRoleName(roleId);
                userDataResDTO.setRoleName(roleName);
                // 找到角色key
                String roleKey = userDao.getRoleKey(roleId);
                userDataResDTO.setRoleKey(roleKey);
                // 找到组织
                String deptId = userDao.getDeptId(pkUserId, sessionOrgId);
                userDataResDTO.setDeptId(deptId);
                // 找到组织名称
                if (StringUtils.hasLength(deptId)) {
                    DepartmentEntity departmentEntity = departmentDao
                            .selectByPrimary(Long.parseLong(deptId));
                    userDataResDTO.setDeptName(departmentEntity.getDepartmentName());
                }
                userDataResDTO
                        .setIsOwner(pkUserId.equals(orgOwner) || ROOT_USER_ID.equals(pkUserId));
            }
        } catch (BusinessException e) {
            log.error("getPage error", e);
            throw new BusinessException(INNER_ERROR);
        }
        return new PageResult<>(resultPageInfo);
    }

    /**
     * 修改密码(登录人自行修改)
     */
    @LogRecord(operationCode = "updatePassword", operationName = "修改密码（个人）", serviceType = ServiceTypeConst.SECURITY_SETTING)
    @Override
    public void updatePassword(UserDataEntity entity) {
        User user = User.builder().id(new UserId(entity.getUserId()))
                .password(new Password(entity.getPassword())).build();
        user.updatedByPortal();
        if (!userRepository.save(user)) {
            throw new BusinessException(ResultCode.UPDATE_PASSWORD_FAIL);
        }
    }

    /**
     * 修改手机号
     */
    @LogRecord(operationCode = "updatePhoneNumber", operationName = "修改手机号", serviceType = ServiceTypeConst.SECURITY_SETTING)
    @Override
    public void updatePhoneNumber(UserBo userBo) {
        String phoneNumber = userBo.getPhoneNumber();
        String userId = userBo.getUserId();
        String smsCode = userBo.getSmsCode();
        if (FALSE.equals(debug)) {
            // 获取redis中的短信验证码文本 邮箱验证
            String expectedText = stringRedisTemplate.opsForValue()
                    .get(CacheConst.SMS_CODE_TEXT_REDIS_KEY_P + userId + phoneNumber);
            // 判断是否过期
            if (StringUtils.isEmpty(expectedText)) {
                // 短信验证码过期
                throw new BusinessException(SMS_CODE_EXPIRED_ERROR);
            }
            // 判断手机号与验证码是否与redis中相同
            if (!expectedText.equals(phoneNumber + smsCode)) {
                // 安全要求： 短信验证码防暴力破解，关键操作每提交一次请求，应发送新的短信验证码，并且不可继续使用旧的验证码
                stringRedisTemplate
                        .delete(CacheConst.SMS_CODE_TEXT_REDIS_KEY_P + userId + phoneNumber);
                throw new BusinessException(SMS_TEXT_ERROR);
            }
        }
        //获取日志操作对象
        User userDataEntity = userRepository.find(new UserId(userId));
        log.info("修改手机号，user：" + JSON.toJSONString(userDataEntity));
        String[] op = new String[1];
        op[0] = userDataEntity.getUsername();
        userBo.setOperationTarget(op);

        // 判断手机号是否存在
        boolean exists = userPhoneNumberDomainService
                .judgePhoneNumberAlreadyExists(new PhoneNumber(phoneNumber), UserTypeEnum.USER,
                        UserTypeEnum.NULL);
        if (exists) {
            throw new BusinessException(PHONE_NUMBER_EXIST);
        }

        // 更改手机号
        userDataEntity.setPhoneNumber(new PhoneNumber(phoneNumber));
        userDataEntity.updatedByPortal();
        if (!userRepository.save(userDataEntity)) {
            throw new BusinessException(ResultCode.UPDATE_PASSWORD_FAIL);
        }
        stringRedisTemplate.delete(CacheConst.SMS_CODE_TEXT_REDIS_KEY_P + userId);
        // 设备模块使用了缓存，系统模块姓名或手机号变更需要清除redisKey
        redisUtil.del(CacheConst.USER_CACHE_KEY_PREFIX + userBo.getUserId());
    }

    /**
     * 批量删除用户
     */
    @LogRecord(operationCode = "deleteUsers", operationName = "删除用户", serviceType = ServiceTypeConst.ORGANIZATION_MANAGEMENT)
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUsers(UserBo userBo) {
        String sessionOrgId = userBo.getOrgId();
        List<Long> ids = userBo.getIds();
        String pkUserId = userBo.getUserId();
        // 查询该账户的账户所有者
        Long orgOwner = userDao.findOrgOwner(sessionOrgId);

        //获取操作对象
        String[] op = new String[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            User userDataEntity = userRepository.find(new UserId(ids.get(i)));
            op[i] = userDataEntity.getUsername();
        }
        userBo.setOperationTarget(op);

        DepartmentDto departmentDto = departmentDao.getPathByUser(userBo.getUserId());
        String deptTreePath = Optional.ofNullable(departmentDto).map(DepartmentDto::getPath)
                .orElse(null);
        if (StringUtils.isEmpty(deptTreePath)) {
            throw new BusinessException(ResultCode.OBJECT_NOT_EXIST);
        }

        Organization sessionOrg = organizationRepository
                .find(new OrganizationId(Long.valueOf(sessionOrgId)));
        for (Long id : ids) {
            // 根据userId查询orgId
            Long orgId = this.userDao.getOrgId(id);
            if (!sessionOrgId.equals(String.valueOf(orgId))) {
                throw new BusinessException(UNAUTHORIZED_ACCESS);
            }

            // 判断是否是自己
            if (id.equals(Long.parseLong(pkUserId))) {
                throw new BusinessException(ResultCode.CANNOT_DELETE_SELF);
            }
            // 判断该用户是不是账户的所有者
            if (id.equals(orgOwner)) {
                throw new BusinessException(ResultCode.CANNOT_DELETE_ORGOWNER);
            }

            // 组织权限限制
            String subTreePath = Optional.ofNullable(departmentDto).map(DepartmentDto::getPath)
                    .orElse(null);
            if (StringUtils.isEmpty(deptTreePath)) {
                throw new BusinessException(ResultCode.OBJECT_NOT_EXIST);
            }
            if (!subTreePath.startsWith(deptTreePath)) {
                throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
            }

        }
        int result = 0;

        result = userRepository
                .removeList(ids.stream().map(UserId::new).collect(Collectors.toList()));

        // 删除用户后，把用户与当前登录的账号的关联关系删除
        userDataDao.deleteUserOrgByUserPks(ids, sessionOrgId);
        // 再删除与角色的关联
        userDataDao.deleteUserRoleByUserPks(ids, sessionOrgId);

        userRepository.removeUserRelate(ids);
        for (Long id : ids) {
            // 设备模块使用了缓存，系统模块姓名或手机号变更需要清除redisKey
            redisUtil.del(CacheConst.USER_CACHE_KEY_PREFIX + id);
        }

        return result;
    }

    private void updatePasswordIfNeed(UserBo userBo) {
        if (ResetPasswordEnum.RESET.getCode().equals(userBo.getResetPassword())) {
            UserCsvDto userInfo = resetPasswordWithOutSms(userBo);
            log.info("用户数据存储");

            // 文件流输出
            List<JSONObject> jsonList = new ArrayList<>();
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSON(userInfo).toString());
            jsonList.add(jsonObject);

            List<Object> head = new ArrayList<>();
            head.add("username");
            head.add("password");

            response.setContentType("text/csv;charset=\"GBK\"");
            response.setHeader("Content-Disposition", "attachment; filename=credentials.csv");
            String[] split = userInfo.getUsername().split("@");
            String username = split[0];
            try {
                CommonCsvUtil.createCsvFile(head, jsonList, username + "credentials", response);
            } catch (UnsupportedEncodingException e) {
                log.error("updateUser error.", e);
            }
        }
    }

    private void updateInfosWithoutSms(UserBo userBo) {
        PhoneNumber newPhoneNumber = null;
        if (StringUtils.hasLength(userBo.getPhoneNumber())) {
            newPhoneNumber = new PhoneNumber(userBo.getPhoneNumber());
        }
        String userId = userBo.getUserId();
        //获取日志操作对象
        User user = userRepository.find(new UserId(userId));
        log.info("修改用户，user：" + JSON.toJSONString(user));

        if (newPhoneNumber != null && !newPhoneNumber.equals(user.getPhoneNumber())) {
            boolean exists = userPhoneNumberDomainService
                    .judgePhoneNumberAlreadyExists(newPhoneNumber, UserTypeEnum.USER,
                            UserTypeEnum.NULL);
            if (exists) {
                // 手机号已注册
                throw new BusinessException(PHONE_NUMBER_ALREADY_EXIST);
            }
        }
        user.setPhoneNumber(newPhoneNumber);
        user.setEmail(userBo.getEmail() != null ? new Email(userBo.getEmail()) : null);
        user.setContactPerson(userBo.getContactPerson());
        user.setContactAddress(
                userBo.getContactAddress() != null ? new Address(userBo.getContactAddress())
                        : null);
        user.updatedByPortal();
        userRepository.save(user);

        // 设备模块使用了缓存，系统模块姓名或手机号变更需要清除redisKey
        redisUtil.del(CacheConst.USER_CACHE_KEY_PREFIX + userBo.getUserId());
    }


    /**
     * 查询用户详情
     */
    @Override
    public UserDataResDTO getOne(String id, String sessionOrgId, String sessionUserId) {
        Long orgId = this.userDao.getOrgId(Long.valueOf(id));
        if (!sessionOrgId.equals(String.valueOf(orgId))) {
            throw new BusinessException(UNAUTHORIZED_ACCESS);
        }
        DepartmentEntity deptById = departmentDao
                .selectByPrimary(Long.parseLong(userDao.getDeptId(id, String.valueOf(orgId))));
        // 要查询的用户的组织
        String deptId = userDao.getDeptId(id, sessionOrgId);
        // 登陆用户的子组织
        //String sessionDeptId = userDao.getDeptId(sessionUserId, sessionOrgId);
        // 因为组织下挂了小区了，小区上又创建了维修工用户，下面的校验会导致无法查看小区的员工信息。
        User entity = userRepository.find(new UserId(id));
        if (entity == null) {
            throw new BusinessException(ResultCode.OBJECT_NOT_EXIST);
        }
        // 如果创建者是用户id,找到名称
        OperateByTypeEnum createdByType = entity.getCreatedByType();
        if (createdByType != null && createdByType.equals(OperateByTypeEnum.USER) && !StringUtils
                .isEmpty(entity.getCreatedBy())
                && NumberUtils.isParsable(entity.getCreatedBy())) {
            User creatorEntity = userRepository.find(new UserId(entity.getCreatedBy()));
            if (creatorEntity != null) {
                String creatorName = creatorEntity.getUsername();
                entity.setCreatedBy(creatorName);
            }
        }
        // 如果更新者是用户id,找到名称
        OperateByTypeEnum updatedByType = entity.getUpdatedByType();
        if (updatedByType != null && updatedByType.equals(OperateByTypeEnum.USER) && !StringUtils
                .isEmpty(entity.getUpdatedBy())) {
            User updatorEntity = userRepository.find(new UserId(entity.getUpdatedBy()));
            if (updatorEntity != null) {
                String updatorName = updatorEntity.getUsername();
                entity.setUpdatedBy(updatorName);
            }
        }

        UserDataResDTO userDataResDTO = userMapper.doToDataDTO(entity);

        userDataResDTO.setId(id);
        userDataResDTO.setDeptTreePath(deptById.getPath());
        //找到该用户对应的角色
        String pkUserId = userDataResDTO.getId();
        String roleId = userDao.getRoleId(pkUserId, sessionOrgId);
        userDataResDTO.setRoleId(roleId);
        // 找到角色名称
        String roleName = userDao.getRoleName(roleId);
        userDataResDTO.setRoleName(roleName);
        // 找到角色key
        String roleKey = userDao.getRoleKey(roleId);
        userDataResDTO.setRoleKey(roleKey);
        // 找到组织
        userDataResDTO.setDeptId(deptId);
        DepartmentEntity departmentEntity = departmentDao.selectByPrimary(Long.parseLong(deptId));
        userDataResDTO.setDeptName(departmentEntity.getDepartmentName());
        // 找到租户信息
        userDataResDTO.setOrgId(sessionOrgId);
        Organization org = organizationRepository
                .find(new OrganizationId(Long.valueOf(sessionOrgId)));
        userDataResDTO.setOrgName(org.getOrgName());
        String orgDeptId = userDao.getUserGrantDeptId(Long.parseLong(sessionOrgId));
        if (Strings.isNotBlank(orgDeptId)) {
            departmentEntity = departmentDao.selectByPrimary(Long.parseLong(orgDeptId));
            userDataResDTO.setOrgDeptName(departmentEntity.getDepartmentName());
        }
        //对手机号解密--2020/12/07 新增
        userDataResDTO.setPhoneNumber(entity.getDecryptedPhoneNumber());
        userDataResDTO.setEmail(entity.getEmail() != null ? entity.getEmail().decrypt() : null);
        UserLabelDto userLabelDto = userDao.getUserLabelById(id);
        userDataResDTO.setLabel(userLabelDto.getLabelId());
        userDataResDTO.setOtherLabelName(userLabelDto.getOtherLabelName());
        userDataResDTO.setLabelName(userLabelDto.getLabelName());
        return userDataResDTO;
    }

    @Override
    public UserCsvDto resetPasswordWithOutSms(UserBo userBo) {
        String userId = userBo.getUserId();
        String password = userBo.getPassword();
        //如果密码为空，则表示自动生成密码
        if (!StringUtils.hasLength(password)) {
            int i = (int) (8 + Math.random() * (20 - 8 + 1));
            password = randomPwUtils.getRandomPassword(i);

        } else {
            //不为空，表示已添加密码，进行正则判断
            if (!password.matches(RegexConst.PASSWORD_REGEX)) {
                throw new BusinessException(ResultCode.PASSWORD_IS_INVALID);
            }
        }

        // 判断手机号是否为当前登录人的手机号
        User userDataEntity = userRepository.find(new UserId(userId));

        // 更改密码
        userDataEntity.setPassword(new Password(password));
        userDataEntity.updatedByPortal();
        if (!userRepository.save(userDataEntity)) {
            throw new BusinessException(ResultCode.UPDATE_PASSWORD_FAIL);
        }
        UserCsvDto userCsvDto = new UserCsvDto(userDataEntity.getUsername(), password);
        return userCsvDto;
    }

    /**
     * 新增用户
     */
    @LogRecord(operationCode = "insertUserD", operationName = "新增用户", serviceType = ServiceTypeConst.ORGANIZATION_MANAGEMENT)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserCsvDto insertUser(UserBo userBo) {
        DepartmentEntity departmentEntity = departmentDao
                .selectByPrimary(
                        Long.parseLong(userDao.getDeptId(userBo.getUserId(), userBo.getOrgId())));

        DepartmentEntity entity = departmentDao.selectByPrimary(Long.parseLong(userBo.getDeptId()));
        // 权限操作
        if (!entity.getPath().startsWith(departmentEntity.getPath())) {
            throw new BusinessException(ResultCode.DEPARTMENT_ULTRA_VIRES);
        }
        //获取日志操作对象
        String[] op = new String[1];
        op[0] = userBo.getUserName();
        userBo.setOperationTarget(op);

        // 入参组织权限校验(限制用户组织下小区级及以上组织)
        List<RoleDTO> roleList = roleDao.selectRoleListByOrgId(userBo.getOrgId());
        //判断账户下是否有这个角色id
        int num = 0;
        RoleDTO role = null;
        for (RoleDTO roleDTO : roleList) {
            if (roleDTO.getId().equals(userBo.getRoleId())) {
                num++;
                role = roleDTO;
            }
        }
        if (num == 0) {
            throw new BusinessException(ResultCode.ROLE_NOT_EXIST);
        }
        // 安全要求：和前端保持一致，前端不允许选择默认角色（置灰了），所以后端也要校验，不然前端可以绕过
        if (RoleTypeEnum.DEFAULT.getCode().equals(role.getRoleType())) {
            throw new BusinessException(ResultCode.USER_DEFAULT_ROLE_NOT_ALLOW_ERROR);
        }

        String password = userBo.getPassword();
        User userDataEntity = userRepository
                .commonQueryOne(UserCommonQuery.builder().username(userBo.getUserName()).build());
        if (userDataEntity != null) {
            throw new BusinessException(ResultCode.USER_USERNAME_ERRER);
        }
        String roleKey = userDao.findRoleKey(userBo.getRoleId());
        //判断角色key是否存在
        if (StringUtils.isEmpty(roleKey)) {
            throw new BusinessException(ResultCode.ROLE_ROLEKEY_ERROR);
        }
        User userdataEntity = User.builder().build();
        //用户名称正则判断
        if (!userBo.getUserName().matches(RegexConst.USERNAME_REGEX)) {
            //不符合正则
            throw new BusinessException(ResultCode.USERNAME_IS_INVALID);
        }
        //雪花算法生成userId
        String userId = String.valueOf(idWorker.nextId());
        userdataEntity.setUserId(userId);
        userdataEntity.setUsername(userBo.getUserName());
        userdataEntity.setEmail(userBo.getEmail() != null ? new Email(userBo.getEmail()) : null);
        //如果密码为空，则表示自动生成密码
        if (StringUtils.isEmpty(password)) {
            int i = (int) (8 + Math.random() * (20 - 8 + 1));
            password = randomPwUtils.getRandomPassword(i);
            userBo.setPassword(password);
            //将生成的随机密码进行加密
            userdataEntity.setPassword(new Password(password));

        } else {
            //不为空，表示已添加密码，进行正则判断
            if (password.matches(RegexConst.PASSWORD_REGEX)) {
                userdataEntity.setPassword(new Password(password));

            } else {
                throw new BusinessException(ResultCode.PASSWORD_IS_INVALID);
            }
        }
        //用户名正则校验
        if (!userdataEntity.getUsername().matches(RegexConst.USERNAME_REGEX)) {
            throw new BusinessException(ResultCode.USERNAME_SEARCH_IN_INVALID);
        }

        //校验数据库是否已有该手机号 --2021/01/22
        String phoneNumber = userBo.getPhoneNumber();

        if (userPhoneNumberDomainService
                .judgePhoneNumberAlreadyExists(new PhoneNumber(phoneNumber), UserTypeEnum.USER,
                        UserTypeEnum.NULL)) {
            // 手机号已注册
            throw new BusinessException(PHONE_NUMBER_ALREADY_EXIST);
        }

        try {
            userdataEntity.setCreatedBy(userBo.getUserId());
            userdataEntity.setCreatedByType(OperateByTypeEnum.USER);
            userdataEntity.setPhoneNumber(new PhoneNumber(phoneNumber));
            userdataEntity.setContactPerson(userBo.getContactPerson());
            userdataEntity.setContactAddress(
                    userBo.getContactAddress() != null ? new Address(userBo.getContactAddress())
                            : null);
            userdataEntity.setUserType(UserTypeEnum.USER);
            userdataEntity.setLongTimeLogin(
                    userBo.getLongTimeLogin() != null ? Integer.valueOf(userBo.getLongTimeLogin())
                            : null);
            userRepository.save(userdataEntity);

            //新增用户标签表关系
            UserLabelDto userLabelDto = new UserLabelDto();
            userLabelDto.setUserId(userdataEntity.getId().getStrValue());
            userLabelDto.setLabelName(userBo.getOtherLabelName());
            userLabelDto.setLabelId(userBo.getLabel());
            userLabelDto.setCreatedBy(userBo.getCreatedBy());
            userLabelDto.setCreateTime(LocalDateTime.now());
            userDataDao.insertUserLabel(userLabelDto);

            userBo.setId(userdataEntity.getId().getValue());
            Organization organization = organizationRepository
                    .find(new OrganizationId(Long.valueOf(userBo.getOrgId())));

            OrganizationEntity sessionOrg = organization2EntityAssembler.toDTO(organization);

            //新增用户账户中间表关系
            userDao.insertUserOrg(userdataEntity.getId().getValue(),
                    Long.parseLong(userBo.getOrgId()),
                    userBo.getDeptId(), userdataEntity.getCreatedBy());

            //新增用户角色中间表关系
            userDao.insertFeferRole(userdataEntity.getId().getValue(),
                    Long.parseLong(userBo.getOrgId()), Long.parseLong(userBo.getRoleId()));

            UserCsvDto userCsvDto = new UserCsvDto(userdataEntity.getUsername(), password);

            return userCsvDto;
        } catch (Exception e) {
            log.error("新增用户失败", e);
            throw new BusinessException(ResultCode.SERVER_BUSY);
        }
    }

    /**
     * 更改用户对应角色权限
     */
    @LogRecord(operationCode = "updateUser", operationName = "编辑用户", serviceType = ServiceTypeConst.ORGANIZATION_MANAGEMENT)
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IdmResDTO updatePermit(UserBo userBo) {
        // 被修改的用户
        String uid = userBo.getUserId();
        // 当前登陆用户
        Long userId = userBo.getId();
        String orgId = userBo.getOrgId();
        String roleId = userBo.getRoleId();

        //获取日志操作对象
        String[] op = new String[1];
        User userDataEntity = userRepository.find(new UserId(uid));
        op[0] = userDataEntity.getUsername();
        userBo.setOperationTarget(op);

        // 根据userId查询orgId
        Long org = this.userDao.getOrgId(Long.valueOf(uid));
        if (!orgId.equals(String.valueOf(org))) {
            throw new BusinessException(UNAUTHORIZED_ACCESS);
        }

        // 查询该账户的账户所有者
        Long orgOwner = userDao.findOrgOwner(orgId);
        // 判断该用户是不是账户的所有者
        if (uid.equals(String.valueOf(orgOwner))) {
            throw new BusinessException(ResultCode.CANNOT_SWITCH);
        }
        List<RoleDTO> roleList = roleDao.selectRoleListByOrgId(orgId);
        //判断账户下是否有这个角色id
        int num = 0;
        RoleDTO role = null;
        for (RoleDTO roleDTO : roleList) {
            if (roleDTO.getId().equals(roleId)) {
                num++;
                role = roleDTO;
            }
        }
        if (num == 0) {
            return new IdmResDTO(ResultCode.ROLE_NOT_EXIST);
        }
        //添加更新时间和更新者
        userDataEntity.updatedByPortal(userId);
        if (!StringUtils.isEmpty(userBo.getLongTimeLogin())) {
            userDataEntity.setLongTimeLogin(Integer.valueOf(userBo.getLongTimeLogin()));
        }
        userRepository.save(userDataEntity);
        //删除中间关联表关系
        userDao.deleteUserRole(uid, orgId);
        //重新添加中间表关联关系
        userDao.insertUserRole(Long.parseLong(uid), Long.parseLong(roleId), orgId,
                LocalDateTime.now(), String.valueOf(userId));
        //删除中间关联表关系
        userDao.deleteUserOrg(uid, orgId);
        //新增用户账户中间表关系
        userDao.insertUserOrg(Long.parseLong(uid), Long.parseLong(userBo.getOrgId()),
                userBo.getDeptId(), String.valueOf(userId));
        redisUtil.del(CacheConst.USER_CACHE_KEY_PREFIX + uid);
        updateInfosWithoutSms(userBo);
        updatePasswordIfNeed(userBo);

        return new IdmResDTO(ResultCode.SUCCESS);
    }

    /**
     * 发送日志记录到kafka
     *
     * @param isSuccess 操作成功标识
     * @param platformOperateLogDTO 日志记录
     */
    public void saveLog2Db(boolean isSuccess, OperateLogDto platformOperateLogDTO) {
        platformOperateLogDTO.setRequestTime(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        platformOperateLogDTO.setRequestIp(IpUtil.getIpAddr(request));
        if (isSuccess) {
            // 操作成功
            platformOperateLogDTO.setStatusCode(StatusCodeEnum.SUCCESS.getCode());
            platformOperateLogDTO.setStatusMsg(StatusCodeEnum.SUCCESS.getName());
            platformOperateLogDTO
                    .setLogLevel(cn.cuiot.dmp.common.enums.LogLevelEnum.INFO.getCode());
        } else {
            platformOperateLogDTO.setLogLevel(LogLevelEnum.ERROR.getCode());
            platformOperateLogDTO.setStatusMsg(StatusCodeEnum.FAILED.getName());
            platformOperateLogDTO.setStatusCode(StatusCodeEnum.FAILED.getCode());
        }

        operateLogService.saveDb(platformOperateLogDTO);
    }

    @Override
    public List<GetDepartmentTreeLazyResDto> getUserDepartmentTreeLazy(
            GetUserDepartmentTreeLazyReqDto dto) {
        final String orgId = dto.getLoginOrgId();
        final String userId = dto.getLoginUserId();
        List<GetDepartmentTreeLazyResDto> result;
        // 只能看到当前租户 所属dept为根,先获取根节点,以用户deptId为主键
        Long deptId = Optional.ofNullable(userDao.getDeptId(userId, orgId)).map(Long::valueOf)
                .orElse(null);
        //如果是空间用户，需要找到他所在的组织
        if (INIT.equals(dto.getType())) {
            result = spaceDao.getRootDepartmentLazy(orgId, deptId.toString());
        } else {
            result = spaceDao.getUserDepartmentLazyChange(dto.getParentId(),
                    Arrays.asList(DepartmentGroupEnum.SYSTEM.getCode(),
                            DepartmentGroupEnum.TENANT.getCode(),
                            DepartmentGroupEnum.COMMUNITY.getCode()));
        }
        return result;
    }

    private void checkSensitiveWord(int count, String userName) {
        boolean flag = verifyService.searchSensitiveWord(userName);
        if (!flag) {
            return;
        }
        log.info("包含敏感词：{}", userName);
        // 用户名包含有敏感词
        if (count == -1) {
            throw new BusinessException(USERNAME_IS_SENSITIVE_WORD);
        }

        throw new BusinessException(USERNAME_IS_SENSITIVE_WORD, "第" + count + "位用户名包含了非法字符，请重新输入！");
    }

    @Override
    public List<LabelTypeDto> getLabelTypeList(String labelType) {
        List<LabelTypeDto> labelTypeList = userDataDao.getLabelTypeList(labelType);
        return labelTypeList.stream()
                .filter(dto -> !OrgLabelEnum.UNICOM_ADMIN.getName().equals(dto.getLabelName()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, String> getDGroup(String orgId, String userId) {
        return departmentDao.getDgroupByUserIdAndOrgId(orgId, userId);
    }

    @Override
    public UserDTO getOneUser(String account, String safeAccount, String password) {
        PhoneNumber phoneNumber = null;
        EncryptedValue encryptedValue = new EncryptedValue(safeAccount);
        if (PhoneUtil.isPhone(encryptedValue.decrypt())) {
            phoneNumber = new PhoneNumber(encryptedValue);
        }
        User user = userRepository.queryUserForLogin(account, phoneNumber, password);
        return userAssembler.toDTO(user);
    }

    @Override
    public String checkUserInDeptId(Long parkId) {
        return userDataDao.checkUserInDeptId(parkId);
    }

    @Override
    public void insertUserD(InsertUserDTO userEntity, String orgId, String loginUserName) {
        log.info(userEntity.getUserName());

        if (StringUtils.isEmpty(userEntity.getUserName()) || StringUtils
                .isEmpty(userEntity.getPhoneNumber())) {
            throw new BusinessException(ResultCode.REQ_PARAM_ERROR);
        }

        //对传入手机号校验 --2020/01/22
        String phoneNumber = userEntity.getPhoneNumber();
        if (StringUtils.hasLength(phoneNumber) || StringUtils.hasLength(userEntity.getEmail())) {
            if (StringUtils.hasLength(phoneNumber) && !phoneNumber
                    .matches(RegexConst.PHONE_NUMBER_REGEX)) {
                throw new BusinessException(ResultCode.PHONE_NUMBER_IS_INVALID);
            }
            if (StringUtils.hasLength(userEntity.getEmail()) && !userEntity.getEmail()
                    .matches(RegexConst.EMAIL_REGEX)) {
                throw new BusinessException(ResultCode.EMAIL_IS_INVALID);
            }
        }

        //只对用户输入的字符校验，以@符号为分界线系统生成的不校验 --2021/01/07
        String userName = userEntity.getUserName().split("@")[0];
        //校验用户名是否含有敏感词 --2020/12/29
        checkSensitiveWord(-1, userName);

        UserBo userBo = new UserBo();
        userBo.setOrgId(orgId);
        userBo.setFrom(userEntity.getFrom());
        userBo.setId(Long.valueOf(userEntity.getLoginUserId()));
        userBo.setUserId(userEntity.getLoginUserId());
        userBo.setUserName(userEntity.getUserName());
        userBo.setPassword(userEntity.getPassWord());
        userBo.setResetPassword(userEntity.getResetPassword());
        userBo.setRoleId(userEntity.getRoleId());
        //新增手机号 --2021/01/22
        userBo.setPhoneNumber(userEntity.getPhoneNumber());
        userBo.setEmail(userEntity.getEmail());
        userBo.setDeptId(userEntity.getDeptId());
        userBo.setContactPerson(userEntity.getContactPerson());
        userBo.setContactAddress(userEntity.getContactAddress());
        // 根据账户标签获取用户标签
        Integer label = organizationDao.getUserLabelByOrg(orgId);
        userBo.setLabel(label);
        userBo.setOtherLabelName(userEntity.getOtherLabelName());
        if (loginUserName.equals("admin") && Objects
                .equals(Const.STR_1, userEntity.getLongTimeLogin())) {
            userBo.setLongTimeLogin(Const.STR_1);
        } else {
            userBo.setLongTimeLogin(null);
        }
        UserCsvDto userInfo = insertUser(userBo);

        if (userInfo == null) {
            throw new BusinessException(ResultCode.REQ_PARAM_ERROR);
        }

        log.info("用户数据存储");

        // 文件流输出
        List<JSONObject> jsonList = new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSON(userInfo).toString());
        jsonList.add(jsonObject);

        List<Object> head = new ArrayList<>();
        head.add("username");
        head.add("password");

        response.setContentType("text/csv;charset=\"GBK\"");
        response.setHeader("Content-Disposition", "attachment; filename=credentials.csv");
        String[] split = userBo.getUserName().split("@");
        String username = split[0];
        try {
            CommonCsvUtil.createCsvFile(head, jsonList, username + "credentials", response);
        } catch (UnsupportedEncodingException e) {
            log.error("insertUser error.", e);
        }
    }

    @Override
    public void checkDepartmentUser(Long orgId, String path) {
        List<DepartmentUserDto> departmentUser = userDataDao.getDepartmentUser(orgId, path);
        if (!CollectionUtils.isEmpty(departmentUser)) {
            throw new BusinessException(ResultCode.DEPARTMENT_EXISTS_USER);
        }
    }
}
