package com.example.music_online_app.online;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.datasource.DefaultDataSourceFactory;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.ProgressiveMediaSource;

import com.example.music_online_app.models.SongModels;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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
            if(isOnlinePath(songModels.getUrl())){
                updateCount();
            }
            Uri uri = Uri.parse(songModels.getUrl());
            MediaItem mediaItem = MediaItem.fromUri(uri);
            player.setMediaItem(mediaItem);
            player.prepare();
            player.play();
        } else {
            player.play();
        }
    }
    public static boolean isOnlinePath(String path) {
        return path != null &&
                (path.contains("http") || path.contains("https") ||
                        path.contains(".m3u") ||
                        path.contains(".pls"));
    }
    public static void stopPlaying(){
        MyExoPlayer.getInstance().stop();
    }

    public static void updateCount(){
        String currentSongId = currentSong.getId();
        FirebaseFirestore.getInstance().collection("Songs")
                .document(currentSongId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Long latestCount = documentSnapshot.getLong("count");
                        if(latestCount == null){
                            latestCount = 1L;
                        } else {
                            latestCount += 1;
                        }
                        Map<String, Object> countMap = new HashMap<>();
                        countMap.put("count", latestCount);
                        FirebaseFirestore.getInstance().collection("Songs")
                                .document(currentSongId)
                                .update(countMap);
                    }
                });
    }

}
