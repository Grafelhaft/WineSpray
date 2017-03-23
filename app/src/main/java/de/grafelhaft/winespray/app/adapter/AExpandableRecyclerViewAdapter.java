package de.grafelhaft.winespray.app.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemClickListener;
import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemLongClickListener;

/**
 * Created by @author Markus Graf (Grafelhaft) on 14.10.2016.
 */

public abstract class AExpandableRecyclerViewAdapter<P extends Parent<C>, C, PHolder extends ParentViewHolder, CHolder extends ChildViewHolder> extends ExpandableRecyclerAdapter<P, C, PHolder, CHolder> {

    private OnRecyclerViewItemClickListener _itemClickListener;
    private OnRecyclerViewItemLongClickListener _itemLongClickListener;

    /**
     * Primary constructor. Sets up {@link #mParentItemList} and {@link #mItemList}.
     * <p>
     * Changes to {@link #mParentItemList} should be made through add/remove methods in
     * {@link ExpandableRecyclerAdapter}
     *
     * @param parentItemList List of all {@link ParentListItem} objects to be
     *                       displayed in the RecyclerView that this
     *                       adapter is linked to
     */
    public AExpandableRecyclerViewAdapter(@NonNull List parentItemList) {
        super(parentItemList);
    }


    public AExpandableRecyclerViewAdapter setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener l) {
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


    public AExpandableRecyclerViewAdapter setOnRecyclerViewItemLongClickListener(OnRecyclerViewItemLongClickListener l) {
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
