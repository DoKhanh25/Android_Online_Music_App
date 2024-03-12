package com.example.music_online_app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.music_online_app.R;
import com.example.music_online_app.ListenerInterface.OnSongListClickListener;
import com.example.music_online_app.models.SongModels;

import java.util.List;

public class SongsListAdapter extends RecyclerView.Adapter<SongsListAdapter.MyViewHolder> {

    private List<SongModels> songModelsList;
    private Context context;
    private OnSongListClickListener listener;

    public SongsListAdapter(){

    }

    public SongsListAdapter(Context context, List<SongModels> songModelsList){
        this.context = context;
        this.songModelsList = songModelsList;
    }

    public SongsListAdapter(
            Context context,
            List<SongModels> songModelsList,
            OnSongListClickListener listener
            ){
        this.listener = listener;
        this.context = context;
        this.songModelsList = songModelsList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_item_recycler_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(songModelsList.get(position), listener);

    }

    @Override
    public int getItemCount() {
        return songModelsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView songnameTextView;
        TextView singerTextView;
        ImageView songImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            songnameTextView = itemView.findViewById(R.id.song_title_text_view);
            singerTextView = itemView.findViewById(R.id.song_singer_text_view);
            songImageView = itemView.findViewById(R.id.song_cover_image);


        }
        public void bind(final SongModels item, final OnSongListClickListener listener) {


            songnameTextView.setText(item.getTitle());
            singerTextView.setText(item.getSinger());

            Glide.with(songImageView).load(item.getCoverUrl())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                    .into(songImageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
