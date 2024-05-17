package cn.cuiot.dmp.baseconfig.flow.dto.flowjson;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 16:06
 */
@Data
public class SettingsInfo {
  private List<String> commiter;
  private List<UserInfo> admin;
  private Boolean sign;
  private JSONObject notify;
}
