package cn.cuiot.dmp.base.infrastructure.xxljob;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.net.HttpCookie;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * XxlJobClient
 *
 * @author: wuyongchong
 * @date: 2024/1/5 17:14
 */
@Slf4j
@Component
public class XxlJobClient {

    @Value("${xxl.job.admin.addresses:}")
    private String adminAddresses;

    @Value("${xxl.job.username:}")
    private String username;

    @Value("${xxl.job.password:}")
    private String password;

    @Value("${xxl.job.defaultJobGroup:}")
    private Integer defaultJobGroup;

    public static final long PRE_READ_MS = 5000;

    private Cache<String, String> cookieCache = CacheBuilder.newBuilder().maximumSize(1)
            .expireAfterWrite(30,
                    TimeUnit.MINUTES).build();

    public Long createXxlJob(String jobName, Byte status, String invokeTarget,
            String cronExpression, String executorParam) {
        XxlJobInfo xxlJobInfo = new XxlJobInfo();
        xxlJobInfo.setJobDesc(jobName);
        xxlJobInfo.setExecutorRouteStrategy("FIRST");
        xxlJobInfo.setJobCron(cronExpression);
        xxlJobInfo.setGlueType("BEAN");
        xxlJobInfo.setExecutorHandler(invokeTarget);
        xxlJobInfo.setExecutorBlockStrategy("SERIAL_EXECUTION");
        xxlJobInfo.setExecutorTimeout(0);
        xxlJobInfo.setExecutorFailRetryCount(0);
        xxlJobInfo.setExecutorParam(executorParam);
        xxlJobInfo.setAuthor("admin");
        xxlJobInfo.setAlarmEmail("");
        Long xxlJobId = createXxlJobInfo(xxlJobInfo);
        if (CustomJobStatus.ENABLE.equals(status)) {
            startXxlJobInfo(xxlJobId);
        }
        return xxlJobId;
    }

    public void updateXxlJob(Long xxlJobId, String jobName, String cronExpression, Byte status,
            String invokeTarget, String executorParam, String execFrequency) {
        XxlJobInfo xxlJobInfo = new XxlJobInfo();
        xxlJobInfo.setId(xxlJobId);
        xxlJobInfo.setJobDesc(jobName);
        xxlJobInfo.setExecutorRouteStrategy("FIRST");
        xxlJobInfo.setJobCron(cronExpression);
        xxlJobInfo.setGlueType("BEAN");
        xxlJobInfo.setExecutorHandler(invokeTarget);
        xxlJobInfo.setExecutorBlockStrategy("SERIAL_EXECUTION");
        xxlJobInfo.setExecutorTimeout(0);
        xxlJobInfo.setExecutorFailRetryCount(0);
        xxlJobInfo.setExecutorParam(executorParam);
        xxlJobInfo.setAuthor("admin");
        xxlJobInfo.setAlarmEmail("");
        updateXxlJobInfo(xxlJobInfo);
        if (CustomJobStatus.ENABLE.equals(status)) {
            if (this.jobIsNeedStart(execFrequency, cronExpression)) {
                startXxlJobInfo(xxlJobId);
            }
        }
        if (CustomJobStatus.DISABLE.equals(status)) {
            stopXxlJobInfo(xxlJobId);
        }
    }

    /**
     * 校验任务是否需要启动 如果是执行频率是一次，执行过的任务没有下一次启动时间，此时去调用xxljob启动任务会报错（开启XxIob定时任务失败）
     *
     * @param execFrequency 执行频率
     * @param execCron cron表达式
     * @return true-需要启动，false-不需要启动
     */
    public Boolean jobIsNeedStart(String execFrequency, String execCron) {
        if (StringUtils.isBlank(execFrequency) || StringUtils.isBlank(execCron)) {
            return true;
        }
        // 如果是执行频率是一次，判断是否已过执行时间，已过则不调用xxljob启用任务（已过执行时间启用任务会报错）
        if (Objects.equals(ExecFrequencyType.JUST_ONCE, execFrequency)) {
            try {
                Date nextValidTime = new CronExpression(execCron)
                        .getNextValidTimeAfter(new Date(System.currentTimeMillis() + PRE_READ_MS));
                if (nextValidTime == null) {
                    return false;
                }
            } catch (ParseException e) {
                log.error("获取下一次执行时间失败:{}", e.getMessage());
                throw new BusinessException(ResultCode.INNER_ERROR, e.getMessage());
            }
        }
        return true;
    }

    /**
     * 新建定时任务
     *
     * @param xxlJobInfo 任务类实体
     * @return xxlJobId
     */
    public Long createXxlJobInfo(XxlJobInfo xxlJobInfo) {
        if (Objects.isNull(xxlJobInfo.getJobGroup())) {
            xxlJobInfo.setJobGroup(defaultJobGroup);
        }
        Map paramMap = JSON.parseObject(JSON.toJSONString(xxlJobInfo), Map.class);
        String res = this
                .sendForm(adminAddresses + XxlJobConstant.XXL_JOB_ADD_JOB_INFO_URL, paramMap);
        XxlReturnT<String> xxlReturnT = JSON
                .parseObject(res, new TypeReference<XxlReturnT<String>>() {
                });
        if (XxlReturnT.SUCCESS_CODE != xxlReturnT.getCode()) {
            //请求失败
            throw new BusinessException(ResultCode.INNER_ERROR, "新建XxlJob定时任务失败");
        }
        return Long.valueOf(xxlReturnT.getContent());
    }

    /**
     * 修改定时任务
     *
     * @param xxlJobInfo 任务类实体
     */
    public void updateXxlJobInfo(XxlJobInfo xxlJobInfo) {
        if (Objects.isNull(xxlJobInfo.getJobGroup())) {
            xxlJobInfo.setJobGroup(defaultJobGroup);
        }
        Map paramMap = JSON.parseObject(JSON.toJSONString(xxlJobInfo), Map.class);
        String res = this
                .sendForm(adminAddresses + XxlJobConstant.XXL_JOB_UPDATE_JOB_INFO_URL, paramMap);
        XxlReturnT<Object> xxlReturnT = JSON
                .parseObject(res, new TypeReference<XxlReturnT<Object>>() {
                });
        if (XxlReturnT.SUCCESS_CODE != xxlReturnT.getCode()) {
            //请求失败
            throw new BusinessException(ResultCode.INNER_ERROR, "修改XxlJob定时任务失败");
        }
    }

    /**
     * 开启指定定时任务
     *
     * @param jobInfoId 任务id
     */
    public void startXxlJobInfo(Long jobInfoId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", jobInfoId);
        String res = this
                .sendForm(adminAddresses + XxlJobConstant.XXL_JOB_START_JOB_INFO_URL, paramMap);
        XxlReturnT<Object> xxlReturnT = JSON
                .parseObject(res, new TypeReference<XxlReturnT<Object>>() {
                });
        if (XxlReturnT.SUCCESS_CODE != xxlReturnT.getCode()) {
            //请求失败
            throw new BusinessException(ResultCode.INNER_ERROR, "开启XxlJob定时任务失败");
        }
    }

    /**
     * 关闭指定定时任务
     *
     * @param jobInfoId 任务id
     */
    public void stopXxlJobInfo(Long jobInfoId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", jobInfoId);
        String res = this
                .sendForm(adminAddresses + XxlJobConstant.XXL_JOB_STOP_JOB_INFO_URL, paramMap);
        XxlReturnT<Object> xxlReturnT = JSON
                .parseObject(res, new TypeReference<XxlReturnT<Object>>() {
                });
        if (XxlReturnT.SUCCESS_CODE != xxlReturnT.getCode()) {
            //请求失败
            throw new BusinessException(ResultCode.INNER_ERROR, "关闭XxlJob定时任务失败");
        }
    }


    /**
     * 执行指定定时任务
     *
     * @param jobInfoId 任务id
     */
    public void triggerXxlJobInfo(Long jobInfoId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", jobInfoId);
        String res = this
                .sendForm(adminAddresses + XxlJobConstant.XXL_JOB_TRIGGER_JOB_INFO_URL, paramMap);
        XxlReturnT<Object> xxlReturnT = JSON
                .parseObject(res, new TypeReference<XxlReturnT<Object>>() {
                });
        if (XxlReturnT.SUCCESS_CODE != xxlReturnT.getCode()) {
            //请求失败
            throw new BusinessException(ResultCode.INNER_ERROR, "执行XxlJob定时任务失败");
        }
    }

    /**
     * 删除定时任务
     *
     * @param jobInfoId 任务id
     */
    public void removeXxlJobInfo(Long jobInfoId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", jobInfoId);
        String res = this
                .sendForm(adminAddresses + XxlJobConstant.XXL_JOB_REMOVE_JOB_INFO_URL, paramMap);
        XxlReturnT<Object> xxlReturnT = JSON
                .parseObject(res, new TypeReference<XxlReturnT<Object>>() {
                });
        if (XxlReturnT.SUCCESS_CODE != xxlReturnT.getCode()) {
            //请求失败
            throw new BusinessException(ResultCode.INNER_ERROR, "删除XxlJob定时任务失败");
        }
    }

    /**
     * 获取cookie
     *
     * @return String
     */
    public String getCookie() {
        String cookieVal = cookieCache.getIfPresent(XxlJobConstant.XXL_JOB_REDIS_COOKIE_KEY_NAME);
        if (StringUtils.isNotBlank(cookieVal)) {
            return cookieVal;
        } else {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("userName", username);
            hashMap.put("password", password);
            HttpResponse response = HttpRequest
                    .post(adminAddresses + XxlJobConstant.XXL_JOB_LOGIN_URL).form(hashMap)
                    .execute();
            if (HttpStatus.HTTP_OK != response.getStatus()) {
                //请求失败
                throw new BusinessException(ResultCode.INNER_ERROR, "获取xxlJob_cookie失败");
            }
            log.info("获取xxljob登录cookie结果:  " + response.getCookieStr());
            List<HttpCookie> cookies = response.getCookies();
            StringBuilder sb = new StringBuilder();
            for (HttpCookie cookie : cookies) {
                sb.append(cookie.toString());
            }
            cookieVal = sb.toString();
            cookieCache.put(XxlJobConstant.XXL_JOB_REDIS_COOKIE_KEY_NAME, cookieVal);
            return cookieVal;
        }
    }

    /**
     * 发送请求
     *
     * @param path 路径
     * @param map 参数
     * @return String
     */
    public String sendForm(String path, Map<String, Object> map) {
        String cookie = getCookie();
        log.info("XxlJobClient==sendForm==url:{},body:{}", path, JSON.toJSONString(map));
        HttpResponse response = HttpRequest.post(path).form(map).cookie(cookie).execute();
        if (HttpStatus.HTTP_OK != response.getStatus()) {
            //请求失败
            log.error("请求xxlJob数据失败");
        }
        String res = response.body();
        log.info("XxlJobClient==sendForm==res:{}", res);
        return res;
    }
}
