package ziy.utils.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;
import java.util.Random;

/**
 * @author ziy
 * @version 1.0
 * @date 下午3:57 2020/10/25
 * @description TODO:解析JSON
 * @className Json
 */
public class Json {
    /**
     * 解析Json数据的入口
     * @param url
     * @param resp
     * @return
     * @throws JsonProcessingException
     */
    public static String parseJson(String url, HttpResponse<String> resp) throws JsonProcessingException {
        if(url.contains("bing")) {
            return Json.parseBingJson(resp.body());
        }
        else if(url.contains("getAppsByCategory") || url.contains("search")){
            return Json.parse360Json(resp.body());

        }
        else if(url.contains("muxiaoguo")) {
            return Json.parseRandomJson(resp.body());
        }
        return "";
    }
    /**
     * 解析必应壁纸API
     * @param json
     * @return 返回今日壁纸链接
     * @throws JsonProcessingException
     */
    private static String parseBingJson(String json) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        //解析JSON数据
        JsonNode images = null;
        images = om.readTree(json).path("images");
        return "https://cn.bing.com"+images.path(0).path("url").asText();
    }

    /**
     * 解析360API返回的json
     * @param json
     * @return
     * @throws JsonProcessingException
     */
    private static String parse360Json(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        //获取data数组
        JsonNode data = mapper.readTree(json).path("data");
        //data数组中随机的一项数据，再取出该数据中的url项
        return data.path(new Random().nextInt(data.size())).path("url").asText();
    }

    /**
     * 解析木小果API返回的JSON
     * @param json
     * @return
     * @throws JsonProcessingException
     */
    private static String parseRandomJson(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(json).path("data").path("imgurl").asText();
    }
}
