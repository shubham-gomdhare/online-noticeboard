package com.sghost.diems;

import com.orm.SugarRecord;

import java.util.List;

public class User extends SugarRecord {
    private  String mUsername;
    private  String mPassword;
    private boolean appOwner;
    public static final String COLUMN_IS_APP_OWNER = "APP_OWNER";


public User(){

}
    public User(String mUsername, String mPassword, boolean appOwner){
        this.mUsername = mUsername;
        this.mPassword = mPassword;
        this.appOwner = appOwner;
    }


    public boolean getappOwner() {
        return appOwner;
    }

    public void setappOwner(boolean appOwner) {
        this.appOwner = appOwner;
    }
    public String getmUsername(){
        return mUsername;
    }
    public void setmUsername(String mUsername){
        this.mUsername = mUsername;
    }
    public  String getmPassword(){
        return mPassword;
    }
    public void setmPassword(String mPassword){
        this.mPassword = mPassword;
    }
    public static User getAppOwner() {
        List<User> list = find(User.class, COLUMN_IS_APP_OWNER + "=?",
                new String[]{String.valueOf(1)}, null, null, "1");
        if(list.isEmpty()) return null;
        return list.get(0);
    }
}


