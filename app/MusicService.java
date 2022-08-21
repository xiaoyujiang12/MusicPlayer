package com.example.musicboxeditbymyself;

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

    MyReceiver serviceReceiver;
    AssetManager am;
    String[] musics = new String[] { "expect.mp3", "miss.mp3", "room.mp3","The next journey.mp3" };
    MediaPlayer mediaPlayer;
    int status = 0x11;
    int current = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        am = getAssets();

        serviceReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MainActivity.CTL_ACTION);
        registerReceiver(serviceReceiver,filter);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                current ++;
                if (current >= 3){current = 0;}
                Intent sendIntent = new Intent(MainActivity.UPDATE_ACTION);
                sendIntent.putExtra("current",current);
                sendBroadcast(sendIntent);
                prepareAndPlay(musics[current]);

            }
        });

    }

    public class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(final Context context, Intent intent) {

            int control = intent.getIntExtra("control",-1);

            switch (control){
                case 1:
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
                case 3:
                    // 如果原来正在播放或暂停
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
                //下一首
                case 4:
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

            Intent sendIntent = new Intent(MainActivity.UPDATE_ACTION);
            sendIntent.putExtra("update",status);
            sendIntent.putExtra("current",current);

            sendBroadcast(sendIntent);
        }
    }

    private void prepareAndPlay(String music){
        try {
            AssetFileDescriptor afd = am.openFd(music);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
            Log.v("hzf","音乐启动啦！");

        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
