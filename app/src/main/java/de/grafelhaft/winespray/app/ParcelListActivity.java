package de.grafelhaft.winespray.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;

import de.grafelhaft.winespray.app.fragment.ParcelDetailFragment;
import de.grafelhaft.winespray.app.fragment.ParcelListFragment;
import de.grafelhaft.winespray.app.util.IntentUtils;
import de.grafelhaft.winespray.model.Parcel;


public class ParcelListActivity extends BaseActivity implements ParcelListFragment.OnParcelClickListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    public void onParcelClicked(Parcel parcel) {
        if (isTwoPane()) {
            ParcelDetailFragment fragment = ParcelDetailFragment.create(parcel);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_detail, fragment, "DETAIL_FRAGMENT")
                    .commit();
        } else {
            Intent intent = new Intent(this, ParcelDetailActivity.class);
            intent.putExtra(IntentUtils.EXTRA_PARCEL_ID, parcel.getId());
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (isTwoPane()) {
                    ParcelDetailFragment fragment = ParcelDetailFragment.create(null);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_detail, fragment, "DETAIL_FRAGMENT")
                            .commit();
                } else {
                    startActivity(new Intent(this, ParcelDetailActivity.class));
                }
                break;
        }
    }
}
