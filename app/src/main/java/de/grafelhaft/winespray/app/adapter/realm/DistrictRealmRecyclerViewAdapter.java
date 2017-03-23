package de.grafelhaft.winespray.app.adapter.realm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.adapter.ARealmRecyclerViewAdapter;
import de.grafelhaft.winespray.app.viewholder.DistrictViewHolder;
import de.grafelhaft.winespray.model.District;
import io.realm.OrderedRealmCollection;
import io.realm.Sort;

/**
 * Created by Markus on 15.09.2016.
 */
public class DistrictRealmRecyclerViewAdapter extends ARealmRecyclerViewAdapter<District, DistrictViewHolder> {

    public DistrictRealmRecyclerViewAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<District> data, boolean autoUpdate) {
        super(context, data.sort("name", Sort.ASCENDING), autoUpdate);
    }

    @Override
    public DistrictViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_grape, parent, false);
        return new DistrictViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DistrictViewHolder holder, int position) {
        District district = getData().get(position);
        holder.bind(district);
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
