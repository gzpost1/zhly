package cn.cuiot.dmp.externalapi.service.query.yfwaterelectricity;

import cn.cuiot.dmp.common.bean.PageQuery;
import cn.cuiot.dmp.common.validator.ValidGroup;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 智慧物联-宇泛水表
 * </p>
 *
 * @author xiaotao
 * @since 2024-10-10
 */
@Data
@Accessors(chain = true)
public class YfWaterMeterDTO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 4713929464511435400L;

    /**
     * 水表编码
     */
    @NotNull(message = "水表编码不能为空",groups = {ValidGroup.Crud.Delete.class, ValidGroup.Crud.Update.class})
    private Long id;

    /**
     * 设备名称
     */
    @NotNull(message = "设备名称不能为空",groups = {ValidGroup.Crud.Insert.class, ValidGroup.Crud.Update.class})
    @Size(min = 1, max = 30, message = "设备名称限30字符",groups = {ValidGroup.Crud.Insert.class, ValidGroup.Crud.Update.class})
    private String name;

    /**
     * 楼盘id，主键
     */
    @NotNull(message = "楼盘不能为空",groups = {ValidGroup.Crud.Insert.class, ValidGroup.Crud.Update.class})
    private Long buildingId;

    /**
     * 设备序列号
     */
    @NotNull(message = "设备序列号不能为空",groups = {ValidGroup.Crud.Insert.class, ValidGroup.Crud.Update.class})
    @Size(min = 1, max = 30, message = "设备序列号限50字符",groups = {ValidGroup.Crud.Insert.class, ValidGroup.Crud.Update.class})
    private String deviceNo;


    /**
     * 采集器编号，水表采集器编号和设备序列号一致
     */
    @NotNull(message = "采集器编号不能为空",groups = {ValidGroup.Crud.Insert.class, ValidGroup.Crud.Update.class})
    private String ip;



    /**
     * 查询用量开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recordBeginTime;

    /**
     * 查询用量结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recordEndTime;

    /**
     * 设备id
     */
    private List<Long> meterIds;

}
