package cn.cuiot.dmp.system.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author caorui
 * @date 2024/5/14
 */
@Data
@TableName(value = "tb_sys_area", autoResultMap = true)
public class SysAreaEntity {

    /**
     * 区域编码，主键
     */
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

}
