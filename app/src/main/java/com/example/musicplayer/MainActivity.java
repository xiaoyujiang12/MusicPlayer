package com.example.musicplayer;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView title , author;
    ImageButton play , stop, next;
    ActivityReceiver activityReceiver;

    public static final String CTL_ACTION = "org.crazyit.action.CTL_ACTION";
    public static final String UPDATE_ACTION = "org.crazyit.action.UPDATE_ACTION";
    int status = 0x11;   // 定义音乐的播放状态，0x11代表没有播放；0x12代表正在播放；0x13代表暂停
    String[] titleStrs = new String[] { "指望", "我怀念的", "房间" ,"下一段旅程"};
    String[] authorStrs = new String[] { "张杰", "孙燕姿", "刘瑞琦","张杰" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = findViewById(R.id.play);
        stop = findViewById(R.id.play);// 获取程序界面中的两个按钮
        next = findViewById(R.id.next);
        title = findViewById(R.id.songname);
        author = findViewById(R.id.maker);
        play.setOnClickListener(this);
        stop.setOnClickListener( this);// 为两个按钮的单击事件添加监听器
        next.setOnClickListener(this);
        activityReceiver = new ActivityReceiver();
        IntentFilter filter = new IntentFilter();// 创建IntentFilter
        filter.addAction(UPDATE_ACTION); // 指定BroadcastReceiver监听的Action
        registerReceiver(activityReceiver,filter); // 注册BroadcastReceiver
        Intent intent = new Intent(MainActivity.this,MusicService.class);
        ComponentName componentName = startService(intent);// 启动后台Service


    }

    public void onClick(View v) {
        Intent intent = new Intent("org.crazyit.action.CTL_ACTION");// 创建Intent
        switch (v.getId()) {
            case R.id.play:
                intent.putExtra("control", 1);
                break;
            // case R.id.stop:
            //    intent.putExtra("control", 2);
            //4.    break;
            case R.id.next:
                intent.putExtra("control", 4);
                break;
            case R.id.iv_menu:
                Intent intent2 = new Intent(this, Menu.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.back:
                Intent intent1 = new Intent(this, LoginActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.playlist:

            case R.id.tv_local_music:

        }
        sendBroadcast(intent); // 发送广播，将被Service组件中的BroadcastReceiver接收到
    }

    // 自定义的BroadcastReceiver，负责监听从Service传回来的广播
    public class ActivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int update = intent.getIntExtra("update",-1);// 获取Intent中的update消息,update代表播放状态
            int current = intent.getIntExtra("current", -1); // 攻取Intent中的current消息,current代表当前正在播放的歌曲
            if (current >= 0){
                title.setText(titleStrs[current]);
                author.setText(authorStrs[current]);
            }
            switch (update){
                case 0x11:
                    play.setImageResource(R.mipmap.ic_play_bar_btn_play);
                    status = 0x11;
                    break;
                case 0x12:
                    play.setImageResource(R.mipmap.ic_play_bar_btn_pause);// 控制系统进入播放状态,设置使用暂停图标
                    status = 0x12;
                    break;
                case 0x13:
                    play.setImageResource(R.mipmap.ic_play_bar_btn_play);
                    status = 0x13;
                    break;
            }
        }
    }
}
