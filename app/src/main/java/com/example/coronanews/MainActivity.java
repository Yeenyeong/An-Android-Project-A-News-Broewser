package com.example.coronanews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    Fragment newsFragment, statisticFragment,entityFragment,scholarFragment,userFragment;
    private final String NEWS_FRAGMENT_KEY = "SAVED_NEWS_FRAGMENT";
    private final String STATISTIC_FRAGMENT_KEY = "SAVED_STATISTIC_FRAGMENT";
    private final String ENTITY_FRAGMENT_KEY = "SAVED_ENTITY_FRAGMENT";
    private final String SCHOLAR_FRAGMENT_KEY = "SAVED_SCHOLAR_FRAGMENT";
    private final String USER_FRAGMENT_KEY = "SAVED_USER_FRAGMENT";
    private final String SELECTED_ITEM_ID = "ITEM_SELECTED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (newsFragment!=null)
            getSupportFragmentManager().putFragment(outState,NEWS_FRAGMENT_KEY,newsFragment);
        if (statisticFragment!=null)
            getSupportFragmentManager().putFragment(outState,STATISTIC_FRAGMENT_KEY,statisticFragment);
        if (entityFragment!=null)
            getSupportFragmentManager().putFragment(outState,ENTITY_FRAGMENT_KEY,entityFragment);
        if (scholarFragment!=null)
            getSupportFragmentManager().putFragment(outState,SCHOLAR_FRAGMENT_KEY,scholarFragment);
        if (userFragment!=null)
            getSupportFragmentManager().putFragment(outState,USER_FRAGMENT_KEY,userFragment);
        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.bottomPanel);
        outState.putInt(SELECTED_ITEM_ID,navigationView.getSelectedItemId());
        super.onSaveInstanceState(outState);
    }

    public void init(Bundle savedInstanceState){
        if (savedInstanceState==null){
            newsFragment = new NewsFragment();
            statisticFragment = new StatisticFragment();
            entityFragment = new EntityFragment();
            scholarFragment = new ScholarFragment();
            userFragment = new UserFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.container, newsFragment).add(R.id.container,statisticFragment).add(R.id.container, entityFragment)
                    .add(R.id.container, scholarFragment).add(R.id.container, userFragment);
            transaction.commit();
            showFragment(R.id.menu_home);
        }
        else {
            newsFragment = getSupportFragmentManager().getFragment(savedInstanceState,NEWS_FRAGMENT_KEY);
            statisticFragment = getSupportFragmentManager().getFragment(savedInstanceState,STATISTIC_FRAGMENT_KEY);
            entityFragment = getSupportFragmentManager().getFragment(savedInstanceState,ENTITY_FRAGMENT_KEY);
            scholarFragment = getSupportFragmentManager().getFragment(savedInstanceState,SCHOLAR_FRAGMENT_KEY);
            userFragment = getSupportFragmentManager().getFragment(savedInstanceState,USER_FRAGMENT_KEY);
            showFragment(savedInstanceState.getInt(SELECTED_ITEM_ID));
        }
        BottomNavigationView navigationView = findViewById(R.id.bottomPanel);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(showFragment(item.getItemId()))
                    return true;
                return false;
            }
        });
    }

    private boolean showFragment(int menuId){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (menuId){
            case R.id.menu_home:
                transaction.hide(statisticFragment).hide(entityFragment).hide(scholarFragment).hide(userFragment);
                transaction.show(newsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.menu_statistic:
                transaction.hide(newsFragment).hide(entityFragment).hide(scholarFragment).hide(userFragment);
                transaction.show(statisticFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.menu_entity:
                transaction.hide(newsFragment).hide(statisticFragment).hide(scholarFragment).hide(userFragment);
                transaction.show(entityFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.menu_scholar:
                transaction.hide(newsFragment).hide(statisticFragment).hide(entityFragment).hide(userFragment);
                transaction.show(scholarFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.menu_user:
                transaction.hide(newsFragment).hide(statisticFragment).hide(entityFragment).hide(scholarFragment);
                transaction.show(userFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            default:
                return false;
        }
        return true;
    }


    private static boolean isExit = false;  // 标识是否退出

    private static Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(this, "再按一次后退键退出程序", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 1500);  // 利用handler延迟发送更改状态信息
        } else {
            this.finish();
        }
    }

}