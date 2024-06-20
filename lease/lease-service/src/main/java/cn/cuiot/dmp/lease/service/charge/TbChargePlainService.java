package cn.cuiot.dmp.lease.service.charge;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.charge.ChargePlainInsertDto;
import cn.cuiot.dmp.lease.dto.charge.ChargePlainPageDto;
import cn.cuiot.dmp.lease.dto.charge.ChargePlainQuery;
import cn.cuiot.dmp.lease.dto.charge.ChargePlainUpdateDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuiot.dmp.lease.mapper.charge.TbChargePlainMapper;
import cn.cuiot.dmp.lease.entity.charge.TbChargePlain;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TbChargePlainService extends ServiceImpl<TbChargePlainMapper, TbChargePlain> {

    public IPage<ChargePlainPageDto> queryForPage(ChargePlainQuery query) {
        return baseMapper.queryForPage(new Page(query.getPageNo(), query.getPageSize()), query);
    }

    /**
     * 创建数据
     * @param createDto
     */
    @Transactional(rollbackFor = Exception.class)
    public void createData(ChargePlainInsertDto createDto) {
        TbChargePlain tbChargePlain = new TbChargePlain();
        BeanUtils.copyProperties(createDto, tbChargePlain);
        tbChargePlain.setId(IdWorker.getId());
        tbChargePlain.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        tbChargePlain.setStatus(EntityConstants.ENABLED);
        baseMapper.insert(tbChargePlain);

        //todo 填充xxljob生成任务

    }

    /**
     * 更新自动计划
     * @param updateDto
     */
    public void updateData(ChargePlainUpdateDto updateDto) {
        //1 根据ID查询并判断
        TbChargePlain tbChargePlain = baseMapper.selectById(updateDto.getId());
        AssertUtil.notNull(tbChargePlain,"数据不存在");
        BeanUtils.copyProperties(updateDto,tbChargePlain);
        this.updateById(tbChargePlain);

        //todo 更新xxl-job任务
    }
}
