package com.mayday.fragment.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.mayday.mv.tool.MDStaggeredRvDividerDecoration;
import com.mayday.mv.tool.MyAdapter;
import com.mayday.mv.tool.VideoActivity;
import com.mayday.mv_until.Video;
import com.mayday.mv_until.video_information;
import com.mayday.xy.customproject.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by xy-pc on 2017/5/28.
 */

public class mv_fragment extends Fragment {

    private RollPagerView pagerView;

    private RecyclerView mRecycler_view;

    private RecyclerView.LayoutManager mLayoutManager;

    private View view = null;

    private MyAdapter adapter;

    private static video_information information = new video_information();

    private int[] image = {R.drawable.top1, R.drawable.top2, R.drawable.top3, R.drawable.top4};



    private static final String top1="http://183.60.197.26/5/r/f/e/v/rfevndlwbicbmyxqfogsoltgvvocuz/he.yinyuetai.com/D4D3015C3937A6CB35C4F5F673041F52.mp4";
    private static final String top2="http://220.170.49.111/5/a/e/s/h/aeshsaealmixdemeshtzsxljwrurhv/he.yinyuetai.com/9E7C0157B7720739A7141013A88C537E.flv";
    private static final String top3="http://220.170.49.110/9/z/m/v/a/zmvaofkrrmkezvalmqgioqqbmsgngl/he.yinyuetai.com/08A801560DE2E6255A05CCED4D97CC19.flv";
    private static final String top4="http://220.170.49.111/2/d/h/w/j/dhwjkikmjjmxdgirfqzkjeqahfhyvk/he.yinyuetai.com/6ACF015575E7122CF7D481B15D7B9DD5.flv";

    private String [] top={top1,top2,top3,top4};

    public  static final String[] video_title = {information.zjycj_title, information.tthg_title, information.hh_title, information.sfy_title, information.rym_title, information.cmzw_title, information.jj_title, information.mayday0329_title, information.maydayAnd831_title};
    public  static final String[] video_url = {information.zjycj_url, information.tthg_url, information.hh_url, information.sfy_url, information.rym_url, information.cmzw_url, information.jj_url, information.mayday0329_url, information.MaydayAnd831_url};
    public  static final int [] images={R.drawable.zmycj,R.drawable.tthg,R.drawable.hh,R.drawable.sfy,R.drawable.rym,R.drawable.cmzw,R.drawable.jj,
    R.drawable.mayday0329,R.drawable.glgl};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mv_fragment, container, false);
        pagerView = (RollPagerView) view.findViewById(R.id.rollPagerView);
        mRecycler_view = (RecyclerView) view.findViewById(R.id.mRecycler_view);
        initDate();
        initView();

        //设置播放时间间隔
        pagerView.setPlayDelay(1000);
        //设置透明度
        pagerView.setAnimationDurtion(500);

        pagerView.setAdapter(new MyPagerAdapter());

        pagerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //viewPager的点击事件
                Toast.makeText(getActivity(), "当前的viewpager---------->>" + position, Toast.LENGTH_SHORT).show();
                //点击进入相应的url地址
                Intent intent=new Intent(getActivity(),VideoActivity.class);
                intent.putExtra("url",top[position]);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initView() {
        mRecycler_view.setLayoutManager(mLayoutManager);
        mRecycler_view.setAdapter(adapter);
        mRecycler_view.addItemDecoration(new MDStaggeredRvDividerDecoration(getActivity()));


    }

    private void initDate() {
        mLayoutManager = new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL);
        adapter = new MyAdapter(getData(), getActivity());
        adapter.setOnClickListeners(new MyAdapter.OnClickListeners() {
            @Override
            public void OnClickeListener(View v, int position) {
                //点击进入相应的url地址
                Intent intent=new Intent(getActivity(),VideoActivity.class);
                intent.putExtra("url",video_url[position]);
                startActivity(intent);
            }
        });
    }

    class MyPagerAdapter extends StaticPagerAdapter {
        @Override
        public View getView(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            imageView.setImageResource(image[position]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return imageView;
        }

        @Override
        public int getCount() {
            return image.length;
        }
    }

    private ArrayList<Video> getData() {
        ArrayList<Video> list = null;
        video_information info = new video_information();
        Video video = null;
        list = new ArrayList<>();
        for (int i = 0; i < video_title.length; i++) {
            video = new Video();
            video.setVideo_title(video_title[i]);
            video.setVideo_url(video_url[i]);
            video.setVideo_image(images[i]);
            list.add(video);
        }
        return list;
    }

}
