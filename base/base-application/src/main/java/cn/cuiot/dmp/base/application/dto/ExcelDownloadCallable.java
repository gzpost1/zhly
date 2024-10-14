package cn.cuiot.dmp.base.application.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

/**
 * @author: wuyongchong
 * @date: 2024/10/14 11:02
 */
public interface ExcelDownloadCallable<T, R> {
    IPage<R> excute(ExcelDownloadDto<T> dto,Long PageNo,Long pageSize);
}
