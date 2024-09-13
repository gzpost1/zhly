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
import cn.cuiot.dmp.system.application.service.ClientUserService;
import cn.cuiot.dmp.system.application.service.DepartmentService;
import cn.cuiot.dmp.system.application.service.UserService;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.entity.UserDataEntity;
import cn.cuiot.dmp.system.infrastructure.entity.bo.UserBo;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ChangeUserStatusDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ClientUserQuery;
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
import cn.cuiot.dmp.system.infrastructure.entity.vo.ClientUserVo;
import cn.cuiot.dmp.system.infrastructure.entity.vo.UserExportVo;
import cn.cuiot.dmp.system.infrastructure.entity.vo.UserImportDownloadVo;
import cn.cuiot.dmp.system.infrastructure.utils.ExcelUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.houbb.sensitive.core.api.SensitiveUtil;
import com.google.common.base.Splitter;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * C端用户管理
 * @author wuyongchong
 * @date 2024-06-14
 */
@Slf4j
@RestController
@RequestMapping("/client-user")
public class ClientUserController{

    @Autowired
    private ClientUserService clientUserService;

    /**
     * 分页查询
     */
    @RequiresPermissions
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<ClientUserVo>> queryForPage(@RequestBody ClientUserQuery query) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        query.setCompanyId(currentOrgId);
        IPage<ClientUserVo> pageData = clientUserService.queryForPage(query);
        return IdmResDTO.success(pageData);
    }

}
