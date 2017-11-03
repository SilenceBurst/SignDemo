package com.sign.signview.utils;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.sign.signview.R;

/*
 * 项目名:    SignView
 * 包名       com.sign.signview.utils
 * 文件名:    ToolbarUtil
 * 创建者:    CYS
 * 创建时间:  2017/8/29 0029 on 9:29
 * 描述:     TODO
 */
public class ToolbarUtil {
    public static void initToolbar(final AppCompatActivity activity, boolean isBack, String title) {
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.tool_bar);
        toolbar.setTitle(title);
        activity.setSupportActionBar(toolbar);
        if (isBack) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
        }
    }
}
