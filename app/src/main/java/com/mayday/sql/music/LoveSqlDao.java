package com.mayday.sql.music;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.mayday.tool.localMusicManager.MusicInfo;

import java.sql.SQLException;
import java.util.List;

/**
 * 该类实现喜爱歌曲的添加以及移除
 * 可以在主播放页面来调用该类
 * Created by xy-pc on 2017/5/26.
 */

public class LoveSqlDao  {
    private Context context;
    private MyLoveSql myLoveSql;
    private Dao<MusicInfo,Integer> dao;

    public LoveSqlDao(Context context) {
        this.context=context;
        myLoveSql=myLoveSql.getInstance(context);
        try {
            dao=myLoveSql.getDaoInstance(MusicInfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加喜欢的歌曲到数据库
     * @param musicInfo
     */
    public void addMusicLove(MusicInfo musicInfo){
        try {
            dao.create(musicInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从数据库中移除喜欢的歌曲(通过歌曲的id和歌曲的名称)
     */
    public void removeMusicLove(String music_title,String music_singer){
        DeleteBuilder<MusicInfo, Integer> deleteBuilder = dao.deleteBuilder();
        try {
            deleteBuilder.where().eq("title",music_title).and().eq("singer",music_singer);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*public List<MusicInfo> queryMusicLove(long music_id){
        List<MusicInfo> list=null;
        //新ListView中的MusicInfo信息
        MusicInfo musicInfo=new MusicInfo();
        QueryBuilder<MusicInfo, Integer> queryBuilder = dao.queryBuilder();
        try {
            list=queryBuilder.where().eq("id",music_id).query();
            for(MusicInfo lis:list){
                musicInfo.setId(lis.getId());
                musicInfo.setTitle(lis.getTitle());
                musicInfo.setSinger(lis.getSinger());
                musicInfo.setDuration(lis.getDuration());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }*/

    /**
     * 查询表中的所有数据
     * 提供给我们展示喜欢的音乐的主界面
     * @return
     */
    public List<MusicInfo> queryMusicLove(){
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean queryIsExist(String title,String singer){
        QueryBuilder<MusicInfo, Integer> queryBuilder = dao.queryBuilder();
        try {
            List<MusicInfo> query = queryBuilder.where().eq("title", title).and().eq("singer", singer).query();
            if(query.size()>0){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
