package de.grafelhaft.winespray.app.adapter.realm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.adapter.ASelectableRealmRecyclerViewAdapter;
import de.grafelhaft.winespray.app.viewholder.NoteViewHolder;
import de.grafelhaft.winespray.model.Note;
import io.realm.OrderedRealmCollection;
import io.realm.Sort;

/**
 * Created by Markus on 16.09.2016.
 */
public class NoteRealmRecyclerViewAdapter extends ASelectableRealmRecyclerViewAdapter<Note, NoteViewHolder> {

    public NoteRealmRecyclerViewAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Note> data, boolean autoUpdate) {
        super(context, data.sort("timeStamp", Sort.DESCENDING), autoUpdate);
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NoteViewHolder holder, final int position) {
        final Note note = getData().get(position);

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.bind(note);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                onItemSelected(holder.getAdapterPosition(), isChecked);
            }
        });

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
