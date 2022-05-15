package org.fade.demo.jacksondemo.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fade.demo.jacksondemo.bean.Constant;
import org.fade.demo.jacksondemo.bean.TwitterEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置
 *
 * @author fade
 * @date 2022/05/15
 */
public class Config {

    private static final Logger LOG = LoggerFactory.getLogger(Config.class);

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        prettyPrint(objectMapper);
        emptyObjectWithException(objectMapper);
        formatTime(objectMapper);
        unknownProperty(objectMapper);
        emptyString(objectMapper);
        allowComment(objectMapper);
        unQuoted(objectMapper);
        singleQuote(objectMapper);
        wrapRoot(objectMapper);
    }

    public static void prettyPrint(ObjectMapper objectMapper) {
        try {
            objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
            String json = objectMapper.writeValueAsString(Constant.TEST_OBJECT);
            LOG.info("关闭SerializationFeature.INDENT_OUTPUT配置");
            LOG.info(json);
            LOG.info("开启SerializationFeature.INDENT_OUTPUT配置");
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            json = objectMapper.writeValueAsString(Constant.TEST_OBJECT);
            LOG.info(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        }
    }

    public static void emptyObjectWithException(ObjectMapper objectMapper) {
        objectMapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        try {
            LOG.info("开启SerializationFeature.FAIL_ON_EMPTY_BEANS配置");
            String json = objectMapper.writeValueAsString(new Object());
            System.out.println(json);
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
        }
        try {
            objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            LOG.info("关闭SerializationFeature.FAIL_ON_EMPTY_BEANS配置");
            String json = objectMapper.writeValueAsString(new Object());
            LOG.info(json);
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
        }
        objectMapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    public static void formatTime(ObjectMapper objectMapper) {
        objectMapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        LOG.info("开启SerializationFeature.WRITE_DATES_AS_TIMESTAMPS配置");
        try {
            String json = objectMapper.writeValueAsString(new Date());
            LOG.info(json);
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
        }
        LOG.info("关闭SerializationFeature.WRITE_DATES_AS_TIMESTAMPS配置");
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            String json = objectMapper.writeValueAsString(new Date());
            LOG.info(json);
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
        }
        objectMapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static void unknownProperty(ObjectMapper objectMapper) {
        objectMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        LOG.info("开启DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES配置");
        Map<String, Object> map = new HashMap<>(16);
        map.put("date", new Date());
        mapAction(objectMapper, map);
        LOG.info("关闭DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES配置");
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapAction(objectMapper, map);
        objectMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    private static void mapAction(ObjectMapper objectMapper, Map<String, Object> map) {
        try {
            String json = objectMapper.writeValueAsString(map);
            LOG.info("序列化结果为：" + json);
            TwitterEntry twitterEntry = objectMapper.readValue(json, TwitterEntry.class);
            LOG.info("反序列化结果为：" + twitterEntry);
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
        }
    }

    public static void emptyString(ObjectMapper objectMapper) {
        objectMapper.disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        Map<String, Object> map = new HashMap<>(16);
        map.put("text", "");
        LOG.info("[失效]关闭DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT配置");
        mapAction(objectMapper, map);
        LOG.info("[失效]开启DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT配置");
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapAction(objectMapper, map);
        map.clear();
        map.put("map", "");
        objectMapper.disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        LOG.info("[有效]关闭DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT配置");
        mapAction(objectMapper, map);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        LOG.info("[有效]开启DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT配置");
        mapAction(objectMapper, map);
        objectMapper.disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    }

    public static void allowComment(ObjectMapper objectMapper) {
        final String json = "{\n" +
                "  \"id\":1125687077,\n" +
                "  \"text\":\"@stroughtonsmith You need to add a \\\"Favourites\\\" tab to TC/iPhone. Like what TwitterFon did. I can't WAIT for your Twitter App!! :) Any ETA?\",\n" +
                "  \"fromUserId\":855523, \n" +
                "  \"toUserId\":815309,\n" +
                " // 语言类型\n" +
                "  \"languageCode\":\"en\"\n" +
                "}";
        objectMapper.disable(JsonParser.Feature.ALLOW_COMMENTS);
        LOG.info("关闭JsonParser.Feature.ALLOW_COMMENTS配置");
        try {
            TwitterEntry twitterEntry = objectMapper.readValue(json, TwitterEntry.class);
            LOG.info(twitterEntry.toString());
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
        }
        objectMapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
        LOG.info("开启JsonParser.Feature.ALLOW_COMMENTS配置");
        try {
            TwitterEntry twitterEntry = objectMapper.readValue(json, TwitterEntry.class);
            LOG.info(twitterEntry.toString());
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
        }
        objectMapper.disable(JsonParser.Feature.ALLOW_COMMENTS);
    }

    public static void unQuoted(ObjectMapper objectMapper) {
        final String json = "{\n" +
                "  id:1125687077,\n" +
                "  \"text\":\"@stroughtonsmith You need to add a \\\"Favourites\\\" tab to TC/iPhone. Like what TwitterFon did. I can't WAIT for your Twitter App!! :) Any ETA?\",\n" +
                "  \"fromUserId\":855523, \n" +
                "  \"toUserId\":815309,\n" +
                "  \"languageCode\":\"en\"\n" +
                "}";
        objectMapper.disable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        LOG.info("关闭JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES配置");
        try {
            TwitterEntry twitterEntry = objectMapper.readValue(json, TwitterEntry.class);
            LOG.info(twitterEntry.toString());
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
        }
        LOG.info("开启JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES配置");
        objectMapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        try {
            TwitterEntry twitterEntry = objectMapper.readValue(json, TwitterEntry.class);
            LOG.info(twitterEntry.toString());
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
        }
        objectMapper.disable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
    }

    public static void singleQuote(ObjectMapper objectMapper) {
        final String json = "{\n" +
                "  'id':1125687077,\n" +
                "  \"text\":\"@stroughtonsmith You need to add a \\\"Favourites\\\" tab to TC/iPhone. Like what TwitterFon did. I can't WAIT for your Twitter App!! :) Any ETA?\",\n" +
                "  \"fromUserId\":855523, \n" +
                "  \"toUserId\":815309,\n" +
                "  \"languageCode\":\"en\"\n" +
                "}";
        objectMapper.disable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        LOG.info("关闭JsonParser.Feature.ALLOW_SINGLE_QUOTES配置");
        try {
            TwitterEntry twitterEntry = objectMapper.readValue(json, TwitterEntry.class);
            LOG.info(twitterEntry.toString());
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
        }
        LOG.info("开启JsonParser.Feature.ALLOW_SINGLE_QUOTES配置");
        objectMapper.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        try {
            TwitterEntry twitterEntry = objectMapper.readValue(json, TwitterEntry.class);
            LOG.info(twitterEntry.toString());
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
        }
        objectMapper.disable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
    }

    public static void wrapRoot(ObjectMapper objectMapper) {
        objectMapper.disable(SerializationFeature.WRAP_ROOT_VALUE);
        LOG.info("关闭SerializationFeature.WRAP_ROOT_VALUE配置");
        try {
            String json = objectMapper.writeValueAsString(Constant.TEST_OBJECT);
            LOG.info(json);
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
        }
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        LOG.info("开启SerializationFeature.WRAP_ROOT_VALUE配置");
        try {
            String json = objectMapper.writeValueAsString(Constant.TEST_OBJECT);
            LOG.info(json);
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
        }
        objectMapper.disable(SerializationFeature.WRAP_ROOT_VALUE);
    }

}
