package de.grafelhaft.winespray.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import de.grafelhaft.winespray.app.fragment.AcreDetailFragment;
import de.grafelhaft.winespray.app.fragment.AcreListFragment;
import de.grafelhaft.winespray.app.fragment.MapFragment;
import de.grafelhaft.winespray.app.fragment.ParcelDetailFragment;
import de.grafelhaft.winespray.app.util.IntentUtils;
import de.grafelhaft.winespray.model.Acre;
import io.realm.Realm;


public class AcreListActivity extends BaseActivity implements AcreListFragment.OnAcreClickListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acre_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    public void onAcreClicked(Acre acre) {
        if (acre != null) {
            if (isTwoPane()) {
                AcreDetailFragment fragment = AcreDetailFragment.create(acre);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.bottomsheet, fragment, "DETAIL_FRAGMENT")
                        .commit();
                getSupportActionBar().setTitle(acre.toString());
                prepareMap(acre);
            } else {
                Intent intent = new Intent(this, AcreDetailActivity.class);
                intent.putExtra(IntentUtils.EXTRA_PARCEL_ID, acre.getId());
                startActivity(intent);
            }
        } else {
            if (isTwoPane()) {
                ((FrameLayout)findViewById(R.id.container_detail)).removeAllViews();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (isTwoPane()) {
                    AcreDetailFragment fragment = AcreDetailFragment.create(null);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_detail, fragment, "DETAIL_FRAGMENT")
                            .commit();
                    prepareMap(null);
                } else {
                    startActivity(new Intent(this, AcreDetailActivity.class));
                }
                break;
        }
    }

    private void prepareMap(Acre acre) {
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.enableMyLocation(false);
        mapFragment.clear();
        if (acre != null && acre.getArea() != null && acre.getArea().getLocations().size() > 0) {
            mapFragment.addPolygon(Realm.getDefaultInstance().copyFromRealm(acre.getArea().getLocations()));
        }
    }
}
