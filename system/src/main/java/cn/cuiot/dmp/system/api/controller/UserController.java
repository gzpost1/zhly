package cn.cuiot.dmp.system.api.controller;

import static cn.cuiot.dmp.common.constant.ResultCode.OLD_PASSWORD_IS_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.PASSWORDS_NOT_EQUALS;
import static cn.cuiot.dmp.common.constant.ResultCode.PASSWORD_IS_INVALID;
import static cn.cuiot.dmp.common.constant.ResultCode.USER_ACCOUNT_NOT_EXIST;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.application.utils.CommonCsvUtil;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.RegexConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.common.utils.Sm4;
import cn.cuiot.dmp.common.utils.ValidateUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.service.DepartmentService;
import cn.cuiot.dmp.system.application.service.UserService;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.entity.UserDataEntity;
import cn.cuiot.dmp.system.infrastructure.entity.bo.UserBo;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ChangeUserStatusDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.DeleteUserDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ExportUserCmd;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetUserDepartmentTreeLazyReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ImportUserDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.InsertUserDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.MoveUserDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SmsCodeCheckReqDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UpdatePasswordDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UpdateUserDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserCsvDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserDataResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.vo.UserExportVo;
import cn.cuiot.dmp.system.infrastructure.entity.vo.UserImportDownloadVo;
import cn.cuiot.dmp.system.infrastructure.utils.ExcelUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.houbb.sensitive.core.api.SensitiveUtil;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户管理
 *
 * @author guoying
 * @className LogControllerImpl
 * @description 用户管理
 * @date 2020-10-23 10:00:07
 */
@RestController
@Slf4j
public class UserController extends BaseController {

    /**
     * 最大分页数
     */
    private static final int MAX_PAGE_SIZE = 2000;

    /**
     * 不限制的特殊值
     */
    private static final String UNLIMIT_VAL = "-1";

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping(value = "/user/getDGroup", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getDGroup() {
        return userService.getDGroup(getOrgId(), getUserId());
    }

    /**
     * 用户列表筛选-分页
     */
    @GetMapping(value = "/user/listUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResult<UserDataResDTO> getPage(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "deptId", required = false) Long deptId,
            @RequestParam(value = "endDeptId", required = false) Long endDeptId,
            @RequestParam(value = "deptIds", required = false) String deptIds,
            @RequestParam(value = "status", required = false) Byte status,
            @RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {

        if (pageSize > MAX_PAGE_SIZE) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT);
        }

        Map<String, Object> params = new HashMap<>(2);
        // 获取session中的orgId
        String sessionOrgId = getOrgId();
        if (StringUtils.isEmpty(sessionOrgId)) {
            throw new BusinessException(ResultCode.ORG_ID_NOT_EXIST);
        }
        params.put("orgId", sessionOrgId);
        params.put("loginUserId", getUserId());

        // 组织为空则使用当前登陆用户的组织
        if (deptId == null&&StringUtils.isEmpty(deptIds)) {
            deptId = Optional.ofNullable(userService.getDeptId(getUserId(), sessionOrgId))
                    .map(Long::valueOf).orElse(null);
        }
        // 获取子组织
        if (deptId != null) {
            //deptId参数为-1则不过滤当前登录部门的数据权限
            if(!UNLIMIT_VAL.equals(deptId.toString())){
                DepartmentEntity departmentEntity = departmentService
                        .getDeptById(String.valueOf(deptId));
                if (departmentEntity != null) {
                    params.put("path", departmentEntity.getPath());
                }
            }
        }

        if(Objects.nonNull(endDeptId)){
            DepartmentEntity endDepartment = departmentService
                    .getDeptById(String.valueOf(endDeptId));
            if (endDepartment != null) {
                List<String> pathList = getPathList(endDepartment.getPath());
                params.put("pathList",pathList);
            }
        }

        if (!StringUtils.isEmpty(deptIds)) {
            params.put("deptIds", Splitter.on(",").splitToList(deptIds));
        }

        if (Objects.nonNull(status)) {
            params.put("status", status);
        }
        if (!StringUtils.isEmpty(name)) {
            params.put("name", name);
        }
        if (!StringUtils.isEmpty(username)) {
            params.put("userName", username);
        }
        if (!StringUtils.isEmpty(userName)) {
            if(Objects.isNull(params.get("userName"))){
                params.put("userName", userName);
            }
        }
        if (!StringUtils.isEmpty(phoneNumber)) {
            String decrypt = Sm4.encryption(phoneNumber);
            params.put("phone", decrypt);
        }
        return userService.getPage(params, sessionOrgId, pageNo, pageSize);
    }

    /**
     * 获得直线路径列表
     * @param path
     * @return
     */
    private List<String> getPathList(String path){
        List<String> strings = Splitter.on("-").splitToList(path);
        List<String> resultList = Lists.newArrayList();
        String tmpPath="";
        for(int i=0;i<4;i++){
            if(i==0){
                tmpPath=strings.get(i);
            }else{
                tmpPath=tmpPath+"-"+strings.get(i);
            }
            resultList.add(tmpPath);
        }
        return resultList;
    }

    /**
     * 查询用户详情
     */
    @GetMapping(value = "/user/getUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDataResDTO getDetail(@RequestParam(value = "id") String id) {
        // 获取session中的orgId
        String sessionOrgId = LoginInfoHolder.getCurrentOrgId().toString();
        String sessionUserId = LoginInfoHolder.getCurrentUserId().toString();
        if (StringUtils.isEmpty(sessionOrgId)) {
            throw new BusinessException(ResultCode.ORG_ID_NOT_EXIST);
        }
        //对敏感信息脱敏返回
        return SensitiveUtil.desCopy(userService.getOne(id, sessionOrgId, sessionUserId));
    }

    /**
     * 新增用户
     */
    @RequiresPermissions
    @LogRecord(operationCode = "insertUserD", operationName = "新增用户", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping(value = "/user/insertUserD", produces = MediaType.APPLICATION_JSON_VALUE)
    public void insertUser(@RequestBody @Valid InsertUserDTO dto) {
        String loginOrgId = LoginInfoHolder.getCurrentOrgId().toString();
        String loginUserId = LoginInfoHolder.getCurrentUserId().toString();

        UserBo userBo = new UserBo();
        userBo.setOrgId(loginOrgId);
        userBo.setLoginUserId(loginUserId);
        userBo.setRoleId(dto.getRoleId());
        userBo.setUsername(dto.getUsername());
        userBo.setName(dto.getName());
        userBo.setPhoneNumber(dto.getPhoneNumber());
        userBo.setDeptId(dto.getDeptId());
        userBo.setPostId(dto.getPostId());
        userBo.setRemark(dto.getRemark());

        UserCsvDto userInfo = userService.insertUser(userBo);

        // 文件流输出
        List<JSONObject> jsonList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("用户名",userInfo.getUsername());
        jsonObject.put("手机号",userInfo.getPhoneNumber());
        jsonObject.put("密码",userInfo.getPassword());
        jsonList.add(jsonObject);

        List<Object> head = new ArrayList<>();
        head.add("用户名");
        head.add("手机号");
        head.add("密码");

        response.setContentType("text/csv;charset=\"UTF-8\"");
        response.setHeader("Content-Disposition", "attachment; filename=credentials.csv");
        String[] split = userBo.getUsername().split("@");
        String username = split[0];
        try {
            CommonCsvUtil.createCsvFile(head, jsonList, username + "credentials", response);
        } catch (UnsupportedEncodingException e) {
            log.error("insertUser error.", e);
        }
    }

    /**
     * 修改用户
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateUser", operationName = "修改用户", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping(value = "/user/updateUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO updateUser(@RequestBody @Valid UpdateUserDTO dto) {

        UserBo userBo = new UserBo();
        userBo.setId(dto.getId());
        userBo.setOrgId(LoginInfoHolder.getCurrentOrgId().toString());
        userBo.setLoginUserId(LoginInfoHolder.getCurrentUserId().toString());
        userBo.setRoleId(dto.getRoleId());
        userBo.setUsername(dto.getUsername());
        userBo.setName(dto.getName());
        userBo.setPhoneNumber(dto.getPhoneNumber());
        userBo.setDeptId(dto.getDeptId());
        userBo.setPostId(dto.getPostId());
        userBo.setRemark(dto.getRemark());

        return userService.updateUser(userBo);
    }


    /**
     * 批量移动用户
     */
    @RequiresPermissions
    @LogRecord(operationCode = "moveUsers", operationName = "移动用户", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping(value = "/user/moveUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO moveUsers(@RequestBody @Valid MoveUserDTO dto) {

        UserBo userBo = new UserBo();
        userBo.setOrgId(LoginInfoHolder.getCurrentOrgId().toString());
        userBo.setLoginUserId(LoginInfoHolder.getCurrentUserId().toString());
        userBo.setDeptId(dto.getDeptId());
        userBo.setIds(dto.getIds());

        userService.moveUsers(userBo);

        return IdmResDTO.success();
    }

    /**
     * 批量启停用
     */
    @RequiresPermissions
    @LogRecord(operationCode = "changeUserStatus", operationName = "批量启停用用户", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping(value = "/user/changeUserStatus", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO changeUserStatus(@RequestBody @Valid ChangeUserStatusDTO dto) {

        UserBo userBo = new UserBo();
        userBo.setOrgId(LoginInfoHolder.getCurrentOrgId().toString());
        userBo.setLoginUserId(LoginInfoHolder.getCurrentUserId().toString());
        userBo.setStatus(dto.getStatus());
        userBo.setIds(dto.getIds());

        userService.changeUserStatus(userBo);

        return IdmResDTO.success();
    }

    /**
     * 批量删除用户
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteUsers", operationName = "删除用户", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping(value = "/user/deleteUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> deleteUsers(@RequestBody @Valid DeleteUserDTO dto) {
        // 获取session中的userId
        Long sessionUserId = LoginInfoHolder.getCurrentUserId();
        if (Objects.isNull(sessionUserId)) {
            throw new BusinessException(ResultCode.USER_ID_NOT_EXIST, "获取当前登录信息失败");
        }
        // 获取session中的orgId
        String sessionOrgId = getOrgId();
        if (StringUtils.isEmpty(sessionOrgId)) {
            throw new BusinessException(ResultCode.ORG_ID_NOT_EXIST, "获取当前登录信息失败");
        }
        UserBo userBo = new UserBo();
        userBo.setOrgId(sessionOrgId);
        userBo.setLoginUserId(sessionUserId.toString());
        userBo.setIds(dto.getIds());
        Map<String, Object> resultMap = new HashMap<>(1);
        resultMap.put("succeedCount", this.userService.deleteUsers(userBo));
        return resultMap;
    }


    /**
     * 导出用户
     */
    @RequiresPermissions
    @LogRecord(operationCode = "exportUsers", operationName = "导出用户", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping(value = "/user/exportUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public void exportUsers(@RequestBody @Valid ExportUserCmd dto) throws IOException {
        UserBo userBo = new UserBo();
        userBo.setOrgId(LoginInfoHolder.getCurrentOrgId().toString());
        userBo.setLoginUserId(LoginInfoHolder.getCurrentUserId().toString());
        userBo.setDeptId(dto.getDeptId());
        List<UserExportVo> dataList = userService.exportUsers(userBo);

        List<Map<String, Object>> sheetsList = new ArrayList<>();

        Map<String, Object> sheet1 = ExcelUtils
                .createSheet("用户列表", dataList, UserExportVo.class);

        sheetsList.add(sheet1);

        Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.XSSF);

        ExcelUtils.downLoadExcel(
                "user-" + DateTimeUtil.dateToString(new Date(), "yyyyMMddHHmmss"),
                response,
                workbook);
    }

    /**
     * 导入用户
     */
    @RequiresPermissions
    @LogRecord(operationCode = "importUsers", operationName = "导入用户", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping(value = "/user/importUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public void importUsers(@RequestParam("file") MultipartFile file,
            @RequestParam(value = "deptId", required = true) String deptId) throws Exception {

        AssertUtil.isFalse((null == file || file.isEmpty()), "上传文件为空");

        ImportParams params = new ImportParams();
        params.setHeadRows(1);

        List<ImportUserDto> importDtoList = ExcelImportUtil
                .importExcel(file.getInputStream(), ImportUserDto.class, params);

        if (org.apache.commons.collections.CollectionUtils.isEmpty(importDtoList)) {
            throw new BusinessException(ResultCode.REQUEST_FORMAT_ERROR, "excel解析失败");
        }
        for (ImportUserDto userDto : importDtoList) {
            if (org.apache.commons.lang3.StringUtils.isBlank(userDto.getUsername())) {
                throw new BusinessException(ResultCode.PARAM_NOT_NULL, "导入失败，用户名不能为空");
            }
            if (org.apache.commons.lang3.StringUtils.isBlank(userDto.getName())) {
                throw new BusinessException(ResultCode.PARAM_NOT_NULL, "导入失败，姓名不能为空");
            }
            if (org.apache.commons.lang3.StringUtils.isBlank(userDto.getPhoneNumber())) {
                throw new BusinessException(ResultCode.PARAM_NOT_NULL, "导入失败，手机号不能为空");
            }
            if (org.apache.commons.lang3.StringUtils.isBlank(userDto.getRoleName())) {
                throw new BusinessException(ResultCode.PARAM_NOT_NULL, "导入失败，角色不能为空");
            }
        }
        //判断用户名是否重复
        if (importDtoList.size() != importDtoList.stream().map(ite -> ite.getUsername()).distinct()
                .collect(Collectors.toList()).size()) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "导入失败，文件中存在用户名重复");
        }
        //判断手机号是否重复
        if (importDtoList.size() != importDtoList.stream().map(ite -> ite.getPhoneNumber())
                .distinct().collect(Collectors.toList()).size()) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "导入失败，文件中存在手机号重复");
        }

        UserBo userBo = new UserBo();
        userBo.setOrgId(LoginInfoHolder.getCurrentOrgId().toString());
        userBo.setLoginUserId(LoginInfoHolder.getCurrentUserId().toString());
        userBo.setDeptId(deptId);
        userBo.setImportDtoList(importDtoList);

        List<UserImportDownloadVo> dataList = userService.importUsers(userBo);

        /*List<Map<String, Object>> sheetsList = new ArrayList<>();

        Map<String, Object> sheet1 = ExcelUtils
                .createSheet("用户", dataList, UserImportDownloadVo.class);

        sheetsList.add(sheet1);

        Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.XSSF);

        ExcelUtils.downLoadExcel(
                "user-credentials-" + DateTimeUtil.dateToString(new Date(), "yyyyMMddHHmmss"),
                response,
                workbook);*/

        // 文件流输出
        List<JSONObject> jsonList = new ArrayList<>();
        for(UserImportDownloadVo downloadVo:dataList){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("用户名",downloadVo.getUsername());
            jsonObject.put("手机号",downloadVo.getPhoneNumber());
            jsonObject.put("密码",downloadVo.getPassword());
            jsonList.add(jsonObject);
        }

        List<Object> head = new ArrayList<>();
        head.add("用户名");
        head.add("手机号");
        head.add("密码");

        response.setContentType("text/csv;charset=\"UTF-8\"");
        response.setHeader("Content-Disposition", "attachment; filename=credentials.csv");
        try {
            CommonCsvUtil.createCsvFile(head, jsonList, "credentials", response);
        } catch (UnsupportedEncodingException e) {
            log.error("insertUser error.", e);
        }

    }


    /**
     * 用户管理组织树（懒加载）
     */
    @GetMapping(value = "/user/getUserDepartmentTreeLazy", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GetDepartmentTreeLazyResDto> getUserDepartmentTreeLazy(
            @Valid GetUserDepartmentTreeLazyReqDto dto) {
        dto.setLoginUserId(getUserId());
        dto.setLoginOrgId(getOrgId());
        return userService.getUserDepartmentTreeLazy(dto);
    }

    /**
     * 查询登录用户信息
     */
    @PostMapping(value = "/getLoginUserInfo", produces = "application/json;charset=UTF-8")
    public UserResDTO queryUserById() {
        // 获取session中的userId
        String sessionUserId = LoginInfoHolder.getCurrentUserId().toString();
        String sessionOrgId = LoginInfoHolder.getCurrentOrgId().toString();
        // 判断是否具有查询权限
        UserResDTO userResDTO = userService
                .getUserMenuByUserIdAndOrgId(sessionUserId, sessionOrgId);
        if (userResDTO == null) {
            // 账号不存在
            throw new BusinessException(USER_ACCOUNT_NOT_EXIST);
        }
        //对加密的手机号码解密--2020/12/08
        //增加手机号为空判断，在不为空的情况下才做解密处理 --2020/12/15
        if (null != userResDTO.getPhoneNumber()) {
            userResDTO.setPhoneNumber(Sm4.decrypt(userResDTO.getPhoneNumber()));
        }
        //安全考虑去除密码返回
        userResDTO.setPassword(null);
        return SensitiveUtil.desCopy(userResDTO);
    }

    /**
     * 修改密码(登录人自行修改)
     */
    @LogRecord(operationCode = "updatePassword", operationName = "修改密码", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping(value = "/user/updatePassword", produces = MediaType.APPLICATION_JSON_VALUE)
    public void updatePassword(@RequestBody UpdatePasswordDto dto) {
        // 获取密码
        ValidateUtil.validate(dto);
        final String password = dto.getPassword();
        final String newPassword = dto.getNewPassword();
        final String newPasswordAgain = dto.getNewPasswordAgain();
        // 密码参数校验
        if (!newPassword.equals(newPasswordAgain)) {
            throw new BusinessException(PASSWORDS_NOT_EQUALS);
        }
        // 判断密码不符合规则
        if (!newPassword.matches(RegexConst.PASSWORD_REGEX) || ValidateUtil.checkRepeat(password)
                || ValidateUtil.checkBoardContinuousChar(password)) {
            throw new BusinessException(PASSWORD_IS_INVALID);
        }
        Long userId = LoginInfoHolder.getCurrentUserId();
        UserResDTO loginUser = userService.getUserById(userId.toString());
        String oldPassword = Sm4.decrypt(loginUser.getPassword());
        if (!password.equals(oldPassword)) {
            throw new BusinessException(OLD_PASSWORD_IS_ERROR);
        }
        UserDataEntity userDataEntity = new UserDataEntity();
        userDataEntity.setPassword(Sm4.encryption(newPassword));
        userDataEntity.setId(userId);
        // 更改密码
        userService.updatePassword(userDataEntity);
    }

    /**
     * 修改手机号(登录人自行修改)
     */
    @LogRecord(operationCode = "updatePhoneNumber", operationName = "修改手机号", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping(value = "/user/updatePhoneNumber", produces = MediaType.APPLICATION_JSON_VALUE)
    public void updatePhoneNumber(@RequestBody SmsCodeCheckReqDTO dto) {
        String phoneNumber = Optional.ofNullable(dto).map(d -> d.getPhoneNumber()).orElse(null);
        if (StringUtils.isEmpty(phoneNumber)) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL);
        }
        // 手机号参数校验
        if (!phoneNumber.matches(RegexConst.PHONE_NUMBER_REGEX)) {
            throw new BusinessException(ResultCode.PHONE_NUMBER_IS_INVALID);
        }
        // 获取短信验证码
        String smsCode = Optional.ofNullable(dto).map(d -> d.getSmsCode()).orElse(null);
        if (StringUtils.isEmpty(smsCode)) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL);
        }
        // 获取session中的userId
        Long userId = LoginInfoHolder.getCurrentUserId();
        if (Objects.isNull(userId)) {
            throw new BusinessException(ResultCode.USER_ID_NOT_EXIST);
        }

        UserBo userBo = new UserBo();
        userBo.setId(userId);
        userBo.setSmsCode(smsCode);
        userBo.setPhoneNumber(phoneNumber);
        userBo.setOrgId(getOrgId());
        // 更改手机号
        userService.updatePhoneNumber(userBo);
    }


    /**
     * 下载模板
     */
    @PostMapping("/user/downloadExcel")
    public void downloadExcel(HttpServletResponse response)
            throws IOException {
        BufferedOutputStream bos = null;
        String templatePath = "template/importUsers.xlsx";
        try (InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(templatePath)) {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder
                    .encode("用户导入模板.xls", "UTF-8"));
            bos = new BufferedOutputStream(response.getOutputStream());
            FileCopyUtils.copy(is, bos);
        } catch (Exception ex) {
            log.error("download extractTemplate error", ex);
            throw new BusinessException(ResultCode.INNER_ERROR, "下载失败");
        } finally {
            if (null != bos) {
                bos.flush();
                bos.close();
            }
        }
    }

}
