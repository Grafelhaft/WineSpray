package de.grafelhaft.winespray.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import de.grafelhaft.grafellib.adapter.ISelectable;
import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemSelectedListener;
import io.realm.OrderedRealmCollection;
import io.realm.RealmModel;

/**
 * Created by Markus on 16.09.2016.
 */
public abstract class ASelectableRealmRecyclerViewAdapter<T extends RealmModel, V extends RecyclerView.ViewHolder> extends ARealmRecyclerViewAdapter<T, V> implements ISelectable, OnRecyclerViewItemSelectedListener {

    protected SparseBooleanArray selectedItems;
    private ArrayList<OnRecyclerViewItemSelectedListener> _listeners = new ArrayList<>();

    public ASelectableRealmRecyclerViewAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<T> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
    }

    @Override
    public void updateData(@Nullable OrderedRealmCollection<T> data) {
        super.updateData(data);
    }

    @Override
    public void onItemSelected(int position, boolean selected) {
        for (OnRecyclerViewItemSelectedListener l : _listeners) {
            l.onItemSelected(position, selected);
        }
    }

    public ASelectableRealmRecyclerViewAdapter addOnRecyclerViewItemSelectedListener(OnRecyclerViewItemSelectedListener l) {
        _listeners.add(l);
        return this;
    }

    public void removeOnRecyclerViewItemSelectedListener(OnRecyclerViewItemSelectedListener l) {
        _listeners.remove(l);
    }

    public void clearOnRecyclerViewItemSelectedListener() {
        _listeners.clear();
    }

    @Override
    public void toggleSelection(View view, int position) {
        if(this.selectedItems.get(position, false)) {
            this.selectedItems.delete(position);
            this.onItemSelected(position, false);
        } else {
            this.selectedItems.put(position, true);
            this.onItemSelected(position, true);
        }

        this.notifyItemChanged(position);
    }

    @Override
    public void selectAll() {
        for(int i = 0; i < this.getData().size(); ++i) {
            this.selectedItems.put(i, true);
        }

        this.notifyDataSetChanged();
    }

    @Override
    public void clearSelection() {
        this.selectedItems.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public int getSelectedItemCount() {
        return this.selectedItems.size();
    }

    public List<T> getSelectedItems() {
        List<T> items = new ArrayList<>();
        for (int i = 0; i < selectedItems.size(); i++) {
            if (selectedItems.get(i, false)) {
                items.add(getItem(i));
            }
        }
        return items;
    }
}
