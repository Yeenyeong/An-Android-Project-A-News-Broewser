package com.example.coronanews;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class StatisticFragment extends Fragment {
    String[] titles = new String[]{"国内", "国际"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_statistic, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.n_s_toolBar);
        toolbar.setTitle(R.string.statistic);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.n_s_Tab);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.n_s_ViewPager);
        ArrayList<Fragment> fragments = new ArrayList<>();
        StatisticPagerAdapter pagerAdapter = new StatisticPagerAdapter(fragments, getSupportFragmentManager());

        for (String title : titles) {
            Fragment fragment = new StatisticContentTest(title);
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

        Button button = (Button) view.findViewById(R.id.n_s_tabButton);
        button.setVisibility(View.GONE);

        return view;
    }

    private FragmentManager getSupportFragmentManager() {
        return getFragmentManager();
    }
}
