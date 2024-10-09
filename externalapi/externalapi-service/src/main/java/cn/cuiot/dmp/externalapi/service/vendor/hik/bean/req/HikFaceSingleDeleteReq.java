package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 修改人脸
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class HikFaceSingleDeleteReq {

    /**
     * 人脸Id，[添加人脸]@[软件产品-综合安防管理平台-API列表-资源目录-人员信息接口-人员及照片接口#修改人脸]接口返回报文中的faceId字段，
     * 或[获取人员列表v2]@[软件产品-综合安防管理平台-API列表-资源目录-人员信息接口-人员及照片接口#获取人员列表v2]接口返回报文中的personPhotoIndexCode字段
     */
    private String faceId;
}
