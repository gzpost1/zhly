package cn.cuiot.dmp.system.application.service.impl;

import static cn.cuiot.dmp.common.constant.ResultCode.INNER_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.PHONE_NUMBER_ALREADY_EXIST;
import static cn.cuiot.dmp.common.constant.ResultCode.PHONE_NUMBER_EXIST;
import static cn.cuiot.dmp.common.constant.ResultCode.SMS_CODE_EXPIRED_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.SMS_TEXT_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.UNAUTHORIZED_ACCESS;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.RegexConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.Sm4;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.domain.types.Address;
import cn.cuiot.dmp.domain.types.Email;
import cn.cuiot.dmp.domain.types.EncryptedValue;
import cn.cuiot.dmp.domain.types.IP;
import cn.cuiot.dmp.domain.types.Password;
import cn.cuiot.dmp.domain.types.PhoneNumber;
import cn.cuiot.dmp.domain.types.enums.OperateByTypeEnum;
import cn.cuiot.dmp.domain.types.id.OrganizationId;
import cn.cuiot.dmp.domain.types.id.UserId;
import cn.cuiot.dmp.system.application.enums.DepartmentGroupEnum;
import cn.cuiot.dmp.system.application.enums.RoleTypeEnum;
import cn.cuiot.dmp.system.application.param.assembler.Organization2EntityAssembler;
import cn.cuiot.dmp.system.application.param.assembler.UserAssembler;
import cn.cuiot.dmp.system.application.param.command.UpdateUserCommand;
import cn.cuiot.dmp.system.application.param.dto.UserDTO;
import cn.cuiot.dmp.system.application.service.MenuService;
import cn.cuiot.dmp.system.application.service.OperateLogService;
import cn.cuiot.dmp.system.application.service.SysPostService;
import cn.cuiot.dmp.system.application.service.UserService;
import cn.cuiot.dmp.system.domain.entity.Organization;
import cn.cuiot.dmp.system.domain.entity.User;
import cn.cuiot.dmp.system.domain.query.UserCommonQuery;
import cn.cuiot.dmp.system.domain.repository.OrganizationRepository;
import cn.cuiot.dmp.system.domain.repository.UserRepository;
import cn.cuiot.dmp.system.domain.service.UserPhoneNumberDomainService;
import cn.cuiot.dmp.system.domain.types.enums.UserStatusEnum;
import cn.cuiot.dmp.system.domain.types.enums.UserTypeEnum;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.entity.MenuEntity;
import cn.cuiot.dmp.system.infrastructure.entity.UserDataEntity;
import cn.cuiot.dmp.system.infrastructure.entity.bo.UserBo;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetUserDepartmentTreeLazyReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ImportUserDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RoleDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserCsvDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserDataResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.vo.UserExportVo;
import cn.cuiot.dmp.system.infrastructure.entity.vo.UserImportDownloadVo;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.DepartmentDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.MenuDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.OrgMenuDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.OrganizationDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.RoleDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.SpaceDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.UserDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.UserDataDao;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.SysPostEntity;
import cn.cuiot.dmp.system.infrastructure.utils.RandomPwUtils;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.PhoneUtil;
import com.alibaba.fastjson.JSON;
import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.core.api.strategory.StrategyPhone;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private MenuService menuService;

    @Autowired
    private OrgMenuDao orgMenuDao;

    @Autowired
    private UserAssembler userAssembler;

    @Autowired
    private SysPostService sysPostService;

    private static final String ROLE_NAME_LIKE = "roleNameLike";

    private static final String INIT = "init";

    @Value("${self.debug}")
    private String debug;

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
    public Map<String, String> getDGroup(String orgId, String userId) {
        return departmentDao.getDgroupByUserIdAndOrgId(orgId, userId);
    }

    /**
     * 用户列表筛选-分页
     */
    @Override
    public PageResult<UserDataResDTO> getPage(Map<String, Object> params, String sessionOrgId,
            int pageNo, int pageSize) {
        PageInfo<UserDataResDTO> resultPageInfo;
        try {
            Organization organization = organizationRepository
                    .find(new OrganizationId(sessionOrgId));
            String orgOwner = String.valueOf(organization.getOrgOwner().getValue());

            PageHelper.startPage(pageNo, pageSize);
            List<UserDataEntity> entities = userDataDao.searchList(params);

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
            resultPageInfo = userAssembler.dataEntityListToDataDtoList(pageInfo);
            for (UserDataResDTO userDataResDTO : resultPageInfo.getList()) {
                String pkUserId = userDataResDTO.getId();
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
     * 查询用户详情
     */
    @Override
    public UserDataResDTO getOne(String pkUserId, String sessionOrgId, String sessionUserId) {
        //判断是否有权限查看
        Long orgId = this.userDao.getOrgId(Long.valueOf(pkUserId));
        if (!sessionOrgId.equals(String.valueOf(orgId))) {
            throw new BusinessException(UNAUTHORIZED_ACCESS);
        }

        // 要查询的用户的组织
        String deptId = userDao.getDeptId(pkUserId, sessionOrgId);
        DepartmentEntity departmentEntity = departmentDao.selectByPrimary(Long.parseLong(deptId));

        User entity = userRepository.find(new UserId(pkUserId));
        if (entity == null) {
            throw new BusinessException(ResultCode.OBJECT_NOT_EXIST);
        }

        UserDataResDTO userDataResDTO = userAssembler.doToDataDTO(entity);

        userDataResDTO.setId(pkUserId);

        //找到该用户对应的角色
        String roleId = userDao.getRoleId(pkUserId, sessionOrgId);
        userDataResDTO.setRoleId(roleId);
        // 找到角色名称
        String roleName = userDao.getRoleName(roleId);
        userDataResDTO.setRoleName(roleName);
        // 找到组织
        userDataResDTO.setDeptId(deptId);
        userDataResDTO.setDeptName(departmentEntity.getDepartmentName());
        userDataResDTO.setDeptPathName(departmentEntity.getPathName());
        userDataResDTO.setDeptTreePath(departmentEntity.getPath());
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
        userDataResDTO.setPhoneNumber(entity.getDecryptedPhoneNumber());
        userDataResDTO.setEmail(entity.getEmail() != null ? entity.getEmail().decrypt() : null);

        //设置岗位名称
        if (Objects.nonNull(entity.getPostId())) {
            SysPostEntity postEntity = sysPostService.getById(entity.getPostId());
            if (Objects.nonNull(postEntity)) {
                userDataResDTO.setPostName(postEntity.getPostName());
            }
        }

        return userDataResDTO;
    }

    /**
     * 新增用户
     */
    @LogRecord(operationCode = "insertUser", operationName = "新增用户", serviceType = ServiceTypeConst.ORGANIZATION_MANAGEMENT)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserCsvDto insertUser(UserBo userBo) {
        /**
         * 判断所选组织是否可选
         */
        String loginDeptId = userDao.getDeptId(userBo.getLoginUserId(), userBo.getOrgId());
        DepartmentEntity loginDepartment = departmentDao
                .selectByPrimary(Long.valueOf(loginDeptId));
        DepartmentEntity departmentEntity = departmentDao
                .selectByPrimary(Long.parseLong(userBo.getDeptId()));
        if (!departmentEntity.getPath().startsWith(loginDepartment.getPath())) {
            throw new BusinessException(ResultCode.DEPARTMENT_ULTRA_VIRES);
        }

        //获取日志操作对象
        String[] op = new String[1];
        op[0] = userBo.getUsername();
        userBo.setOperationTarget(op);

        /**
         * 判断所选角色是否可选
         */
        List<RoleDTO> roleList = roleDao.selectRoleListByOrgId(userBo.getOrgId());
        RoleDTO role = null;
        for (RoleDTO roleDTO : roleList) {
            if (roleDTO.getId().equals(userBo.getRoleId())) {
                role = roleDTO;
                break;
            }
        }
        if (Objects.isNull(role)) {
            throw new BusinessException(ResultCode.ROLE_NOT_EXIST, "角色不可选");
        }
        //默认角色不可选
        if (RoleTypeEnum.DEFAULT.getCode().equals(role.getRoleType())) {
            throw new BusinessException(ResultCode.USER_DEFAULT_ROLE_NOT_ALLOW_ERROR);
        }

        //用户名正则校验
        String userName = userBo.getUsername();
        if (!userName.matches(RegexConst.USERNAME_REGEX)) {
            throw new BusinessException(ResultCode.USERNAME_SEARCH_IN_INVALID);
        }

        //判断用户名是否已经存在
        if (Objects.nonNull(userRepository
                .commonQueryOne(UserCommonQuery.builder().username(userName).build()))) {
            throw new BusinessException(ResultCode.USER_USERNAME_ERRER);
        }

        /**
         * 判断手机号
         */
        String phoneNumber = userBo.getPhoneNumber();
        if (StringUtils.hasLength(phoneNumber)
                && !PhoneUtil.isPhone(phoneNumber)) {
            throw new BusinessException(ResultCode.PHONE_NUMBER_IS_INVALID, "请输入正确的11位手机号");
        }
        if (userPhoneNumberDomainService
                .judgePhoneNumberAlreadyExists(new PhoneNumber(phoneNumber), UserTypeEnum.USER,
                        UserTypeEnum.NULL)) {
            // 手机号已存在
            throw new BusinessException(PHONE_NUMBER_ALREADY_EXIST);
        }

        User userEntity = User.builder().build();

        userEntity.setUsername(userBo.getUsername());
        userEntity.setName(userBo.getName());
        userEntity.setEmail(userBo.getEmail() != null ? new Email(userBo.getEmail()) : null);
        /**
         * 随机密码
         */
        String password = randomPwUtils.getRandomPassword((int) (8 + Math.random() * (20 - 8 + 1)));
        userBo.setPassword(password);
        userEntity.setPassword(new Password(password));

        userEntity.setCreatedBy(userBo.getLoginUserId().toString());
        userEntity.setCreatedByType(OperateByTypeEnum.USER);
        userEntity.setPhoneNumber(new PhoneNumber(phoneNumber));
        userEntity.setPostId(userBo.getPostId());
        userEntity.setRemark(userBo.getRemark());
        userEntity.setUserType(UserTypeEnum.USER);
        userEntity.setLongTimeLogin(
                userBo.getLongTimeLogin() != null ? Integer.valueOf(userBo.getLongTimeLogin())
                        : null);

        try {

            userRepository.save(userEntity);

            userBo.setId(userEntity.getId().getValue());

            Organization organization = organizationRepository
                    .find(new OrganizationId(Long.valueOf(userBo.getOrgId())));

            //新增用户账户中间表关系
            userDao.insertUserOrg(SnowflakeIdWorkerUtil.nextId(), userEntity.getId().getValue(),
                    Long.parseLong(userBo.getOrgId()),
                    userBo.getDeptId(), userEntity.getCreatedBy());

            //新增用户角色中间表关系
            userDao.insertFeferRole(SnowflakeIdWorkerUtil.nextId(),
                    userEntity.getId().getValue(),
                    Long.parseLong(userBo.getOrgId()), Long.parseLong(userBo.getRoleId()));

            UserCsvDto userCsvDto = new UserCsvDto(userEntity.getUsername(), password);

            return userCsvDto;
        } catch (Exception e) {
            log.error("新增用户失败", e);
            throw new BusinessException(ResultCode.SERVER_BUSY);
        }
    }

    /**
     * 更改用户
     */
    @LogRecord(operationCode = "updateUser", operationName = "编辑用户", serviceType = ServiceTypeConst.ORGANIZATION_MANAGEMENT)
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IdmResDTO updateUser(UserBo userBo) {

        String loginUserId = userBo.getLoginUserId();
        String orgId = userBo.getOrgId();
        String roleId = userBo.getRoleId();

        //获取日志操作对象
        String[] op = new String[1];
        op[0] = userBo.getUsername();
        userBo.setOperationTarget(op);

        /**
         * 判断所选组织部门是否可选
         */
        String loginDeptId = userDao.getDeptId(userBo.getLoginUserId(), userBo.getOrgId());
        DepartmentEntity loginDepartment = departmentDao
                .selectByPrimary(Long.valueOf(loginDeptId));
        DepartmentEntity departmentEntity = departmentDao
                .selectByPrimary(Long.parseLong(userBo.getDeptId()));
        if (!departmentEntity.getPath().startsWith(loginDepartment.getPath())) {
            throw new BusinessException(ResultCode.DEPARTMENT_ULTRA_VIRES);
        }

        /**
         * 只能操作本企业的用户
         */
        Long oldOrgId = this.userDao.getOrgId(userBo.getId());
        if (!orgId.equals(String.valueOf(oldOrgId))) {
            throw new BusinessException(UNAUTHORIZED_ACCESS);
        }

        // 管理员用户不可修改
        Long orgOwner = userDao.findOrgOwner(orgId);
        if (userBo.getId().equals(orgOwner)) {
            throw new BusinessException(ResultCode.CANNOT_SWITCH, "管理员用户不可修改");
        }

        /**
         * 判断所选角色是否可选
         */
        List<RoleDTO> roleList = roleDao.selectRoleListByOrgId(userBo.getOrgId());
        RoleDTO role = null;
        for (RoleDTO roleDTO : roleList) {
            if (roleDTO.getId().equals(userBo.getRoleId())) {
                role = roleDTO;
                break;
            }
        }
        if (Objects.isNull(role)) {
            throw new BusinessException(ResultCode.ROLE_NOT_EXIST, "该角色不可选");
        }
        //默认角色不可选
        if (RoleTypeEnum.DEFAULT.getCode().equals(role.getRoleType())) {
            throw new BusinessException(ResultCode.USER_DEFAULT_ROLE_NOT_ALLOW_ERROR);
        }

        //用户名正则校验
        String username = userBo.getUsername();
        if (!username.matches(RegexConst.USERNAME_REGEX)) {
            throw new BusinessException(ResultCode.USERNAME_SEARCH_IN_INVALID);
        }

        //判断用户名是否已经存在
        User userByUsername = userRepository
                .commonQueryOne(UserCommonQuery.builder().username(username).build());
        if (Objects.nonNull(userByUsername) && !userByUsername.getId().getValue()
                .equals(userBo.getId())) {
            throw new BusinessException(ResultCode.USER_USERNAME_ERRER);
        }

        User userEntity = User.builder().build();
        userEntity.setId(new UserId(userBo.getId()));
        userEntity.setUsername(userBo.getUsername());
        userEntity.setName(userBo.getName());
        userEntity.setPostId(userBo.getPostId());
        userEntity.setRemark(userBo.getRemark());
        userEntity.setLongTimeLogin(
                userBo.getLongTimeLogin() != null ? Integer.valueOf(userBo.getLongTimeLogin())
                        : null);
        userEntity.updatedByPortal(loginUserId);
        userRepository.save(userEntity);

        //删除中间关联表关系
        userDao.deleteUserRole(userBo.getId().toString(), orgId);
        //重新添加中间表关联关系
        userDao.insertUserRole(SnowflakeIdWorkerUtil.nextId(), userBo.getId(),
                Long.parseLong(roleId), orgId,
                LocalDateTime.now(), loginUserId);

        //删除中间关联表关系
        userDao.deleteUserOrg(userBo.getId().toString(), orgId);
        //新增用户账户中间表关系
        userDao.insertUserOrg(SnowflakeIdWorkerUtil.nextId(), userBo.getId(),
                Long.parseLong(userBo.getOrgId()),
                userBo.getDeptId(), loginUserId);

        redisUtil.del(CacheConst.USER_CACHE_KEY_PREFIX + userBo.getId());

        return new IdmResDTO(ResultCode.SUCCESS);
    }

    /**
     * 批量移动用户
     */
    @Override
    @LogRecord(operationCode = "moveUsers", operationName = "移动用户", serviceType = ServiceTypeConst.ORGANIZATION_MANAGEMENT)
    @Transactional(rollbackFor = Exception.class)
    public void moveUsers(UserBo userBo) {
        String sessionOrgId = userBo.getOrgId();
        List<Long> ids = userBo.getIds();
        String loginUserId = userBo.getLoginUserId();
        String deptId = userBo.getDeptId();

        /**
         * 判断所选组织部门是否可选
         */
        String loginDeptId = userDao.getDeptId(userBo.getLoginUserId(), userBo.getOrgId());
        DepartmentEntity loginDepartment = departmentDao
                .selectByPrimary(Long.valueOf(loginDeptId));
        DepartmentEntity departmentEntity = departmentDao
                .selectByPrimary(Long.parseLong(deptId));
        if (!departmentEntity.getPath().startsWith(loginDepartment.getPath())) {
            throw new BusinessException(ResultCode.DEPARTMENT_ULTRA_VIRES);
        }

        String deptTreePath = Optional.ofNullable(loginDepartment).map(DepartmentEntity::getPath)
                .orElse(null);
        if (StringUtils.isEmpty(deptTreePath)) {
            throw new BusinessException(ResultCode.OBJECT_NOT_EXIST, "查询组织部门信息缺失");
        }

        // 查询该账户的账户所有者
        Long orgOwner = userDao.findOrgOwner(sessionOrgId);

        for (Long pkUserId : ids) {
            // 根据userId查询orgId
            Long orgId = this.userDao.getOrgId(pkUserId);
            if (!sessionOrgId.equals(String.valueOf(orgId))) {
                throw new BusinessException(UNAUTHORIZED_ACCESS);
            }
            // 判断是否是自己
            if (pkUserId.toString().equals(loginUserId)) {
                throw new BusinessException(ResultCode.CANNOT_OPERATION, "不能移动当前登录用户自己");
            }

            // 管理员不能移动
            if (pkUserId.equals(orgOwner)) {
                throw new BusinessException(ResultCode.CANNOT_OPERATION, "管理员不能移动");
            }

            // 组织权限限制
            DepartmentDto departmentDto = departmentDao.getPathByUser(pkUserId.toString());
            String subTreePath = Optional.ofNullable(departmentDto).map(DepartmentDto::getPath)
                    .orElse(null);
            if (StringUtils.isEmpty(subTreePath)) {
                throw new BusinessException(ResultCode.OBJECT_NOT_EXIST);
            }
            if (!subTreePath.startsWith(deptTreePath)) {
                throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
            }
        }
        for (Long pkUserId : ids) {
            //删除中间关联表关系
            userDao.deleteUserOrg(pkUserId.toString(), sessionOrgId);
            //新增用户账户中间表关系
            userDao.insertUserOrg(SnowflakeIdWorkerUtil.nextId(), pkUserId,
                    Long.parseLong(sessionOrgId),
                    deptId, loginUserId);
            // 设备模块使用了缓存，系统模块姓名或手机号变更需要清除redisKey
            redisUtil.del(CacheConst.USER_CACHE_KEY_PREFIX + pkUserId);
        }
    }

    /**
     * 批量启停用
     */
    @LogRecord(operationCode = "changeUserStatus", operationName = "启停用用户", serviceType = ServiceTypeConst.ORGANIZATION_MANAGEMENT)
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeUserStatus(UserBo userBo) {
        String sessionOrgId = userBo.getOrgId();
        List<Long> ids = userBo.getIds();
        String loginUserId = userBo.getLoginUserId();
        Byte status = userBo.getStatus();

        /**
         * 判断所选组织部门是否可选
         */
        String loginDeptId = userDao.getDeptId(userBo.getLoginUserId(), userBo.getOrgId());
        DepartmentEntity loginDepartment = departmentDao
                .selectByPrimary(Long.valueOf(loginDeptId));

        String deptTreePath = Optional.ofNullable(loginDepartment).map(DepartmentEntity::getPath)
                .orElse(null);
        if (StringUtils.isEmpty(deptTreePath)) {
            throw new BusinessException(ResultCode.OBJECT_NOT_EXIST, "查询组织部门信息缺失");
        }

        // 查询该账户的账户所有者
        Long orgOwner = userDao.findOrgOwner(sessionOrgId);

        for (Long pkUserId : ids) {
            // 根据userId查询orgId
            Long orgId = this.userDao.getOrgId(pkUserId);
            if (!sessionOrgId.equals(String.valueOf(orgId))) {
                throw new BusinessException(UNAUTHORIZED_ACCESS);
            }
            // 判断是否是自己
            if (pkUserId.toString().equals(loginUserId)) {
                throw new BusinessException(ResultCode.CANNOT_OPERATION, "不能操作当前登录用户自己");
            }

            // 管理员不能移动
            if (pkUserId.equals(orgOwner)) {
                throw new BusinessException(ResultCode.CANNOT_OPERATION, "管理员不能操作");
            }

            // 组织权限限制
            DepartmentDto departmentDto = departmentDao.getPathByUser(pkUserId.toString());
            String subTreePath = Optional.ofNullable(departmentDto).map(DepartmentDto::getPath)
                    .orElse(null);
            if (StringUtils.isEmpty(subTreePath)) {
                throw new BusinessException(ResultCode.OBJECT_NOT_EXIST);
            }
            if (!subTreePath.startsWith(deptTreePath)) {
                throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
            }
        }
        for (Long pkUserId : ids) {
            User userEntity = User.builder().build();
            userEntity.setId(new UserId(pkUserId));
            userEntity.setStatus(UserStatusEnum.valueOf(status.intValue()));
            userEntity.updatedByPortal(loginUserId);
            userRepository.save(userEntity);
        }
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
        String loginUserId = userBo.getLoginUserId();

        // 查询该账户的账户所有者
        Long orgOwner = userDao.findOrgOwner(sessionOrgId);

        //获取操作对象
        String[] op = new String[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            User userDataEntity = userRepository.find(new UserId(ids.get(i)));
            op[i] = userDataEntity.getUsername();
        }
        userBo.setOperationTarget(op);

        DepartmentDto sessionDepartment = departmentDao.getPathByUser(loginUserId);
        String deptTreePath = Optional.ofNullable(sessionDepartment).map(DepartmentDto::getPath)
                .orElse(null);
        if (StringUtils.isEmpty(deptTreePath)) {
            throw new BusinessException(ResultCode.OBJECT_NOT_EXIST, "查询组织部门信息缺失");
        }

        Organization sessionOrg = organizationRepository
                .find(new OrganizationId(Long.valueOf(sessionOrgId)));

        for (Long pkUserId : ids) {
            // 根据userId查询orgId
            Long orgId = this.userDao.getOrgId(pkUserId);
            if (!sessionOrgId.equals(String.valueOf(orgId))) {
                throw new BusinessException(UNAUTHORIZED_ACCESS);
            }

            // 判断是否是自己
            if (pkUserId.toString().equals(loginUserId)) {
                throw new BusinessException(ResultCode.CANNOT_DELETE_SELF);
            }

            // 管理员不能删除
            if (pkUserId.equals(orgOwner)) {
                throw new BusinessException(ResultCode.CANNOT_DELETE_ORGOWNER);
            }

            // 组织权限限制
            DepartmentDto departmentDto = departmentDao.getPathByUser(pkUserId.toString());
            String subTreePath = Optional.ofNullable(departmentDto).map(DepartmentDto::getPath)
                    .orElse(null);
            if (StringUtils.isEmpty(subTreePath)) {
                throw new BusinessException(ResultCode.OBJECT_NOT_EXIST);
            }
            if (!subTreePath.startsWith(deptTreePath)) {
                throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
            }
        }

        int result = userRepository
                .removeList(ids.stream().map(UserId::new).collect(Collectors.toList()));

        // 删除用户后，把用户与当前登录的账号的关联关系删除
        userDataDao.deleteUserOrgByUserPks(ids, sessionOrgId);
        // 再删除与角色的关联
        userDataDao.deleteUserRoleByUserPks(ids, sessionOrgId);

        userRepository.removeUserRelate(ids);

        for (Long pkUserId : ids) {
            // 设备模块使用了缓存，系统模块姓名或手机号变更需要清除redisKey
            redisUtil.del(CacheConst.USER_CACHE_KEY_PREFIX + pkUserId);
        }

        return result;
    }

    /**
     * 导出用户
     */
    @Override
    public List<UserExportVo> exportUsers(UserBo userBo) {
        String sessionOrgId = userBo.getOrgId();
        String loginUserId = userBo.getLoginUserId();
        String deptId = userBo.getDeptId();

        /**
         * 判断所选组织部门是否可选
         */
        String loginDeptId = userDao.getDeptId(userBo.getLoginUserId(), userBo.getOrgId());
        DepartmentEntity loginDepartment = departmentDao
                .selectByPrimary(Long.valueOf(loginDeptId));
        DepartmentEntity departmentEntity = departmentDao
                .selectByPrimary(Long.parseLong(deptId));
        if (!departmentEntity.getPath().startsWith(loginDepartment.getPath())) {
            throw new BusinessException(ResultCode.DEPARTMENT_ULTRA_VIRES);
        }

        Map<String, Object> params = Maps.newHashMap();
        params.put("orgId", sessionOrgId);
        params.put("loginUserId", loginUserId);
        params.put("path", departmentEntity.getPath());

        List<UserDataEntity> entities = userDataDao.searchList(params);

        //手机号解密
        IStrategy strategyPhone = new StrategyPhone();
        for (UserDataEntity userDataEntity : entities) {
            String phoneNumber = Sm4.decrypt(userDataEntity.getPhoneNumber());
           /* if (StringUtils.hasLength(phoneNumber)) {
                final String phoneSensitive = (String) strategyPhone.des(phoneNumber, null);
                userDataEntity.setPhoneNumber(phoneSensitive);
            }*/
            userDataEntity.setPhoneNumber(phoneNumber);
        }
        List<UserExportVo> resultList = userAssembler.entityListToExportVoList(entities);
        return resultList;
    }

    /**
     * 导入用户
     */
    @LogRecord(operationCode = "importUsers", operationName = "导入用户", serviceType = ServiceTypeConst.ORGANIZATION_MANAGEMENT)
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserImportDownloadVo> importUsers(UserBo cmd) {
        String sessionOrgId = cmd.getOrgId();
        String loginUserId = cmd.getLoginUserId();
        String deptId = cmd.getDeptId();
        List<ImportUserDto> dtoList = cmd.getImportDtoList();

        /**
         * 判断所选组织部门是否可选
         */
        String loginDeptId = userDao.getDeptId(cmd.getLoginUserId(), cmd.getOrgId());
        DepartmentEntity loginDepartment = departmentDao
                .selectByPrimary(Long.valueOf(loginDeptId));
        DepartmentEntity departmentEntity = departmentDao
                .selectByPrimary(Long.parseLong(deptId));
        if (!departmentEntity.getPath().startsWith(loginDepartment.getPath())) {
            throw new BusinessException(ResultCode.DEPARTMENT_ULTRA_VIRES);
        }

        List<RoleDTO> roleList = roleDao.selectRoleListByOrgId(sessionOrgId);
        if (CollectionUtils.isEmpty(roleList)) {
            throw new BusinessException(ResultCode.ROLE_NOT_EXIST, "查询不到可用的角色");
        }

        List<UserBo> userBoList = Lists.newArrayList();
        for (ImportUserDto userDto : dtoList) {
            //判断用户名是否已经存在
            if (Objects.nonNull(userRepository
                    .commonQueryOne(
                            UserCommonQuery.builder().username(userDto.getUsername()).build()))) {
                throw new BusinessException(ResultCode.USER_USERNAME_ERRER,
                        "导入失败，用户名[" + userDto.getUsername() + "]已经存在");
            }
            /**
             * 判断手机号
             */
            String phoneNumber = userDto.getPhoneNumber();
            if (!PhoneUtil.isPhone(phoneNumber)) {
                throw new BusinessException(ResultCode.PHONE_NUMBER_IS_INVALID,
                        "导入失败，手机号[" + phoneNumber + "]输入不正确");
            }
            if (userPhoneNumberDomainService
                    .judgePhoneNumberAlreadyExists(new PhoneNumber(phoneNumber), UserTypeEnum.USER,
                            UserTypeEnum.NULL)) {
                throw new BusinessException(PHONE_NUMBER_ALREADY_EXIST,
                        "导入失败，手机号[" + phoneNumber + "]已经存在");
            }
            /**
             * 判断角色
             */
            RoleDTO roleDTO = roleDao
                    .selectRoleByName(userDto.getRoleName(), Long.valueOf(sessionOrgId));
            if (Objects.isNull(roleDTO)) {
                throw new BusinessException(ResultCode.ROLE_NOT_EXIST,
                        "导入失败，角色[" + userDto.getRoleName() + "]不存在");
            }
            if (RoleTypeEnum.DEFAULT.getCode().equals(roleDTO.getRoleType())) {
                throw new BusinessException(ResultCode.USER_DEFAULT_ROLE_NOT_ALLOW_ERROR,
                        "导入失败，角色[" + userDto.getRoleName() + "]不可选");
            }
            Boolean roleFlag = false;
            for (RoleDTO tmpRole : roleList) {
                if (tmpRole.getId().equals(roleDTO.getId())) {
                    roleFlag = true;
                    break;
                }
            }
            if (!roleFlag) {
                throw new BusinessException(ResultCode.ROLE_NOT_EXIST,
                        "导入失败，角色[" + userDto.getRoleName() + "]不可选");
            }
            /**
             * 判断岗位
             */
            SysPostEntity postEntity = null;
            if (org.apache.commons.lang3.StringUtils.isNotBlank(userDto.getPostName())) {
                postEntity = sysPostService
                        .getByName(userDto.getPostName(), Long.valueOf(sessionOrgId));
                if (Objects.isNull(postEntity)) {
                    throw new BusinessException(ResultCode.REQ_PARAM_ERROR,
                            "导入失败，岗位[" + userDto.getPostName() + "]不存在");
                }
            }
            UserBo userBo = new UserBo();
            userBo.setOrgId(sessionOrgId);
            userBo.setLoginUserId(loginUserId);
            userBo.setRoleId(roleDTO.getId());
            userBo.setUsername(userDto.getUsername());
            userBo.setName(userDto.getName());
            userBo.setPhoneNumber(userDto.getPhoneNumber());
            userBo.setDeptId(deptId);
            if (Objects.nonNull(postEntity)) {
                userBo.setPostId(postEntity.getId());
            }
            /**
             * 随机密码
             */
            String password = randomPwUtils
                    .getRandomPassword((int) (8 + Math.random() * (20 - 8 + 1)));
            userBo.setPassword(password);
            userBo.setRemark(userDto.getRemark());

            userBoList.add(userBo);
        }

        List<UserImportDownloadVo> resultList = Lists.newArrayList();

        for (UserBo userBo : userBoList) {
            User userEntity = User.builder().build();
            userEntity.setUsername(userBo.getUsername());
            userEntity.setName(userBo.getName());
            userEntity.setPassword(new Password(userBo.getPassword()));
            userEntity.setCreatedBy(userBo.getLoginUserId());
            userEntity.setCreatedByType(OperateByTypeEnum.USER);
            userEntity.setPhoneNumber(new PhoneNumber(userBo.getPhoneNumber()));
            userEntity.setPostId(userBo.getPostId());
            userEntity.setRemark(userBo.getRemark());
            userEntity.setUserType(UserTypeEnum.USER);
            userRepository.save(userEntity);
            userBo.setId(userEntity.getId().getValue());

            //新增用户账户中间表关系
            userDao.insertUserOrg(SnowflakeIdWorkerUtil.nextId(), userEntity.getId().getValue(),
                    Long.parseLong(userBo.getOrgId()),
                    userBo.getDeptId(), userEntity.getCreatedBy());

            //新增用户角色中间表关系
            userDao.insertFeferRole(SnowflakeIdWorkerUtil.nextId(),
                    userEntity.getId().getValue(),
                    Long.parseLong(userBo.getOrgId()), Long.parseLong(userBo.getRoleId()));

            UserImportDownloadVo downloadVo = new UserImportDownloadVo();
            downloadVo.setUsername(userBo.getUsername());
            downloadVo.setPhoneNumber(userBo.getPhoneNumber());
            downloadVo.setPassword(userBo.getPassword());
            resultList.add(downloadVo);
        }

        return resultList;
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

    @Override
    public UserResDTO getUserMenuByUserIdAndOrgId(String userId, String orgId) {
        // 获取用户信息
        UserResDTO userResDTO = getUserInfoByUserIdAndOrgId(userId, orgId);

        List<MenuEntity> menuList = menuService.getPermissionMenus(orgId, userId);

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
        UserResDTO userResDTO = userAssembler.doToDTO(userEntity);
        userResDTO.setOrgId(orgId);
        // 查询用户组织的类型
        DepartmentDto userDept = departmentDao.getPathByUser(userId);
        if (userDept != null) {
            userResDTO.setDGroup(userDept.getDGroup());
        }
        // 获取账户信息
        Organization organization = organizationRepository.find(new OrganizationId(orgId));
        if (organization == null) {
            throw new BusinessException(ResultCode.ORG_IS_NOT_EXIST);
        }
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
        userResDTO.setName(userEntity.getName());
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

    /**
     * 修改密码(登录人自行修改)
     */
    @LogRecord(operationCode = "updatePassword", operationName = "修改密码（个人）", serviceType = ServiceTypeConst.SECURITY_SETTING)
    @Override
    public void updatePassword(UserDataEntity entity) {
        User user = User.builder().id(new UserId(entity.getId()))
                .password(new Password(entity.getPassword())).build();
        user.updatedByPortal();
        if (!userRepository.save(user)) {
            throw new BusinessException(ResultCode.UPDATE_PASSWORD_FAIL);
        }
    }

    /**
     * 修改手机号(登录人自行修改)
     */
    @LogRecord(operationCode = "updatePhoneNumber", operationName = "修改手机号", serviceType = ServiceTypeConst.SECURITY_SETTING)
    @Override
    public void updatePhoneNumber(UserBo userBo) {
        String phoneNumber = userBo.getPhoneNumber();
        Long userId = userBo.getId();
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
        redisUtil.del(CacheConst.USER_CACHE_KEY_PREFIX + userId);
    }


    @Override
    public UserCsvDto resetPasswordWithOutSms(UserBo userBo) {
        Long userId = userBo.getId();
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
    public int updateByCommand(UpdateUserCommand updatedUser) {
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

    /**
     * 查询用户
     */
    @Override
    public List<BaseUserDto> lookUpUserList(BaseUserReqDto query) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("roleIdList", query.getRoleIdList());
        params.put("userIdList", query.getUserIdList());
        params.put("deptIds", query.getDeptIdList());
        List<UserDataEntity> entities = userDataDao.searchList(params);
        if (CollectionUtils.isNotEmpty(entities)) {
            List<BaseUserDto> dtoList = userAssembler
                    .dataEntityListToBaseUserDtoList(entities);
            return dtoList;
        }
        return Lists.newArrayList();
    }

    /**
     * 获取用户信息
     */
    @Override
    public BaseUserDto lookUpUserInfo(BaseUserReqDto query) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("userId", query.getUserId());
        List<UserDataEntity> entities = userDataDao.searchList(params);
        if (CollectionUtils.isNotEmpty(entities)) {
            List<BaseUserDto> dtoList = userAssembler
                    .dataEntityListToBaseUserDtoList(entities);
            return dtoList.get(0);
        }
        return null;
    }
}
