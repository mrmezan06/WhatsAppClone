package com.mezan.whatsappclone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.mezan.whatsappclone.Chat.ChatObject;
import com.stfalcon.frescoimageviewer.ImageViewer;


import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private List<MessageObject> messageList;

     class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mMessage,
                 mSender;
        LinearLayout mLayout;
        Button mViewMedia;

        MyViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.msg_Lay_root);
            mMessage = view.findViewById(R.id.message);
            mSender = view.findViewById(R.id.sender);
            mViewMedia = view.findViewById(R.id.viewMedia);
        }
    }



     MessageAdapter(List<MessageObject> messageList){
        this.messageList = messageList;
    }

    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);

        return  new MessageAdapter.MyViewHolder(itemView);
}

    @Override
    public void onBindViewHolder(final MessageAdapter.MyViewHolder holder, int position) {
            holder.mMessage.setText(messageList.get(position).getMessage());
            holder.mSender.setText(messageList.get(position).getSenderId());

            if(messageList.get(holder.getAdapterPosition()).mediaUrlList.isEmpty()){
                holder.mViewMedia.setVisibility(View.GONE);
            }else {
                holder.mViewMedia.setVisibility(View.VISIBLE);
            }

            holder.mViewMedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new ImageViewer.Builder(view.getContext(), messageList.get(holder.getAdapterPosition()).mediaUrlList)
                            .setStartPosition(0)
                            .show();
                }
            });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
//3h 54m
