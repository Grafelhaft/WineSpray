package de.grafelhaft.winespray.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemClickListener;
import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemLongClickListener;
import de.grafelhaft.grafellib.fragment.BaseFragment;
import de.grafelhaft.grafellib.view.IFragmentTabIconResProvider;
import de.grafelhaft.winespray.app.AcreDetailActivity;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.adapter.realm.AcreRealmRecyclerViewAdapter;
import de.grafelhaft.winespray.app.dialog.DeleteRealmObjectDialog;
import de.grafelhaft.winespray.model.Acre;
import io.realm.Realm;

/**
 * Created by Markus on 14.09.2016.
 */
public class AcreListFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener,
        IFragmentTabIconResProvider,
        OnRecyclerViewItemClickListener,
        OnRecyclerViewItemLongClickListener {

    private RecyclerView _recyclerView;
    private SwipeRefreshLayout _refreshLayout;
    private AcreRealmRecyclerViewAdapter _adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            _onAcreClickListener = (OnAcreClickListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int inflateLayoutRes() {
        return R.layout.fragment_acre_list;
    }

    @Override
    protected void init(Bundle bundle) {
        _adapter = new AcreRealmRecyclerViewAdapter(getActivity(), Realm.getDefaultInstance().where(Acre.class).findAllAsync(), true);
        _adapter.setOnRecyclerViewItemClickListener(this);
        _adapter.setOnRecyclerViewItemLongClickListener(this);

        _refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        _refreshLayout.setOnRefreshListener(this);

        _recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        _recyclerView.setHasFixedSize(true);
        _recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        _recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .drawable(R.drawable.shape_divider_horizontal)
                .margin(getResources().getDimensionPixelSize(R.dimen.divider_margin), getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin))
                .build());
        _recyclerView.setAdapter(_adapter);
        _recyclerView.post(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        });
    }

    @Override
    public void onRefresh() {
        _refreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                startActivity(new Intent(getActivity(), AcreDetailActivity.class));
                break;
        }
    }

    @Override
    public int getFragmentTabIcon() {
        return R.drawable.ic_terrain_black_48dp;
    }


    /*LISTENER*/

    private OnAcreClickListener _onAcreClickListener;

    @Override
    public void onItemClicked(View view, int i) {
        Acre acre = _adapter.getData().get(i);
        notifyAcreClicked(acre);
    }

    @Override
    public boolean onItemLongClicked(View view, int i) {
        notifyAcreClicked(null);

        DeleteRealmObjectDialog dialog = DeleteRealmObjectDialog.create(_adapter.getData().get(i));
        dialog.show(getActivity().getSupportFragmentManager(), "DELETE_DIALOG");
        return true;
    }

    public interface OnAcreClickListener {
        void onAcreClicked(Acre acre);
    }

    public AcreListFragment addOnAcreClickListener(OnAcreClickListener l) {
        this._onAcreClickListener = l;
        return this;
    }

    public AcreListFragment removeOnAcreClickListener() {
        this._onAcreClickListener = null;
        return this;
    }

    private void notifyAcreClicked(Acre acre) {
        if (_onAcreClickListener != null) {
            _onAcreClickListener.onAcreClicked(acre);
        }
    }
}