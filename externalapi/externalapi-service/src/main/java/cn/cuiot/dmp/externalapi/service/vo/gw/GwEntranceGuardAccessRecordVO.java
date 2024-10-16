package cn.cuiot.dmp.externalapi.service.vo.gw;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @Excel(name = "所属组织", orderNum = "8", width = 20)
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
    @Excel(name = "所属楼盘", orderNum = "9", width = 20)
    private String buildingName;

    /**
     * 人员项目
     */
    @Excel(name = "人员姓名", orderNum = "0", width = 20)
    private String personName;

    /**
     * 手机号
     */
    @Excel(name = "手机号", orderNum = "1", width = 20)
    private String phone;

    /**
     * 人员分组
     */
    private Long personGroupId;

    /**
     * 人员分组名称
     */
    @Excel(name = "人员分组", orderNum = "2", width = 20)
    private String personGroupName;

    /**
     * 图片
     */
    @Excel(name = "抓拍照片", orderNum = "3", width = 20)
    private String image;

    /**
     * 门禁id
     */
    private String entranceGuardId;

    /**
     * 门禁名称
     */
    @Excel(name = "门禁名称", orderNum = "4", width = 20)
    private String entranceGuardName;

    /**
     * sn
     */
    @Excel(name = "门禁SN", orderNum = "5", width = 20)
    private String sn;

    /**
     * 识别模式（认证类型）0-刷人脸,1-刷IC卡,2-刷身份证号，3-密码，4-二维码，5-人证比对,6-远程开门
     */
    private Integer scanTpye;

    /**
     * 识别模式
     */
    @Excel(name = "识别模式", width = 30, orderNum = "7", replace = {"刷人脸_0", "刷IC卡_1", "刷身份证号_2", "密码_3","二维码_4" ,"人证比对_5","远程开门_6"})
    private String scanTypeName;

    /**
     * 进出门方向 0出，1进
     */
    private Integer inOut;

    @Excel(name = "进出类型", width = 30, orderNum = "6", replace = {"进_1", "出_0"})
    private String inOutName;

    /**
     * 是否开门 0不开，1开
     */
    private Integer isOpenDoor;

    @Excel(name = "是否通行", orderNum = "10", width = 20, replace = {"开_1", "不开_0"})
    private String isOpenName;
    /**
     * 识别时间戳
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "识别时间",orderNum = "11",  width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date snapTime;
}
