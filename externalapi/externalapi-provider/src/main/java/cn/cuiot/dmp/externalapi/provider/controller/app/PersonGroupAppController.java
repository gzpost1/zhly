package cn.cuiot.dmp.externalapi.provider.controller.app;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.entity.PersonGroupEntity;
import cn.cuiot.dmp.externalapi.service.query.PersonGroupPageQuery;
import cn.cuiot.dmp.externalapi.service.service.PersonGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * app-人员分组
 *
 * @Author: zc
 * @Date: 2024-09-09
 */
@RestController
@RequestMapping("/app/personGroup")
public class PersonGroupAppController {

    @Autowired
    private PersonGroupService personGroupService;

    /**
     * 分页查询
     */
    @PostMapping("/queryForList")
    public IdmResDTO<List<PersonGroupEntity>> queryForList(@RequestBody PersonGroupPageQuery query) {
        return IdmResDTO.success(personGroupService.queryForList(query));
    }
}
