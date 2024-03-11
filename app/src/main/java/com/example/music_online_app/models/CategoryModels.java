package com.example.music_online_app.models;

public class CategoryModels {

    String name;
    String coverUrl;
    
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
}
