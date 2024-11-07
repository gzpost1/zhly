package cn.cuiot.dmp.externalapi.provider.controller.admin.park;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.base.auth.ThirdRequestNeedAuth;
import cn.cuiot.dmp.externalapi.service.entity.park.IdentificationRecordEntity;
import cn.cuiot.dmp.externalapi.service.query.IdentificationRecordQuery;
import cn.cuiot.dmp.externalapi.service.service.park.IdentificationRecordService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 宇泛事件回调
 * @author pengjian
 * @create 2024/11/6 14:35
 */
@RestController
@RequestMapping("/event/callbacks")
public class EventCallBacksController {

    @Autowired
    private IdentificationRecordService identificationRecordService;
    /**
     * 识别记录上报
     * @param params
     * @return
     */
    @RequestMapping("/identificationRecordReport")
    public IdmResDTO identificationRecordReport(@RequestParam Map<String , String> params){
        return identificationRecordService.identificationRecordReport(params);
    }

    /**
     * 分页查询
     * @param query
     * @return
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<IdentificationRecordEntity>> queryForPage(@RequestBody IdentificationRecordQuery query) {

        return IdmResDTO.success(identificationRecordService.queryForPage(query));
    }

    /**
     * 导出
     * @param query
     * @return
     */
    @PostMapping("/export")
    public IdmResDTO export(@RequestBody IdentificationRecordQuery query){
        identificationRecordService.export(query);
        return IdmResDTO.success();
    }
}
