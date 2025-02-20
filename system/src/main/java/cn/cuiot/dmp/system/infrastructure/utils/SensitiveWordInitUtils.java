package cn.cuiot.dmp.system.infrastructure.utils;

import cn.cuiot.dmp.system.infrastructure.entity.SensitivityEntity;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * @Description: 敏感词过滤工具类
 * @author xumengfan
 * @date 2020年12月09日
 */
@Component
public class SensitiveWordInitUtils {
    /**
     * 敏感词库
     */
    public HashMap sensitiveWordMap;

    /**
     * 初始化敏感词
     *
     * @return
     */
    public Map initKeyWord(List<SensitivityEntity> sensitiveWords) {
        try {
            // 从敏感词集合对象中取出敏感词并封装到Set集合中
            Set<String> keyWordSet = new HashSet<>(16);
            for (SensitivityEntity sensitiveWord : sensitiveWords) {
                keyWordSet.add(sensitiveWord.getSensitiveWord().trim());
            }
            // 将敏感词库加入到HashMap中
            addSensitiveWordToHashMap(keyWordSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sensitiveWordMap;
    }

    /**
     * 封装敏感词库
     *
     * @param keyWordSet
     */
    private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        // 初始化HashMap对象并控制容器的大小
        sensitiveWordMap = new HashMap(keyWordSet.size());
        // 敏感词
        String key = null;
        // 用来按照相应的格式保存敏感词库数据
        Map nowMap = null;
        // 用来辅助构建敏感词库
        Map<String, String> newWorMap = null;
        // 使用一个迭代器来循环敏感词集合
        Iterator<String> iterator = keyWordSet.iterator();
        while (iterator.hasNext()) {
            key = iterator.next();
            // 等于敏感词库，HashMap对象在内存中占用的是同一个地址，所以此nowMap对象的变化，sensitiveWordMap对象也会跟着改变
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                // 截取敏感词当中的字，在敏感词库中字为HashMap对象的Key键值
                char keyChar = key.charAt(i);

                // 判断这个字是否存在于敏感词库中
                Object wordMap = nowMap.get(keyChar);
                if (wordMap != null) {
                    nowMap = (Map) wordMap;
                } else {
                    newWorMap = new HashMap<>(16);
                    newWorMap.put("isEnd", "0");
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }
                // 如果该字是当前敏感词的最后一个字，则标识为结尾字
                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1");
                }
            }
        }
    }

}
