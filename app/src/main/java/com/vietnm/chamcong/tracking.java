package com.vietnm.chamcong;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.fooock.lib.phone.tracker.Configuration;
import com.fooock.lib.phone.tracker.PhoneTracker;
import com.google.android.gms.maps.model.LatLng;

public class tracking extends Service {

    private static String LOG_TAG = "Tracking ";


    public class TrackingBinder extends Binder {

        public tracking getService()  {
            return tracking.this;
        }
    }
    private final IBinder binder = new TrackingBinder();

    public tracking() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(LOG_TAG,"onBind");
        return this.binder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(LOG_TAG, "onRebind");
        super.onRebind(intent);
    }

    PhoneTracker phoneTracker;
    PhoneTracker.GpsLocationListener GPS_Listener;
    Configuration configuration;
    Location GPS_Loc;
    long timesta = 0;

    @Override
    public void onCreate(){
        super.onCreate();
        // Tạo đối tượng MediaPlayer, chơi file nhạc của bạn.
        //mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.mysong);
        // Listen for missing permissions
        phoneTracker = new PhoneTracker(this);
        configuration  = new Configuration.Builder().create();
        phoneTracker.addPermissionListener(new PhoneTracker.PermissionListener() {
            @Override
            public void onPermissionNotGranted(String... permission) {
            }
        });
        //GPS_Loc.setLongitude(0);
        //GPS_Loc.setLatitude(0);
        Configuration.Gps gpsConf = new Configuration.Gps();
        gpsConf.setMinDistanceUpdate(10);
        gpsConf.setMinTimeUpdate(7000);

        phoneTracker.setConfiguration(configuration);
        phoneTracker.setGpsLocationListener(GPS_Listener);
        phoneTracker.start();
        // Set the listener to receive location updates from gps
        phoneTracker.setGpsLocationListener(new PhoneTracker.GpsLocationListener() {
            @Override
            public void onLocationReceived(long timestamp, Location location) {
                GPS_Loc = location;
                timesta = timestamp;
            }
        });
    }

    // Hủy bỏ dịch vụ.
    @Override
    public void onDestroy() {
        // Giải phóng nguồn dữ nguồn phát nhạc.
        //mediaPlayer.release();
        phoneTracker.stop();
        super.onDestroy();
    }

    public LatLng getGPS() {


        if (GPS_Loc != null)
        {
            LatLng temp = new LatLng(GPS_Loc.getLatitude(), GPS_Loc.getLongitude());
            return temp;
        }
        else
        {
            LatLng temp = new LatLng(0, 0);
            return temp;
        }
    }

    public boolean getStatus()    {
        return phoneTracker.isRunning();
    }
}
