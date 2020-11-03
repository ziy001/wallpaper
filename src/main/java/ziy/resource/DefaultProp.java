package ziy.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author ziy
 * @version 1.0
 * @date 13:03 2020/10/31
 * @description TODO:默认的配置文件读取类
 * @className DefaultProps
 */
public class DefaultProp extends Resource{
    private String fileName;
    private Properties props;
    public DefaultProp(String fileName) {
        this.props = new Properties();
        this.fileName = fileName;
    }

    /**
     * 获取指定键的值
     * @param key
     * @return
     */
    public String getProperty(String key) {
        String value = "";
        try (InputStream in = Resource.getInputStream(fileName)){
            props.load(in);
            value = props.getProperty("time");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 设置指定键的值
     * @param key
     * @param value
     */
    public void setProperty(String key, String value) {
        try(final OutputStream out = getOutputStream(fileName)) {
            props.setProperty(key, value);
            props.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
            props.remove(key, value);
        }
    }
}
