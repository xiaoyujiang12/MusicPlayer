package com.example.musicplayer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import java.io.IOException;

public class MusicService extends Service {
    MyReceiver serviceReceiver;//广播
    AssetManager am;
    String[] musics = new String[] { "expect.mp3", "miss.mp3", "room.mp3" ,"The next journey.mp3"};
    MediaPlayer mediaPlayer;
    int status = 0x11;// 当前的状态,0x11代表没有播放；0x12代表正在播放；0x13代表暂停
    int current = 0; // 记录当前正在播放的音乐
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        am = getAssets();//获取assets文件夹里的所有歌曲
        serviceReceiver = new MyReceiver();// 创建具有筛选功能的服务端接收器
        IntentFilter filter = new IntentFilter(); // 创建IntentFilter
        filter.addAction(MainActivity.CTL_ACTION);
        registerReceiver(serviceReceiver,filter);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // 为MediaPlayer播放完成事件绑定监听器
            @Override
            public void onCompletion(MediaPlayer mp) {
                current ++;
                if (current >= 3){current = 0;}
                Intent sendIntent = new Intent(MainActivity.UPDATE_ACTION);
                sendIntent.putExtra("current",current);// 发送广播通知Activity更改文本框
                sendBroadcast(sendIntent);// 发送广播通知Activity更改文本框
                prepareAndPlay(musics[current]); // 准备、并播放音乐
            }
        });
    }


    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            int control = intent.getIntExtra("control",-1);//接收发送来的广播数据
            switch (control){
                case 1: // 播放或暂停,判断上一次的状态
                    if (status == 0x11){
                        prepareAndPlay(musics[current]);
                        status = 0x12;
                    }
                    else if (status == 0x12){
                        mediaPlayer.pause();
                        status = 0x13;
                    }
                    else if (status == 0x13){
                        mediaPlayer.start();
                        status = 0x12;
                    }
                    break;
                case 2:
                    if (status == 0x12 || status == 0x13){
                        mediaPlayer.stop();
                        status = 0x11;
                    }
                case 3:// 如果原来正在播放或暂停
                    if (status == 0x12 || status == 0x13) {
                        mediaPlayer.stop();
                        if (current - 1 < 0) {
                            current = musics.length - 1;
                        } else {
                            current--;
                        }
                        prepareAndPlay(musics[current]);
                        status = 0x12;
                    }
                    break;
                case 4://下一首
                    if (status == 0x12 || status == 0x13) {
                        mediaPlayer.stop();
                        if (current + 1 >= musics.length) {
                            current = 0;
                        } else {
                            current++;
                        }
                        prepareAndPlay(musics[current]);
                        status = 0x12;
                    }
                    break;
            }
            Intent sendIntent = new Intent(MainActivity.UPDATE_ACTION); // 发送广播通知Activity更改图标、文本框
            sendIntent.putExtra("update",status);
            sendIntent.putExtra("current",current);
            sendBroadcast(sendIntent);// 发送广播 ，将被Activity中的BroadcastReceiver接收到
        }
    }

    private void prepareAndPlay(String music){
        try {
            AssetFileDescriptor afd = am.openFd(music); // 打开指定音乐文件
            mediaPlayer.reset();
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());  // 使用MediaPlayer加载指定的声音文件。
            mediaPlayer.prepare();// 准备声音
            mediaPlayer.start(); // 播放
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}

