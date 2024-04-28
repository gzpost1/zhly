package cn.cuiot.dmp.baseconfig.flow.dto.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author LoveMyOrange
 * @create 2022-10-16 9:42
 */
@Data
public class CommentVO {
    private String comments;
    private String userId;
    private String userName;
    private Date createTime;
}
