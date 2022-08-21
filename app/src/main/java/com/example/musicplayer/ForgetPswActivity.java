package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgetPswActivity extends AppCompatActivity implements View.OnClickListener{
    private DBOpenHelper mDBOpenHelper;
    private Button mBtRegisteractivitySure;
    private EditText mEtRegisteractivityUsername;
    private EditText mEtRegisteractivityPassword1;
    private EditText mEtRegisteractivityPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_psw);
        initView();
        mDBOpenHelper = new DBOpenHelper(this);
    }

    private void initView() {
        mBtRegisteractivitySure = findViewById(R.id.sure);
        mEtRegisteractivityUsername = findViewById(R.id.username);
        mEtRegisteractivityPassword1 = findViewById(R.id.psw);
        mEtRegisteractivityPassword2 = findViewById(R.id.psw1);
        mBtRegisteractivitySure.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_login1:
                Intent intent1 = new Intent(this, LoginActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.sure:    //确认按钮
                //获取用户输入的用户名、密码
                String username = mEtRegisteractivityUsername.getText().toString().trim();
                String password1 = mEtRegisteractivityPassword1.getText().toString().trim();
                String password2 = mEtRegisteractivityPassword2.getText().toString().trim();
                //确认验证
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(ForgetPswActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(password1)){
                    Toast.makeText(ForgetPswActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(password2)){
                    Toast.makeText(ForgetPswActivity.this, "请再次输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!password1.equals(password2)){
                    Toast.makeText(ForgetPswActivity.this, "输入两次的密码不一样", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Toast.makeText(ForgetPswActivity.this, "密码重置成功", Toast.LENGTH_SHORT).show();
                    mDBOpenHelper.add(username, password2);
                    Intent intent2 = new Intent(this, MainActivity.class);
                    startActivity(intent2);
                    finish();
                }
        }
    }
}
