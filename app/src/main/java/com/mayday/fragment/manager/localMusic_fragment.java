package com.mayday.fragment.manager;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
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
 * Created by xy-pc on 2017/3/28.
 */

public class localMusic_fragment extends Fragment{

    private Context mContext;

    private RecyclerView mRecylerView;

    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<MusicInfo> musicList;

    private MyAdapter adapter;

    private BaseActivity mainActivity;

    private MyApplication app;

    private Button refluse;


    //当fragment与Activity产生关联的时候调用
    @Override
    public void onAttach(Context context) {
        mainActivity= (BaseActivity) context;
        app= (MyApplication) getActivity().getApplication();
        super.onAttach(context);
    }

    @Override
    public void onResume() {
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
        super.onStart();
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.localmusic_fragment,container,false);
        mRecylerView= (RecyclerView) view.findViewById(R.id.localMusicRecyclerView);
        refluse=(Button)view.findViewById(R.id.refluse);
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

        refluse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaScannerConnection.scanFile(getActivity(), new String[] { Environment
                        .getExternalStorageDirectory().getAbsolutePath()}, null, null);
                initData();
            }
        });
        return view;
    }

    private void initData() {
        //这里每次都调用过了的啊，没道理不扫描吧！！！
        musicList= MusicmediaUtils.getMusicInfos(mContext);
        adapter=new MyAdapter(mContext,musicList);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mainActivity.mUnbindService();
        super.onDestroy();
    }


}
