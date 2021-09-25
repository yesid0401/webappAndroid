package com.tissini.webview.helpers;

import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadImageTask  extends AsyncTask<String, Void, Bitmap> {

    final NotificationManagerCompat manager;
    final NotificationCompat.Builder builder;

    public LoadImageTask(final NotificationManagerCompat manager, final NotificationCompat.Builder builder) {
        this.manager = manager;
        this.builder = builder;
    }

    @Override
    protected Bitmap doInBackground(final String... strings) {
        if (strings == null || strings.length == 0) {
            return null;
        }
        try {
            final URL url = new URL(strings[0]);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            final InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(final Bitmap bitmap) {
        if (bitmap == null || manager == null || builder == null) {
            return;
        }


        builder.setLargeIcon(bitmap);
        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null));
        manager.notify(4, builder.build());
    }
}
