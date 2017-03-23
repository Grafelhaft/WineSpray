package de.grafelhaft.winespray.model;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static org.junit.Assert.*;

/**
 * Created by @author Markus Graf (Grafelhaft) on 12.10.2016.
 */
@RunWith(AndroidJUnit4.class)
public class RealmInheritanceTest {

    private Realm getRealmInstance() {
        Context appContext = InstrumentationRegistry.getTargetContext();

        Realm.init(appContext);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        Realm.deleteRealm(realmConfig);
        Realm.setDefaultConfiguration(realmConfig);

        return Realm.getDefaultInstance();
    }

    @Test
    public void areaTest() throws Exception {
        Realm realm = getRealmInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Area area = new Area()
                        .setSize(200);
                RealmHelper.setAutoId(area, Area.class);

                realm.copyToRealmOrUpdate(area);

                Area area2 = new Area()
                        .setSize(200);
                RealmHelper.setAutoId(area2, Area.class);

                realm.copyToRealmOrUpdate(area2);
            }
        });

        RealmResults<Area> realmResults = realm.where(Area.class).findAll();

        assertEquals(realmResults.size(), 2, 0);
        assertEquals(realmResults.get(0).getId(), 0, 0);
        assertEquals(realmResults.get(1).getId(), 1, 0);
    }

}