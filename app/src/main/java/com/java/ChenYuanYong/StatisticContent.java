package com.java.ChenYuanYong;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticContent extends Fragment {

    List<String> regions = new ArrayList<>();
    Map<String, RegionData> countryData = new HashMap<>();
    StatisticContentAdapter adapter;
    ExpandableListView expandableListView;
    SwipeRefreshLayout swipeRefreshLayout;
    String tabName;
    StatisticContent() {}
    StatisticContent(String tabName) {
        this.tabName = tabName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistic_expand, container, false);

        swipeRefreshLayout = view.findViewById(R.id.statistic_swipe_refresh);

        regions.add("loading...");
        RegionData regionData = new RegionData();
        regionData.addData(0, 0, 0);
        countryData.put("loading...", regionData);
        expandableListView = view.findViewById(R.id.statistic_expand);
        adapter = new StatisticContentAdapter(regions, countryData, getContext());
        expandableListView.setAdapter(adapter);

        swipeRefreshLayout.setRefreshing(true);
        new StatisticNetworking(tabName, mHandler).start();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new StatisticNetworking(tabName, mHandler).start();
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int position) {
                for (int i = 0; i < adapter.getGroupCount(); i++){
                    if (position!=i){
                        expandableListView.collapseGroup(i);
                    }
                }
            }
        });

        return view;
    }

    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Pack pack = (Pack) msg.obj;
            regions.clear();
            regions.addAll(pack.rg);
            Collections.sort(regions);
            countryData.clear();
            countryData.putAll(pack.mp);
            swipeRefreshLayout.setRefreshing(false);
            adapter.notifyDataSetChanged();
        }
    };
}

class Pack{
    List<String> rg;
    Map<String, RegionData> mp;
}


class RegionData {
    public class DailyData {
        public int confirmed, cured, dead;

        public DailyData(int confirmed, int cured, int dead) {
            this.confirmed = confirmed;
            this.cured = cured;
            this.dead = dead;
        }
    }

    List<DailyData> data = new ArrayList<>();

    RegionData() {}

    public void addData(int confirmed, int cured, int dead) {
        data.add(new DailyData(confirmed, cured, dead));
    }

    public DailyData getLatestData() {
        return data.get(data.size() - 1);
    }

    public List<DailyData> getData() {
        return data;
    }
}

class StatisticNetworking extends Thread {

    List<String> regions = new ArrayList<>();
    Map<String, RegionData> countryData = new HashMap<>();
    String tabName;
    private Handler mHandler;

    StatisticNetworking(String tabName, Handler mHandler) {
        this.tabName = tabName;
        this.mHandler = mHandler;
    }

    public void run() {
        try {
            URL url = new URL("https://covid-dashboard.aminer.cn/api/dist/epidemic.json");
            HttpURLConnection coon = (HttpURLConnection) url.openConnection();
            coon.setRequestMethod("GET");

            if (coon.getResponseCode() == 200) {
                //获取输入流
                BufferedReader in = new BufferedReader(new InputStreamReader(coon.getInputStream()));
                String msg = in.readLine(); //读入新闻列表
                parseData(msg);
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message message = mHandler.obtainMessage();
        Pack pack = new Pack();
        pack.mp = countryData;
        pack.rg = regions;
        message.obj = pack;
        mHandler.sendMessage(message);
//        mHandler.sendEmptyMessage(0);
    }

    private void parseData(String msg) {
        JsonObject obj = new JsonParser().parse(msg).getAsJsonObject(); //解析json数据
        for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
            String key = entry.getKey();
            JsonArray val = (JsonArray) entry.getValue().getAsJsonObject().get("data");

            if ((tabName.equals("国内")) && key.contains("China") && key.split("\\|").length == 2
                    || tabName.equals("国际") && !key.contains("China") && !key.contains("|")) {

                RegionData regionData = new RegionData();
                for (int i = val.size() - 10; i < val.size(); i++) {
                    JsonArray data = (JsonArray) val.get(i);
                    int confirm = data.get(0).getAsInt();
                    int cured = data.get(2).getAsInt();
                    int dead = data.get(3).getAsInt();
                    regionData.addData(confirm, cured, dead);
                }
                if (key.contains("|"))
                    key = key.split("\\|")[1];
                regions.add(key);
                countryData.put(key, regionData);
            }
        }
    }
}
