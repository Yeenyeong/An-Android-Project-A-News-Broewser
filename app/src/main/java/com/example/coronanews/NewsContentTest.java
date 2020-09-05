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

public class NewsContentTest extends Fragment {
    private String tabName;
    NewsContentTest(String t){this.tabName = t;}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_recycle_view, container, false);
        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.newsSwipeFresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /***
                 *  新线程模拟一下加载的过程
                 */
                class Tmp extends Thread{
                    SwipeRefreshLayout s;
                    Tmp(SwipeRefreshLayout s) {this.s=s;}
                    public void run(){
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
        String[] dataSet = new String[]{tabName};

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.newsRecycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        NewsContentAdapter adapter = new NewsContentAdapter(dataSet);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
