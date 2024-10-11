package cn.cuiot.dmp.externalapi.service.entity.hik;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 海康门禁点事件
 *
 * @Author: zc
 * @Date: 2024-10-11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tb_haikang_acs_door_events")
public class HikAcsDoorEventsEntity extends YjBaseEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 事件ID，唯一标识这个事件
     */
    private String eventId;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 事件产生时间
     */
    private LocalDateTime eventTime;

    /**
     * 人员唯一编码
     */
    private String personId;

    /**
     * 卡号
     */
    private String cardNo;

    /**
     * 人员姓名
     */
    private String personName;

    /**
     * 人员所属组织编码
     */
    private String orgIndexCode;

    /**
     * 人员所属组织名称
     */
    private String orgName;

    /**
     * 门禁点名称
     */
    private String doorName;

    /**
     * 门禁点编码
     */
    private String doorIndexCode;

    /**
     * 门禁点所在区域编码
     */
    private String doorRegionIndexCode;

    /**
     * 抓拍图片地址
     */
    private String picUri;

    /**
     * 是否存在抓拍图片（0否；1是）
     */
    private Byte isExistPicUri;

    /**
     * 图片存储服务的唯一标识
     */
    private String svrIndexCode;

    /**
     * 事件类型
     */
    private Integer eventType;

    /**
     * 进出类型
     */
    private Integer inAndOutType;

    /**
     * 读卡器唯一标识
     */
    private String readerDevIndexCode;

    /**
     * 读卡器名称
     */
    private String readerDevName;

    /**
     * 控制器设备唯一标识
     */
    private String devIndexCode;

    /**
     * 控制器设备名称
     */
    private String devName;

    /**
     * 身份证图片地址
     */
    private String identityCardUri;

    /**
     * 事件入库时间
     */
    private LocalDateTime receiveTime;

    /**
     * 工号
     */
    private String jobNo;

    /**
     * 学号
     */
    private String studentId;

    /**
     * 证件号码
     */
    private String certNo;

    /**
     * 事件图片
     */
    private String picture;
}
