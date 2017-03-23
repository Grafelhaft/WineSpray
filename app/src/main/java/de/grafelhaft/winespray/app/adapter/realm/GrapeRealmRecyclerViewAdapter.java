package de.grafelhaft.winespray.app.adapter.realm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.adapter.ARealmRecyclerViewAdapter;
import de.grafelhaft.winespray.app.viewholder.GrapeViewHolder;
import de.grafelhaft.winespray.model.Grape;
import io.realm.OrderedRealmCollection;
import io.realm.Sort;

/**
 * Created by Markus on 15.09.2016.
 */
public class GrapeRealmRecyclerViewAdapter extends ARealmRecyclerViewAdapter<Grape, GrapeViewHolder> {

    public GrapeRealmRecyclerViewAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Grape> data, boolean autoUpdate) {
        super(context, data.sort("name", Sort.ASCENDING), autoUpdate);
    }

    @Override
    public GrapeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_grape, parent, false);
        return new GrapeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GrapeViewHolder holder, int position) {
        Grape grape = getData().get(position);
        holder.bind(grape);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemClicked(v, holder.getAdapterPosition());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return notifyItemLongClicked(v, holder.getAdapterPosition());
            }
        });
    }
}
