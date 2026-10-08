package com.ladokgi.apps.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExportCsvTest {

    @Test
    public void plainFieldIsNotQuoted() {
        assertEquals("Budi", ExportCSV.escapeCsvField("Budi"));
        assertEquals("1234", ExportCSV.escapeCsvField("1234"));
    }

    @Test
    public void separatorAndNewlinesAndQuotesAreQuoted() {
        assertEquals("\"Budi, Santoso\"", ExportCSV.escapeCsvField("Budi, Santoso"));
        assertEquals("\"line1\nline2\"", ExportCSV.escapeCsvField("line1\nline2"));
        assertEquals("\"say \"\"hi\"\"\"", ExportCSV.escapeCsvField("say \"hi\""));
        assertEquals("\"  spaced  \"", ExportCSV.escapeCsvField("  spaced  "));
    }

    @Test
    public void nullBecomesEmptyField() {
        assertEquals("", ExportCSV.escapeCsvField(null));
    }

    @Test
    public void rowJoinsFieldsWithCommas() {
        assertEquals("a,b,c", ExportCSV.toCsvRow(new String[]{"a", "b", "c"}));
        assertEquals("a,\"b,c\"", ExportCSV.toCsvRow(new String[]{"a", "b,c"}));
        assertEquals("", ExportCSV.toCsvRow(new String[]{}));
    }
}
