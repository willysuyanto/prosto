package com.ladokgi.apps.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportCSV {
    private Activity activity;

    public ExportCSV(Activity activity) {
        this.activity = activity;
    }

    // Method to handle permission result
    public void exportCSV(List<String[]> data, String fileName) {
        // Request storage permission if not granted
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }

        // Check if external storage is writable
        if (!isExternalStorageWritable()) {
            Toast.makeText(activity, "External storage is not writable", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the Downloads directory
        File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(downloadsDirectory, fileName);
        Log.d("TAG", "File path: " + file.getAbsolutePath());

        try (FileWriter fileWriter = new FileWriter(file);
             CSVWriter csvWriter = new CSVWriter(fileWriter)) {

            csvWriter.writeAll(data);
            Toast.makeText(activity, "CSV file exported successfully", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "CSV file exported successfully at " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(activity, "Error exporting CSV file", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "Error exporting CSV file", e);
        }
    }

    // Method to check if external storage is writable
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    // Method to handle permission result
    public void onRequestPermissionsResult(int requestCode, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you may want to inform the user or take action accordingly
                Toast.makeText(activity, "Storage permission granted. Please try exporting again.", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied
                Toast.makeText(activity, "Storage permission is required to export CSV", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
