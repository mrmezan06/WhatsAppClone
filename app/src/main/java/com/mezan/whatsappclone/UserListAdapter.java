package com.mezan.whatsappclone;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {

    private List<UserObject> userList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, mobile;
        public LinearLayout mLayout;

        public MyViewHolder(View view) {
            super(view);

            name =  view.findViewById(R.id.name);
            mobile = view.findViewById(R.id.mobile);
            mLayout = view.findViewById(R.id.mLayout);
        }
    }


   public UserListAdapter(List<UserObject> userList){
        this.userList=userList;
   }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        UserObject user = userList.get(position);
        holder.name.setText(user.getName());
        holder.mobile.setText(user.getPhone());

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();
                FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat").child(key).setValue(true);
                FirebaseDatabase.getInstance().getReference().child("user").child(userList.get(position).getUid()).child("chat").child(key).setValue(true);
                Log.d("Firebase UID",userList.get(position).getUid().toString());
                Log.d("Firebase UID",FirebaseAuth.getInstance().getUid().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
