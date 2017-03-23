package de.grafelhaft.winespray.app;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import de.grafelhaft.grafellib.util.PrefUtils;
import de.grafelhaft.winespray.app.broadcast.ClearDbBroadcastReceiver;
import de.grafelhaft.winespray.app.broadcast.ExportBroadcastReceiver;
import de.grafelhaft.winespray.app.broadcast.ImportBroadcastReceiver;
import de.grafelhaft.winespray.app.service.GpsLocationService;
import de.grafelhaft.winespray.app.util.FileUtils;
import de.grafelhaft.winespray.app.util.IntentUtils;


public class SplashScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean ftu = PrefUtils.isFirstTimeStarting(getApplicationContext());
                if (ftu) {
                    //FileUtils.initDirs();
                }

                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
                localBroadcastManager.registerReceiver(new ImportBroadcastReceiver(), new IntentFilter(IntentUtils.FILTER_IMPORT));
                localBroadcastManager.registerReceiver(new ExportBroadcastReceiver(), new IntentFilter(IntentUtils.FILTER_EXPORT));
                localBroadcastManager.registerReceiver(new ClearDbBroadcastReceiver(), new IntentFilter(IntentUtils.FILTER_CLEAR));

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }, 1000);
    }
}
