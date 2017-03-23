package de.grafelhaft.winespray.app.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.io.File;

import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.importer.ParserToDb;
import de.grafelhaft.winespray.app.util.FileUtils;
import de.grafelhaft.winespray.model.parser.DataSet;
import de.grafelhaft.winespray.model.parser.ParserException;
import de.grafelhaft.winespray.model.parser.ParserFactory;
import de.grafelhaft.winespray.model.parser.ParserVersion;
import de.grafelhaft.winespray.model.parser.WipLwkRlpParser;

/**
 * Created by @author Markus Graf (Grafelhaft) on 16.10.2016.
 */

public class ImportFileListDialog extends ADialogFragment {

    private String _path;
    private int _selectedItem;

    public static ImportFileListDialog create(String path) {
        ImportFileListDialog dialog = new ImportFileListDialog();
        dialog._path = path;
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final File[] files = FileUtils.listFiles(_path);
        String[] filesNames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            filesNames[i] = files[i].getName();
        }

        builder.setTitle("Files")
                .setSingleChoiceItems(filesNames, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        _selectedItem = which;
                    }
                })
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String input = FileUtils.readFile(files[_selectedItem].getPath());
                        WipLwkRlpParser parser = (WipLwkRlpParser) ParserFactory.createParser(ParserVersion.WIP_LWK_RLP, input);
                        assert parser != null;
                        DataSet dataSet = null;
                        try {
                            dataSet = parser.parse();
                        } catch (ParserException e) {
                            e.printStackTrace();
                        }
                        ParserToDb.save(dataSet);

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
