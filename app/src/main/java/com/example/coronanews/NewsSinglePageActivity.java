package com.example.coronanews;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class NewsSinglePageActivity extends AppCompatActivity {

    static TextView newsTitle, newsTime, newsSource, newsBody, newsTopTitle;
    News news;

    static Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            News news = (News) msg.obj;
            newsTitle.setText(news.getTitle());
            newsTime.setText(news.getTime());
            String source = "";
            if (news.getType().equals("news"))
                source = news.getSource();
            else if (news.getType().equals("paper"))
                source =  news.getAuthor().get(0);
            newsSource.setText(source);
            newsTopTitle.setText(source);
            newsBody.setText(news.getBody());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_single_page);
        newsTitle = findViewById(R.id.news_SP_title);
        newsTime = findViewById(R.id.news_SP_time);
        newsSource = findViewById(R.id.news_SP_source);
        newsBody = findViewById(R.id.news_SP_body);
        newsTopTitle = findViewById(R.id.news_page_top_title);

        final String id = getIntent().getStringExtra("ID");
        final NewsDao newsDao = NewsContent.getNewsDatabase().newsDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                news = newsDao.findById(id);
                Message msg = handler.obtainMessage();
                msg.obj = news;
                handler.sendMessage(msg);
            }
        }).start();

        Toolbar toolbar = findViewById(R.id.news_page_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
