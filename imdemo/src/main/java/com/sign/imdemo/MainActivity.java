package com.sign.imdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.SavePowerConfig;
import com.qiyukf.unicorn.api.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.UICustomization;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.UnreadCountChangeListener;
import com.qiyukf.unicorn.api.YSFOptions;
import com.qiyukf.unicorn.api.YSFUserInfo;

public class MainActivity extends AppCompatActivity {

    private TextView tvNumber;
    private TextView tvCustomerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvCustomerService = findViewById(R.id.tv_customer_service);

        YSFUserInfo userInfo = new YSFUserInfo();
        //关联用户资料
        userInfo.userId = "uid";
        userInfo.authToken = "auth-token-from-user-server";
        userInfo.data = "[{'key':'real_name','value':'sign'},{'key':'avatar','value':'http://hjtech.wicp.net:8101/jej-api/upload/file/file-20171107112251.png'}]";
        Unicorn.setUserInfo(userInfo);

        //未读消息数量
        Unicorn.addUnreadCountChangeListener(mUnreadCountListener, true);
        updateUnreadCount(Unicorn.getUnreadCount());

        //跳转至聊天界面
        initListener();

        //设置客户端用户头像
        YSFOptions options = new YSFOptions();
        options.uiCustomization = new UICustomization();
        options.uiCustomization.rightAvatar = "http://hjtech.wicp.net:8101/jej-api/upload/file/file-20171107112251.png";

        options.statusBarNotificationConfig = new StatusBarNotificationConfig();
        options.savePowerConfig = new SavePowerConfig();
        options.savePowerConfig.customPush = true;
        Unicorn.updateOptions(options);
    }

    private void initListener() {
        final String title = "我是标题";
        String consultUrl = "https://www.baidu.com/";
        String consultTitle = "我是访客来源";
        String consultExtra = "我是额外信息";
        /**
         * 设置访客来源，标识访客是从哪个页面发起咨询的，用于客服了解用户是从什么页面进入。
         * 三个参数分别为：来源页面的url，来源页面标题，来源页面额外信息（可自由定义）。
         * 设置来源后，在客服会话界面的"用户资料"栏的页面项，可以看到这里设置的值。
         */
        final ConsultSource source = new ConsultSource(consultUrl, consultTitle, consultExtra);

        tvCustomerService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 请注意： 调用该接口前，应先检查Unicorn.isServiceAvailable()，
                 * 如果返回为false，该接口不会有任何动作
                 *
                 * @param context 上下文
                 * @param title   聊天窗口的标题
                 * @param source  咨询的发起来源，包括发起咨询的url，title，描述信息等
                 */
                Unicorn.openServiceActivity(MainActivity.this, title, source);
            }
        });
    }

    // 添加未读数变化监听，add 为 true 是添加，为 false 是撤销监听。
    // 退出界面时，必须撤销，以免造成资源泄露
    private UnreadCountChangeListener mUnreadCountListener = new UnreadCountChangeListener() { // 声明一个成员变量
        @Override
        public void onUnreadCountChange(int count) {
            // 在此更新界面, count 为当前未读数，
            // 也可以用 Unicorn.getUnreadCount() 获取总的未读数
            updateUnreadCount(count);
        }
    };

    private void updateUnreadCount(int count) {
        if (count > 99) {
            tvCustomerService.setText("联系客服" + "(99+)");
        } else if (count > 0) {
            tvCustomerService.setText("联系客服" + "(" + count + ")");
        } else {
            tvCustomerService.setText("联系客服");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出登录
        Unicorn.logout();
        //移除未读消息监听
        Unicorn.addUnreadCountChangeListener(mUnreadCountListener, false);
    }
}
