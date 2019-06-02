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
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.Request;
import com.project.fotogram.R;
import com.project.fotogram.communication.RequestWithParams;
import com.project.fotogram.communication.VolleySingleton;
import com.project.fotogram.dialogs.MyDialog;
import com.project.fotogram.model.SessionInfo;
import com.project.fotogram.utility.Constants;
import com.project.fotogram.utility.UtilityMethods;

public class ProfilePhotoUpdateActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatephoto);
        ImageButton createPostImage = (ImageButton) findViewById(R.id.action_createPost);
        ImageButton ownProfile = (ImageButton) findViewById(R.id.action_ownProfile);
        ImageButton searchFriend = (ImageButton) findViewById(R.id.action_searchFriend);
        ImageButton goBack = (ImageButton) findViewById(R.id.action_goBack);
        createPostImage.setOnClickListener(getMenuOnClickListener());
        ownProfile.setOnClickListener(getMenuOnClickListener());
        searchFriend.setOnClickListener(getMenuOnClickListener());
        goBack.setOnClickListener(getMenuOnClickListener());

        Button searchPhotoButt = (Button) findViewById(R.id.update_profile_photo_button);
        searchPhotoButt.setOnClickListener(getSearchPhotoButtonListener());

        Button savePostButt = (Button) findViewById(R.id.action_saveNewPhoto);
        savePostButt.setOnClickListener(getSavePostButtonListener());
    }

    public View.OnClickListener getSearchPhotoButtonListener() {
        return new View.OnClickListener() {
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
        };
    }

    public View.OnClickListener getSavePostButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("fotogramLogs", "save post cliccato!");
                try {
                    ImageView loadedImage = (ImageView) findViewById(R.id.profile_photo_updated);
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) loadedImage.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    byte[] imageBytes = UtilityMethods.resizePhoto(bitmap);
                    String imageToBeLoaded = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    RequestWithParams createPostRequest = new RequestWithParams(Request.Method.POST, Constants.BASEURL + "picture_update", response -> {
                        Log.d("fotogramLogs", "finito caricamento!");
                        finish();
                    }, error -> UtilityMethods.manageCommunicationError(ProfilePhotoUpdateActivity.this, error));

                    Log.d("fotogramLogs", "imageToBeLoaded: " + imageToBeLoaded);
                    createPostRequest.addParam("session_id", SessionInfo.getInstance().getSessionId(ProfilePhotoUpdateActivity.this));
                    createPostRequest.addParam("picture", imageToBeLoaded);
                    VolleySingleton.getInstance(ProfilePhotoUpdateActivity.this.getApplicationContext()).addToRequestQueue(createPostRequest);
                } catch (Exception e) {
                    Log.d("fotogramLogs", e.getMessage());
                }

            }
        };
    }

    public View.OnClickListener getMenuOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.action_createPost:
                        Intent createPostIntent = new Intent(ProfilePhotoUpdateActivity.this, PostCreationActivity.class);
                        startActivity(createPostIntent);
                        break;
                    case R.id.action_ownProfile:
                        Intent ownProfile = new Intent(ProfilePhotoUpdateActivity.this, ProfileActivity.class);
                        ownProfile.putExtra("username", SessionInfo.getInstance().getCurrentUsername(ProfilePhotoUpdateActivity.this));
                        startActivity(ownProfile);
                        break;
                    case R.id.action_searchFriend:
                        Intent search = new Intent(ProfilePhotoUpdateActivity.this, SearchActivity.class);
                        startActivity(search);
                        break;
                    case R.id.action_goBack:
                        finish();
                        break;
                }
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1053:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, REQUEST_CODE);
                } else {
                    MyDialog dialog = new MyDialog();
                    dialog.setMsg("You don't have the permissions to access the storage");
                    dialog.show(this.getSupportFragmentManager(), "MyDialog");
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
