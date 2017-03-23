package de.grafelhaft.winespray.app;

import de.grafelhaft.winespray.app.service.GpsLocationService;
import de.grafelhaft.winespray.model.app.RealmApplication;

/**
 * Created by Markus on 15.09.2016.
 */
public class WineApplication extends RealmApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //MultiDex.install(this);
        GpsLocationService.getInstance().start(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        GpsLocationService.getInstance().stop();
    }
}
