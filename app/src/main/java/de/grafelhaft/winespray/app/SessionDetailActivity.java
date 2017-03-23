package de.grafelhaft.winespray.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import de.grafelhaft.grafellib.util.TimeUtils;
import de.grafelhaft.winespray.app.dialog.DeleteRealmObjectDialog;
import de.grafelhaft.winespray.app.dialog.OnDialogCallbackListener;
import de.grafelhaft.winespray.app.dialog.RenameDialog;
import de.grafelhaft.winespray.app.fragment.SessionDetailFragment;
import de.grafelhaft.winespray.app.util.IntentUtils;
import de.grafelhaft.winespray.app.util.ResourceUtils;
import de.grafelhaft.winespray.model.Session;
import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.Realm;

public class SessionDetailActivity extends BaseActivity implements View.OnClickListener {

    private Session _session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_detail);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_session_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        if (item.getItemId() == R.id.action_delete) {
            DeleteRealmObjectDialog dialog = DeleteRealmObjectDialog.create(_session);
            dialog.setOnDialogCallbackListener(new OnDialogCallbackListener() {
                @Override
                public void onCallback(boolean positive, boolean negative, boolean neutral) {
                    if (positive) {
                        finish();
                    }
                }
            });
            dialog.show(getSupportFragmentManager(), "DELETE_DIALOG");
        }

        if (item.getItemId() == R.id.action_edit) {
            if (_session != null) {
                RenameDialog dialog = RenameDialog.create(_session.getName());
                dialog.setOnNameChangedListener(new RenameDialog.OnNameChangedListener() {
                    @Override
                    public void onNameChanged(final String name) {
                        if (_session != null) {
                            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    _session.setName(name);
                                }
                            });
                            CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
                            toolbarLayout.setTitle(name);
                        }
                    }
                });
                dialog.show(getSupportFragmentManager(), "RENAME_DIALOG");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void init(Bundle savedInstanceState) {
        long sessionId;
        if (_session == null && savedInstanceState != null) {
            sessionId = savedInstanceState.getLong(IntentUtils.EXTRA_SESSION_ID);
        } else {
            sessionId = getIntent().getLongExtra(IntentUtils.EXTRA_SESSION_ID, -1);
        }

        if (sessionId >= 0) {
            _session = (Session) RealmHelper.findWhereId(Session.class, sessionId);

            String title;
            if (_session.getName() == null) {
                title = TimeUtils.formatDate(
                        TimeUtils.convertTime(_session.getStartTime()),
                        "dd.MMMyyyy HH:mm"
                );
            } else {
                title = _session.getName();
            }

            getSupportActionBar().setTitle(title);

            ((ImageView) findViewById(R.id.toolbar_image)).setImageResource(ResourceUtils.getVineyardRes(sessionId));

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SessionDetailFragment.create(_session))
                    .commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                startActivity(new Intent(this, ActiveRunActivity.class)
                .putExtra(IntentUtils.EXTRA_SESSION_ID, _session.getId()));
                break;
        }
    }
}
