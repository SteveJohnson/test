package com.loginext.loginextassignment;

import android.app.Application;

public class LogiNextApplication extends Application {
  private static LogiNextApplication instance;

  public static LogiNextApplication getInstance() {
    return instance;
  }

  @Override public void onCreate() {
    super.onCreate();

    instance = this;

  }

}