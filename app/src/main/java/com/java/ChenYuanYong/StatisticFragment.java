package com.java.ChenYuanYong;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class StatisticFragment extends Fragment {
    String[] titles = new String[]{"国内", "国际"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);

        TabLayout tabLayout = view.findViewById(R.id.statistic_Tab);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        final ViewPager viewPager = view.findViewById(R.id.statistic_ViewPager);
        final ArrayList<StatisticContent> fragments = new ArrayList<>();
        StatisticPagerAdapter pagerAdapter = new StatisticPagerAdapter(fragments, getFragmentManager());

        for (String title : titles) {
            StatisticContent fragment = new StatisticContent(title);
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

        return view;
    }
}



class StatisticPagerAdapter extends FragmentPagerAdapter {
    private List<StatisticContent> fragmentList;

    public StatisticPagerAdapter(ArrayList<StatisticContent> fragmentList, FragmentManager fm) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }
    //关于hashcode,参照：https://blog.csdn.net/ZuoLiangZuoXiaoQi/article/details/78255679
    @Override
    public long getItemId(int position) {
        return fragmentList.get(position).hashCode();
    }

    @Override
    public int getCount() {
        return fragmentList != null && !fragmentList.isEmpty() ? fragmentList.size() : 0;
    }
}