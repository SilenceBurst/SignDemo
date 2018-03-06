package com.sign.injectdemo;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;

/**
 * Created by cys on 2018/3/1 0001.
 */

public class ViewInjectUtil {
    public static void inject(Object obj, View view) {
        Field[] fields = obj.getClass().getFields();
        for (Field field : fields) {
            field.setAccessible(true);
            //如果指定类型的注解存在于此元素上，则返回 true，否则返回 false。
            if (field.isAnnotationPresent(ViewInject.class)) {
                //该元素如果存在指定类型的注解，则返回这些注解，否则返回 null。
                ViewInject annotation = field.getAnnotation(ViewInject.class);
                int viewId = annotation.value();
                boolean canClick = annotation.click();
                try {
                    field.set(obj, view == null ? ((Activity) obj).findViewById(viewId) : view.findViewById(viewId));
                    if (canClick) {
                        if (view == null) {
                            ((Activity)obj).findViewById(viewId).setOnClickListener((View.OnClickListener) obj);
                        }else{
                            view.findViewById(viewId).setOnClickListener((View.OnClickListener) obj);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
