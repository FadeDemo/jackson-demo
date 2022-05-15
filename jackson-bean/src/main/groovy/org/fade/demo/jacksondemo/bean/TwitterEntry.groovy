package org.fade.demo.jacksondemo.bean

import com.fasterxml.jackson.annotation.JsonRootName
import groovy.transform.ToString

@ToString
@JsonRootName("value")
class TwitterEntry {

    /**
     * 推特消息id
     */
    long id

    /**
     * 消息内容
     */
    String text

    /**
     * 消息创建者
     */
    int fromUserId

    /**
     * 消息接收者
     */
    int toUserId

    /**
     * 语言类型
     */
    String languageCode

    Map<String, Object> map

}
