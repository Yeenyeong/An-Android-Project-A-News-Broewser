package com.example.coronanews;

import android.os.Bundle;
import android.util.Log;
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
        String[] myChannel = {"聚类", "新闻", "论文"};
        String[] recommendChannel1 = {};


        List<Channel> myChannelList = new ArrayList<>();
        List<Channel> recommendChannelList1 = new ArrayList<>();


        for (int i = 0; i < myChannel.length; i++) {
            String aMyChannel = myChannel[i];
            Channel channel;
            channel = new Channel(aMyChannel, (Object) i);
            myChannelList.add(channel);
        }

        for (String aMyChannel : recommendChannel1) {
            Channel channel = new Channel(aMyChannel);
            recommendChannelList1.add(channel);
        }


        data.put("我的频道", myChannelList);
        data.put("推荐频道", recommendChannelList1);

        channelView.setChannelFixedCount(1);
//        channelView.setInsertRecommendPosition(6);
        channelView.setStyleAdapter(new MyAdapter());
//        channelView.setOnChannelListener(new ChannelListenerAdapter() {
//            @Override
//            public void channelItemClick(int position, Channel channel) {
//                Log.i(TAG, position + ".." + channel);
//            }
//
//            @Override
//            public void channelEditStateItemClick(int position, Channel channel) {
//                Log.i(TAG + "EditState:", position + ".." + channel);
//            }
//
//            @Override
//            public void channelEditFinish(List<Channel> channelList) {
//                Log.i(TAG, channelList.toString());
//                Log.i(TAG, channelView.isChange() + "");
//                Log.i(TAG, channelView.getOtherChannel().toString());
//            }
//
//            @Override
//            public void channelEditStart() {
//                Log.i(TAG, "channelEditStart");
//            }
//        });
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

