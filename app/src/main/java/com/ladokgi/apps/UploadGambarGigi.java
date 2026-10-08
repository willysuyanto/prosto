package com.ladokgi.apps;

import android.app.ProgressDialog;

import androidx.annotation.NonNull;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ladokgi.apps.data.SessionManager;
import com.ladokgi.apps.databinding.ActivityUploadGambarGigiBinding;
import com.ladokgi.apps.homepage.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UploadGambarGigi extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALERY_REQUEST_CODE = 101;
    private static final int CAMERA_PERMISSION_CODE = 102;

    private ActivityUploadGambarGigiBinding binding;
    private String currentPhotoPath = "";
    private Uri currentUri;
    private String docPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadGambarGigiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SessionManager.init(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            docPath = extras.getString("docPath", "");
        }

        CollectionReference konsultasi = db.collection("users")
                .document(SessionManager.getUsername())
                .collection("konsultasi");

        binding.camera.setOnClickListener(v -> {
            if (ensureCameraPermission()) {
                launchCamera();
            }
        });

        binding.galery.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALERY_REQUEST_CODE);
        });

        binding.submit.setOnClickListener(v -> upload(konsultasi, storageRef));

        binding.skip.setOnClickListener(v -> goHome());
    }

    private boolean ensureCameraPermission() {
        if (androidx.core.content.ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        androidx.core.app.ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        return false;    }

    private void launchCamera() {
        try {
            File photoFile = createImageFile();
            Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                    "com.ladokgi.apps.fileprovider", photoFile);
            currentUri = photoURI;
            Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Gagal membuat berkas foto", Toast.LENGTH_SHORT).show();
        }
    }

    private void upload(CollectionReference konsultasi, StorageReference storageRef) {
        if (currentUri == null) {
            Toast.makeText(getApplicationContext(), "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(UploadGambarGigi.this);
        progressDialog.setTitle("Sedang Upload Data");
        progressDialog.show();

        String uploadPath = "images/" + docPath + "/" + System.currentTimeMillis() + ".jpg";
        storageRef.child(uploadPath).putFile(currentUri)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), "Gagal upload gambar", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put("gambar_gigi", uploadPath);
                    konsultasi.document(docPath).update(map).addOnCompleteListener(write -> {
                        progressDialog.hide();
                        if (write.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Berhasil Upload Gambar", Toast.LENGTH_SHORT).show();
                            goHome();
                        }
                    });
                });
    }

    private void goHome() {
        startActivity(new Intent(UploadGambarGigi.this, MainActivity.class));
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == CAMERA_REQUEST_CODE) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
            int targetW = binding.images.getWidth();
            int targetH = binding.images.getHeight();
            int scaleFactor = 2;
            if (targetW > 0 && targetH > 0 && bmOptions.outWidth > 0 && bmOptions.outHeight > 0) {
                scaleFactor = Math.max(1, Math.min(bmOptions.outWidth / targetW, bmOptions.outHeight / targetH));
            }
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;
            binding.images.setImageBitmap(BitmapFactory.decodeFile(currentPhotoPath, bmOptions));
        } else if (requestCode == GALERY_REQUEST_CODE && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            currentUri = imageUri;
            try {
                binding.images.setImageBitmap(BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(imageUri)));
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Gagal membuka gambar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE
                && grantResults.length > 0
                && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        }
    }

    public String getFileName(Uri uri) {
        if (uri == null) {
            return "";
        }
        if ("content".equals(uri.getScheme())) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        if (index >= 0) {
                            String name = cursor.getString(index);
                            if (name != null) {
                                return name;
                            }
                        }
                    }
                } finally {
                    cursor.close();
                }
            }
        }
        String path = uri.getPath();
        if (path == null) {
            return "";
        }
        int cut = path.lastIndexOf('/');
        return cut >= 0 ? path.substring(cut + 1) : path;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        File image = File.createTempFile("JPEG_" + timeStamp + "_", ".jpg",
                getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
