package cn.cuiot.dmp.base.application.service;

import static cn.cuiot.dmp.common.constant.ResultCode.PASSWORD_IS_INVALID;

import cn.cuiot.dmp.common.constant.RegexConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.ValidateUtil;

/**
 * 密码测试
 * @author: wuyongchong
 * @date: 2024/7/9 17:45
 */
public class PwdTest {

    public static void main(String[] args) {
        String password = "Yjwl@20201688";
        if (!password.matches(RegexConst.PASSWORD_REGEX) || ValidateUtil.checkRepeat(password)
                || ValidateUtil.checkBoardContinuousChar(password)) {
            System.out.println("false");
        }else{
            System.out.println("true");
        }
    }
}
