package de.grafelhaft.winespray.app.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;

import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.viewmodel.ProfileItem;

/**
 * Created by Markus on 16.09.2016.
 */
public class ProfileViewHolder extends ChildViewHolder {

    public TextView title;
    public ImageView icon;

    public ProfileViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        icon = (ImageView) itemView.findViewById(R.id.icon);
    }

    public void bind(ProfileItem item) {
        title.setText(item.getTitle());
        icon.setImageResource(item.getIconRes());
    }
}
