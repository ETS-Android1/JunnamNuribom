package com.junnam.nuribom.View;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.junnam.nuribom.DTO.ListItemData;
import com.junnam.nuribom.DTO.MarkerData;
import com.junnam.nuribom.DTO.MemberData;
import com.junnam.nuribom.DTO.MyFavorData;
import com.junnam.nuribom.DTO.MyReviewData;
import com.junnam.nuribom.DTO.NuribomData;
import com.junnam.nuribom.R;
import com.junnam.nuribom.Util.ActivityManager;
import com.junnam.nuribom.Util.JsonParse;
import com.junnam.nuribom.Util.NuribomApplication;
import com.junnam.nuribom.Util.URLDefine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , FilterFragment.OnFragmentInteractionListener,ItemFragment.OnListFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener, JoinFragment.OnFragmentInteractionListener, MyFavorFragment.OnFavorFragmentInteractionListener, MyReviewFragment.OnReviewFragmentInteractionListener,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnInfoWindowClickListener {

    private ItemFragment itemFragment;

    private ProgressBar progressBar;
    private NuribomData nuribomData;
    private FloatingActionButton fab;
    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private LatLng myLatLng;
    private Marker currLocationMarker;
    private HashMap<Marker, MarkerData> markers;
    private BitmapDescriptor me_icon;

    private TimerTask timerTask;
    private Timer timer;
    private LocationManager locationManager;
    private int connectCount = 0;

    private View mapView;
    private View frameView;

    private boolean mapVisible = true;

    private NuribomApplication app;
    private ActivityManager activityManager;
    private MemberData memberData;

    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initLayout();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                if(mapVisible == true) {

                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.layout_frame);
                    Log.d("MyTag", "Enter postExecute");
                    if (fragment instanceof ItemFragment) {
                        Log.d("MyTag", "Enter ItemFragment Function call");
                        ((ItemFragment) fragment).setData(myLatLng, nuribomData);
                    }

                    fab.setImageResource(R.drawable.icon_map);
                    fab.setContentDescription("지도로 보기");
                    frameView.setVisibility(View.VISIBLE);
                    mapView.setVisibility(View.GONE);
                    mapVisible = false;
                } else {
                    fab.setImageResource(R.drawable.icon_list);
                    fab.setContentDescription("리스트로 보기");
                    frameView.setVisibility(View.GONE);
                    mapView.setVisibility(View.VISIBLE);
                    mapVisible = true;

                }

            }
        });

//        nuribomData = new NuribomData();

        if(nuribomData.getDataSize() == 0) {
            new HospitalAsyncTask().execute();
            new CenterAsyncTask().execute();
            new EmergencyAsyncTask().execute();
            new DentistAsyncTask().execute();
        }

    }

    private void initLayout() {

        app = (NuribomApplication)getApplication();
        memberData = app.instanceMemberData();
        Log.d("MyTag", "GPSCheck = " +memberData.isGPSPermission());

        if(!memberData.isLogin()) {
            memberData.getPreferences();
            Log.d("MyTag", "member_name = " +memberData.getMember_name());
            if (memberData.getMember_name() != null) {
                Toast.makeText(MainActivity.this, memberData.getMember_name() + "님 환영합니다.", Toast.LENGTH_SHORT).show();
                setMenuLogin();
            } else {
                setMenuLogout();
            }
        } else {
            setMenuLogin();
        }

        activityManager = app.instanceActivityManager();
        activityManager.addActivity(this);

        nuribomData = app.instanceNuribomData();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapView = (View)findViewById(R.id.map);
        frameView = (View)findViewById(R.id.layout_frame);

        frameView.setVisibility(View.GONE);

        mapFragment.getMapAsync(this);

        itemFragment = ItemFragment.newInstance(1);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layout_frame, itemFragment).commit();

        timerTask = new TimerTask() {
            @Override
            public void run() {

                if (locationManager == null) {
                    locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

                }

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    if(connectCount == 0) {
                        buildGoogleApiClient();
                        mGoogleApiClient.connect();
                        connectCount++;
                    }
                } else {
                    connectCount = 0;
                }

            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 3000, 5000);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    public void setMenuLogin() {
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.name);
        TextView nav_email = (TextView)hView.findViewById(R.id.email);
        nav_user.setText(memberData.getMember_name());
        nav_email.setText(memberData.getMember_id());
        nav_email.setVisibility(View.VISIBLE);
        navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_join).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_fav).setVisible(true);
        navigationView.getMenu().findItem(R.id.nav_review).setVisible(true);
        navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
    }

    public void setMenuLogout() {
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.name);
        TextView nav_email = (TextView)hView.findViewById(R.id.email);
        nav_user.setText("방문자");
        nav_email.setVisibility(View.GONE);
        navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
        navigationView.getMenu().findItem(R.id.nav_join).setVisible(true);
        navigationView.getMenu().findItem(R.id.nav_fav).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_review).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);

        myLatLng= new LatLng(34.8161102, 126.4631714);

        MarkerOptions markerOptions = new MarkerOptions();
        me_icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_me);
        markerOptions.icon(me_icon);

        markerOptions.position(myLatLng);
        markerOptions.title("내 위치");
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        currLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 14.0f));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }

        if(!memberData.isGPSPermission()) {
            if (chkGpsService()) {
                buildGoogleApiClient();
                mGoogleApiClient.connect();
                mMap.setMyLocationEnabled(true);
            }
        }

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setCompassEnabled(false);

        if (nuribomData != null) {
            addMarkers();
        }
    }

    private boolean chkGpsService() {

        memberData.setGPSPermission(true);
        Log.d("MyTag", "GPSCheck2 = " +memberData.isGPSPermission());
        String gps = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {

            // GPS OFF 일때 Dialog 표시
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
            gsDialog.setTitle("위치 서비스 설정");
            gsDialog.setMessage("무선 네트워크 사용,\n GPS 위성 사용을 모두 체크하셔야 정확한 위치 서비스가 가능합니다.\n위치 서비스 기능을 설정하시겠습니까?\n(취소 시 전남도청으로\n 위치가 초기화 됩니다)");
            gsDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // GPS설정 화면으로 이동
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }}).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    return ;
                }
            }).create().show();
            return false;

        } else {

            return true;
        }
    }

    protected synchronized void buildGoogleApiClient() {

        Log.d("MyTag", "Enter build Client");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public void onConnected(Bundle bundle) {

        mMap.setMyLocationEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {

            myLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(myLatLng);
            BitmapDrawable me_icon = (BitmapDrawable) getResources().getDrawable(R.drawable.icon_me);
            Bitmap b = me_icon.getBitmap();
            Bitmap me_marker = Bitmap.createScaledBitmap(b, 150, 200, false);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(me_marker));
            if (currLocationMarker != null) {
                currLocationMarker.remove();
            }
            currLocationMarker = mMap.addMarker(markerOptions);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(myLatLng).zoom(14).build();

            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {

        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }

        if(me_icon != null) {

            myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(myLatLng);
            markerOptions.icon(me_icon);
            if (currLocationMarker != null) {
                currLocationMarker.remove();
            }
            currLocationMarker = mMap.addMarker(markerOptions);
        }
    }


    private void addMarkers() {

        mMap.clear();


        if(me_icon != null) {

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(myLatLng);
            markerOptions.icon(me_icon);
            if (currLocationMarker != null) {
                currLocationMarker.remove();
            }
            currLocationMarker = mMap.addMarker(markerOptions);
        }

        markers = new HashMap<>();

        for(int i=0; i<nuribomData.getDataSize(); i++) {

            MarkerData markerData = nuribomData.getData(i);

            if(markerData.getType() ==1 && nuribomData.isHospitalVisible() == false)
                continue;

            if(markerData.getType() == 2 && nuribomData.isCenterVisible() == false)
                continue;

            if(markerData.getType() == 3 && nuribomData.isEmergencyVisible() == false)
                continue;

            if(markerData.getType() == 4 && nuribomData.isDentistVisible() ==false)
                continue;

            if(markerData.getLocaton().latitude != 0.0 && markerData.getLocaton().longitude != 0.0) {
                MarkerOptions markerOptions = new MarkerOptions();

                BitmapDescriptor icon = null;
                switch (markerData.getType()) {
                    case 1:
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_hospital);
                        break;
                    case 2:
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_center);
                        break;
                    case 3:
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_ambulance);
                        break;
                    case 4:
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_medicine);
                        break;
                }
                markerOptions.icon(icon);
                markerOptions.position(markerData.getLocaton());
                markerOptions.title(markerData.getName());

                Marker marker = mMap.addMarker(markerOptions);
                markers.put(marker, markerData);
            }
        }

    }



    public class HospitalAsyncTask extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Void... params) {

            return JsonHospitaltInfo();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("true")) {

            } else {
                Toast.makeText(MainActivity.this, "병원 정보를 가져오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }

    public class CenterAsyncTask extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {

            return JsonCenterInfo();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("true")) {

            } else {
                Toast.makeText(MainActivity.this, "재활센터 정보를 가져오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }

    public class EmergencyAsyncTask extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {

            return JsonEmergencyInfo();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("true")) {

            } else {
                Toast.makeText(MainActivity.this, "응급의료원 정보를 가져오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }

    public class DentistAsyncTask extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {

            return JsonDentistInfo();
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            if (result.equals("true")) {
                addMarkers();
            } else {
                Toast.makeText(MainActivity.this, "약국 정보를 가져오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }

    private String JsonHospitaltInfo() {

        String result = "";
        try {
            JsonParse jsonParse = new JsonParse();
            JSONObject json;

            json = jsonParse.getJSONFromUrl(URLDefine.SELECT_HOSPITAL_URL);

            if (json != null) {
                JSONArray contacts = json.getJSONArray("result");

                for (int i = 0; i < contacts.length(); i++) {

                    JSONObject c = contacts.getJSONObject(i);

                    MarkerData markerData = new MarkerData();
                    markerData.setIdx(Integer.parseInt(c.getString("idx")));
                    markerData.setType(1);
                    markerData.setName(c.getString("hospital_name"));
                    double lat = Double.parseDouble(c.getString("lat"));
                    double lng = Double.parseDouble(c.getString("lng"));
                    markerData.setLocaton(new LatLng(lat, lng));
                    markerData.setAddr(c.getString("hospital_addr"));
                    markerData.setRate(Float.parseFloat(c.getString("rate")));

                    nuribomData.addData(markerData);
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

    private String JsonCenterInfo() {

        String result = "";
        try {
            JsonParse jsonParse = new JsonParse();
            JSONObject json;

            json = jsonParse.getJSONFromUrl(URLDefine.SELECT_CENTER_URL);

            if (json != null) {
                JSONArray contacts = json.getJSONArray("result");

                for (int i = 0; i < contacts.length(); i++) {

                    JSONObject c = contacts.getJSONObject(i);

                    MarkerData markerData = new MarkerData();
                    markerData.setIdx(Integer.parseInt(c.getString("idx")));
                    markerData.setType(2);
                    markerData.setName(c.getString("center_name"));
                    double lat = Double.parseDouble(c.getString("lat"));
                    double lng = Double.parseDouble(c.getString("lng"));
                    markerData.setLocaton(new LatLng(lat, lng));
                    markerData.setAddr(c.getString("center_addr"));
                    markerData.setRate(Float.parseFloat(c.getString("rate")));

                    nuribomData.addData(markerData);

                }
                result = "true";
            } else {
                result = "false";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("MyTag", "Error = " + e.toString());
            result = "false";
        }
        return result;
    }


    private String JsonEmergencyInfo() {

        String result = "";
        try {
            JsonParse jsonParse = new JsonParse();
            JSONObject json;

            json = jsonParse.getJSONFromUrl(URLDefine.SELECT_EMERGENCY_URL);

            if (json != null) {
                JSONArray contacts = json.getJSONArray("result");

                for (int i = 0; i < contacts.length(); i++) {

                    JSONObject c = contacts.getJSONObject(i);

                    MarkerData markerData = new MarkerData();
                    markerData.setIdx(Integer.parseInt(c.getString("idx")));
                    markerData.setType(3);
                    markerData.setName(c.getString("emergency_name"));
                    double lat = Double.parseDouble(c.getString("lat"));
                    double lng = Double.parseDouble(c.getString("lng"));
                    markerData.setLocaton(new LatLng(lat, lng));
                    markerData.setAddr(c.getString("emergency_addr"));
                    markerData.setRate(Float.parseFloat(c.getString("rate")));

                    nuribomData.addData(markerData);

                }
                result = "true";
            } else {
                result = "false";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("MyTag", "Error = " + e.toString());
            result = "false";
        }
        return result;
    }

    private String JsonDentistInfo() {

        String result = "";
        try {
            JsonParse jsonParse = new JsonParse();
            JSONObject json;

            json = jsonParse.getJSONFromUrl(URLDefine.SELECT_DENTIST_URL);

            if (json != null) {
                JSONArray contacts = json.getJSONArray("result");

                for (int i = 0; i < contacts.length(); i++) {

                    JSONObject c = contacts.getJSONObject(i);

                    MarkerData markerData = new MarkerData();
                    markerData.setIdx(Integer.parseInt(c.getString("idx")));
                    markerData.setType(4);
                    markerData.setName(c.getString("dentist_name"));
                    double lat = Double.parseDouble(c.getString("lat"));
                    double lng = Double.parseDouble(c.getString("lng"));
                    markerData.setLocaton(new LatLng(lat, lng));
                    markerData.setAddr(c.getString("dentist_addr"));
                    markerData.setRate(Float.parseFloat(c.getString("rate")));

                    nuribomData.addData(markerData);

                }
                result = "true";
            } else {
                result = "false";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("MyTag", "Error = " + e.toString());
            result = "false";
        }
        return result;
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        MarkerData markerData = markers.get(marker);


        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);

        intent.putExtra("type", markerData.getType());
        intent.putExtra("idx", markerData.getIdx());
        intent.putExtra("sx", myLatLng.longitude);
        intent.putExtra("sy", myLatLng.latitude);

        activityManager.removeActvity(MainActivity.this);
        overridePendingTransition(0,0);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (exit_count == 0) {
                Toast.makeText(this, "뒤로가기를 한번 더 누르시면 종료됩니다.",
                        Toast.LENGTH_SHORT).show();
                exitHandler.sendEmptyMessageDelayed(0, 2000);
                exit_count++;

            } else if (exit_count == 1) {
                activityManager.removeAllActvity();
                memberData = null;
                finish();
                System.runFinalizersOnExit(true);
                System.exit(0);
                return;
            }
        }
    }

    int exit_count = 0;


    Handler exitHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                exit_count = 0;
            }
            return false;
        }
    });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            if(nuribomData != null) {
                FilterFragment fragment = FilterFragment.newInstance(nuribomData.isHospitalVisible(), nuribomData.isCenterVisible(),
                        nuribomData.isEmergencyVisible(), nuribomData.isDentistVisible());
                fragment.show(getSupportFragmentManager(), "dialog");
            } else {
                FilterFragment fragment = FilterFragment.newInstance(true, true,
                        true, true);
                fragment.show(getSupportFragmentManager(), "dialog");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_fav) {
            MyFavorFragment fragment = MyFavorFragment.newInstance(1, memberData.getMember_id());
            fragment.show(getSupportFragmentManager(), "dialog");
        } else if (id == R.id.nav_review) {
            MyReviewFragment fragment = MyReviewFragment.newInstance(1, memberData.getMember_id());
            fragment.show(getSupportFragmentManager(), "dialog");
        } else if (id == R.id.nav_login) {
            LoginFragment fragment = LoginFragment.newInstance("param1", "param2");
            fragment.show(getSupportFragmentManager(), "dialog");
        } else if (id == R.id.nav_join) {
            JoinFragment fragment = JoinFragment.newInstance("param1", "param2");
            fragment.show(getSupportFragmentManager(), "dialog");
        } else if (id == R.id.nav_logout) {
            String member_name = memberData.getMember_name();
            memberData.removeLogin();
            memberData.setLogin(false);
            Toast.makeText(MainActivity.this, member_name + "님 로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            setMenuLogout();
        } else if (id == R.id.nav_info) {
            InfoFragment fragment = InfoFragment.newInstance("param1", "param2");
            fragment.show(getSupportFragmentManager(), "dialog");
        }else if (id == R.id.nav_giude) {
            DialogGuideFragment fragment = DialogGuideFragment.newInstance("param1", "param2");
            fragment.show(getSupportFragmentManager(), "dialog");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Filter Fragment Interaction
    public void onFragmentInteraction(boolean hospitalVisible, boolean centerVisible, boolean emergencyVisible, boolean dentistVisible){

        if(nuribomData != null) {
            nuribomData.setHospitalVisible(hospitalVisible);
            nuribomData.setCenterVisible(centerVisible);
            nuribomData.setEmergencyVisible(emergencyVisible);
            nuribomData.setDentistVisible(dentistVisible);

            addMarkers();
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.layout_frame);
            Log.d("MyTag", "Enter postExecute");
            if (fragment instanceof ItemFragment) {
                Log.d("MyTag", "Enter ItemFragment Function call");
                ((ItemFragment) fragment).setData(myLatLng, nuribomData);
            }
        }
    }

    //Login Fragment Interaction
    @Override
    public void onFragmentInteraction(MemberData memberData) {
        this.memberData = memberData;
        app.setMemberData(memberData);
        this.memberData.setLogin(true);
        this.memberData.setmContext(this);
        this.memberData.saveLogin();
        setMenuLogin();
        Toast.makeText(MainActivity.this, memberData.getMember_name() + "님 환영합니다.", Toast.LENGTH_SHORT).show();
    }

    //Join Fragment Interaction
//    @Override
//    public void onFragmentInteraction(MemberData memberData) {
//
//    }

    //List Fragment Interaction
    @Override
    public void onListFragmentInteraction(ListItemData item) {

        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);

        intent.putExtra("type", item.getType());
        intent.putExtra("idx", item.getIdx());
        intent.putExtra("sx", myLatLng.longitude);
        intent.putExtra("sy", myLatLng.latitude);

        activityManager.removeActvity(MainActivity.this);
        overridePendingTransition(0,0);
        startActivity(intent);
    }

    @Override
    public void OnFavorFragmentInteractionListener(MyFavorData item) {

        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);

        intent.putExtra("type", item.getType());
        intent.putExtra("idx", item.getType_idx());
        intent.putExtra("sx", myLatLng.longitude);
        intent.putExtra("sy", myLatLng.latitude);

        activityManager.removeActvity(MainActivity.this);
        overridePendingTransition(0,0);
        startActivity(intent);

    }


    @Override
    public void OnReviewFragmentInteractionListener(MyReviewData item) {
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);

        intent.putExtra("type", item.getType());
        intent.putExtra("idx", item.getType_idx());
        intent.putExtra("sx", myLatLng.longitude);
        intent.putExtra("sy", myLatLng.latitude);

        activityManager.removeActvity(MainActivity.this);
        overridePendingTransition(0,0);
        startActivity(intent);
    }
}
