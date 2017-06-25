package de.grafelhaft.winespray.app.adapter.realm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.adapter.ARealmRecyclerViewAdapter;
import de.grafelhaft.winespray.app.viewholder.SessionViewHolder;
import de.grafelhaft.winespray.model.Session;
import io.realm.OrderedRealmCollection;

/**
 * Created by Markus on 15.09.2016.
 */
public class SessionRealmRecyclerViewAdapter extends ARealmRecyclerViewAdapter<Session, SessionViewHolder> {

    public SessionRealmRecyclerViewAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Session> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
    }

    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_session, parent, false);
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SessionViewHolder holder, int position) {
        final Session session = getData().get(position);
        holder.bind(context, session);
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
}
