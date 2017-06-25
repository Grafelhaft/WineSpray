package de.grafelhaft.winespray.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import de.grafelhaft.winespray.app.controller.RunController;
import de.grafelhaft.winespray.app.fragment.RunListFragment;
import de.grafelhaft.winespray.app.util.IntentUtils;
import de.grafelhaft.winespray.model.Session;
import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.Realm;

@Deprecated
public class ActiveSessionActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_SESSION_ACTIVE = 100;

    private Session _session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_active);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        init(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (_session != null) {
            outState.putLong(IntentUtils.EXTRA_SESSION_ID, _session.getId());
        }
    }

    private void init(Bundle savedInstanceState) {
        long id = -1;
        if (savedInstanceState != null) {
            id = savedInstanceState.getLong(IntentUtils.EXTRA_SESSION_ID, -1);
        } else {
            id = getIntent().getLongExtra(IntentUtils.EXTRA_SESSION_ID, -1);
        }
        if (id >= 0) {
            _session = (Session) RealmHelper.findWhereId(Session.class, id);
        }

        if (_session == null) {
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    _session = new Session();
                    RealmHelper.setAutoId(_session, Session.class);
                    _session.setName(getString(R.string.session));
                    _session.setStartTime(System.currentTimeMillis());
                    _session = realm.copyToRealmOrUpdate(_session);
                }
            });
        }

        inflateFragment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                startActivityForResult(new Intent(this, ActiveRunActivity.class)
                        .putExtra(IntentUtils.EXTRA_SESSION_ID, _session.getId()), REQUEST_SESSION_ACTIVE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SESSION_ACTIVE:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (_session.getRuns().size() <= 0) {
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    _session.deleteFromRealm();
                }
            });
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

    private void inflateFragment() {
        RunListFragment fragment = RunListFragment.create(_session);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}
