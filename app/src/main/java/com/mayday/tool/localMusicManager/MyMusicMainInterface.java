package com.mayday.tool.localMusicManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mayday.ShowPlayMusic.PlayService;
import com.mayday.sql.music.LoveSqlDao;
import com.mayday.xy.customproject.BaseActivity;
import com.mayday.xy.customproject.MyApplication;
import com.mayday.xy.customproject.R;

import java.util.ArrayList;

/**
 * 播放音乐主界面
 * Created by xy-pc on 2017/5/22.
 */

public class MyMusicMainInterface extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private ImageView iv_back, iv_singer_album;
    private TextView tv_music_title, tv_music_singer;
    private SeekBar sb_music_progress;
    private ImageView iv_isLike, iv_lyric, iv_next, iv_play, iv_prev, play_mode;

    private int[] like = {R.drawable.global_btn_love_disable, R.drawable.global_btn_love_select};

    //开始/结束时间
    private TextView start_time, end_time;

    private MusicInfo musicInfo;
    private ArrayList<MusicInfo>musicInfos=null;
    private MyApplication app;

    //歌曲的总时间
    private String formatTime;
    //歌曲当前播放位置的时间


    //正在播放的歌曲id
    int pos;

    //创建我们的数据库(收藏音乐库)
    private LoveSqlDao loveSqlDao;


    int huidiao=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.music_main);
        musicInfos= MusicmediaUtils.getMusicInfos(this);
        app = (MyApplication) getApplication();
        //绑定服务
        mBindService();

        initView();

        initClick();

        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("progress_broadcast");
        registerReceiver(broadcastReceiver, intentFilter);

        loveSqlDao = new LoveSqlDao(MyMusicMainInterface.this);
    }

    /**
     * 当前播放的歌曲id
     *
     * @param position
     */
    @Override
    public void onPositions(int position) {

        this.pos = position;
        Log.i("chenxiaoxiao", "回调函数回调--------------->>"+(huidiao++)+"次");

        musicInfo = musicInfos.get(pos);
        sb_music_progress.setMax((int) musicInfo.getDuration());

        //将歌曲的总时间转换为我们的分/秒格式
        long musicLong = musicInfo.getDuration();
        formatTime = MusicmediaUtils.formatTime(musicLong);

        boolean isExist=loveSqlDao.queryIsExist(musicInfo.getTitle(),musicInfo.getSinger());

        if(isExist){
            iv_isLike.setImageResource(like[1]);
            musicInfo.setLove(true);
        }else {
            iv_isLike.setImageResource(like[0]);
            musicInfo.setLove(false);
        }
        Log.i("chenxiaoxiao", "--------------->>"+isExist);
        //显示专辑封面
//        showAlbumCover();
        //获取歌曲信息
        showTitleAndSinger();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (app.isYes) {
            iv_play.setImageResource(R.drawable.mv_pause_btn_pressed);
        }else {
            iv_play.setImageResource(R.drawable.mv_play_btn_pressed);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        //保存状态
        SharedPreferences.Editor editor=getSharedPreferences("playMode",MODE_PRIVATE).edit();
        editor.putInt("mode",playService.getPlayMode());
        editor.commit();
    }

    //重新回到该页面的时候，判断是否显示爱心

    @Override
    protected void onResume() {
        Log.i("chenxiaoxiao", "--------------------->>5onResume: ");
        SharedPreferences reconver=getSharedPreferences("playMode",MODE_PRIVATE);
        if(reconver!=null){
            int mode = reconver.getInt("mode", 1);
            if(mode==PlayService.LOOP_PLAY){
                play_mode.setImageResource(R.drawable.voice_fail);
            }else if(mode==PlayService.ONES_PLAY){
                play_mode.setImageResource(R.drawable.single_btn_selected);
            }else  if(mode==PlayService.RANDOM_PLAY){
                play_mode.setImageResource(R.drawable.shuffle_btn_selected);
            }else {
                System.out.println("不可能执行");
            }
        }
        super.onResume();
    }

    private void showTitleAndSinger() {
        tv_music_title.setText(musicInfo.getTitle());
        tv_music_singer.setText(musicInfo.getSinger());
        end_time.setText(formatTime);
    }

    /*private void showAlbumCover() {
        Bitmap bitmap = MusicmediaUtils.getArtwork(this, musicInfo.getId(), musicInfo.getAlbum_id(), true);
        if (bitmap != null) {
            iv_singer_album.setImageBitmap(bitmap);
        }
    }*/


    private void initClick() {
        iv_back.setOnClickListener(this);
        iv_next.setOnClickListener(this);
        iv_play.setOnClickListener(this);
        iv_prev.setOnClickListener(this);
        sb_music_progress.setOnSeekBarChangeListener(this);
        play_mode.setOnClickListener(this);
        iv_lyric.setOnClickListener(this);
        iv_isLike.setOnClickListener(this);
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_singer_album = (ImageView) findViewById(R.id.iv_singer_album);

        tv_music_title = (TextView) findViewById(R.id.tv_music_title);
        tv_music_singer = (TextView) findViewById(R.id.tv_music_singer);

        sb_music_progress = (SeekBar) findViewById(R.id.sb_music_progress);
        iv_isLike = (ImageView) findViewById(R.id.iv_isLike);
        iv_lyric = (ImageView) findViewById(R.id.iv_lyric);
        iv_next = (ImageView) findViewById(R.id.iv_next);
        iv_play = (ImageView) findViewById(R.id.iv_play);
        iv_prev = (ImageView) findViewById(R.id.iv_prev);
        play_mode = (ImageView) findViewById(R.id.play_mode);

        start_time = (TextView) findViewById(R.id.start_time);
        end_time = (TextView) findViewById(R.id.end_time);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //廣播接收器
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("progress_broadcast".equals(intent.getAction())) {
                int progress = intent.getIntExtra("MusicProgress", 0);

                sb_music_progress.setProgress(progress);
                String formatTime1 = MusicmediaUtils.formatTime(progress);
                start_time.setText(formatTime1);
                Log.d("Monster", "---->>" + progress + "sound");

                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                //当前时间
                editor.putString("formatTime1", formatTime1);
                editor.putInt("progress", progress);
                editor.commit();
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.iv_play:
                if (playService.isPlaying()) {
                    iv_play.setImageResource(R.drawable.mv_play_btn_pressed);
                    app.isYes=false;
                    playService.pause();
                    SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);
                    start_time.setText(data.getString("formatTime1", ""));
                    sb_music_progress.setProgress(data.getInt("progress", 0));
                } else if (playService.isPause()) {
                    iv_play.setImageResource(R.drawable.mv_pause_btn_pressed);
                    app.isYes=true;
                    playService.starts();
                }else{
                    //如果没有在上一个界面启动播放音乐，则当我们在该页面上点击播放按钮时就会从我们的第一首歌开始播放。
                    playService.play(0);
                    iv_play.setImageResource(R.drawable.mv_pause_btn_pressed);
                }
                break;

            case R.id.iv_next:
                playService.next();
                boolean isExist=loveSqlDao.queryIsExist(musicInfo.getTitle(),musicInfo.getSinger());
                if(musicInfo.isLove()&&isExist){
                    iv_isLike.setImageResource(like[1]);
                    musicInfo.setLove(true);
                }else {
                    iv_isLike.setImageResource(like[0]);
                    musicInfo.setLove(false);
                }
                break;

            case R.id.iv_prev:
                playService.prev();
                boolean isExist1=loveSqlDao.queryIsExist(musicInfo.getTitle(),musicInfo.getSinger());
                if(musicInfo.isLove()&&isExist1){
                    iv_isLike.setImageResource(like[1]);
                    musicInfo.setLove(true);
                }else {
                    iv_isLike.setImageResource(like[0]);
                    musicInfo.setLove(false);
                }
                break;

            case R.id.play_mode:
                switch (playService.getPlayMode()) {
                    case PlayService.LOOP_PLAY:
                        play_mode.setImageResource(R.drawable.shuffle_btn_selected);
                        playService.setPlayMode(PlayService.RANDOM_PLAY);
                        break;

                    case PlayService.RANDOM_PLAY:
                        play_mode.setImageResource(R.drawable.single_btn_selected);
                        playService.setPlayMode(PlayService.ONES_PLAY);
                        break;

                    case PlayService.ONES_PLAY:
                        play_mode.setImageResource(R.drawable.voice_fail);
                        playService.setPlayMode(PlayService.LOOP_PLAY);
                        break;

                }
                break;

            case R.id.iv_lyric:
                Intent intent = new Intent(this, ShowLyc.class);
//                musicInfos = MusicmediaUtils.getMusicInfos(this);
                intent.putExtra("musicTitle", musicInfos.get(pos).getTitle());
                intent.putExtra("musicSinger", musicInfos.get(pos).getSinger());
                startActivity(intent);
                break;

            case R.id.iv_isLike:
                if (!musicInfo.isLove()){
                    loveSqlDao.addMusicLove(musicInfo);
                    iv_isLike.setImageResource(like[1]);
//                    Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
                    musicInfo.setLove(true);
                    Toast.makeText(this,"------->>"+musicInfo.isLove(),Toast.LENGTH_SHORT).show();
                }else if(musicInfo.isLove()){
                    loveSqlDao.removeMusicLove(musicInfo.getTitle(), musicInfo.getSinger());
                    iv_isLike.setImageResource(like[0]);
//                    Toast.makeText(this, "已取消收藏", Toast.LENGTH_SHORT).show();
                    musicInfo.setLove(false);
                    Toast.makeText(this,"------->>"+musicInfo.isLove(),Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this,"不可能执行该行代码",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        mUnbindService();
        super.onDestroy();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (b) {
            playService.pause();
            playService.seekTo(i);
            playService.starts();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //开始拖拽的位置
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //结束拖拽的位置
    }
}
