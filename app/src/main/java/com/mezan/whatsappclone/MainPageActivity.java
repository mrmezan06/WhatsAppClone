package com.mezan.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mezan.whatsappclone.Chat.ChatAdapter;
import com.mezan.whatsappclone.Chat.ChatObject;

import java.util.ArrayList;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {

    private Button mLogout,mFindUser;

    private RecyclerView mChatlist;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    List<ChatObject> chatList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Fresco.initialize(this);

        mLogout=findViewById(R.id.logout);
        mFindUser=findViewById(R.id.findUser);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return;
            }
        });
        chatList = new ArrayList<>();
        getPermission();
        initializeRecyclerView();
        getUserChatList();

        mFindUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),FindUserActivity.class));
            }
        });



    }
    private void getUserChatList(){
        DatabaseReference mUserChatDB = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat");

        //addValueEventListener Always check the database
        mUserChatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        ChatObject mChat = new ChatObject(childSnapshot.getKey());
                        boolean exist=false;
                        for(ChatObject mChatIT : chatList){
                            if(mChatIT.getChatId().equals(mChat.getChatId())){
                                exist=true;
                            }
                        }
                        if(exist){
                            continue;
                        }
                        chatList.add(mChat);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initializeRecyclerView() {

        mChatlist=findViewById(R.id.chatList);


        //Adapter
        mAdapter=new ChatAdapter(chatList);


        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        mChatlist.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        mChatlist.setLayoutManager(mLayoutManager);
        mChatlist.setItemAnimator(new DefaultItemAnimator());
        mChatlist.setAdapter(mAdapter);

    }
    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS},1);
        }
    }


}
