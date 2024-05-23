package cn.cuiot.dmp.archive.api.controller;

import cn.cuiot.dmp.archive.application.param.dto.ArchivesApiQueryDTO;
import cn.cuiot.dmp.archive.application.param.vo.BaseArchivesVO;
import cn.cuiot.dmp.archive.application.param.vo.BuildingArchivesVO;
import cn.cuiot.dmp.archive.application.service.ArchivesApiService;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 档案中心-对接系统配置二维码档案
 *
 * @author caorui
 * @date 2024/5/23
 */
@RestController
@RequestMapping("/archives")
public class ArchivesApiController {

    @Autowired
    private ArchivesApiService archivesApiService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public BaseArchivesVO<Object> queryForDetail(@RequestBody @Valid ArchivesApiQueryDTO queryDTO) {
        return archivesApiService.queryForDetail(queryDTO);
    }

}
