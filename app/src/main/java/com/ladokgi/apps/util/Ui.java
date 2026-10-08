package com.ladokgi.apps.util;

import android.content.Context;
import android.widget.Toast;

/** UI helpers shared across screens. */
public final class Ui {

    private Ui() {
    }

    public static void toast(Context context, CharSequence text) {
        Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, int resId) {
        toast(context, context.getString(resId));
    }
}
