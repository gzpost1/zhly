package cn.cuiot.dmp.externalapi.service.entity.park;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author pengjian
 * @since 2024-07-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_platfrom_info")
public class PlatfromInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private Long id;



    /**
     * 平台id
     */
    private String platformId;

    /**
     * 公司id
     */
    private Long companyId;

    /**
     * 对接系统参数json数据
     */
    private String data;
}
