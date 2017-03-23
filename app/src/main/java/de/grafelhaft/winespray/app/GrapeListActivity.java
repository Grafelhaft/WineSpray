package de.grafelhaft.winespray.app;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import de.grafelhaft.winespray.app.fragment.GrapeListFragment;
import de.grafelhaft.winespray.model.Grape;

public class GrapeListActivity extends BaseActivity implements GrapeListFragment.OnGrapeClickListener {

    private boolean _isTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grape_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.container_detail) != null) {
            _isTwoPane = true;
        }
    }

    @Override
    public void onGrapeClicked(Grape grape) {
        if (_isTwoPane) {

        } else {

        }
    }
}
