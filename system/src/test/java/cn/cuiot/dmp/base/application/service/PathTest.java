package cn.cuiot.dmp.base.application.service;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.util.List;

/**
 * @author: wuyongchong
 * @date: 2024/6/14 15:51
 */
public class PathTest {

    public static void main(String[] args) {
        String path="00-BEIJING-222";
        List<String> resultList = getPathList(path);
        System.out.println(resultList);
    }

    private static List<String> getPathList(String path){
        List<String> strings = Splitter.on("-").splitToList(path);
        List<String> resultList = Lists.newArrayList();
        String tmpPath="";
        for(int i=0;i<strings.size();i++){
            if(i==0){
                tmpPath=strings.get(i);
            }else{
                tmpPath=tmpPath+"-"+strings.get(i);
            }
            resultList.add(tmpPath);
        }
        return resultList;
    }

}
