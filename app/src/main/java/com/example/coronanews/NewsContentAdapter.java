package com.example.coronanews;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_footer, parent, false);
            return new FooterViewHolder(view);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView newsTitle, newsBody, newsSource;
        public LinearLayout newsItem;
        public ItemViewHolder(View view) {
            super(view);
            newsTitle = view.findViewById(R.id.news_SP_title);
            newsBody = view.findViewById(R.id.newsBody);
            newsSource = view.findViewById(R.id.newsSource);
            newsItem = view.findViewById(R.id.newsItem);
        }
    }

    //增加一项，作为footer
    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        ProgressBar loading;
        LinearLayout linearLayout;

        public FooterViewHolder(View view) {
            super(view);
            loading = view.findViewById(R.id.pb_loading);
            textView = view.findViewById(R.id.tv_loading);
            linearLayout = view.findViewById(R.id.ll_end);
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
    public void setItem (final ItemViewHolder viewHolder, final int position){

        final Handler handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (dataSet.get(position).isHasRead()){
                    viewHolder.newsTitle.setTextColor(viewHolder.newsTitle.getResources().getColor(R.color.colorHasRead));
                    viewHolder.newsBody.setTextColor(viewHolder.newsTitle.getResources().getColor(R.color.colorHasRead));
                    viewHolder.newsSource.setTextColor(viewHolder.newsTitle.getResources().getColor(R.color.colorHasRead));
                } else {
                    viewHolder.newsTitle.setTextColor(viewHolder.newsTitle.getResources().getColor(R.color.colorUnReadTitle));
                    viewHolder.newsBody.setTextColor(viewHolder.newsTitle.getResources().getColor(R.color.colorUnReadBody));
                    viewHolder.newsSource.setTextColor(viewHolder.newsTitle.getResources().getColor(R.color.colorUnReadSource));
                }
            }
        };

        final NewsDao newsDao = NewsContent.getNewsDatabase().newsDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                dataSet.set(position, newsDao.findById(dataSet.get(position).getId()));
                handler.sendEmptyMessage(0);
            }
        }).start();

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

        /***
         * 设置新闻条目的点击事件
         */
        viewHolder.newsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),NewsSinglePageActivity.class);
                intent.putExtra("ID",dataSet.get(position).getId());

                dataSet.get(position).setHasRead(true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        newsDao.update(dataSet.get(position));
                    }
                }).start();
                notifyItemChanged(position);
                view.getContext().startActivity(intent);
            }
        });
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
