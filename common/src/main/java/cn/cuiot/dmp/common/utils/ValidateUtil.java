package cn.cuiot.dmp.common.utils;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.util.CollectionUtils;

/**
 * @param
 * @Author xieSH
 * @Description 通用校验工具
 * @Date 2021/8/17 10:24
 * @return
 **/
public class ValidateUtil {

    private ValidateUtil() {
        throw new BusinessException(ResultCode.CANNOT_CREATE_CONST_CLASS_OBJECT);
    }

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 数字2
     */
    public static final int NUMBER_2 = 2;

    /**
     * 通用字段注解校验
     *
     * @param pojo
     */
    public static <T> void validate(T pojo, Class... groups) {
        Set<ConstraintViolation<T>> violationSet = validator.validate(pojo, groups);
        if (CollectionUtils.isEmpty(violationSet)) {
            return;
        }
        List<String> exceptionMsgList = violationSet.stream().map(ConstraintViolation::getMessage).sorted().collect(Collectors.toList());
        throw new BusinessException("140007",exceptionMsgList.get(BigDecimal.ZERO.intValue()));
    }


    /**
     * 计算字符串的长度，中文算两个字符，字母数字算一个字符
     *
     * @param str 字符串
     * @return
     */
    public static int calculateStrLength(String str) {
        int length = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < str.length(); i++) {
            String temp = str.substring(i, i + 1);
            if (temp.matches(chinese)) {
                // 匹配为中文
                length += 2;
            } else {
                length += 1;
            }
        }
        return length;
    }
    /**
     * 不能连续字符（如123、abc）连续3位或3位以上
     *
     * @param str 字符串
     * @return
     */
    public static Boolean checkRepeat(String str){
        String[] arr = str.split("");
        boolean flag = false;
        for (int i = 1; i < arr.length-1; i++) {
            int firstIndex = getUnicode(arr[i-1].charAt(0));
            int secondIndex = getUnicode(arr[i].charAt(0));
            int thirdIndex = getUnicode (arr[i+1].charAt(0));
            if((thirdIndex - secondIndex == 1)&&(secondIndex - firstIndex==1)){
                flag =  true;
            }
        }
        return flag;
    }
    /**
     * 转码
     *
     * @param c 字符串
     * @return
     */
    private static int getUnicode(char c){
        String returnUniCode = null;
        returnUniCode =String.valueOf((int)c);
        return  Integer.parseInt(returnUniCode);
    }
    /**
     * 判断密码是否是键盘连续；例如 ‘qwe,asd’
     *
     * @param str 字符串
     * @return
     */
    public static Boolean checkBoardContinuousChar(String str) {
        boolean flag = false;
        //定义横向穷举
        String[][] keyCode = {
                {"`~·", "1=", "2@@", "3#", "4$￥", "5%", "6^……", "7&", "8*", "9(（", "0）)", "-_", "=+"},
                {" ","qQ", "wW", "eE", "rR", "tT", "yY", "uU", "iI", "oO", "pP", "[{【", "]}】", "\\|、"},
                {" ","aA", "sS", "dD", "fF", "gG", "hH", "jJ", "kK", "lL", ";:", "\'\"’“"},
                {" ","zZ", "xX", "cC", "vV", "bB", "nN", "mM", ",《<", ".>》", "/?？"}
        };

        //找出给出的字符串，每个字符，在坐标系中的位置。
        char[] c = str.toCharArray();
        List<Integer> x = new ArrayList<>();
        List<Integer> y = new ArrayList<>();
        for (char temp : c) {
            toHere:
            for (int j = 0; j < keyCode.length; j++) {
                for (int k = 0; k < keyCode[j].length; k++) {
                    String jk = keyCode[j][k];
                    if (jk.contains(String.valueOf(temp))) {
                        x.add(j);
                        y.add(k);
                        break toHere;
                    }
                }
            }
        }
        for (int i = 0; i < x.size() - NUMBER_2; i++) {
            // 如果X一致，那么就是在一排，四者在同一行上
            if (x.get(i).equals(x.get(i + 1)) && x.get(i + 1).equals(x.get(i + 2))) {
                if (y.get(i) > y.get(i + 2)) {
                    if (y.get(i) - 1 == y.get(i + 1) && y.get(i) - 2 == y.get(i + 2) ) {
                        flag = true;
                        break;
                    }
                } else {
                    if (y.get(i) + 1 == y.get(i + 1) && y.get(i) + 2 == y.get(i + 2) ) {
                        flag = true;
                        break;
                    }
                }

            } else if (!x.get(i).equals(x.get(i + 1))
                    && !x.get(i + 1).equals(x.get(i + 2))
                    && !x.get(i).equals(x.get(i + 2))
            ) {//四者均不在同一行上,但是如果y相同，说明是一列
                if (y.get(i).equals(y.get(i + 1)) && y.get(i + 1).equals(y.get(i + 2))) {
                    flag = true;
                    break;
                }
            }

        }
        return flag;
    }

}
