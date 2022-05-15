package org.fade.demo.jacksondemo.core;

import com.fasterxml.jackson.core.*;
import org.fade.demo.jacksondemo.bean.TwitterEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.fade.demo.jacksondemo.bean.Constant.TEST_JSON_STR;
import static org.fade.demo.jacksondemo.bean.Constant.TEST_OBJECT;

/**
 * @author fade
 * @date 2022/05/15
 */
public class Streaming {

    private static final Logger logger = LoggerFactory.getLogger(Streaming.class);

    JsonFactory jsonFactory = new JsonFactory();

    /**
     * 反序列化测试(JSON -> Object)，入参是JSON字符串
     * @param json JSON字符串
     * @return {@link TwitterEntry}
     */
    public TwitterEntry deserializeJsonStr(String json) throws IOException {
        JsonParser jsonParser = jsonFactory.createParser(json);
        if (jsonParser.nextToken() != JsonToken.START_OBJECT) {
            jsonParser.close();
            logger.error("起始位置没有大括号");
            throw new IOException("起始位置没有大括号");
        }
        TwitterEntry result = new TwitterEntry();
        try {
            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = jsonParser.getCurrentName();
                logger.info("正在解析字段 [{}]", jsonParser.getCurrentName());
                // 解析下一个
                jsonParser.nextToken();
                switch (fieldName) {
                    case "id":
                        result.setId(jsonParser.getLongValue());
                        break;
                    case "text":
                        result.setText(jsonParser.getText());
                        break;
                    case "fromUserId":
                        result.setFromUserId(jsonParser.getIntValue());
                        break;
                    case "toUserId":
                        result.setToUserId(jsonParser.getIntValue());
                        break;
                    case "languageCode":
                        result.setLanguageCode(jsonParser.getText());
                        break;
                    default:
                        logger.error("未知字段 '" + fieldName + "'");
                        throw new IOException("未知字段 '" + fieldName + "'");
                }
            }
        } catch (IOException e) {
            logger.error("反序列化出现异常 :", e);
        } finally {
            jsonParser.close(); // important to close both parser and underlying File reader
        }
        return result;
    }


    /**
     * 序列化测试(Object -> JSON)
     * @param twitterEntry  {@link TwitterEntry}
     * @return 由对象序列化得到的JSON字符串
     */
    public String serialize(TwitterEntry twitterEntry) {
        String rlt;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (JsonGenerator jsonGenerator = jsonFactory.createGenerator(byteArrayOutputStream, JsonEncoding.UTF8)) {
            // 格式化
            jsonGenerator.useDefaultPrettyPrinter();
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("id", twitterEntry.getId());
            jsonGenerator.writeStringField("text", twitterEntry.getText());
            jsonGenerator.writeNumberField("fromUserId", twitterEntry.getFromUserId());
            jsonGenerator.writeNumberField("toUserId", twitterEntry.getToUserId());
            jsonGenerator.writeStringField("languageCode", twitterEntry.getLanguageCode());
            jsonGenerator.writeEndObject();
        } catch (IOException e) {
            logger.error("序列化出现异常 :", e);
        }
        rlt = byteArrayOutputStream.toString();
        return rlt;
    }


    public static void main(String[] args) throws Exception {
        Streaming streamingDemo = new Streaming();
        // 执行一次对象转JSON操作
        logger.info("********************执行一次对象转JSON操作********************");
        String serializeResult = streamingDemo.serialize(TEST_OBJECT);
        logger.info("序列化结果是JSON字符串 : \n{}\n\n", serializeResult);
        // 用本地字符串执行一次JSON转对象操作
        logger.info("********************执行一次本地JSON反序列化操作********************");
        TwitterEntry deserializeResult = streamingDemo.deserializeJsonStr(TEST_JSON_STR);
        logger.info("\n本地JSON反序列化结果是个java实例 : \n{}\n\n", deserializeResult);
    }

}
