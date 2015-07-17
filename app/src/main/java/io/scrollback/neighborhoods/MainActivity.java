package io.scrollback.neighborhoods;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import io.scrollback.library.ScrollbackFragment;

public class MainActivity extends AppCompatActivity {
    ScrollbackFragment scrollbackFragment = SbFragment.getInstance();
    FrameLayout areaFrame;
    FrameLayout sbFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        areaFrame = (FrameLayout) findViewById(R.id.area_container);
        sbFrame = (FrameLayout) findViewById(R.id.scrollback_container);

        if (savedInstanceState == null) {
            showAreaFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.scrollback_container, scrollbackFragment)
                    .commit();
        }
    }

    public void showAreaFragment() {
        getSupportActionBar().show();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.area_container, MainFragment.newInstance())
                .commit();

        areaFrame.setVisibility(View.VISIBLE);
        sbFrame.setVisibility(View.INVISIBLE);
    }

    public void showSbFragment() {
        // If you want to customize view animations (like material reveal) look here:
        // https://developer.android.com/training/material/animations.html#Reveal
        getSupportActionBar().hide();

        areaFrame.setVisibility(View.INVISIBLE);
        sbFrame.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return scrollbackFragment.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
