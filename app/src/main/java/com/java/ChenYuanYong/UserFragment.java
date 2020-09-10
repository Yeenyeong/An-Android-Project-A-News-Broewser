package com.java.ChenYuanYong;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        Button history = view.findViewById(R.id.user_history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),HistoryActivity.class);
                intent.putExtra("USER_FRAGMENT","HISTORY");
                startActivity(intent);
            }
        });

        ImageView img_history = view.findViewById(R.id.img_history);
        img_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),HistoryActivity.class);
                intent.putExtra("USER_FRAGMENT","HISTORY");
                startActivity(intent);
            }
        });

        Button cache = view.findViewById(R.id.user_cache);
        cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),HistoryActivity.class);
                intent.putExtra("USER_FRAGMENT","CACHE");
                startActivity(intent);
            }
        });

        ImageView img_cache = view.findViewById(R.id.img_cache);
        img_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),HistoryActivity.class);
                intent.putExtra("USER_FRAGMENT","CACHE");
                startActivity(intent);
            }
        });

        Button quit = view.findViewById(R.id.user_quit);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getActivity().finish();
            }
        });

        ImageView img_quit = view.findViewById(R.id.img_quit);
        img_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        return view;
    }
}
