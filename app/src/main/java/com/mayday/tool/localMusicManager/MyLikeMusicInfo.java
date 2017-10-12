package com.mayday.tool.localMusicManager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.mayday.sql.music.LoveSqlDao;
import com.mayday.xy.customproject.BaseActivity;
import com.mayday.xy.customproject.R;

import java.util.List;

/**
 * Created by xy-pc on 2017/6/26.
 */

public class MyLikeMusicInfo extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private RelativeLayout no_like;
    private ImageView iv_noLike;
    private MyLikeAdapter adapter;
    private List<MusicInfo> list;
    private LoveSqlDao loveSqlDao;
    //当前歌曲的id(喜爱歌曲的id)

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.like_music);
        listView = (ListView) findViewById(R.id.lv_likelistview);
        no_like = (RelativeLayout) findViewById(R.id.no_like);
        iv_noLike = (ImageView) findViewById(R.id.iv_noLike);
        loveSqlDao = new LoveSqlDao(MyLikeMusicInfo.this);

        list = loveSqlDao.queryMusicLove();
        Log.i("xiaoxiao", "------------->>" + list.size() + "个喜爱歌曲");
        //判断是否有收藏的音乐，若没有则提醒用户添加音乐
        if (list.size() == 0) {
            iv_noLike.setVisibility(View.VISIBLE);
            no_like.setVisibility(View.VISIBLE);
        } else {
            iv_noLike.setVisibility(View.GONE);
            no_like.setVisibility(View.GONE);
        }
        adapter = new MyLikeAdapter(this, list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mBindService();
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onPositions(int position) {
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        playService.play(i);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mUnbindService();
    }
}
