package haidnor.remoting.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;

import java.io.IOException;
import java.util.*;

public class Jackson {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    /**
     * Object > json
     */
    public static String toJsonStr(Object bean) {
        if (bean == null) {
            throw new IllegalArgumentException("the bean parameter can not be null");
        }
        try {
            return objectMapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json > Object
     */
    public static <T> T toBean(String json, Class<T> type) {
        if (json == null) {
            throw new IllegalArgumentException("the json parameter can not be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("the type parameter can not be null");
        }
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json > List
     */
    public static <T> List<T> toList(String json, Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("the type parameter can not be null");
        }
        if (json == null) {
            return new ArrayList<>();
        }
        try {
            CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
            return objectMapper.readValue(json, collectionType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json > toSet
     */
    public static <T> Set<T> toSet(String json, Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("the type parameter can not be null");
        }
        if (json == null) {
            return new HashSet<>();
        }
        try {
            CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(HashSet.class, type);
            return objectMapper.readValue(json, collectionType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json > Map
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> keyType, Class<V> valueType) {
        if (keyType == null) {
            throw new IllegalArgumentException("the keyType parameter can not be null");
        }
        if (valueType == null) {
            throw new IllegalArgumentException("the valueType parameter can not be null");
        }
        if (json == null) {
            return new LinkedHashMap<>();
        }
        try {
            MapType mapType = objectMapper.getTypeFactory().constructMapType(LinkedHashMap.class, keyType, valueType);
            return objectMapper.readValue(json, mapType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
