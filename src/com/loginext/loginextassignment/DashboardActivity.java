package com.loginext.loginextassignment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class DashboardActivity extends Activity implements OnClickListener{
  private static final int SELECT_PICTURE = 1;
  private static final int SAVE_SIGNATURE = 2;
  private static final int IMAGE_PICKER_SELECT = 111;
  private static final int REQUEST_IMAGE_CAPTURE = 222;
  private static final int IMAGE_CODE = 111111;
  private GoogleMap googleMap;
  private Button eSignature;
  private Button photo;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dashboard);

    findViewByIds();
    setValues();

  }

  private void setValues() {
    initMapValues();
    initListeners();
  }

  private void initMapValues() {
    // Loading map
    initilizeMap();

    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    // Showing / hiding your current location
    googleMap.setMyLocationEnabled(true);

    // Enable / Disable zooming controls
    googleMap.getUiSettings().setZoomControlsEnabled(false);

    // Enable / Disable my location button
    googleMap.getUiSettings().setMyLocationButtonEnabled(true);

    // Enable / Disable Compass icon
    googleMap.getUiSettings().setCompassEnabled(true);

    // Enable / Disable Rotate gesture
    googleMap.getUiSettings().setRotateGesturesEnabled(true);

    // Enable / Disable zooming functionality
    googleMap.getUiSettings().setZoomGesturesEnabled(true);
  }

  private void initListeners() {
    eSignature.setOnClickListener(this);
    photo.setOnClickListener(this);
  }

  private void findViewByIds() {
    eSignature = (Button) findViewById(R.id.e_signature);
    photo = (Button) findViewById(R.id.photo);
  }

  private void initilizeMap() {
    if (googleMap == null) {
      googleMap = ((MapFragment) getFragmentManager().findFragmentById(
          R.id.map)).getMap();

      if (googleMap == null) {
        Log.d(DashboardActivity.class.getSimpleName(), "Unable to create map!");
      }
    }
  }

  @Override public void onClick(View view) {
    int id = view.getId();
    switch (id) {
    case R.id.e_signature:
      Intent intent = new Intent(DashboardActivity.this, SignOnActivity.class);
      startActivityForResult(intent, SAVE_SIGNATURE);
      break;
    case R.id.photo:
      getPictures();
      break;
    default:
      Log.d(DashboardActivity.class.getSimpleName(), "No Click Listener for this type");
      break;
    }
  }

  private void getPictures() {
    Intent pickIntent = new Intent();
    pickIntent.setType("image/*");
    pickIntent.setAction(Intent.ACTION_GET_CONTENT);

    Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    String pickTitle = "Select or take a new Picture";
    Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
    chooserIntent.putExtra
    (
        Intent.EXTRA_INITIAL_INTENTS, 
        new Intent[] { takePhotoIntent }
        );

    startActivityForResult(chooserIntent, SELECT_PICTURE);    
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Uri imageUri = null;

    if(resultCode != RESULT_OK) {
      Log.d(DashboardActivity.class.getSimpleName(), "Apologies Error in retrieving Image");
      return;
    }

    switch (requestCode) {
    case REQUEST_IMAGE_CAPTURE:
      imageUri = findPicutreUri();
      startUploadingService(imageUri, IMAGE_CODE);
      break;
    case IMAGE_PICKER_SELECT:
      imageUri = data.getData();
      startUploadingService(imageUri, IMAGE_CODE);
      break;
    default:
      break;
    }
  }

  private Uri findPicutreUri() {
    Uri mPhotoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    return mPhotoUri;
  }

  public void startUploadingService(Uri fileUri, int videoCode) {
    String filePath = getRealPathFromURI(getApplicationContext(), fileUri, videoCode);
    Intent postChirpService = new Intent(DashboardActivity.this, PostImageService.class);
    postChirpService.putExtra(PostImageService.EXTRA_KEY_FILE_PATH, filePath);
    startService(postChirpService);
  }

  public String getRealPathFromURI(Context context, Uri contentUri, int fileCode) {
    Cursor cursor = null;
    try {
      String[] proj = { MediaStore.Images.Media.DATA };
      cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
      int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
      cursor.moveToFirst();
      return cursor.getString(column_index);
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }
}
