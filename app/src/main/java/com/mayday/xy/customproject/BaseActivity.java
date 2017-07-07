package com.mayday.xy.customproject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mayday.ShowPlayMusic.PlayService;

/**
 * Created by xy-pc on 2017/5/22.
 */

public abstract class BaseActivity extends FragmentActivity {

    public PlayService playService = null;

    private Boolean isBind = false;

    private static final String TAG = AppCompatActivity.class.getSimpleName();

    public BaseActivity() {
        super();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.music_main);
    }


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            PlayService.MyBinder binder = (PlayService.MyBinder) iBinder;
            playService = binder.getBindsObject();
            isBind = true;
            //初始化回调接口
            musicPostion.onPosition(playService.currentMusic());
            playService.setMusciPosition(musicPostion);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG, componentName.toString());
            playService = null;
            isBind = false;
        }
    };

    public void mBindService() {
        if (!isBind) {
            Intent intent = new Intent(this, PlayService.class);
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            isBind = true;
        }
    }

    public void mUnbindService() {
        unbindService(serviceConnection);
        isBind = false;
    }

    private PlayService.MusicPostion musicPostion = new PlayService.MusicPostion() {
        @Override
        public void onPosition(int position) {
            onPositions(position);
        }
    };

    public abstract void onPositions(int position);

}
