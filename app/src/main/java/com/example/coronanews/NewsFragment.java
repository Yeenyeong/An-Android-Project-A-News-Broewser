package com.example.coronanews;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class NewsFragment extends Fragment {
    String[] titles = new String[]{"聚类","新闻","论文"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_statistic, container, false);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.n_s_Tab);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.n_s_ViewPager);
        ArrayList<Fragment> fragments = new ArrayList<>();
        NewsPagerAdapter pagerAdapter = new NewsPagerAdapter(fragments, getSupportFragmentManager());

        TextView editText = (TextView) view.findViewById(R.id.search_bar);


        for (String title : titles) {
            Fragment fragment = new NewsContentTest();
            fragments.add(fragment);
            //关于此处和PagerAdapter使用hashcode,参照：https://blog.csdn.net/ZuoLiangZuoXiaoQi/article/details/78255679
            viewPager.setId(fragment.hashCode());
            tabLayout.addTab(tabLayout.newTab());
        }
        tabLayout.setupWithViewPager(viewPager, false);
        viewPager.setAdapter(pagerAdapter);

        for (int i = 0; i < titles.length; i++) {
            tabLayout.getTabAt(i).setText(titles[i]);
        }

        final Context context = getContext();
        Button button = (Button) view.findViewById(R.id.n_s_tabButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CustomChannelActivity.class);
                startActivity(intent);
            }
        });

        TextView searchBar = (TextView) view.findViewById(R.id.search_bar);
        searchBar.setText("搜索新闻  |  论文");
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private FragmentManager getSupportFragmentManager() {
        return getFragmentManager();
    }
}
