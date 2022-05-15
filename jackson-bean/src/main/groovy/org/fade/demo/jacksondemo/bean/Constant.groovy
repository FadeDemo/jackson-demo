package org.fade.demo.jacksondemo.bean

class Constant {

    public final static String TEST_JSON_DATA_URL = "https://raw.githubusercontent.com/zq2599/blog_demos/master/files/twitteer_message.json"

    public final static String TEST_JSON_STR = "{\n" +
            "  \"id\":1125687077,\n" +
            "  \"text\":\"@stroughtonsmith You need to add a \\\"Favourites\\\" tab to TC/iPhone. Like what TwitterFon did. I can't WAIT for your Twitter App!! :) Any ETA?\",\n" +
            "  \"fromUserId\":855523, \n" +
            "  \"toUserId\":815309,\n" +
            "  \"languageCode\":\"en\"\n" +
            "}"

    public final static TwitterEntry TEST_OBJECT = new TwitterEntry()

    static {
        TEST_OBJECT.setId(123456L)
        TEST_OBJECT.setFromUserId(101)
        TEST_OBJECT.setToUserId(102)
        TEST_OBJECT.setText("this is a message for serializer test")
        TEST_OBJECT.setLanguageCode("zh")
    }

}
