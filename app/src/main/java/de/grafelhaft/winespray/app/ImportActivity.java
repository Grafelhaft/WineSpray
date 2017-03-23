package de.grafelhaft.winespray.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import de.grafelhaft.grafellib.adapter.ViewStatePagerAdapter;
import de.grafelhaft.grafellib.fragment.OnStepReadyListener;
import de.grafelhaft.grafellib.view.ExtendedViewPager;
import de.grafelhaft.winespray.app.dialog.MessageDialog;
import de.grafelhaft.winespray.app.dialog.OnDialogCallbackListener;
import de.grafelhaft.winespray.app.fragment.AcreListFragment;
import de.grafelhaft.winespray.app.fragment.importer.CreateAcreFragment;
import de.grafelhaft.winespray.app.fragment.importer.SelectParcelsFragment;
import de.grafelhaft.winespray.app.fragment.importer.SelectSourceFragment;
import de.grafelhaft.winespray.model.Acre;
import de.grafelhaft.winespray.model.Parcel;
import de.grafelhaft.winespray.model.parser.DataSet;
import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.Realm;

public class ImportActivity extends BaseActivity implements CreateAcreFragment.OnAcreCreatedListener, SelectSourceFragment.OnDataParsedListener {

    private DataSet _dataSet;
    private List<Acre> _acres = new ArrayList<>();

    private ExtendedViewPager _pager;
    private ViewStatePagerAdapter _adapter;

    @Override
    public void onBackPressed() {
        if (_pager.getCurrentItem() > 1) {
            _pager.previous();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        init();
    }

    private void init() {
        _adapter = new ViewStatePagerAdapter(getSupportFragmentManager(),
                new Fragment[]{
                        new SelectSourceFragment().setOnDataParsedListener(this),
                        new CreateAcreFragment()
                                .addOnAcreCreatedListener(this)
                                .setOnStepReadyListener(new OnStepReadyListener() {
                            @Override
                            public void onStepReady(boolean b) {
                                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        for (Acre a : _acres) {
                                            RealmHelper.setAutoId(a, Acre.class);
                                            a.setArea(a.getArea());
                                            realm.copyToRealmOrUpdate(a);
                                        }
                                    }
                                });
                                finish();
                            }
                        }),
                        new SelectParcelsFragment().setOnStepReadyListener(new OnStepReadyListener() {
                            @Override
                            public void onStepReady(boolean b) {
                                _pager.previous();
                                CreateAcreFragment acreFragment = (CreateAcreFragment) _adapter.getItem(1);
                                acreFragment.setAcres(_acres);
                            }
                        })
                });

        _pager = (ExtendedViewPager) findViewById(R.id.pager);
        _pager.setPagingEnabled(false);
        _pager.setAdapter(_adapter);
    }

    @Override
    public void onParsed(DataSet dataSet) {
        _dataSet = dataSet;
        SelectParcelsFragment parcelsFragment = (SelectParcelsFragment) _adapter.getItem(2);
        parcelsFragment.setParcels(dataSet.getParcels());
        _pager.next();
    }

    @Override
    public void onAcreCreated(Acre acre) {
        _acres.add(acre);
        SelectParcelsFragment parcelsFragment = (SelectParcelsFragment) _adapter.getItem(2);
        parcelsFragment.setAcre(acre);
        _pager.next();
    }

    @Override
    public void onAcreDeleted(Acre acre) {
        _acres.remove(acre);
    }

    @Override
    public void onAcreClicked(Acre acre) {
        SelectParcelsFragment parcelsFragment = (SelectParcelsFragment) _adapter.getItem(2);
        parcelsFragment.setAcre(acre);
        _pager.next();
    }

}
