package de.grafelhaft.winespray.app.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import de.grafelhaft.winespray.app.ImportActivity;
import de.grafelhaft.winespray.app.SettingsActivity;

/**
 * Created by @author Markus Graf (Grafelhaft) on 16.10.2016.
 */

public class ImportBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startActivity(new Intent(context, ImportActivity.class)
        .setAction(intent.getAction())
        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
