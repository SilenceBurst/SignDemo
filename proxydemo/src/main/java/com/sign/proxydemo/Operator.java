package com.sign.proxydemo;

//场景操作
public class Operator {

    public static void main(String args[]) {
        //静态代理
//        Boss boss = new Boss();
//        Secretary secretary = new Secretary(boss);
//        secretary.meeting();
//        secretary.eat();


        //动态代理
//        Boss dynamicBoss = new Boss();
//        InvocationHandler handler = new DynamicProXY(dynamicBoss);
//        IBehavior behavior = (IBehavior) Proxy.newProxyInstance(dynamicBoss.getClass().getClassLoader(), dynamicBoss.getClass().getInterfaces(), handler);
//        System.out.println("behavior.getClass()" + behavior.getClass());
//        behavior.meeting();
//        behavior.eat();

        //cglib动态代理
        CGLibProXY cgLibProXY = new CGLibProXY();
        Boss boss = (Boss) cgLibProXY.getInstance(new Boss());
        System.out.println("boss.getClass()" + boss.getClass());
        boss.meeting();
        boss.eat();
    }
}
