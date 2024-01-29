package haidnor.remoting.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 标砖 JSON 格式序列化，不包含类型信息
 *
 * @author wang xiang
 */
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
     * Object > byte[]
     */
    public static byte[] toJsonBytes(Object bean) {
        if (bean == null) {
            throw new IllegalArgumentException("the bean parameter can not be null");
        }
        try {
            return objectMapper.writeValueAsBytes(bean);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // ------------------------------------------------------------------------------------------------------------------

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

    /**
     * byte[] > Object
     */
    public static <T> T toBean(byte[] jsonBytes, Class<T> type) {
        if (jsonBytes == null) {
            throw new IllegalArgumentException("the jsonBytes parameter can not be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("the type parameter can not be null");
        }
        try {
            return objectMapper.readValue(jsonBytes, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * str > Object
     */
    public static <T> T toBean(String json, TypeReference<T> typeReference) {
        if (json == null) {
            throw new IllegalArgumentException("the json parameter can not be null");
        }
        if (typeReference == null) {
            throw new IllegalArgumentException("the typeReference parameter can not be null");
        }
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * byte[] > Object
     */
    public static <T> T toBean(byte[] jsonBytes, TypeReference<T> typeReference) {
        if (jsonBytes == null) {
            throw new IllegalArgumentException("the jsonBytes parameter can not be null");
        }
        if (typeReference == null) {
            throw new IllegalArgumentException("the typeReference parameter can not be null");
        }
        try {
            return objectMapper.readValue(jsonBytes, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * src > Object
     */
    public static <T> T toBean(InputStream src, Class<T> type) {
        if (src == null) {
            throw new IllegalArgumentException("the inputStream parameter can not be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("the type parameter can not be null");
        }
        try {
            return objectMapper.readValue(src, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * src > Object
     */
    public static <T> T toBean(InputStream src, TypeReference<T> typeReference) {
        if (src == null) {
            throw new IllegalArgumentException("the inputStream parameter can not be null");
        }
        if (typeReference == null) {
            throw new IllegalArgumentException("the typeReference parameter can not be null");
        }
        try {
            return objectMapper.readValue(src, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // JsonNode --------------------------------------------------------------------------------------------------------

    /**
     * JsonNode > Object
     */
    public static <T> T treeToValue(JsonNode jsonNode, Class<T> type) {
        try {
            return objectMapper.treeToValue(jsonNode, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json > List<JsonNode>
     */
    public static List<JsonNode> toJsonNodeList(String json) {
        return toList(json, JsonNode.class);
    }

    /**
     * json > JsonNode
     */
    public static JsonNode toJsonNode(String json) {
        return toBean(json, JsonNode.class);
    }

}
