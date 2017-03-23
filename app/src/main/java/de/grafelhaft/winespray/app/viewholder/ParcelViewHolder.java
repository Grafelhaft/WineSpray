package de.grafelhaft.winespray.app.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.util.MathUtils;
import de.grafelhaft.winespray.model.Acre;
import de.grafelhaft.winespray.model.Parcel;

/**
 * Created by @author Markus Graf on 12.10.2016.
 */

public class ParcelViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView subtitle;
    public TextView text;

    public ParcelViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        subtitle = (TextView) itemView.findViewById(R.id.subtitle);
        text = (TextView) itemView.findViewById(R.id.text);
    }

    public void bind(Parcel parcel) {
        title.setText(parcel.getNumerator() + "/" + parcel.getDenominator());
        if (parcel.getGrape() != null) {
            subtitle.setText(parcel.getGrape().getName());
            subtitle.setVisibility(View.VISIBLE);
        } else {
            subtitle.setVisibility(View.GONE);
        }
        if (parcel.getArea() != null) {
            text.setText(MathUtils.format2Decimals(parcel.getArea().getSize()/10000, 4) + " ha");
        }
    }
}
