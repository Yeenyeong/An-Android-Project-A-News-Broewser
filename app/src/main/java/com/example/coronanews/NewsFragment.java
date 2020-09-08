package com.example.coronanews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    public static final int TAB_SELECTED_FIRST = 1, TAB_SELECTED_SECOND = 2, TAB_PRESERVED = 0;
    public static final String TAB_NEWS_STATUS = "TAB_NEWS_STATUS", TAB_PAPER_STATUS = "TAB_PAPER_STATUS";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_statistic, container, false);

        tabLayout = view.findViewById(R.id.n_s_Tab);

        viewPager = view.findViewById(R.id.n_s_ViewPager);
        ArrayList<Fragment> fragments = new ArrayList<>();
        pagerAdapter = new NewsPagerAdapter(fragments, getFragmentManager());

        clusterTabFragment = new Fragment();
        newsTabFragment = new NewsContent("新闻");
        paperTabFragment = new NewsContent("论文");
        fragments.add(clusterTabFragment);
        fragments.add(newsTabFragment);
        fragments.add(paperTabFragment);

        for (int i = 0; i < 3; i++) {
            //关于此处和PagerAdapter使用hashcode,参照：https://blog.csdn.net/ZuoLiangZuoXiaoQi/article/details/78255679
            viewPager.setId(fragments.get(i).hashCode());
            tabLayout.addTab(tabLayout.newTab());
        }
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager, false);
        String[] titles = new String[]{"聚类", "新闻", "论文"};
        //设置标题要在最后
        for (int i = 0; i < 3; i++) {
            tabLayout.getTabAt(i).setText(titles[i]);
        }

        Button button = view.findViewById(R.id.n_s_tabButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CustomChannelActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        TextView searchBar = view.findViewById(R.id.search_bar);
        searchBar.setText("搜索新闻  |  论文");
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(TAB_NEWS_STATUS, TAB_SELECTED_FIRST);
        editor.putInt(TAB_PAPER_STATUS, TAB_SELECTED_SECOND);
        editor.commit();
        return view;
    }

    class NewsPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;

        public NewsPagerAdapter(List<Fragment> fragmentList, FragmentManager fm) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            int tabNewsStatus = sharedPref.getInt(TAB_NEWS_STATUS, TAB_SELECTED_FIRST);
            int tabPaperStatus = sharedPref.getInt(TAB_PAPER_STATUS, TAB_SELECTED_SECOND);
            System.out.println(tabNewsStatus + "   " + tabPaperStatus);
            tabLayout.removeAllTabs();

            ArrayList<Fragment> fragments = new ArrayList<>();
            fragments.add(clusterTabFragment);

            List<String> titles = new ArrayList<>();
            titles.add("聚类");

            if (tabNewsStatus == TAB_SELECTED_FIRST) {
                fragments.add(newsTabFragment);
                titles.add("新闻");
            } else if (tabPaperStatus == TAB_SELECTED_FIRST) {
                fragments.add(paperTabFragment);
                titles.add("论文");
            }
            if (tabNewsStatus == TAB_SELECTED_SECOND) {
                fragments.add(newsTabFragment);
                titles.add("新闻");
            } else if (tabPaperStatus == TAB_SELECTED_SECOND) {
                fragments.add(paperTabFragment);
                titles.add("论文");
            }

            for (int i = 0; i < titles.size(); i++) {
                //关于此处和PagerAdapter使用hashcode,参照：https://blog.csdn.net/ZuoLiangZuoXiaoQi/article/details/78255679
                viewPager.setId(fragments.get(i).hashCode());
                tabLayout.addTab(tabLayout.newTab());
            }

            pagerAdapter = new NewsPagerAdapter(fragments, getFragmentManager());
            tabLayout.setupWithViewPager(viewPager, true);
            for (int i = 0; i < titles.size(); i++) {
                tabLayout.getTabAt(i).setText(titles.get(i));
            }

        }
    }
}