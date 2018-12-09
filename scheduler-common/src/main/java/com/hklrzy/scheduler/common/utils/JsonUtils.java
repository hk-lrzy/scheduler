package com.hklrzy.scheduler.common.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created 2018/8/5.
 *
 * @author ke.hao
 */
public class JsonUtils {

    private static ObjectMapper om = new ObjectMapper();

    static {
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.registerModule(new JavaTimeModule());
    }

    public static String toJSONString(Object object) {
        try {
            return om.writeValueAsString(object);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static Map<String, Object> toMap(String origin) {
        checkJson(origin);
        try {
            return (Map<String, Object>) om.readValue(origin, Map.class);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static <T> Map<String, T> toMap(JsonParser jsonParser, Class<T> clazz) {
        try {
            MapLikeType mapLikeType = om.getTypeFactory().constructMapLikeType(Map.class, String.class, clazz);
            return om.readValue(jsonParser, mapLikeType);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static Map<String, Object> toMap(byte[] byteArray) {
        return toMap(new String(byteArray, Charsets.UTF_8));
    }


    public static <T> Map<String, T> toMap(String json, Class<T> clazz) {
        checkJson(json);
        try {
            MapLikeType mapLikeType = om.getTypeFactory().constructMapLikeType(Map.class, String.class, clazz);
            return om.readValue(json, mapLikeType);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T> T parseValue(String text, Class<T> clazz) {
        checkJson(text);
        try {
            return om.readValue(text, clazz);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T> T parseValue(String text, Type type) {
        checkJson(text);
        try {
            return om.readValue(text, om.getTypeFactory().constructType(type));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T> T parseValue(JsonParser parser, Class<T> clazz) {
        try {
            return om.readValue(parser, clazz);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        if (Strings.isNullOrEmpty(text)) {
            return null;
        }
        try {
            CollectionType collectionType = om.getTypeFactory().constructCollectionType(List.class, clazz);
            return om.readValue(text, collectionType);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static <T> List<T> parseArray(JsonParser parser, Class<T> clazz) {
        if (parser == null) {
            return null;
        }
        try {
            CollectionType collectionType = om.getTypeFactory().constructCollectionType(List.class, clazz);
            return om.readValue(parser, collectionType);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * 后续遍历组合复杂泛型结构
     * <p>如果泛型结构为List<List<String>>，那么参数就是 List.class,List.class,String.class </p>
     *
     * @param parser   Jackson下是JSON数据结构
     * @param rawClazz 多层顺序的泛型数组
     * @param <T>      泛型没有任何用处
     * @return
     */
    public static <T> List<T> parseArray(JsonParser parser, Class<?>... rawClazz) {
        if (parser == null) {
            return null;
        }
        try {
            JavaType wrapperJavaType = om.getTypeFactory().constructType(rawClazz[rawClazz.length - 1]);
            for (int i = rawClazz.length - 2; i >= 0; i--) {
                wrapperJavaType = om.getTypeFactory().constructParametricType(rawClazz[i], wrapperJavaType);
            }
            CollectionType collectionType = om.getTypeFactory().constructCollectionType(List.class, wrapperJavaType);

            return om.readValue(parser, collectionType);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * 将数据转换为List<Map<K.class,V.class>> 这样的符合数据结构
     *
     * @param parser     Jackson下的Json数据结构
     * @param keyClass   Map.Key的类型
     * @param valueClass Map.V的类型
     * @param <K>        泛型K
     * @param <V>        泛型V
     * @return
     */
    public static <K, V> List<Map<K, V>> parseListMap(JsonParser parser, Class<K> keyClass, Class<V> valueClass) {
        if (parser == null) {
            return null;
        }
        try {
            MapType mapType = om.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
            CollectionType collectionType = om.getTypeFactory().constructCollectionType(List.class, mapType);

            return om.readValue(parser, collectionType);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static JsonNode parseToTree(String text) {
        try {
            return om.readTree(text);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }


    public static JsonNode parseToTree(Object object) {
        return om.valueToTree(object);
    }

    private static void checkJson(String json) {
        if (Strings.isNullOrEmpty(json)) {
            throw new IllegalArgumentException("Json text is null or empty!!!");
        }
    }
}
