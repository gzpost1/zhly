package cn.cuiot.dmp.externalapi.service.sync.hik;

import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import java.util.List;

/**
 * 回调处理接口
 * @author: wuyongchong
 * @date: 2024/10/11 10:16
 */
public interface HaikangPlatfromInfoCallable {

    void process(List<PlatfromInfoRespDTO> platfromInfoList);

}
