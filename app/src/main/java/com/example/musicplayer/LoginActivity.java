package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    //创建数据表
    private DBOpenHelper mDBOpenHelper;
    private TextView mTvLoginactivityRegister;
    private EditText mEtLoginactivityUsername;
    private EditText mEtLoginactivityPassword;
    private Button mBtLoginactivityLogin;
    //重写 onCreate() 方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//保存实例状态
        setContentView(R.layout.login);//设置视图内容的配置文件
        initView();
        mDBOpenHelper = new DBOpenHelper(this);
    }

    private void initView() {
        // 初始化控件
        mBtLoginactivityLogin = findViewById(R.id.login);
        mTvLoginactivityRegister = findViewById(R.id.register);
        mEtLoginactivityUsername = findViewById(R.id.username);
        mEtLoginactivityPassword = findViewById(R.id.psw);
        // 设置点击事件监听器
        mBtLoginactivityLogin.setOnClickListener(this);
        mTvLoginactivityRegister.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            // 跳转到注册界面
            case R.id.register:
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            //跳转到忘记密码页面
            case R.id.find_psw:
                startActivity(new Intent(this,ForgetPswActivity.class));
                finish();
                break;
            //登录
            case R.id.login:
                String name = mEtLoginactivityUsername.getText().toString().trim();
                String password = mEtLoginactivityPassword.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {//进行匹配验证,先判断用户名密码是否为空
                    ArrayList<User> data = mDBOpenHelper.getAllData();
                    boolean match = false;
                    for (int i = 0; i < data.size(); i++) {//判断是否与数据库中的数据相匹配
                        User user = data.get(i);
                        if (name.equals(user.getName()) && password.equals(user.getPassword())) {
                            match = true;
                            break;
                        } else {
                            match = false;
                        }
                    }
                    if (match) {
                        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();//登录成功之后，进行页面跳转：
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();//销毁此Activity
                    } else {
                        Toast.makeText(this, "用户名或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请输入你的用户名或密码", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
