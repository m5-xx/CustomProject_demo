package com.mayday.net.music;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mayday.tool.localMusicManager.MusicInfo;
import com.mayday.tool.localMusicManager.MusicmediaUtils;
import com.mayday.xy.customproject.R;

import java.util.List;

/**
 * 使用与收藏的音乐界面以及网路歌曲的界面显示
 * Created by xy-pc on 2017/5/26.
 */

public class NetMusicAdapter extends BaseAdapter {

    private Context context;

    private List<MusicInfo> list;

    private ViewHolder vh = null;


    public NetMusicAdapter(Context context, List<MusicInfo> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.net_music_adapter, viewGroup, false);
            vh = new ViewHolder();
            vh.tv_mylikeTitle = (TextView) view.findViewById(R.id.tv_mylikeTitle);
            vh.tv_mylikeSinger = (TextView) view.findViewById(R.id.tv_mylikeSinger);
            vh.bt_download = (Button) view.findViewById(R.id.bt_downloade);
            view.setTag(vh);
        }
        vh = (ViewHolder) view.getTag();
        final MusicInfo musicInfo = list.get(i);

//        long musicLong = musicInfo.getDuration();
//        String formatTime = MusicmediaUtils.formatTime(musicLong);

        vh.tv_mylikeTitle.setText("歌名:" + musicInfo.getTitle());
        vh.tv_mylikeSinger.setText("歌手:" + musicInfo.getSinger());
        vh.bt_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //这里需要判断该url地址是否已经被下载过
                String url = musicInfo.getUrl();
                String name=musicInfo.getTitle();
                Intent intent=new Intent(context,NetService.class);
                intent.putExtra("url",url);
                intent.putExtra("name",name);
                intent.setAction(NetService.GETURL);
                context.startService(intent);
                Toast.makeText(context,musicInfo.getTitle()+"开始下载",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    static class ViewHolder {
        TextView tv_mylikeTitle;
        TextView tv_mylikeSinger;
//        TextView tv_mylikeTime;
        Button bt_download;
    }

}
