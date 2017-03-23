package de.grafelhaft.winespray.app.adapter;

import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.grafelhaft.grafellib.adapter.ARecyclerViewAdapter;
import de.grafelhaft.winespray.app.R;

/**
 * Created by Markus on 18.09.2016.
 */
public class PairRecyclerViewAdapter extends ARecyclerViewAdapter<Pair<String,String>, PairRecyclerViewAdapter.SimpleViewHolder> {

    public PairRecyclerViewAdapter(List<Pair<String,String>> pairs) {
        this.getData().addAll(pairs);
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_simple, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, int position) {
        Pair<String,String> pair = getData().get(position);
        holder.bind(pair);
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

    @Override
    public int getItemCount() {
        return getData().size();
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView text;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            text = (TextView) itemView.findViewById(R.id.text);
        }

        public void bind(Pair<String,String> pair) {
            title.setText(pair.first);
            text.setText(pair.second);
        }
    }
}
