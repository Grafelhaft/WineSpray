package de.grafelhaft.winespray.app.adapter.realm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.adapter.ARealmRecyclerViewAdapter;
import de.grafelhaft.winespray.app.viewholder.RunViewHolder;
import de.grafelhaft.winespray.model.Run;
import io.realm.OrderedRealmCollection;

/**
 * Created by Markus on 15.09.2016.
 */
public class RunRealmRecyclerViewAdapter extends ARealmRecyclerViewAdapter<Run, RunViewHolder> {

    public RunRealmRecyclerViewAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Run> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
    }

    @Override
    public RunViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_run, parent, false);
        return new RunViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RunViewHolder holder, int position) {
        Run run = getData().get(position);
        holder.bind(run);
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
