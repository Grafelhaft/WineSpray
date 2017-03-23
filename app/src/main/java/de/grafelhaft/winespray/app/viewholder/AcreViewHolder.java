package de.grafelhaft.winespray.app.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.util.MathUtils;
import de.grafelhaft.winespray.model.Acre;
import de.grafelhaft.winespray.model.Grape;

/**
 * Created by @author Markus Graf on 12.10.2016.
 */

public class AcreViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView text;
    public TextView count;

    public AcreViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        text = (TextView) itemView.findViewById(R.id.text);
        count = (TextView) itemView.findViewById(R.id.count);
    }

    public void bind(Acre acre) {
        count.setText(this.getAdapterPosition()+1+"");
        if (acre.getName() != null) {
            title.setText(acre.getName());
        } else {
            title.setText(R.string.unknown);
        }
        if (acre.getArea() != null) text.setText(MathUtils.format2Decimals(acre.getArea().getSize()/10000, 4) + " ha");
    }
}
