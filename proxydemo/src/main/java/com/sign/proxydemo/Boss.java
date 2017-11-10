package com.sign.proxydemo;

//老板
public class Boss implements IBehavior {

    @Override
    public void meeting() {
        System.out.println("我是老板，我只管开会");
    }

    @Override
    public void eat() {
        System.out.println("我是老板，我只管吃饭");
    }
}
