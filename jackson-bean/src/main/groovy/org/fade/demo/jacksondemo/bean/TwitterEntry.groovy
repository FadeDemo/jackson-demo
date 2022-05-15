package org.fade.demo.jacksondemo.bean

import groovy.transform.ToString

@ToString
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

}
