package cn.cuiot.dmp.baseconfig.flow.dto.flowjson;

import lombok.Data;

import java.util.List;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 18:57
 */
@Data
public class ConditionInfo {
  private String id;
  private String title;
  private String valueType;
  private String compare;
  private List<Object> value;
}
