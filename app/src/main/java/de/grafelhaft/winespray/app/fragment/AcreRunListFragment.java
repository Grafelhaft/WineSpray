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
import de.grafelhaft.winespray.app.util.IntentUtils;
import de.grafelhaft.winespray.model.Acre;
import de.grafelhaft.winespray.model.Run;
import de.grafelhaft.winespray.model.Session;
import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * A placeholder fragment containing a simple view.
 */
public class AcreRunListFragment extends BaseFragment implements OnRecyclerViewItemClickListener, OnRecyclerViewItemLongClickListener {

    private Acre _acre;

    private RecyclerView _recyclerView;
    private RunRealmRecyclerViewAdapter _adapter;


    public static AcreRunListFragment create(Acre acre) {
        AcreRunListFragment fragment = new AcreRunListFragment();
        fragment._acre = acre;
        return fragment;
    }

    public AcreRunListFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(IntentUtils.EXTRA_ACRE_ID, _acre.getId());
    }

    @Override
    protected int inflateLayoutRes() {
        return R.layout.fragment_run_list;
    }

    @Override
    protected void init(Bundle bundle) {
        if (_acre == null && bundle != null) {
            Long id = bundle.getLong(IntentUtils.EXTRA_ACRE_ID);
            _acre = (Acre) RealmHelper.findWhereId(Acre.class, id);
        }

        if (_acre != null) {
            RealmResults<Run> runs = Realm.getDefaultInstance().where(Run.class).equalTo("acre.id", _acre.getId()).findAll();
            _adapter = new RunRealmRecyclerViewAdapter(getActivity(), runs, true);
            _adapter.setOnRecyclerViewItemClickListener(this);
            _adapter.setOnRecyclerViewItemLongClickListener(this);

            _recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
            _recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            _recyclerView.setHasFixedSize(true);
            _recyclerView.setAdapter(_adapter);
        }
    }

    @Override
    public void onItemClicked(View view, int i) {
        Run run = _adapter.getData().get(i);
        startActivity(new Intent(getActivity(), RunDetailActivity.class)
        .putExtra(IntentUtils.EXTRA_RUN_ID, run.getId()));
    }

    @Override
    public boolean onItemLongClicked(View view, int i) {
        /*Run run = _adapter.getItem(i);
        DeleteRealmObjectDialog dialog = DeleteRealmObjectDialog.create(run);
        dialog.show(getActivity().getSupportFragmentManager(), "DELETE_DIALOG");*/
        return true;
    }
}
