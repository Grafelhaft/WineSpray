package de.grafelhaft.winespray.app.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.service.GpsLocationService;
import de.grafelhaft.winespray.model.Location;
import de.grafelhaft.winespray.model.Note;
import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.Realm;

/**
 * Created by Markus on 18.09.2016.
 */
public class NoteDialog extends ADialogFragment {

    private Note _note;

    private EditText _editNoteName;

    public static NoteDialog create(Note note) {
        NoteDialog dialog = new NoteDialog();
        dialog._note = note;
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_note, null);
        _editNoteName = (EditText) view.findViewById(R.id.edit_note_name);

        if (_note != null) {
            _editNoteName.append(_note.getContent());
        }

        builder.setView(view)
                .setPositiveButton(R.string.dialog_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (_note != null) {

                            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    _note.setContent(_editNoteName.getText().toString());
                                }
                            });

                        } else {

                            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    Note note = new Note().setContent(_editNoteName.getText().toString());
                                    RealmHelper.setAutoId(note, Note.class);
                                    note.setLocation(new Location(GpsLocationService.getInstance().getCurrentLocation()));
                                    realm.copyToRealmOrUpdate(note);
                                }
                            });

                        }

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

        if (_note != null) {
            builder.
                    setNeutralButton(R.string.title_fragment_map, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (_listener != null) {
                                _listener.onCallback(false, false, true);
                            }
                        }
                    });
        }
        return builder.create();
    }
}
