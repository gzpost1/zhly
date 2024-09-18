package cn.cuiot.dmp.externalapi.service.constant;

import java.util.Arrays;
import java.util.List;

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
     * 更新识别主体
     */
    public static final String  UPDATE_SUBJECT_URL= "http://wo-api.uni-ubi.com/v2/admit/update";

    /**
     * 删除识别主体
     */
    public static final String  DELETE_SUBJECT_URL= "http://wo-api.uni-ubi.com/v2/admit/delete";


    /**
     * 注册
     */
    public static final String  CREATE_SUBJECT_REGISTER= "http://wo-api.uni-ubi.com/v2/face/register";


    /**
     * 删除人像
     */
    public static final String  DELETE_SUBJECT_REGISTER= "http://wo-api.uni-ubi.com/v2/face/delete";

    /**
     * 分页查询设备信息
     */
    public static final String QUERY_DEVICE_PAGE_V2 = "http://wo-api.uni-ubi.com/v2/device/page";

    /**
     * 设备操作指令下发（重启/重置）
     */
    public static final String DEVICE_COMMAND_V2 = "http://wo-api.uni-ubi.com/v2/device/command";

    /**
     * 授权设备
     */
    public static final List<String> AUTH_DEVICE_NOS = Arrays.asList("84E0F42E6D3D527B","84E0F42E628A527B","84E0F42E6D3B527B");

    /**
     * 授权管理
     */
    public static final String AUTH_MANAGEMENT_URL="http://wo-api.uni-ubi.com/v2/auth/device";
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

    /**
     * 大华返回失败
     */
    public static final String RESULT_ERROR_DH="0";

    /**
     * 批量插入集合大小
     */
    public static final Integer MAX_BATCH_SIZE = 100;

    /**
     * 楼盘为空
     */
    public static final String COMMUNITY_TYPE ="0";
}
