package com.example.music_online_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_online_app.R;
import com.example.music_online_app.models.CategoryModels;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>{
    private List<CategoryModels> categoryModels;
    private Context context;

    public CategoryAdapter(Context context, List<CategoryModels> categoryModels){
        this.context = context;
        this.categoryModels = categoryModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item_recycler_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String image = categoryModels.get(position).getCoverUrl();
        String name = categoryModels.get(position).getName();

        holder.textView.setText(name);

    }

    @Override
    public int getItemCount() {
        return categoryModels.size() ;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.name_text_view);
            imageView = itemView.findViewById(R.id.cover_image_view);
        }

    }
}
