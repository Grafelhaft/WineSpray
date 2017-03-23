package de.grafelhaft.winespray.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.grafelhaft.grafellib.adapter.ARecyclerViewAdapter;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.viewholder.AcreViewHolder;
import de.grafelhaft.winespray.model.Acre;
import io.realm.OrderedRealmCollection;
import io.realm.Sort;

/**
 * Created by Markus on 15.09.2016.
 */
public class AcreRecyclerViewAdapter extends ARecyclerViewAdapter<Acre, AcreViewHolder> {

    public AcreRecyclerViewAdapter(List<Acre> acres) {
        if (acres != null) {
            getData().addAll(acres);
        }
    }

    @Override
    public AcreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_acre, parent, false);
        return new AcreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AcreViewHolder holder, int position) {
        final Acre acre = getData().get(position);
        holder.bind(acre);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemClicked(v, holder.getAdapterPosition());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return notifyItemLongClicked(view, holder.getAdapterPosition());
            }
        });
    }

    public void update(List<Acre> acres) {
        getData().clear();
        getData().addAll(acres);
        notifyDataSetChanged();
    }
}
