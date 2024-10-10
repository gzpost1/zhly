package cn.cuiot.dmp.externalapi.service.entity.hik;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 门禁点信息
 * </p>
 *
 * @author wuyongchong
 * @since 2024-10-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_haikang_acs_door")
public class HaikangAcsDoorEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;


    /**
     * 资源唯一编码
     */
    private String indexCode;


    /**
     * 资源类型
     */
    private String resourceType;


    /**
     * 资源名称
     */
    private String name;


    /**
     * 门禁点编号
     */
    private String doorNo;


    /**
     * 通道号
     */
    private String channelNo;


    /**
     * 父级资源编号
     */
    private String parentIndexCode;


    /**
     * 一级控制器id
     */
    private String controlOneId;


    /**
     * 二级控制器id
     */
    private String controlTwoId;


    /**
     * 读卡器1
     */
    private String readerInId;


    /**
     * 读卡器2
     */
    private String readerOutId;


    /**
     * 门序号
     */
    private String doorSerial;


    /**
     * 接入协议
     */
    private String treatyType;


    /**
     * 所属区域
     */
    private String regionIndexCode;


    /**
     * 所属区域目录,以@符号分割，包含本节点
     */
    private String regionPath;


    /**
     * 描述
     */
    private String description;


    /**
     * 通道类型，door：门禁点
     */
    private String channelType;


    /**
     * 区域名称
     */
    private String regionName;


    /**
     * 所属区域目录名，以"/"分隔
     */
    private String regionPathName;


    /**
     * 安装位置
     */
    private String installLocation;


    /**
     * 状态小于0则代表资源已被删除
     */
    private Byte status;


    /**
     * 设门状态，0 初始状态，1 开门状态，2关门状态，3离线状态,小于0则代表资源已被删除
     */
    private Byte doorState;


    /**
     * 权限状态 0无 1有
     */
    private Byte authState;


    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


    /**
     * 数据落地时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dataTime;


}
