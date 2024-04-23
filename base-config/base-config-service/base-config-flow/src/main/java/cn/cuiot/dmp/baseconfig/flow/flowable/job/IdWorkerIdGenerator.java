package cn.cuiot.dmp.baseconfig.flow.flowable.job;

import cn.cuiot.dmp.base.infrastructure.utils.SpringContextHolder;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.flowable.common.engine.impl.cfg.IdGenerator;
import org.springframework.stereotype.Component;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/17 13:01
 */
@Component
public class IdWorkerIdGenerator implements IdGenerator {

  @Override
  public String getNextId() {
    return SpringContextHolder.getBean(IdWorker.class).getId()+"";
  }
}
