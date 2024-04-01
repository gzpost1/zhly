package cn.cuiot.dmp.system.application.service.impl;

import static cn.cuiot.dmp.common.constant.ResultCode.ACCOUNT_LABEL_NOT_EXIST;
import static cn.cuiot.dmp.common.constant.ResultCode.INNER_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.OBJECT_NOT_EXIST;
import static cn.cuiot.dmp.common.constant.ResultCode.PHONE_NUMBER_ALREADY_EXIST;
import static cn.cuiot.dmp.common.constant.ResultCode.PHONE_NUMBER_EXIST;
import static cn.cuiot.dmp.common.constant.ResultCode.USERNAME_ALREADY_EXIST;

import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.common.constant.NumberConst;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.RegexConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.common.utils.Const;
import cn.cuiot.dmp.common.utils.RandomCodeWorker;
import cn.cuiot.dmp.common.utils.RoleConst;
import cn.cuiot.dmp.common.utils.Sm4;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorker;
import cn.cuiot.dmp.common.utils.ValidateUtil;
import cn.cuiot.dmp.domain.types.Password;
import cn.cuiot.dmp.domain.types.PhoneNumber;
import cn.cuiot.dmp.domain.types.enums.OperateByTypeEnum;
import cn.cuiot.dmp.domain.types.id.OrganizationId;
import cn.cuiot.dmp.domain.types.id.UserId;
import cn.cuiot.dmp.system.application.enums.DepartmentGroupEnum;
import cn.cuiot.dmp.base.application.enums.OrgStatusEnum;
import cn.cuiot.dmp.base.application.enums.OrgTypeEnum;
import cn.cuiot.dmp.system.application.enums.ResetPasswordEnum;
import cn.cuiot.dmp.system.application.enums.UserSourceTypeEnum;
import cn.cuiot.dmp.system.application.param.assembler.Organization2EntityAssembler;
import cn.cuiot.dmp.system.application.param.assembler.Organization2GetOrganizationVoAssembler;
import cn.cuiot.dmp.system.application.param.assembler.Organization2OrganizationResDTOAssembler;
import cn.cuiot.dmp.system.application.service.DepartmentService;
import cn.cuiot.dmp.system.application.service.OrganizationService;
import cn.cuiot.dmp.system.application.service.UserService;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.entity.OrganizationEntity;
import cn.cuiot.dmp.system.infrastructure.entity.UserDataEntity;
import cn.cuiot.dmp.system.infrastructure.entity.bo.UserBo;
import cn.cuiot.dmp.system.infrastructure.entity.dto.CompanyReqDto.CompanyDetailReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.CompanyReqDto.UpdateCompanyReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.CompanyResDto.CompanyDetailResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.InsertOrganizationDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ListOrganizationDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OperateOrganizationDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrgCsvDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrgLabelDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrgTypeDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrganizationResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ResetUserPasswordReqDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UpdateOrganizationDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserCsvDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserDataReqDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserDataResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserLabelDto;
import cn.cuiot.dmp.system.infrastructure.entity.vo.GetOrganizationVO;
import cn.cuiot.dmp.system.infrastructure.entity.vo.ListOrganizationVO;
import cn.cuiot.dmp.system.infrastructure.messaging.spring.SystemEventSendAdapter;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.DepartmentDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.OrgMenuDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.OrgTypeMenuRootDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.OrganizationDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.RoleDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.UserDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.UserDataDao;
import cn.cuiot.dmp.base.application.utils.CommonCsvUtil;
import cn.cuiot.dmp.system.infrastructure.utils.DepartmentUtil;
import cn.cuiot.dmp.system.infrastructure.utils.DeptTreePathUtils;
import cn.cuiot.dmp.system.infrastructure.utils.OrgRedisUtil;
import cn.cuiot.dmp.system.infrastructure.utils.RandomPwUtils;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.system.user_manage.domain.entity.Organization;
import cn.cuiot.dmp.system.user_manage.domain.entity.User;
import cn.cuiot.dmp.system.user_manage.domain.service.UserPhoneNumberDomainService;
import cn.cuiot.dmp.system.user_manage.domain.types.enums.OrgSourceEnum;
import cn.cuiot.dmp.system.user_manage.domain.types.enums.UserTypeEnum;
import cn.cuiot.dmp.system.user_manage.query.OrganizationCommonQuery;
import cn.cuiot.dmp.system.user_manage.query.UserCommonQuery;
import cn.cuiot.dmp.system.user_manage.repository.OrganizationRepository;
import cn.cuiot.dmp.system.user_manage.repository.UserRepository;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.core.api.strategory.StrategyPhone;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @Author 26432
 * @Date 2021/12/8   17:32
 */
@Slf4j
@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPhoneNumberDomainService userPhoneNumberDomainService;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserDataDao userDataDao;

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private Organization2EntityAssembler organization2EntityAssembler;

    @Autowired
    private Organization2GetOrganizationVoAssembler organization2GetOrganizationVoAssembler;

    @Autowired
    private Organization2OrganizationResDTOAssembler organization2OrganizationResDTOAssembler;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private RandomPwUtils randomPwUtils;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private OrgRedisUtil orgRedisUtil;

    @Resource
    protected HttpServletResponse response;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentUtil departmentUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private OrgTypeMenuRootDao orgTypeMenuRootDao;

    @Autowired
    private OrgMenuDao orgMenuDao;

    @Autowired
    private SystemEventSendAdapter systemEventSendAdapter;

    @Autowired
    private DeptTreePathUtils deptTreePathUtils;

    @Autowired
    private Organization2OrganizationResDTOAssembler assembler;



    private static final String THREE_HUNDRED = "300";

    public static final int EIGHT = 8;
    public static final int NINE = 9;
    public static final int TEN = 10;
    public static final int FOURTEEN = 14;
    public static final int SIXTEEN = 16;

    /**
     * 雪花算法生成器
     */
    private SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);

    /**
     * 根据用户类型返回
     *
     * @param user
     * @param userType
     * @return
     */
    private String getUsername(String user, Integer userType) {
        if (UserSourceTypeEnum.PORTAL.getCode().equals(userType)) {
            User entity = userRepository.find(new UserId(user));
            if (entity != null) {
                return entity.getUsername();
            }
        }
        return user;
    }


    @Override
    public OrganizationResDTO getOneById(String orgId) {
        Organization organization = organizationRepository.find(new OrganizationId(Long.valueOf(orgId)));
        OrganizationResDTO dto = organization2OrganizationResDTOAssembler.toDTO(organization);
        Optional.ofNullable(organization).ifPresent(e -> {
            // 查询账户所有者信息
            User orgOwner = userRepository.find(new UserId(dto.getOrgOwner()));
            if (orgOwner != null) {
                dto.setOrgOwnerName(orgOwner.getUsername());
                dto.setPassword(orgOwner.getPassword().getHashEncryptValue());
            }
            String createdBy = getUsername(dto.getCreatedBy(), dto.getCreatedByType());
            dto.setCreatedBy(createdBy);
            String updatedBy = getUsername(dto.getUpdatedBy(), dto.getUpdatedByType());
            dto.setUpdatedBy(updatedBy);
            UserDataEntity lastOnlineUser = organizationDao.getLastOnlineUser(orgId);
            if (lastOnlineUser != null) {
                dto.setLastOnlineUser(lastOnlineUser.getUsername());
            }
        });
        return dto;
    }


    /**
     * 新增子账户
     *
     * @param dto
     */
    @LogRecord(operationCode = "insertSonOrganization", operationName = "新增账户", serviceType = ServiceTypeConst.ORGANIZATION_MANAGEMENT)
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insertOrganization(InsertOrganizationDto dto) {
        // 判断只有物联网账户才可以创建账户
        String sessionOrgId = dto.getLoginOrgId();
        Organization sessionOrg = organizationRepository.find(new OrganizationId(Long.valueOf(sessionOrgId)));
        if (!cn.cuiot.dmp.system.user_manage.domain.types.enums.OrgTypeEnum.PROVINCE.getValue().equals(sessionOrg.getOrgTypeId().getValue())) {
            throw new BusinessException(ResultCode.CANNOT_OPERATION);
        }
        // 查询key是否唯一，不唯一则不能新建
        List<Organization> organizationList = organizationRepository.commonQuery(OrganizationCommonQuery.builder().
                orgKey(dto.getOrgKey())
                .build());
        if (CollUtil.isEmpty(organizationList)) {
            throw new BusinessException(ResultCode.ORG_ORGKEY_ERRER);
        }
        // 统一社会信用代码唯一性校验
        Organization tempOrganization = organizationRepository.commonQueryOne(OrganizationCommonQuery.builder().socialCreditCode(dto.getSocialCreditCode()).build());
        String creditCodeExist = tempOrganization != null ? tempOrganization.getSocialCreditCode() : null;
        if (!StringUtils.isEmpty(creditCodeExist)) {
            throw new BusinessException(ResultCode.SOCIAL_CREDIT_CODE_EXIST);
        }
        // 判断要添加进的组织是否为市级组织层级
        DepartmentEntity departmentEntity = departmentService.getDeptById(dto.getDeptId());
        if (!Objects.equals(departmentEntity.getLevel(), NumberConst.TWO)) {
            throw new BusinessException(ResultCode.ONLY_LEVEL_TWO_DEPT);
        }

        User userDataEntity = User.builder().build();

        // 判断手机号是否存在
        if (userPhoneNumberDomainService.judgePhoneNumberAlreadyExists(new PhoneNumber(dto.getPhoneNumber()),UserTypeEnum.USER,UserTypeEnum.NULL)) {
            throw new BusinessException(PHONE_NUMBER_ALREADY_EXIST);
        }
        userDataEntity.setPhoneNumber(new PhoneNumber(dto.getPhoneNumber()));
        String username = dto.getUsername();
        User userByUsername = userRepository.commonQueryOne(UserCommonQuery.builder().username(username).build());
        if (userByUsername != null && userByUsername.getDeletedFlag() == 0) {
            throw new BusinessException(USERNAME_ALREADY_EXIST);
        }

        //获取账户日志操作对象
        String[] operationTarget = new String[1];
        operationTarget[0] = dto.getOrgName();
        dto.setOperationTarget(operationTarget);

        // 创建新用户
        userDataEntity.setUserId(String.valueOf(idWorker.nextId()));
        userDataEntity.setUsername(username);
        //如果密码为空，则表示自动生成密码
        String password = dto.getPassword();
        boolean setPassword = false;
        if (StringUtils.isEmpty(password)) {
            int i = (int) (8 + Math.random() * (20 - 8 + 1));
            password = randomPwUtils.getRandomPassword(i);
            //将生成的随机密码进行加密
            userDataEntity.setPassword(new Password(password));
        } else {
            setPassword = true;
            //不为空，表示已添加密码，进行正则判断
            if (password.matches(RegexConst.PASSWORD_REGEX) && !ValidateUtil.checkRepeat(password)
                    && !ValidateUtil.checkBoardContinuousChar(password)) {
                //加密
                userDataEntity.setPassword(new Password(password));
            } else {
                throw new BusinessException(ResultCode.PASSWORD_IS_INVALID);
            }
        }
        userDataEntity.setCreatedBy(dto.getUserId());
        userDataEntity.setCreatedByType(OperateByTypeEnum.USER);
        userDataEntity.setUserType(UserTypeEnum.USER);
        if (!userRepository.save(userDataEntity)) {
            throw new BusinessException(ResultCode.INNER_ERROR);
        }

        String labelName = userDataDao.selectLabelNameByOrgId(dto.getLabel());
        Integer userLabel = userDataDao.selectLabelIdByName(labelName);
        //新增用户标签表关系
        UserLabelDto userLabelDto = new UserLabelDto();
        userLabelDto.setUserId(userDataEntity.getId().getStrValue());
        userLabelDto.setLabelName(dto.getOtherLabelName());
        userLabelDto.setLabelId(userLabel);
        userLabelDto.setCreatedBy(dto.getUserId());
        userLabelDto.setCreateTime(LocalDateTime.now());
        userDataDao.insertUserLabel(userLabelDto);

        //账户名正则判断
        if (!dto.getOrgName().matches(RegexConst.ORG_NAME)
                && ValidateUtil.calculateStrLength(dto.getOrgName()) > 32) {
            throw new BusinessException(ResultCode.ORG_NAME_ERROR);
        }
        //账户key正则判断
        Integer maxDeptHigh = 2;
        Long roleId = RoleConst.DEFAULT_SUPER_OPERATOR_ROLE_PK;
        List<String> menuList = null;
        if (OrgTypeEnum.COMMUNITY.getCode().equals(dto.getOrgTypeId())) {
            // 物业账户根据标签来确认默认角色id
            Integer label = dto.getLabel();
            if (NINE == label) {
                roleId = RoleConst.DEFAULT_FACTORY_PARK_ADMIN;
            } else if (TEN == label) {
                roleId = RoleConst.DEFAULT_COMMUNITY_ADMIN;
            } else {
                throw new BusinessException(ACCOUNT_LABEL_NOT_EXIST);
            }
            maxDeptHigh = 7;
            List<String> menuIdList = orgTypeMenuRootDao.getMenuIdListByOrgType(dto.getOrgTypeId());
            menuList = dto.getMenuRootList().stream().filter(menuIdList::contains).distinct().collect(Collectors.toList());
        } else if (OrgTypeEnum.COMMON.getCode().equals(dto.getOrgTypeId())) {
            roleId = RoleConst.DEFAULT_COMMON_ADMIN;
            maxDeptHigh = 7;
            List<String> menuIdList = orgTypeMenuRootDao.getMenuIdListByOrgType(dto.getOrgTypeId());
            menuList = dto.getMenuRootList().stream().filter(menuIdList::contains).collect(Collectors.toList());
        }

        //账户类型为企业账户
        Organization organization = Organization.builder()
                .orgId(String.valueOf(idWorker.nextId()))
                .orgKey(dto.getOrgKey())
                .orgName(dto.getOrgName())
                .orgTypeId(cn.cuiot.dmp.system.user_manage.domain.types.enums.OrgTypeEnum.valueOf(dto.getOrgTypeId().longValue()))
                .status(cn.cuiot.dmp.system.user_manage.domain.types.enums.OrgStatusEnum.valueOf(dto.getStatus()))
                //账户所有者
                .orgOwner(userDataEntity.getId())
                .createdBy(dto.getUserId())
                .createdOn(LocalDateTime.now())
                .createdByType(OperateByTypeEnum.valueOf(UserSourceTypeEnum.SYSTEM.getCode()))
                .maxDeptHigh(maxDeptHigh)
                .socialCreditCode(dto.getSocialCreditCode())
                .companyName(dto.getCompanyName())
                .source(OrgSourceEnum.valueOf(dto.getSource()))
                .build();
        organizationRepository.save(organization);

        //插入账户标签信息
        if (StringUtils.isEmpty(dto.getOtherLabelName())) {
                throw new BusinessException(ResultCode.OTHER_LABEL_NAME_NOT_NULL);
        }
        //创建账户标签关系表
        OrgLabelDto orgLabelDto = new OrgLabelDto();
        orgLabelDto.setOrgId(organization.getId().getStrValue());
        orgLabelDto.setLabelId(dto.getLabel());
        orgLabelDto.setLabelName(dto.getOtherLabelName());
        orgLabelDto.setCreatedBy(dto.getUserId());
        orgLabelDto.setCreateTime(LocalDateTime.now());
        organizationDao.insertOrgLabel(orgLabelDto);

        if (!CollectionUtils.isEmpty(menuList)) {
            orgMenuDao.insertOrgMenu(organization.getId().getValue(), menuList, dto.getUserId(), LocalDateTime.now());
        }
        Long currentOrgPk = organization.getId().getValue();
        // 创建根节点组织
        DepartmentEntity rootDepartment = createRootDepartment(dto, currentOrgPk, dto.getDeptId());
        Long deptId = rootDepartment.getId();

        Long oId = organization.getId().getValue();
        Long orgOwner = userDataEntity.getId().getValue();

        // 新建子账户的所有者的角色在该子账户下为默认管理员角色
        userDao.insertFeferRole(orgOwner, oId, roleId);
        // 将数据库的两个默认角色与新建的账户绑定
        Map<String, Object> adminRoleMap = new HashMap<>(3);
        adminRoleMap.put("roleId", roleId);
        adminRoleMap.put("orgId", oId);
        adminRoleMap.put("createdBy", dto.getLoginOrgId());
        roleDao.insertOrgRole(adminRoleMap);

        // 新建子账户的所有者与该账户绑定
        userDao.insertUserOrg(orgOwner, oId, String.valueOf(deptId), dto.getLoginOrgId());
        // 添加租户授权关系
        userDao.insertUserGrant(oId, null, dto.getLoginOrgId(), dto.getDeptId(), null);

        // 文件流输出
        if(!setPassword){
            createCsvFile(username, password);
        }

        return currentOrgPk;
    }

    private DepartmentEntity createRootDepartment(InsertOrganizationDto dto, Long orgId, String deptId) {
        DepartmentEntity departmentEntity = departmentDao.selectByPrimary(Long.valueOf(deptId));
        final String departmentName = dto.getOrgName();
        DepartmentEntity entity = new DepartmentEntity();
        entity.setCode(RandomCodeWorker.generateShortUuid());
        entity.setDepartmentName(departmentName);
        entity.setPath(departmentEntity.getPath() + "-" + entity.getCode());
        entity.setPkOrgId(orgId);
        entity.setLevel(0);
        entity.setCreatedOn(LocalDateTime.now());
        entity.setCreatedBy(dto.getUserId());
        entity.setDGroup(DepartmentGroupEnum.TENANT.getCode());

        orgRedisUtil.doubleDeleteForDbOperation(() -> departmentDao.insertDepartment(entity),
                String.valueOf(entity.getPkOrgId()));
        return entity;
    }

    /**
     * @param username 用户名
     * @return password 未加密密码
     */
    private void createCsvFile(String username, String password) {
        List<JSONObject> jsonList = new ArrayList<>();
        UserDataEntity userInfo = new UserDataEntity();
        userInfo.setUsername(username);
        userInfo.setPassword(password);
        JSONObject jsonObject = JSON.parseObject(JSON.toJSON(userInfo).toString());
        jsonList.add(jsonObject);

        List<Object> head = new ArrayList<>();
        head.add("username");
        head.add("password");

        response.setContentType("text/csv;charset=\"GBK\"");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=credentials.csv");
        try {
            CommonCsvUtil.createCsvFile(head, jsonList, username + "credentials", response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /**
     * 编辑子账户
     *
     * @param dto
     */
    @LogRecord(operationCode = "updateOrganization", operationName = "更新账户", serviceType = ServiceTypeConst.ORGANIZATION_MANAGEMENT)
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long updateOrganization(UpdateOrganizationDto dto) {
        //获取账户日志操作对象
        String[] operationTarget = new String[1];
        operationTarget[0] = dto.getOrgName();
        dto.setOperationTarget(operationTarget);
        //查询修改目标账户是否存在
        Organization tempOrganization = organizationRepository.find(new OrganizationId(dto.getId()));
        if (tempOrganization == null) {
            throw new BusinessException(ResultCode.ORG_ID_NOT_EXIST);
        }
        dto.setOrgTypeId(Math.toIntExact(tempOrganization.getOrgTypeId().getValue()));
        List<String> menuIdList = orgTypeMenuRootDao.getMenuIdListByOrgType(dto.getOrgTypeId());
        List<String> menuList = menuIdList.stream().filter(o -> dto.getMenuRootList().contains(o)).collect(Collectors.toList());
        orgMenuDao.deleteByOrgId(dto.getId());
        if (!CollectionUtils.isEmpty(menuList)) {
            orgMenuDao.insertOrgMenu(dto.getId(), menuList, dto.getUserId(), LocalDateTime.now());
        }
        //账户名称长度校验
        if (ValidateUtil.calculateStrLength(dto.getOrgName()) > Const.ORG_NAME_LENGHT) {
            throw new BusinessException(ResultCode.ORG_NAME_ERROR);
        }
        //账户名正则校验
        if (!dto.getOrgName().matches(RegexConst.ORG_NAME)) {
            throw new BusinessException(ResultCode.ORG_NAME_ERROR);
        }
        // 修改目标租户归属的省公司
        String deptId = userDao.getUserGrantDeptId(dto.getId());
        String loginDeptId = userDao.getDeptId(dto.getUserId(), dto.getOrgId());
        if (!departmentUtil.checkPrivilege(loginDeptId, deptId)) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }

        OrganizationEntity organizationEntity = new OrganizationEntity();

        Organization organization = Organization.builder()
                .id(new OrganizationId(dto.getId()))
                .orgName(dto.getOrgName())
                .status(cn.cuiot.dmp.system.user_manage.domain.types.enums.OrgStatusEnum.valueOf(dto.getStatus()))
                .updatedOn(LocalDateTime.now())
                .updatedBy(dto.getUserId())
                .createdByType(OperateByTypeEnum.valueOf(UserSourceTypeEnum.PORTAL.getCode()))
                .build();
        //更改账户信息
        Long aLong = organizationRepository.save(organization) ? 1L : 0L;

        //修改账户标签信息
        if (StringUtils.isEmpty(dto.getOtherLabelName())) {
                throw new BusinessException(ResultCode.OTHER_LABEL_NAME_NOT_NULL);
        }
        organizationEntity.setOtherLabelName(dto.getOtherLabelName());
        OrgLabelDto orgLabelDto = new OrgLabelDto();
        orgLabelDto.setOrgId(organization.getId().getStrValue());
        orgLabelDto.setLabelId(dto.getLabel());
        orgLabelDto.setLabelName(dto.getOtherLabelName());
        orgLabelDto.setUpdatedBy(dto.getUserId());
        orgLabelDto.setUpdatedOn(LocalDateTime.now());
        organizationDao.updateOrgLabel(orgLabelDto);

        // 查询租户授权关系
        String toDeptId = userDao.getUserGrantDeptId(dto.getId());
        if (!dto.getDeptId().equals(toDeptId)) {
            throw new BusinessException(INNER_ERROR);
        }

        // 添加租户授权关系
        userDao.deleteUserGrant(dto.getId());
        userDao.insertUserGrant(dto.getId(), null, dto.getOrgId(), dto.getDeptId(), null);

        // 更改登陆用户名称， 先找到账户归属用户
        Organization org = organizationRepository.find(new OrganizationId(dto.getId()));

        User ownerUser = userRepository.find(new UserId(org.getOrgOwner().getValue()));
        if (ownerUser == null) {
            // 账号不存在
            throw new BusinessException(ResultCode.USER_ACCOUNT_NOT_EXIST);
        }
        if (!ownerUser.getUsername().equals(dto.getUsername())) {
            throw new BusinessException(ResultCode.USERNAME_CANNOT_UPDATE);
        }
        if (StringUtils.hasLength(dto.getPhoneNumber())) {
            PhoneNumber newPhoneNumber = new PhoneNumber(dto.getPhoneNumber());
            if (!ownerUser.getPhoneNumber().equals(newPhoneNumber)) {
                // 判断手机号是否存在
                if (userPhoneNumberDomainService.judgePhoneNumberAlreadyExists(new PhoneNumber(dto.getPhoneNumber()),UserTypeEnum.USER,UserTypeEnum.NULL)) {
                    throw new BusinessException(PHONE_NUMBER_EXIST);
                }
                ownerUser.setPhoneNumber(newPhoneNumber);
                ownerUser.updatedByPortal();
                userRepository.save(ownerUser);
                redisUtil.del(CacheConst.USER_CACHE_KEY_PREFIX + ownerUser.getId().getStrValue());
            }
        }
        // 如果禁用，则清除登陆信息，踢下线
        if (OrgStatusEnum.DISABLE.getCode().equals(organization.getStatus().getValue())
                && !tempOrganization.getStatus().getValue().equals(organization.getStatus().getValue())) {
            offlineByOrgId(dto.getId());
        } else if (!tempOrganization.getStatus().getValue().equals(organization.getStatus().getValue())) {
            removeOfflineCommand(dto.getId());
        }
        dto.setUserId(String.valueOf(tempOrganization.getOrgOwner().getValue()));
        updatePasswordIfNeed(dto);
        return aLong;
    }

    @Override
    public OrgCsvDto resetOrgPassword(UpdateOrganizationDto dto) {
        String passWord = dto.getPassWord();

        //如果密码为空，则表示自动生成密码
        if (!StringUtils.hasLength(passWord)) {
            int i = (int) (8 + Math.random() * (20 - 8 + 1));
            passWord = randomPwUtils.getRandomPassword(i);

        } else {
            //不为空，表示已添加密码，进行正则判断
            if (!passWord.matches(RegexConst.PASSWORD_REGEX)) {
                throw new BusinessException(ResultCode.PASSWORD_IS_INVALID);
            }
        }

        //更改密码
        OrganizationEntity organizationParams = new OrganizationEntity();
        organizationParams.setId(dto.getId());
        organizationParams.setResetPassword(ResetPasswordEnum.ALREADY_RESET.getCode());
        organizationParams.setUpdatedBy(dto.getUserId());
        organizationParams.setUpdatedByType(UserSourceTypeEnum.PORTAL.getCode());
        organizationParams.setUpdatedOn(LocalDateTime.now());

        // ResetPassword, db、mapper中均为找到该字段
        Organization organization = Organization.builder()
                .id(new OrganizationId(dto.getId()))
                .updatedOn(LocalDateTime.now())
                .updatedBy(dto.getUserId())
                .createdByType(OperateByTypeEnum.valueOf(UserSourceTypeEnum.PORTAL.getCode()))
                .build();

        boolean success = organizationRepository.save(organization);
        if (!success) {
            throw new BusinessException(ResultCode.UPDATE_PASSWORD_FAIL);
        }

        // 更改用户密码
        User updateUserParams = User.builder().build();
        updateUserParams.setPassword(new Password(passWord));
        updateUserParams.setUpdatedBy(String.valueOf(dto.getId()));
        updateUserParams.setUpdatedByType(OperateByTypeEnum.USER);
        updateUserParams.setUpdatedOn(LocalDateTime.now());
        // 修改的条件
        updateUserParams.setId(new UserId(dto.getUserId()));
        if (!userRepository.save(updateUserParams)) {
            throw new BusinessException(ResultCode.UPDATE_PASSWORD_FAIL);
        }

        //组装账户文件表参数
        return new OrgCsvDto(dto.getUsername(), passWord);
    }

    /**
     * 查询子账户详细信息
     *
     * @param orgId
     * @param sessionUserId
     * @param sessionOrgId
     * @return
     */
    @Override
    public GetOrganizationVO findOne(String orgId, String sessionUserId, String sessionOrgId) {
        //当前登陆者的组织
        String loginDeptId = userService.getDeptId(sessionUserId, sessionOrgId);
        // 查看目标租户的组织
        String findDeptId = userDao.getUserGrantDeptId(Long.parseLong(orgId));
        if (!departmentUtil.checkPrivilege(loginDeptId, findDeptId)) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }

        Organization organization = organizationRepository.find(new OrganizationId(orgId));
        OrgLabelDto orgLabelDto = organizationDao.getOrgLabelById(organization.getId().getStrValue());
        GetOrganizationVO vo = organization2GetOrganizationVoAssembler.toDTO(organization);
        vo.setLabel(orgLabelDto.getLabelId());
        vo.setOtherLabelName(orgLabelDto.getLabelName());

        //查询账户所有者登录名，手机号码
        User userById = userRepository.find(new UserId(organization.getOrgOwner().getValue()));
        Optional.ofNullable(userById).ifPresent(e -> {
            vo.setUsername(e.getUsername());
            vo.setPhoneNumber(e.getDecryptedPhoneNumber());
        });
        String deptId = userDao.getUserGrantDeptId(Long.valueOf(orgId));
        if (deptId != null) {
            DepartmentEntity departmentEntity = departmentDao.selectByPrimary(Long.parseLong(deptId));
            vo.setDeptId(Long.parseLong(deptId));
            vo.setDeptName(departmentEntity.getDepartmentName());
        }
        OrgTypeDto orgTypeDto = organizationDao.getOrgType(vo.getOrgTypeId());
        vo.setOrgTypeName(orgTypeDto.getName());
        vo.setMenuRootList(orgMenuDao.getMenuListByOrgId(orgId));
        return vo;
    }


    @Override
    public PageResult<ListOrganizationVO> commercialOrgList(ListOrganizationDto dto) {
        // 获取子组织
        if (StringUtils.hasLength(dto.getDeptId())) {
            List deptIds = departmentService.getChildrenDepartmentIds(String.valueOf(dto.getLoginOrgId()),
                    Long.valueOf(dto.getDeptId()));
            dto.setDeptIds(deptIds);
        }

        PageMethod.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<ListOrganizationVO> voList = queryOrgList(dto);
        PageInfo<ListOrganizationVO> pageInfo = new PageInfo<>(voList);
        return new PageResult<>(pageInfo);
    }

    private List<ListOrganizationVO> queryOrgList(ListOrganizationDto dto) {
        // 根据手机号查询租户id
        if (!StringUtils.isEmpty(dto.getPhoneNumber())) {
            String orgId = organizationDao.getOrgIdByUserPhoneNumber(Sm4.encryption(dto.getPhoneNumber()));
            if (StringUtils.isEmpty(orgId)) {
                return Collections.emptyList();
            }
            dto.setOrgId(orgId);
        }
        List<ListOrganizationVO> voList = organizationDao.getCommercialOrgList(dto);
        for (ListOrganizationVO vo : voList) {
            vo.setPhoneNumber(Sm4.decrypt(vo.getPhoneNumber()));
            IStrategy iStrategy = new StrategyPhone();
            // 脱敏
            vo.setPhoneNumber((String) iStrategy.des(vo.getPhoneNumber(), null));
            if (OrgSourceEnum.ENTERPRISE.getValue().equals(vo.getSource())) {
                vo.setCreatedByChannel("物联应用平台");
                vo.setCreatedBy(null);
            } else if(OrgSourceEnum.PRIVATE.getValue().equals(vo.getSource())){
                vo.setCreatedByChannel("管理员创建");
                if (StringUtils.isEmpty(vo.getCreatedBy())) {
                    vo.setCreatedBy(vo.getUsername());
                }
            } else if(OrgSourceEnum.SELF_REGISTRATION.getValue().equals(vo.getSource())){
                vo.setCreatedByChannel("自注册");
            }
        }
        return voList;
    }

    @LogRecord(operationCode = "updateStatus", operationName = "启用/禁用账户", serviceType = ServiceTypeConst.ORGANIZATION_MANAGEMENT)
    @Override
    public Integer operateOrganization(OperateOrganizationDto dto) {
        Organization organization = Organization.builder()
                .id(new OrganizationId(dto.getId()))
                .status(cn.cuiot.dmp.system.user_manage.domain.types.enums.OrgStatusEnum.valueOf(dto.getStatus()))
                .build();
        Integer result = organizationRepository.save(organization) ? 1 : 0;
        // 如果禁用，则清除登陆信息，踢下线
        if (OrgStatusEnum.DISABLE.getCode().equals(dto.getStatus())) {
            offlineByOrgId(dto.getId());
        } else {
            removeOfflineCommand(dto.getId());
        }
        return result;
    }

    private void removeOfflineCommand(Long id) {
        String oldKickOrgIds = redisUtil.get(CacheConst.LOGIN_ORG_TO_KICK);
        if (!StringUtils.isEmpty(oldKickOrgIds)) {
            String[] oldOrgIdArr = oldKickOrgIds.split(",");
            List<String> oldOrgIds = Arrays.asList(oldOrgIdArr);
            // 过滤重复id
            Set orgSet = new HashSet<>(oldOrgIds);
            List<String> noDuplicateOrgIds = new ArrayList<>(orgSet);
            if (noDuplicateOrgIds.contains(String.valueOf(id))) {
                noDuplicateOrgIds.remove(String.valueOf(id));
            }
            if (!CollectionUtils.isEmpty(noDuplicateOrgIds)) {
                String newOrgIds = org.apache.commons.lang.StringUtils.join(noDuplicateOrgIds, ",");
                redisUtil.set(CacheConst.LOGIN_ORG_TO_KICK, newOrgIds, Const.SESSION_TIME);
            } else {
                redisUtil.del(CacheConst.LOGIN_ORG_TO_KICK);
            }
        }
    }

    private void offlineByOrgId(Long orgId) {
        String oldKickOrgIds = redisUtil.get(CacheConst.LOGIN_ORG_TO_KICK);
        if (!StringUtils.isEmpty(oldKickOrgIds)) {
            if (oldKickOrgIds.contains(String.valueOf(orgId))) {
                return;
            }
            oldKickOrgIds = oldKickOrgIds + "," + orgId;
        } else {
            oldKickOrgIds = String.valueOf(orgId);
        }
        redisUtil.set(CacheConst.LOGIN_ORG_TO_KICK, oldKickOrgIds, Const.SESSION_TIME);
    }

    private void updatePasswordIfNeed(UpdateOrganizationDto dto) {
        if (ResetPasswordEnum.RESET.getCode().equals(dto.getResetPassword())) {
            OrgCsvDto orgCsvDto = resetOrgPassword(dto);
            log.info("用户数据存储");

            // 文件流输出
            List<JSONObject> jsonList = new ArrayList<>();
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSON(orgCsvDto).toString());
            jsonList.add(jsonObject);

            List<Object> head = new ArrayList<>();
            head.add("userName");
            head.add("password");

            response.setContentType("text/csv;charset=\"GBK\"");
            response.setHeader("Content-Disposition", "attachment; filename=credentials.csv");
            String[] split = orgCsvDto.getUserName().split("@");
            String userName = split[0];
            try {
                CommonCsvUtil.createCsvFile(head, jsonList, userName + "credentials", response);
            } catch (UnsupportedEncodingException e) {
                log.error("updateOrg error.", e);
            }
        }
    }

    @Override
    public List<OrgTypeDto> getOrgTypeList() {
        return organizationDao.getOrgTypeList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAccount(String orgId) {
        String path = departmentDao.getPathByOrgId(orgId);
        // 根据账户id获取用户数量
        Integer userCount = userDao.getUserCount(path);
        if (userCount > NumberConst.ONE) {
            throw new BusinessException(ResultCode.ACCOUNT_HAS_USER);
        }
        // 获取是否有小区
        Integer communityCount = departmentDao.getCommunityByPath(path);
        if (communityCount > NumberConst.ZERO) {
            throw new BusinessException(ResultCode.ACCOUNT_HAS_COMMUNITY);
        }
        OrganizationEntity needDelete = null;
        try {
            Organization organization = organizationRepository.find(new OrganizationId(Long.valueOf(orgId)));
            needDelete = organization2EntityAssembler.toDTO(organization);
            organizationRepository.remove(organization);
            List<Long> userList = userDao.getUserId(orgId);
            userDao.deleteUserOrgByOrgId(orgId);
            if (!CollectionUtils.isEmpty(userList)) {
                userRepository.removeList(userList.stream().map(UserId::new).collect(Collectors.toList()));
                userRepository.removeUserRelate(userList);
            }
            DepartmentEntity communityIdByPath = departmentDao.getCommunityIdByPath(path);
            if(!ObjectUtil.isEmpty(communityIdByPath)){
                departmentDao.deleteByPrimaryKey(communityIdByPath.getId());
            }
            organizationDao.delOrgLab(orgId);
            organizationDao.delOrgRole(orgId);
            userDao.deleteUserGrant(Long.parseLong(orgId));
        } catch (Exception e) {
            log.error("账户删除失败 {}", e.getMessage());
            throw new BusinessException(ResultCode.ACCOUNT_DELETED_ERROR);
        }

        //发送事件
        systemEventSendAdapter.sendOrganizationDeleteActionEvent(needDelete);
        return 1;
    }

    @Override
    public CompanyDetailResDto companyDetail(CompanyDetailReqDto companyDetailReqDto) {
        Organization organization = organizationRepository.find(new OrganizationId(Long.valueOf(companyDetailReqDto.getOrgId())));
        if (organization == null) {
            throw new BusinessException(OBJECT_NOT_EXIST);
        }
        if (!cn.cuiot.dmp.system.user_manage.domain.types.enums.OrgTypeEnum.COMMUNITY.getValue().equals(organization.getOrgTypeId().getValue())
                && !cn.cuiot.dmp.system.user_manage.domain.types.enums.OrgTypeEnum.COMMON.getValue().equals(organization.getOrgTypeId().getValue())) {
            return null;
        }
        CompanyDetailResDto companyDetailResDto = new CompanyDetailResDto();
        companyDetailResDto.setCompanyName(organization.getCompanyName());
        companyDetailResDto.setSocialCreditCode(organization.getSocialCreditCode());
        return companyDetailResDto;
    }

    @Override
    public void updateCompany(UpdateCompanyReqDto updateCompanyReqDto) {
        Organization organization = organizationRepository.find(new OrganizationId(Long.valueOf(updateCompanyReqDto.getOrgId())));
        if (organization == null) {
            throw new BusinessException(OBJECT_NOT_EXIST);
        }
        if (!cn.cuiot.dmp.system.user_manage.domain.types.enums.OrgTypeEnum.COMMUNITY.getValue().equals(organization.getOrgTypeId().getValue())
                && !cn.cuiot.dmp.system.user_manage.domain.types.enums.OrgTypeEnum.COMMON.getValue().equals(organization.getOrgTypeId().getValue())) {
            return;
        }
        if (!StringUtils.isEmpty(organization.getCompanyName())
                || !StringUtils.isEmpty(organization.getSocialCreditCode())) {
            throw new BusinessException(ResultCode.COMPANY_INFO_ALREADY_EXIST);
        }

        OrganizationCommonQuery organizationCommonQuery = OrganizationCommonQuery.builder()
                .id(new OrganizationId(updateCompanyReqDto.getOrgId()))
                .orgTypeId(cn.cuiot.dmp.system.user_manage.domain.types.enums.OrgTypeEnum.COMMUNITY)
                .build();

        Organization updateOrganization = Organization.builder()
                .companyName(updateCompanyReqDto.getCompanyName())
                .socialCreditCode(updateCompanyReqDto.getSocialCreditCode())
                .updatedBy(updateCompanyReqDto.getUserId())
                .updatedOn(LocalDateTime.now())
                .updatedByType(OperateByTypeEnum.USER)
                .build();

        boolean success = organizationRepository.updateByParams(updateOrganization, organizationCommonQuery);

        if (!success) {
            throw new BusinessException(ResultCode.INNER_ERROR);
        }
    }

    @Override
    public PageResult<UserDataResDTO> queryUserPageList(UserDataReqDTO userDataReqDTO) {
        // 当前登陆者的组织
        String loginDeptId = userService.getDeptId(userDataReqDTO.getSessionUserId(), userDataReqDTO.getSessionOrgId());
        // 查看目标租户的组织
        String findDeptId = userDao.getUserGrantDeptId(userDataReqDTO.getOrgId());
        // 越权校验
        if (Boolean.FALSE.equals(departmentUtil.checkPrivilege(loginDeptId, findDeptId))) {
            return new PageResult<>();
        }

        Map<String, Object> params = new HashMap<>(16);
        if (!StringUtils.isEmpty(userDataReqDTO.getUsername())) {
            params.put("userName", userDataReqDTO.getUsername());
        }
        if (!StringUtils.isEmpty(userDataReqDTO.getPhoneNumber())) {
            String decrypt = Sm4.encryption(userDataReqDTO.getPhoneNumber());
            params.put("phone", decrypt);
        }
        params.put("orgId", userDataReqDTO.getOrgId());
        return userService.getPage(params, String.valueOf(userDataReqDTO.getOrgId()), userDataReqDTO.getCurrentPage(), userDataReqDTO.getPageSize(), null);
    }

    @Override
    public void resetUserPassword(ResetUserPasswordReqDTO resetUserPasswordReqDTO) {
        String userId = String.valueOf(resetUserPasswordReqDTO.getId());
        // 当前登陆者的path
        String userPath = deptTreePathUtils.getDeptTreePath(resetUserPasswordReqDTO.getSessionOrgId(), resetUserPasswordReqDTO.getSessionUserId());
        // 重置目标用户的path
        String reqPath = departmentUtil.getUserDept(userId).getPath();
        // 越权校验
        if (!reqPath.startsWith(userPath)) {
            throw new BusinessException(ResultCode.RESET_PASSWORD_ERROR);
        }
        UserBo userBo = new UserBo();
        userBo.setUserId(userId);
        redisUtil.del(CacheConst.USER_CACHE_KEY_PREFIX + userId);
        log.info("【账户详情下用户重置密码】，redis，key：{}", CacheConst.USER_CACHE_KEY_PREFIX + userId);
        UserCsvDto userCsvDto = userService.resetPasswordWithOutSms(userBo);
        // 文件流输出
        createCsvFile(userCsvDto.getUsername(), userCsvDto.getPassword());
    }

}
