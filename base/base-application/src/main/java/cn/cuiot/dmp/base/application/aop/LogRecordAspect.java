package cn.cuiot.dmp.base.application.aop;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.constant.OperationSourceContants;
import cn.cuiot.dmp.base.application.dto.ResponseWrapper;
import cn.cuiot.dmp.base.application.rocketmq.LogSendService;
import cn.cuiot.dmp.base.application.utils.IpUtil;
import cn.cuiot.dmp.base.infrastructure.syslog.LogContextHolder;
import cn.cuiot.dmp.base.infrastructure.syslog.OptTargetData;
import cn.cuiot.dmp.base.infrastructure.syslog.OptTargetInfo;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.enums.LogLevelEnum;
import cn.cuiot.dmp.common.enums.StatusCodeEnum;
import cn.cuiot.dmp.common.log.dto.OperateLogDto;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.AuthContants;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.domain.types.enums.UserTypeEnum;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.RequestFacade;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author guoying
 * @className LogRecordAspect
 * @description
 * @date 2020-09-07 17:01:43
 */
@Slf4j
@Order(2)
@Aspect
@Component
public class LogRecordAspect {

    @Autowired
    private LogSendService sendService;

    /**
     * 小程序特殊操作路径
     */
    private final static String APP_PATH="/app/";

    @Pointcut("@annotation(cn.cuiot.dmp.base.application.annotation.LogRecord)")
    public void logRecord() {
    }

    @Around("logRecord()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 请求信息
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();

        // 方法执行前
        OperateLogDto operateLogDto = this.beforeProceed(request, joinPoint);

        // 执行方法
        Object obj = null;
        Boolean proceedSuccess = false;
        try {
            obj = joinPoint.proceed();
            proceedSuccess=true;
        } catch (Throwable e) {
            log.error("LogRecordAspect joinPoint.proceed error", e);
            operateLogDto.setLogLevel(LogLevelEnum.ERROR.getCode());
            operateLogDto.setStatusCode(StatusCodeEnum.FAILED.getCode());
            operateLogDto.setStatusMsg(e.getMessage());
            throw e;
        } finally {
            // 方法执行后
            if(proceedSuccess) {
                this.afterProceed(joinPoint, operateLogDto, obj);
            }
            LogContextHolder.remove();
        }
        return obj;
    }

    /**
     * 方法执行前处理
     */
    private OperateLogDto beforeProceed(HttpServletRequest request, ProceedingJoinPoint joinPoint) {

        OperateLogDto operateLogDto = new OperateLogDto();

        //企业ID
        operateLogDto.setOrgId(StrUtil.toStringOrNull(LoginInfoHolder.getCurrentOrgId()));
        //请求时间
        operateLogDto.setRequestTime(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        //请求IP
        operateLogDto.setRequestIp(IpUtil.getIpAddr(request));
        //操作者ID
        operateLogDto.setOperationById(StrUtil.toStringOrNull(LoginInfoHolder.getCurrentUserId()));
        //用户类型
        Integer userType = LoginInfoHolder.getCurrentUserType();
        operateLogDto.setUserType(userType);
        //操作者名称
        if (UserTypeEnum.USER.getValue().equals(userType)) {
            operateLogDto.setOperationByName(
                    StrUtil.toStringOrNull(LoginInfoHolder.getCurrentUsername()));
        } else {
            operateLogDto
                    .setOperationByName(StrUtil.toStringOrNull(LoginInfoHolder.getCurrentName()));
        }
        //操作端
        String operationSource = request.getHeader(AuthContants.OPERATION_SOURCE);
        operateLogDto.setOperationSource(operationSource);
        if(StringUtils.isBlank(operateLogDto.getOperationSource())){
            operateLogDto.setOperationSource(OperationSourceContants.WEB_END);
            if(request.getServletPath().contains(APP_PATH)||StringUtils.isNotBlank(request.getHeader(AuthContants.COMMUNITY))){
                if (UserTypeEnum.USER.getValue().equals(userType)) {
                    operateLogDto.setOperationSource(OperationSourceContants.APP_MANAGE_END);
                }else {
                    operateLogDto.setOperationSource(OperationSourceContants.APP_CLIENT_END);
                }
            }
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // 请求的参数
        if (!CollectionUtils.isEmpty(request.getParameterMap())) {
            Map<String, String> parameterMap = this.convertMap(request.getParameterMap());
            operateLogDto.setRequestParams(JSON.toJSONString(parameterMap));
        } else {
            try {
                String[] parameterNames = signature.getParameterNames();
                Object[] parameterValues = joinPoint.getArgs();
                JSONObject jsonObject = new JSONObject(parameterNames.length);
                for (int i = 0; i < parameterNames.length; i++) {
                    Object value = parameterValues[i];
                    if (value instanceof ResponseWrapper || value instanceof RequestFacade) {
                        continue;
                    }
                    jsonObject.put(parameterNames[i], value);
                }
                operateLogDto.setRequestParams(jsonObject.toJSONString());
            } catch (Exception e) {
                log.error("LogRecordAspect beforeProceed error", e);
            }
        }

        //注解信息
        LogRecord logRecord = signature.getMethod().getAnnotation(LogRecord.class);
        if (null != logRecord) {
            //操作编码
            operateLogDto.setOperationCode(logRecord.operationCode());
            //操作名称
            operateLogDto.setOperationName(logRecord.operationName());
            //业务类型
            operateLogDto.setServiceType(logRecord.serviceType());
            //业务类型名称
            operateLogDto.setServiceTypeName(logRecord.serviceTypeName());
        }

        return operateLogDto;
    }

    /**
     * 执行方法后
     */
    private void afterProceed(ProceedingJoinPoint joinPoint, OperateLogDto operateLogDto,
            Object obj) {
        //方法执行后
        try {
            if (StringUtils.isBlank(operateLogDto.getLogLevel())) {
                operateLogDto.setLogLevel(LogLevelEnum.INFO.getCode());
            }
            if (StringUtils.isBlank(operateLogDto.getStatusCode())) {
                if (obj instanceof IdmResDTO) {
                    IdmResDTO respDto = (IdmResDTO) obj;
                    operateLogDto.setStatusCode(respDto.getCode());
                    operateLogDto.setStatusMsg(respDto.getMessage());
                } else {
                    operateLogDto.setStatusCode(StatusCodeEnum.SUCCESS.getCode());
                    operateLogDto.setStatusMsg(StatusCodeEnum.SUCCESS.getName());
                }
            }
            //设置响应内容
            operateLogDto.setResponseParams(obj == null ? "" : JsonUtil.writeValueAsString(obj));
            //设置操作对象内容
            OptTargetInfo optTargetInfo = LogContextHolder.getOptTargetInfo();
            operateLogDto.setOperationTargetInfo(getOptTargetInfoStr(optTargetInfo));

            if(Objects.nonNull(optTargetInfo)){
                //针对类似登录接口手动获取orgId
                if(StringUtils.isBlank(operateLogDto.getOrgId())){
                    operateLogDto.setOrgId(StrUtil.toStringOrNull(optTargetInfo.getCompanyId()));
                }
                if(StringUtils.isNotBlank(optTargetInfo.getOperationName())){
                    operateLogDto.setOperationName(optTargetInfo.getOperationName());
                }
                if(StringUtils.isBlank(operateLogDto.getOperationById())){
                    operateLogDto.setOperationById(StrUtil.toStringOrNull(optTargetInfo.getOperationById()));
                }
                if(StringUtils.isBlank(operateLogDto.getOperationByName())){
                    operateLogDto.setOperationByName(StrUtil.toStringOrNull(optTargetInfo.getOperationByName()));
                }
                if(Objects.isNull(operateLogDto.getUserType())){
                    operateLogDto.setUserType(optTargetInfo.getUserType());
                }
            }
            // 发送记录日志消息
            sendService.sendOperaLog(operateLogDto);
        } catch (Exception e) {
            log.error("LogRecordAspect joinPoint.afterProceed error", e);
        }
    }

    /**
     * 转换request请求参数
     *
     * @param parameterMap request获取的参数数组
     */
    private Map<String, String> convertMap(Map<String, String[]> parameterMap) {
        Map<String, String> map = new HashMap<>(parameterMap.size());
        for (String key : parameterMap.keySet()) {
            map.put(key, parameterMap.get(key)[0]);
        }
        return map;
    }

    /**
     * 转换操作对象内容为字符串
     */
    private String getOptTargetInfoStr(OptTargetInfo optTargetInfo) {
        if(Objects.isNull(optTargetInfo)){
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder("");
        List<OptTargetData> targetDatas = optTargetInfo.getTargetDatas();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(targetDatas)) {
            stringBuilder.append(optTargetInfo.getName());
            stringBuilder.append("{");
            stringBuilder.append(Joiner.on(",").join(targetDatas.stream().map(item->item.getDataName()+"("+item.getDataId()+")").collect(
                    Collectors.toList())));
            stringBuilder.append("}");
        }
        return stringBuilder.toString();
    }

}
