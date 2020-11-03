package ziy.graphical.builder;


import org.springframework.beans.factory.config.MethodInvokingFactoryBean;

import java.util.Objects;

/**
 * @author ziy
 * @version 1.0
 * @date 8:38 2020/10/28
 * @description TODO:提供构建者模式对图形化界面的程序编写提供便捷
 * @className Builder
 */
public class Builder extends MethodInvokingFactoryBean {
    /**
     * 构建对象
     */
    private Object component;
    /**
     *
     * @param component
     */
    private Builder(Object component) {
        this.component = component;
        this.setTargetObject(component);
        this.setTargetClass(component.getClass());
    }

    /**
     * 执行指定的方法
     * @param name 方法名
     * @param args 方法入参
     * @return 构建者对象
     * @throws Exception
     */
    public Builder execute(String name, Object... args) {
        executeMethod(name, args);
        return this;
    }

    /**
     * 执行set系列方法
     * @param postName 去掉set并首字母小写的方法名
     * @param args 方法入参
     * @return 构建者对象
     * @throws Exception
     */
    public Builder set(String postName, Object... args) {
        String methodName = "set";
        if(Objects.nonNull(postName)) {
            methodName += parseString(postName);
        }
        executeMethod(methodName, args);
        return this;
    }

    /**
     * 执行add系列方法
     * @param args
     * @return
     */
    public Builder add(String postName, Object... args) {
        String methodName = "add";
        if(Objects.nonNull(postName)) {
            methodName += parseString(postName);
        }
        executeMethod(methodName, args);
        return this;
    }

    /**
     * 执行方法
     * @param methodName 方法名
     * @param args 方法入参
     * @return 执行状态
     */
    private void executeMethod(String methodName, Object[] args) {
        try{
            this.setTargetMethod(methodName);
            this.setArguments(args);
            this.prepare();
            this.invoke();
        } catch (Exception e) {
            System.err.println(component.getClass().getTypeName() +"."+ methodName + "."+ args +" ==> "+ "失败");
            e.printStackTrace();
        }
    }
    private static String parseString(String name) {
        if(name.equals("") || name.replaceAll(" ", "").equals("")) {
            return "";
        }
        return name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
    }
    public Object build() {
        return component;
    }
    public static Builder newBuilder(Object component) {
        return new Builder(component);
    }
}
