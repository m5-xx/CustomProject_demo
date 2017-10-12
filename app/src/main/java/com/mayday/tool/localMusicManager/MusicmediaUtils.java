package com.mayday.tool.localMusicManager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * Created by xy-pc on 2017/6/20.
 *
 */

public class MusicmediaUtils {
    //添加歌曲信息到集合中，并以集合形式返回
    public static ArrayList<MusicInfo>  getMusicInfos(Context context) {
        //长度>2min以上
        ContentResolver contentResolver = context.getContentResolver();
        //MediaStore.Audio.Media.DURATION + ">120000"第三个参数可以指定获取到歌曲的时长(过滤)
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.MIME_TYPE + "=? or "
                        + MediaStore.Audio.Media.MIME_TYPE + "=?",  new String[] { "audio/mpeg", "audio/x-ms-wma" },null);
        ArrayList<MusicInfo> musicList = new ArrayList<>();
        if(cursor!=null){
            while (cursor.moveToNext()){
                MusicInfo music = new MusicInfo();
                //歌曲ID
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                //歌曲名称
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                //专辑名称
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                //专辑ID(KEY)
                int albun_id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                //歌手
                String singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                //歌曲总长
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                //文件路径
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                //是否为音乐(为0就表示不为音乐)
                int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
                if (isMusic != 0) {
                    music.setId(id);
                    music.setTitle(title);
                    music.setAlbum(album);
                    music.setAlbum_id(albun_id);
                    music.setSinger(singer);
                    music.setDuration(duration);
                    music.setUrl(url);
                    musicList.add(music);
                }
            }
            cursor.close();
        }
        return musicList;
    }

    //将歌曲的时间转化为分钟加秒
    static String formatTime(long time) {
        String min = time / (1000 * 60) + "";   //4.33333
        String sec = time % (1000 * 60) + "";      //0.33333
        if (min.length() < 2) {
            min = "0" + time / (1000 * 60) + "";
        } else {
            min = time / (1000 * 60) + "";
        }
        if (sec.length() == 4) {
            sec = "0" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 3) {
            sec = "00" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 2) {
            sec = "000" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 1) {
            sec = "0000" + (time % (1000 * 60)) + "";
        }

        return min + ":" + sec.trim().substring(0, 2);
    }

}
