package de.grafelhaft.winespray.app.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import de.grafelhaft.grafellib.util.TimeUtils;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.util.ResourceUtils;
import de.grafelhaft.winespray.model.Session;

/**
 * Created by @author Markus Graf on 12.10.2016.
 */

public class SessionViewHolder extends RecyclerView.ViewHolder {

    TextView title, subTitle, hint;
    ImageView image;

    public SessionViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        subTitle = (TextView) itemView.findViewById(R.id.subtitle);
        hint = (TextView) itemView.findViewById(R.id.hint);
        image = (ImageView) itemView.findViewById(R.id.image);
    }

    public void bind(Context context, Session session) {
        Date date = TimeUtils.convertTime(session.getStartTime());
        String dateString = TimeUtils.formatDate(date, "dd.MMMyyyy");

        if (session.getName() != null) {
            title.setText(session.getName());
        }
        subTitle.setText(dateString);
        hint.setText(context.getString(R.string.format_run_count, session.getRuns().size()));

        image.setImageResource(ResourceUtils.getVineyardRes(session.getId()));
    }
}
