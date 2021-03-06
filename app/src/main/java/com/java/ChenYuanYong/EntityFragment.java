package com.java.ChenYuanYong;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class EntityFragment extends Fragment {

    private EntityHomeFragment home_frag;
    private EntitySearchBackFragment search_back_frag;

    private ArrayList<Entity> entity_list;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            entity_list.clear();
            entity_list.addAll((ArrayList<Entity>)msg.obj);
            FragmentTransaction frag_trans = getChildFragmentManager().beginTransaction();
            search_back_frag.setEntity_list(entity_list);
            frag_trans.hide(home_frag).show(search_back_frag);
            frag_trans.addToBackStack(null);
            frag_trans.commit();

        }
    };


    public EntityFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entity, container, false);

        SearchView searchView = (SearchView) view.findViewById(R.id.entity_search_view);
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconified(false);
        try {
            Class<?> argClass = searchView.getClass();
            Field ownField = argClass.getDeclaredField("mSearchPlate");
            Field ownField2 = argClass.getDeclaredField("mSubmitArea");
            ownField.setAccessible(true);
            ownField2.setAccessible(true);
            View mView = (View) ownField.get(searchView);
            mView.setBackgroundColor(Color.TRANSPARENT);
            View mView2 = (View) ownField2.get(searchView);
            mView2.setBackgroundColor(Color.TRANSPARENT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                new EntityNetwork(handler, s).start();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")){
                    FragmentTransaction frag_trans = getChildFragmentManager().beginTransaction();
                    frag_trans.hide(search_back_frag).show(home_frag);
                    frag_trans.addToBackStack(null);
                    frag_trans.commit();
                }
                return false;
            }
        });

        home_frag = EntityHomeFragment.newInstance("EntityHome", "");
        search_back_frag = EntitySearchBackFragment.newInstance("SearchBack", "");
        entity_list = new ArrayList<>();

        FragmentTransaction frag_trans = getChildFragmentManager().beginTransaction();
        frag_trans = getChildFragmentManager().beginTransaction();
        frag_trans.add(R.id.entity_container, home_frag).add(R.id.entity_container, search_back_frag).hide(search_back_frag).show(home_frag);
        frag_trans.addToBackStack(null);
        frag_trans.commit();

        return view;
    }
}
