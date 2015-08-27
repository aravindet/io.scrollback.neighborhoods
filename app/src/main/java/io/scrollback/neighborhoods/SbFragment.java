package io.scrollback.neighborhoods;

import io.scrollback.library.ScrollbackFragment;

public class SbFragment extends ScrollbackFragment {
    public static ScrollbackFragment newInstance() {
        ScrollbackFragment instance = new SbFragment();

        instance.setLocation(Constants.PROTOCOL, Constants.HOST, Constants.PATH);
        instance.setEnableDebug(true);

        return instance;
    }
}
