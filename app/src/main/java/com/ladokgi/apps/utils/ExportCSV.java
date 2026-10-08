package com.ladokgi.apps.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Saves a CSV through the Storage Access Framework, so no storage permission
 * is required and the user picks the destination folder.
 *
 * The launcher must be registered on a lifecycle owner that is still below
 * STARTED, so callers pass the owner explicitly (typically from onCreate()).
 */
public class ExportCSV {

    private static final String LINE_SEPARATOR = "\r\n";

    private final Context context;
    private final ActivityResultLauncher<String> createDocument;
    private List<String[]> pendingData;

    public ExportCSV(ActivityResultCaller caller, Context context) {
        this.context = context;
        this.createDocument = caller.registerForActivityResult(
                new ActivityResultContracts.CreateDocument("text/csv"), this::onDocumentCreated);
    }

    public void exportCSV(List<String[]> data, String fileName) {
        pendingData = data;
        createDocument.launch(fileName);
    }

    private void onDocumentCreated(Uri uri) {
        if (uri == null || pendingData == null) {
            return;
        }
        try {
            ContentResolver resolver = context.getContentResolver();
            try (OutputStream out = resolver.openOutputStream(uri)) {
                if (out == null) {
                    throw new IllegalStateException("no output stream");
                }
                try (Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
                    for (String[] row : pendingData) {
                        writer.write(toCsvRow(row));
                        writer.write(LINE_SEPARATOR);
                    }
                }
            }
            toast("CSV file exported successfully");
        } catch (Exception e) {
            toast("Error exporting CSV file");
        } finally {
            pendingData = null;
        }
    }

    /** RFC 4180 row: fields are quoted when they contain a separator, quote or newline. */
    static String toCsvRow(String[] fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(escapeCsvField(fields[i]));
        }
        return sb.toString();
    }

    static String escapeCsvField(String value) {
        String field = value == null ? "" : value;
        boolean needsQuotes = field.contains(",") || field.contains("\"")
                || field.contains("\n") || field.contains("\r")
                || !field.equals(field.trim());
        if (!needsQuotes) {
            return field;
        }
        return "\"" + field.replace("\"", "\"\"") + "\"";
    }

    private void toast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
