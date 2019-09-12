package com.duan.android.activitystartup.sensor;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.duan.android.activitystartup.R;

import java.util.List;

/**
 #define SENSOR_TYPE_ACCELEROMETER       1 // 加速度
 #define SENSOR_TYPE_MAGNETIC_FIELD      2 // 磁力
 #define SENSOR_TYPE_ORIENTATION         3 // 方向
 #define SENSOR_TYPE_GYROSCOPE           4 // 陀螺仪
 #define SENSOR_TYPE_LIGHT               5 // 光线感应
 #define SENSOR_TYPE_PRESSURE            6 // 压力
 #define SENSOR_TYPE_TEMPERATURE         7 // 温度
 #define SENSOR_TYPE_PROXIMITY           8 // 接近 (距离)
 #define SENSOR_TYPE_GRAVITY             9 // 重力
 #define SENSOR_TYPE_LINEAR_ACCELERATION 10// 线性加速度
 #define SENSOR_TYPE_ROTATION_VECTOR     11// 旋转矢量
 */

public class SensorActivity extends AppCompatActivity {

    private TextView tvSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        tvSensor = findViewById(R.id.tv_sensor); // 展示设备传感器们
        obtainSensors();
    }


    // 获取当前设备传感器列表 sensor.getStringType() 要求 API>=20
    @SuppressLint("NewApi")
    private void obtainSensors() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        Sensor sensor;
        for (int i = 0; i < sensorList.size(); i++) {
            sensor = sensorList.get(i);
            tvSensor.append("\n  " +String.valueOf(i+1) + ": \n 类别：" + sensor.getStringType() + "\n 名称：" + sensor.getName() + "\n 厂商：" + sensor.getVendor() + "\n ");
        }
    }

}
