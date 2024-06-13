package cn.cuiot.dmp.content.controller.app;//	模板

import cn.cuiot.dmp.base.application.annotation.ResolveExtData;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.content.dal.entity.ImgTextType;
import cn.cuiot.dmp.content.service.ImgTextTypeService;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * app - 图文类型
 *
 * @author hantingyao
 * @Description
 * @data 2024/6/12 15:10
 */
@RestController
@RequestMapping("/app/imgTextType")
@ResolveExtData
@Slf4j
public class AppImgTextTypeController {

    @Autowired
    private ImgTextTypeService imgTextTypeService;

    /**
     * 查询图文类型列表
     *
     * @return
     */
    @PostMapping("/queryForList")
    public List<ImgTextType> queryForList() {
        return imgTextTypeService.queryForList(LoginInfoHolder.getCurrentOrgId().toString());
    }
}
