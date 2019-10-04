package com.mezan.whatsappclone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder>{
    ArrayList<String> mediaList;
    Context context;
    public MediaAdapter(Context context,ArrayList<String> mediaList){
        this.context = context;
        this.mediaList = mediaList;
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      //  LayoutInflater.from(parent.getContext()).inflate(R.layout.,null);
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public class MediaViewHolder extends RecyclerView.ViewHolder{

        public MediaViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
