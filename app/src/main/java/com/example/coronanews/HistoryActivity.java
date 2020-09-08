package com.example.coronanews;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.TintInfo;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    List<News> historyNews = new ArrayList<>();
    String queryType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_page);

        Intent intent = getIntent();
        queryType = intent.getStringExtra("USER_FRAGMENT");

        Toolbar toolbar = findViewById(R.id.history_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标

        TextView title = findViewById(R.id.history_page_title);
        if (queryType.equals("CACHE")){
            title.setText("本地缓存");
        }

        final HistoryRecycleViewAdapter adapter = new HistoryRecycleViewAdapter(historyNews);
        RecyclerView recyclerView = findViewById(R.id.history_recycle_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        final Handler handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                adapter.updateData(historyNews);
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<News> result = new ArrayList<>();
                NewsDao newsDao = NewsContent.getNewsDao();
                if (queryType.equals("HISTORY"))
                    result = newsDao.getRead();
                else if (queryType.equals("CACHE"))
                    result = newsDao.getAll();

                if(result.size()==0){
                    News news = new News();
                    news.setId("NONE");
                    result.add(news);
                }
                historyNews.clear();
                historyNews = result;
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

class Holder extends RecyclerView.ViewHolder {
    public TextView newsTitle;
    public TextView newsBody;
    public TextView newsSource;
    public LinearLayout newsItem;
    public Holder(View view) {
        super(view);
        newsTitle = view.findViewById(R.id.news_SP_title);
        newsBody = view.findViewById(R.id.newsBody);
        newsSource = view.findViewById(R.id.newsSource);
        newsItem = view.findViewById(R.id.newsItem);
    }
}

class HistoryRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    News news;
    List<News> historyNews;
    HistoryRecycleViewAdapter(List<News> searchResultId){
        this.historyNews =searchResultId;
    }

    public void updateData(List<News> list){
        this.historyNews.clear();
        this.historyNews = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_content, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final News news = historyNews.get(position);
        final Holder viewHolder = (Holder) holder;

        if (news.getId().equals("NONE")){
            viewHolder.newsTitle.setText("没有浏览历史...");
            viewHolder.newsBody.setText("有一说一");
            viewHolder.newsSource.setText("确实没有");
        } else {
            viewHolder.newsTitle.setText(news.getTitle());
            String body = news.getBody();
            if (body.length() > 60) {
                body = body.substring(0, 60) + "...";//设置正文最大预览长度为60
            }

            //设置正文
            viewHolder.newsBody.setText(body);
            if (body.length() == 0)
                viewHolder.newsBody.setText("无正文");

            //设置来源
            if (news.getType().equals("news")) {
                String source = news.getSource();
                source = "来源:" + source;
                viewHolder.newsSource.setText(source);
            } else {
                viewHolder.newsSource.setText(news.getAuthor().get(0));
            }

            viewHolder.newsItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(),NewsSinglePageActivity.class);
                    intent.putExtra("ID",news.getId());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return historyNews.size();
    }
}
