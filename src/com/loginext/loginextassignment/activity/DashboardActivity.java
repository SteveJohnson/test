package com.loginext.loginextassignment.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.loginext.loginextassignment.R;
import com.loginext.loginextassignment.common.AppConstants;
import com.loginext.loginextassignment.common.Util;
import com.loginext.loginextassignment.common.Util.OnDialogListener;

public class DashboardActivity extends Activity implements OnClickListener, OnDialogListener{
  private GoogleMap googleMap;
  private Button eSignature;
  private Button photo;
  private Uri imageFile;

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
      startActivityForResult(intent, AppConstants.SAVE_SIGNATURE);
      break;
    case R.id.photo:
      Util.createDialog(DashboardActivity.this, "Capture or Pic from Gallery", "Capture or Pic from Gallery", "Capture", "Pic", this);
      break;
    default:
      Log.d(DashboardActivity.class.getSimpleName(), "No Click Listener for this type");
      break;
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Uri imageUri = null;
    Bundle extras = new Bundle();

    if(resultCode != RESULT_OK) {
      Log.d(DashboardActivity.class.getSimpleName(), "Apologies Error in retrieving Image");
      return;
    }

    switch (requestCode) {
    case AppConstants.REQUEST_IMAGE_CAPTURE:
      extras.putString(AppConstants.IMAGE_URI, String.valueOf(imageFile));
      Util.startActivity(ImagePageActivity.class, extras);  
      break;
    case AppConstants.IMAGE_PICKER_SELECT:
      imageUri = data.getData();
      extras.putString(AppConstants.IMAGE_URI, String.valueOf(imageUri));
      Util.startActivity(ImagePageActivity.class, extras);
      break;
    default:
      break;
    }
  }

  private void takeCustomPicture() {
    imageFile  = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFile);
    startActivityForResult(intent, AppConstants.REQUEST_IMAGE_CAPTURE);
  }

  private void pickPhotoIntent() {
    Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    if (pickPhotoIntent.resolveActivity(getPackageManager()) != null) {
      startActivityForResult(pickPhotoIntent, AppConstants.IMAGE_PICKER_SELECT);
    }
  }

  @Override public void onPositiveButton() {
    takeCustomPicture();
  }

  @Override public void onNegativeButton() {
    pickPhotoIntent();
  }
}
