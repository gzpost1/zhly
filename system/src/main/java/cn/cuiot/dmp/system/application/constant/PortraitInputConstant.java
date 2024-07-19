package cn.cuiot.dmp.system.application.constant;

/**
 * @author pengjian
 * @create 2024/7/18 18:42
 */
public class PortraitInputConstant {

    /**
     * 大华
     */
    public static final String PLATFORM_TYPE_DH="platform_type_dh";

    /**
     * 鉴权接口
     */
    public static final String PLATFORM_AUTH = "http://wo-api.uni-ubi.com/v1/{projectGuid}/auth";

    /**
     * 识别主体创建
     */
    public static final String  CREATE_SUBJECT_URL= "http://wo-api.uni-ubi.com/v2/admit/create";

    /**
     * 注册
     */
    public static final String  CREATE_SUBJECT_REGISTER= "http://wo-api.uni-ubi.com/v2/face/register";
    public static final String APP_KEY_DH ="appKey";
    /**
     * 时间戳
     */
    public static final String TIMESTAMP_DH ="timestamp";

    /**
     * 签名
     */
    public static final String SIGN_DH ="sign";

     //       token
   public static final String   CREATE_SUBJECT_TOKEN="token"  ;
       //projectGuid
    public static final String   CREATE_SUBJECT_GUID="projectGuid"  ;

    /**
     * 返回成功
     */
    public static final String RESULT_DH="1";

}
