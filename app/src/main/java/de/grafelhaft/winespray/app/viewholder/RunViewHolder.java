package de.grafelhaft.winespray.app.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

import de.grafelhaft.grafellib.util.TimeUtils;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.model.Run;
import de.grafelhaft.winespray.model.Session;

/**
 * Created by @author Markus Graf on 12.10.2016.
 */

public class RunViewHolder extends RecyclerView.ViewHolder {

    public TextView title, subtitle;

    public RunViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        subtitle = (TextView) itemView.findViewById(R.id.subtitle);
    }

    public void bind(Run run) {
        Date start = TimeUtils.convertTime(run.getStartTime());
        String startString = TimeUtils.formatDate(start, "dd.MM.yyyy");

        String startTime = TimeUtils.formatDate(TimeUtils.convertTime(run.getStartTime()), "HH:mm");

        String endTime;
        if (run.getEndTime() > 0) {
            Date end = TimeUtils.convertTime(run.getEndTime());
            endTime = TimeUtils.formatDate(end, " - HH:mm");
        } else {
            endTime = " - active";
        }

        title.setText(startString);
        subtitle.setText(startTime + endTime);
    }
}
