package com.example.music_online_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.music_online_app.ListenerInterface.OnSongListClickListener;
import com.example.music_online_app.adapter.SongsListAdapter;
import com.example.music_online_app.models.CategoryModels;
import com.example.music_online_app.models.SongModels;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class OnlineAlbumActivity extends AppCompatActivity {

    CategoryModels categoryModels;

    public OnlineAlbumActivity(CategoryModels categoryModels){
        this.categoryModels = categoryModels;
    }
    public OnlineAlbumActivity(){
    }

    RecyclerView recyclerView;

    TextView textView;
    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_album);

        Intent intent = getIntent();
        categoryModels = (CategoryModels) intent.getSerializableExtra("category");

        if(categoryModels == null){
            Toast.makeText(this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
            Intent newIntent = new Intent(this, MainActivity.class);
            startActivity(newIntent);
            finish();
            return;
        }


        //binding dữ liệu
        recyclerView = findViewById(R.id.song_list_recycler_view);
        textView = findViewById(R.id.name_text_view);
        imageView = findViewById(R.id.cover_image_view);

        // set giao diện
        textView.setText(categoryModels.getName());
        Glide.with(imageView).load(categoryModels.getCoverUrl())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                .into(imageView);



        getListSongModelsFromFireBase();


    }

    public void getListSongModelsFromFireBase(){
        FirebaseFirestore.getInstance().collection("Songs")
                .get().addOnSuccessListener(v -> {
                    List<SongModels> songModelsList = v.toObjects(SongModels.class);
                    setUpRecyclerView(songModelsList);
                });

    }
    public List<SongModels> getListSongInAlbum(List<String> songs, List<SongModels> songModelsList){
        List<SongModels> songModels = new ArrayList<>();
        for (String song: songs){
            for (SongModels songModel: songModelsList){
                if(song.equals(songModel.getId())){
                    songModels.add(songModel);
                }
            }
        }
        return songModels;
    }

    private void setUpRecyclerView(List<SongModels> songModelsList){
        SongsListAdapter songsListAdapter = new SongsListAdapter(
                this,
                getListSongInAlbum(categoryModels.getSongs(), songModelsList),
                new OnSongListClickListener() {
                    @Override
                    public void onItemClick(SongModels songModels) {
                    }
                });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(songsListAdapter);
    }
}