package com.example.coronanews;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsContentAdapter extends RecyclerView.Adapter<NewsContentAdapter.MyViewHolder> {
    String[] dataSet;
    NewsContentAdapter(String[] dataSet){
        this.dataSet = dataSet;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public MyViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

    @NonNull
    @Override
    public NewsContentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView view = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.newscontenttest,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(dataSet[0]);
    }

    @Override
    public int getItemCount() {
        return 100;
    }
}
