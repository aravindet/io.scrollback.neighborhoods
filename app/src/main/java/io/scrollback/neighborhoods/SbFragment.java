package io.scrollback.neighborhoods;

import android.view.KeyEvent;

import io.scrollback.library.ScrollbackFragment;

public class SbFragment extends ScrollbackFragment {
    private static SbFragment instance = new SbFragment() {
        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            return super.onKeyDown(keyCode, event);
        }
    };

    public static ScrollbackFragment getInstance() {
        return instance;
    }
}
