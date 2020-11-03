package ziy.resource;

import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.net.URL;

/**
 * @author ziy
 * @version 1.0
 * @date 11:36 2020/10/29
 * @description TODO:资源访问
 * @className Resource
 */
public class Resource {
    /**
     * 返回文件的URL
     * @param fileName
     * @return
     */
    public static URL getURL(String fileName) {
        URL url = null;
        try {
            url = new ClassPathResource(fileName).getURL();
        } catch (IOException e) {
            System.err.println(fileName +"资源访问错误");
        }
        return url;
    }

    /**
     * 返回类路径下的文件的输入流
     * @param fileName
     * @return
     */
    public static InputStream getInputStream(String fileName) {
        InputStream in = null;
        try {
            in = new ClassPathResource(fileName).getInputStream();
            System.out.println(new ClassPathResource(fileName).getURL());
        } catch (IOException e) {
            System.err.println(fileName +"资源访问错误");
        }
        return in;
    }
    public static OutputStream getOutputStream(String fileName) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(new ClassPathResource(fileName).getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

}
