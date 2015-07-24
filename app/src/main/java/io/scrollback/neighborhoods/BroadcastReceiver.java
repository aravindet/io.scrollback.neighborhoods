package io.scrollback.neighborhoods;

import io.scrollback.library.ScrollbackBroadcastReceiver;

public class BroadcastReceiver extends ScrollbackBroadcastReceiver {
    public BroadcastReceiver() {
        super(IntentService.class.getName());
    }
}
