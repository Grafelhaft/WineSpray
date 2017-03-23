package de.grafelhaft.winespray.app.viewholder;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;

import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.viewmodel.ProfileCategory;

/**
 * Created by @author Markus Graf (Grafelhaft) on 14.10.2016.
 */

public class ProfileParentViewHolder extends ParentViewHolder {

    public TextView title;

    public ProfileParentViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
    }

    public void bind(ProfileCategory category) {
        title.setText(category.getTitle());
    }
}
