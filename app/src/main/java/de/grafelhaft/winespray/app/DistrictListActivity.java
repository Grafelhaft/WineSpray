package de.grafelhaft.winespray.app;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import de.grafelhaft.winespray.app.fragment.DistrictListFragment;
import de.grafelhaft.winespray.app.fragment.GrapeListFragment;
import de.grafelhaft.winespray.model.District;
import de.grafelhaft.winespray.model.Grape;

public class DistrictListActivity extends BaseActivity implements DistrictListFragment.OnDistrictClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onDistrictClicked(District district) {
        if (isTwoPane()) {

        } else {

        }
    }
}
