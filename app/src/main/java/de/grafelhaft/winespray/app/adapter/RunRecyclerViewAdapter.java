package de.grafelhaft.winespray.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.grafelhaft.grafellib.adapter.ARecyclerViewAdapter;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.viewholder.RunViewHolder;
import de.grafelhaft.winespray.model.Run;

/**
 * Created by Markus on 15.09.2016.
 */
public class RunRecyclerViewAdapter extends ARecyclerViewAdapter<Run, RunViewHolder> {

    public RunRecyclerViewAdapter() {

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
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }
}
