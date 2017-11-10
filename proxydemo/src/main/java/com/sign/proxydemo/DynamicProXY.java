package com.sign.proxydemo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

//动态代理
public class DynamicProXY implements InvocationHandler {
    private Object obj;

    public DynamicProXY(Object obj) {
        this.obj = obj;
    }

    /**
     * @param proxy  生成的动态代理对象
     * @param method 动态代理对象的某个方法的method对象（即我们委托给代理的行为）
     * @param args   调用某个方法的参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("proxy.getClass()" + proxy.getClass());
        System.out.println("obj.getClass()" + obj.getClass());
        Object result = null;
        if (method.getName().equals("meeting")) {
            //开会前
            System.out.println("召集相关人员，整理会议室");
            //开会
            result = method.invoke(this.obj, args);
            //开会后
            System.out.println("保存会议记录，打扫会议室");
        } else if ("eat".equals(method.getName())) {
            //吃饭前
            System.out.println("定饭馆，订菜，通知、招待客户");
            //定好了，人齐了
            result = method.invoke(this.obj, args);
            //吃饭后
            System.out.println("安排客户住宿，送客户");
        }
        return result;
    }
}
