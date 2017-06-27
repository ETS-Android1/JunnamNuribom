package com.junnam.nuribom.View;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.junnam.nuribom.DTO.DetailData;
import com.junnam.nuribom.DTO.MemberData;
import com.junnam.nuribom.DTO.ReviewItemData;
import com.junnam.nuribom.R;
import com.junnam.nuribom.Util.ActivityManager;
import com.junnam.nuribom.Util.JsonParse;
import com.junnam.nuribom.Util.NuribomApplication;
import com.junnam.nuribom.Util.URLDefine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener , ReviewFragment.OnFragmentInteractionListener{

    private NuribomApplication app;
    private MemberData memberData;
    private ActivityManager activityManager;

    private ListView listView;
    private ProgressBar progressBar;
    private TextView textView_name, textView_type, textView_addr, textView_tel, textView_rate;
    private RatingBar ratingBar_rate;
    private Button button_route, button_call, button_review;

    private int detail_type;
    private int detail_idx;
    private double detail_sx;
    private double detail_sy;

    private DetailData detailData;
    private Menu menu;
    private int favor_flag;

    private ArrayList<ReviewItemData> reviewItemDatas;
    private ArrayAdapter<ReviewItemData> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initLayout();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        intent.getExtras();


        detail_type = intent.getIntExtra("type", 0);
        detail_idx = intent.getIntExtra("idx", -1);
        detail_sx = intent.getDoubleExtra("sx", 126.4631714);
        detail_sy = intent.getDoubleExtra("sy", 34.8161102);



        new DetailAsyncTask().execute();
        new ReviewListAsyncTask().execute();

    }

    private void initLayout() {

        app = (NuribomApplication)getApplication();

        memberData = app.instanceMemberData();

        activityManager = app.instanceActivityManager();
        activityManager.addActivity(this);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        listView = (ListView)findViewById(R.id.listView);

        View header = getLayoutInflater().inflate(R.layout.listview_header, null, false);

        textView_name = (TextView)header.findViewById(R.id.name);
        textView_type = (TextView)header.findViewById(R.id.type);
        textView_addr = (TextView)header.findViewById(R.id.addr);
        textView_tel = (TextView)header.findViewById(R.id.tel);
        textView_rate = (TextView)header.findViewById(R.id.textView_rate);

        button_route = (Button)header.findViewById(R.id.button_route);
        button_call = (Button)header.findViewById(R.id.button_call);
        button_review = (Button)header.findViewById(R.id.button_review);

        ratingBar_rate = (RatingBar)header.findViewById(R.id.ratingBar);


        button_route.setOnClickListener(this);
        button_call.setOnClickListener(this);
        button_review.setOnClickListener(this);

        listView.addHeaderView(header);

       // mCustomArrayAdapter = new CustomArrayAdapter(MainActivity.this, R.layout.list_row, mArrays);

        // Adapter 연결
        reviewItemDatas = new ArrayList<>();
        adapter = new ReviewItemViewAdapter(getApplicationContext(), R.layout.listview_review, reviewItemDatas);

        listView.setAdapter(adapter) ;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_route:
                Uri uri = Uri.parse("http://m.map.naver.com/route.nhn?menu=route&sname=내위치&sx=" +detail_sx +"&sy=" +detail_sy
                +"&ename=" +detailData.getName() +"&ex=" +detailData.getLng() + "&ey=" +detailData.getLat() +"&pathType=0&showMap=true");
                Intent it  = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(it);
                break;

            case R.id.button_call:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+detailData.getTel()));
                startActivity(intent);
                break;

            case R.id.button_review:
                if(memberData.getMember_id() != null)
                {
                    ReviewFragment fragment = ReviewFragment.newInstance(memberData.getMember_id(), detail_type, detail_idx, detailData.getType(), detailData.getName());
                    fragment.show(getSupportFragmentManager(), "dialog");
                } else {
                    Toast.makeText(DetailActivity.this, "로그인이 필요한 서비스입니다.", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public class DetailAsyncTask extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            return JsonDetail();
        }

        @Override
        protected void onPostExecute(String result) {
//            progressBar.setVisibility(View.GONE);
            if (result.equals("true")) {

                textView_name.setText(detailData.getName());
                textView_name.setContentDescription("이름 , "+ detailData.getName());
                textView_type.setText(detailData.getType());
                textView_type.setContentDescription("타입 ," +detailData.getType());
                textView_addr.setText("주소 : " +detailData.getAddr());
                textView_addr.setContentDescription("주소 : "+ detailData.getAddr() +", ");
                textView_tel.setText("전화번호 : " +detailData.getTel());
                ratingBar_rate.setRating(detailData.getRate());
                textView_rate.setText(String.valueOf(detailData.getRate())+"점");

                if(memberData.getMember_id() != null)
                    new FavorCheckAsyncTask().execute();
                else
                    progressBar.setVisibility(View.GONE);
            } else {
                Toast.makeText(DetailActivity.this, "정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }


    private String JsonDetail() {

        String result = "";
        try {
            JsonParse jsonParse = new JsonParse();
            JSONObject json;

            json = jsonParse.getJSONFromUrl(URLDefine.SELECT_DETAIL_URL +"type=" +detail_type +"&idx=" +detail_idx);

            if (json != null) {
                JSONArray contacts = json.getJSONArray("result");

                if(contacts.length() == 0)
                    return "false";

                for (int i = 0; i < contacts.length(); i++) {

                    JSONObject c = contacts.getJSONObject(i);

                    detailData = new DetailData();
                    switch (detail_type) {
                        case 1:
                            detailData.setName(c.getString("hospital_name"));
                            detailData.setType(c.getString("hospital_type"));
                            detailData.setAddr(c.getString("hospital_addr"));
                            detailData.setTel(c.getString("hospital_tel"));
                            break;
                        case 2:
                            detailData.setName(c.getString("center_name"));
                            detailData.setType(c.getString("center_type"));
                            detailData.setAddr(c.getString("center_addr"));
                            detailData.setTel(c.getString("center_tel"));
                            break;
                        case 3:
                            detailData.setName(c.getString("emergency_name"));
                            detailData.setType("응급 의료 기관");
                            detailData.setAddr(c.getString("emergency_addr"));
                            detailData.setTel(c.getString("emergency_tel"));
                            break;
                        case 4:
                            detailData.setName(c.getString("dentist_name"));
                            detailData.setType("약국");
                            detailData.setAddr(c.getString("dentist_addr"));
                            detailData.setTel(c.getString("dentist_tel"));
                            break;
                    }


                    detailData.setRate(Float.parseFloat(c.getString("rate")));
                    detailData.setLat(Double.parseDouble(c.getString("lat")));
                    detailData.setLng(Double.parseDouble(c.getString("lng")));


                }
                result = "true";
            } else {
                result = "false";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("MyTag", "Error = " + e.toString());
            e.printStackTrace();
            result = "false";
        }
        return result;
    }


    public class FavorCheckAsyncTask extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
//            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            return JsonFavorCheck();
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            if (result.equals("true")) {
                menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_favorite_black));
                menu.getItem(0).setTitle("단골장소 지정 해제 버튼");
                favor_flag = 1;
            } else {
                menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_favorite_border_black));
                menu.getItem(0).setTitle("단골장소 지정 버튼");
                favor_flag = 0;
            }
            super.onPostExecute(result);
        }
    }


    private String JsonFavorCheck() {

        String result = "";
        try {
            JsonParse jsonParse = new JsonParse();
            JSONObject json;

            json = jsonParse.getJSONFromUrl(URLDefine.SELECT_FAVOR_URL +"type=" +detail_type +"&type_idx=" +detail_idx +"&member_id=" +memberData.getMember_id());

            if (json != null) {
                JSONArray contacts = json.getJSONArray("result");

                for (int i = 0; i < contacts.length(); i++) {

                    JSONObject c = contacts.getJSONObject(i);

                   if(c.getString("isFavor").equals("false"))
                       return "false";
                }
                result = "true";
            } else {
                result = "false";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("MyTag", "Error = " + e.toString());
            e.printStackTrace();
            result = "false";
        }
        return result;
    }

    public class ReviewListAsyncTask extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            reviewItemDatas.clear();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            return JsonReviewList();
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            if (result.equals("true")) {
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(DetailActivity.this, "후기 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }


    private String JsonReviewList() {

        String result = "";
        try {
            JsonParse jsonParse = new JsonParse();
            JSONObject json;

            json = jsonParse.getJSONFromUrl(URLDefine.SELECT_REVIEW_URL +"type=" +detail_type +"&type_idx=" +detail_idx);

            if (json != null) {
                JSONArray contacts = json.getJSONArray("result");

                for (int i = 0; i < contacts.length(); i++) {

                    JSONObject c = contacts.getJSONObject(i);

                    ReviewItemData reviewItemData = new ReviewItemData();
                    reviewItemData.setMember_id(c.getString("member_id"));
                    reviewItemData.setContent(c.getString("content"));
                    reviewItemData.setIdx(Integer.parseInt(c.getString("idx")));
                    reviewItemData.setRate(Float.parseFloat(c.getString("rate")));


                    reviewItemDatas.add(reviewItemData);
                }
                result = "true";
            } else {
                result = "false";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("MyTag", "Error = " + e.toString());
            e.printStackTrace();
            result = "false";
        }
        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        this.menu = menu;
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            activityManager.removeActvity(DetailActivity.this);
            overridePendingTransition(0,0);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.action_favor) {
            if(memberData.getMember_id() != null)
                new FavorAsyncTask().execute();
            else
                Toast.makeText(DetailActivity.this, "로그인이 필요한 서비스입니다.", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }


    public class FavorAsyncTask extends AsyncTask<Void, String, String> {


        @Override
        protected void onPreExecute() {
//            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            return JsonFavor();
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            if (result.equals("true")) {

                if(favor_flag == 1) {
                    menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_favorite_border_black));
                    menu.getItem(0).setTitle("단골장소 지정 버튼");
                    Toast.makeText(DetailActivity.this, "단골장소를 해제하였습니다.", Toast.LENGTH_SHORT).show();
                    favor_flag = 0;
                } else if(favor_flag == 0) {
                    menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_favorite_black));
                    menu.getItem(0).setTitle("단골장소 지정 해제 버튼");
                    Toast.makeText(DetailActivity.this, "단골 장소로 지정하였습니다.", Toast.LENGTH_SHORT).show();
                    favor_flag = 1;
                }
            } else {

            }
            super.onPostExecute(result);
        }
    }


    private String JsonFavor() {

        String result = "";
        try {
            JsonParse jsonParse = new JsonParse();
            JSONObject json = null;

            if(favor_flag == 1) {
                json = jsonParse.getJSONFromUrl(URLDefine.DELETE_FAVOR_URL + "type=" + detail_type + "&type_idx=" + detail_idx + "&member_id=" + memberData.getMember_id());
            } else if(favor_flag == 0) {
                String type_name = URLEncoder.encode(detailData.getType(), "UTF-8");
                String name = URLEncoder.encode(detailData.getName(), "UTF-8");

                json = jsonParse.getJSONFromUrl(URLDefine.INSERT_FAVOR_URL + "type=" + detail_type + "&type_idx=" + detail_idx + "&member_id=" + memberData.getMember_id() +"&type_name=" +type_name + "&name=" +name);
            }
            if (json != null) {
                JSONArray contacts = json.getJSONArray("result");

                for (int i = 0; i < contacts.length(); i++) {

                    JSONObject c = contacts.getJSONObject(i);

                    if(c.getString("isFavor").equals("false"))
                        return "false";
                }
                result = "true";
            } else {
                result = "false";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("MyTag", "Error = " + e.toString());
            e.printStackTrace();
            result = "false";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public void onFragmentInteraction(Float newRate, String member_id, String content, float rate) {
        ReviewItemData reviewItemData = new ReviewItemData();
        reviewItemData.setIdx(-1);
        reviewItemData.setMember_id(member_id);
        reviewItemData.setContent(content);
        reviewItemData.setRate(rate);

        reviewItemDatas.add(0, reviewItemData);
        adapter.notifyDataSetChanged();

        ratingBar_rate.setRating(newRate);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        activityManager.removeActvity(DetailActivity.this);
        overridePendingTransition(0,0);
        startActivity(intent);
    }
}
