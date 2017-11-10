package com.sign.proxydemo;

//秘书
public class Secretary implements IBehavior {
    private Boss boss;

    public Secretary(Boss boss) {
        this.boss = boss;
    }

    @Override
    public void meeting() {
        //开会前
        System.out.println("召集相关人员，整理会议室");
        //人齐了，收拾好了，叫老板开会
        boss.meeting();
        //开会后
        System.out.println("保存会议记录，打扫会议室");
    }

    @Override
    public void eat() {
        //吃饭前
        System.out.println("定饭馆，订菜，通知、招待客户");
        //定好了，人齐了
        boss.eat();
        //吃饭后
        System.out.println("安排客户住宿，送客户");
    }
}
