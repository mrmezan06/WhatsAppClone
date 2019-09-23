package com.mezan.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FindUserActivity extends AppCompatActivity {

    private RecyclerView mUserlist;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    List<UserObject> userList,contactList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

        contactList=new ArrayList<>();
        userList=new ArrayList<>();
        initializeRecyclerView();
        getContact();

    }


    private void initializeRecyclerView() {

        mUserlist=findViewById(R.id.userList);



        mAdapter=new UserListAdapter(userList);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        mUserlist.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        mUserlist.setLayoutManager(mLayoutManager);
        mUserlist.setItemAnimator(new DefaultItemAnimator());
        mUserlist.setAdapter(mAdapter);

    }
    private void getContact(){
        String ISOPrefix = getCountryISO();
        Cursor cursor=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            phone = phone.replace(" ","");
            phone = phone.replace("-","");
            phone = phone.replace("(","");
            phone = phone.replace(")","");

            if(!phone.startsWith("+")){
                phone = ISOPrefix + phone;
            }


            UserObject mContact = new UserObject("",name, phone);


            contactList.add(mContact);

            getUserDetails(mContact);

        }
    }

    private void getUserDetails(UserObject mContact) {
        DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("user");
        Query query = mUserDB.orderByChild("phone").equalTo(mContact.getPhone());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String name="",
                            phone="";
                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        if(childSnapshot.child("phone").getValue() != null){
                            phone = childSnapshot.child("phone").getValue().toString();
                            Log.d("XGF",phone);
                        }
                        if(childSnapshot.child("name").getValue() != null){
                            name = childSnapshot.child("name").getValue().toString();
                            Log.d("XGFNAME",name);
                        }
                        Log.d("XGFNai",name+phone);
                        UserObject mUser = new UserObject(childSnapshot.getKey(),name,phone);

                        Log.d("UserID",childSnapshot.getKey());

                        if(name.equals(phone)){
                            for(UserObject mContactIT : contactList){
                                if(mContactIT.getPhone().equals(mUser.getPhone())){
                                    mUser.setName(mContactIT.getName());
                                }
                            }
                        }


                        userList.add(mUser);
                        mAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String getCountryISO(){
        String iso = null;
        TelephonyManager telephonyManager= (TelephonyManager) getApplicationContext().getSystemService(TELEPHONY_SERVICE);

        if(telephonyManager.getNetworkCountryIso() != null){
            if(!telephonyManager.getNetworkCountryIso().equals("")){
                iso = telephonyManager.getNetworkCountryIso();
            }
        }
        return CountryToPhonePrefix.getPhone(iso);
    }

}


/*2h10m*/