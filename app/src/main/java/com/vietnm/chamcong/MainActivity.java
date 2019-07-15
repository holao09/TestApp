package com.vietnm.chamcong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity {

    TextView test;
    private boolean binded=false;
    private tracking track_service;
    ServiceConnection track_service_conn = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                tracking.TrackingBinder binder = (tracking.TrackingBinder) service;
                track_service = binder.getService();
            binded = true;
        }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            binded = false;
        }
        };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test = findViewById(R.id.GPS_Info);
    }
    @Override
    protected void onStart() {
        super.onStart();

        // Tạo đối tượng Intent cho WeatherService.
        Intent intent = new Intent(this, tracking.class);

        // Gọi method bindService(..) để giàng buộc dịch vụ với giao diện.
        this.bindService(intent, track_service_conn, Context.BIND_AUTO_CREATE);
    }

    // Khi Activity ngừng hoạt động.
    @Override
    protected void onStop() {
        super.onStop();
        if (binded) {
            // Hủy giàng buộc kết nối với dịch vụ.
            this.unbindService(track_service_conn);
            binded = false;
        }
    }

    public void startTrack(View view)  {

        LatLng loc =  this.track_service.getGPS();

        test.append("Lat : "+ loc.latitude + "\n Long : " +loc.longitude);
//        if (this.track_service.getStatus())
//        test.setText("status  OK" );
//        else test.setText("status  Not OK" );
    }

    // Method này được gọi khi người dùng Click vào nút Stop.
    public void stopTrack(View view)  {
        test.setText("GPS::");
    }


}