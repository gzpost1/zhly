package cn.cuiot.dmp.base.infrastructure.xxljob;

/**
 * XxlJobConstant
 * @author: wuyongchong
 * @date: 2024/1/5 17:17
 */
public class XxlJobConstant {
    /**
     * xxlJob路径相关
     */
    //xxl_job登录路径
    public static final String XXL_JOB_LOGIN_URL = "/login";

    //xxl_job任务存储路径
    public static final String XXL_JOB_ADD_JOB_INFO_URL = "/jobinfo/add";
    //xxl_job任务开启路径
    public static final String XXL_JOB_START_JOB_INFO_URL = "/jobinfo/start";
    //xxl_job任务关闭路径
    public static final String XXL_JOB_STOP_JOB_INFO_URL = "/jobinfo/stop";
    //xxl_job任务删除路径
    public static final String XXL_JOB_REMOVE_JOB_INFO_URL = "/jobinfo/remove";
    //xxl_job任务修改路径
    public static final String XXL_JOB_UPDATE_JOB_INFO_URL = "/jobinfo/update";

    //xxl_job任务执行路径
    public static final String XXL_JOB_TRIGGER_JOB_INFO_URL = "/jobinfo/trigger";

    /**
     * redis键名
     */
    //cookie
    public static final String XXL_JOB_REDIS_COOKIE_KEY_NAME = "xxlJobCookie";
}
