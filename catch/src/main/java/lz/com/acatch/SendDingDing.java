package lz.com.acatch;

/**
 * -----------作者----------日期----------变更内容-----
 * -          刘泽      2019-01-15       发送钉钉机器人
 */
public class SendDingDing implements BaseSendError {

    @Override
    public String sendErrorStr() {
        String body = "{\n" +
                "    \"msgtype\": \"text\", \n" +
                "    \"text\": {\n" +
                "        \"content\": \"" + BaseSendError.BODYSTR + "\"\n" +
                "    }, \n" +
                "    \"at\": {\n" +
                "        \"atMobiles\": [\n" +
                "            \"15130515779\"\n" +
                "        ], \n" +
                "        \"isAtAll\": false\n" +
                "    }\n" +
                "}";

        return body;
    }
}
