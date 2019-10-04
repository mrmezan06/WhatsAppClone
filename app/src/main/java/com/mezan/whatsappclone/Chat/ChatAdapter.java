package com.mezan.whatsappclone.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.mezan.whatsappclone.ChatActivity;
import com.mezan.whatsappclone.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<ChatObject> chatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public LinearLayout mLayout;

        public MyViewHolder(View view) {
            super(view);

            mTitle =  view.findViewById(R.id.title);
            mLayout = view.findViewById(R.id.chatLayout);
        }
    }



    public ChatAdapter(List<ChatObject> chatList){
        this.chatList = chatList;
    }

    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chats, parent, false);

        return  new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ChatAdapter.MyViewHolder holder, int position) {
        ChatObject chat = chatList.get(position);
        holder.mTitle.setText(chat.getChatId());


        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("chatID",chatList.get(holder.getAdapterPosition()).getChatId());
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
                }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
