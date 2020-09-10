package com.example.coronanews;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.os.Handler;
import android.os.Message;

public class EntityNetwork extends Thread {
    private String ENTITY_URL;
    private String QUERY;

    private ArrayList<Entity> entity_list;

    private Handler handler;

    public EntityNetwork(Handler handler, final String query) {
        ENTITY_URL = "https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery?entity=";
        QUERY = query;
        this.handler = handler;
        entity_list = new ArrayList<>();
        entity_list.clear();
    }

    public void run() {
        try {
            URL url = new URL(ENTITY_URL + QUERY);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // load entity info
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String entity_msg = br.readLine();
            parseJson(entity_msg);

            Message msg = Message.obtain();
            msg.obj = entity_list;
            handler.handleMessage(msg);

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private void parseJson(String s) {
        try {
            JSONObject jsonObj = new JSONObject(s);
            JSONArray dataArr = jsonObj.getJSONArray("data");

            for (int i = 0; i < dataArr.length(); ++ i) {
                Entity ett = new Entity();
                JSONObject obj = dataArr.getJSONObject(i);
                ett.setLabel(obj.getString("label"));
                ett.setUrl(obj.getString("url"));

                if (obj.has("img")) {
                    ett.setImgUrl(obj.getString("img"));
                }
                else {
                    ett.setImgUrl("");
                }

                JSONObject abstractInfo = obj.getJSONObject("abstractInfo");
                JSONObject covid = abstractInfo.getJSONObject("COVID");

                ett.setEnwiki(abstractInfo.getString("enwiki"));
                ett.setBaidu(abstractInfo.getString("baidu"));
                ett.setZhwiki(abstractInfo.getString("zhwiki"));

                JSONObject properties = covid.getJSONObject("properties");
                JSONArray relations = covid.getJSONArray("relations");
                ett.setProperties(properties);
                ett.setRelations(relations);

                entity_list.add(ett);
/*
                System.out.println("label: "+ett.getLabel());
                System.out.println("url: "+ett.getUrl());
                System.out.println("img: "+ett.getImgUrl());
                System.out.println("enwiki: "+ett.getEnwiki());
                System.out.println("baidu: "+ett.getBaidu());
                System.out.println("zhwiki: "+ett.getZhwiki());
*/
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
