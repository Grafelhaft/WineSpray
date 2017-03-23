package de.grafelhaft.winespray.app.viewholder;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import de.grafelhaft.grafellib.util.TimeUtils;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.model.Note;


/**
 * Created by Markus on 14.09.2016.
 */
public class NoteViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

    public TextView title, subtitle;
    public CheckBox checkBox;

    public NoteViewHolder(View itemView) {
        super(itemView);
        title =  (TextView) itemView.findViewById(R.id.title);
        subtitle =  (TextView) itemView.findViewById(R.id.subtitle);
        checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
    }

    public void bind(Note note) {
        title.setText(note.getContent());
        subtitle.setText(TimeUtils.formatDate(TimeUtils.convertTime(note.getTimeStamp()), "dd.MMMyyyy HH:mm"));
        select(note.isChecked());
        checkBox.setOnCheckedChangeListener(this);
    }

    public void select(boolean selected) {
        checkBox.setChecked(selected);

        if (selected) {
            title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            title.setPaintFlags(title.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
