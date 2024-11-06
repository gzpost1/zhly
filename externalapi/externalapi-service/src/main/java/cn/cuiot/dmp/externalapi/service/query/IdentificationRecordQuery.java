package cn.cuiot.dmp.externalapi.service.query;

import cn.cuiot.dmp.common.bean.PageQuery;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 *
 * @author wuyongchong
 * @since 2024-11-06
 */
@Getter
@Setter
public class IdentificationRecordQuery extends PageQuery {

    /**
     * 所属楼盘
     */
    private List<Long> communityIds;


    /**
    * 识别模式,1:人像识别, 2:刷卡识别 ,3:人卡合一 4,人证比对 5:按钮开门 6：远程开门 7:密码识别 8：人+密码开门 9:口罩测温检测 10:二维码识别 11: 刷身份证 12:指纹识别
    */
    private Byte recMode;



    /**
    * 识别开始时间
    */
    private Date startTime;

    /**
     * 识别结束时间
     */
    private Date endTime;


    /**
    * 设备名称
    */
    private String deviceName;


    /**
    * 识别主体姓名
    */
    private String admitName;

    /**
     * 识别卡号
     */
    private String cardNo;


    /**
     * 查询类型
     */
    private String queryType;


}
