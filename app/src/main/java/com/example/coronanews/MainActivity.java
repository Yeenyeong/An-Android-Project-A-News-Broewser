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

    public void init(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, newsFragment).add(R.id.container,statisticFragment);
        transaction.commit();
        showFragment(R.id.menu_home);
        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.bottomPanel);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.menu_home){
                    showFragment(R.id.menu_home);
                    return true;
                }else if(item.getItemId()==R.id.menu_statistic){
                    showFragment(R.id.menu_statistic);
                    return true;
                }
                return false;
            }
        });
    }

    private void showFragment(int menuId){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (menuId){
            case R.id.menu_home:
                transaction.hide(statisticFragment);
                transaction.show(newsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.menu_statistic:
                transaction.hide(newsFragment);
                transaction.show(statisticFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.menu_entry:
                break;
            case R.id.menu_scholar:
                break;
            case R.id.menu_user:
                break;
        }
    }

}