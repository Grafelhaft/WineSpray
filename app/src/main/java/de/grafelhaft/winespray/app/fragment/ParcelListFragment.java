package de.grafelhaft.winespray.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemClickListener;
import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemLongClickListener;
import de.grafelhaft.grafellib.fragment.BaseFragment;
import de.grafelhaft.grafellib.view.IFragmentTabIconResProvider;
import de.grafelhaft.winespray.app.ParcelDetailActivity;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.adapter.realm.ParcelRealmRecyclerViewAdapter;
import de.grafelhaft.winespray.app.dialog.DeleteRealmObjectDialog;
import de.grafelhaft.winespray.model.Acre;
import de.grafelhaft.winespray.model.Parcel;
import io.realm.Realm;

/**
 * Created by Markus on 14.09.2016.
 */
public class ParcelListFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener,
        IFragmentTabIconResProvider,
        OnRecyclerViewItemClickListener,
        OnRecyclerViewItemLongClickListener {

    private RecyclerView _recyclerView;
    private SwipeRefreshLayout _refreshLayout;
    private ParcelRealmRecyclerViewAdapter _adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            _onParcelClickListener = (OnParcelClickListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int inflateLayoutRes() {
        return R.layout.fragment_parcel_list;
    }

    @Override
    protected void init(Bundle bundle) {
        _adapter = new ParcelRealmRecyclerViewAdapter(getActivity(), Realm.getDefaultInstance().where(Parcel.class).findAllAsync(), true);
        _adapter.setOnRecyclerViewItemClickListener(this);
        _adapter.setOnRecyclerViewItemLongClickListener(this);

        _refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        _refreshLayout.setOnRefreshListener(this);

        _recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        _recyclerView.setHasFixedSize(true);
        _recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
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
                startActivity(new Intent(getActivity(), ParcelDetailActivity.class));
                break;
        }
    }

    @Override
    public int getFragmentTabIcon() {
        return R.drawable.ic_terrain_black_48dp;
    }


    /*LISTENER*/

    private OnParcelClickListener _onParcelClickListener;

    @Override
    public void onItemClicked(View view, int i) {
        Parcel parcel = _adapter.getData().get(i);
        notifyParcelClicked(parcel);
    }

    @Override
    public boolean onItemLongClicked(View view, int i) {
        DeleteRealmObjectDialog dialog = DeleteRealmObjectDialog.create(_adapter.getData().get(i));
        dialog.show(getActivity().getSupportFragmentManager(), "DELETE_DIALOG");
        return true;
    }

    public interface OnParcelClickListener {
        void onParcelClicked(Parcel parcel);
    }

    public ParcelListFragment addOnParcelClickListener(OnParcelClickListener l) {
        this._onParcelClickListener = l;
        return this;
    }

    public ParcelListFragment removeOnParcelClickListener() {
        this._onParcelClickListener = null;
        return this;
    }

    private void notifyParcelClicked(Parcel parcel) {
        if (_onParcelClickListener != null) {
            _onParcelClickListener.onParcelClicked(parcel);
        }
    }
}