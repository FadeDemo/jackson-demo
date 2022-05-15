package org.fade.demo.jacksondemo.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fade.demo.jacksondemo.bean.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

}
