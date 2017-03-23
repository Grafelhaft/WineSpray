package de.grafelhaft.winespray.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemClickListener;
import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemLongClickListener;
import de.grafelhaft.grafellib.fragment.BaseFragment;
import de.grafelhaft.grafellib.view.IFragmentTabIconResProvider;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.adapter.realm.GrapeRealmRecyclerViewAdapter;
import de.grafelhaft.winespray.app.dialog.AddGrapeDialog;
import de.grafelhaft.winespray.app.dialog.DeleteRealmObjectDialog;
import de.grafelhaft.winespray.model.Grape;
import io.realm.Realm;

/**
 * Created by Markus on 14.09.2016.
 */
public class GrapeListFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener,
        IFragmentTabIconResProvider,
        View.OnClickListener,
        OnRecyclerViewItemClickListener,
        OnRecyclerViewItemLongClickListener {

    private RecyclerView _recyclerView;
    private SwipeRefreshLayout _refreshLayout;
    private GrapeRealmRecyclerViewAdapter _adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            _onGrapeClickListener = (OnGrapeClickListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int inflateLayoutRes() {
        return R.layout.fragment_grape_list;
    }

    @Override
    protected void init(Bundle bundle) {
        _adapter = new GrapeRealmRecyclerViewAdapter(getActivity(), Realm.getDefaultInstance().where(Grape.class).findAllAsync(), true);
        _adapter.setOnRecyclerViewItemClickListener(this);
        _adapter.setOnRecyclerViewItemLongClickListener(this);

        _refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        _refreshLayout.setOnRefreshListener(this);
        _refreshLayout.setRefreshing(true);

        _recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        _recyclerView.setHasFixedSize(true);
        _recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        _recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .drawable(R.drawable.shape_divider_horizontal)
                .margin(getResources().getDimensionPixelSize(R.dimen.divider_margin), getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin))
                .build());
        _recyclerView.setAdapter(_adapter);
        _recyclerView.post(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    public void onRefresh() {
        _refreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                AddGrapeDialog dialog = new AddGrapeDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "ADD_DIALOG");
                break;
        }
    }

    @Override
    public int getFragmentTabIcon() {
        return R.drawable.ic_local_florist_black_48dp;
    }

    /*LISTENER*/

    private OnGrapeClickListener _onGrapeClickListener;

    @Override
    public void onItemClicked(View view, int i) {
        Grape grape = _adapter.getData().get(i);
        notifyFruitClicked(grape);
    }

    @Override
    public boolean onItemLongClicked(View view, int i) {
        DeleteRealmObjectDialog dialog = DeleteRealmObjectDialog.create(_adapter.getData().get(i));
        dialog.show(getActivity().getSupportFragmentManager(), "DELETE_DIALOG");
        return true;
    }

    public interface OnGrapeClickListener {
        void onGrapeClicked(Grape grape);
    }

    public GrapeListFragment addOnGrapeClickListener(OnGrapeClickListener l) {
        this._onGrapeClickListener = l;
        return this;
    }

    public GrapeListFragment removeOnGrapeClickListener() {
        this._onGrapeClickListener = null;
        return this;
    }

    private void notifyFruitClicked(Grape grape) {
        if (_onGrapeClickListener != null) {
            _onGrapeClickListener.onGrapeClicked(grape);
        }
    }
}
