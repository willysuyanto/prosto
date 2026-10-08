package com.ladokgi.apps.util;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.ladokgi.apps.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Builds the three profile dropdowns (gender, education, denture year) in one place.
 * They are Material exposed-dropdown menus, so the label floats once a value is chosen
 * and an empty field simply shows its hint.
 */
public final class ProfileSpinners {

    public static final String GENDER_MALE = "Laki-Laki";
    public static final String GENDER_FEMALE = "Perempuan";

    private ProfileSpinners() {
    }

    public static void bind(MaterialAutoCompleteTextView gender, MaterialAutoCompleteTextView education,
                            MaterialAutoCompleteTextView year) {
        Context context = gender.getContext();

        List<String> genders = new ArrayList<>();
        genders.add(GENDER_MALE);
        genders.add(GENDER_FEMALE);

        List<String> educations = new ArrayList<>();
        educations.addAll(Arrays.asList(context.getResources().getStringArray(R.array.data_pendidikan)));

        List<String> years = new ArrayList<>();
        int currentYear = Calendar.getInstance(TimeZone.getDefault()).get(Calendar.YEAR);
        for (int y = currentYear; y >= 1990; y--) {
            years.add(String.valueOf(y));
        }

        apply(gender, genders);
        apply(education, educations);
        apply(year, years);
    }

    /** True while the dropdown shows no value yet (only its hint). */
    public static boolean isPlaceholder(MaterialAutoCompleteTextView field) {
        return TextUtils.isEmpty(field.getText());
    }

    /** Selects the given value when present, otherwise leaves the field empty. */
    public static void select(MaterialAutoCompleteTextView field, String value) {
        field.setText(value == null ? "" : value);
    }

    private static void apply(MaterialAutoCompleteTextView field, List<String> items) {
        field.setSimpleItems(items.toArray(new String[0]));
    }
}
