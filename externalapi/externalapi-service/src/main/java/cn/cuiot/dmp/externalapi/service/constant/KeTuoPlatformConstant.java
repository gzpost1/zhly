package cn.cuiot.dmp.externalapi.service.constant;

/**
 * @author pengjian
 * @create 2024/9/3 11:11
 */
public class KeTuoPlatformConstant {

    //获取停车场列表
    public static String GET_PARKING_LOT_LIST = "config/platform/GetParkingLotList";

    //剩余车位查询
    public static String GET_FREE_SPACE_NUM ="api/wec/GetFreeSpaceNum";

    /**
     * 获取通道信息
     */
    public static String GET_PARK_NODE = "api/wec/GetParkingNode";

    /**
     * 服务编码
     */
    public static String PARK_LOT_SERVICE_CODE ="getParkingLotList";

    /**
     * 通道信息服务编码
     */
    public static String PARK_NODE_SERVICE_CODE ="getParkingNode";

    /**
     * 进出站记录服务编码
     */
    public static String CAR_IN_OUT_SERVICE_CODE= "getCarInoutInfo";

    /**
     * 查询进出记录
     */
    public static String CAR_IN_OUT_SERVICE_CODE_URL= "api/wec/GetCarInoutInfo";

    /**
     * 道闸控制
     */
    public static String GATE_CONTROL_CODE ="api/control/GateControl";

    /**
     * 通道状态
     */
    public static String PARK_NODE_STATUS ="api/wec/GetParkingNodeStatus";

    /**
     * 查询道闸状态服务编码
     */
    public static String PARK_NODE_STATUS_SERVICE_CODE = "getParkingNodeStatus";
    /**
     * 剩余车位查询
     */
    public static String FREE_SPACE_NUM="getFreeSpaceNum";

    /**
     *请求头 版本
     */
    public static String PARK_HEADER_VERSION="version";

    /**
     * 版本
     */
    public static String PARK_VERSION="1.0.0";

    /**
     * 头部
     */
    public static String PARK_HEADER_ACCEPT ="accept-language";

    /**
     * 语言编码
     */
    public static String PARK_ACCEPT ="zh-CN";

    /**
     * 返回成功
     */
    public static String SUCCESS_CODE = "0";

    /**
     * 每页数据
     */
    public static Integer  PARK_NODE_PAGE_INDEX = 1;
    /**
     * 页数
     */
    public static Integer PARK_NODE_PAGE_SIZE = 50;
}
