package com.loginext.loginextassignment.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.loginext.loginextassignment.R;

public class SignOnActivity extends Activity {
  private SignaturePad mSignaturePad;
  private Button mClearButton;
  private Button mSaveButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signon);

    mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
    mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
      @Override
      public void onSigned() {
        mSaveButton.setEnabled(true);
        mClearButton.setEnabled(true);
      }

      @Override
      public void onClear() {
        mSaveButton.setEnabled(false);
        mClearButton.setEnabled(false);
      }
    });

    mClearButton = (Button) findViewById(R.id.clear_button);
    mSaveButton = (Button) findViewById(R.id.save_button);

    mClearButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mSignaturePad.clear();
      }
    });

    mSaveButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
        if(addSignatureToGallery(signatureBitmap)) {
          Toast.makeText(SignOnActivity.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(SignOnActivity.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  public File getAlbumStorageDir(String albumName) {
    // Get the directory for the user's public pictures directory.
    File file = new File(Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_PICTURES), albumName);
    if (!file.mkdirs()) {
      Log.e("SignaturePad", "Directory not created");
    }
    return file;
  }

  public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
    Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(newBitmap);
    canvas.drawColor(Color.WHITE);
    canvas.drawBitmap(bitmap, 0, 0, null);
    OutputStream stream = new FileOutputStream(photo);
    newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
    stream.close();
  }

  public boolean addSignatureToGallery(Bitmap signature) {
    boolean result = false;
    try {
      File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
      saveBitmapToJPG(signature, photo);
      Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
      Uri contentUri = Uri.fromFile(photo);
      mediaScanIntent.setData(contentUri);
      SignOnActivity.this.sendBroadcast(mediaScanIntent);
      result = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }
}
