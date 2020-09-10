package com.example.coronanews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class EntityImageView extends ImageView {

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap)msg.obj;
            setImageBitmap(bitmap);
        }
    };

    public EntityImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EntityImageView(Context context) {
        super(context);
    }

    public EntityImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void getImage(final String url) {
        if (url == "" || url == null) return;

        new Thread() {
            @Override
            public void run() {
                try {
                    URL img_url = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection)img_url.openConnection();
                    conn.setRequestMethod("GET");

                    InputStream in = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);

                    Message msg = Message.obtain();
                    msg.obj = bitmap;
                    handler.sendMessage(msg);

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }.start();
    }
}
