package cn.cuiot.dmp.content.service;//	模板

import cn.cuiot.dmp.content.dal.entity.ImgTextType;

import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/3 10:29
 */
public interface ImgTextTypeService {
    /**
     * 查询图文类型列表
     *
     * @param orgId
     * @return
     */
    List<ImgTextType> queryForList(String orgId);

    /**
     * 根据名称查询
     *
     * @param name
     * @return
     */
    ImgTextType getByName(String name);

    /**
     * 创建图文类型
     *
     * @param imgTextType
     * @return
     */
    Boolean create(ImgTextType imgTextType);

    /**
     * 更新图文类型
     *
     * @param imgTextType
     * @return
     */
    Boolean update(ImgTextType imgTextType);

    /**
     * 删除图文类型
     *
     * @param id
     * @return
     */
    Boolean remove(Long id);

    /**
     * 获取所有数量
     */
    Long getAllCount();
}
