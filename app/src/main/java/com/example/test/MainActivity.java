package com.example.test;

import android.annotation.TargetApi;
import android.bluetooth.le.ScanResult;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import com.fooock.lib.phone.tracker.Configuration;
import com.fooock.lib.phone.tracker.PhoneTracker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.telephony.CellInfo;
import android.telephony.NeighboringCellInfo;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private  PhoneTracker phoneTracker = new PhoneTracker(this);
    private TextView TestText;
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

/*        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        });*/
        TestText = findViewById(R.id.testText);
        phoneTracker.addPermissionListener(new PhoneTracker.PermissionListener() {
            @Override
            public void onPermissionNotGranted(String... permission) {
                Log.d(TAG, "Permission not granted: " + Arrays.deepToString(permission));
            }
        });
/*// Listen for configuration changes
        phoneTracker.setConfigurationChangeListener(new PhoneTracker.ConfigurationChangeListener() {
            @Override
            public void onConfigurationChange(Configuration configuration) {
                Log.d(TAG, "Tracker configuration changed!");
            }
        });*/
        phoneTracker.start();
        // Check the state of the tracker
        boolean running = phoneTracker.isRunning();
        Log.d(TAG, "Running: " + running);
        TestText.setText("Running:"+ running);

        // Create a new wifi configuration
        Configuration.Wifi wifiConf = new Configuration.Wifi();
        wifiConf.setScanDelay(3000);

        // Create a new cell configuration
        Configuration.Cell cellConf = new Configuration.Cell();
        cellConf.setScanDelay(5000);

        // Create a gps configuration
        Configuration.Gps gpsConf = new Configuration.Gps();
        gpsConf.setMinDistanceUpdate(10);
        gpsConf.setMinTimeUpdate(7000);

        // Create a new configuration
        Configuration configuration = new Configuration.Builder()
                .useCell(true).cell(cellConf)
                .useWifi(true).wifi(wifiConf)
                .useGps(true).gps(gpsConf)
                .create();

        // Set the new init configuration
        phoneTracker.setConfiguration(configuration);

        // Set the listener for cell scans
        phoneTracker.setCellScanListener(new PhoneTracker.CellScanListener() {
            @Override
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            public void onCellInfoReceived(long timestamp, List<CellInfo> cells) {
                Log.d(TAG, "timestamp = [" + timestamp + "], cells = [" + cells.size() + "]");
            }

            @Override
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            public void onNeighborCellReceived(long timestamp, List<NeighboringCellInfo> cells) {
                Log.d(TAG, "timestamp = [" + timestamp + "], cells = [" + cells.size() + "]");
            }
        });

        // Set the listener to receive location updates from gps
        phoneTracker.setGpsLocationListener(new PhoneTracker.GpsLocationListener() {
            @Override
            public void onLocationReceived(long timestamp, Location location) {
                Log.d(TAG, "timestamp = [" + timestamp + "], location = [" + location + "]");
                TestText.append("location:"+ location);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {

        phoneTracker.start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        phoneTracker.stop();
    }
}

