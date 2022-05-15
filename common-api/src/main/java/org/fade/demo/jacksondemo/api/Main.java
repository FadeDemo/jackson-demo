package org.fade.demo.jacksondemo.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fade.demo.jacksondemo.bean.TwitterEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author fade
 * @date 2022/05/15
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        // 和设置PrettyPrinter的作用一样
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        logger.info("以下是序列化操作");
        byte[] array = singleObject2Json(mapper);
        logger.info("\n\n以下是反序列化操作");
        String objectJsonStr = json2SingleObject(mapper, array);
        logger.info("\n\n以下是集合序列化操作");
        String mapJsonStr = collection2json(mapper);
        logger.info("\n\n以下是集合反序列化操作");
        json2Collection(mapper, mapJsonStr);
        timeSerialize(mapper, objectJsonStr);
        // json数组
        String jsonArrayStr = "[{\n" +
                "  \"id\":1,\n" +
                "  \"text\":\"text1\",\n" +
                "  \"fromUserId\":11, \n" +
                "  \"toUserId\":111,\n" +
                "  \"languageCode\":\"en\"\n" +
                "},\n" +
                "{\n" +
                "  \"id\":2,\n" +
                "  \"text\":\"text2\",\n" +
                "  \"fromUserId\":22, \n" +
                "  \"toUserId\":222,\n" +
                "  \"languageCode\":\"zh\"\n" +
                "},\n" +
                "{\n" +
                "  \"id\":3,\n" +
                "  \"text\":\"text3\",\n" +
                "  \"fromUserId\":33, \n" +
                "  \"toUserId\":333,\n" +
                "  \"languageCode\":\"en\"\n" +
                "}]";
        array(mapper, jsonArrayStr);
    }

    private static void array(ObjectMapper mapper, String jsonArrayStr) throws JsonProcessingException {
        // json数组 -> 对象数组
        TwitterEntry[] twitterEntryArray = mapper.readValue(jsonArrayStr, TwitterEntry[].class);
        logger.info("json数组反序列化成对象数组：{}", Arrays.toString(twitterEntryArray));
        // json数组 -> 对象集合
        List<TwitterEntry> twitterEntryList = mapper.readValue(jsonArrayStr, new TypeReference<>() {});
        logger.info("json数组反序列化成对象集合：{}", twitterEntryList);
    }

    private static void timeSerialize(ObjectMapper mapper, String objectJsonStr) throws JsonProcessingException {
        // 时间类型格式
        Map<String, Object> dateMap = new HashMap<>(16);
        dateMap.put("today", new Date());
        String dateMapStr = mapper.writeValueAsString(dateMap);
        logger.info("默认的时间序列化：{}", dateMapStr);
        // 设置时间格式
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
        dateMapStr = mapper.writeValueAsString(dateMap);
        logger.info("自定义的时间序列化：{}", dateMapStr);
        System.out.println(objectJsonStr);
    }

    private static void json2Collection(ObjectMapper mapper, String mapJsonStr) throws JsonProcessingException {
        Map<String, Object> mapFromStr = mapper.readValue(mapJsonStr, new TypeReference<>() {});
        logger.info("从字符串反序列化的HashMap对象：{}", mapFromStr);
        // JsonNode类型操作
        JsonNode jsonNode = mapper.readTree(mapJsonStr);
        String name = jsonNode.get("name").asText();
        int age = jsonNode.get("age").asInt();
        String city = jsonNode.get("addr").get("city").asText();
        String street = jsonNode.get("addr").get("street").asText();
        logger.info("用JsonNode对象和API反序列化得到的数：name[{}]、age[{}]、city[{}]、street[{}]", name, age, city, street);
    }

    private static String collection2json(ObjectMapper mapper) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>(16);
        map.put("name", "tom");
        map.put("age", 11);
        Map<String, String> addr = new HashMap<>(16);
        addr.put("city","深圳");
        addr.put("street", "粤海");
        map.put("addr", addr);
        String mapJsonStr = mapper.writeValueAsString(map);
        logger.info("HashMap序列化的字符串：{}", mapJsonStr);
        return mapJsonStr;
    }

    private static String json2SingleObject(ObjectMapper mapper, byte[] array) throws IOException {
        // 字符串 -> 对象
        String objectJsonStr = "{\n" +
                "  \"id\":1125687077,\n" +
                "  \"text\":\"@stroughtonsmith You need to add a \\\"Favourites\\\" tab to TC/iPhone. Like what TwitterFon did. I can't WAIT for your Twitter App!! :) Any ETA?\",\n" +
                "  \"fromUserId\":855523, \n" +
                "  \"toUserId\":815309,\n" +
                "  \"languageCode\":\"en\"\n" +
                "}";
        TwitterEntry tFromStr = mapper.readValue(objectJsonStr, TwitterEntry.class);
        logger.info("从字符串反序列化的对象：{}", tFromStr);
        // 文件 -> 对象
        TwitterEntry tFromFile = mapper.readValue(new File("twitter.json"), TwitterEntry.class);
        logger.info("从文件反序列化的对象：{}", tFromFile);
        // byte数组 -> 对象
        TwitterEntry tFromBytes = mapper.readValue(array, TwitterEntry.class);
        logger.info("从byte数组反序列化的对象：{}", tFromBytes);
        return objectJsonStr;
    }

    private static byte[] singleObject2Json(ObjectMapper mapper) throws IOException {
        // 对象 -> 字符串
        TwitterEntry twitterEntry = new TwitterEntry();
        twitterEntry.setId(123456L);
        twitterEntry.setFromUserId(101);
        twitterEntry.setToUserId(102);
        twitterEntry.setText("this is a message for serializer test");
        twitterEntry.setLanguageCode("zh");
        String jsonStr = mapper.writeValueAsString(twitterEntry);
        logger.info("序列化的字符串：{}", jsonStr);
        // 对象 -> 文件
        mapper.writeValue(new File("twitter.json"), twitterEntry);
        // 对象 -> byte数组
        return mapper.writeValueAsBytes(twitterEntry);
    }

}
