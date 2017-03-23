package de.grafelhaft.winespray.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import java.util.List;

import de.grafelhaft.grafellib.async.OnTaskOutputListener;
import de.grafelhaft.winespray.acrecontrol.Constants;
import de.grafelhaft.winespray.acrecontrol.PolygonBuilder;
import de.grafelhaft.winespray.app.fragment.MapFragment;
import de.grafelhaft.winespray.app.service.AcreControlService;
import de.grafelhaft.winespray.app.util.IntentUtils;
import de.grafelhaft.winespray.model.Acre;
import de.grafelhaft.winespray.model.Area;
import de.grafelhaft.winespray.model.Location;
import de.grafelhaft.winespray.model.State;
import de.grafelhaft.winespray.model.Unit;
import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.Realm;

/**
 * Created by @author Markus Graf (Grafelhaft) on 26.10.2016.
 */

public class AcreRecordActivity extends BaseActivity implements View.OnClickListener, OnTaskOutputListener<List<Location>>, AcreControlService.OnPointAddedListener {

    private Acre _acre;
    private int _state = State.NEW;

    private MapFragment _mapFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acre_record);

        long acreId = getIntent().getLongExtra(IntentUtils.EXTRA_ACRE_ID, -1);

        if (acreId >= 0) {
            _acre = (Acre) RealmHelper.findWhereId(Acre.class, acreId);
        }

        if (_acre != null) {
            init();
        } else {
            finish();
        }
    }

    private void init() {
        findViewById(R.id.fab).setOnClickListener(this);

        _mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        _mapFragment.enableMyLocation(true);
    }

    @Override
    public void onTaskOutput(final List<Location> locations) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Area old = _acre.getArea();

                Area area = realm.createObject(Area.class)
                        .setLocations(locations)
                        .setSize(old.getSize() <= 0 ? new PolygonBuilder(locations, Constants.UNIT_DEGREES).setOutput(Constants.UNIT_METER).build().area() : old.getSize())
                        .setSizeUseful(old.getSizeUseful() <= 0 ? new PolygonBuilder(locations, Constants.UNIT_DEGREES).setOutput(Constants.UNIT_METER).build().area() : old.getSizeUseful());

                _acre.setArea(area);
                _acre = realm.copyToRealmOrUpdate(_acre);
            }
        });
        finish();
    }

    @Override
    public void onPointAdded(Location location) {
        _mapFragment.addPoint(location);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                if (_state == State.NEW || _state == State.PAUSED) {
                    fab.setImageResource(R.drawable.ic_stop_white_48dp);
                    _state = State.ACTIVE;
                    AcreControlService.getInstance().startRecording(this, this);
                } else {
                    fab.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                    _state = State.STOPPED;
                    AcreControlService.getInstance().stopRecording();
                }
                break;
        }
    }
}
