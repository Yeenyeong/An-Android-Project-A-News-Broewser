package com.example.coronanews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    Fragment newsFragment = new NewsFragment();
    Fragment statisticFragment = new StatisticFragment();
    Fragment entityFragment = new EntityFragment();
    Fragment scholarFragment = new ScholarFragment();
    Fragment userFragment = new UserFragment();

    public void init(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, newsFragment).add(R.id.container,statisticFragment).add(R.id.container, entityFragment)
        .add(R.id.container, scholarFragment).add(R.id.container, userFragment);
        transaction.commit();
        showFragment(R.id.menu_home);
        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.bottomPanel);
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

}