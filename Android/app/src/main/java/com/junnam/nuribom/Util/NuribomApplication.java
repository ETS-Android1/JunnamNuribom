package com.junnam.nuribom.Util;

import android.app.Application;
import android.util.Log;

import com.junnam.nuribom.DTO.MemberData;
import com.junnam.nuribom.DTO.NuribomData;

/**
 * Created by hong on 2016. 7. 11..
 */
public class NuribomApplication extends Application {

    private ActivityManager activityManager;
    private MemberData memberData;
    private NuribomData nuribomData;

    @Override
    public void onCreate() {

        super.onCreate();
    }


    public ActivityManager instanceActivityManager() {
        if(activityManager == null)
            activityManager = new ActivityManager();

        return activityManager;
    }

    public MemberData instanceMemberData() {
        if(this.memberData == null) {
            Log.d("MyTag", "enter this");
            this.memberData = new MemberData(this);
        }


        Log.d("MyTag", "enter this2");
        Log.d("MyTag", "member_id = " +this.memberData.getMember_id());
        Log.d("MyTag", "member_pass = " +this.memberData.getMember_pass());
        Log.d("MyTag", "member_name = " +this.memberData.getMember_name());
        return this.memberData;
    }

    public void setMemberData(MemberData memberData) {
        this.memberData = memberData;
    }

    public NuribomData instanceNuribomData() {
        if(nuribomData == null)
            nuribomData = new NuribomData();

        return nuribomData;
    }

}