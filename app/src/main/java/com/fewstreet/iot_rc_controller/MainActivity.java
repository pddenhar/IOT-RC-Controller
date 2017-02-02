package com.fewstreet.iot_rc_controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.MobileAnarchy.Android.Widgets.Joystick.DualJoystickView;
import com.MobileAnarchy.Android.Widgets.Joystick.JoystickMovedListener;

import net.vector57.mrpc.MRPC;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private DualJoystickView joystick;
    private String TAG = "MainActivity";
    private MRPC _mrpc;
    private float throttle_range;
    private String mrpc_path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joystick = (DualJoystickView)findViewById(R.id.dualjoystickView);

        joystick.setOnJostickMovedListener(_listenerLeft, _listenerRight);

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            _mrpc = new MRPC(this, Util.getBroadcastAddress(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
        throttle_range = SettingsActivity.getThrottleRange(this);
        mrpc_path = SettingsActivity.getMRPCDevicePath(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        _mrpc.close();
        _mrpc = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    private void sendThrottle(float val) {
        if(_mrpc != null){
            _mrpc.RPC(mrpc_path + ".throttle", val, null, false);
        }
    }
    private void sendSteering(float val) {
        if(_mrpc != null){
            _mrpc.RPC(mrpc_path + ".steering", val, null, false);
        }
    }

    private JoystickMovedListener _listenerLeft = new JoystickMovedListener() {

        @Override
        public void OnMoved(int pan, int tilt) {
            float mapped = ((float)tilt*-1*throttle_range + 100)/200;
            sendThrottle(mapped);
        }

        @Override
        public void OnReleased() {
            Log.d(TAG, "Released");
        }

        public void OnReturnedToCenter() {
            Log.d(TAG, "stopped");
            sendThrottle(0.5f);
        };
    };

    private JoystickMovedListener _listenerRight = new JoystickMovedListener() {

        @Override
        public void OnMoved(int pan, int tilt) {
            float mapped = ((float)pan + 100)/200;
            sendSteering(mapped);
        }

        @Override
        public void OnReleased() {
            Log.d(TAG, "Released");
        }

        public void OnReturnedToCenter() {
            Log.d(TAG, "stopped");
            sendSteering(0.5f);
        };
    };
}
