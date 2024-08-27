
package cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap;

import lombok.Data;

/**
 * 云智眼-查询设备云端录像信息resp
 *
 * @Author: zc
 * @Date: 2024-08-16
 */
@Data
public class VsuapRecordListResp {
    /**
     * 录像文件Id
     */
    private Long recordId;
    /**
     * 录像文件下载地址
     */
    private String recordFileUrl;
    /**
     * 录像封面图下载地址
     */
    private String picFileUrl;
    /**
     * 录制文件格式
     * M3U8:  M3U8格式文件
     */
    private String format;
    /**
     * 录制文件开始时间, 13位时间戳
     */
    private long startTime;
    /**
     * 录制文件结束时间, 13位时间戳
     */
    private long endTime;
    /**
     * 录制文件保存时长, 单位:天
     */
    private String storage;

}
