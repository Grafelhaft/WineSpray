package de.grafelhaft.winespray.app.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import de.grafelhaft.grafellib.adapter.ARecyclerViewAdapter;

/**
 * Created by Markus on 11.09.2016.
 */
public abstract class ALazyRecyclerViewAdapter<Type, ViewHolder extends RecyclerView.ViewHolder> extends ARecyclerViewAdapter<Type, ViewHolder> {

    private boolean _isLoading;

    private int _visibleThreshold = 5;

    private int _lastVisibleItem;
    private int _totalItemCount;

    private OnLoadMoreListener _onLoadMoreListener;

    public ALazyRecyclerViewAdapter(RecyclerView recyclerView) {
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                _totalItemCount = linearLayoutManager.getItemCount();
                _lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!_isLoading && _totalItemCount <= (_lastVisibleItem + _visibleThreshold)) {
                    if (_onLoadMoreListener != null) {
                        _onLoadMoreListener.onLoadMore();
                    }
                    _isLoading = true;
                }
            }
        });
    }

    public void onLoadedFinished() {
        this._isLoading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener l) {
        this._onLoadMoreListener = l;
    }

    public void removeOnLoadMoreListener() {
        this._onLoadMoreListener = null;
    }

}
