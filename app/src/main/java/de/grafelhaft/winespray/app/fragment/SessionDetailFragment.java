package de.grafelhaft.winespray.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemClickListener;
import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemLongClickListener;
import de.grafelhaft.grafellib.fragment.BaseFragment;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.RunDetailActivity;
import de.grafelhaft.winespray.app.adapter.realm.RunRealmRecyclerViewAdapter;
import de.grafelhaft.winespray.app.dialog.DeleteRealmObjectDialog;
import de.grafelhaft.winespray.app.dialog.OnDialogCallbackListener;
import de.grafelhaft.winespray.app.util.IntentUtils;
import de.grafelhaft.winespray.model.Run;
import de.grafelhaft.winespray.model.Session;
import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.OrderedRealmCollection;

/**
 * Created by Markus on 14.09.2016.
 */
public class SessionDetailFragment extends BaseFragment implements OnRecyclerViewItemClickListener, OnRecyclerViewItemLongClickListener {

    private Session _session;

    private RecyclerView _recyclerView;
    private RunRealmRecyclerViewAdapter _adapter;

    public static SessionDetailFragment create(Session session) {
        SessionDetailFragment fragment = new SessionDetailFragment();
        fragment._session = session;
        return fragment;
    }

    @Override
    protected int inflateLayoutRes() {
        return R.layout.fragment_session_detail;
    }

    @Override
    protected void init(Bundle bundle) {
        //setHasOptionsMenu(true);

        if (_session == null && bundle != null) {
            long id = bundle.getLong(IntentUtils.EXTRA_SESSION_ID);
            _session = (Session) RealmHelper.findWhereId(Session.class, id);
        }

        if (_session != null) {

            _adapter = new RunRealmRecyclerViewAdapter(getActivity(), (OrderedRealmCollection<Run>) _session.getRuns(), true);
            _adapter.setOnRecyclerViewItemClickListener(this);
            _adapter.setOnRecyclerViewItemLongClickListener(this);

            _recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
            _recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            _recyclerView.setHasFixedSize(true);
            _recyclerView.setAdapter(_adapter);

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(IntentUtils.EXTRA_SESSION_ID, _session.getId());
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_session_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            DeleteRealmObjectDialog dialog = DeleteRealmObjectDialog.create(_session);
            dialog.setOnDialogCallbackListener(new OnDialogCallbackListener() {
                @Override
                public void onCallback(boolean positive, boolean negative, boolean neutral) {
                    if (positive) {
                        getActivity().finish();
                    }
                }
            });
            dialog.show(getActivity().getSupportFragmentManager(), "DELETE_DIALOG");
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
                            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(name);
                        }
                    }
                });
                dialog.show(getActivity().getSupportFragmentManager(), "RENAME_DIALOG");
            }
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onItemClicked(View view, int i) {
        Run run = _adapter.getItem(i);
        startActivity(new Intent(getActivity(), RunDetailActivity.class)
                .putExtra(IntentUtils.EXTRA_RUN_ID, run.getId()));
    }

    @Override
    public boolean onItemLongClicked(View view, int i) {
        Run run = _adapter.getItem(i);
        DeleteRealmObjectDialog dialog = DeleteRealmObjectDialog.create(run);
        dialog.show(getActivity().getSupportFragmentManager(), "DELETE_DIALOG");
        return true;
    }
}
