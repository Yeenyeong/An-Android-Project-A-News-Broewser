package com.java.ChenYuanYong;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ScholarFragment extends Fragment {

    List<Scholar> scholars = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ScholarRecycleViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scholar, container, false);

        recyclerView = view.findViewById(R.id.scholar_recycle_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ScholarRecycleViewAdapter(scholars);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = view.findViewById(R.id.scholar_swipe_refresh);
        swipeRefreshLayout.setRefreshing(true);
        new ScholarNetworking(mHandler).start();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ScholarNetworking(mHandler).start();
            }
        });
        return view;
    }

    Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            swipeRefreshLayout.setRefreshing(false);
            scholars.clear();
            scholars.addAll((List<Scholar>)msg.obj);
            adapter.notifyDataSetChanged();
        }
    };
}


class ScholarRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Scholar> scholars;

    ScholarRecycleViewAdapter(List<Scholar> scholars) {
        this.scholars = scholars;
    }

    class Holder extends RecyclerView.ViewHolder {
        LinearLayout scholar_item;
        MyImageView scholar_avatar;
        TextView scholar_name;
        TextView scholar_position;
        TextView scholar_affiliation;

        public Holder(View view) {
            super(view);
            scholar_item = view.findViewById(R.id.scholar_item);
            scholar_avatar = view.findViewById(R.id.scholar_avatar);
            scholar_name = view.findViewById(R.id.scholar_name);
            scholar_position = view.findViewById(R.id.scholar_position);
            scholar_affiliation = view.findViewById(R.id.scholar_affiliation);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scholar_content, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final Scholar scholar = scholars.get(position);
        final Holder viewHolder = (Holder) holder;

        if (!scholar.name_zh.equals(""))
            viewHolder.scholar_name.setText(scholar.name_zh);
        else
            viewHolder.scholar_name.setText(scholar.name);

        viewHolder.scholar_position.setText(scholar.position);
        viewHolder.scholar_affiliation.setText(scholar.affiliation);
        viewHolder.scholar_avatar.setImageURL(scholar.avatar);

        viewHolder.scholar_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ScholarSinglePage.class);
                intent.putExtra("SCHOLAR",scholar);
                view.getContext().startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return scholars.size();
    }
}

class Scholar implements Serializable {
    String id;
    String name;
    String name_zh;
    String avatar;
    String position;
    String affiliation;
    String bio;
    String edu;
    String work;
    boolean isPassedAway;
}

class ScholarNetworking extends Thread {

    List<Scholar> scholars = new ArrayList<>();
    private Handler mHandler;

    ScholarNetworking(Handler mHandler) {
        this.mHandler = mHandler;
    }

    public void run() {
        try {
            URL url = new URL("https://innovaapi.aminer.cn/predictor/api/v1/valhalla/highlight/get_ncov_expers_list?v=2");
            HttpURLConnection coon = (HttpURLConnection) url.openConnection();
            coon.setRequestMethod("GET");

            if (coon.getResponseCode() == 200) {
                //获取输入流
                BufferedReader in = new BufferedReader(new InputStreamReader(coon.getInputStream()));
                String msg = in.readLine(); //读入学者
                parseData(msg);
                in.close();
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        Message message = mHandler.obtainMessage();
        message.obj = scholars;
        mHandler.sendMessage(message);
    }

    private void parseData(String msg) throws JSONException, IOException {
        JSONObject obj = new JSONObject(msg); //解析json数据
        JSONArray data = obj.getJSONArray("data");
        for (int i = 0; i < data.length(); i++) {

            Scholar scholar = new Scholar();

            JSONObject singleOne = data.getJSONObject(i);

            scholar.id = singleOne.getString("id");
            scholar.name = singleOne.getString("name");
            scholar.name_zh = singleOne.getString("name_zh");

            scholar.avatar = singleOne.getString("avatar");

            JSONObject profile = singleOne.getJSONObject("profile");
            scholar.affiliation = profile.getString("affiliation");
            scholar.bio = profile.getString("bio");
            scholar.bio = scholar.bio.replace("<br><br>","\n\n");
            scholar.bio = scholar.bio.replace("<br>","\n\n");

            if (profile.has("position"))
                scholar.position = profile.getString("position");
            else
                scholar.position = "";

            if (profile.has("edu"))
                scholar.edu = profile.getString("edu");
            else
                scholar.edu = "";
            scholar.edu = scholar.bio.replace("<br><br>","\n\n");
            scholar.edu = scholar.bio.replace("<br>","\n\n");

            if (profile.has("work"))
                scholar.work = profile.getString("work");
            else
                scholar.work = "";
            scholar.work = scholar.work.replace("<br><br>","\n\n");
            scholar.work = scholar.work.replace("<br>","\n\n");

            scholar.isPassedAway = singleOne.getBoolean("is_passedaway");
            scholars.add(scholar);
        }
    }
}
