package ziy.graphical;

import ziy.resource.Resource;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * @author ziy
 * @version 1.0
 * @date 11:29 2020/11/1
 * @description TODO:消息提示对话框
 * @className MessageDialog
 */
public class MessageDialog {
    public static final Image SUCCESS_MESSAGE;
    public static final Image ERROR_MESSAGE;
    static {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        SUCCESS_MESSAGE = toolkit.createImage(Resource.getURL("icon/success.png"));
        ERROR_MESSAGE = toolkit.createImage(Resource.getURL("icon/error.png"));
    }
    /**
     * 显示消息提示框，保证界面上只能出现一个消息提示对话框
     * @param title
     * @param message
     */
    public static void showMessageDialog(String title, String message, Image image) {
        //创建对话框
        JDialog jDialog = new JDialog();
        jDialog.setTitle(title);
        jDialog.setModal(true);
        jDialog.setSize(175, 90);
        jDialog.setLocation(Home.getHome().getCenterPoint(Home.getHome(), jDialog.getSize()));

        //创建内容
        JTextField messageField = new JTextField(message);
        messageField.setEnabled(false);
        messageField.setDisabledTextColor(Color.BLACK);
        messageField.setOpaque(true);
        //加入
        jDialog.setIconImage(image);
        jDialog.add(messageField);
        jDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        jDialog.setVisible(true);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        jDialog.setVisible(false);
    }

}
