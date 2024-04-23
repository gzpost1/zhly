package cn.cuiot.dmp.baseconfig.flow.dto.flowjson;

import lombok.Data;

import java.util.List;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 20:08
 */
@Data
public class EmailInfo {
  private String subject;
  private List<String> to;
  private String content;
}
