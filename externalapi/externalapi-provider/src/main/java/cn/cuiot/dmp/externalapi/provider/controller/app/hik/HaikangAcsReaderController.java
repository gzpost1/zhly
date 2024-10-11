package cn.cuiot.dmp.externalapi.provider.controller.app.hik;

import cn.cuiot.dmp.base.application.annotation.ResolveExtData;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangAcsReaderQuery;
import cn.cuiot.dmp.externalapi.service.service.hik.HaikangAcsReaderService;
import cn.cuiot.dmp.externalapi.service.vo.hik.HaikangAcsReaderVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * APP端-海康门禁读卡器
 *
 * @author: wuyongchong
 * @date: 2024/10/10 15:56
 */
@Slf4j
@RestController
@RequestMapping("/app/hik/acs-reader")
public class HaikangAcsReaderController {

    @Autowired
    private HaikangAcsReaderService haikangAcsReaderService;

    /**
     * 分页查询
     */
    @ResolveExtData
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<HaikangAcsReaderVo>> queryForPage(
            @RequestBody HaikangAcsReaderQuery query) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        query.setCompanyId(currentOrgId);
        IPage<HaikangAcsReaderVo> pageData = haikangAcsReaderService.queryForPage(query);
        return IdmResDTO.success(pageData);
    }

    /**
     * 下拉列表查询
     */
    @ResolveExtData
    @PostMapping("/listForSelect")
    public IdmResDTO<List<HaikangAcsReaderVo>> listForSelect(
            @RequestBody HaikangAcsReaderQuery query) {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        query.setCompanyId(currentOrgId);
        List<HaikangAcsReaderVo> list = haikangAcsReaderService.listForSelect(query);
        return IdmResDTO.success(list);
    }

}
