package de.grafelhaft.winespray.app.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import de.grafelhaft.winespray.app.ImportActivity;
import io.realm.Realm;

/**
 * Created by @author Markus Graf (Grafelhaft) on 16.10.2016.
 */

public class ClearDbBroadcastReceiver extends BroadcastReceiver {

    private static int counter = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        counter++;
        if (counter >= 2) {
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.deleteAll();
                }
            });
            counter = 0;
        }
    }
}
