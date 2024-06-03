package cn.cuiot.dmp.content.service;//	模板

import cn.cuiot.dmp.content.dal.entity.ImgTextType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/3 10:29
 */
public interface ImgTextTypeService extends IService<ImgTextType> {
    /**
     * 查询图文类型列表

     * @param orgId
     * @return
     */
    List<ImgTextType> queryForList(String orgId);
}
