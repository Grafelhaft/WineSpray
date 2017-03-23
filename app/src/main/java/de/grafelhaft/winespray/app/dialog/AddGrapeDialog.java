package de.grafelhaft.winespray.app.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.model.Grape;
import de.grafelhaft.winespray.model.GrapeColor;
import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.Realm;

/**
 * Created by Markus on 18.09.2016.
 */
public class AddGrapeDialog extends ADialogFragment {

    private EditText _editFruitName;
    private Spinner _spinner;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_grape_add, null);
        _editFruitName = (EditText) view.findViewById(R.id.edit_fruit_name);
        _spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_spinner,
                new String[] { getString(R.string.grape_white), getString(R.string.grape_red) });
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spinner.setAdapter(adapter);

        builder.setView(view)
                .setPositiveButton(R.string.dialog_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Grape grape = new Grape()
                                        .setName(_editFruitName.getText().toString());
                                RealmHelper.setAutoId(grape, Grape.class);

                                String color = (String) _spinner.getItemAtPosition(_spinner.getSelectedItemPosition());
                                if (getString(R.string.grape_white).equals(color)) {
                                    grape.setColor(GrapeColor.WHITE);
                                } else
                                if (getString(R.string.grape_red).equals(color)) {
                                    grape.setColor(GrapeColor.RED);
                                }

                                realm.copyToRealmOrUpdate(grape);
                            }
                        });

                        if (_listener != null) {
                            _listener.onCallback(true, false, false);
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (_listener != null) {
                            _listener.onCallback(false, true, false);
                        }
                    }
                });
        return builder.create();
    }
}
