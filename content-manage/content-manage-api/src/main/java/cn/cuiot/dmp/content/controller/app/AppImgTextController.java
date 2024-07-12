package cn.cuiot.dmp.content.controller.app;//	模板

import cn.cuiot.dmp.base.application.annotation.ResolveExtData;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.content.param.query.ContentImgTextPageQuery;
import cn.cuiot.dmp.content.param.vo.ImgTextVo;
import cn.cuiot.dmp.content.service.ContentImgTextService;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;

/**
 * app-图文
 *
 * @author hantingyao
 * @Description
 * @data 2024/6/11 10:32
 */

@RestController
@RequestMapping("/app/imgText")
@ResolveExtData
public class AppImgTextController {

    @Autowired
    private ContentImgTextService imgTextService;

    /**
     * 分页列表
     */
    @PostMapping("/queryForPage")
    public IPage<ImgTextVo> queryForPage(@RequestBody @Valid ContentImgTextPageQuery pageQuery) {
        pageQuery.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        pageQuery.setBuildings(Collections.singletonList(LoginInfoHolder.getCommunityId()));
        return imgTextService.queryForPage(pageQuery);
    }

    /**
     * 获取图文详情
     * @param idParam
     * @return
     */
    @PostMapping("/getImgTextDetail")
    public ImgTextVo getImgTextDetail(@RequestBody @Valid IdParam idParam) {
        return imgTextService.queryForDetail(idParam.getId());
    }
}
