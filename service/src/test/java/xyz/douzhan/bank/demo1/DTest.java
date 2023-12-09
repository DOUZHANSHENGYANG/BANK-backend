package xyz.douzhan.bank.demo1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/7 21:31
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@SpringBootTest
public class DTest {
    @Test
    public void testParseJsonString() throws JsonProcessingException {
        String json="{\n" +
                "    \"log_id\": 2648325511,\n" +
                "    \"direction\": 0,\n" +
                "    \"image_status\": \"normal\",\n" +
                "    \"idcard_type\": \"normal\",\n" +
                "    \"edit_tool\": \"Adobe Photoshop CS3 Windows\",\n" +
                "    \"words_result\": {\n" +
                "        \"住址\": {\n" +
                "            \"location\": {\n" +
                "                \"left\": 267,\n" +
                "                \"top\": 453,\n" +
                "                \"width\": 459,\n" +
                "                \"height\": 99\n" +
                "            },\n" +
                "            \"words\": \"南京市江宁区弘景大道3889号\"\n" +
                "        },\n" +
                "        \"公民身份号码\": {\n" +
                "            \"location\": {\n" +
                "                \"left\": 443,\n" +
                "                \"top\": 681,\n" +
                "                \"width\": 589,\n" +
                "                \"height\": 45\n" +
                "            },\n" +
                "            \"words\": \"330881199904173914\"\n" +
                "        },\n" +
                "        \"出生\": {\n" +
                "            \"location\": {\n" +
                "                \"left\": 270,\n" +
                "                \"top\": 355,\n" +
                "                \"width\": 357,\n" +
                "                \"height\": 45\n" +
                "            },\n" +
                "            \"words\": \"19990417\"\n" +
                "        },\n" +
                "        \"姓名\": {\n" +
                "            \"location\": {\n" +
                "                \"left\": 267,\n" +
                "                \"top\": 176,\n" +
                "                \"width\": 152,\n" +
                "                \"height\": 50\n" +
                "            },\n" +
                "            \"words\": \"伍云龙\"\n" +
                "        },\n" +
                "        \"性别\": {\n" +
                "            \"location\": {\n" +
                "                \"left\": 269,\n" +
                "                \"top\": 262,\n" +
                "                \"width\": 33,\n" +
                "                \"height\": 52\n" +
                "            },\n" +
                "            \"words\": \"男\"\n" +
                "        },\n" +
                "        \"民族\": {\n" +
                "            \"location\": {\n" +
                "                \"left\": 492,\n" +
                "                \"top\": 279,\n" +
                "                \"width\": 30,\n" +
                "                \"height\": 37\n" +
                "            },\n" +
                "            \"words\": \"汉\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"words_result_num\": 6\n" +
                "}\n";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        JsonNode wordsResultNode = jsonNode.get("words_result");

        Map<String, String> resultMap = new HashMap<>();
        Iterator<Map.Entry<String, JsonNode>> fields = wordsResultNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String key = field.getKey();
            String words = field.getValue().get("words").asText();
            resultMap.put(key, words);
        }
        System.out.println(resultMap);
    }
}
