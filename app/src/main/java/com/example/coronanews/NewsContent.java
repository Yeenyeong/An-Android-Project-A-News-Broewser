package com.example.coronanews;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

class News{
    private String id = "";
    private String title = "";
    private String body = "";
    private List<String> author = new ArrayList<>();
    private String type = "";
    private String source = "";
    private String time = "";
    private boolean hasRead = false;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public List<String> getAuthor() { return author; }
    public void setAuthor(List<String> author) { this.author = author; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public boolean getHasRead () { return hasRead; }
    public void setHasRead(boolean hasRead) { this.hasRead = hasRead; }
}

public class NewsContent extends Fragment {
    private int pageCount = 1;
    private List<News> dataSet = new LinkedList<>();
    RecyclerView recyclerView;
    NewsContentAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    String tabName;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.setLoadState(adapter.LOADING_COMPLETE);
        }
    };

    NewsContent(){}
    NewsContent(String tabName) {this.tabName = tabName;}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_recycle_view, container, false);

        recyclerView = view.findViewById(R.id.newsRecycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewsContentAdapter(dataSet);
        recyclerView.setAdapter(adapter);

//        //模拟获取数据
//        getData();

        swipeRefreshLayout = view.findViewById(R.id.newsSwipeFresh);
        swipeRefreshLayout.setRefreshing(true);
        refreshData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageCount = (int) (Math.random()*20);
                refreshData();
            }
        });

        recyclerView.addOnScrollListener(new NewsRecycleViewScrollListener() {
            @Override
            public void onLoadMore() {
                adapter.setLoadState(adapter.LOADING);
                loadMoreData();
            }
        });

        return view;
    }

    public void refreshData(){
        if (tabName.equals("新闻"))
            new NewsNetworking(mHandler,swipeRefreshLayout,dataSet,NewsNetworking.MODE_REFRESH,NewsNetworking.TYPE_NEWS, pageCount).start();
        if(tabName.equals("论文"))
            new NewsNetworking(mHandler,swipeRefreshLayout,dataSet,NewsNetworking.MODE_REFRESH,NewsNetworking.TYPE_PAPER, pageCount).start();
        pageCount++;
    }

    public void loadMoreData(){
        if (tabName.equals("新闻"))
            new NewsNetworking(mHandler,swipeRefreshLayout,dataSet,NewsNetworking.MODE_LOAD_MORE,NewsNetworking.TYPE_NEWS, pageCount).start();
        if  (tabName.equals("论文"))
            new NewsNetworking(mHandler,swipeRefreshLayout,dataSet,NewsNetworking.MODE_LOAD_MORE,NewsNetworking.TYPE_PAPER, pageCount).start();

        pageCount++;
    }
}
