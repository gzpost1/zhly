package cn.cuiot.dmp.lease.dto.contract;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import cn.cuiot.dmp.common.bean.PageQuery;;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * 退订或作废信息
 *
 * @author MJ~
 * @since 2024-06-17
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TbContractCancelParam extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 合同名称
     */
    private String name;

    /**
     * 退订日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate date;

    /**
     * 退订原因
     */
    private String reason;

    /**
     * 退订说明
     */
    private String remark;

    /**
     * 退订附件
     */
    private String path;

    /**
     * 0.退订 1.作废
     */
    private Integer type;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

    private Byte status;

    @TableLogic
    @Builder.Default
    private Byte deleted = 0;


}
