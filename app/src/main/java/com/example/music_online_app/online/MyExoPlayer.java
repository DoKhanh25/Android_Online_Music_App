package com.example.music_online_app.online;

import android.content.Context;

import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;

import com.example.music_online_app.models.SongModels;

public class MyExoPlayer {

    private static ExoPlayer player = null;

    private static SongModels currentSong = null;

    public static ExoPlayer getInstance(){
        return player;
    }
    public static SongModels getCurrentSong(){
        return currentSong;
    }

    public static void startPlaying(Context context, SongModels songModels){
        if(player == null){
            player = new ExoPlayer.Builder(context).build();
        }
        if(currentSong == null || !currentSong.equals(songModels)){
            currentSong = songModels;

            MediaItem mediaItem = MediaItem.fromUri(currentSong.getUrl());
            player.setMediaItem(mediaItem);
            player.prepare();
            player.play();
        } else {
            player.play();
        }
    }
    public static void stopPlaying(){
        MyExoPlayer.getInstance().stop();
    }


}
