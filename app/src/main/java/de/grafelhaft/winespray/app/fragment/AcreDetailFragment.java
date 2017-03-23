package de.grafelhaft.winespray.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemClickListener;
import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemLongClickListener;
import de.grafelhaft.grafellib.fragment.BaseFragment;
import de.grafelhaft.winespray.app.AcreRecordActivity;
import de.grafelhaft.winespray.app.BaseActivity;
import de.grafelhaft.winespray.app.ParcelDetailActivity;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.RunDetailActivity;
import de.grafelhaft.winespray.app.adapter.realm.ParcelRealmRecyclerViewAdapter;
import de.grafelhaft.winespray.app.adapter.realm.RunRealmRecyclerViewAdapter;
import de.grafelhaft.winespray.app.dialog.DeleteRealmObjectDialog;
import de.grafelhaft.winespray.app.dialog.RenameDialog;
import de.grafelhaft.winespray.app.util.IntentUtils;
import de.grafelhaft.winespray.app.util.MathUtils;
import de.grafelhaft.winespray.model.Acre;
import de.grafelhaft.winespray.model.Area;
import de.grafelhaft.winespray.model.District;
import de.grafelhaft.winespray.model.Location;
import de.grafelhaft.winespray.model.Parcel;
import de.grafelhaft.winespray.model.Run;
import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Markus on 14.09.2016.
 */
public class AcreDetailFragment extends BaseFragment implements OnRecyclerViewItemClickListener, OnRecyclerViewItemLongClickListener {

    private Acre _acre;

    private RecyclerView _recyclerView;
    private ParcelRealmRecyclerViewAdapter _adapter;

    private EditText _editArea, _editAreaUseful;
    private Spinner _spinnerDistrict;

    public static AcreDetailFragment create(Acre acre) {
        AcreDetailFragment fragment = new AcreDetailFragment();
        fragment._acre = acre;
        return fragment;
    }

    @Override
    protected int inflateLayoutRes() {
        return R.layout.fragment_acre_detail;
    }

    @Override
    protected void init(Bundle bundle) {
        setHasOptionsMenu(true);

        if (_acre == null && bundle != null) {
            long id = bundle.getLong(IntentUtils.EXTRA_SESSION_ID);
            _acre = (Acre) RealmHelper.findWhereId(Acre.class, id);
        }

        if (_acre != null && _acre.isManaged()) {
            _adapter = new ParcelRealmRecyclerViewAdapter(getActivity(), (OrderedRealmCollection<Parcel>) _acre.getParcels(), true);
            _adapter.setOnRecyclerViewItemClickListener(this);
            _adapter.setOnRecyclerViewItemLongClickListener(this);

            _recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
            _recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            _recyclerView.setHasFixedSize(true);
            _recyclerView.setAdapter(_adapter);


            RealmResults<Run> runs = Realm.getDefaultInstance().where(Run.class).equalTo("acre.id", _acre.getId()).findAll();
            final RunRealmRecyclerViewAdapter runAdapter = new RunRealmRecyclerViewAdapter(getActivity(), runs, true);
            runAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClicked(View view, int i) {
                    Run run = runAdapter.getItem(i);
                    startActivity(new Intent(getActivity(), RunDetailActivity.class)
                            .putExtra(IntentUtils.EXTRA_RUN_ID, run.getId()));
                }
            });

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_2);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(runAdapter);
        }

        if (_acre == null) {
            getActivity().finish();
        }

        bind(_acre);
    }

    private void bind(final Acre acre) {
        _spinnerDistrict = (Spinner) findViewById(R.id.spinner_district);
        RealmResults<District> results = Realm.getDefaultInstance().where(District.class).findAll().sort("name", Sort.ASCENDING);
        ArrayAdapter<District> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_spinner, results);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spinnerDistrict.setAdapter(adapter);

        _editArea = (EditText) findViewById(R.id.text_area);
        _editAreaUseful = (EditText) findViewById(R.id.text_area_useful);

        if (acre.getArea() != null && acre.getArea().getSize() > 0) {
            _editArea.setText(MathUtils.format2Decimals(acre.getArea().getSize()/10000, 4));
        }

        if (acre.getArea() != null && acre.getArea().getSizeUseful() > 0) {
            _editAreaUseful.setText(MathUtils.format2Decimals(acre.getArea().getSizeUseful()/10000, 4));
        }

        if (acre.getDistrict() != null) {
            int position = adapter.getPosition(acre.getDistrict());
            _spinnerDistrict.setSelection(position);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_acre, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            RenameDialog dialog = RenameDialog.create(_acre.getName());
            dialog.setOnNameChangedListener(new RenameDialog.OnNameChangedListener() {
                @Override
                public void onNameChanged(final String name) {
                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            _acre.setName(name);
                            ((BaseActivity)getActivity()).getSupportActionBar().setTitle(_acre.toString());
                        }
                    });
                }
            });
            dialog.show(getActivity().getSupportFragmentManager(), "RENAME_DIALOG");
            return true;
        }
        if (item.getItemId() == R.id.action_save) {

            if (_acre != null && _acre.isValid()) {
                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        try {
                            _acre.getArea().setSize(Double.parseDouble(_editArea.getText().toString())*10000);
                        } catch (Exception e) {
                            _acre.getArea().setSize(0);
                        }

                        try {
                            _acre.getArea().setSizeUseful(Double.parseDouble(_editAreaUseful.getText().toString())*10000);
                        } catch (Exception e) {
                            _acre.getArea().setSizeUseful(0);
                        }

                        District district = (District) _spinnerDistrict.getItemAtPosition(_spinnerDistrict.getSelectedItemPosition());
                        _acre.setDistrict(district);

                        _acre = realm.copyToRealmOrUpdate(_acre);
                    }
                });
            }

            if (!((BaseActivity) getActivity()).isTwoPane()) {
                getActivity().finish();
            }
            return true;
        }
        if (item.getItemId() == R.id.action_delete) {
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    if (_acre.isManaged()) {
                        _acre.deleteFromRealm();
                    }
                }
            });
            getActivity().finish();
        }
        if (item.getItemId() == R.id.action_reset) {
            getActivity().onOptionsItemSelected(item);
        }
        if (item.getItemId() == R.id.action_record) {
            startActivity(new Intent(getActivity(), AcreRecordActivity.class)
            .putExtra(IntentUtils.EXTRA_ACRE_ID, _acre.getId()));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(IntentUtils.EXTRA_SESSION_ID, _acre.getId());
    }

    @Override
    public void onPause() {
        super.onPause();

        if (_acre != null && _acre.isValid()) {
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try {
                        _acre.getArea().setSize(Double.parseDouble(_editArea.getText().toString())*10000);
                    } catch (NumberFormatException e) {
                        _acre.getArea().setSize(0);
                    }

                    try {
                        _acre.getArea().setSizeUseful(Double.parseDouble(_editAreaUseful.getText().toString())*10000);
                    } catch (NumberFormatException e) {
                        _acre.getArea().setSizeUseful(0);
                    }

                    District district = (District) _spinnerDistrict.getItemAtPosition(_spinnerDistrict.getSelectedItemPosition());
                    _acre.setDistrict(district);

                    _acre = realm.copyToRealmOrUpdate(_acre);
                }
            });
        }
    }

    @Override
    public void onItemClicked(View view, int i) {
        Parcel parcel = _adapter.getItem(i);
        startActivity(new Intent(getActivity(), ParcelDetailActivity.class)
                .putExtra(IntentUtils.EXTRA_PARCEL_ID, parcel.getId()));
    }

    @Override
    public boolean onItemLongClicked(View view, int i) {
        DeleteRealmObjectDialog dialog = DeleteRealmObjectDialog.create(_adapter.getData().get(i));
        dialog.show(getActivity().getSupportFragmentManager(), "DELETE_DIALOG");
        return true;
    }

}
