package com.loginext.loginextassignment.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.loginext.loginextassignment.R;
import com.loginext.loginextassignment.common.AppConstants;
import com.loginext.loginextassignment.common.Util;
import com.loginext.loginextassignment.fragment.ImagePageFragment;

public class ImagePageActivity extends LogiNextBaseActivity {
  private Toolbar toolbar;
  private Uri image;
  private String title;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image_page_layout);

    toolbar = (Toolbar) findViewById(R.id.app_bar);
    setToolbar(toolbar);

    initValues();
    Util.replaceFragment(getSupportFragmentManager(), new ImagePageFragment(image, title), R.id.fragment_container);  
  }

  @Override protected void onStart() {
    super.onStart();
  }

  @Override protected void onResume() {
    super.onResume();
  }

  @Override protected void onPause() {
    super.onPause();
  }

  @Override protected void onStop() {
    super.onStop();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  }

  @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
  }

  @Override public void onBackPressed() {
    finish();
  }

  private void initValues() {
    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    image = Uri.parse(extras.getString(AppConstants.IMAGE_URI));
    title = extras.getString(AppConstants.IMAGE_TITLE);

    setActionBarTitle(extras.getString(AppConstants.ACTION_BAR_TITLE));

    if(toolbar != null) {
      toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }
  }

}
