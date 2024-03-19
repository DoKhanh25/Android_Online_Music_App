package com.example.music_online_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.music_online_app.models.SongModels;
import com.example.music_online_app.online.MyExoPlayer;

public class SongPlayerActivity extends AppCompatActivity {

    TextView songTitleTextView;
    TextView albumTextView;
    ImageView coverImageView;
    PlayerView playerView;
    ImageView songEffectView;

    ExoPlayer exoPlayer;

    Player.Listener playerListener = new Player.Listener() {
        @Override
        public void onIsPlayingChanged(boolean isPlaying) {
            Player.Listener.super.onIsPlayingChanged(isPlaying);
            showSongEffectGif(isPlaying);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player);

        // binding
        songTitleTextView = findViewById(R.id.song_title_text_view);
        albumTextView = findViewById(R.id.album_text_view);
        coverImageView = findViewById(R.id.cover_image_view);
        playerView = findViewById(R.id.player_view);
        songEffectView = findViewById(R.id.song_effect_gif_view);

        // get data
        Intent intent = getIntent();
        SongModels songModels = (SongModels) intent.getSerializableExtra("songModels");
        String categoryName = (String) intent.getSerializableExtra("albumName");
        boolean isBarSongClick = (boolean) intent.getSerializableExtra("isBarClick");

        SongModels songRs = songModels != null ? songModels : MyExoPlayer.getCurrentSong();

        if(songModels != null && categoryName != null){
            MyExoPlayer.startPlaying(getApplicationContext(), songRs);
            albumTextView.setText("Album: " + categoryName.toUpperCase());
        }

        exoPlayer = MyExoPlayer.getInstance();
        songTitleTextView.setText(songRs.getTitle());
        playerView.showController();

        Glide.with(coverImageView).load(songRs.getCoverUrl())
                .circleCrop()
                .into(coverImageView);
        Glide.with(songEffectView).load(R.drawable.bhfo)
                .circleCrop()
                .into(songEffectView);

        exoPlayer.addListener(playerListener);

        playerView.setPlayer(exoPlayer);

        //
    }
    public void showSongEffectGif(boolean show){
        if(show){
            songEffectView.setVisibility(View.VISIBLE);
        } else {
            songEffectView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exoPlayer.removeListener(playerListener);
    }
}