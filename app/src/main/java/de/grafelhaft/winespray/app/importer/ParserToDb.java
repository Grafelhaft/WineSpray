package de.grafelhaft.winespray.app.importer;

import de.grafelhaft.winespray.model.Acre;
import de.grafelhaft.winespray.model.District;
import de.grafelhaft.winespray.model.EinzelLage;
import de.grafelhaft.winespray.model.Grape;
import de.grafelhaft.winespray.model.Region;
import de.grafelhaft.winespray.model.parser.DataSet;
import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by @author Markus Graf (Grafelhaft) on 13.10.2016.
 */

public class ParserToDb {

    public static void save(DataSet dataSet) {

        for (final Acre a : dataSet.getAcres()) {
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmHelper.setAutoId(a, Acre.class);
                    realm.copyToRealmOrUpdate(a);
                }
            });
        }

        Acre acre = (Acre) RealmHelper.findWhereId(Acre.class, 4);
        Grape grape = acre.getParcels().get(0).getGrape();
        long grapeId = grape.getId();
        RealmResults<Grape> grapes = (RealmResults<Grape>) RealmHelper.findAll(Grape.class);
        RealmResults<EinzelLage> regions = (RealmResults<EinzelLage>) RealmHelper.findAll(EinzelLage.class);
    }
}
