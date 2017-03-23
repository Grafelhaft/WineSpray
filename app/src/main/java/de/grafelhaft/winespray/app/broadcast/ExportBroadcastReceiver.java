package de.grafelhaft.winespray.app.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import de.grafelhaft.grafellib.util.TimeUtils;
import de.grafelhaft.winespray.app.ImportActivity;
import de.grafelhaft.winespray.app.dialog.DialogActivity;
import de.grafelhaft.winespray.app.util.FileUtils;
import de.grafelhaft.winespray.model.Location;
import de.grafelhaft.winespray.model.Run;
import de.grafelhaft.winespray.model.Session;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by @author Markus Graf (Grafelhaft) on 16.10.2016.
 */

public class ExportBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                RealmResults<Session> sessions = Realm.getDefaultInstance().where(Session.class).findAll();

                final List<File> files = new ArrayList<>();
                for (Session s : sessions) {
                    for (Run r : s.getRuns()) {
                        try {
                            files.add(FileUtils.writeFile(FileUtils.PATH_EXPORT, "run_" + s.getId() + "_" + TimeUtils.formatDate(TimeUtils.convertTime(r.getStartTime()), "yyyy_MM_dd_HH_mm_ss") + ".txt", r.toJSON().toString()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        /*context.startActivity(new Intent(context, DialogActivity.class)
                                .putExtra(DialogActivity.EXTRA_TITLE, "Export")
                                .putExtra(DialogActivity.EXTRA_MESSAGE, "Files have been exported and stored on your device."));*/
                        if (files.size() > 0) {
                            Intent share = new Intent();
                            share.setAction(Intent.ACTION_SEND_MULTIPLE);
                            share.setType("*/*");

                            ArrayList<Uri> uris = new ArrayList<>();
                            for (File f : files) {
                                uris.add(Uri.fromFile(f));
                            }

                            share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                            context.startActivity(Intent.createChooser(share, "Share file"));
                        }
                    }
                });
            }
        };
        new Thread(runnable).start();
    }
}
