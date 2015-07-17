package io.scrollback.neighborhoods;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import io.scrollback.library.AuthStatus;
import io.scrollback.library.FollowMessage;
import io.scrollback.library.NavMessage;
import io.scrollback.library.ReadyMessage;
import io.scrollback.library.ScrollbackFragment;
import io.scrollback.library.ScrollbackMessageHandler;

public class MainActivity extends AppCompatActivity {
    ScrollbackFragment scrollbackFragment = SbFragment.getInstance();
    AreaFragment areaFragment;

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
