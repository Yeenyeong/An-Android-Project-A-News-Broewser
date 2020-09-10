package com.java.ChenYuanYong;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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

class NewsNetworking extends Thread {
    private int pageCount;
    private Handler mHandler;
    private SwipeRefreshLayout s;
    private List<News> dataSet;
    private int MODE, TYPE;
    public final static int MODE_REFRESH = 0, MODE_LOAD_MORE = 1;
    public final static int TYPE_NEWS = 0, TYPE_PAPER = 1;

    NewsNetworking(Handler handler, SwipeRefreshLayout s, List<News> dataSet, int MODE, int TYPE, int pageCount) {
        this.mHandler = handler;
        this.s = s;
        this.dataSet = dataSet;
        this.MODE = MODE;
        this.TYPE = TYPE;
        this.pageCount = pageCount;
    }

    public void run() {
        try {
            URL url = setURL();
            HttpURLConnection coon = (HttpURLConnection) url.openConnection();
            coon.setRequestMethod("GET");

            if (MODE == MODE_REFRESH)
                dataSet.clear();

            if (coon.getResponseCode() == 200) {
                //获取输入流
                BufferedReader in = new BufferedReader(new InputStreamReader(coon.getInputStream()));
                String msg = in.readLine(); //读入新闻列表
                parseInput(msg);
                s.setRefreshing(false);
                in.close();
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        mHandler.sendEmptyMessage(0);
    }

    private URL setURL() throws MalformedURLException {
        URL url;
        if (TYPE == TYPE_NEWS){
            url = new URL("https://covid-dashboard.aminer.cn/api/events/list" + "?type=news" + "&page=" + pageCount);
        }
        else {
            url = new URL("https://covid-dashboard.aminer.cn/api/events/list" + "?type=paper" + "&page=" + pageCount + "&size=10");
        }
        return url;
    }

    private void parseInput(String msg) throws JSONException {
        JSONObject obj = new JSONObject(msg); //解析json数据
        JSONArray data = obj.getJSONArray("data"); //获取data
        List<JSONObject> newsContent = new ArrayList<>();
        for (int i = 0; i < data.length(); i++)
            newsContent.add(data.getJSONObject(i));
        //这里根据模式的不同需要不同的加载方式
        for (int i = 0; i < newsContent.size(); i++) {
            JSONObject content = newsContent.get(i);
            News news = parseJson(content);
            dataSet.add(news);
            NewsDao newsDao = NewsContent.getNewsDao();
            newsDao.insert(news);
        }
    }

    private News parseJson(JSONObject content) throws JSONException {
        News news = new News();
        news.setId(content.getString("_id"));
        news.setTitle(content.getString("title"));
        news.setBody(content.getString("content"));
        news.setSource(content.getString("source"));
        news.setTime(content.getString("date"));
        news.setType(content.getString("type"));
        if (news.getType().equals("paper")) {
            JSONArray authorList = content.getJSONArray("authors");
            for (int j = 0; j < authorList.length(); j++) {
                List<String> list = new ArrayList<>();
                JSONObject author = authorList.getJSONObject(j);
                if (author.has("name"))
                    list.add(author.getString("name"));
                if (author.has("name_zh"))
                    list.add(author.getString("name_zh"));
                news.setAuthor(list);
            }
        }
        news.setHasRead(false);
        return news;
    }
}
