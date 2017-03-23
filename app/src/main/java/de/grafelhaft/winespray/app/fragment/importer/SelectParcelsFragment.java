package de.grafelhaft.winespray.app.fragment.importer;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Iterator;
import java.util.List;

import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemSelectedListener;
import de.grafelhaft.grafellib.fragment.SetupFragment;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.adapter.SelectParcelRecyclerViewAdapter;
import de.grafelhaft.winespray.model.Acre;
import de.grafelhaft.winespray.model.Parcel;

/**
 * Created by @author Markus Graf (Grafelhaft) on 21.10.2016.
 */

public class SelectParcelsFragment extends SetupFragment implements OnRecyclerViewItemSelectedListener, View.OnClickListener {

    private Acre _acre;
    private List<Parcel> _parcels;

    private RecyclerView _recyclerView;
    private SelectParcelRecyclerViewAdapter _adapter;

    public static SelectParcelsFragment create(Acre acre, List<Parcel> parcels) {
        SelectParcelsFragment fragment = new SelectParcelsFragment();
        fragment._acre = acre;
        fragment._parcels = parcels;
        return fragment;
    }

    @Override
    protected int inflateLayoutRes() {
        return R.layout.fragment_importer_select_parcel;
    }

    @Override
    protected void init(Bundle bundle) {
        if (_parcels != null) {
            _adapter = new SelectParcelRecyclerViewAdapter(_parcels);
            _adapter.addOnRecyclerViewItemSelectedListener(this);

            _recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
            _recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            _recyclerView.setAdapter(_adapter);
        }

        findViewById(R.id.button_done).setOnClickListener(this);
    }

    @Override
    public void onItemSelected(int position, boolean isSelected) {
        if (_acre != null) {
            Parcel parcel = _adapter.getData().get(position);
            if (isSelected) {
                if (!_acre.getParcels().contains(parcel)) {
                    _acre.getParcels().add(parcel);
                }
            } else {
                _acre.getParcels().remove(parcel);
            }
        }
    }

    public void setAcre(Acre acre) {
        this._acre = acre;
        if (_adapter != null & _recyclerView != null & _parcels != null) {
            _adapter.update(acre);
        }
    }

    public void setParcels(List<Parcel> parcels) {
        this._parcels = parcels;
        if (_adapter != null) {
            _adapter.update(parcels);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_done:
                _adapter.clearSelection();
                setReady(true);
                break;
        }
    }
}
