package com.example.music_online_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
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
import com.example.music_online_app.authentication.LoginActivity;
import com.example.music_online_app.models.CategoryModels;
import com.example.music_online_app.models.SongModels;
import com.example.music_online_app.offline.OfflineSongsActivity;
import com.example.music_online_app.online.MyExoPlayer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final String MOST_PLAYED_COVER_URL = "https://firebasestorage.googleapis.com/v0/b/music-online-authentication.appspot.com/o/Categories_images%2Fcat-mostplayed.jpg?alt=media&token=3d822fc7-b1e9-4f26-8af0-fd0ed6e27b2e";
    private final String CATEGORY_NAME_EXTRA = "category";
    private final String BAR_CLICK = "isBarClick";
    CategoryAdapter categoryAdapter;
    SectionListAdapter sectionListAdapter;
    RecyclerView recyclerView, section1RecyclerView;
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

    // Section Most Played
    RelativeLayout sectionMostPlayedLayout;
    RecyclerView sectionMostPlayedRecyclerView;
    CategoryModels sectionMostPlayedCategory;

    // Navigation
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.categories_recycler_view);
        section1RecyclerView = findViewById(R.id.section_1_recycler_view);
        section2RecyclerView = findViewById(R.id.section_2_recycler_view);

        section1Button = findViewById(R.id.btn_section_1);
        shimmerFrameLayout = findViewById(R.id.shimmer_main);
        scrollView = findViewById(R.id.scroll_view);
        section1Layout = findViewById(R.id.section_1_main_layout);
        section2Layout = findViewById(R.id.section_2_main_layout);
        playerBarLayout = findViewById(R.id.player_bar);
        barSongImageView = findViewById(R.id.bar_song_image_view);
        barSongTextView = findViewById(R.id.bar_song_text_view);


        // most played view
        sectionMostPlayedLayout = findViewById(R.id.section_most_played_main_layout);
        sectionMostPlayedRecyclerView = findViewById(R.id.section_most_played_recycler_view);

        //setup navigation
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolBar);

        setupToolBar();

        getCategoryFromFirebase();
        getSection1FromFirebase();
        getSection2FromFirebase();
        getMostPlayedSectionFromFirebase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showPlayingBar();
    }
    // category
    public void setupToolBar(){
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
        navigationView.setCheckedItem(R.id.nav_home);
    }


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

    // most played section
    public void getMostPlayedSectionFromFirebase(){
        FirebaseFirestore.getInstance().collection("Songs")
                .orderBy("count", Query.Direction.DESCENDING).limit(5L)
                .get()
                .addOnSuccessListener(s-> {
                    List<SongModels> songModels = s.toObjects(SongModels.class);
                    List<String> songsId = new ArrayList<>();
                    for (SongModels song: songModels){
                        songsId.add(song.getId());
                    }
                    this.sectionMostPlayedCategory = new CategoryModels();
                    this.sectionMostPlayedCategory.setName("Most Played");
                    this.sectionMostPlayedCategory.setCoverUrl(MOST_PLAYED_COVER_URL);
                    this.sectionMostPlayedCategory.setSongsList(songsId);

                    setupSectionMostPlayed(songModels);

                });
        sectionMostPlayedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OnlineAlbumActivity.class);
                intent.putExtra(CATEGORY_NAME_EXTRA, sectionMostPlayedCategory);
                startActivity(intent);
            }
        });
    }

    public void setupSectionMostPlayed(List<SongModels> songModelsList){
        sectionListAdapter = new SectionListAdapter(getApplicationContext(), songModelsList, new OnSongListClickListener() {
            @Override
            public void onItemClick(SongModels songModels) {

            }
        });
        sectionMostPlayedRecyclerView.setAdapter(sectionListAdapter);
        sectionMostPlayedRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_local:
                Intent intent = new Intent(this, OfflineSongsActivity.class);
                startActivity(intent);
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
}