package com.example.coronanews;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.LinkedList;
import java.util.List;

abstract class NewsRecycleViewScrollListener extends RecyclerView.OnScrollListener {
    private boolean isSlidingUp = false;

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            int itemCount = manager.getItemCount();

            int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
            if (lastItemPosition == (itemCount - 1) && isSlidingUp) {
                onLoadMore();
            }
        }
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        isSlidingUp = dy > 0;
    }

    public abstract void onLoadMore();
}

public class NewsContent extends Fragment {

    private static NewsDatabase db;
    private static NewsDao newsDao;
    public static NewsDao getNewsDao() { return newsDao; }

    private int pageCount = 1;
    private List<News> dataSet = new LinkedList<>();
    RecyclerView recyclerView;
    NewsContentAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    String tabName;

    NewsContent(){}
    NewsContent(String tabName) {this.tabName = tabName;}


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        db = Room.databaseBuilder(getContext().getApplicationContext(), NewsDatabase.class,"news-database")
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE).build();
        newsDao = db.newsDao();

        View view = inflater.inflate(R.layout.news_recycle_view, container, false);

        adapter = new NewsContentAdapter(dataSet);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.newsRecycleView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new NewsRecycleViewScrollListener() {
            @Override
            public void onLoadMore() {
                adapter.setLoadState(adapter.LOADING);
                loadMoreData();
            }
        });

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

        return view;
    }


    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.setLoadState(adapter.LOADING_COMPLETE);
        }
    };

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
