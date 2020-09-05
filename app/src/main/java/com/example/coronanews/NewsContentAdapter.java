package com.example.coronanews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<String> dataSet;
    private final int TYPE_ITEM = 1, TYPE_FOOTER = 2;
    public final int LOADING = 1, LOADING_COMPLETE = 2, LOADING_END = 3;
    private int loadState;

    NewsContentAdapter(List<String> dataSet) {
        this.dataSet = dataSet;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ItemViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.contentTest);
        }
    }

    //增加一项，作为footer
    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        ProgressBar loading;
        LinearLayout linearLayout;

        public FooterViewHolder(View view) {
            super(view);
            loading = (ProgressBar) view.findViewById(R.id.pb_loading);
            textView = (TextView) view.findViewById(R.id.tv_loading);
            linearLayout = (LinearLayout) view.findViewById(R.id.ll_end);
        }
    }

    //判断是否为最后一项
    @Override
    public int getItemViewType(int position) {
        return position + 1 == getItemCount() ? TYPE_FOOTER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            TextView view = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.contenttest, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_footer, parent, false);
            return new FooterViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.textView.setText(dataSet.get(position));

        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder viewHolder = (FooterViewHolder) holder;
            switch (loadState) {
                case LOADING:
                    viewHolder.loading.setVisibility(View.VISIBLE);
                    viewHolder.textView.setVisibility(View.VISIBLE);
                    viewHolder.linearLayout.setVisibility(View.GONE);
                    break;
                case LOADING_COMPLETE:
                    viewHolder.loading.setVisibility(View.INVISIBLE);
                    viewHolder.textView.setVisibility(View.INVISIBLE);
                    viewHolder.linearLayout.setVisibility(View.GONE);
                    break;
                case LOADING_END:
                    viewHolder.loading.setVisibility(View.GONE);
                    viewHolder.textView.setVisibility(View.GONE);
                    viewHolder.linearLayout.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size() + 1;
    }

    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
    }
}
