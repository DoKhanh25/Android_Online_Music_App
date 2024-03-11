package com.example.music_online_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.music_online_app.adapter.CategoryAdapter;
import com.example.music_online_app.models.CategoryModels;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    CategoryAdapter categoryAdapter;

    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.categories_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        getCategoryFromFirebase();
    }

    public void getCategoryFromFirebase(){
        FirebaseFirestore.getInstance().collection("category")
                .get().addOnSuccessListener(v -> {
                    List<CategoryModels> categoryModels = v.toObjects(CategoryModels.class);
                    setupCategoryRecyclerView(categoryModels);
                });
    }

    public void setupCategoryRecyclerView(List<CategoryModels> categoryModels){
        categoryAdapter = new CategoryAdapter(this, categoryModels);
        recyclerView.setAdapter(categoryAdapter);
    }
}