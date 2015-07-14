package com.loginext.loginextassignment.service;

import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.loginext.loginextassignment.common.AppConstants;
import com.loginext.loginextassignment.receiver.PostImageReceiver;
import com.parse.entity.mime.HttpMultipartMode;
import com.parse.entity.mime.MultipartEntity;
import com.parse.entity.mime.content.FileBody;

@SuppressWarnings("deprecation")
public class PostImageService extends Service {
  private long totalSize = 0;

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    if (intent != null) {
      String filePath = intent.getStringExtra(AppConstants.EXTRA_KEY_FILE_PATH);
      sendFile(filePath);
    }

    return super.onStartCommand(intent, flags, startId);
  }

  private void sendFile(String filePath) {
    postText("Gary's Test", new File(filePath));
  }

  public void postText(String postText, File file) {
    new Uploadtask(file).execute();
  }

  private String fullChirpPostUrl() {
    return "http://www.frozeniceplus.com/AndroidFileUpload/fileUpload.php";
  }

  @Override public void onDestroy() {
    super.onDestroy();
  }

  @Override public IBinder onBind(Intent intent) {
    return null;
  }

  private class Uploadtask extends AsyncTask<Void, Integer, String> {
    private File file;

    public Uploadtask(File file) {
      this.file = file;
    }

    @Override protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override protected void onProgressUpdate(Integer... progress) {
      Log.d(PostImageService.class.getSimpleName(), "Post Image Progress : " + progress[0]);
    }

    @Override protected String doInBackground(Void... params) {
      return upload();
    }

    private String upload() {
      String responseString = "no";

      File sourceFile = file;
      if (!sourceFile.isFile()) {
        return "not a file";
      }

      HttpClient httpclient = new DefaultHttpClient();
      HttpPost httppost = new HttpPost(fullChirpPostUrl());

      try {
        CustomMultiPartEntity entity = new CustomMultiPartEntity(new ProgressListener() {

          @Override public void transferred(long num) {
            publishProgress((int) ((num / (float) totalSize) * 100));
          }
        });

        entity.addPart("file", new FileBody(sourceFile));
        totalSize = entity.getContentLength();
        httppost.setEntity(entity);
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity r_entity = response.getEntity();
        responseString = EntityUtils.toString(r_entity);

      } catch (ClientProtocolException e) {
        responseString = e.toString();
      } catch (IOException e) {
        responseString = e.toString();
      }

      return responseString;

    }

    @Override protected void onPostExecute(String result) {
      super.onPostExecute(result);

      // return result
      Intent intentResponse = new Intent(PostImageService.this, PostImageReceiver.class);
      intentResponse.putExtra("test", "test");
      sendBroadcast(intentResponse);
    }

  }

  public class CustomMultiPartEntity extends MultipartEntity {
    private final ProgressListener listener;

    public CustomMultiPartEntity(final ProgressListener listener) {
      super();
      this.listener = listener;
    }

    public CustomMultiPartEntity(final HttpMultipartMode mode, final ProgressListener listener) {
      super(mode);
      this.listener = listener;
    }

    public CustomMultiPartEntity(HttpMultipartMode mode, final String boundary, final Charset charset, final ProgressListener listener) {
      super(mode, boundary, charset);
      this.listener = listener;
    }

    @Override public void writeTo(final OutputStream outstream) throws IOException {
      super.writeTo(new CountingOutputStream(outstream, this.listener));
    }

    public class CountingOutputStream extends FilterOutputStream {
      private final ProgressListener listener;
      private long transferred;

      public CountingOutputStream(final OutputStream out, final ProgressListener listener) {
        super(out);
        this.listener = listener;
        this.transferred = 0;
      }

      public void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
        this.transferred += len;
        this.listener.transferred(this.transferred);
      }

      public void write(int b) throws IOException {
        out.write(b);
        this.transferred++;
        this.listener.transferred(this.transferred);
      }
    }
  }

  public static interface ProgressListener {
    void transferred(long num);
  }
}
