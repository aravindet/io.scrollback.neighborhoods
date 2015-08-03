package io.scrollback.neighborhoods;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class Tutorial {
    private Context mContext;

    private static SharedPreferences storage;
    private static final String STORE_NAME = "TUTORIAL_STORE";

    Tutorial(Context c) {
        mContext = c;

        if (storage == null){
            storage = c.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE);
        }
    }

    private void show(final int view, final String hint_key, final Runnable runnable) {
        if (storage.getBoolean(hint_key, false)) {
            if (runnable != null) {
                runnable.run();
            }

            return;
        }

        final Dialog dialog = new Dialog(mContext, R.style.TutorialDialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics metrics = new DisplayMetrics();

        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        dialog.getWindow().setLayout(metrics.widthPixels, metrics.heightPixels);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialog_ok);

        // If button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor e = storage.edit();

                e.putBoolean(hint_key, true);
                e.apply();

                dialog.dismiss();

                if (runnable != null) {
                    runnable.run();
                }
            }
        });

        dialog.show();
    }

    public void showAllTips(final Runnable runnable) {
        show(R.layout.search_tip, "search_hint_shown", new Runnable() {
            @Override
            public void run() {
                show(R.layout.item_tip, "item_hint_shown", runnable);
            }
        });
    }
}
