package de.grafelhaft.winespray.app.fragment;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.regex.Pattern;

import de.grafelhaft.grafellib.fragment.BaseFragment;
import de.grafelhaft.winespray.app.BaseActivity;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.dialog.OnDialogCallbackListener;
import de.grafelhaft.winespray.app.dialog.RenameDialog;
import de.grafelhaft.winespray.app.util.IntentUtils;
import de.grafelhaft.winespray.app.util.MathUtils;
import de.grafelhaft.winespray.model.Acre;
import de.grafelhaft.winespray.model.Area;
import de.grafelhaft.winespray.model.Grape;
import de.grafelhaft.winespray.model.Parcel;
import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Markus on 14.09.2016.
 */
public class ParcelDetailFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {

    private Parcel _parcel;

    private EditText _editAreaSize;
    private Spinner _spinGrape, _spinAcre;

    public static ParcelDetailFragment create(Parcel parcel) {
        ParcelDetailFragment fragment = new ParcelDetailFragment();
        fragment._parcel = parcel;
        return fragment;
    }

    @Override
    protected int inflateLayoutRes() {
        return R.layout.fragment_parcel_detail;
    }

    @Override
    protected void init(Bundle bundle) {
        setHasOptionsMenu(true);

        _editAreaSize = (EditText) findViewById(R.id.text_area);

        _spinGrape = (Spinner) findViewById(R.id.spinner_grape);
        _spinAcre = (Spinner) findViewById(R.id.spinner_acre);

        if (_parcel == null) {
            _parcel = new Parcel();
            RealmHelper.setAutoId(_parcel, Parcel.class);

            RenameDialog dialog = RenameDialog.create(_parcel.toString(), Pattern.compile("^\\d*[/]?\\d?$"));
            dialog.setOnNameChangedListener(new RenameDialog.OnNameChangedListener() {
                @Override
                public void onNameChanged(final String name) {
                    String[] split = name.split("/");
                    if (split.length > 1) {
                        try {
                            _parcel.setNumerator(Long.parseLong(split[0]));
                            _parcel.setDenominator(Integer.parseInt(split[1]));
                        } catch (NumberFormatException e) {}
                    } else {
                        try {
                            _parcel.setNumerator(Long.parseLong(name));
                            _parcel.setDenominator(0);
                        } catch (NumberFormatException e) {}
                    }
                    ((BaseActivity)getActivity()).getSupportActionBar().setTitle(_parcel.toString());
                }
            });
            dialog.setOnDialogCallbackListener(new OnDialogCallbackListener() {
                @Override
                public void onCallback(boolean positive, boolean negative, boolean neutral) {
                    if (negative) {
                        getActivity().finish();
                    }
                }
            });
            dialog.show(getActivity().getSupportFragmentManager(), "RENAME_DIALOG");
        }

        bind(_parcel);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit_save, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            RenameDialog dialog = RenameDialog.create(_parcel.toString(), Pattern.compile("^\\d*[/]?\\d?$"));
            dialog.setOnNameChangedListener(new RenameDialog.OnNameChangedListener() {
                @Override
                public void onNameChanged(final String name) {
                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            String[] split = name.split("/");
                            if (split.length > 1) {
                                try {
                                    _parcel.setNumerator(Long.parseLong(split[0]));
                                    _parcel.setDenominator(Integer.parseInt(split[1]));
                                } catch (NumberFormatException e) {}
                            } else {
                                try {
                                    _parcel.setNumerator(Long.parseLong(name));
                                    _parcel.setDenominator(0);
                                } catch (NumberFormatException e) {}
                            }
                            ((BaseActivity)getActivity()).getSupportActionBar().setTitle(_parcel.toString());
                        }
                    });
                }
            });
            dialog.show(getActivity().getSupportFragmentManager(), "RENAME_DIALOG");
            return true;
        }
        if (item.getItemId() == R.id.action_save) {
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try {
                        double area = Double.parseDouble(_editAreaSize.getText().toString());

                        Area parcelArea;
                        if (_parcel.getArea() != null) {
                            parcelArea = _parcel.getArea();
                        } else {
                            parcelArea = new Area();
                        }
                        parcelArea.setSize(area*10000);
                        parcelArea.setSizeUseful(area*10000);

                        _parcel.setArea(parcelArea);
                    } catch (NumberFormatException e) {

                    }

                    Grape grape = (Grape) _spinGrape.getItemAtPosition(_spinGrape.getSelectedItemPosition());
                    _parcel.setGrape(grape);

                    Acre acre;
                    try {
                        acre = realm.where(Acre.class).equalTo("parcels.id", _parcel.getId()).findAll().first();
                        acre.getParcels().remove(_parcel);
                    } catch (IndexOutOfBoundsException e) {}
                    acre = (Acre) _spinAcre.getItemAtPosition(_spinAcre.getSelectedItemPosition());
                    acre.getParcels().add(_parcel);

                    acre = realm.copyToRealmOrUpdate(acre);
                    //_parcel = realm.copyToRealmOrUpdate(_parcel);
                }
            });
            if (!((BaseActivity) getActivity()).isTwoPane()) {
                getActivity().finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void bind(Parcel parcel) {
        if (parcel.getArea() != null && parcel.getArea().getSize() > 0) {
            _editAreaSize.setText(MathUtils.format2Decimals(parcel.getArea().getSize()/10000, 4));
        }

        RealmResults<Grape> grapes = Realm.getDefaultInstance().where(Grape.class).findAll().sort("name", Sort.ASCENDING);
        ArrayAdapter<Grape> grapeAdapter = new ArrayAdapter<Grape>(getActivity(), R.layout.list_item_spinner, grapes);
        grapeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spinGrape.setAdapter(grapeAdapter);
        _spinGrape.setOnItemSelectedListener(this);

        RealmResults<Acre> acres = Realm.getDefaultInstance().where(Acre.class).findAll().sort("id", Sort.ASCENDING);
        ArrayAdapter<Acre> acreAdapter = new ArrayAdapter<Acre>(getActivity(), R.layout.list_item_spinner, acres);
        acreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spinAcre.setAdapter(acreAdapter);
        _spinAcre.setOnItemSelectedListener(this);

        long acreId = getActivity().getIntent().getLongExtra(IntentUtils.EXTRA_ACRE_ID, -1);
        if (acreId >= 0) {
            Acre acre = (Acre) RealmHelper.findWhereId(Acre.class, acreId);
            int position = acreAdapter.getPosition(acre);
            _spinAcre.setSelection(position);
        } else {
            try {
                Acre acre = acres.where().equalTo("parcels.id", _parcel.getId()).findFirst();
                int position = acreAdapter.getPosition(acre);
                _spinAcre.setSelection(position);
            } catch (IndexOutOfBoundsException e) {}
        }

        if (parcel.getGrape() != null) {
            int position = grapeAdapter.getPosition(parcel.getGrape());
            _spinGrape.setSelection(position);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
