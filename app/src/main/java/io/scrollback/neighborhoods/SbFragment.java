package io.scrollback.neighborhoods;

import android.view.KeyEvent;

import io.scrollback.library.Constants;
import io.scrollback.library.ScrollbackFragment;

public class SbFragment extends ScrollbackFragment {
    private static SbFragment instance;

    public static ScrollbackFragment getInstance() {
        if (instance == null) {
            instance = new SbFragment() {
                @Override
                public boolean onKeyDown(int keyCode, KeyEvent event) {
                    return super.onKeyDown(keyCode, event);
                }
            };

            instance.setLocation(Constants.PROTOCOL, Constants.HOST, Constants.PATH);
            instance.setEnableDebug(BuildConfig.DEBUG);
        }

        return instance;
    }
}
