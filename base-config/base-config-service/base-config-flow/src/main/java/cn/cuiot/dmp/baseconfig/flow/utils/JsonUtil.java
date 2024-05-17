package cn.cuiot.dmp.baseconfig.flow.utils;

import cn.cuiot.dmp.common.serialize.LongTypeJsonSerializer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public final class JsonUtil {

  private JsonUtil() throws IllegalAccessException {
    throw new IllegalAccessException("can't get the instance of util class ");
  }

  private static ObjectMapper objectMapper;

  static {
    objectMapper = new ObjectMapper().findAndRegisterModules();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    SimpleModule module = new SimpleModule();
    //解决Long 长度丢失问题
    module.addSerializer(Long.TYPE, new LongTypeJsonSerializer());
    module.addSerializer(Long.class, new LongTypeJsonSerializer());

  }

  private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

  public static <T> T readValue(String src, Class<T> clazz) {
    if (StringUtils.isEmpty(src)) {
      return null;
    }
    T t = null;
    try {
      t = objectMapper.readValue(src, clazz);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
    return t;
  }

  public static <T> T readValue(String src, String listName, TypeReference<T> typeReference) {
    if (StringUtils.isEmpty(src)) {
      return null;
    }
    Map map = readValue(src, Map.class);
    List<T> list = (List<T>) map.get(listName);
    String json = writeValueAsString(list);
    T t = null;
    try {
//            t = objectMapper.readValue(json, typeReference);
      t = objectMapper.readValue(json, typeReference);

    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
    return t;
  }
  public static <T> List<T> json2List(String src, String listName, Class<T> cls) {
    if (StringUtils.isEmpty(src)) {
      return null;
    }
    Map map = readValue(src, Map.class);
    List<T> list = (List<T>) map.get(listName);
    if(list==null){
      return null;
    }
    String json = writeValueAsString(list);
    return  json2List(json,cls);
  }

  public static <T> List<T> json2List(String json, Class<T> beanClass) {
    try {

      return (List<T>) objectMapper.readValue(json, getCollectionType(List.class, beanClass));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Map json2Map(String json) {
    try {

      return (Map) objectMapper.readValue(json, Map.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
    return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
  }


  public static <T> T readValue(String src, TypeReference<T> typeReference) {
    if (StringUtils.isEmpty(src)) {
      return null;
    }
    T t = null;
    try {
      t = objectMapper.readValue(src, typeReference);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
    return t;
  }

  public static String writeValueAsString(Object obj) {
    if (null == obj) {
      return null;
    }
    String src = null;
    try {
      src = objectMapper.writeValueAsString(obj);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
    return src;
  }

}