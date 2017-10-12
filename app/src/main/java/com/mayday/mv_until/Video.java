package com.mayday.mv_until;

/**
 * Created by xy-pc on 2017/6/22.
 */

public class Video {
    private String video_title;
    private String video_url;
    private int video_image;

    public Video(String video_title, String video_url, int video_image) {
        this.video_title = video_title;
        this.video_url = video_url;
        this.video_image = video_image;
    }

    public Video() {
    }

    public String getVideo_title() {
        return video_title;
    }

    public void setVideo_title(String video_title) {
        this.video_title = video_title;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public int getVideo_image() {
        return video_image;
    }

    public void setVideo_image(int video_image) {
        this.video_image = video_image;
    }
}
