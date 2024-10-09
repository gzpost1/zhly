package cn.cuiot.dmp.externalapi.service.vo.gw;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 格物门禁-通行记录 vo
 *
 * @Author: zc
 * @Date: 2024-09-10
 */
@Data
public class GwEntranceGuardAccessRecordVO {

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 所属组织
     */
    private String deptPathName;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 楼盘id
     */
    private Long buildingId;

    /**
     * 楼盘名称
     */
    private String buildingName;

    /**
     * 人员项目
     */
    private String personName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 人员分组
     */
    private Long personGroupId;

    /**
     * 人员分组名称
     */
    private String personGroupName;

    /**
     * 图片
     */
    private String image;

    /**
     * 门禁id
     */
    private String entranceGuardId;

    /**
     * 门禁名称
     */
    private String entranceGuardName;

    /**
     * sn
     */
    private String sn;

    /**
     * 识别模式（认证类型）0-刷人脸,1-刷IC卡,2-刷身份证号，3-密码，4-二维码，5-人证比对,6-远程开门
     */
    private Integer scanTpye;

    /**
     * 进出门方向 0出，1进
     */
    private Integer inOut;

    /**
     * 是否开门 0不开，1开
     */
    private Integer isOpenDoor;

    /**
     * 识别时间戳
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date snapTime;
}
