package cn.cuiot.dmp.externalapi.service.vendor.watermeter.constant;

/**
 * 物联网水表（山东科德）静态常量
 *
 * @date 2024/8/22 10:17
 * @author gxp
 */
public class WaterMeterConstant {

    /**
     * 获取上报数据
     */
    public static final String QUERY_REPORT_DATA_LIST = "/static/kddz/kd_WatermeterCollectionrecord/list";

    /**
     * 下发阀控指令
     */
    public static final String CREATE_SUBJECT_URL= "/static/kddz/kd_WatermeterCollectionrecord/valveControls";

    /**
     * 上报数据 返回成功 状态码
     */
    public static final Integer WATER_METER_REPORT_DATA_RESULT_CODE_SUCCESS = 0;

    /**
     * 下发阀控指令 返回成功 状态码
     */
    public static final String WATER_METER_COMMAND_CONTROL_RESULT_CODE_SUCCESS = "0";

    /**
     * 头部key字段
     */
    public static final String WATER_METER_HEADER_KEY_FIELD = "key";

    /**
     * url标识?
     */
    public static final String URL_LABEL = "?";

    /**
     * url参数分隔符
     */
    public static final String URL_PARAMS_SEPARATE_LABEL = "&";

    /**
     * url参数赋值符
     */
    public static final String URL_PARAMS_EQUAL_LABEL = "=";

}
