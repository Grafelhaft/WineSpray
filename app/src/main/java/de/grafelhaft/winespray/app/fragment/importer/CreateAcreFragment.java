package de.grafelhaft.winespray.app.fragment.importer;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemClickListener;
import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemLongClickListener;
import de.grafelhaft.grafellib.fragment.BaseFragment;
import de.grafelhaft.grafellib.fragment.SetupFragment;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.adapter.AcreRecyclerViewAdapter;
import de.grafelhaft.winespray.app.dialog.DecisionDialog;
import de.grafelhaft.winespray.app.dialog.OnDialogCallbackListener;
import de.grafelhaft.winespray.app.dialog.RenameDialog;
import de.grafelhaft.winespray.app.fragment.AcreListFragment;
import de.grafelhaft.winespray.model.Acre;
import de.grafelhaft.winespray.model.realm.RealmHelper;

/**
 * Created by @author Markus Graf (Grafelhaft) on 21.10.2016.
 */

public class CreateAcreFragment extends SetupFragment implements Toolbar.OnMenuItemClickListener, View.OnClickListener, OnRecyclerViewItemClickListener, OnRecyclerViewItemLongClickListener {

    private RecyclerView _recyclerView;
    private AcreRecyclerViewAdapter _adapter;

    private List<Acre> _acres;

    private List<OnAcreCreatedListener> listeners = new ArrayList<>();

    @Override
    protected int inflateLayoutRes() {
        return R.layout.fragment_importer_acre_create;
    }

    @Override
    protected void init(Bundle bundle) {
        setRetainInstance(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_add);
        toolbar.setOnMenuItemClickListener(this);

        findViewById(R.id.button_done).setOnClickListener(this);

        _adapter = new AcreRecyclerViewAdapter(null);
        _adapter.setOnRecyclerViewItemClickListener(this);
        _adapter.setOnRecyclerViewItemLongClickListener(this);

        _recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        _recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        _recyclerView.setAdapter(_adapter);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            RenameDialog dialog = RenameDialog.create("");
            dialog.setOnNameChangedListener(new RenameDialog.OnNameChangedListener() {
                @Override
                public void onNameChanged(String name) {
                    Acre acre = new Acre();
                    acre.setName(name);

                    for (OnAcreCreatedListener l : listeners) {
                        l.onAcreCreated(acre);
                    }
                }
            });
            dialog.show(getActivity().getSupportFragmentManager(), "RENAME_DIALOG");
            return true;
        }
        return false;
    }

    public void setAcres(List<Acre> acres) {
        this._acres = acres;
        _adapter.update(acres);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_done:
                setReady(true);
                break;
        }
    }

    @Override
    public void onItemClicked(View view, int i) {
        Acre acre = _adapter.getData().get(i);
        for (OnAcreCreatedListener l : listeners) {
            l.onAcreClicked(acre);
        }
    }

    @Override
    public boolean onItemLongClicked(View view, int i) {
        final Acre acre = _adapter.getData().get(i);

        DecisionDialog dialog = DecisionDialog.create(getActivity(), R.string.dialog_title_delete, R.string.dialog_text_delete);
        dialog.setOnDialogCallbackListener(new OnDialogCallbackListener() {
            @Override
            public void onCallback(boolean positive, boolean negative, boolean neutral) {
                if (positive) {
                    _acres.remove(acre);
                    _adapter.update(_acres);

                    for (OnAcreCreatedListener l : listeners) {
                        l.onAcreDeleted(acre);
                    }
                }
            }
        });
        dialog.show(getActivity().getSupportFragmentManager(), "DELETE_DIALOG");
        return true;
    }

    public interface OnAcreCreatedListener {
        void onAcreCreated(Acre acre);
        void onAcreDeleted(Acre acre);
        void onAcreClicked(Acre acre);
    }

    public CreateAcreFragment addOnAcreCreatedListener(OnAcreCreatedListener l) {
        this.listeners.add(l);
        return this;
    }

    public CreateAcreFragment removeOnAcreCreatedListener(OnAcreCreatedListener l) {
        this.listeners.remove(l);
        return this;
    }

}
