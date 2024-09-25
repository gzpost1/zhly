package cn.cuiot.dmp.system.infrastructure.entity.bean;

import cn.cuiot.dmp.common.constant.Constant;
import cn.cuiot.dmp.system.infrastructure.entity.vo.UniUbiEntranceGuardQueryVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * 门禁（宇泛）设备请求参数
 *
 * @date 2024/8/21 14:13
 * @author gxp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UniUbiDeviceQueryReq implements Serializable {
    private static final long serialVersionUID = -8527541751522467489L;

    /**
     * 页码
     */
    private Integer index;

    /**
     * 每页个数(上限100)
     */
    private Integer length;

    /**
     * 设备序列号
     */
    private String deviceNo;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备标签
     */
    private String tag;

    /**
     * 设备应用版本号
     */
    private String versionNo;

    /**
     * 设备状态,1:未绑定2:已绑定3:已禁用
     */
    private String state;

    /**
     * 查询时间区间开始(UTC时间/毫秒值)，如2018-08-08T08:18:28+0000
     */
    private String startTime;

    /**
     * 查询时间区间结束(UTC时间/毫秒值)，如2018-08-08T08:18:28+0000
     */
    private String endTime;

    /**
     * 设备来源（见附录）
     */
    private String source;


    public Integer getLength() {
        if(Objects.isNull(this.length) || this.length < 1){
            return 10;
        }
        if(this.length > Constant.UNI_UBI_PAGE_LENGTH_MAX){
            return 100;
        }
        return length;
    }

    public UniUbiDeviceQueryReq(UniUbiEntranceGuardQueryVO vo) {
        this.index = vo.getPageNo();
        this.length = vo.getPageSize();
        this.name = vo.getName();
    }

    public UniUbiDeviceQueryReq(Integer index , Integer length){
        this.index = index;
        this.length = length;
    }
}
