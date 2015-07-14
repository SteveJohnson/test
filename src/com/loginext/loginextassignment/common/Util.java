package com.loginext.loginextassignment.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.loginext.loginextassignment.LogiNextApplication;

public class Util {

  public interface OnDialogListener {
    public void onPositiveButton();
    public void onNegativeButton();
  }

  /**
   * Add Fragment Without Back Stack Entry
   */
  public static void addFragment(FragmentManager fragmentManager, Fragment fragment, int layout) {
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.add(layout, fragment, fragment.getClass().getSimpleName());
    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();
  }

  /**
   * Replace Fragment Without Back Stack Entry
   */
  public static void replaceFragment(FragmentManager fragmentManager, Fragment fragment, int layout) {
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(layout, fragment, fragment.getClass().getSimpleName());
    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();
  }

  public static void startActivity(Class<? extends Activity> clazz, Bundle extras) {
    Context context = LogiNextApplication.getInstance().getApplicationContext();
    Intent intent = new Intent(context, clazz);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.putExtras(extras);
    context.startActivity(intent);
  }

  public static void loadFragmentNotAddingStackEntry(FragmentManager fragmentManager, Fragment fragment, int layout, String fragmentTag) {
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(layout, fragment, fragmentTag);
    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();
  }

  public static String loadString(String key) {
    Context context = LogiNextApplication.getInstance().getApplicationContext();
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    return sharedPreferences.getString(key, String.valueOf(1));
  }

  public static boolean loadBoolean(String key) {
    Context context = LogiNextApplication.getInstance().getApplicationContext();
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    return sharedPreferences.getBoolean(key, false);
  }

  public static int loadInt(String key) {
    Context context = LogiNextApplication.getInstance().getApplicationContext();
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    return sharedPreferences.getInt(key, -1);
  }

  public static void saveString(String key, String value) {
    Context context = LogiNextApplication.getInstance().getApplicationContext();
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(key, value);
    editor.commit();
    editor.apply();
  }

  public static void saveBoolean(String key, boolean bool) {
    Context context = LogiNextApplication.getInstance().getApplicationContext();
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putBoolean(key, bool);
    editor.commit();
    editor.apply();
  }

  public static void saveInt(String key, int number) {
    Context context = LogiNextApplication.getInstance().getApplicationContext();
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putInt(key, number);
    editor.commit();
    editor.apply();
  }

  public static void createDialog(Context context, String title, String message, String positiveButton, String negativeButton, final OnDialogListener onDialogListener) {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
    if(title != null) {
      alertDialogBuilder.setTitle(title);  
    }

    if(message != null) {
      alertDialogBuilder.setMessage(message);
    }

    if(positiveButton != null) {
      alertDialogBuilder.setPositiveButton(positiveButton, new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          dialog.dismiss();
          onDialogListener.onPositiveButton();
        }
      });
    }

    if(negativeButton != null) {
      alertDialogBuilder.setNegativeButton(negativeButton, new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          dialog.dismiss();
          onDialogListener.onNegativeButton();
        }
      });
    }

    alertDialogBuilder.show();
  }
}