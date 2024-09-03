package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.system.application.param.dto.ListCustomFieldsDto;
import cn.cuiot.dmp.system.application.param.dto.ListCustomFieldsQueryDto;
import cn.cuiot.dmp.system.application.param.vo.ListCustomFieldsVO;
import cn.cuiot.dmp.system.application.service.ListCustomFieldsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 列表自定义字段
 *
 * @Author: zc
 * @Date: 2024-09-03
 */
@RestController
@RequestMapping("/listCustomFields")
public class ListCustomFieldsController {

    @Autowired
    private ListCustomFieldsService listCustomFieldsService;

    /**
     * 更新
     */
    @PostMapping("saveOrUpdate")
    public IdmResDTO<?> saveOrUpdate(@RequestBody @Valid ListCustomFieldsDto dto) {
        listCustomFieldsService.saveOrUpdate(dto);
        return IdmResDTO.success();
    }

    /**
     * 根据标识查询数据
     */
    @PostMapping("queryByIdentification")
    public IdmResDTO<ListCustomFieldsVO> queryByIdentification(@RequestBody @Valid ListCustomFieldsQueryDto dto) {
        return IdmResDTO.success(listCustomFieldsService.queryByIdentification(dto));
    }
}
