package de.grafelhaft.winespray.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import java.util.List;

import de.grafelhaft.grafellib.util.PrefUtils;
import de.grafelhaft.winespray.app.controller.RunController;
import de.grafelhaft.winespray.app.dialog.MessageDialog;
import de.grafelhaft.winespray.app.dialog.OnDialogCallbackListener;
import de.grafelhaft.winespray.app.fragment.ActiveRunFragment;
import de.grafelhaft.winespray.app.fragment.MapFragment;
import de.grafelhaft.winespray.app.util.IntentUtils;
import de.grafelhaft.winespray.app.util.ViewUtils;
import de.grafelhaft.winespray.model.RunConfig;
import de.grafelhaft.winespray.model.Session;
import de.grafelhaft.winespray.model.State;
import de.grafelhaft.winespray.model.realm.RealmHelper;

public class ActiveRunActivity extends BaseActivity implements SpaceOnClickListener, State.OnStateChangedListener {

    private int _state = State.NEW;
    private long _sessionId;

    SpaceNavigationView _navigationView;

    @Override
    public void onBackPressed() {
        if (_state == State.ACTIVE) {
            MessageDialog dialog = MessageDialog.create(this, R.string.dialog_title_message, R.string.still_active_note);
            dialog.setOnDialogCallbackListener(new OnDialogCallbackListener() {
                @Override
                public void onCallback(boolean positive, boolean negative, boolean neutral) {
                    finish();
                }
            });
            dialog.show(getSupportFragmentManager(), "MESSAGE_DIALOG");
        } else {
            NavUtils.navigateUpTo(this, new Intent(this, SessionDetailActivity.class).putExtra(IntentUtils.EXTRA_SESSION_ID, _sessionId));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_active);

        if (savedInstanceState == null) {
            _sessionId = getIntent().getLongExtra(IntentUtils.EXTRA_SESSION_ID, -1);
            _state = getIntent().getIntExtra(IntentUtils.EXTRA_STATE_ID, State.NEW);
        } else {
            _sessionId = savedInstanceState.getLong(IntentUtils.EXTRA_SESSION_ID, -1);
            _state = savedInstanceState.getInt(IntentUtils.EXTRA_STATE_ID, State.NEW);
        }

        if (PrefUtils.readBooleanPref(this, R.string.pref_key_screen_always_on, false)) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.drawActiveRun(true);
        mapFragment.enableMyLocation(true);

        _navigationView = (SpaceNavigationView) findViewById(R.id.space);
        _navigationView.initWithSaveInstanceState(savedInstanceState);
        if (_state != State.STOPPED && _state != State.NEW) {
            _navigationView.setCentreButtonIcon(R.drawable.ic_stop_white_48dp);
        } else {
            _navigationView.setCentreButtonIcon(R.drawable.ic_play_arrow_white_48dp);
        }
        _navigationView.setCentreButtonColor(getResources().getColor(R.color.colorAccent));
        _navigationView.addSpaceItem(new SpaceItem(getString(R.string.live), R.drawable.ic_fiber_manual_record_white_24dp));
        _navigationView.addSpaceItem(new SpaceItem(getString(R.string.total), R.drawable.ic_sigma_white_24dp));
        _navigationView.setSpaceOnClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(IntentUtils.EXTRA_SESSION_ID, _sessionId);
        outState.putInt(IntentUtils.EXTRA_STATE_ID, _state);
    }

    private void startRun() {
        if (_sessionId >= 0) {
            Session session = (Session) RealmHelper.findWhereId(Session.class, _sessionId);

            if (session != null) {
                RunController.getInstance().start(this, _sessionId, new RunConfig(
                        Integer.parseInt(PrefUtils.readStringPref(getApplicationContext(), R.string.pref_key_update_interval, getApplicationContext().getString(R.string.pref_default_update_interval))),
                        Integer.parseInt(PrefUtils.readStringPref(getApplicationContext(), R.string.pref_key_nozzle_count, getApplicationContext().getString(R.string.pref_default_nozzle_count))),
                        Double.parseDouble(PrefUtils.readStringPref(getApplicationContext(), R.string.pref_key_working_width, getApplicationContext().getString(R.string.pref_default_working_width)))
                ));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        RunController.getInstance().removeOnStateChangedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RunController.getInstance().addOnStateChangedListener(this);
    }

    @Override
    public void onCentreButtonClick() {
        FloatingActionButton fab = null;
        List<View> views = ViewUtils.getAllChildren(findViewById(R.id.space));
        for (View v : views) {
            if (v instanceof FloatingActionButton) {
                fab = (FloatingActionButton) v;
            }
        }

        if (_state == State.NEW || _state == State.PAUSED) {
            startRun();
            fab.setImageResource(R.drawable.ic_stop_white_48dp);
        } else {
            RunController.getInstance().stop(this);
            fab.setImageResource(R.drawable.ic_play_arrow_white_48dp);
        }
    }

    @Override
    public void onItemClick(int itemIndex, String itemName) {
        ActiveRunFragment runFragment = (ActiveRunFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_data);
        switch (itemIndex) {
            case 0:
                runFragment.setMode(ActiveRunFragment.MODE_LIVE);
                break;
            case 1:
                runFragment.setMode(ActiveRunFragment.MODE_TOTAL);
                break;
        }
    }

    @Override
    public void onItemReselected(int itemIndex, String itemName) {
        onItemClick(itemIndex, itemName);
    }

    @Override
    public void onStateChanged(int state) {
        _state = state;
        if (_state == State.STOPPED) {
            this.onBackPressed();
        }
    }
}
