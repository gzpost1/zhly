package cn.cuiot.dmp.system.infrastructure.entity.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gengyu
 * @deprecated 修改主菜单
 * @Date 2020/9/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMainMenuDto {
    /**
     * 菜单自增ID
     */
    private Long id;

    /**
     * 菜单类型
     */
    private int menuType;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 排序
     */
    private int sort;
    /**
     *
     */
    private String menuUrl;
    /**
     * 显示状态
     */
    private int hidden;
    /**
     * 修改时间
     */
    private LocalDateTime updateOn;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改类型
     */
    private int updateByType;

}
