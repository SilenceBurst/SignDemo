package com.sign.jdbcdemo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private TextView tvResult;
    private Runnable runnable;
    private Button btnSearch;
    private EditText edtKey;

    private Handler myHandler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            tvResult.setText(data.get("result").toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        initRunnable();
    }

    private void initRunnable() {
        runnable = new Runnable() {
            private Connection con = null;

            @Override
            public void run() {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    //122.112.226.122为ip地址
                    //3306为端口号，sun_graduation为数据库名称，第二个test为数据库登录账户，密码test1234
                    con = DriverManager.getConnection("jdbc:mysql://122.112.226.122:3306/sun_graduation?useUnicode=true&characterEncoding=gbk", "test", "test1234");
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    //搜索
                    startSearch(con);
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
            }

            public void startSearch(Connection con) throws SQLException {
                try {
                    //查询表名为“xhzd_surnfu”的列zi值为mResult的列为pinyin的值
                    String sql = "select * from xhzd_surnfu where zi = '" + edtKey.getText().toString() + "'";
//                    String sql = "select pinyin from xhzd_surnfu where id = 1";
                    Statement stmt = con.createStatement();//创建Statement
                    ResultSet rs = stmt.executeQuery(sql);//ResultSet类似Cursor
                    StringBuilder sbResult = new StringBuilder();
                    //ResultSet最初指向第一行
                    while (rs.next()) {
                        if (!TextUtils.isEmpty(rs.getString("pinyin"))) {
                            sbResult.append("拼音：").append(rs.getString("pinyin")).append("\n");
                        }
                        if (!TextUtils.isEmpty(rs.getString("bushou"))) {
                            sbResult.append("部首：").append(rs.getString("bushou")).append("\n");
                        }
                        if (!TextUtils.isEmpty(rs.getString("bihua"))) {
                            sbResult.append("笔画：").append(rs.getString("bihua")).append("\n");
                        }
                        if (!TextUtils.isEmpty(rs.getString("xiangjie"))) {
                            sbResult.append("详解：").append(rs.getString("xiangjie")).append("\n");
                        }
                    }
                    if (sbResult.length() == 0) {
                        sbResult.append("搜索结果为空");
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("result", sbResult.toString());
                    Message msg = new Message();
                    msg.setData(bundle);
                    myHandler.sendMessage(msg);
                    rs.close();
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null)
                        try {
                            con.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_search) {
            if (TextUtils.isEmpty(edtKey.getText().toString())) {
                Toast.makeText(mContext, "请输入要查询的字", Toast.LENGTH_LONG).show();
                return;
            }
            //查询要在子线程中
            new Thread(runnable).start();
        }
    }

    private void initView() {
        mContext = this;
        edtKey = findViewById(R.id.edt_key);
        btnSearch = findViewById(R.id.btn_search);
        tvResult = findViewById(R.id.tv_result);
        btnSearch.setOnClickListener(this);
    }
}
