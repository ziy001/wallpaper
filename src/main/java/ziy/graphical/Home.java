package ziy.graphical;

import ziy.graphical.builder.Builder;
import ziy.pool.Pool;
import ziy.resource.DefaultProp;
import ziy.utils.Util;
import ziy.utils.request.Request;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * @author ziy
 * @version 1.0
 * @date 下午12:42 2020/10/25
 * @description TODO:软件主界面类->单例类
 * @className Home
 */
public class Home extends JFrame{
    private static Home home;
    private static Toolkit toolkit;
    /**
     * 图片Size (默认值)，值和主界面的值一样
     */
    private static int picAndHomeWidth;
    /**
     * 图片的高度
     */
    private static int picHeight;
    /**
     * 功能区布局
     */
    private static final FlowLayout FLOW_LAYOUT;
    /**
     * 图片显示框对象
     */
    private PicWindow  picWindow;
    /**
     * 下载路径
     */
    public static final String PATH = "C:\\simpleWallpapers";
    public static final DefaultProp PROPS;
    /**
     * 数据的初始化，实现窗体的动态自适应调整
     */
    static {
        toolkit = Toolkit.getDefaultToolkit();
        //配置文件
        PROPS = new DefaultProp("settings.properties");
        //设置壁纸Size
        picAndHomeWidth = 854;
        picHeight = 480;
        FLOW_LAYOUT = new FlowLayout(FlowLayout.LEFT, 25, 20);

    }


    private Home(String title) {
        super(title);
        if(Objects.nonNull(home)) {
            throw new RuntimeException("已经存在界面无法再次创建");
        }
    }

    public static Home getHome() {
        return home;
    }


    /**
     * 界面构造的入口方法
     */
    public void build() {
        //禁止窗口拖动和最大化
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //多线程并发构造
        try {
            CompletableFuture.allOf(
                    CompletableFuture.supplyAsync(() -> buildPicture(), Pool.pool)
                            .thenAccept((res) -> add(res, BorderLayout.NORTH)),
                    CompletableFuture.supplyAsync(() -> buildSpace(), Pool.pool)
                            .thenAccept((res) -> add(res, BorderLayout.CENTER)),
                    CompletableFuture.supplyAsync(() -> buildFeatures(), Pool.pool)
                            .thenAccept((res) -> add(res, BorderLayout.SOUTH))
            ).get(7, TimeUnit.SECONDS);
            this.pack();
            this.setLocation(getCenterPoint(null, null));
            this.setVisible(true);
        } catch (Exception e) {
            //主界面构造超时
            System.err.println("启动时界面出错，请重新启动!");
            e.printStackTrace();
            System.exit(1);
        }

    }

    /**
     * 创建图片显示框
     * @return 显示框组件
     */
    private PicWindow buildPicture() {
        picWindow  = (PicWindow)Builder.newBuilder(new PicWindow())
                .set("border",  BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED)))
                .set("preferredSize", new Dimension(picAndHomeWidth, picHeight))
                .build();
        return picWindow;
    }
    private JPanel buildSpace() {
        return (JPanel)Builder.newBuilder(new JPanel())
                .set("preferredSize",new Dimension(picAndHomeWidth, 0))
                .build();
    }
    /**
     * 创建功能区的组件
     * @return 功能区组件
     */
    private JPanel buildFeatures() {
        JTextField textField = buildInArea();
        //功能区
        JPanel feature = (JPanel)Builder.newBuilder(new JPanel())
                .add(null, buildButton("今日壁纸", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        picWindow.setText(Request.today());
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                    }
                }))
                .add(null, buildButton("随机壁纸", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        picWindow.setText(Request.random());

                    }
                }))
                .add(null, buildButton("设为壁纸", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!Util.setWallpaper(PATH, picWindow.getUrl())) {
                            //设置失败
                            MessageDialog.showMessageDialog("设置", "设置失败", MessageDialog.ERROR_MESSAGE);
                            return;
                        }
                        MessageDialog.showMessageDialog("设置", "设置成功", MessageDialog.SUCCESS_MESSAGE);

                    }
                }))
                .add(null, buildButton("保存壁纸", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!Util.downloadImg(PATH, picWindow.getUrl())) {
                            //下载失败
                            MessageDialog.showMessageDialog("保存", "保存失败", MessageDialog.ERROR_MESSAGE);
                            return;
                        }
                        MessageDialog.showMessageDialog("保存", "保存成功", MessageDialog.SUCCESS_MESSAGE);
                    }
                }))
                .set("layout", FLOW_LAYOUT)
                .set("border", BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED)))
                .build();
        return feature;
    }

    /**
     * 创建功能区的Button组件
     * @param name Button的名字
     * @param l 监听事件
     * @return Button组件
     */
    private JButton buildButton(String name, ActionListener l) {
        JButton jButton = new JButton(name);
        //对按钮属性统一设置
        jButton.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
        jButton.setPreferredSize(new Dimension(100, 25));
        jButton.setFocusPainted(false);
        jButton.addActionListener(l);
        return jButton;
    }

    private JTextField buildInArea() {
        //获取时间配置
        String value = PROPS.getProperty("time");
        //自定义时间输入框
        JTextField custom = new JTextField();
        custom.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        custom.setText(value);
        custom.setPreferredSize(new Dimension(50, 25));
        //焦点处理
        custom.setEnabled(false);
        custom.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                if (!custom.isEnabled()) {
                    custom.setEnabled(true);
                }
            }
        });
        custom.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                custom.setEnabled(false);
            }
        });
        return custom;
    }

    /**
     * 返回居中指针
     * @param component 相对的父组件
     * @param size 自身组件的size
     * @return 居中指针
     */
    Point getCenterPoint(@Nullable Component component, @Nonnull Dimension size) {
        if(Objects.isNull(component)) {
            int width = toolkit.getScreenSize().width;
            int height = toolkit.getScreenSize().height;
            return new Point(width/2 - getWidth()/2, height/2 - getHeight()/2);
        }
        int width = component.getX() + component.getWidth()/2 - size.width/2;
        int height = component.getY() + component.getHeight()/2 - size.width/2;
        return new Point(width, height);
    }
    /**
     * 单例类静态工厂
     */
    public static Home createBean(String title) {
        try {
            home = new Home(title);
        } catch (RuntimeException ex) {
            return null;
        }
        return home;
    }

    public PicWindow getPicWindow() {
        return picWindow;
    }

    public static int getPicAndHomeWidth() {
        return picAndHomeWidth;
    }

    public static int getPicHeight() {
        return picHeight;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
