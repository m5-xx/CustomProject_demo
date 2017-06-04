package com.mayday.tool.localMusicManager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mayday.xy.customproject.R;

import java.util.List;

/**
 * Created by xy-pc on 2017/5/26.
 */

public class MyLikeAdapter extends BaseAdapter {

    private Context context;

    private List<MusicInfo> list;

    private ViewHolder vh = null;


    public MyLikeAdapter(Context context, List<MusicInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.like_music_adapter, viewGroup, false);
            vh = new ViewHolder();
            vh.tv_mylikeTitle = (TextView) view.findViewById(R.id.tv_mylikeTitle);
            vh.tv_mylikeSinger = (TextView) view.findViewById(R.id.tv_mylikeSinger);
            vh.tv_mylikeTime = (TextView) view.findViewById(R.id.tv_mylikeTime);
            view.setTag(vh);
        }
        vh = (ViewHolder) view.getTag();
        MusicInfo musicInfo = list.get(i);
        long musicLong = musicInfo.getDuration();
        String formatTime = MusicmediaUtils.formatTime(musicLong);

        vh.tv_mylikeTitle.setText("歌名:" + musicInfo.getTitle());
        vh.tv_mylikeSinger.setText("歌手:" + musicInfo.getSinger());
        vh.tv_mylikeTime.setText(formatTime);

        return view;
    }

    static class ViewHolder {
        TextView tv_mylikeTitle;
        TextView tv_mylikeSinger;
        TextView tv_mylikeTime;
    }

}
