package com.example.coronanews;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news, container, false);
        String[] titles = new String[]{"聚类","新闻","论文"};
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.newsTab);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.newsViewPager);
        ArrayList<Fragment> fragments = new ArrayList<>();
        FmPagerAdapter pagerAdapter = new FmPagerAdapter(fragments, getSupportFragmentManager());

        for (int i = 0; i < titles.length; i++) {
            fragments.add(new NewsContentTest(titles[i]));
            tabLayout.addTab(tabLayout.newTab());
        }
        tabLayout.setupWithViewPager(viewPager, false);
        viewPager.setAdapter(pagerAdapter);

        for (int i = 0; i < titles.length; i++) {
            tabLayout.getTabAt(i).setText(titles[i]);
        }

        final Context context = getContext();

        Button button = (Button) view.findViewById(R.id.tabBarButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"button",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private FragmentManager getSupportFragmentManager() {
        return getFragmentManager();
    }
}
