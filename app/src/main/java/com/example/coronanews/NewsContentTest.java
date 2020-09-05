package com.example.coronanews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NewsContentTest extends Fragment {
    private String tabName;
    private List<String> dataSet = new ArrayList<>();

    NewsContentTest(String t) {
        this.tabName = t;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.contenttest, container, false);
        View view = inflater.inflate(R.layout.news_recycle_view, container, false);
        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.newsSwipeFresh);

        //模拟获取数据
        getData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //线程模拟一下加载的过程
                class Tmp extends Thread {
                    SwipeRefreshLayout s;

                    Tmp(SwipeRefreshLayout s) {
                        this.s = s;
                    }

                    public void run() {
                        try {
                            sleep(2000);
                            s.setRefreshing(false);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Tmp tmp = new Tmp(swipeRefreshLayout);
                tmp.start();
            }
        });


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.newsRecycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        final NewsContentAdapter adapter = new NewsContentAdapter(dataSet);
        recyclerView.setAdapter(adapter);

        recyclerView.setOnScrollListener(new NewsRecycleViewScrollListener() {
            @Override
            public void onLoadMore() {
                adapter.setLoadState(adapter.LOADING);
                if (dataSet.size() < 78) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getData();
                                    adapter.setLoadState(adapter.LOADING_COMPLETE);
                                }
                            });
                        }
                    }, 1000);

                } else adapter.setLoadState(adapter.LOADING_END);
            }
        });

        return view;
    }

    /***
     *  模拟加载数据
     */
    public void getData() {
        char letter = 'A';
        for (int i = 0; i < 26; i++)
            dataSet.add(String.valueOf(letter++));
    }

}
