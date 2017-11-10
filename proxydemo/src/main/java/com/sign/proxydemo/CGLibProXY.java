package com.sign.proxydemo;


import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CGLibProXY implements MethodInterceptor {

    private Object target;

    public Object getInstance(Object target) {
        this.target = target;
        Enhancer enhancer = new Enhancer();
        //设置父类
        enhancer.setSuperclass(this.target.getClass());
        //设置回调
        enhancer.setCallback(this);
        //创建代理对象
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("o.getClass()" + o.getClass());

        Object result = null;
        if (method.getName().equals("meeting")) {
            //开会前
            System.out.println("召集相关人员，整理会议室");
            //开会
            result = methodProxy.invokeSuper(o, objects);
//            result = method.invoke(target, objects);
            //开会后
            System.out.println("保存会议记录，打扫会议室");
        } else if ("eat".equals(method.getName())) {
            //吃饭前
            System.out.println("定饭馆，订菜，通知、招待客户");
            //定好了，人齐了
            result = methodProxy.invokeSuper(o, objects);
//            result = method.invoke(target, objects);
            //吃饭后
            System.out.println("安排客户住宿，送客户");
        }
        return result;
    }
}
