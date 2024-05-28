package cn.cuiot.dmp.archive.application.param.query;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liujianyu
 * @description 设备档案查询条件
 * @since 2024-05-15 10:39
 */
@Data
public class DeviceArchivesQuery extends PageQuery {

    /**
     * 所属楼盘id
     */
    @NotNull(message = "请选择所属楼盘")
    private Long loupanId;

    /**
     * id
     */
    private Long id;

    /**
     * 设备名称（支持输入汉字、英文、符号、数字，长度支持30字符）
     */
    private String deviceName;

    /**
     * 安装位置（支持输入汉字、英文、符号、数字，长度支持30字符）
     */
    private String installationLocation;

    /**
     * 设备状态（具体选项）
     */
    private Long deviceStatus;

    /**
     * 设备类别（下拉选择自定义配置中数据）
     */
    private Long deviceCategory;
}
