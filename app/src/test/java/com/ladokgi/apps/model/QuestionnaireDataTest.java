package com.ladokgi.apps.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class QuestionnaireDataTest {

    @Test
    public void perilakuHas18GroupsAnd22Sliders() {
        List<QuestionnaireData.Group> groups = QuestionnaireData.perilaku();
        assertEquals(18, groups.size());
        int sliders = 0;
        for (QuestionnaireData.Group g : groups) {
            sliders += g.sliders.size();
        }
        assertEquals(22, sliders);
    }

    @Test
    public void nonPerilakuHas10GroupsAnd10Sliders() {
        List<QuestionnaireData.Group> groups = QuestionnaireData.nonPerilaku();
        assertEquals(10, groups.size());
        for (QuestionnaireData.Group g : groups) {
            assertEquals(1, g.sliders.size());
        }
    }

    @Test
    public void groupNumbersAreSequential() {
        int expected = 1;
        for (QuestionnaireData.Group g : QuestionnaireData.perilaku()) {
            assertEquals(expected++, g.titleNumber);
        }
        expected = 1;
        for (QuestionnaireData.Group g : QuestionnaireData.nonPerilaku()) {
            assertEquals(expected++, g.titleNumber);
        }
    }

    @Test
    public void defaultsMatchLegacySeekbarMidpoint() {
        for (QuestionnaireData.Group g : QuestionnaireData.perilaku()) {
            for (int value : g.values) {
                assertEquals(QuestionnaireData.DEFAULT_PROGRESS + 1, value);
            }
        }
    }

    @Test
    public void perilakuFirestoreKeysMatchLegacySchema() {
        assertEquals("pertanyaan1_1", QuestionnaireData.perilakuKey(1, 0));
        assertEquals("pertanyaan1_3", QuestionnaireData.perilakuKey(1, 2));
        assertEquals("pertanyaan2_1", QuestionnaireData.perilakuKey(2, 0));
        assertEquals("pertanyaan2_3", QuestionnaireData.perilakuKey(2, 2));
        assertEquals("pertanyaan3", QuestionnaireData.perilakuKey(3, 0));
        assertEquals("pertanyaan18", QuestionnaireData.perilakuKey(18, 0));
        assertEquals("pertanyaan1", QuestionnaireData.nonPerilakuKey(1));
        assertEquals("pertanyaan10", QuestionnaireData.nonPerilakuKey(10));
    }

    @Test
    public void sliderEndpointsMatchInstrumentWording() {
        List<QuestionnaireData.Group> perilaku = QuestionnaireData.perilaku();
        QuestionnaireData.Group first = perilaku.get(0);
        assertEquals("Merugikan", first.sliders.get(0).left);
        assertEquals("Menguntungkan", first.sliders.get(0).right);
        assertEquals("Merepotkan", first.sliders.get(1).left);
        assertEquals("Memudahkan", first.sliders.get(1).right);

        QuestionnaireData.Group second = perilaku.get(1);
        assertEquals("Buruk", second.sliders.get(0).left);
        assertEquals("Baik", second.sliders.get(0).right);

        for (int i = 2; i < perilaku.size(); i++) {
            assertEquals("Tidak Setuju", perilaku.get(i).sliders.get(0).left);
            assertEquals("Setuju", perilaku.get(i).sliders.get(0).right);
        }

        List<QuestionnaireData.Group> nonPerilaku = QuestionnaireData.nonPerilaku();
        for (int i = 0; i < 7; i++) {
            assertEquals("Sering", nonPerilaku.get(i).sliders.get(0).left);
            assertEquals("Tidak Pernah", nonPerilaku.get(i).sliders.get(0).right);
        }
        for (int i = 7; i < 10; i++) {
            assertEquals("Banyak", nonPerilaku.get(i).sliders.get(0).left);
            assertEquals("Tidak Ada", nonPerilaku.get(i).sliders.get(0).right);
        }
        assertTrue(QuestionnaireData.SCALE_STEPS == 7);
    }
}
