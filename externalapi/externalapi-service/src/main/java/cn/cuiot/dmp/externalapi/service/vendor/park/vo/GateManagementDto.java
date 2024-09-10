package cn.cuiot.dmp.externalapi.service.vendor.park.vo;

import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
/**
 *
 * @author pengjian
 * @since 2024-09-09
 */
@Getter
@Setter
public class GateManagementDto implements Serializable {

    private Integer nodeId;
    /**
    * 通道名称
    */
    @Length(max = 50,message = "通道名称长度必须小于50位")
    private String nodeName;

    /**
    * 使用类别 0停车场内1入口2出口3中间入口4中间出口
    */
    private Integer useType;

    /**
    * 道闸状态1常抬0正常-1状态未知
    */
    private Integer status;

    /**
    * 企业id
    */
    private Long companyId;

    /**
    * 楼盘id
    */
    private Long communityId;



}
