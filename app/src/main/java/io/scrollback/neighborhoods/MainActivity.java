package io.scrollback.neighborhoods;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import io.scrollback.library.AuthStatus;
import io.scrollback.library.FollowMessage;
import io.scrollback.library.NavMessage;
import io.scrollback.library.ReadyMessage;
import io.scrollback.library.ScrollbackFragment;
import io.scrollback.library.ScrollbackMessageHandler;

public class MainActivity extends AppCompatActivity implements LocationListener {
    ScrollbackFragment scrollbackFragment = SbFragment.getInstance();
    AreaFragment areaFragment;

    FrameLayout areaFrame;
    FrameLayout sbFrame;
    private LocationManager locationManager;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        areaFrame = (FrameLayout) findViewById(R.id.area_container);
        sbFrame = (FrameLayout) findViewById(R.id.scrollback_container);

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        if (savedInstanceState == null) {
            showAreaFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.scrollback_container, scrollbackFragment)
                    .commit();
        }

        scrollbackFragment.setMessageHandler(new ScrollbackMessageHandler() {
            @Override
            public void onNavMessage(final NavMessage message) {
                if (message != null && message.mode != null) {
                    scrollbackFragment.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (message.mode.equals("home")) {
                                showAreaFragment();
                            } else {
                                hideAreaFragment();
                            }
                        }
                    });
                }
            }

            @Override
            public void onAuthMessage(AuthStatus message) { }

            @Override
            public void onFollowMessage(FollowMessage message) { }

            @Override
            public void onReadyMessage(ReadyMessage message) { }
        });

        scrollbackFragment.setCanChangeStatusBarColor(false);


    }

    public void showAreaFragment() {
        getSupportActionBar().show();

        areaFragment = AreaFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.area_container, areaFragment)
                .commit();

        areaFrame.setVisibility(View.VISIBLE);
        sbFrame.setVisibility(View.INVISIBLE);
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    public void hideAreaFragment() {
        // If you want to customize view animations (like material reveal) look here:
        // https://developer.android.com/training/material/animations.html#Reveal
        getSupportActionBar().hide();

        if (areaFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(areaFragment)
                    .commit();

            areaFragment = null;
        }

        areaFrame.setVisibility(View.INVISIBLE);
        sbFrame.setVisibility(View.VISIBLE);
        locationManager.removeUpdates(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return scrollbackFragment.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        if(areaFrame.getVisibility()==View.VISIBLE)
            locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(areaFragment!= null) {
            areaFragment.sortAreas(location.getLatitude(), location.getLongitude());
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }
}
