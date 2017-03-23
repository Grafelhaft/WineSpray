package de.grafelhaft.winespray.app;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import de.grafelhaft.grafellib.util.TimeUtils;
import de.grafelhaft.winespray.app.fragment.MapFragment;
import de.grafelhaft.winespray.app.fragment.RunDetailFragment;
import de.grafelhaft.winespray.app.util.IntentUtils;
import de.grafelhaft.winespray.model.Acre;
import de.grafelhaft.winespray.model.Run;
import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.Realm;

public class RunDetailActivity extends BaseActivity implements View.OnClickListener, RunDetailFragment.OnAcreChangedListener {

    private Run _run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        long runId;
        if (_run == null && savedInstanceState != null) {
            runId = savedInstanceState.getLong(IntentUtils.EXTRA_RUN_ID, -1);
        } else {
            runId = getIntent().getLongExtra(IntentUtils.EXTRA_RUN_ID, -1);
        }

        if (runId >= 0) {
            _run = (Run) RealmHelper.findWhereId(Run.class, runId);

            getSupportActionBar().setTitle(TimeUtils.formatDate(TimeUtils.convertTime(_run.getStartTime()), "dd.MMMyyyy HH:mm"));

            inflateFragment();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (_run != null) {
            outState.putLong(IntentUtils.EXTRA_RUN_ID, _run.getId());
        }
    }

    private void inflateFragment() {
        MapFragment mapFragment = new MapFragment();
        mapFragment.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin));
        mapFragment.showZoomButtons(false);
        mapFragment.enableMyLocation(true);
        mapFragment.addHeatMap(_run.getDataPoints());
        mapFragment.addPolygon(_run.getAcre());
        mapFragment.addRoute(Realm.getDefaultInstance().copyFromRealm(_run.getRoute()));

        RunDetailFragment runDetailFragment = RunDetailFragment.create(_run);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_map, mapFragment, "FRAGMENT_MAP");
        transaction.replace(R.id.bottomsheet, runDetailFragment);
        transaction.commit();

        if (!isTwoPane()) {
            View bottomSheet = findViewById(R.id.bottomsheet);
            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setPeekHeight(getResources().getDimensionPixelSize(R.dimen.bottomsheet_height_collapsed));
            behavior.setHideable(false);
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        findViewById(R.id.fab).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                MapFragment fragment = (MapFragment) getSupportFragmentManager().findFragmentByTag("FRAGMENT_MAP");
                fragment.onMyLocationButtonClick();

                View bottomSheet = findViewById(R.id.bottomsheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
        }
    }

    @Override
    public void onAcreChanged(Acre acre) {
        if (acre.getArea() != null && acre.getArea().getLocations().size() > 0) {
            MapFragment fragment = (MapFragment) getSupportFragmentManager().findFragmentByTag("FRAGMENT_MAP");
            fragment.clearPolygons();
            fragment.addPolygon(Realm.getDefaultInstance().copyFromRealm(acre.getArea().getLocations()));
            fragment.redraw();
        }
    }
}
