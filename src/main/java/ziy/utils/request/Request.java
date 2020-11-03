package ziy.utils.request;


import java.io.*;
import java.net.URI;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.*;

/**
 * @author ziy
 * @version 1.0
 * @date 下午2:01 2020/10/25
 * @description TODO:网络请求工具类
 * @className Request
 */
public class Request {
    /**
     * API集合
     */
    private static final Map<String, String> APIS = Map.of(
            "bing", "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=zh-CN",
            "360", "http://wallpaper.apc.360.cn/index.php?%20c=WallPaper&a=getAppsByCategory&cid={0}&start={1}&count={2}",
            "random", "https://api.muxiaoguo.cn/api/sjbz?method=pc&type=sina"
    );
    private static int[] cids = new int[]{1, 5, 6, 9, 10, 11, 12, 14, 15, 16, 22, 26, 30 ,35};

    /**
     * 构造Request并返回
     * @param uri url
     * @return HttpRequest对象
     */
    private static HttpRequest getRequest(String uri) {
        return HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();
    }
    /**
     * 获取今日的壁纸链接（必应的今日壁纸）
     * @return
     */
    public static String today() {
        return send(APIS.get("bing"));
    }

    /**
     * 随机获取壁纸
     * @return
     */
    public static String random() {
        String imgUrl;
        if (new Random().nextBoolean()) {
            imgUrl = send(APIS.get("random"));
        }
        else {
                String url = MessageFormat.format(APIS.get("360"),
                        cids[new Random().nextInt(cids.length)], String.valueOf(new Random().nextInt(8005)), 5);
            imgUrl = send(url);
        }
        return imgUrl;
    }

    /**
     * 发送请求
     * @param url API
     * @return 一张壁纸的链接
     */
    private static String send(String url) {
        HttpRequest request = getRequest(url);
        HttpResponse.BodyHandler<String> stringBodyHandler = HttpResponse.BodyHandlers.ofString(Charset.forName("utf-8"));
        HttpResponse<String> resp = null;
        String imgUrl = "";
        try {
            //发送请求体
            resp = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build()
                    .send(request, stringBodyHandler);
            if (resp.statusCode() != 200) {
                System.err.println("服务器异常");
                throw new Exception("服务器异常");
            }
            else {
                imgUrl = Json.parseJson(url, resp);
            }
        } catch (Exception e) {
            System.err.println(request.uri()+" 请求失败");
            imgUrl = send(APIS.get("random"));
        }
        return imgUrl;
    }

    /**
     * 文件下载
     * @param filePath
     * @param imgUrl
     * @return
     * @throws IOException
     */
    public static void download(Path filePath, String imgUrl) throws IOException {
        //发送请求
        URLConnection conn = URI.create(imgUrl).toURL().openConnection();
        conn.setConnectTimeout(5);
        try(InputStream in = conn.getInputStream();
            RandomAccessFile rw = new RandomAccessFile(filePath.toFile(), "rw")
        ) {
            rw.setLength(conn.getContentLength());
            byte[] bytes = in.readAllBytes();
            rw.write(bytes);
        }
    }
}
