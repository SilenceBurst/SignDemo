package com.sign.signview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.sign.signview.R;
import com.sign.signview.utils.ToolbarUtil;
import com.sign.signview.view.GuaGuaView;

public class GuaGuaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gua_gua);
        ToolbarUtil.initToolbar(this, true, "刮刮卡");
        GuaGuaView guaGuaView = (GuaGuaView) findViewById(R.id.guagua);
        guaGuaView.setOnGuaGuaCompleteListener(new GuaGuaView.OnGuaGuaCompleteListener() {
            @Override
            public void onComplete() {
                Toast.makeText(GuaGuaActivity.this, "恭喜你，没中奖!", Toast.LENGTH_SHORT).show();
            }
        });
        guaGuaView.setText("刮刮卡终于结束了");
    }
}
