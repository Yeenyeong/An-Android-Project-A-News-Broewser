package com.java.ChenYuanYong;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cheng.channel.Channel;
import com.cheng.channel.ChannelView;
import com.cheng.channel.ViewHolder;
import com.cheng.channel.adapter.BaseStyleAdapter;
import com.cheng.channel.adapter.ChannelListenerAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.java.ChenYuanYong.NewsFragment.TAB_NEWS_STATUS;
import static com.java.ChenYuanYong.NewsFragment.TAB_PAPER_STATUS;
import static com.java.ChenYuanYong.NewsFragment.TAB_PRESERVED;
import static com.java.ChenYuanYong.NewsFragment.TAB_SELECTED_FIRST;
import static com.java.ChenYuanYong.NewsFragment.TAB_SELECTED_SECOND;

public class CustomChannelActivity extends AppCompatActivity {
    private String TAG = "CustomChannelActivity:";
    private ChannelView channelView;
    private LinkedHashMap<String, List<Channel>> data = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_channel);

        channelView = findViewById(R.id.channelView);
        init();
    }

    private void init() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int tabNewsStatus = sharedPref.getInt(TAB_NEWS_STATUS, TAB_SELECTED_FIRST);
        int tabPaperStatus = sharedPref.getInt(TAB_PAPER_STATUS, TAB_SELECTED_SECOND);

        List<String> myChannel = new ArrayList<>();
        List<String> recommendChannel1 = new ArrayList<>();

        myChannel.add("聚类");
        if (tabNewsStatus==TAB_SELECTED_FIRST){
            myChannel.add("新闻");
        } else if (tabPaperStatus==TAB_SELECTED_FIRST){
            myChannel.add("论文");
        }
        if (tabNewsStatus==TAB_SELECTED_SECOND){
            myChannel.add("新闻");
        } else if (tabPaperStatus==TAB_SELECTED_SECOND){
            myChannel.add("论文");
        }
        if (tabNewsStatus==TAB_PRESERVED){
            recommendChannel1.add("新闻");
        }
        if (tabPaperStatus==TAB_PRESERVED){
            recommendChannel1.add("论文");
        }

        List<Channel> myChannelList = new ArrayList<>();
        List<Channel> recommendChannelList1 = new ArrayList<>();


        for (int i = 0; i < myChannel.size(); i++) {
            String aMyChannel = myChannel.get(i);
            myChannelList.add(new Channel(aMyChannel, (Object) i));
        }

        for (String aMyChannel : recommendChannel1) {
            recommendChannelList1.add(new Channel(aMyChannel));
        }

        data.put("我的频道", myChannelList);
        data.put("推荐频道", recommendChannelList1);

        channelView.setChannelFixedCount(1);
        channelView.setStyleAdapter(new MyAdapter());
        channelView.setOnChannelListener(new ChannelListenerAdapter() {
            @Override
            public void channelItemClick(int position, Channel channel) {
                if(channelView.isChange())
                    saveStatus();
            }
            @Override
            public void channelEditStateItemClick(int position, Channel channel) {
                if(channelView.isChange())
                    saveStatus();
            }
            @Override
            public void channelEditFinish(List<Channel> channelList) {
                if(channelView.isChange())
                    saveStatus();
            }

        });
    }

    private void saveStatus(){
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        List<Channel> channels = channelView.getMyChannel();
        if (channels.size()==1){
            editor.putInt(TAB_NEWS_STATUS,TAB_PRESERVED);
            editor.putInt(TAB_PAPER_STATUS,TAB_PRESERVED);
        }else if (channels.size()==2){
            if (channels.get(1).getChannelName().equals("新闻")){
                editor.putInt(TAB_NEWS_STATUS, TAB_SELECTED_FIRST);
                editor.putInt(TAB_PAPER_STATUS,TAB_PRESERVED);
            }else if(channels.get(1).getChannelName().equals("论文")){
                editor.putInt(TAB_PAPER_STATUS, TAB_SELECTED_FIRST);
                editor.putInt(TAB_NEWS_STATUS,TAB_PRESERVED);
            }
        }else if(channels.size()==3){
            if (channels.get(1).getChannelName().equals("新闻")){
                editor.putInt(TAB_NEWS_STATUS, TAB_SELECTED_FIRST);
                editor.putInt(TAB_PAPER_STATUS, TAB_SELECTED_SECOND);
            }else if(channels.get(1).getChannelName().equals("论文")){
                editor.putInt(TAB_PAPER_STATUS, TAB_SELECTED_FIRST);
                editor.putInt(TAB_NEWS_STATUS, TAB_SELECTED_SECOND);
            }
        }
        editor.commit();
    }

    class MyAdapter extends BaseStyleAdapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder createStyleView(ViewGroup parent, String channelName) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_custom_channel, null);
            MyViewHolder customViewHolder = new MyViewHolder(inflate);
            customViewHolder.tv.setText(channelName);
            return customViewHolder;
        }

        @Override
        public LinkedHashMap<String, List<Channel>> getChannelData() {
            return data;
        }

        @Override
        public void setNormalStyle(MyViewHolder viewHolder) {
            viewHolder.tv.setBackgroundResource(R.drawable.bg_channel_custom_normal);
            viewHolder.iv.setVisibility(View.INVISIBLE);
        }

        @Override
        public void setFixedStyle(MyViewHolder viewHolder) {
            viewHolder.tv.setTextColor(getResources().getColor(R.color.colorPrimary));
            viewHolder.tv.setBackgroundResource(R.drawable.bg_channel_custom_fixed);
        }

        @Override
        public void setEditStyle(MyViewHolder viewHolder) {
            viewHolder.tv.setBackgroundResource(R.drawable.bg_channel_custom_edit);
            viewHolder.iv.setVisibility(View.VISIBLE);
        }

        @Override
        public void setFocusedStyle(MyViewHolder viewHolder) {
            viewHolder.tv.setBackgroundResource(R.drawable.bg_channel_custom_focused);
        }

        class MyViewHolder extends ViewHolder {
            TextView tv;
            ImageView iv;

            public MyViewHolder(View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.tv_channel);
                iv = itemView.findViewById(R.id.iv_delete);
            }
        }
    }
}

