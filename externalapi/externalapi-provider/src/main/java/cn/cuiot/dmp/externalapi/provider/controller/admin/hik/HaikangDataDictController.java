package cn.cuiot.dmp.externalapi.provider.controller.admin.hik;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangDataDictEntity;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangDataDictQuery;
import cn.cuiot.dmp.externalapi.service.service.hik.HaikangDataDictService;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端-海康数据字典管理
 *
 * @author: wuyongchong
 * @date: 2024/10/10 11:54
 */
@Slf4j
@RestController
@RequestMapping("/hik/data-dict")
public class HaikangDataDictController {

    @Autowired
    private HaikangDataDictService haikangDataDictService;

    /**
     * 字典查询
     */
    @PostMapping("/selectListByQuery")
    public IdmResDTO<List<HaikangDataDictEntity>> selectListByQuery(
            @RequestBody @Valid HaikangDataDictQuery query) {
        List<HaikangDataDictEntity> list = haikangDataDictService.selectListByQuery(
                query);
        return IdmResDTO.success(list);
    }

}
