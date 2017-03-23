package de.grafelhaft.winespray.app.adapter.realm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.adapter.ARealmRecyclerViewAdapter;
import de.grafelhaft.winespray.app.viewholder.ParcelViewHolder;
import de.grafelhaft.winespray.model.Parcel;
import io.realm.OrderedRealmCollection;
import io.realm.Sort;

/**
 * Created by Markus on 15.09.2016.
 */
public class ParcelRealmRecyclerViewAdapter extends ARealmRecyclerViewAdapter<Parcel, ParcelViewHolder> {

    public ParcelRealmRecyclerViewAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Parcel> data, boolean autoUpdate) {
        super(context, data.sort("numerator", Sort.ASCENDING), autoUpdate);
    }

    @Override
    public ParcelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_parcel, parent, false);
        return new ParcelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ParcelViewHolder holder, int position) {
        final Parcel parcel = getData().get(position);
        holder.bind(parcel);
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
