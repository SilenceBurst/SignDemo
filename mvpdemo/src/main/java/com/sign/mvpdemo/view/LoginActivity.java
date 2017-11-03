package com.sign.mvpdemo.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sign.mvpdemo.MainActivity;
import com.sign.mvpdemo.R;
import com.sign.mvpdemo.presenter.LoginPresenter;

import static com.sign.mvpdemo.R.id.edt_pwd;

public class LoginActivity extends AppCompatActivity implements ILoginView ,View.OnClickListener{
    private EditText edtName, edtPwd;
    private Button btnLogin;
    private ProgressBar progressBar;

    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtName = (EditText) findViewById(R.id.edt_name);
        edtPwd = (EditText) findViewById(edt_pwd);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        presenter = new LoginPresenter(this);
    }

    @Override
    protected void onDestroy() {
        presenter.destroy();
        super.onDestroy();
    }

    @Override
    public void setNameError() {
        edtName.setError("用户名不能为空");
    }

    @Override
    public void setPwdError() {
        edtPwd.setError("密码不能为空");
    }

    @Override
    public void loginSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void showProgress(boolean whether) {
        if(whether){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                presenter.login(edtName.getText().toString(),edtPwd.getText().toString());
                break;
            default:
                break;
        }
    }
}
