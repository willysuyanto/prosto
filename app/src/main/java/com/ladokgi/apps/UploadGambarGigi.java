package com.ladokgi.apps;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ladokgi.apps.homepage.MainActivity;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UploadGambarGigi extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_CODE = 102;
    private static final int STORAGE_PERMISSION_CODE = 103;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALERY_REQUEST_CODE = 101;

    String currentPhotoPath="";
    Uri currentUri;
    String docPath="";
    public Uri photoURI;

    ImageView imageView;
    Button btnCamera, btnGalery, btnSubmit;
    TextView btnSkip;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_gambar_gigi);

        checkPermissionCamera();
        checkPermissionStorage();

        imageView = findViewById(R.id.images);
        btnCamera = findViewById(R.id.camera);
        btnGalery = findViewById(R.id.galery);
        btnSubmit = findViewById(R.id.submit);
        btnSkip = findViewById(R.id.skip);

        Bundle extras = getIntent().getExtras();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage store = FirebaseStorage.getInstance();
        StorageReference storageRef = store.getReference();

        Hawk.init(getApplicationContext()).build();
        String currentUser = Hawk.get("username");
        CollectionReference konsultasi = db.collection("users").document(currentUser).collection("konsultasi");
        if (extras != null) {
            docPath = extras.getString("docPath");
            Log.d( "Non-Perilaku", docPath);
        }

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    Log.d("ContentValues", "PhotoFile: " + photoFile);
                } catch (IOException e) {
                    Log.d("ContentValues", "Gagal: " + photoFile);
                }
                if (photoFile != null) {
                    photoURI = FileProvider.getUriForFile(getApplicationContext(), "com.ladokgi.apps.fileprovider", photoFile);
                    Log.d("ContentValues", "onClick: " + photoURI);
                    currentUri = photoURI;
                    takePictureIntent.putExtra("output", photoURI);
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                }
            }
        });

        btnGalery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALERY_REQUEST_CODE);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code for showing progressDialog while uploading
                ProgressDialog progressDialog
                        = new ProgressDialog(UploadGambarGigi.this);
                progressDialog.setTitle("Sedang Upload Data");
                progressDialog.show();
                if(currentUri != null){
                    String uploadPath = "images/"+docPath+"/"+currentUri.getLastPathSegment()+".jpg";
                    StorageReference upload = storageRef.child(uploadPath);
                    UploadTask uploadTask = upload.putFile(currentUri);
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                Map<String, Object> map = new HashMap<>();
                                map.put("gambar_gigi", uploadPath);
                                konsultasi.document(docPath).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            progressDialog.hide();
                                            Toast.makeText(getApplicationContext(), "Berhasil Upload Gambar", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(UploadGambarGigi.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                            }else{
                            progressDialog.hide();
                            }
                        }
                    });
                }
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadGambarGigi.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int scaleFactor;
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ContentValues", "Request Code : " + requestCode);
        new Date();
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                int targetW = this.imageView.getWidth();
                int targetH = this.imageView.getHeight();
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;
                if (targetW > 0 || targetH > 0) {
                    scaleFactor = Math.min(photoW / targetW, photoH / targetH);
                } else {
                    scaleFactor = 2;
                }
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;
                Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
                Log.d( "onActivityResult: ", currentPhotoPath);
                imageView.setImageBitmap(imageBitmap);
            }
        } else if (requestCode != GALERY_REQUEST_CODE) {
        } else {
            if (resultCode == RESULT_OK) {
                try {
                    Uri imageUri = data.getData();
                    currentUri = imageUri;
                    String substring = getFileName(imageUri).substring(0, getFileName(imageUri).length() - 4);
                    Bitmap selectedImage = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    imageView.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissionCamera() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.CAMERA") == 0) {
            Toast.makeText(getApplicationContext(), "Camera Permission Grandted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermissions(new String[]{"android.permission.CAMERA"}, CAMERA_PERMISSION_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissionStorage() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            Toast.makeText(getApplicationContext(), "Storage Permission Grandted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, STORAGE_PERMISSION_CODE);
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        result = cursor.getString(cursor.getColumnIndex("_display_name"));
                    }
                } catch (Throwable th) {
                    cursor.close();
                    throw th;
                }
            }
            cursor.close();
        }
        if (result != null) {
            return result;
        }
        String result2 = uri.getPath();
        int cut = result2.lastIndexOf(47);
        if (cut != -1) {
            return result2.substring(cut + 1);
        }
        return result2;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File image = File.createTempFile("JPEG_" + timeStamp + "_", ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        StringBuilder sb = new StringBuilder();
        sb.append("createImageFile: ");
        sb.append(image);
        Log.d("ContentValues", sb.toString());
        currentPhotoPath = image.getAbsolutePath();
        Log.d("ContentValues", "current: " + currentPhotoPath);
        return image;
    }
}