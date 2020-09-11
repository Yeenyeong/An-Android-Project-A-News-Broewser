package com.java.ChenYuanYong;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    NewsPagerAdapter pagerAdapter;
    Fragment newsTabFragment, paperTabFragment, clusterTabFragment;
    ArrayList<Fragment> fragmentList = new ArrayList<>();
    public static final int TAB_SELECTED_FIRST = 1, TAB_SELECTED_SECOND = 2, TAB_PRESERVED = 0;
    public static final String TAB_NEWS_STATUS = "TAB_NEWS_STATUS", TAB_PAPER_STATUS = "TAB_PAPER_STATUS";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        tabLayout = view.findViewById(R.id.news_Tab);

        viewPager = view.findViewById(R.id.news_ViewPager);
        pagerAdapter = new NewsPagerAdapter(getFragmentManager());

        clusterTabFragment = new ClusterContent();
        newsTabFragment = new NewsContent("新闻");
        paperTabFragment = new NewsContent("论文");

        //关于此处和PagerAdapter使用hashcode,参照：https://blog.csdn.net/ZuoLiangZuoXiaoQi/article/details/78255679
        viewPager.setId(newsTabFragment.hashCode());
        viewPager.setId(paperTabFragment.hashCode());
        viewPager.setId(clusterTabFragment.hashCode());

        setUpTabs();

        ImageView button = view.findViewById(R.id.news_tabButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CustomChannelActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        TextView searchBar = view.findViewById(R.id.news_search_bar);
        searchBar.setText("搜索新闻  |  论文");
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewsSearchActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setUpTabs();
    }

    class NewsPagerAdapter extends FragmentPagerAdapter {

        public NewsPagerAdapter(FragmentManager fm) {
            super(fm);
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

    private void setUpTabs() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int tabNewsStatus = sharedPref.getInt(TAB_NEWS_STATUS, TAB_SELECTED_FIRST);
        int tabPaperStatus = sharedPref.getInt(TAB_PAPER_STATUS, TAB_SELECTED_SECOND);
        tabLayout.removeAllTabs();

        int current = viewPager.getCurrentItem();

        fragmentList.clear();
        fragmentList.add(clusterTabFragment);

        List<String> titles = new ArrayList<>();
        titles.add("聚类");
        if (tabNewsStatus == TAB_PRESERVED){
            if(tabPaperStatus == TAB_SELECTED_FIRST){
                titles.add("论文");
                fragmentList.add(paperTabFragment);
            }
        }
        else if (tabNewsStatus == TAB_SELECTED_FIRST) {
            titles.add("新闻");
            fragmentList.add(newsTabFragment);
            if (tabPaperStatus == TAB_SELECTED_SECOND){
                fragmentList.add(paperTabFragment);
                titles.add("论文");
            }
        } else if (tabNewsStatus == TAB_SELECTED_SECOND) {
            titles.add("论文");
            titles.add("新闻");
            fragmentList.add(paperTabFragment);
            fragmentList.add(newsTabFragment);
        }

        viewPager.setAdapter(pagerAdapter);
        tabLayout.removeAllTabs();
        tabLayout.setupWithViewPager(viewPager, false);
        for (int i = 0; i < titles.size(); i++) {
            tabLayout.getTabAt(i).setText(titles.get(i));
        }

        if (fragmentList.size()>current){
            viewPager.setCurrentItem(current);
        }
    }

}