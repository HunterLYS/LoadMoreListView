package com.iqusong.qslistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;


/**
 * Created by cary on 15/4/2.
 */
public class LoadMoreListView extends ListView {

    public interface OnLoadMoreListener {
        void onLoadingMore();
        void onScrollFirstItem(boolean isFirst);
    }
    public OnLoadMoreListener onLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener listener){
        this.onLoadMoreListener = listener;
    }

    private View mFooterView;
    public View mHeadView;

    public LoadMoreListView(Context context) {
        super(context);
        init();
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnScrollListener(new OnScrollListener() {

            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;


                if (onLoadMoreListener != null) {
                    if (firstVisibleItem == 0 && isScrollAtTop()) {
                        onLoadMoreListener.onScrollFirstItem(true);

                    } else {
                        onLoadMoreListener.onScrollFirstItem(false);
                    }
                }
            }

            private void isScrollCompleted() {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                        && this.currentScrollState == SCROLL_STATE_IDLE) {
                    if (null != onLoadMoreListener) {
                        onLoadMoreListener.onLoadingMore();
                    }
                }
            }
        });
    }

    public void showFooterLoadingView() {
        if (null == mFooterView) {
            mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.list_loading_view, null);
            addFooterView(mFooterView);

        }

        TextView textViewLoading = (TextView) mFooterView.findViewById(R.id.text_loading);
        textViewLoading.setText(getResources().getString(R.string.load_more_loading_text));
        mFooterView.setVisibility(View.VISIBLE);
    }

    public void showFooterFinishedView() {
        if (null == mFooterView) {
            mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.list_loading_view, null);
            addFooterView(mFooterView);
        }

        TextView textViewLoading = (TextView) mFooterView.findViewById(R.id.text_loading);
        textViewLoading.setText(getResources().getString(R.string.load_more_finish_text));
        mFooterView.setVisibility(View.VISIBLE);
    }


    public void hideFooterView() {
        if (mFooterView==null) return;
        mFooterView.setVisibility(View.GONE);
    }

    public void setFooterText(String text) {
        if (null == mFooterView) {
            mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.list_loading_view, null);
            addFooterView(mFooterView);
        }

        TextView textView = (TextView) mFooterView.findViewById(R.id.text_loading);
        textView.setText(text);
        mFooterView.setVisibility(View.VISIBLE);
    }


    public void setHeadView(View view) {
        this.mHeadView = view;
        if (mHeadView == null) return;
        addHeaderView(mHeadView);
    }

    public void showHeadView() {
        if (mHeadView == null) {
            return;
        }

        mHeadView.setVisibility(View.VISIBLE);
        mHeadView.setPadding(0, 0, 0, 0);
    }

    public void hideHeadView() {
        if (mHeadView == null) return;
        mHeadView.setVisibility(View.GONE);
        int mHeight = -mHeadView.getHeight();
        mHeadView.setPadding(0, mHeight, 0, 0);
    }

    public void hideHeadView(int height) {
        if (mHeadView == null) return;
        mHeadView.setVisibility(View.GONE);
        int mHeight = -mHeadView.getHeight();
        if (mHeight == 0) {
            mHeight = -height;
        }
        mHeadView.setPadding(0, mHeight, 0, 0);
    }

    public boolean isScrollAtTop() {
        View firstChild = getChildAt(0);
        if (firstChild != null) {
            if (firstChild.getTop() == 0) {
                return true;
            }
        }
        return false;
    }

}
