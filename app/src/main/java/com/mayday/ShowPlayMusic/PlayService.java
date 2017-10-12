package com.mayday.ShowPlayMusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mayday.tool.localMusicManager.MusicInfo;
import com.mayday.tool.localMusicManager.MusicmediaUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 播放音乐核心服务类
 * Created by xy-pc on 2017/6/20.
 */

public class PlayService extends Service implements MediaPlayer.OnCompletionListener {

    //媒体播放类
    public MediaPlayer mPlay;
    //音乐文件
    private ArrayList<MusicInfo> musciInfo;
    //当前歌曲
    private int currentPosition;
    //音乐类
    MusicInfo musics;
    //是否暂停
    public boolean isPause = false;

    //循环播放
    public static final int LOOP_PLAY = 1;
    //随机播放
    public static final int RANDOM_PLAY = 2;
    //单曲播放
    public static final int ONES_PLAY = 3;

    //播放模式
    public int play_mode = LOOP_PLAY;

    public MusicPostion musicPostion;

    private Timer mTimer = new Timer();

    //拖动进度条后的播放位置
    public int seek;

    //Activity/Fragment调用getBindsObject()来绑定Service
    public class MyBinder extends Binder {
        public PlayService getBindsObject() {
            return PlayService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlay = new MediaPlayer();
        musciInfo = MusicmediaUtils.getMusicInfos(this);
        mPlay.setOnCompletionListener(this);
    }

    //播放的三种模式
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Random random = new Random();
        switch (play_mode) {
            case LOOP_PLAY:
                next();
                break;

            case RANDOM_PLAY:
                play(random.nextInt() * musciInfo.size());
                break;

            case ONES_PLAY:
                play(currentPosition);
                break;
        }
    }

    //播放音乐(从头开始播放)
    public void play(int position) {
        if (position < 0 && position >= musciInfo.size()) {
            musics = musciInfo.get(0);
        }
        musics = musciInfo.get(position);
        try {
            //MediaPalyer核心api
            mPlay.reset();
            mPlay.setDataSource(this, Uri.parse(musics.getUrl()));
            mPlay.prepare();
            mPlay.start();
            currentPosition = position;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (musicPostion != null) {
            musicPostion.onPosition(currentPosition);
        }

//        if(mPlay!=null&&isPause==false){
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                int progress = getCurrentProgress();
                //發送廣播到主綫程中
                Intent intent = new Intent("progress_broadcast");

                intent.putExtra("MusicProgress", progress);
                PlayService.this.sendBroadcast(intent);
            }

        }, 1000, 1000);
//        }
    }

    //播放音乐(从暂停的时候开始播放)
    //TODO this is my first todo// TODO: 2017/5/20
    public void starts() {
        if (mPlay != null && !mPlay.isPlaying()) {
            mPlay.start();
            isPause = false;
        }
    }

    //暂停音乐
    public void pause() {
        if (mPlay.isPlaying()) {
            //暂停
            mPlay.pause();
            isPause = true;
        }
    }

    //上一首
    public void prev() {
        if (currentPosition - 1 < 0) {
            currentPosition = musciInfo.size() - 1;
        } else {
            currentPosition--;
        }
        play(currentPosition);
    }

    //下一首
    public void next() {
        if (currentPosition + 1 >= musciInfo.size()) {
            currentPosition = 0;
        } else {
            currentPosition++;
        }
        play(currentPosition);
    }

    //是否播放
    public boolean isPlaying() {
        return mPlay.isPlaying();
    }

    //是否暂停
    public boolean isPause() {
        return isPause;
    }

    //当前歌曲的进度值
    public int getCurrentProgress() {
        if (mPlay != null) {
            return mPlay.getCurrentPosition();
        }
        return 0;
    }

    //获取到当前播放的歌曲
    public int currentMusic() {
        return currentPosition;
    }

    //提供给UI的设置播放模式
    public void setPlayMode(int playMode) {
        this.play_mode = playMode;
    }

    //得到当前的播放模式
    public int getPlayMode() {
        return play_mode;
    }

    //指定播放位置
    public void seekTo(int seek) {
        this.seek = seek;
        mPlay.seekTo(seek);
    }

    public interface MusicPostion {
        void onPosition(int position);
    }


    public void setMusciPosition(MusicPostion musciPosition) {
        this.musicPostion = musciPosition;
    }

}
