package com.example.music_online_app.models;

public class OfflineMusicModel {
    String path;
    String title;
    String singer;
    String album;
    String duration;
    public OfflineMusicModel(String path, String title, String singer, String album, String duration) {
        this.path = path;
        this.title = title;
        this.singer = singer;
        this.album = album;
        this.duration = duration;
    }

    public OfflineMusicModel(){

    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public String getSinger() {
        return singer;
    }

    public String getAlbum() {
        return album;
    }

    public String getDuration() {
        return duration;
    }


    public void setPath(String path) {
        this.path = path;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

}
