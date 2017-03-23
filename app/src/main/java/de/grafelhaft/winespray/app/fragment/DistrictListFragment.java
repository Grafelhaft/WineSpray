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
import de.grafelhaft.winespray.app.adapter.realm.DistrictRealmRecyclerViewAdapter;
import de.grafelhaft.winespray.app.dialog.AddGrapeDialog;
import de.grafelhaft.winespray.app.dialog.DeleteRealmObjectDialog;
import de.grafelhaft.winespray.model.District;
import de.grafelhaft.winespray.model.Grape;
import io.realm.Realm;

/**
 * Created by Markus on 14.09.2016.
 */
public class DistrictListFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener,
        IFragmentTabIconResProvider,
        View.OnClickListener,
        OnRecyclerViewItemClickListener,
        OnRecyclerViewItemLongClickListener {

    private RecyclerView _recyclerView;
    private SwipeRefreshLayout _refreshLayout;
    private DistrictRealmRecyclerViewAdapter _adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            _onDistrictClickListener = (OnDistrictClickListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int inflateLayoutRes() {
        return R.layout.fragment_district_list;
    }

    @Override
    protected void init(Bundle bundle) {
        _adapter = new DistrictRealmRecyclerViewAdapter(getActivity(), Realm.getDefaultInstance().where(District.class).findAllAsync(), true);
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

                break;
        }
    }

    @Override
    public int getFragmentTabIcon() {
        return R.drawable.ic_local_florist_black_48dp;
    }

    /*LISTENER*/

    private OnDistrictClickListener _onDistrictClickListener;

    @Override
    public void onItemClicked(View view, int i) {
        District district = _adapter.getData().get(i);
        notifyDistrictClicked(district);
    }

    @Override
    public boolean onItemLongClicked(View view, int i) {
        DeleteRealmObjectDialog dialog = DeleteRealmObjectDialog.create(_adapter.getData().get(i));
        dialog.show(getActivity().getSupportFragmentManager(), "DELETE_DIALOG");
        return true;
    }

    public interface OnDistrictClickListener {
        void onDistrictClicked(District district);
    }

    public DistrictListFragment addOnDistrictClickListener(OnDistrictClickListener l) {
        this._onDistrictClickListener = l;
        return this;
    }

    public DistrictListFragment removeOnDistrictClickListener() {
        this._onDistrictClickListener = null;
        return this;
    }

    private void notifyDistrictClicked(District district) {
        if (_onDistrictClickListener != null) {
            _onDistrictClickListener.onDistrictClicked(district);
        }
    }
}
