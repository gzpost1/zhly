package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 添加人脸
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class HikFaceSingleAddReq {

    /**
     * 人员Id，[获取人员列表v2]@[软件产品-综合安防管理平台-API列表-资源目录-人员信息接口-人员及照片接口#获取人员列表v2]接口返回报文中的personId字段
     */
    private String personId;

    /**
     * 人脸图片base64编码后的字符数据
     */
    private String faceData;
}
