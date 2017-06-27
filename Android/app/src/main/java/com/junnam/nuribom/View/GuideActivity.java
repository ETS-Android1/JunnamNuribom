package com.junnam.nuribom.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.junnam.nuribom.DTO.MemberData;
import com.junnam.nuribom.R;
import com.junnam.nuribom.Util.ActivityManager;
import com.junnam.nuribom.Util.NuribomApplication;

public class GuideActivity extends AppCompatActivity {


    private NuribomApplication app;
    private ActivityManager activityManager;
    private MemberData memberData;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private ImageView imageView_page1, imageView_page2, imageView_page3, imageView_page4, imageView_page5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guide);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initLayout();
        loginCheck();

    }



    private void initLayout() {

        app = (NuribomApplication) getApplication();
        activityManager = app.instanceActivityManager();
        activityManager.addActivity(GuideActivity.this);
        memberData = app.instanceMemberData();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        imageView_page1 = (ImageView) findViewById(R.id.imageView_page1);
        imageView_page2 = (ImageView) findViewById(R.id.imageView_page2);
        imageView_page3 = (ImageView) findViewById(R.id.imageView_page3);
        imageView_page4 = (ImageView) findViewById(R.id.imageView_page4);
        imageView_page5 = (ImageView) findViewById(R.id.imageView_page5);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                mViewPager.setCurrentItem(position);

                switch (position) {
                    case 0:
                        imageView_page1.setImageResource(R.drawable.button_round_blue);
                        imageView_page2.setImageResource(R.drawable.button_round_darkgray);
                        imageView_page3.setImageResource(R.drawable.button_round_darkgray);
                        imageView_page4.setImageResource(R.drawable.button_round_darkgray);
                        imageView_page5.setImageResource(R.drawable.button_round_darkgray);
                        break;
                    case 1:
                        imageView_page1.setImageResource(R.drawable.button_round_darkgray);
                        imageView_page2.setImageResource(R.drawable.button_round_blue);
                        imageView_page3.setImageResource(R.drawable.button_round_darkgray);
                        imageView_page4.setImageResource(R.drawable.button_round_darkgray);
                        imageView_page5.setImageResource(R.drawable.button_round_darkgray);
                        break;
                    case 2:
                        imageView_page1.setImageResource(R.drawable.button_round_darkgray);
                        imageView_page2.setImageResource(R.drawable.button_round_darkgray);
                        imageView_page3.setImageResource(R.drawable.button_round_blue);
                        imageView_page4.setImageResource(R.drawable.button_round_darkgray);
                        imageView_page5.setImageResource(R.drawable.button_round_darkgray);
                        break;
                    case 3:
                        imageView_page1.setImageResource(R.drawable.button_round_darkgray);
                        imageView_page2.setImageResource(R.drawable.button_round_darkgray);
                        imageView_page3.setImageResource(R.drawable.button_round_darkgray);
                        imageView_page4.setImageResource(R.drawable.button_round_blue);
                        imageView_page5.setImageResource(R.drawable.button_round_darkgray);
                        break;
                    case 4:
                        imageView_page1.setImageResource(R.drawable.button_round_darkgray);
                        imageView_page2.setImageResource(R.drawable.button_round_darkgray);
                        imageView_page3.setImageResource(R.drawable.button_round_darkgray);
                        imageView_page4.setImageResource(R.drawable.button_round_darkgray);
                        imageView_page5.setImageResource(R.drawable.button_round_blue);
                        break;
                }

            }
        });

        ((Button) findViewById(R.id.button_start)).setOnClickListener(buttonClickListener);

    }

    private void loginCheck() {

        if(!memberData.isLogin()) {
            memberData.getPreferences();
            Log.d("MyTag", "member_name = " +memberData.getMember_name());
            if (memberData.getMember_name() != null) {
                Toast.makeText(GuideActivity.this, "자동 로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                activityManager.removeActvity(GuideActivity.this);
                overridePendingTransition(0, 0);
                startActivity(intent);
            }
        }


    }


    View.OnClickListener buttonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            activityManager.removeActvity(GuideActivity.this);
            overridePendingTransition(0,0);
            startActivity(intent);
        }
    };

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class
            // below).

            Fragment fragment = GuideFragment.newInstance(position);

            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

    }

    int exit_count = 0;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (exit_count == 0) {
                    Toast.makeText(this, "뒤로가기를 한번 더 누르시면 종료됩니다.",
                            Toast.LENGTH_SHORT).show();
                    exitHandler.sendEmptyMessageDelayed(0, 2000);
                    exit_count++;

                } else if (exit_count == 1) {
                    activityManager.removeAllActvity();
                    finish();
                    break;
                }

        }
        return false;
    }

    Handler exitHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                exit_count = 0;
            }
            return false;
        }
    });


}
