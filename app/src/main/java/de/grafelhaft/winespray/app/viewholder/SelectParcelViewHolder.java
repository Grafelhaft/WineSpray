package de.grafelhaft.winespray.app.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.model.Parcel;

/**
 * Created by @author Markus Graf on 12.10.2016.
 */

public class SelectParcelViewHolder extends RecyclerView.ViewHolder {

    public TextView title, subtitle;
    public CheckBox checkBox;

    public SelectParcelViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        subtitle = (TextView) itemView.findViewById(R.id.subtitle);
        checkBox = (CheckBox) itemView.findViewById(R.id.checkbox_parcel);
    }

    public void bind(Parcel parcel, boolean selected) {
        title.setText(parcel.getNumerator() + "/" + parcel.getDenominator());
        if (parcel.getGrape() != null) {
            subtitle.setText(parcel.getGrape().getName());
            subtitle.setVisibility(View.VISIBLE);
        } else {
            subtitle.setVisibility(View.GONE);
        }
        checkBox.setChecked(selected);
    }

}
