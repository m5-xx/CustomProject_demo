package com.mayday.tool.localMusicManager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mayday.xy.customproject.R;

import java.util.ArrayList;

/**
 * Created by xy-pc on 2017/5/20.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;

    private ArrayList<MusicInfo> musicinfos;

    private MyAdapter.OnClickListener mOnclickListener;


    public MyAdapter(Context context, ArrayList<MusicInfo> musicinfos) {
        this.context = context;
        this.musicinfos = musicinfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.localmusic_adapter, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MusicInfo musciInfo = musicinfos.get(position);
        //获取到专辑的封面
//        Bitmap bitmap=MusicmediaUtils.getArtwork(context,musciInfo.getId(),musciInfo.getAlbum_id(),true);
//        //通过得到专辑的id来得到专辑的封面图片
//        holder.iv_music_default.setImageBitmap(bitmap);
        holder.iv_music_default.setImageResource(R.drawable.pic_music);
        holder.tv_title.setText(musciInfo.getTitle());
        holder.tv_singer.setText(musciInfo.getSinger());
        //暂时不写点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=0;
                if (mOnclickListener!=null){
                    pos = holder.getLayoutPosition();
                    mOnclickListener.onClickListener(holder.itemView,pos);
                }
            }

        });
    }

    @Override
    public int getItemCount() {
        return musicinfos == null ? 0 : musicinfos.size();
    }

    //GridView自定义ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_music_default;
        TextView tv_title;
        TextView tv_singer;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_music_default = (ImageView) itemView.findViewById(R.id.iv_music_default);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_singer = (TextView) itemView.findViewById(R.id.tv_singer);
        }
    }

    public interface  OnClickListener{
        void onClickListener(View view,int position);
    }

    public void setOnClickListener(OnClickListener listener){
        this.mOnclickListener=listener;
    }

}
