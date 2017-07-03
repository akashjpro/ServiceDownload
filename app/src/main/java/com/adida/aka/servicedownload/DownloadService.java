package com.adida.aka.servicedownload;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Aka on 7/3/2017.
 */

public class DownloadService extends Service {

    public String mPath;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Giữ được dữ liệu sau khi kill app // chỉ lưu 1 lần
//        return START_REDELIVER_INTENT;

        //chet khi kill app
//    return START_NOT_STICKY;
        String photoURL = "https://s-media-cache-ak0.pinimg.com/originals/4d/07/ff/4d07ff22aba1feaeebc817d46e6b1021.jpg";
        downloadFile(photoURL);
        //hồi sinh sau khi Kill app
        return START_STICKY;
    }

    private void downloadFile(final String photoURL) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(photoURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    InputStream inputStream = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                   mPath = saveFile(bitmap);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    private String saveFile(Bitmap bitmap) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/" + Environment.DIRECTORY_DOWNLOADS + "/" + System.currentTimeMillis() + ".jpg";

        try {
            File file = new File(path);
            if(file.exists()){
                file.delete();
            }
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            return  path;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }
}
