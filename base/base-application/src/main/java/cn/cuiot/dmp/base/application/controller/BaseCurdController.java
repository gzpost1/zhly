package cn.cuiot.dmp.base.application.controller;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.enums.BeanValidationGroup;
import cn.cuiot.dmp.base.application.service.BaseService;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.bean.PageQuery;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.utils.BeanMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @Description
 * @Author Mujun~
 * @Date 2020-09-27 10:28
 */

@RestController
@Slf4j
public abstract class BaseCurdController<S extends BaseService<T>, T, P extends PageQuery> extends BaseController {

    @Autowired
    protected S service;

    /**
     * 分页查询
     *
     * @param params
     * @return
     */
    @RequiresPermissions
    @PostMapping("/queryForPage")
    public PageResult<T> queryForPage(
            @RequestBody P params) {
        return service.page(params);
    }

    /**
     * 详细信息查询
     *
     * @param idParam
     * @return
     */
    @RequiresPermissions
    @PostMapping("/queryForDetail")
    public T queryForDetail(
            @RequestBody @Valid IdParam idParam) {
        T t = (T) service.getById(idParam.getId());
        return t;
    }

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    @RequiresPermissions
    @PostMapping("/create")
    public boolean create(
            @RequestBody @Valid T entity) {
        return service.save(entity);
    }

    /**
     * 单条删除
     *
     * @param params
     * @return
     */
    @RequiresPermissions
    @PostMapping("/delete")
    public boolean delete(
            @RequestBody @Valid IdParam params) {
        return service.removeById(params.getId());
    }

    /**
     * 单个或批量删除
     *
     * @return
     */
    @RequiresPermissions
    @PostMapping("/batchDelete")
    public boolean batchDelete(
            @RequestBody @Valid IdsParam ids) {
        List<Long> idArrays = ids.getIds();
        return service.removeByIds(idArrays);
    }

    /**
     * 更新信息
     *
     * @param entity
     * @return
     */
    @RequiresPermissions
    @PostMapping("/update")
    public boolean update(
            @RequestBody @Validated(BeanValidationGroup.Update.class) T entity) {
        return service.updateById(entity);
    }

    /**
     * 更新状态
     *
     * @param params
     * @return
     */
    @RequiresPermissions
    @PostMapping("/updateStatus")
    public boolean updateStatus(
            @RequestBody @Valid UpdateStatusParam params) {
        T entity = BeanMapper.copyBean(params, getTClass());
        return service.updateById(entity);
    }

    public Class<T> getTClass() {
        return (Class<T>)
                ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    /**
     * 列表
     *
     * @param params
     * @return
     */
    @PostMapping("/list")
    public List<T> list(
            @RequestBody T params) {
        return service.list(params);
    }
}
