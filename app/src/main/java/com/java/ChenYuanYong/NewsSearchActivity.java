package com.java.ChenYuanYong;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class NewsSearchActivity extends AppCompatActivity {

    List<News> searchResult = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_search_page);

        Toolbar toolbar = findViewById(R.id.news_search_page_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标

        final NewsSearchRecycleViewAdapter adapter = new NewsSearchRecycleViewAdapter(searchResult);
        RecyclerView recyclerView = findViewById(R.id.news_search_recycle_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        final SearchView searchView = findViewById(R.id.newsSearchView);
        //设置搜索框的样式
        try {
            Class<?> argClass = searchView.getClass();
            Field ownField = argClass.getDeclaredField("mSearchPlate");
            Field ownField2 = argClass.getDeclaredField("mSubmitArea");
            ownField.setAccessible(true);
            ownField2.setAccessible(true);
            View mView = (View) ownField.get(searchView);
            mView.setBackgroundColor(Color.TRANSPARENT);
            View mView2 = (View) ownField2.get(searchView);
            mView2.setBackgroundColor(Color.TRANSPARENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        searchView.setSubmitButtonEnabled(true);

        final Handler handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                adapter.updateData(searchResult);
            }
        };

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<News> result = NewsContent.getNewsDao().search(s);
                        if(result.size()==0){
                            News news = new News();
                            news.setId("NONE");
                            result.add(news);
                        }
                        searchResult.clear();
                        searchResult = result;
                        handler.sendEmptyMessage(0);
                    }
                }).start();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
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

class ViewHolder extends RecyclerView.ViewHolder {
    public TextView newsTitle;
    public TextView newsBody;
    public TextView newsSource;
    public LinearLayout newsItem;
    public ViewHolder(View view) {
        super(view);
        newsTitle = view.findViewById(R.id.news_SP_title);
        newsBody = view.findViewById(R.id.newsBody);
        newsSource = view.findViewById(R.id.newsSource);
        newsItem = view.findViewById(R.id.newsItem);
    }
}

class NewsSearchRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    News news;
    List<News> searchResult;
    NewsSearchRecycleViewAdapter(List<News> searchResultId){
        this.searchResult =searchResultId;
    }

    public void updateData(List<News> list){
        this.searchResult.clear();
        this.searchResult = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final News news = searchResult.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;

        if (news.getId().equals("NONE")){
            viewHolder.newsTitle.setText("没有搜到想要内容诶...");
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
        return searchResult.size();
    }
}
