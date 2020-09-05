package com.example.coronanews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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

    Fragment fragment = new NewsFragment();
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

}