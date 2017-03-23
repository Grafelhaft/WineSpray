package de.grafelhaft.winespray.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.grafelhaft.grafellib.adapter.ASelectableRecyclerViewAdapter;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.viewholder.SelectParcelViewHolder;
import de.grafelhaft.winespray.model.Acre;
import de.grafelhaft.winespray.model.Parcel;

/**
 * Created by @author Markus Graf (Grafelhaft) on 21.10.2016.
 */

public class SelectParcelRecyclerViewAdapter extends ASelectableRecyclerViewAdapter<Parcel, SelectParcelViewHolder> {

    public SelectParcelRecyclerViewAdapter(List<Parcel> parcels) {
        getData().addAll(sort(parcels));
    }

    @Override
    public SelectParcelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_parcel_select, parent, false);
        return new SelectParcelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SelectParcelViewHolder holder, final int position) {
        holder.bind(getData().get(position), isItemSelected(holder.getAdapterPosition()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSelection(v, holder.getAdapterPosition());
            }
        });
    }

    public List<Parcel> sort(List<Parcel> parcels) {
        if (parcels != null) {
            Collections.sort(parcels, new Comparator<Parcel>() {
                @Override
                public int compare(Parcel p1, Parcel p2) {
                    if (p1.getNumerator() < p2.getNumerator()) {
                        return -1;
                    }
                    if (p1.getNumerator() > p2.getNumerator()) {
                        return +1;
                    }
                    return 0;
                }
            });
            return parcels;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }

    public void update(List<Parcel> parcels) {
        getData().clear();
        getData().addAll(sort(parcels));
        clearSelection();
        notifyDataSetChanged();
    }

    public void update(Acre acre) {
        clearSelection();
        List<Integer> index = new ArrayList<>();
        for (Parcel p : acre.getParcels()) {
            index.add(getData().indexOf(p));
        }
        for (Integer i : index) {
            toggleSelection(null, i);
        }
    }

}
