package com.loginext.loginextassignment.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.loginext.loginextassignment.common.AppConstants;
import com.loginext.loginextassignment.common.Util;

public class NetworkReceiver extends BroadcastReceiver{
  @Override public void onReceive(Context context, Intent intent) {

    if(Util.isNetworkAvailable() && Util.loadBoolean(AppConstants.STORE_FORWARD)){
      Util.startUploadingService(Uri.parse(Util.loadString(AppConstants.STORE)), AppConstants.IMAGE_CODE);
    }
  }
}