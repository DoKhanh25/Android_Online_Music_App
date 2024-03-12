package com.example.music_online_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.music_online_app.models.CategoryModels;

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

        categoryModels = (CategoryModels) getIntent().getSerializableExtra("category");


        //binding dữ liệu
        recyclerView = findViewById(R.id.song_list_recycler_view);
        textView = findViewById(R.id.name_text_view);
        imageView = findViewById(R.id.cover_image_view);

        // set giao diện
        textView.setText(categoryModels.getName());
        Glide.with(imageView).load(categoryModels.getCoverUrl())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                .into(imageView);
    }
}