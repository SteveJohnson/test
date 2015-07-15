package com.loginext.loginextassignment.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loginext.loginextassignment.R;

public class LogiNextBaseActivity extends FragmentActivity {
  private Toolbar toolbar;
  protected ImageView mActionBarNavigationIconLeft;
  protected ImageView mActionBarNavigationIconRight;
  protected TextView mActionBarSectionTitle;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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

  protected void setToolbar(Toolbar toolbar) {
    this.toolbar = toolbar;
    buildBaseActionBar();
  }

  public Toolbar getToolbar() {
    return toolbar;
  }

  protected void buildBaseActionBar() {
    if(toolbar == null) {
      throw new ClassCastException("Toolbar not set from the activity");
    }

    /*setSupportActionBar(toolbar);*/

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
      toolbar.setContentInsetsAbsolute(0,0); 
    }

    findActionBarViewByIds(toolbar);
  }

  private void findActionBarViewByIds(View view) {
    mActionBarNavigationIconLeft = (ImageView) view.findViewById(R.id.action_bar_navigation_icon_left);
    mActionBarNavigationIconRight = (ImageView) view.findViewById(R.id.action_bar_navigation_icon_right);
    mActionBarSectionTitle = (TextView) view.findViewById(R.id.action_bar_section_title);
  }

  protected void setActionBarTitle(String string) {
    mActionBarSectionTitle.setText(string);
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
  }

}
