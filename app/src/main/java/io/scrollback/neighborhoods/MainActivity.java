package io.scrollback.neighborhoods;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import io.scrollback.library.ScrollbackFragment;

public class MainActivity extends AppCompatActivity {
    ScrollbackFragment scrollbackFragment = SbFragment.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.area_container, MainFragment.newInstance())
                    .commit();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.scrollback_container, scrollbackFragment)
                    .commit();
        }

        scrollbackFragment.setEnableDebug(BuildConfig.DEBUG);
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
