package cn.cuiot.dmp.baseconfig.flow.dto.flowjson;

import lombok.Data;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 15:57
 */
@Data
public class FormItemProps {
  private Boolean required;
  private Boolean enablePrint;
  private String  placeHolder;
}
