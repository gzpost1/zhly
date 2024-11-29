package cn.cuiot.dmp.base.application.dto;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;

/**
 * @Description TODO
 * @Date 2024-11-29 10:01
 * @Author by Mujun~
 */
public class BaseExcelModel implements IExcelModel, IExcelDataModel {
    private String errorMsg;

    private Integer rowNum;

    @Override
    public Integer getRowNum() {
        return rowNum;
    }

    @Override
    public void setRowNum(Integer rowNum) {
        this.rowNum=rowNum;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void setErrorMsg(String errorMsg) {
        this.errorMsg=errorMsg;
    }
}
