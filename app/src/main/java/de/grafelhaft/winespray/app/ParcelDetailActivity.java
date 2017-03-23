package de.grafelhaft.winespray.app;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import de.grafelhaft.winespray.app.fragment.ParcelDetailFragment;
import de.grafelhaft.winespray.app.util.IntentUtils;
import de.grafelhaft.winespray.model.Parcel;
import de.grafelhaft.winespray.model.realm.RealmHelper;


public class ParcelDetailActivity extends BaseActivity {

    private Parcel _parcel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        long parcelId;
        if (savedInstanceState == null) {
            parcelId = getIntent().getLongExtra(IntentUtils.EXTRA_PARCEL_ID, -1);
        } else {
            parcelId = savedInstanceState.getLong(IntentUtils.EXTRA_PARCEL_ID, -1);
        }

        if (parcelId >= 0) {
            _parcel = (Parcel) RealmHelper.findWhereId(Parcel.class, parcelId);
            if (_parcel != null) {
                getSupportActionBar().setTitle(_parcel.getNumerator() + "/" + _parcel.getDenominator());
            }
        } else {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }

        init();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (_parcel != null) {
            outState.putLong(IntentUtils.EXTRA_PARCEL_ID, _parcel.getId());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        inflateFragment();
    }

    private void inflateFragment() {
        ParcelDetailFragment fragment = ParcelDetailFragment.create(_parcel);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}
