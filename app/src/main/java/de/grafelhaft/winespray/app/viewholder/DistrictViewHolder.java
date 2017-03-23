package de.grafelhaft.winespray.app.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.model.District;

/**
 * Created by @author Markus Graf on 12.10.2016.
 */

public class DistrictViewHolder extends RecyclerView.ViewHolder {

    public TextView title;

    public DistrictViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
    }

    public void bind(District district) {
        title.setText(district.getName());
    }
}
