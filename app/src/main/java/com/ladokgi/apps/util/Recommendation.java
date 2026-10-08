package com.ladokgi.apps.util;

import android.content.Context;

import com.ladokgi.apps.R;

/** The single source of truth for the consultation recommendation wording. */
public final class Recommendation {

    private Recommendation() {
    }

    public static int labelRes(boolean hasilPerilaku, boolean hasilNonPerilaku) {
        if (hasilPerilaku && hasilNonPerilaku) {
            return R.string.result_no_treatment;
        } else if (hasilPerilaku || hasilNonPerilaku) {
            return R.string.result_consider_treatment;
        }
        return R.string.result_treatment_needed;
    }

    public static String label(Context context, boolean hasilPerilaku, boolean hasilNonPerilaku) {
        return context.getString(labelRes(hasilPerilaku, hasilNonPerilaku));
    }
}
