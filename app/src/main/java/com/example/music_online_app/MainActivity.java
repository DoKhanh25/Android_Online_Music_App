package com.example.music_online_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.music_online_app.ListenerInterface.OnSongListClickListener;
import com.example.music_online_app.adapter.CategoryAdapter;
import com.example.music_online_app.ListenerInterface.OnCategoryClickListener;
import com.example.music_online_app.adapter.SectionListAdapter;
import com.example.music_online_app.models.CategoryModels;
import com.example.music_online_app.models.SongModels;
import com.example.music_online_app.online.MyExoPlayer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String CATEGORY_NAME_EXTRA = "category";
    private final String BAR_CLICK = "isBarClick";
    CategoryAdapter categoryAdapter;
    SectionListAdapter sectionListAdapter;
    RecyclerView recyclerView, section1RecyclerView;
    TextView textView;
    ShimmerFrameLayout shimmerFrameLayout;
    ScrollView scrollView;
    FirebaseAuth mAuth;
    MaterialButton section1Button;
    CategoryModels section1Category;
    CategoryModels section2Category;
    RelativeLayout section1Layout;
    RelativeLayout section2Layout;
    RecyclerView section2RecyclerView;
    RelativeLayout playerBarLayout;
    ImageView barSongImageView;
    TextView barSongTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.categories_recycler_view);
        section1RecyclerView = findViewById(R.id.section_1_recycler_view);
        section2RecyclerView = findViewById(R.id.section_2_recycler_view);
        textView = findViewById(R.id.user_name);
        section1Button = findViewById(R.id.btn_section_1);
        shimmerFrameLayout = findViewById(R.id.shimmer_main);
        scrollView = findViewById(R.id.scroll_view);
        section1Layout = findViewById(R.id.section_1_main_layout);
        section2Layout = findViewById(R.id.section_2_main_layout);
        playerBarLayout = findViewById(R.id.player_bar);
        barSongImageView = findViewById(R.id.bar_song_image_view);
        barSongTextView = findViewById(R.id.bar_song_text_view);

        textView.setText("Xin chào " + mAuth.getCurrentUser().getEmail());

        getCategoryFromFirebase();
        getSection1FromFirebase();
        getSection2FromFirebase();

    }

    @Override
    protected void onResume() {
        super.onResume();
        showPlayingBar();
    }
    // category

    public void getCategoryFromFirebase(){

        turnOnShimmer(true);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            turnOnShimmer(false);
        }, 2500);

        FirebaseFirestore.getInstance().collection("category")
                .get().addOnSuccessListener(v -> {
                    List<CategoryModels> categoryModels = v.toObjects(CategoryModels.class);
                    setupCategoryRecyclerView(categoryModels);
                });
    }


    public void setupCategoryRecyclerView(List<CategoryModels> categoryModels){
        categoryAdapter = new CategoryAdapter(this, categoryModels, new OnCategoryClickListener() {
            @Override
            public void onItemClick(CategoryModels categoryModels) {
                Intent intent = new Intent(getApplicationContext(), OnlineAlbumActivity.class);
                intent.putExtra("category", categoryModels);
                startActivity(intent);

            }
        });
        recyclerView.setAdapter(categoryAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    // section 1
    public void getSection1FromFirebase(){
        FirebaseFirestore.getInstance().collection("sections").document("section1").get()
                .addOnSuccessListener(s-> {
                    CategoryModels categoryModels = s.toObject(CategoryModels.class);
                    this.section1Category = categoryModels;
                    FirebaseFirestore.getInstance().collection("Songs").whereIn("id",categoryModels.getSongs()).get()
                            .addOnSuccessListener(v->{
                        List<SongModels> songModels = v.toObjects(SongModels.class);
                        setupSection1RecyclerView(songModels);
                    });
                });
        section1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OnlineAlbumActivity.class);
                intent.putExtra(CATEGORY_NAME_EXTRA, section1Category);
                startActivity(intent);
            }
        });
    }

    public void setupSection1RecyclerView(List<SongModels> songModelsList){
        sectionListAdapter = new SectionListAdapter(getApplicationContext(), songModelsList, new OnSongListClickListener() {
            @Override
            public void onItemClick(SongModels songModels) {

            }
        });

        section1RecyclerView.setAdapter(sectionListAdapter);
        section1RecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    public void getSection2FromFirebase(){
        FirebaseFirestore.getInstance().collection("sections").document("section2").get()
                .addOnSuccessListener(s-> {
                    CategoryModels categoryModels = s.toObject(CategoryModels.class);
                    this.section2Category = categoryModels;
                    FirebaseFirestore.getInstance().collection("Songs").whereIn("id",categoryModels.getSongs()).get()
                            .addOnSuccessListener(v->{
                                List<SongModels> songModels = v.toObjects(SongModels.class);
                                setupSection2RecyclerView(songModels);
                            });
                });
        section2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OnlineAlbumActivity.class);
                intent.putExtra(CATEGORY_NAME_EXTRA, section2Category);
                startActivity(intent);
            }
        });
    }

    public void setupSection2RecyclerView(List<SongModels> songModelsList){
        sectionListAdapter = new SectionListAdapter(getApplicationContext(), songModelsList, new OnSongListClickListener() {
            @Override
            public void onItemClick(SongModels songModels) {

            }
        });

        section2RecyclerView.setAdapter(sectionListAdapter);
        section2RecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }


    private void turnOnShimmer(boolean turnOn){
        if(turnOn){
            this.shimmerFrameLayout.startShimmerAnimation();
            this.scrollView.setVisibility(View.INVISIBLE);
        } else {
            this.shimmerFrameLayout.stopShimmerAnimation();
            this.shimmerFrameLayout.setVisibility(View.INVISIBLE);
            this.scrollView.setVisibility(View.VISIBLE);
        }
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