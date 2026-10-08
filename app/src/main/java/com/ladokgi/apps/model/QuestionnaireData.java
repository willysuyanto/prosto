package com.ladokgi.apps.model;

import androidx.annotation.StringRes;

import com.ladokgi.apps.R;

import java.util.ArrayList;
import java.util.List;

/** Static definition of both questionnaire aspects (instrument wording + scales). */
public final class QuestionnaireData {

    public static final int SCALE_STEPS = 7;
    public static final int DEFAULT_PROGRESS = 3; // value 4, same as the legacy seekbars

    public static final class SliderDef {
        public final String left;
        public final String right;

        public SliderDef(String left, String right) {
            this.left = left;
            this.right = right;
        }
    }

    public static final class Group {
        public final int titleNumber;
        @StringRes
        public final int subQuestionRes;
        public final List<SliderDef> sliders;
        public final int[] values; // 1..7 per slider, kept in the model so recycling cannot lose input

        public Group(int titleNumber, @StringRes int subQuestionRes, List<SliderDef> sliders) {
            this.titleNumber = titleNumber;
            this.subQuestionRes = subQuestionRes;
            this.sliders = sliders;
            this.values = new int[sliders.size()];
            for (int i = 0; i < values.length; i++) {
                values[i] = DEFAULT_PROGRESS + 1;
            }
        }
    }

    private QuestionnaireData() {
    }

    public static List<Group> perilaku() {
        List<Group> groups = new ArrayList<>();
        groups.add(new Group(1, R.string.perilaku_pertanyaan1, list(
                new SliderDef("Merugikan", "Menguntungkan"),
                new SliderDef("Merepotkan", "Memudahkan"),
                new SliderDef("Membosankan", "Menyenangkan"))));
        groups.add(new Group(2, R.string.perilaku_pertanyaan2, list(
                new SliderDef("Buruk", "Baik"),
                new SliderDef("Membuang Waktu", "Bermanfaat"),
                new SliderDef("Membosankan", "Menyenangkan"))));
        int[] res = {
                R.string.perilaku_pertanyaan3, R.string.perilaku_pertanyaan4,
                R.string.perilaku_pertanyaan5, R.string.perilaku_pertanyaan6,
                R.string.perilaku_pertanyaan7, R.string.perilaku_pertanyaan8,
                R.string.perilaku_pertanyaan9, R.string.perilaku_pertanyaan10,
                R.string.perilaku_pertanyaan11, R.string.perilaku_pertanyaan12,
                R.string.perilaku_pertanyaan13, R.string.perilaku_pertanyaan14,
                R.string.perilaku_pertanyaan15, R.string.perilaku_pertanyaan16,
                R.string.perilaku_pertanyaan17, R.string.perilaku_pertanyaan18
        };
        for (int i = 0; i < res.length; i++) {
            groups.add(new Group(i + 3, res[i], list(new SliderDef("Tidak Setuju", "Setuju"))));
        }
        return groups;
    }

    public static List<Group> nonPerilaku() {
        List<Group> groups = new ArrayList<>();
        int[] res = {
                R.string.non_perilaku_pertanyaan1, R.string.non_perilaku_pertanyaan2,
                R.string.non_perilaku_pertanyaan3, R.string.non_perilaku_pertanyaan4,
                R.string.non_perilaku_pertanyaan5, R.string.non_perilaku_pertanyaan6,
                R.string.non_perilaku_pertanyaan7, R.string.non_perilaku_pertanyaan8,
                R.string.non_perilaku_pertanyaan9, R.string.non_perilaku_pertanyaan10
        };
        for (int i = 0; i < res.length; i++) {
            String left = i < 7 ? "Sering" : "Banyak";
            String right = i < 7 ? "Tidak Pernah" : "Tidak Ada";
            groups.add(new Group(i + 1, res[i], list(new SliderDef(left, right))));
        }
        return groups;
    }

    /** Firestore key for the given slider: perilaku uses pertanyaan1_1..pertanyaan2_3 then pertanyaanN. */
    public static String perilakuKey(int groupNumber, int sliderIndex) {
        if (groupNumber <= 2) {
            return "pertanyaan" + groupNumber + "_" + (sliderIndex + 1);
        }
        return "pertanyaan" + groupNumber;
    }

    public static String nonPerilakuKey(int groupNumber) {
        return "pertanyaan" + groupNumber;
    }

    private static List<SliderDef> list(SliderDef... defs) {
        List<SliderDef> out = new ArrayList<>();
        for (SliderDef d : defs) {
            out.add(d);
        }
        return out;
    }
}
