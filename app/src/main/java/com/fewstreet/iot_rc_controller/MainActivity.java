package com.fewstreet.iot_rc_controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.MobileAnarchy.Android.Widgets.Joystick.DualJoystickView;
import com.MobileAnarchy.Android.Widgets.Joystick.JoystickMovedListener;

import net.vector57.mrpc.MRPC;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private DualJoystickView joystick;
    private String TAG = "MainActivity";
    private MRPC _mrpc;
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        _mrpc.close();
        _mrpc = null;
    }

    private JoystickMovedListener _listenerLeft = new JoystickMovedListener() {

        @Override
        public void OnMoved(int pan, int tilt) {
            float mapped = ((float)tilt*-1 + 100)/200;
            if(_mrpc != null){
                _mrpc.RPC("*.throttle", mapped, null, false);
            }
        }

        @Override
        public void OnReleased() {
            Log.d(TAG, "Released");
        }

        public void OnReturnedToCenter() {
            Log.d(TAG, "stopped");
            if(_mrpc != null){
                _mrpc.RPC("*.throttle", 0.5, null, false);
            }
        };
    };

    private JoystickMovedListener _listenerRight = new JoystickMovedListener() {

        @Override
        public void OnMoved(int pan, int tilt) {
            float mapped = ((float)pan + 100)/200;
            Log.d(TAG, " " + mapped);
            if(_mrpc != null){
                _mrpc.RPC("*.steering", mapped, null, false);
            }
        }

        @Override
        public void OnReleased() {
            Log.d(TAG, "Released");
        }

        public void OnReturnedToCenter() {
            Log.d(TAG, "stopped");
            if(_mrpc != null){
                _mrpc.RPC("*.steering", 0.5, null, false);
            }
        };
    };
}
