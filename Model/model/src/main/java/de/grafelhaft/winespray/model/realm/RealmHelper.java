package de.grafelhaft.winespray.model.realm;

import java.util.concurrent.atomic.AtomicLong;

import de.grafelhaft.winespray.model.IModel;
import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by @author Markus Graf (Grafelhaft) on 12.10.2016.
 */

public class RealmHelper {

    private static final String LOG_TAG = RealmHelper.class.getName();

    public static synchronized <T extends RealmModel> AtomicLong getNextId(Class<T> clazz) {
        Realm realm = Realm.getDefaultInstance();
        Number results = realm.where(clazz).max("id");
        if (results == null) {
            return new AtomicLong(0);
        } else {
            return new AtomicLong(results.longValue() + 1);
        }
    }

    public static synchronized <T extends RealmModel> long setAutoId(IModel model, Class<T> clazz) {
        long id = getNextId(clazz).longValue();
        model.setId(id);
        return id;
    }


    public static <T extends io.realm.RealmObject> RealmObject write(T object) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        T realmObject = realm.copyToRealm(object);
        realm.commitTransaction();
        return realmObject;
    }

    public static <T extends io.realm.RealmObject> RealmObject writeUpdate(T object) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        T realmObject = realm.copyToRealmOrUpdate(object);
        realm.commitTransaction();
        return realmObject;
    }

    public static <T extends RealmObject> void writeUpdateAsync(final T object) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                T realmObject = realm.copyToRealmOrUpdate(object);
            }
        });
    }

    public static <T extends RealmObject> void writeAsync(final T object) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                T realmObject = realm.copyToRealm(object);
            }
        });
    }

    public static <T extends RealmObject> void writeAsync(final T object, Realm.Transaction.OnSuccess onSuccess) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                T realmObject = realm.copyToRealm(object);
            }
        }, onSuccess);
    }

    public static <T extends RealmObject> void writeAsync(final T object, Realm.Transaction.OnError onError) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                T realmObject = realm.copyToRealm(object);
            }
        }, onError);
    }

    public static <T extends RealmObject> void writeAsync(final T object, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                T realmObject = realm.copyToRealm(object);
            }
        }, onSuccess, onError);
    }

    public static <T extends RealmObject> void remove(final T object) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                object.deleteFromRealm();
            }
        });
    }

    public static <T extends RealmObject> RealmResults<? extends RealmObject> findAll(Class<T> clazz) {
        Realm realm = Realm.getDefaultInstance();

        return realm
                .where(clazz)
                .findAll();
    }

    public static <T extends RealmObject> RealmModel findWhereId(Class<T> clazz, long id) {
        Realm realm = Realm.getDefaultInstance();

        RealmResults results = realm
                .where(clazz)
                .equalTo("id", id)
                .findAll();

        return results.first();
    }
}
