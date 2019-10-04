package com.mezan.whatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mezan.whatsappclone.Chat.ChatAdapter;
import com.mezan.whatsappclone.Chat.ChatObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Intent.EXTRA_ALLOW_MULTIPLE;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mChat;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<MessageObject> messageList = new ArrayList<>();

    EditText messageText;
    Button senderBtn,addMediaBtn;
    String chatID;
    DatabaseReference mChatDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messageText = findViewById(R.id.messageET);
        senderBtn = findViewById(R.id.msgSend);
        addMediaBtn = findViewById(R.id.mediaSend);

        Intent it = getIntent();
        Bundle bundle = it.getExtras();

        if(bundle != null){
            chatID = bundle.getString("chatID");
        }
        mChatDB = FirebaseDatabase.getInstance().getReference().child("chat").child(chatID);

        senderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        addMediaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        initializeRecyclerView();

        getChatMessages();
    }

    int PICK_IMAGE_CODE = 1;
    ArrayList<String> mediaURIList = new ArrayList<>();
    private void OpenGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent,"Select Image(s)"),PICK_IMAGE_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == PICK_IMAGE_CODE){
                if(data.getClipData() == null){
                    mediaURIList.add(data.getData().toString());
                }else {
                    for(int i=0;i<data.getClipData().getItemCount();i++){
                        mediaURIList.add(data.getClipData().getItemAt(i).getUri().toString());
                    }
                }
            }
        }
    }

    private void getChatMessages() {

        mChatDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    String  text = "",
                            creatorID = "";
                    if(dataSnapshot.child("text").getValue() != null){
                        text = dataSnapshot.child("text").getValue().toString();

                    }
                    if (dataSnapshot.child("creator").getValue() != null){
                        creatorID = dataSnapshot.child("creator").getValue().toString();
                    }
                    MessageObject mMessageObject = new MessageObject(dataSnapshot.getKey(),creatorID,text);
                    messageList.add(mMessageObject);
                    mLayoutManager.scrollToPosition(messageList.size()-1);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(){
        if(!messageText.getText().toString().isEmpty()){
            DatabaseReference newMssageDb = mChatDB.push();

            Map newMessageMap = new HashMap<>();
            newMessageMap.put("text",messageText.getText().toString());
            newMessageMap.put("creator", FirebaseAuth.getInstance().getUid());
            newMssageDb.updateChildren(newMessageMap);
        }
        messageText.setText(null);
    }

    private void initializeRecyclerView() {

        mChat=findViewById(R.id.messageRV);


        //Adapter
        mAdapter=new MessageAdapter(messageList);


        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        mChat.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        mChat.setLayoutManager(mLayoutManager);
        mChat.setItemAnimator(new DefaultItemAnimator());
        mChat.setAdapter(mAdapter);

    }
}
