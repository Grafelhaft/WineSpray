package de.grafelhaft.winespray.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemClickListener;
import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemLongClickListener;
import io.realm.OrderedRealmCollection;
import io.realm.RealmModel;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Markus on 16.09.2016.
 */
public abstract class ARealmRecyclerViewAdapter<T extends RealmModel, V extends RecyclerView.ViewHolder> extends RealmRecyclerViewAdapter<T, V> {

    private OnRecyclerViewItemClickListener _itemClickListener;
    private OnRecyclerViewItemLongClickListener _itemLongClickListener;

    public ARealmRecyclerViewAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<T> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
    }

    public ARealmRecyclerViewAdapter<T, V> setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener l) {
        this._itemClickListener = l;
        return this;
    }

    public void removeOnRecyclerViewItemClickListener() {
        this._itemClickListener = null;
    }

    public void notifyItemClicked(View view, int position) {
        if(this._itemClickListener != null) {
            this._itemClickListener.onItemClicked(view, position);
        }

    }

    public ARealmRecyclerViewAdapter<T, V> setOnRecyclerViewItemLongClickListener(OnRecyclerViewItemLongClickListener l) {
        this._itemLongClickListener = l;
        return this;
    }

    public void removeOnRecyclerViewItemLongClickListener() {
        this._itemLongClickListener = null;
    }

    public boolean notifyItemLongClicked(View view, int position) {
        return this._itemLongClickListener != null && this._itemLongClickListener.onItemLongClicked(view, position);
    }
}
