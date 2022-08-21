package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private String realCode;
    private DBOpenHelper mDBOpenHelper;
    private Button mBtRegisteractivityRegister;
    private EditText mEtRegisteractivityUsername;
    private EditText mEtRegisteractivityPassword1;
    private EditText mEtRegisteractivityPassword2;
    private EditText mEtRegisteractivityPhonecodes;
    private ImageView mIvRegisteractivityShowcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initView();
        mDBOpenHelper = new DBOpenHelper(this);
        //将验证码用图片的形式显示出来
        mIvRegisteractivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
    }

    private void initView(){
        mBtRegisteractivityRegister = findViewById(R.id.register);
        mEtRegisteractivityUsername = findViewById(R.id.username);
        mEtRegisteractivityPassword1 = findViewById(R.id.psw);
        mEtRegisteractivityPassword2 = findViewById(R.id.psw1);
        mEtRegisteractivityPhonecodes = findViewById(R.id.Codes);
        mIvRegisteractivityShowcode = findViewById(R.id.showCode);
        mIvRegisteractivityShowcode.setOnClickListener(this);
        mBtRegisteractivityRegister.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.showCode:    //改变随机验证码的生成
                mIvRegisteractivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
                break;
            case R.id.back_login:
                Intent intent1 = new Intent(this, LoginActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.register:    //注册按钮
                //获取用户输入的用户名、密码、验证码
                String username = mEtRegisteractivityUsername.getText().toString().trim();
                String password = mEtRegisteractivityPassword2.getText().toString().trim();
                String phoneCode = mEtRegisteractivityPhonecodes.getText().toString().toLowerCase();
                //注册验证
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(phoneCode) ) {
                    if (phoneCode.equals(realCode)) {
                        //将用户名和密码加入到数据库中
                        mDBOpenHelper.add(username, password);
                        Intent intent2 = new Intent(this, MainActivity.class);
                        startActivity(intent2);
                        finish();
                        Toast.makeText(this,  "验证通过，注册成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "验证码错误,注册失败", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "未完善信息，注册失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
