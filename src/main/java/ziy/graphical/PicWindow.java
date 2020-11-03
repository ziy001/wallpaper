package ziy.graphical;

import javax.swing.*;
import java.text.MessageFormat;

/**
 * @author ziy
 * @version 1.0
 * @date 17:49 2020/10/31
 * @description TODO:图片显示框
 * @className PicWindow
 */
public class PicWindow extends JLabel {
    /**
     * 固定的text格式
     */
    private static final String TEXT = "<html><img src=\"{0}\" height=\""+Home.getPicHeight()+"\" width=\""+ Home.getPicAndHomeWidth() +"\"/></html>";
    private String url;
    @Override
    public void setText(String url) {
        this.url = url;
        String format = MessageFormat.format(TEXT, url);
        System.out.println("载入图片 "+ format);
        super.setText(format);
    }

    public String getUrl() {
        return url;
    }
}
