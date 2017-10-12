package com.mayday.fragment.manager;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mayday.tool.localMusicManager.MDGridRvDividerDecoration;
import com.mayday.tool.localMusicManager.MusicInfo;
import com.mayday.tool.localMusicManager.MusicmediaUtils;
import com.mayday.tool.localMusicManager.MyAdapter;
import com.mayday.xy.customproject.BaseActivity;
import com.mayday.xy.customproject.MyApplication;
import com.mayday.xy.customproject.R;

import java.io.BufferedWriter;
import java.util.ArrayList;

/**
 * 当android的系统启动的时候，系统会自动扫描sdcard内的多媒体文件，并把获得的信息保存在一个系统数据库中，以后在其他程序中如果想要访问多媒体文件的信息，
 * 其实就是在这个数据库中进行的，而不是直接去sdcard中取
 * Created by xy-pc on 2017/6/19.
 */

public class localMusic_fragment extends Fragment{

    private Context mContext;

    private RecyclerView mRecylerView;

    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<MusicInfo> musicList;

    private MyAdapter adapter;

    private BaseActivity mainActivity;

    private MyApplication app;


    //当fragment与Activity产生关联的时候调用
    @Override
    public void onAttach(Context context) {
        //Fragement获取Activity的实例
        mainActivity= (BaseActivity) context;
        app= (MyApplication) getActivity().getApplication();
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        Log.i("monster", "onResume: ");
        super.onResume();
        mainActivity.mBindService();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();

    }

    @Override
    public void onStart() {
        Log.i("monster", "onStart: ");
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("monster", "onCreateView: ");
        View view=inflater.inflate(R.layout.localmusic_fragment,container,false);
        mRecylerView= (RecyclerView) view.findViewById(R.id.localMusicRecyclerView);
        initData();
        mLayoutManager=new GridLayoutManager(mContext,3, OrientationHelper.VERTICAL,false);
        mRecylerView.setLayoutManager(mLayoutManager);
        mRecylerView.setAdapter(adapter);
        mRecylerView.addItemDecoration(new MDGridRvDividerDecoration(mContext));
        adapter.setOnClickListener(new MyAdapter.OnClickListener() {
            @Override
            public void onClickListener(View view, int position) {
                //处理RecyclerView Item的点击事件
                mainActivity.playService.play(position);
                app.isYes=true;
                Toast.makeText(mContext,"--->>"+position ,Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void initData() {
        MediaScannerConnection.scanFile(getActivity(), new String[] { Environment
                .getExternalStorageDirectory().getAbsolutePath()}, null, null);
           Log.i("Ming", "initData: ");
        musicList= MusicmediaUtils.getMusicInfos(mContext);
        adapter=new MyAdapter(mContext,musicList);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("monster", "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("monster", "onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("monster", "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        mainActivity.mUnbindService();
        super.onDestroy();
    }

}
