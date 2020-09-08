package com.example.coronanews;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.os.Handler;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsNetworking extends Thread {
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
            NewsDao newsDao = NewsContent.getNewsDatabase().newsDao();
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
        return news;
    }
}