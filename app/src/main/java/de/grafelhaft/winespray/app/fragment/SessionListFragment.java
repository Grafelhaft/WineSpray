package de.grafelhaft.winespray.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemClickListener;
import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemLongClickListener;
import de.grafelhaft.grafellib.fragment.BaseFragment;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.SessionDetailActivity;
import de.grafelhaft.winespray.app.adapter.realm.SessionRealmRecyclerViewAdapter;
import de.grafelhaft.winespray.app.dialog.DeleteRealmObjectDialog;
import de.grafelhaft.winespray.app.util.IntentUtils;
import de.grafelhaft.winespray.model.Session;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Markus on 14.09.2016.
 */
public class SessionListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnRecyclerViewItemClickListener, OnRecyclerViewItemLongClickListener {

    private RecyclerView _recyclerView;
    private SwipeRefreshLayout _refreshLayout;
    private SessionRealmRecyclerViewAdapter _adapter;

    @Override
    protected int inflateLayoutRes() {
        return R.layout.fragment_session_list;
    }

    @Override
    protected void init(Bundle bundle) {
        _adapter = new SessionRealmRecyclerViewAdapter(getActivity(), getData(), true);
        _adapter.setOnRecyclerViewItemClickListener(this);
        _adapter.setOnRecyclerViewItemLongClickListener(this);

        _refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        _refreshLayout.setOnRefreshListener(this);
        _refreshLayout.setRefreshing(false);

        _recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        _recyclerView.setHasFixedSize(true);
        _recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //_recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).colorResId(R.color.transparent).size(getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin)).build());
        _recyclerView.setAdapter(_adapter);
        _recyclerView.post(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        });

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SessionDetailActivity.class);

                View shared = findViewById(R.id.fab);
                String name = getString(R.string.transition_fab);

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), shared, name);
                startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public void onRefresh() {
        _adapter.updateData(getData());
        _refreshLayout.setRefreshing(false);
    }

    private RealmResults<Session> getData() {
        return Realm.getDefaultInstance().where(Session.class).findAllAsync().sort("startTime", Sort.DESCENDING);
    }

    @Override
    public void onItemClicked(View view, int i) {
        Session session = _adapter.getData().get(i);
        Intent intent = new Intent(getActivity(), SessionDetailActivity.class)
                .putExtra(IntentUtils.EXTRA_SESSION_ID, session.getId());

        Pair<View, String> fab = Pair.create(findViewById(R.id.fab), getString(R.string.transition_fab));
        Pair<View, String> image = Pair.create(findViewById(R.id.image), getString(R.string.transition_image));

        //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), image);

        startActivity(intent);
    }

    @Override
    public boolean onItemLongClicked(View view, int i) {
        DeleteRealmObjectDialog dialog = DeleteRealmObjectDialog.create(_adapter.getData().get(i));
        dialog.show(getActivity().getSupportFragmentManager(), "DELETE_DIALOG");
        return true;
    }
}
