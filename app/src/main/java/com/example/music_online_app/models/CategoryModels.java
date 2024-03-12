package com.example.music_online_app.models;

import java.io.Serializable;
import java.util.List;

public class CategoryModels implements Serializable {

    String name;
    String coverUrl;
    List<String> songs;
    
    public CategoryModels(){

    }

    public CategoryModels(String name, String coverUrl) {
        this.name = name;
        this.coverUrl = coverUrl;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
    public List<String> getSongs() {
        return songs;
    }

    public void setSongsList(List<String> songs) {
        this.songs = songs;
    }
}
