package cn.cuiot.dmp.system.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author caorui
 * @date 2024/5/14
 */
@Data
@TableName(value = "tb_dict_area", autoResultMap = true)
public class DictAreaEntity {

    /**
     * 区域编码，主键
     */
    @TableId("code")
    private String code;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 省级编码
     */
    private String provinceCode;

    /**
     * 省级名称
     */
    private String provinceName;

    /**
     * 市级编码
     */
    private String cityCode;

    /**
     * 市级名称
     */
    private String cityName;

    /**
     * 区/县编码
     */
    private String countyCode;

    /**
     * 区/县名称
     */
    private String countyName;

    /**
     * 乡/镇编码
     */
    private String townCode;

    /**
     * 乡/镇名称
     */
    private String townName;

    /**
     * 村编码
     */
    private String villageCode;

    /**
     * 村名称
     */
    private String villageName;

    /**
     * 城乡分类代码
     */
    private String type;

    /**
     * 上级/父级区域编码
     */
    private String parentCode;

    private static final long serialVersionUID = 1L;

    /**
     * 地址级别：0：省 1：城市 2：县
     */
    private Integer level;

    /**
     * 地址
     */
    private String address;

    /**
     * 带分隔地址（逗号）
     */
    private String addressComma;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

}
