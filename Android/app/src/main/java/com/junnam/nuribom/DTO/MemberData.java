package com.junnam.nuribom.DTO;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hong on 2016. 7. 11..
 */
public class MemberData {

    private Context mContext;
    private String member_id;
    private String member_pass;
    private String member_name;
    private boolean isLogin;
    private boolean GPSPermission;

    public MemberData() {
    }

    public MemberData(Context context) {
        mContext = context;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMember_pass() {
        return member_pass;
    }

    public void setMember_pass(String member_pass) {
        this.member_pass = member_pass;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public boolean isGPSPermission() {
        return GPSPermission;
    }

    public void setGPSPermission(boolean GPSPermission) {
        this.GPSPermission = GPSPermission;
    }

    public void saveLogin() {
        SharedPreferences pref = this.mContext.getSharedPreferences("login", mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("member_id", this.member_id);
        editor.putString("member_pass", this.member_pass);
        editor.putString("member_name", this.member_name);
        editor.commit();

    }

    public void removeLogin(){

        SharedPreferences pref = this.mContext.getSharedPreferences("login", mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();

        this.member_id = null;
        this.member_name = null;
        this.member_pass = null;
    }

    public void getPreferences(){
        SharedPreferences pref = mContext.getSharedPreferences("login", mContext.MODE_PRIVATE);
        this.member_id =  pref.getString("member_id", null);
        this.member_name = pref.getString("member_name", null);
        this.member_pass = pref.getString("member_pass", null);
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
