package com.mezan.whatsappclone;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {

    private ArrayList<String> mediaList;
    Context context;





    MediaAdapter(ArrayList<String> mediaList,Context context){
        this.mediaList = mediaList;
        this.context = context;
    }

    @Override
    public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        need layout inflation

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_media, null, false);

        MediaViewHolder holder = new MediaViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(MediaAdapter.MediaViewHolder holder, int position) {
        Glide.with(context).load(Uri.parse(mediaList.get(position))).into(holder.mMedia);

    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }
    class MediaViewHolder extends RecyclerView.ViewHolder{
        ImageView mMedia;
        MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            mMedia = itemView.findViewById(R.id.mediaFile);

        }
    }
}
