package com.mayday.xy.customproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mayday.ShowPlayMusic.PlayService;
import com.mayday.fragment.manager.localMusic_fragment;
import com.mayday.fragment.manager.webView_fragment;
import com.mayday.fragment.manager.netMusic_fragment;
import com.mayday.fragment.manager.mv_fragment;
import com.mayday.tool.localMusicManager.MyLikeMusicInfo;
import com.mayday.tool.localMusicManager.MyMusicMainInterface;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_album, ll_picture, ll_download, ll_history;
    private ImageView iv_album, iv_picture, iv_download, iv_history;
    private TextView tv_album, tv_picture, tv_download, tv_history;
    private TextView tv_title;
    private Fragment Album_fragment, Picture_fragment, Download_fragment, History_fragment;

    private ImageView play_pluse;
    private ImageView Ilove;

    //要不要显示喜欢的音乐的图标
    int i=0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initFirebase();
        startService(new Intent(this, PlayService.class));
        //初始化控件
        initView();
        //初始化点击事件
        initEvent();
        //初始化并设置当前Fragment
        i = initFragment(0);
    }

    @Override
    public void onPositions(int position) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(i!=0){
            Ilove.setVisibility(View.GONE);
        }
    }

    /**
     * fragment重要方法
     * @param index 需要显示的fragment
     * @return
     */
    private int initFragment(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        //开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //隐藏所有Fragment
        hideFragment(transaction);
        switch (index) {
            //第一个Fragment界面
            case 0:
                if (Album_fragment == null) {
                    Album_fragment = new localMusic_fragment();
                    transaction.add(R.id.fl_content, Album_fragment);
                    Ilove.setVisibility(View.VISIBLE);
                } else {
                    transaction.show(Album_fragment);
                    Ilove.setVisibility(View.VISIBLE);
                }
                break;
            case 1:
                if (Picture_fragment == null) {
                    Picture_fragment = new mv_fragment();
                    transaction.add(R.id.fl_content, Picture_fragment);
                    Ilove.setVisibility(View.GONE);

                } else {
                    Ilove.setVisibility(View.GONE);
                    transaction.show(Picture_fragment);

                }
                break;
            case 2:
                if (Download_fragment == null) {
                    Download_fragment = new webView_fragment();
                    transaction.add(R.id.fl_content, Download_fragment);
                    Ilove.setVisibility(View.GONE);

                } else {
                    Ilove.setVisibility(View.GONE);
                    transaction.show(Download_fragment);

                }
                break;
            case 3:
                if (History_fragment == null) {
                    History_fragment = new netMusic_fragment();
                    transaction.add(R.id.fl_content, History_fragment);
                    Ilove.setVisibility(View.GONE);

                } else {
                    Ilove.setVisibility(View.GONE);
                    transaction.show(History_fragment);
                }
                break;
        }
        transaction.commit();
        return index;
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (Album_fragment != null) {
            transaction.hide(Album_fragment);
        }
        if (Picture_fragment != null) {
            transaction.hide(Picture_fragment);
        }
        if (Download_fragment != null) {
            transaction.hide(Download_fragment);
        }
        if (History_fragment != null) {
            transaction.hide(History_fragment);
        }

    }

    private void initEvent() {
        ll_album.setOnClickListener(this);
        ll_picture.setOnClickListener(this);
        ll_download.setOnClickListener(this);
        ll_history.setOnClickListener(this);
        play_pluse.setOnClickListener(this);
        Ilove.setOnClickListener(this);
    }

    private void initView() {
        ll_album = (LinearLayout) findViewById(R.id.ll_album);
        ll_picture = (LinearLayout) findViewById(R.id.ll_picture);
        ll_download = (LinearLayout) findViewById(R.id.ll_download);
        ll_history = (LinearLayout) findViewById(R.id.ll_history);

        iv_album = (ImageView) findViewById(R.id.iv_album);
        iv_picture = (ImageView) findViewById(R.id.iv_picture);
        iv_download = (ImageView) findViewById(R.id.iv_download);
        iv_history = (ImageView) findViewById(R.id.iv_history);

        tv_album = (TextView) findViewById(R.id.tv_album);
        tv_picture = (TextView) findViewById(R.id.tv_picture);
        tv_download = (TextView) findViewById(R.id.tv_download);
        tv_history = (TextView) findViewById(R.id.tv_history);

        tv_title = (TextView) findViewById(R.id.tv_title);

        play_pluse= (ImageView) findViewById(R.id.play_pluse);

        Ilove= (ImageView) findViewById(R.id.iv_Ilove);

    }

    @Override
    public void onClick(View view) {
        //在每次点击后将底部文字改为灰色
        restartBotton();
        switch (view.getId()) {
            case R.id.ll_album:
                tv_title.setText("本地音乐");
                iv_album.setImageResource(R.drawable.local_music_scan_end_pic);
                tv_album.setTextColor(0xFF1B940A);
                initFragment(0);
                break;

            case R.id.ll_picture:
                tv_picture.setTextColor(0xFF1B940A);
                tv_title.setText("MV");
                initFragment(1);
                break;

            case R.id.ll_download:
                tv_download.setTextColor(0xFF1B940A);
                tv_title.setText("在线歌曲");

                initFragment(2);
                break;

            case R.id.ll_history:
                tv_history.setTextColor(0xFF1B940A);
                tv_title.setText("网络搜索");
                initFragment(3);
                break;
            default:
                break;

            case R.id.play_pluse:
                startActivity(new Intent(this, MyMusicMainInterface.class));
                break;

            case R.id.iv_Ilove:
                startActivity(new Intent(this, MyLikeMusicInfo.class));

                break;
        }
    }

    private void restartBotton() {
        tv_album.setTextColor(0xffffffff);
        tv_picture.setTextColor(0xffffffff);
        tv_download.setTextColor(0xffffffff);
        tv_history.setTextColor(0xffffffff);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this,PlayService.class));
        //不处理当我们的应用被杀掉后再次进入到主播放界面时的播放状态了0-0
    }

}
