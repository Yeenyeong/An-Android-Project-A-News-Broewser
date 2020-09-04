package com.example.coronanews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NewsFragmentTest extends Fragment {
    private String tabName;
    NewsFragmentTest(String t){this.tabName = t;}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newstest, container, false);
        TextView text = (TextView) view.findViewById(R.id.testNews);
        text.setText(tabName);
        return view;
    }
}
