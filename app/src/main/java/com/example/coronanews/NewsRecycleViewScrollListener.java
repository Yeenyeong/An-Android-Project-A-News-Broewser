package com.example.coronanews;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class NewsRecycleViewScrollListener extends RecyclerView.OnScrollListener {
    private boolean isSlidingUp = false;

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            int itemCount = manager.getItemCount();

            int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
            if (lastItemPosition == (itemCount - 1) && isSlidingUp) {
                onLoadMore();
            }

//            int firstItemPosition = manager.findFirstCompletelyVisibleItemPosition();
//            if(firstItemPosition==0 && !isSlidingUp){
//                onRefreshData();
//            }

        }
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        isSlidingUp = dy > 0;
    }

    public abstract void onLoadMore();
//
//    public abstract void onRefreshData();
}
