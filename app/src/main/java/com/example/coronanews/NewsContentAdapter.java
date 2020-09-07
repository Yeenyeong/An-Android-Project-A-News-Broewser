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
    List<News> dataSet;
    private final int TYPE_ITEM = 1, TYPE_FOOTER = 2;
    public final int LOADING = 1, LOADING_COMPLETE = 2, LOADING_END = 3;
    private int loadState;

    NewsContentAdapter(List<News> dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_content, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_footer, parent, false);
            return new FooterViewHolder(view);
        }
        return null;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView newsTitle, newsBody, newsSource;
        public ItemViewHolder(View view) {
            super(view);
            newsTitle = view.findViewById(R.id.newsTitle);
            newsBody = view.findViewById(R.id.newsBody);
            newsSource = view.findViewById(R.id.newsSource);
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

    /***
     * 在这里设置新闻内容
     */
    public void setItem (ItemViewHolder viewHolder, int position){

        //设置标题
        viewHolder.newsTitle.setText(dataSet.get(position).getTitle());
        String body = dataSet.get(position).getBody();
        if (body.length()>60) {
            body = body.substring(0, 60) + "...";//设置正文最大预览长度为60
        }

        //设置正文
        viewHolder.newsBody.setText(body);
        if (body.length()==0)
            viewHolder.newsBody.setVisibility(View.GONE);

        //设置来源
        if (dataSet.get(position).getType().equals("news")){
            String source = dataSet.get(position).getSource();
            source = "来源:"+source;
            viewHolder.newsSource.setText(source);
        }else {
            viewHolder.newsSource.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            setItem((ItemViewHolder) holder, position);

        } else if (holder instanceof FooterViewHolder) {//设置正在加载
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
