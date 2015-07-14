package com.loginext.loginextassignment.fragment;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouch.OnImageViewTouchSingleTapListener;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase.DisplayType;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.loginext.loginextassignment.R;
import com.loginext.loginextassignment.common.AppConstants;
import com.loginext.loginextassignment.service.PostImageService;

public class ImagePageFragment extends Fragment implements OnImageViewTouchSingleTapListener, OnClickListener{
  private Uri image;
  private String title;
  /**
   * Views to be set values to if applicable
   */
  private ImageViewTouch mArticleImage;
  private TextView mArticleDescription;
  private Toolbar toolbar;
  private Button upload;

  public ImagePageFragment() {
  }

  public ImagePageFragment(Uri image, String title) {
    this();
    this.image = image;
    this.title = title;
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_article_image_layout, container, false);
    findViewByIds(view);
    return view;
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if(savedInstanceState != null) {
      image = Uri.parse(savedInstanceState.getString(AppConstants.IMAGE));
      title = savedInstanceState.getString(AppConstants.TITLE);
    }

    setValuesToViews();
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putString(AppConstants.IMAGE, String.valueOf(image));
    outState.putString(AppConstants.TITLE, title);
  }

  private void findViewByIds(View view) {
    mArticleImage = (ImageViewTouch) view.findViewById(R.id.article_image);
    mArticleDescription = (TextView) view.findViewById(R.id.article_image_description);
    upload = (Button) view.findViewById(R.id.upload);
  }

  private void setValuesToViews() {
    String filePath = getRealPathFromURI(getActivity().getApplicationContext(), image, AppConstants.IMAGE_CODE);
    mArticleImage.setDisplayType(DisplayType.FIT_TO_SCREEN);
    mArticleImage.setImageBitmap(BitmapFactory.decodeFile(filePath));  
    mArticleImage.setSingleTapListener(this);
    mArticleDescription.setText(title);
    upload.setOnClickListener(this);
  }

  private void toggleDetailView() {
    if(toolbar == null) {
      return;
    }

    if(toolbar.getVisibility() == View.VISIBLE) {
      hideComponents();
    } else if(toolbar.getVisibility() == View.GONE) {
      showComponents();
    }
  }

  private void showComponents() {
    if(toolbar != null) {
      toolbar.setVisibility(View.VISIBLE);  
    }

    if(mArticleDescription != null) {
      mArticleDescription.setVisibility(View.VISIBLE);
    }
  }

  private void hideComponents() {
    if(toolbar != null) {
      toolbar.setVisibility(View.GONE);  
    }

    if(mArticleDescription != null) {
      mArticleDescription.setVisibility(View.GONE);  
    }
  }

  public void onShareClick() {
    createShareIntent();
  }

  private void createShareIntent() {
    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setType("text/plain");
    shareIntent.putExtra(Intent.EXTRA_TEXT, image);
    startActivity(Intent.createChooser(shareIntent, "Share..."));
  }

  @Override public void onSingleTapConfirmed() {
    toggleDetailView();
  }

  @Override public void onClick(View v) {
    startUploadingService(image, AppConstants.IMAGE_CODE);
  }

  public void startUploadingService(Uri fileUri, int videoCode) {
    String filePath = getRealPathFromURI(getActivity(), fileUri, videoCode);
    Intent postChirpService = new Intent(getActivity(), PostImageService.class);
    postChirpService.putExtra(AppConstants.EXTRA_KEY_FILE_PATH, filePath);
    getActivity().startService(postChirpService);
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
