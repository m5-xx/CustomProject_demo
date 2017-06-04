package com.mayday.mv.tool;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mayday.mv_until.Video;
import com.mayday.xy.customproject.R;

import java.util.ArrayList;

/**
 * Created by xy-pc on 2017/6/4.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<Video> list = null;
    private Context context;

    private MyAdapter.OnClickListeners onClickListeners;

    public MyAdapter(ArrayList<Video> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnClickListeners(MyAdapter.OnClickListeners onClickListeners1){
        this.onClickListeners=onClickListeners1;
    }


    @Override
    public int getItemViewType(int position) {
        return position%2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == 1) {
            v = LayoutInflater.from(context).inflate(R.layout.view_rv_item, parent, false);
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.view_rv_item_two, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.imageView.setImageResource(list.get(position).getVideo_image());
        holder.mv_title.setText(list.get(position).getVideo_title());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onClickListeners!=null){
                    int layoutPosition = holder.getLayoutPosition();
                    onClickListeners.OnClickeListener(holder.imageView,layoutPosition);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView mv_title;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_tv);
            mv_title= (TextView) itemView.findViewById(R.id.mv_title);
        }
    }

    public interface OnClickListeners{
       void OnClickeListener(View v,int position);
    }
}
