package cn.cuiot.dmp.baseconfig.flow.dto.flowjson;

import lombok.Data;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 16:10
 */
@Data
public class UserInfo {
  /**
   * 用户/部门/角色 id
   */
  private String id;
  /**
   * 用户/部门/角色 名称
   */
  private String name;
  /**
   * 用户（user）/部门(dept)/角色(role) 类型
   */
  private String type;

  private String sex;
  private Boolean selected;
}
