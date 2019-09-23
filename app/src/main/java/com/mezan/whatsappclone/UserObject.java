package com.mezan.whatsappclone;

public class UserObject {
    private String name,phone,uid;

    public UserObject(){
        
    }

    public UserObject(String uid,String name,String phone){
        this.name=name;
        this.phone=phone;
        this.uid=uid;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
