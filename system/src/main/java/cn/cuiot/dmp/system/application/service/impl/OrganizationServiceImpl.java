package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.base.application.enums.OrgStatusEnum;
import cn.cuiot.dmp.base.application.utils.CommonCsvUtil;
import cn.cuiot.dmp.base.infrastructure.constants.MsgBindingNameConstants;
import cn.cuiot.dmp.base.infrastructure.constants.MsgTagConstants;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.stream.StreamMessageSender;
import cn.cuiot.dmp.base.infrastructure.stream.messaging.SimpleMsg;
import cn.cuiot.dmp.base.infrastructure.syslog.LogContextHolder;
import cn.cuiot.dmp.base.infrastructure.syslog.OptTargetData;
import cn.cuiot.dmp.base.infrastructure.syslog.OptTargetInfo;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.EventActionEnum;
import cn.cuiot.dmp.common.enums.OrgTypeEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.*;
import cn.cuiot.dmp.domain.types.Password;
import cn.cuiot.dmp.domain.types.PhoneNumber;
import cn.cuiot.dmp.domain.types.enums.OperateByTypeEnum;
import cn.cuiot.dmp.domain.types.enums.UserTypeEnum;
import cn.cuiot.dmp.domain.types.id.OrganizationId;
import cn.cuiot.dmp.domain.types.id.UserId;
import cn.cuiot.dmp.system.application.enums.DepartmentGroupEnum;
import cn.cuiot.dmp.system.application.enums.ResetPasswordEnum;
import cn.cuiot.dmp.system.application.enums.UserSourceTypeEnum;
import cn.cuiot.dmp.system.application.feign.ContentApiFeignService;
import cn.cuiot.dmp.system.application.param.assembler.Organization2EntityAssembler;
import cn.cuiot.dmp.system.application.param.assembler.Organization2GetOrganizationVoAssembler;
import cn.cuiot.dmp.system.application.param.assembler.Organization2OrganizationResDTOAssembler;
import cn.cuiot.dmp.system.application.param.event.OrganizationActionEvent;
import cn.cuiot.dmp.system.application.service.AuditConfigService;
import cn.cuiot.dmp.system.application.service.DepartmentService;
import cn.cuiot.dmp.system.application.service.OrganizationService;
import cn.cuiot.dmp.system.application.service.UserService;
import cn.cuiot.dmp.system.domain.entity.Organization;
import cn.cuiot.dmp.system.domain.entity.User;
import cn.cuiot.dmp.system.domain.query.OrganizationCommonQuery;
import cn.cuiot.dmp.system.domain.repository.FormConfigTypeRepository;
import cn.cuiot.dmp.system.domain.repository.OrganizationRepository;
import cn.cuiot.dmp.system.domain.repository.UserRepository;
import cn.cuiot.dmp.system.domain.service.UserPhoneNumberDomainService;
import cn.cuiot.dmp.system.domain.types.enums.OrgSourceEnum;
import cn.cuiot.dmp.system.domain.types.enums.UserStatusEnum;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.entity.OrganizationEntity;
import cn.cuiot.dmp.system.infrastructure.entity.UserDataEntity;
import cn.cuiot.dmp.system.infrastructure.entity.bo.UserBo;
import cn.cuiot.dmp.system.infrastructure.entity.dto.*;
import cn.cuiot.dmp.system.infrastructure.entity.vo.GetOrganizationVO;
import cn.cuiot.dmp.system.infrastructure.entity.vo.ListOrganizationVO;
import cn.cuiot.dmp.system.infrastructure.messaging.spring.SystemEventSendAdapter;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.*;
import cn.cuiot.dmp.system.infrastructure.utils.DepartmentUtil;
import cn.cuiot.dmp.system.infrastructure.utils.DeptTreePathUtils;
import cn.cuiot.dmp.system.infrastructure.utils.OrgRedisUtil;
import cn.cuiot.dmp.system.infrastructure.utils.RandomPwUtils;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cn.cuiot.dmp.common.constant.ResultCode.PHONE_NUMBER_ALREADY_EXIST;

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
    private OrgTypeMenuDao orgTypeMenuDao;

    @Autowired
    private OrgMenuDao orgMenuDao;

    @Autowired
    private SystemEventSendAdapter systemEventSendAdapter;

    @Autowired
    private StreamMessageSender streamMessageSender;

    @Autowired
    private DeptTreePathUtils deptTreePathUtils;

    @Autowired
    private FormConfigTypeRepository formConfigTypeRepository;

    @Autowired
    private AuditConfigService auditConfigService;

    @Autowired
    private ContentApiFeignService contentApiFeignService;

    /**
     * 根据用户类型返回
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
        Organization organization = organizationRepository
                .find(new OrganizationId(Long.valueOf(orgId)));
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
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insertOrganization(InsertOrganizationDto dto) {
        //获取当前登录用户的账号信息
        Organization sessionOrg = organizationRepository
                .find(new OrganizationId(dto.getSessionOrgId()));
        // 判断只有平台账户才可以创建账户
        if (!OrgTypeEnum.PLATFORM.getValue()
                .equals(sessionOrg.getOrgTypeId().getValue())) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }

        //判断选择的所属组织是否可以选择
        String sessionDeptId = userDao
                .getDeptId(dto.getSessionUserId().toString(), dto.getSessionOrgId().toString());
        if (!departmentUtil.checkPrivilege(sessionDeptId, dto.getDeptId().toString())) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION, "所属组织不可选");
        }

        //判断企业编码是否唯一
        List<Organization> organizationList = organizationRepository
                .commonQuery(OrganizationCommonQuery.builder().
                        orgKey(dto.getOrgKey())
                        .build());
        if (CollectionUtils.isNotEmpty(organizationList)) {
            throw new BusinessException(ResultCode.ORG_ORGKEY_ERRER, "企业编码已存在");
        }

        // 判断要添加进的组织是否为市级组织层级
        /*DepartmentEntity departmentEntity = departmentService.getDeptById(dto.getDeptId().toString());
        if (!Objects.equals(departmentEntity.getLevel(), NumberConst.TWO)) {
            throw new BusinessException(ResultCode.ONLY_LEVEL_TWO_DEPT);
        }*/

        // 判断手机号是否存在
        if (userPhoneNumberDomainService
                .judgePhoneNumberAlreadyExists(new PhoneNumber(dto.getPhoneNumber()),
                        UserTypeEnum.USER, UserTypeEnum.NULL)) {
            throw new BusinessException(PHONE_NUMBER_ALREADY_EXIST, "手机号已存在，请更换手机号");
        }

        // 判断用户名是否存在
        /*String username = dto.getPhoneNumber();
        User userByUsername = userRepository.commonQueryOne(UserCommonQuery.builder().username(username).build());
        if (userByUsername != null && userByUsername.getDeletedFlag() == 0) {
            throw new BusinessException(USERNAME_ALREADY_EXIST);
        }*/

        /**
         * 构建用户信息
         */
        User userDataEntity = User.builder().build();
        //用户名默认为手机号
        String username = dto.getPhoneNumber();
        //随机用户密码
        String password = randomPwUtils.getRandomPassword((int) (8 + Math.random() * (20 - 8 + 1)));
        userDataEntity.setName(dto.getAdminName());
        userDataEntity.setPhoneNumber(new PhoneNumber(dto.getPhoneNumber()));
        userDataEntity.setUsername(username);
        userDataEntity.setPassword(new Password(password));
        userDataEntity.setCreatedBy(dto.getSessionUserId().toString());
        userDataEntity.setCreatedByType(OperateByTypeEnum.USER);
        userDataEntity.setUserType(UserTypeEnum.USER);
        userDataEntity.setStatus(UserStatusEnum.OPEN);
        if (!userRepository.save(userDataEntity)) {
            throw new BusinessException(ResultCode.INNER_ERROR);
        }

        //获取账户日志操作对象
        String[] operationTarget = new String[1];
        operationTarget[0] = dto.getCompanyName();
        dto.setOperationTarget(operationTarget);

        Long orgTypeId = OrgTypeEnum.ENTERPRISE.getValue();
        Integer maxDeptHigh = 7;
        /**
         * 权限配置
         */
        List<String> menuList = dto.getMenuList();
        int size = menuList.size();
        List<String> menuIdList = orgTypeMenuDao.getMenuIdListByOrgType(orgTypeId);
        menuList = menuList.stream().filter(menuIdList::contains).distinct()
                .collect(Collectors.toList());
        if (size != menuList.size()) {
            throw new BusinessException(ResultCode.CANNOT_OPERATION, "配置企业权限有误");
        }

        //账户类型为企业账户
        Organization organization = Organization.builder()
                .orgKey(dto.getOrgKey())
                .orgName(dto.getCompanyName())
                .companyName(dto.getCompanyName())
                .orgTypeId(OrgTypeEnum
                        .valueOf(orgTypeId))
                .status(cn.cuiot.dmp.system.domain.types.enums.OrgStatusEnum.ENABLE)
                //账户所有者
                .orgOwner(userDataEntity.getId())
                .createdBy(dto.getSessionUserId().toString())
                .createdOn(LocalDateTime.now())
                .createdByType(OperateByTypeEnum.valueOf(UserSourceTypeEnum.SYSTEM.getCode()))
                .maxDeptHigh(maxDeptHigh)
                .description(dto.getDescription())
                .expStartDate(dto.getExpStartDate())
                .expEndDate(DateUtil.endOfDay(dto.getExpEndDate()))
                .source(OrgSourceEnum.PRIVATE)
                .build();
        organizationRepository.save(organization);

        Long pkOrgId = organization.getId().getValue();

        //设置日志操作对象内容
        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                .name("企业")
                .targetDatas(Lists.newArrayList(new OptTargetData(organization.getCompanyName(), organization.getId().getValue().toString())))
                .build());

        //保存菜单权限
        if (!CollectionUtils.isEmpty(menuList)) {
            List<OrgMenuDto> menuDtoList = menuList.stream().map(ite -> {
                return new OrgMenuDto(SnowflakeIdWorkerUtil.nextId(), pkOrgId, Long.valueOf(ite),
                        LocalDateTime.now(), dto.getSessionUserId());
            }).collect(Collectors.toList());
            orgMenuDao.insertOrgMenu(menuDtoList);
        }

        // 创建根节点组织
        DepartmentEntity rootDepartment = createRootDepartment(dto, pkOrgId,
                dto.getDeptId().toString());
        Long pkDeptId = rootDepartment.getId();

        // 用户与账号绑定
        userDao.insertUserOrg(SnowflakeIdWorkerUtil.nextId(), userDataEntity.getId().getValue(),
                pkOrgId, String.valueOf(pkDeptId),
                dto.getSessionOrgId().toString());

        // 添加租户授权关系
        userDao.insertUserGrant(SnowflakeIdWorkerUtil.nextId(), pkOrgId, null,
                dto.getSessionOrgId().toString(),
                dto.getDeptId().toString(), null);

        //发送事件
        OrganizationEntity toDTO = organization2EntityAssembler.toDTO(organization);
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    systemEventSendAdapter.sendOrganizationCreateActionEvent(toDTO);
                }
            });
        } else {
            systemEventSendAdapter.sendOrganizationCreateActionEvent(toDTO);
        }

        // 文件流输出
        createCsvFile(username, dto.getPhoneNumber(), password);

        //发送MQ消息
        streamMessageSender.sendGenericMessage(
                MsgBindingNameConstants.SYSTEM_PRODUCER,
                SimpleMsg.builder()
                        .delayTimeLevel(2)
                        .operateTag(MsgTagConstants.ORGANIZATION_ADD)
                        .data(toDTO)
                        .dataId(toDTO.getId())
                        .info("创建企业")
                        .build());

        return pkOrgId;
    }

    /**
     * 创建根节点组织
     */
    private DepartmentEntity createRootDepartment(InsertOrganizationDto dto, Long pkOrgId,
                                                  String deptId) {

        DepartmentEntity parentEntity = departmentDao.selectByPrimary(Long.valueOf(deptId));

        DepartmentEntity entity = new DepartmentEntity();
        entity.setId(SnowflakeIdWorkerUtil.nextId());
        entity.setCode(RandomCodeWorker.generateShortUuid());
        entity.setDepartmentName(dto.getCompanyName());
        entity.setPath(parentEntity.getPath() + "-" + entity.getCode());
        entity.setPathName(dto.getCompanyName());
        entity.setPkOrgId(pkOrgId);
        entity.setLevel(0);
        entity.setCreatedOn(LocalDateTime.now());
        entity.setCreatedBy(dto.getSessionUserId().toString());
        entity.setDGroup(DepartmentGroupEnum.TENANT.getCode());
        orgRedisUtil.doubleDeleteForDbOperation(() -> departmentDao.insertDepartment(entity),
                String.valueOf(entity.getPkOrgId()));
        return entity;
    }

    /**
     * 生成用户密码CSV文件数据
     *
     * @param username 用户名
     * @return password 未加密密码
     */
    private void createCsvFile(String username, String phoneNumber, String password) {
        List<JSONObject> jsonList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("用户名", username);
        jsonObject.put("手机号", phoneNumber);
        jsonObject.put("密码", password);
        jsonList.add(jsonObject);

        List<Object> head = new ArrayList<>();
        head.add("用户名");
        head.add("手机号");
        head.add("密码");

        response.setContentType("text/csv;charset=\"UTF-8\"");
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
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrganization(UpdateOrganizationDto dto) {
        //获取当前登录用户的账号信息
        Organization sessionOrg = organizationRepository
                .find(new OrganizationId(dto.getSessionOrgId()));
        // 判断只有平台账户才可以修改账户
        if (!OrgTypeEnum.PLATFORM.getValue()
                .equals(sessionOrg.getOrgTypeId().getValue())) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }
        //查询修改目标账户是否存在
        Long pkOrgId = dto.getId();
        Organization oldOrganization = organizationRepository
                .find(new OrganizationId(pkOrgId));
        if (oldOrganization == null) {
            throw new BusinessException(ResultCode.ORG_ID_NOT_EXIST);
        }

        //设置日志操作对象内容
        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                .name("企业")
                .targetDatas(Lists.newArrayList(new OptTargetData(oldOrganization.getOrgName(), oldOrganization.getId().getValue().toString())))
                .build());

        //判断登录用户是否有权限修改
        String grantDeptId = userDao.getUserGrantDeptId(pkOrgId);
        String sessionDeptId = userDao
                .getDeptId(dto.getSessionUserId().toString(), dto.getSessionOrgId().toString());
        if (!departmentUtil.checkPrivilege(sessionDeptId, grantDeptId)) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }

        //判断选择的所属组织是否可以选择
        if (!departmentUtil.checkPrivilege(sessionDeptId, dto.getDeptId().toString())) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION, "所属组织不可选");
        }

        //获取账户日志操作对象
        String[] operationTarget = new String[1];
        operationTarget[0] = dto.getCompanyName();
        dto.setOperationTarget(operationTarget);

        /**
         * 保存菜单权限
         */
        Long orgTypeId = OrgTypeEnum.ENTERPRISE.getValue();
        List<String> menuList = dto.getMenuList();
        int size = menuList.size();
        List<String> menuIdList = orgTypeMenuDao.getMenuIdListByOrgType(orgTypeId);
        menuList = menuList.stream().filter(menuIdList::contains).distinct()
                .collect(Collectors.toList());
        if (size != menuList.size()) {
            throw new BusinessException(ResultCode.CANNOT_OPERATION, "配置企业权限有误");
        }

        orgMenuDao.deleteByOrgId(pkOrgId);
        if (!CollectionUtils.isEmpty(menuList)) {
            List<OrgMenuDto> menuDtoList = menuList.stream().map(ite -> {
                return new OrgMenuDto(SnowflakeIdWorkerUtil.nextId(), pkOrgId, Long.valueOf(ite),
                        LocalDateTime.now(), dto.getSessionUserId());
            }).collect(Collectors.toList());
            orgMenuDao.insertOrgMenu(menuDtoList);
        }

        Organization organization = Organization.builder()
                .id(new OrganizationId(dto.getId()))
                .orgName(dto.getCompanyName())
                .companyName(dto.getCompanyName())
                .description(dto.getDescription())
                .expStartDate(dto.getExpStartDate())
                .expEndDate(DateUtil.endOfDay(dto.getExpEndDate()))
                .updatedOn(LocalDateTime.now())
                .updatedBy(dto.getSessionUserId().toString())
                .createdByType(OperateByTypeEnum.valueOf(UserSourceTypeEnum.PORTAL.getCode()))
                .build();
        //更改账户信息
        organizationRepository.save(organization);

        // 查询租户授权关系
        //String toDeptId = userDao.getUserGrantDeptId(pkOrgId);
        /*if (!dto.getDeptId().equals(toDeptId)) {
            throw new BusinessException(INNER_ERROR);
        }*/

        // 修改租户授权关系
        userDao.deleteUserGrant(pkOrgId);
        userDao.insertUserGrant(SnowflakeIdWorkerUtil.nextId(), pkOrgId, null,
                dto.getSessionOrgId().toString(),
                dto.getDeptId().toString(), null);

        //修改组织结构
        List<DepartmentEntity> departmentEntities = departmentDao
                .selectRootByOrgId(pkOrgId.toString());
        DepartmentEntity departmentEntity = null;
        if (CollectionUtils.isNotEmpty(departmentEntities)) {
            departmentEntity = departmentEntities.get(0);
        }
        //变更所属组织需要path
        if (!dto.getDeptId().toString().equals(grantDeptId)) {
            DepartmentEntity parentDept = departmentDao.selectByPrimary(dto.getDeptId());
            if (Objects.nonNull(departmentEntity)) {
                String oldPath = departmentEntity.getPath();
                String newPath = parentDept.getPath() + "-" + departmentEntity.getCode();
                departmentEntity.setPath(newPath);
                departmentDao.updatePath(oldPath, newPath);
            }
        }
        //变更名称,需要变更path name
        if (!oldOrganization.getCompanyName().equals(dto.getCompanyName())) {
            if (Objects.nonNull(departmentEntity)) {
                departmentEntity.setDepartmentName(dto.getCompanyName());
                departmentDao.updateDepartmentName(departmentEntity.getId(),
                        departmentEntity.getDepartmentName());
                departmentDao.updatePathNameByPath(departmentEntity.getPath(),
                        oldOrganization.getCompanyName(), dto.getCompanyName());
            }
        }

        // 更改登陆用户名称， 先找到账户归属用户
        Organization org = organizationRepository.find(new OrganizationId(pkOrgId));
        User ownerUser = userRepository.find(new UserId(org.getOrgOwner().getValue()));
        if (ownerUser == null) {
            // 账号不存在
            throw new BusinessException(ResultCode.USER_ACCOUNT_NOT_EXIST);
        }

        dto.setOrgOwnerUserId(ownerUser.getId().toString());
        dto.setOrgOwnerUsername(ownerUser.getUsername());
        //需要重置密码
        updatePasswordIfNeed(dto);

        //发送事件
        OrganizationEntity toDTO = organization2EntityAssembler.toDTO(organization);
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    systemEventSendAdapter.sendOrganizationUpdateActionEvent(toDTO);
                }
            });
        } else {
            systemEventSendAdapter.sendOrganizationUpdateActionEvent(toDTO);
        }

        //发送MQ消息
        streamMessageSender.sendGenericMessage(
                MsgBindingNameConstants.SYSTEM_PRODUCER,
                SimpleMsg.builder()
                        .delayTimeLevel(2)
                        .operateTag(MsgTagConstants.ORGANIZATION_UPDATE)
                        .data(toDTO)
                        .dataId(toDTO.getId())
                        .info("修改企业")
                        .build());
    }

    /**
     * 重置企业密码
     */
    @Override
    public OrgCsvDto resetOrgPassword(UpdateOrganizationDto dto) {

        String passWord = randomPwUtils.getRandomPassword((int) (8 + Math.random() * (20 - 8 + 1)));

        // ResetPassword, db、mapper中均为找到该字段
        Organization organization = Organization.builder()
                .id(new OrganizationId(dto.getId()))
                .updatedOn(LocalDateTime.now())
                .updatedBy(dto.getSessionUserId().toString())
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
        updateUserParams.setId(new UserId(dto.getOrgOwnerUserId()));
        if (!userRepository.save(updateUserParams)) {
            throw new BusinessException(ResultCode.UPDATE_PASSWORD_FAIL);
        }
        //组装账户文件表参数
        return new OrgCsvDto(dto.getOrgOwnerUsername(), passWord);
    }

    /**
     * 查询子账户详细信息
     */
    @Override
    public GetOrganizationVO findOne(String pkOrgId, String sessionUserId, String sessionOrgId) {

        //获取当前登录用户的账号信息
        Organization sessionOrg = organizationRepository
                .find(new OrganizationId(sessionOrgId));

        if (!OrgTypeEnum.PLATFORM.getValue()
                .equals(sessionOrg.getOrgTypeId().getValue())) {
            if(!pkOrgId.equals(sessionOrgId)){
                throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
            }
        }

        String grantDeptId = userDao.getUserGrantDeptId(Long.parseLong(pkOrgId));

        //查看数据权限判断
        /*String loginDeptId = userService.getDeptId(sessionUserId, sessionOrgId);
        if (!departmentUtil.checkPrivilege(loginDeptId, grantDeptId)) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }*/

        Organization organization = organizationRepository.find(new OrganizationId(pkOrgId));

        GetOrganizationVO vo = organization2GetOrganizationVoAssembler.toDTO(organization);

        /*OrgLabelDto orgLabelDto = organizationDao
                .getOrgLabelById(organization.getId().getStrValue());
        vo.setLabel(orgLabelDto.getLabelId());
        vo.setOtherLabelName(orgLabelDto.getLabelName());*/

        /**
         * 查询账户所有者姓名、登录名、手机号码
         */
        User userById = userRepository.find(new UserId(organization.getOrgOwner().getValue()));
        Optional.ofNullable(userById).ifPresent(e -> {
            vo.setAdminName(e.getName());
            vo.setUsername(e.getUsername());
            vo.setPhoneNumber(e.getDecryptedPhoneNumber());
        });
        /**
         * 设置所属组织信息
         */
        if (Objects.nonNull(grantDeptId)) {
            DepartmentEntity departmentEntity = departmentDao
                    .selectByPrimary(Long.parseLong(grantDeptId));
            vo.setDeptId(Long.parseLong(grantDeptId));
            vo.setDeptName(departmentEntity.getDepartmentName());
        }
        //设置组织类型
        OrgTypeDto orgTypeDto = organizationDao.getOrgType(vo.getOrgTypeId());
        vo.setOrgTypeName(orgTypeDto.getName());

        //设置已勾选配置的菜单权限
        vo.setMenuList(orgMenuDao.getMenuListByOrgId(pkOrgId));

        return vo;
    }


    @Override
    public PageResult<ListOrganizationVO> commercialOrgList(ListOrganizationDto dto) {
        //获取当前登录用户的企业信息
        Organization sessionOrg = organizationRepository
                .find(new OrganizationId(dto.getLoginOrgId()));
        if(OrgTypeEnum.PLATFORM.getValue().equals(sessionOrg.getOrgTypeId().getValue())){
            if (StringUtils.isBlank(dto.getDeptId())) {
                String deptId = userService.getDeptId(dto.getLoginUserId().toString(), dto.getLoginOrgId().toString());
                dto.setDeptId(deptId);
            }
            // 获取子组织
            if (StringUtils.isNotBlank(dto.getDeptId())) {
                List deptIds = departmentService
                        .getChildrenDepartmentIds(String.valueOf(dto.getLoginOrgId()),
                                Long.valueOf(dto.getDeptId()));
                dto.setDeptIds(deptIds);
            }
            // 根据手机号查询企业id
            if (!StringUtils.isEmpty(dto.getPhoneNumber())) {
                String orgId = organizationDao
                        .getOrgIdByUserPhoneNumber(Sm4.encryption(dto.getPhoneNumber()));
                dto.setOrgId(orgId);
            }
        }else{
            dto.setOrgId(dto.getLoginOrgId().toString());
        }
        PageMethod.startPage(dto.getPageNo(), dto.getPageSize());
        List<ListOrganizationVO> voList = organizationDao.getCommercialOrgList(dto);
        for (ListOrganizationVO vo : voList) {
            vo.setPhoneNumber(Sm4.decrypt(vo.getPhoneNumber()));
        }
        PageInfo<ListOrganizationVO> pageInfo = new PageInfo<>(voList);
        return new PageResult<>(pageInfo);
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
        /*Integer userCount = userDao.getUserCount(path);
        if (userCount > NumberConst.ONE) {
            throw new BusinessException(ResultCode.ACCOUNT_HAS_USER);
        }*/

        // 获取是否有小区
        /*Integer communityCount = departmentDao.getCommunityByPath(path);
        if (communityCount > NumberConst.ZERO) {
            throw new BusinessException(ResultCode.ACCOUNT_HAS_COMMUNITY);
        }*/

        OrganizationEntity toDTO = null;
        try {
            Organization organization = organizationRepository
                    .find(new OrganizationId(Long.valueOf(orgId)));
            toDTO = organization2EntityAssembler.toDTO(organization);
            organizationRepository.remove(organization);

            List<Long> userList = userDao.getUserId(orgId);
            userDao.deleteUserOrgByOrgId(orgId);
            if (!CollectionUtils.isEmpty(userList)) {
                userRepository.removeList(
                        userList.stream().map(UserId::new).collect(Collectors.toList()));
                userRepository.removeUserRelate(userList);
            }

            DepartmentEntity communityIdByPath = departmentDao.getCommunityIdByPath(path);
            if (!ObjectUtil.isEmpty(communityIdByPath)) {
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
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            OrganizationEntity finalDTO = toDTO;
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    systemEventSendAdapter.sendOrganizationDeleteActionEvent(finalDTO);
                }
            });
        } else {
            systemEventSendAdapter.sendOrganizationDeleteActionEvent(toDTO);
        }
        //发送MQ消息
        streamMessageSender.sendGenericMessage(
                MsgBindingNameConstants.SYSTEM_PRODUCER,
                SimpleMsg.builder()
                        .delayTimeLevel(2)
                        .operateTag(MsgTagConstants.ORGANIZATION_DELETE)
                        .data(toDTO)
                        .dataId(toDTO.getId())
                        .info("删除企业")
                        .build());
        return 1;
    }

    @Override
    public void resetUserPassword(ResetUserPasswordReqDTO resetUserPasswordReqDTO) {
        String userId = String.valueOf(resetUserPasswordReqDTO.getId());
        // 当前登陆者的path
        String userPath = deptTreePathUtils
                .getDeptTreePath(resetUserPasswordReqDTO.getSessionOrgId(),
                        resetUserPasswordReqDTO.getSessionUserId());
        // 重置目标用户的path
        String reqPath = departmentUtil.getUserDept(userId).getPath();
        // 越权校验
        if (!reqPath.startsWith(userPath)) {
            throw new BusinessException(ResultCode.RESET_PASSWORD_ERROR);
        }
        UserBo userBo = new UserBo();
        userBo.setId(Long.valueOf(userId));
        redisUtil.del(CacheConst.USER_CACHE_KEY_PREFIX + userId);
        log.info("【账户详情下用户重置密码】，redis，key：{}", CacheConst.USER_CACHE_KEY_PREFIX + userId);
        UserCsvDto userCsvDto = userService.resetPasswordWithOutSms(userBo);
        // 文件流输出
        createCsvFile(userCsvDto.getUsername(), userCsvDto.getPhoneNumber(), userCsvDto.getPassword());
    }

    /**
     * 启停用
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(UpdateStatusParam updateStatusParam, String sessionUserId,
                             String sessionOrgId) {
        Long pkOrgId = updateStatusParam.getId();
        // 获取企业账户信息
        Organization find = organizationRepository.find(new OrganizationId(pkOrgId));
        if (find == null) {
            throw new BusinessException(ResultCode.ORG_IS_NOT_EXIST);
        }

        //设置日志操作对象内容
        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                 .operationName(EntityConstants.ENABLED.equals(updateStatusParam.getStatus())?"启用企业":"禁用企业")
                .name("企业")
                .targetDatas(Lists.newArrayList(new OptTargetData(find.getCompanyName(), find.getId().getValue().toString())))
                .build());

        Organization organization = Organization.builder()
                .id(new OrganizationId(pkOrgId))
                .status(cn.cuiot.dmp.system.domain.types.enums.OrgStatusEnum
                        .valueOf(updateStatusParam.getStatus().intValue()))
                .updatedOn(LocalDateTime.now())
                .updatedBy(sessionUserId)
                .createdByType(OperateByTypeEnum.valueOf(UserSourceTypeEnum.PORTAL.getCode()))
                .build();
        //更改账户信息
        organizationRepository.save(organization);
        // 如果禁用，则清除登陆信息，踢下线
        if (OrgStatusEnum.DISABLE.getCode().equals(organization.getStatus().getValue())) {
            offlineByOrgId(pkOrgId);
        } else if (!find.getStatus().getValue().equals(organization.getStatus().getValue())) {
            removeOfflineCommand(pkOrgId);
        }

        //发送事件
        OrganizationEntity toDTO = organization2EntityAssembler.toDTO(organization);
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    if (OrgStatusEnum.DISABLE.getCode().equals(organization.getStatus().getValue())) {
                        systemEventSendAdapter.sendOrganizationDisableActionEvent(toDTO);
                    } else {
                        systemEventSendAdapter.sendOrganizationEnableActionEvent(toDTO);
                    }
                }
            });
        } else {
            if (OrgStatusEnum.DISABLE.getCode().equals(organization.getStatus().getValue())) {
                systemEventSendAdapter.sendOrganizationDisableActionEvent(toDTO);
            } else {
                systemEventSendAdapter.sendOrganizationEnableActionEvent(toDTO);
            }
        }
        //发送MQ消息
        streamMessageSender.sendGenericMessage(
                MsgBindingNameConstants.SYSTEM_PRODUCER,
                SimpleMsg.builder()
                        .delayTimeLevel(2)
                        .operateTag(MsgTagConstants.ORGANIZATION_UPDATE)
                        .data(toDTO)
                        .dataId(toDTO.getId())
                        .info("修改企业")
                        .build());
    }

    /**
     * 记录变更日志
     */
    @Override
    public void recordOrganizationChange(OrganizationActionEvent event) {
        Long pkOrgId = event.getId();
        String changeType = event.getAction();
        String changeName = EventActionEnum.parseDesc(event.getAction()) + "企业";

        Date changeDate = Objects.nonNull(event.getUpdatedOn())
                ? DateTimeUtil.localDateTimeToDate(event.getUpdatedOn())
                : (Objects.nonNull(event.getCreatedOn())
                ? DateTimeUtil.localDateTimeToDate(event.getCreatedOn())
                : new Date());

        String changeUserId = StringUtils.isNotBlank(event.getUpdatedBy()) ? event.getUpdatedBy()
                : event.getCreatedBy();

        String changeUsername = null;
        String changePerson = null;

        Organization organization = organizationRepository.find(new OrganizationId(pkOrgId));

        GetOrganizationVO vo = organization2GetOrganizationVoAssembler.toDTO(organization);

        User userById = userRepository.find(new UserId(organization.getOrgOwner().getValue()));
        Optional.ofNullable(userById).ifPresent(e -> {
            vo.setAdminName(e.getName());
            vo.setUsername(e.getUsername());
            vo.setPhoneNumber(e.getDecryptedPhoneNumber());
        });

        if (StringUtils.isNotBlank(changeUserId)) {
            User changeUser = userRepository.find(new UserId(changeUserId));
            if (Objects.nonNull(changeUser)) {
                changeUsername = changeUser.getUsername();
                changePerson = changeUser.getName();
            }
        }
        /**
         * 设置所属组织信息
         */
        String grantDeptId = userDao.getUserGrantDeptId(pkOrgId);
        if (grantDeptId != null) {
            DepartmentEntity departmentEntity = departmentDao
                    .selectByPrimary(Long.parseLong(grantDeptId));
            vo.setDeptId(Long.parseLong(grantDeptId));
            vo.setDeptName(departmentEntity.getDepartmentName());
        }
        //设置组织类型
        OrgTypeDto orgTypeDto = organizationDao.getOrgType(vo.getOrgTypeId());
        vo.setOrgTypeName(orgTypeDto.getName());

        //设置已勾选配置的菜单权限
        vo.setMenuList(orgMenuDao.getMenuListByOrgId(pkOrgId.toString()));

        OrganizationChangeDto changeDto = new OrganizationChangeDto();
        changeDto.setId(SnowflakeIdWorkerUtil.nextId());
        changeDto.setPkOrgId(pkOrgId);
        changeDto.setChangeType(changeType);
        changeDto.setChangeName(changeName);
        changeDto.setChangeDate(changeDate);
        changeDto.setChangeUserId(changeUserId);
        changeDto.setChangeUsername(changeUsername);
        changeDto.setChangePerson(changePerson);
        changeDto.setChangeData(JSONObject.toJSONString(vo));

        organizationDao.saveOrganizationChange(changeDto);
    }

    /**
     * 查询企业变更记录
     */
    @Override
    public List<OrganizationChangeDto> selectOrganizationChangeByOrgId(String pkOrgId,
                                                                       String sessionUserId, String sessionOrgId) {
        //数据权限判断
        String loginDeptId = userService.getDeptId(sessionUserId, sessionOrgId);
        String grantDeptId = userDao.getUserGrantDeptId(Long.parseLong(pkOrgId));
        if (!departmentUtil.checkPrivilege(loginDeptId, grantDeptId)) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }
        return organizationDao.selectOrganizationChangeByOrgId(pkOrgId);
    }

    /**
     * 获得变更详情内容
     */
    @Override
    public OrganizationChangeDto getOrganizationChangeById(Long id, String sessionUserId,
                                                           String sessionOrgId) {
        OrganizationChangeDto organizationChangeDto = organizationDao
                .getOrganizationChangeById(id);
        if (Objects.nonNull(organizationChangeDto)) {
            //数据权限判断
            String loginDeptId = userService.getDeptId(sessionUserId, sessionOrgId);
            String grantDeptId = userDao.getUserGrantDeptId(organizationChangeDto.getPkOrgId());
            if (!departmentUtil.checkPrivilege(loginDeptId, grantDeptId)) {
                throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
            }
            if (StringUtils.isNotBlank(organizationChangeDto.getChangeData())) {
                organizationChangeDto.setChangeDataObj(JsonUtil.readValue(organizationChangeDto.getChangeData(), GetOrganizationVO.class));
            }
        }
        return organizationChangeDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateInitFlag(Long companyId) {
        AssertUtil.notNull(companyId, "企业id不能为空");
        contentApiFeignService.initModule(companyId);
        // 初始化表单配置
        formConfigTypeRepository.queryByCompany(companyId, EntityConstants.ENABLED);
        // 初始化审核配置
        auditConfigService.queryByCompany(companyId);
        return organizationRepository.updateInitFlag(companyId, EntityConstants.ENABLED);
    }

}
