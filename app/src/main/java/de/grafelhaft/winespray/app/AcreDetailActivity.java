package de.grafelhaft.winespray.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import de.grafelhaft.winespray.app.fragment.AcreDetailFragment;
import de.grafelhaft.winespray.app.fragment.MapFragment;
import de.grafelhaft.winespray.app.fragment.ParcelDetailFragment;
import de.grafelhaft.winespray.app.util.IntentUtils;
import de.grafelhaft.winespray.model.Acre;
import de.grafelhaft.winespray.model.Area;
import de.grafelhaft.winespray.model.District;
import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


public class AcreDetailActivity extends BaseActivity implements View.OnClickListener {

    private Acre _acre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acre_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        long acreId;
        if (savedInstanceState == null) {
            acreId = getIntent().getLongExtra(IntentUtils.EXTRA_PARCEL_ID, -1);
        } else {
            acreId = savedInstanceState.getLong(IntentUtils.EXTRA_PARCEL_ID, -1);
        }

        if (acreId >= 0) {
            _acre = (Acre) RealmHelper.findWhereId(Acre.class, acreId);
            if (_acre != null) {
                getSupportActionBar().setTitle(_acre.getName());
            }
        } else {
            //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            _acre = new Acre()
                    .setName(getString(R.string.title_activity_acre_detail))
                    .setArea(new Area());
            RealmHelper.setAutoId(_acre, Acre.class);

            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(_acre);
                }
            });
        }

        init();
        findViewById(R.id.fab).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                startActivity(new Intent(this, ParcelDetailActivity.class)
                        .putExtra(IntentUtils.EXTRA_ACRE_ID, _acre.getId()));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareMap();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (_acre != null) {
            outState.putLong(IntentUtils.EXTRA_PARCEL_ID, _acre.getId());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.action_reset) {
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    if (_acre.getArea() != null && _acre.getArea().isManaged()) {
                        _acre.getArea().getLocations().clear();
                    }
                }
            });
            prepareMap();
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        inflateFragment();
    }

    private void inflateFragment() {
        AcreDetailFragment fragment = AcreDetailFragment.create(_acre);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.bottomsheet, fragment);
        transaction.commit();

        if (!isTwoPane()) {
            View bottomSheet = findViewById(R.id.bottomsheet);
            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setPeekHeight(getResources().getDimensionPixelSize(R.dimen.bottomsheet_height_collapsed));
            behavior.setHideable(false);
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    private void prepareMap() {
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.enableMyLocation(false);
        mapFragment.clear();
        if (_acre != null && _acre.getArea() != null && _acre.getArea().getLocations().size() > 0) {
            mapFragment.addPolygon(Realm.getDefaultInstance().copyFromRealm(_acre.getArea().getLocations()));
        }
    }
}
