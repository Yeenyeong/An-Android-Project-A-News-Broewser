package com.java.ChenYuanYong;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.INotificationSideChannel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClusterContent extends Fragment {

    List<String> labels = new ArrayList<>();
    Map<String, List<Event>> cluster = new HashMap<>(); //<Label, Events>
    Map<String, Integer> wordCluster = new HashMap<>(); //<word, category>
    SwipeRefreshLayout swipeRefreshLayout;
    ExpandableListView expandableListView;
    ClusterContentAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cluster_expand, container, false);
        try {
            readRaw();
        } catch (IOException e) {
            e.printStackTrace();
        }

        swipeRefreshLayout = view.findViewById(R.id.cluster_swipe_refresh);
        expandableListView = view.findViewById(R.id.cluster_expand);
        labels.add("loading...");
        List<Event> tmp = new ArrayList<>();
        Event event = new Event();
        event.title = "none";
        event.date = "none";
        tmp.add(event);
        cluster.put("loading...", tmp);

        new ClusterNetworking(mHandler, wordCluster).start();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ClusterNetworking(mHandler, wordCluster).start();
            }
        });

        adapter = new ClusterContentAdapter(labels, cluster, getContext());
        expandableListView.setAdapter(adapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int position) {
                for (int i = 0; i < adapter.getGroupCount(); i++) {
                    if (position != i) {
                        expandableListView.collapseGroup(i);
                    }
                }
            }
        });

        return view;
    }

    Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            cluster.clear();
            labels.clear();

            cluster.putAll((Map<String, List<Event>>) msg.obj);
            labels.addAll(cluster.keySet());
            swipeRefreshLayout.setRefreshing(false);
            adapter.updateData(labels,cluster);
        }
    };

    private void readRaw() throws IOException {
        InputStream is = getResources().openRawResource(R.raw.word_cluster);
        Reader in = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(in);
        String line = null;
        while (null != (line = bufferedReader.readLine())) {
            String[] s = line.split(" ");
            wordCluster.put(s[0], Integer.parseInt(s[1]));
        }
        bufferedReader.close();
        in.close();
        is.close();
    }

}

class Event implements Serializable {
    String id;
    String title;
    int category;
    String date;
}

class ClusterNetworking extends Thread {

    Map<String, List<Event>> cluster = new HashMap<>(); //<Label, Events>
    Map<String, Integer> wordCluster; //<word, category>
    Map<String, Integer> wordCount = new HashMap<>(); // <word, count>
    List<Event> allEvents = new ArrayList<>();
    List<String> labels = new ArrayList<>();

    int category_num = 8;

    private Handler mHandler;

    ClusterNetworking(Handler mHandler, Map<String, Integer> wordCluster) {
        this.mHandler = mHandler;
        this.wordCluster = wordCluster;
    }

    public void run() {
        try {
//            int size = (int) (Math.random() * 600 + 100);
            int size = 700;
            URL url = new URL("https://covid-dashboard.aminer.cn/api/events/list?type=event&size=" + size);
            HttpURLConnection coon = (HttpURLConnection) url.openConnection();
            coon.setRequestMethod("GET");

            if (coon.getResponseCode() == 200) {
                //获取输入流
                BufferedReader in = new BufferedReader(new InputStreamReader(coon.getInputStream()));
                String msg = in.readLine();
                parseData(msg);
                clusterData();
                in.close();
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        Message message = mHandler.obtainMessage();
        message.obj = cluster;
        mHandler.sendMessage(message);
    }

    private void parseData(String msg) throws JSONException, IOException {
        JSONObject obj = new JSONObject(msg); //解析json数据
        JSONArray data = obj.getJSONArray("data");
        for (int i = 0; i < data.length(); i++) {
            Event event = new Event();
            JSONObject singleOne = data.getJSONObject(i);
            event.id = singleOne.getString("_id");
            event.title = singleOne.getString("title");
            event.date = singleOne.getString("date");

            String text = singleOne.getString("seg_text");
            String[] segText = text.split(" ");

            int[] count = new int[category_num]; //分为10类
            // 统计每一类对应的关键词的数量
            for (int j = 0; j < segText.length; j++) {
                if (wordCluster.containsKey(segText[j])) {
                    count[wordCluster.get(segText[j])]++;
                }
            }
            // 找到关键词数量最多的类
            int maximum = -1;
            int category = -1;
            for (int j = 0; j < category_num; j++) {
                if (maximum < count[j]) {
                    maximum = count[j];
                    category = j;
                }
            }
            event.category = category;
            // 用于分类的关键词计数
            for (int j = 0; j < segText.length; j++) {
                if (wordCluster.containsKey(segText[j]) && wordCluster.get(segText[j]) == category) {
                    if (wordCount.containsKey(segText[j]))
                        wordCount.put(segText[j], wordCount.get(segText[j]) + 1);
                    else
                        wordCount.put(segText[j], 1);

                }
            }
            allEvents.add(event);
        }
    }

    private void clusterData() {
        // 找到每一类对应的计数最多的标签
        for (int i = 0; i < category_num; i++) {
            int maximum = -1;
            String label = "";
            for (String key : wordCount.keySet()) {
                if (wordCluster.get(key) == i && wordCount.get(key) > maximum) {
                    maximum = wordCount.get(key);
                    label = key;
                }
            }
            labels.add(label);
        }

        // 将event分为category_num类
        for (int i = 0; i < category_num; i++) {
            List<Event> list = new ArrayList<>();
            for (Event e : allEvents) {
                if (e.category == i) {
                    list.add(e);
                }
            }
            cluster.put(labels.get(i), list);
        }
    }

}
