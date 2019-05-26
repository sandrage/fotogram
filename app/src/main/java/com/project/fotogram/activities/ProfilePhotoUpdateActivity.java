package com.project.fotogram.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.project.fotogram.R;
import com.project.fotogram.communication.RequestWithParams;
import com.project.fotogram.communication.VolleySingleton;
import com.project.fotogram.model.SessionInfo;
import com.project.fotogram.utility.Constants;
import com.project.fotogram.utility.UtilityMethods;

import java.io.ByteArrayOutputStream;

public class ProfilePhotoUpdateActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatephoto);
        Button searchPhotoButt = (Button) findViewById(R.id.update_profile_photo_button);
        searchPhotoButt.setOnClickListener(new View.OnClickListener() {
            //todo perform this
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ProfilePhotoUpdateActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent gallery = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(gallery, REQUEST_CODE);
                } else {
                    UtilityMethods.checkPermissions(ProfilePhotoUpdateActivity.this, 1053);
                }
            }
        });

        Button savePostButt = (Button) findViewById(R.id.action_saveNewPhoto);
        savePostButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("fotogramLogs", "save post cliccato!");
                try {
                    //todo scale better this
                    ImageView loadedImage = (ImageView) findViewById(R.id.profile_photo_updated);
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) loadedImage.getDrawable();
                    ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() % 100, bitmap.getHeight() % 100, false);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayStream);
                    byte[] imageBytes = byteArrayStream.toByteArray();

                    String imageToBeLoaded = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                    RequestWithParams createPostRequest = new RequestWithParams(Request.Method.POST, Constants.BASEURL + "picture_update", response -> {
                        Log.d("fotogramLogs", "finito caricamento!");
                        finish();
                    }, error -> UtilityMethods.manageCommunicationError(error));

                    Log.d("fotogramLogs", "imageToBeLoaded: " + imageToBeLoaded);
                    createPostRequest.addParam("session_id", SessionInfo.getInstance().getSessionId(ProfilePhotoUpdateActivity.this));
                    createPostRequest.addParam("picture", imageToBeLoaded);
                    VolleySingleton.getInstance(ProfilePhotoUpdateActivity.this.getApplicationContext()).addToRequestQueue(createPostRequest);
                } catch (Exception e) {
                    Log.d("fotogramLogs", e.getMessage());
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1053:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, REQUEST_CODE);
                } else {
                    //TODO dovrei mostrare un messaggio con l'errore che non può accedere alla gallery
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.profile_photo_updated);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }
}
