package com.example.coronanews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /***
         * 试一下TabLayout的feature,之后删掉.
         */
        init();
    }

    public void init(){
        String[] titles = new String[]{"聚类","新闻","论文"};
        TabLayout tabLayout = (TabLayout) findViewById(R.id.newsTab);
        ViewPager viewPager = (ViewPager) findViewById(R.id.newsViewPager);
        ArrayList<Fragment> fragments = new ArrayList<>();
        FmPagerAdapter pagerAdapter = new FmPagerAdapter(fragments, getSupportFragmentManager());

        for (int i = 0; i < titles.length; i++) {
            fragments.add(new NewsFragmentTest(titles[i]));
            tabLayout.addTab(tabLayout.newTab());
        }
        tabLayout.setupWithViewPager(viewPager, false);
        viewPager.setAdapter(pagerAdapter);

        for (int i = 0; i < titles.length; i++) {
            tabLayout.getTabAt(i).setText(titles[i]);
        }

    }
}