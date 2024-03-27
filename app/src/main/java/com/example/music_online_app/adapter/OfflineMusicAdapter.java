package com.example.music_online_app.adapter;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_online_app.R;
import com.example.music_online_app.models.OfflineMusicModel;

import java.util.ArrayList;

public class OfflineMusicAdapter extends RecyclerView.Adapter<OfflineMusicAdapter.ViewHolder> {
    private Context context;
    private ArrayList<OfflineMusicModel> offlineMusicModels;




    public OfflineMusicAdapter(Context context, ArrayList<OfflineMusicModel> offlineMusicModels){
        this.context = context;
        this.offlineMusicModels = offlineMusicModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.offline_music_item,  parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try{
            holder.fileName.setText(offlineMusicModels.get(position).getTitle());
            byte[] image = getAlbumArt(offlineMusicModels.get(position).getPath());

            if(image != null){
                Glide.with(context).asBitmap().load(image).into(holder.albumArt);
            } else {
                Glide.with(context).load(R.drawable.baseline_album).into(holder.albumArt);
            }
        } catch (Exception e){
            Log.e("err", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return offlineMusicModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView fileName;
        ImageView albumArt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.music_filename);
            albumArt = itemView.findViewById(R.id.music_img);
        }
    }

    private byte[] getAlbumArt(String uri) throws Exception{
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}
