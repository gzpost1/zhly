package cn.cuiot.dmp.base.application.aop;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.application.dto.ResponseWrapper;
import cn.cuiot.dmp.base.application.service.OperateLogService;
import cn.cuiot.dmp.base.application.utils.FileExportUtils;
import cn.cuiot.dmp.base.application.utils.IpUtil;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.enums.LogLevelEnum;
import cn.cuiot.dmp.common.enums.StatusCodeEnum;
import cn.cuiot.dmp.common.log.dto.OperateLogDto;
import cn.cuiot.dmp.common.log.intf.ResourceParam;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.RequestFacade;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
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
public class LogRecordAspect extends BaseController {

    @Autowired
    OperateLogService operateLogService;

    @Pointcut("@annotation(cn.cuiot.dmp.base.application.annotation.LogRecord)")
    public void logRecord() {
    }

    @Around("logRecord()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 请求信息
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();

        String header = "token";
        if (StringUtils.isBlank(request.getHeader(header))) {
            // openApi请求
            return joinPoint.proceed();
        }

        // 方法执行前
        OperateLogDto operateLogDto = this.beforeProceed(request, joinPoint);

        // 执行方法
        Object obj = this.proceed(joinPoint, operateLogDto);

        // 方法执行后
        this.afterProceed(joinPoint, operateLogDto, obj);

        return obj;
    }

    /**
     * 方法执行前处理
     */
    private OperateLogDto beforeProceed(HttpServletRequest request, ProceedingJoinPoint joinPoint)
            throws Throwable {
        //填充数据
        OperateLogDto operateLogDto = new OperateLogDto();
        operateLogDto.setOrgId(getOrgId());
        operateLogDto.setOperationById(getUserId());
        operateLogDto.setOperationByName(getUserName());
        operateLogDto.setRequestTime(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        operateLogDto.setRequestIp(IpUtil.getIpAddr(request));

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
            operateLogDto.setOperationCode(logRecord.operationCode());
            operateLogDto.setOperationName(logRecord.operationName());
            operateLogDto.setServiceType(logRecord.serviceType());
        }

        return operateLogDto;
    }

    /**
     * 执行方法
     */
    private Object proceed(ProceedingJoinPoint joinPoint, OperateLogDto operateLogDto)
            throws Throwable {
        //方法执行异常处理
        Object obj = null;
        try {
            obj = joinPoint.proceed();
        } catch (Exception e) {
            log.error("LogRecordAspect joinPoint.proceed error", e);

            // 获取操作对象
            String operationTarget = this.getOperationTarget(joinPoint);
            if (StringUtils.isNotBlank(operationTarget)) {
                operateLogDto.setOperationTarget(operationTarget);
            }

            String operationTargetInfo = getOperationTargetInfo(joinPoint);
            if (StringUtils.isNotBlank(operationTargetInfo)) {
                operateLogDto.setOperationTargetInfo(operationTargetInfo);
            }

            operateLogDto.setLogLevel(LogLevelEnum.ERROR.getCode());
            operateLogDto.setStatusCode(StatusCodeEnum.FAILED.getCode());
            operateLogDto.setStatusMsg(StatusCodeEnum.FAILED.getName());
            //记录日志
            // 发生异常时写日志
            operateLogService.saveDb(operateLogDto);

            throw e;
        }

        return obj;
    }

    /**
     * 获取操作对象
     */
    private String getOperationTarget(ProceedingJoinPoint joinPoint) throws Throwable {
        String operationTarget = null;

        try {
            Object[] args = joinPoint.getArgs();
            Method method = ResourceParam.class.getDeclaredMethod("getOperationTarget");
            if (null != args && null != method) {
                Object result = method.invoke(args[0]);
                if (null != result) {
                    String[] operationTargetArray = (String[]) result;
                    if (ArrayUtils.isNotEmpty(operationTargetArray)) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < operationTargetArray.length; i++) {
                            sb.append(operationTargetArray[i]).append(",");
                        }
                        sb.deleteCharAt(sb.length() - 1);
                        operationTarget = sb.toString();
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取操作对象错误，{}", e.getMessage());
        }

        return operationTarget;
    }

    /**
     * 获取操作对象信息
     */
    private String getOperationTargetInfo(ProceedingJoinPoint joinPoint) throws Throwable {
        String operationTargetInfo = null;

        try {
            Object[] args = joinPoint.getArgs();
            Method method = ResourceParam.class.getDeclaredMethod("getOperationTargetInfo");
            if (null != args && null != method) {
                Object result = method.invoke(args[0]);
                if (null != result) {
                    operationTargetInfo = (String) result;
                }
            }
        } catch (Exception e) {
            log.error("获取操作对象信息错误，{}", e.getMessage());
        }

        return operationTargetInfo;
    }

    /**
     * 保存文件存到日志
     *
     * @param operateLogDto 日志
     * @throws IOException 异常
     */
    private void saveFileInfo(OperateLogDto operateLogDto) throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Object fileFlag = request.getAttribute(FileExportUtils.EXPORT_FLAG);
        if (fileFlag == null) {
            return;
        }

        HttpServletResponse response = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
        if (response == null || !FileExportUtils.isFileContentType(response)) {
            return;
        }

        ResponseWrapper wrapper = (ResponseWrapper) response;
        byte[] bodyBytes = wrapper.getBodyBytes();

        String fileName = FileExportUtils.getFileName(response);
        operateLogDto.setResponseParams(fileName);

        operateLogDto.setFile(bodyBytes);
    }

    /**
     * 执行方法后
     */
    private void afterProceed(ProceedingJoinPoint joinPoint, OperateLogDto operateLogDto,
            Object obj) throws Throwable {
        //方法执行后
        try {
            // 获取操作对象
            String operationTarget = this.getOperationTarget(joinPoint);
            if (StringUtils.isNotBlank(operationTarget)) {
                operateLogDto.setOperationTarget(operationTarget);
            }

            String operationTargetInfo = getOperationTargetInfo(joinPoint);
            if (StringUtils.isNotBlank(operationTargetInfo)) {
                operateLogDto.setOperationTargetInfo(operationTargetInfo);
            }
            if (obj instanceof IdmResDTO) {
                IdmResDTO respDto = (IdmResDTO) obj;
                operateLogDto.setResponseParams(respDto.toString());
                operateLogDto.setStatusCode(respDto.getCode());
                operateLogDto.setStatusMsg(respDto.getMessage());
            } else {
                operateLogDto.setResponseParams(obj == null ? "" : obj.toString());
                operateLogDto.setStatusCode(StatusCodeEnum.SUCCESS.getCode());
                operateLogDto.setStatusMsg(StatusCodeEnum.SUCCESS.getName());
                saveFileInfo(operateLogDto);
            }
            operateLogDto.setLogLevel(LogLevelEnum.INFO.getCode());

            // 记录日志
            // 正常处理时写日志
            operateLogService.saveDb(operateLogDto);

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
     * 获取request body中的参数
     *
     * @param request 请求
     */
    public String getParameterByRequest(HttpServletRequest request) throws IOException {
        List<String> lines = null;
        try {
            lines = IOUtils.readLines(request.getInputStream());
            log.info("获取request body中的参数: " + JSON.toJSONString(lines));
        } catch (IOException e) {
            log.info("获取request body中的参数错误：" + e.getMessage());
            throw e;
        }

        StringBuilder sb = new StringBuilder();
        if (!CollectionUtils.isEmpty(lines)) {
            lines.forEach(sb::append);
        }

        return sb.toString();
    }

}
