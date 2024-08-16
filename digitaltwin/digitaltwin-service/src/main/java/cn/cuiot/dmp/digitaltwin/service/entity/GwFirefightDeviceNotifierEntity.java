package cn.cuiot.dmp.digitaltwin.service.entity;

import cn.cuiot.dmp.digitaltwin.service.entity.dto.GwFirefightDeviceNotifierDto;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * 格物消防-设备信息（联系人）
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
@Data
@TableName("tb_gw_firefight_device_notifier")
public class GwFirefightDeviceNotifierEntity {
    /**
     * 父级id(对应设备id)
     */
    private Long parentId;

    /**
     * 联系人姓名
     */
    private String name;

    /**
     * 联系方式
     */
    private String mobile;

    /**
     * 优先级
     */
    private String priority;

    public static GwFirefightDeviceNotifierEntity dtoToEntity(GwFirefightDeviceNotifierDto dto, Long parentId) {
        GwFirefightDeviceNotifierEntity notifierEntity = new GwFirefightDeviceNotifierEntity();
        BeanUtils.copyProperties(dto, notifierEntity);
        notifierEntity.setParentId(parentId);
        return notifierEntity;
    }
}
