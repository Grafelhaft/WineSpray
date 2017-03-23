package de.grafelhaft.winespray.model.app;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by @author Markus Graf (Grafelhaft) on 27.10.2016.
 */

public class RealmApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        //Realm.deleteRealm(realmConfig);
        Realm.setDefaultConfiguration(realmConfig);
    }
}
