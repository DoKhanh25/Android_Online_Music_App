package com.example.music_online_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.music_online_app.adapter.CategoryAdapter;
import com.example.music_online_app.ListenerInterface.OnCategoryClickListener;
import com.example.music_online_app.models.CategoryModels;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    CategoryAdapter categoryAdapter;

    RecyclerView recyclerView;

    TextView textView;

    ShimmerFrameLayout shimmerFrameLayout;
    ScrollView scrollView;
    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.categories_recycler_view);
        textView = findViewById(R.id.user_name);
        textView.setText("Xin chào " + mAuth.getCurrentUser().getEmail());

        shimmerFrameLayout = findViewById(R.id.shimmer_main);
        scrollView = findViewById(R.id.scroll_view);


        getCategoryFromFirebase();
    }

    public void getCategoryFromFirebase(){

        turnOnShimmer(true);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            turnOnShimmer(false);
        }, 2000);

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
                finish();

            }
        });
        recyclerView.setAdapter(categoryAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

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
}