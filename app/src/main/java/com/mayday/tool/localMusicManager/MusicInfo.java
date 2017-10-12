package com.mayday.tool.localMusicManager;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by xy-pc on 2017/6/20.
 */

public class MusicInfo {
    //当前音乐的item.id
    @DatabaseField(generatedId = true)
    private long id;
    //当前音乐的名称
    @DatabaseField(columnName = "title")
    private String title;
    //歌手
    @DatabaseField(columnName = "singer")
    private String singer;
    //歌曲专辑名称
    @DatabaseField(columnName = "album")
    private String album;
    //专辑的id(key)
    @DatabaseField(columnName = "album_id")
    private int album_id;//目的是在后面获取专辑的封面做准备
    //歌曲的时长
    @DatabaseField(columnName = "duration")
    private long duration;
    //歌曲的大小
    @DatabaseField(columnName = "size")
    private long size;
    //当前音乐的路劲
    @DatabaseField(columnName = "url")
    private String url;
    //是否为音乐
    @DatabaseField(columnName = "isMusic")
    private int isMusic;

    //是否被收藏
    private boolean Love;

    public boolean isLove() {
        return Love;
    }

    public void setLove(boolean love) {
        Love = love;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIsMusic() {
        return isMusic;
    }

    public void setIsMusic(int isMusic) {
        this.isMusic = isMusic;
    }

    @Override
    public String toString() {
        return "歌名" + title + "歌手" + singer;
    }
}
