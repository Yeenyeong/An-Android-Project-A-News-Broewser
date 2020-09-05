package com.example.coronanews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

    Fragment fragment = new HomeFragment();
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    public void init(){
        transaction.add(R.id.container,fragment);
        transaction.commit();
        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.bottomPanel);
        navigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                if(item.getItemId()==R.layout.news){

                    transaction.show(fragment);
                    transaction.commit();
                }
            }
        });
    }


//    public void init(){
//
//        String[] titles = new String[]{"聚类","新闻","论文"};
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.newsTab);
//        final ViewPager viewPager = (ViewPager) findViewById(R.id.newsViewPager);
//        ArrayList<Fragment> fragments = new ArrayList<>();
//        FmPagerAdapter pagerAdapter = new FmPagerAdapter(fragments, getSupportFragmentManager());
//
//        for (int i = 0; i < titles.length; i++) {
//            fragments.add(new NewsFragmentTest(titles[i]));
//            tabLayout.addTab(tabLayout.newTab());
//        }
//        tabLayout.setupWithViewPager(viewPager, false);
//        viewPager.setAdapter(pagerAdapter);
//
//        for (int i = 0; i < titles.length; i++) {
//            tabLayout.getTabAt(i).setText(titles[i]);
//        }
//
//        Button button = (Button) findViewById(R.id.tabBarButton);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this,"button",Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

}