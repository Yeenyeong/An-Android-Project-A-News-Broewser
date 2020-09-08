package com.example.coronanews;

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
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class NewsSearchActivity extends AppCompatActivity {

    List<String> searchResultId = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_search_page);

        Toolbar toolbar = findViewById(R.id.news_search_page_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标

        final SearchRecycleViewAdapter adapter = new SearchRecycleViewAdapter(searchResultId);
        RecyclerView recyclerView = findViewById(R.id.news_search_recycle_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        final SearchView searchView = findViewById(R.id.newsSearchView);
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
//        searchView.requestFocus();

        final Handler handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                adapter.updateData(searchResultId);
            }
        };


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<String> result = NewsContent.getNewsDao().search(s);
                        if(result.size()==0){
                            Toast.makeText(NewsSearchActivity.this,"找不到新闻",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            searchResultId = result;
                            handler.sendEmptyMessage(0);
                        }
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

class viewHolder extends RecyclerView.ViewHolder {
    static public TextView newsTitle;
    static public TextView newsBody;
    static public TextView newsSource;
    static public LinearLayout newsItem;
    public viewHolder(View view) {
        super(view);
        newsTitle = view.findViewById(R.id.news_SP_title);
        newsBody = view.findViewById(R.id.newsBody);
        newsSource = view.findViewById(R.id.newsSource);
        newsItem = view.findViewById(R.id.newsItem);
    }
}

class SearchRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    News news;
    List<String> searchResultId;
    SearchRecycleViewAdapter(List<String> searchResultId){
        this.searchResultId=searchResultId;
    }

    public void updateData(List<String> list){
        this.searchResultId = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_content, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final String id = searchResultId.get(position);
        final Handler handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                viewHolder.newsTitle.setText(news.getTitle());
                String body = news.getBody();
                if (body.length()>60) {
                    body = body.substring(0, 60) + "...";//设置正文最大预览长度为60
                }

                //设置正文
                viewHolder.newsBody.setText(body);
                if (body.length()==0)
                    viewHolder.newsBody.setVisibility(View.GONE);

                //设置来源
                if (news.getType().equals("news")){
                    String source = news.getSource();
                    source = "来源:"+source;
                    viewHolder.newsSource.setText(source);
                }else {
                    viewHolder.newsSource.setVisibility(View.GONE);
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                news = NewsContent.getNewsDao().findById(id);
                handler.sendEmptyMessage(0);
            }
        }).start();

        viewHolder.newsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),NewsSinglePageActivity.class);
                intent.putExtra("ID",id);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchResultId.size();
    }
}
