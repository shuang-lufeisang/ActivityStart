package com.duan.android.activitystartup.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.IOException;
import java.util.List;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.duan.android.activitystartup.R;

public class LocationActivity extends AppCompatActivity {

    static String TAG = "LocationActivity";
    String latLongString = "定位失败，点击重新定位！";
    private TextView city;
    private TextView showJW = null;
    private Button button;
    private LocationManager locationManager;

    private double latitude = 0;

    private double longitude = 0;

    private void initView(){
        initPermissions();
        button = findViewById(R.id.bt);
        showJW = findViewById(R.id.showJW);
        city =  findViewById(R.id.city);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getJW();
                    }
                }).start();

            }
        });
    }

    // 获取定位权限
    private void initPermissions(){
        PackageManager pkgManager = getPackageManager();
        boolean fineLocationPermission =
                pkgManager.checkPermission(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        getPackageName()) == PackageManager.PERMISSION_GRANTED;

        boolean coarseLocationPermission =
                pkgManager.checkPermission(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        getPackageName()) == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 23 && !fineLocationPermission || !coarseLocationPermission) {
            requestPermission();
        } else {}
    }
    private static final int REQUEST_PERMISSION = 0;
    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[] {
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                },
                REQUEST_PERMISSION
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        initView();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getJW();
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.arg1){
                case 1:
                    double[] data = (double[]) msg.obj;
                    showJW.setText("经度：" + data[0] + "\t纬度:" + data[1]);
                    break;
                case 2:
                    String locationMessage = (String) msg.obj;
                    city.setText(locationMessage);
                    break;
            }


        }
    };


    /**
     * 点击获取经纬度 - 当地城市
     */
    public void getJW() {
        Log.e(TAG, "=====  点击获取经纬度  =====");
        new Thread() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermission();
                }
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude();   // 经度
                    longitude = location.getLongitude(); // 纬度

                    double[] data = {latitude, longitude};
                    Message msg = handler.obtainMessage();
                    msg.obj = data;
                    msg.arg1 = 1;
                    handler.sendMessage(msg);

////////////////////////////////////////////////////////////////////////////////////////////////
                    /**
                     * 将经纬度转换成中文地址
                     */
                    List<Address> addList = null;
                    Geocoder ge = new Geocoder(getApplicationContext());
                    try {
                        addList = ge.getFromLocation(data[0], data[1], 1);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    String locationCity = "定位失败，点击重新定位！";
                    if (addList != null && addList.size() > 0) {
                        for (int i = 0; i < addList.size(); i++) {
                            Address address = addList.get(i);
                            latLongString = address.getLocality();
                            // feature: 指事物突出的特点（1.人的容貌特征；2.地理特征）
                            // getFeatureName: Returns the feature name of the address, for example, "Golden Gate Bridge", or null if it is unknown
                            Log.e(TAG, "address getFeatureName: " + address.getFeatureName());//嘉定区南翔商务中心(银翔路南)

                            // getAdminArea: Returns the administrative(行政的) area name of the address, for example, "CA",
                            Log.e(TAG, "address getAdminArea: " + address.getAdminArea());      //上海市
                            Log.e(TAG, "address getSubAdminArea: " + address.getSubAdminArea());//上海市

                            Log.e(TAG, "address getCountryName: " + address.getCountryName());//中国

                            Log.e(TAG, "address getLocality: " + address.getLocality());        //嘉定区
                            Log.e(TAG, "address getSubLocality: " + address.getSubLocality());  //null

                            Log.e(TAG, "address .getAddressLine(1): " + address.getAddressLine(1));//null

                            // getThoroughfare: Returns the thoroughfare(大道) name of the address, for example, "1600 Ampitheater Parkway",
                            Log.e(TAG, "address getThoroughfare: " + address.getThoroughfare());// 银翔路
                            Log.e(TAG, "address getSubThoroughfare: " + address.getSubThoroughfare());// 515号
                            locationCity = address.getAdminArea();
                        }
                    }else {
                        Log.e(TAG, "addList== null||size=0 " );
                    }
                    msg = handler.obtainMessage();
                    msg.obj = locationCity;
                    msg.arg1 = 2;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


}
