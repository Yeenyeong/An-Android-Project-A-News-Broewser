package com.example.coronanews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class NewsSinglePageActivity extends AppCompatActivity {
    final int DISLIKE_NEWS = -10, LIKE_NEWS = -20, FOUND_NEWS = -30;
    TextView newsTitle, newsTime, newsSource, newsBody, newsTopTitle;
    News news;
    String id;
    Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == DISLIKE_NEWS){
                Toast.makeText(NewsSinglePageActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
            } else if(msg.what == LIKE_NEWS){
                Toast.makeText(NewsSinglePageActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
            } else if(msg.what == FOUND_NEWS){
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
        id = getIntent().getStringExtra("ID");

        new Thread(new Runnable() {
            @Override
            public void run() {
                NewsDao newsDao = NewsContent.getNewsDao();
                news = newsDao.findById(id);
                handler.sendEmptyMessage(FOUND_NEWS);
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
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.news_share:
                Intent intent = new Intent(this,NewsSharePageActivity.class);
                intent.putExtra("TITLE",news.getTitle());
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
