package com.loginext.loginextassignment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

public class PostImageReceiver extends BroadcastReceiver {
  @Override public void onReceive(Context context, Intent intent) {
    MediaPlayer mp = MediaPlayer.create(context, R.raw.image_posted);
    mp.start();
  }
}