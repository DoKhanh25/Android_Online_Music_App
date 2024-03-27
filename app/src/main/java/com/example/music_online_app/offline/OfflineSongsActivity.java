package com.example.music_online_app.offline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.music_online_app.ListenerInterface.OnOfflineSongClickListener;
import com.example.music_online_app.MainActivity;
import com.example.music_online_app.R;
import com.example.music_online_app.SongPlayerActivity;
import com.example.music_online_app.adapter.OfflineMusicAdapter;
import com.example.music_online_app.authentication.LoginActivity;
import com.example.music_online_app.models.OfflineMusicModel;
import com.example.music_online_app.models.SongModels;
import com.example.music_online_app.online.MyExoPlayer;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class OfflineSongsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_CODE = 1;
    private final String SONG_NAME_EXTRA = "songModels";
    private final String ALBUM_NAME = "albumName";
    private final String BAR_CLICK = "isBarClick";
    public static ArrayList<OfflineMusicModel> offlineMusicModels;
    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    TextView noMusicTextView;
    RecyclerView recyclerView;
    RelativeLayout playerBarLayout;
    TextView barSongTextView;
    ImageView barSongImageView;
    OfflineMusicAdapter offlineMusicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_songs);
        //binding
        toolbar = findViewById(R.id.toolBar);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.offline_recyclerView);
        noMusicTextView = findViewById(R.id.no_music_text_view);
        playerBarLayout = findViewById(R.id.player_bar);
        barSongTextView = findViewById(R.id.bar_song_text_view);
        barSongImageView = findViewById(R.id.bar_song_image_view);

        setUpToolbar();
        permission();
        checkOfflineSongExist();
    }
    public void checkOfflineSongExist(){
        if(offlineMusicModels.size() > 0){
            Log.d("OfflineList", String.valueOf(offlineMusicModels.size()));
            setUpOfflineSongRecylerView();
            noMusicTextView.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            noMusicTextView.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkOfflineSongExist();
        showPlayingBar();
    }

    private void setUpOfflineSongRecylerView(){
        offlineMusicAdapter = new OfflineMusicAdapter(getApplicationContext(), offlineMusicModels, new OnOfflineSongClickListener() {
            @Override
            public void onItemClick(OfflineMusicModel offlineMusicModel) {
                SongModels songModels = new SongModels();
                songModels.setSinger(offlineMusicModel.getSinger());
                songModels.setTitle(offlineMusicModel.getTitle());
                songModels.setUrl(offlineMusicModel.getPath());
                navigateActivity(songModels);
            }
        });
        recyclerView.setAdapter(offlineMusicAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void setUpToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView.bringToFront();
        toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_local);
    }

    private void permission(){
        if(checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_MEDIA_AUDIO}, REQUEST_CODE);
            Toast.makeText(this, "Cấp quyền", Toast.LENGTH_SHORT).show();
        } else {
            offlineMusicModels = getAllMusicFile(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                offlineMusicModels = getAllMusicFile(this);
            }
            else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }
    }

    public static ArrayList<OfflineMusicModel> getAllMusicFile(Context context){
        ArrayList<OfflineMusicModel> musicFileModelArrayList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST
        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null){
            while (cursor.moveToNext()){
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String singer = cursor.getString(4);

                OfflineMusicModel musicFileModel = new OfflineMusicModel();
                musicFileModel.setAlbum(album);
                musicFileModel.setTitle(title);
                musicFileModel.setDuration(duration);
                musicFileModel.setPath(path);
                musicFileModel.setSinger(singer);
                musicFileModelArrayList.add(musicFileModel);
            }
            cursor.close();
        }
        return musicFileModelArrayList;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_local:
                break;
            case R.id.nav_album:
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(this, LoginActivity.class);
                startActivity(intent2);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    private void navigateActivity(SongModels songModels){
        Intent intent = new Intent(getApplicationContext(), SongPlayerActivity.class);
        intent.putExtra(SONG_NAME_EXTRA, songModels);
        intent.putExtra(ALBUM_NAME, "");
        intent.putExtra(BAR_CLICK, false);
        startActivity(intent);
    }

    private void showPlayingBar(){
        playerBarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SongPlayerActivity.class);
                intent.putExtra(BAR_CLICK, true);
                intent.putExtra("songModels", MyExoPlayer.getCurrentSong());
                startActivity(intent);
            }
        });

        if(MyExoPlayer.getCurrentSong() != null){
            playerBarLayout.setVisibility(View.VISIBLE);
            barSongTextView.setText(MyExoPlayer.getCurrentSong().getTitle());
            Glide.with(barSongImageView).load(MyExoPlayer.getCurrentSong().getCoverUrl())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                    .into(barSongImageView);
        } else {
            playerBarLayout.setVisibility(View.GONE);
        }
    }
}