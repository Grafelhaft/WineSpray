package de.grafelhaft.winespray.app.fragment.importer;


import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemClickListener;
import de.grafelhaft.grafellib.fragment.SetupFragment;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.adapter.PairRecyclerViewAdapter;
import de.grafelhaft.winespray.app.util.FileUtils;
import de.grafelhaft.winespray.model.parser.DataSet;
import de.grafelhaft.winespray.model.parser.ParserException;
import de.grafelhaft.winespray.model.parser.ParserFactory;
import de.grafelhaft.winespray.model.parser.ParserVersion;
import de.grafelhaft.winespray.model.parser.WipLwkRlpParser;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectSourceFragment extends SetupFragment implements OnRecyclerViewItemClickListener {

    private RecyclerView _recyclerView;
    private PairRecyclerViewAdapter _adapter;

    private OnDataParsedListener _listener;

    private File[] _files;

    public SelectSourceFragment() {
        // Required empty public constructor
    }

    @Override
    protected int inflateLayoutRes() {
        return R.layout.fragment_importer_select_source;
    }

    @Override
    protected void init(Bundle bundle) {
        setRetainInstance(true);

        _files = FileUtils.listFiles(FileUtils.PATH_IMPORT);

        List<Pair<String,String>> items = new ArrayList<>();
        for (File f : _files) {
            if (/*isFileValid(f.getName())*/true) {
                items.add(new Pair<>(f.getName(), getString(R.string.action_open)));
            }
        }

        _adapter = new PairRecyclerViewAdapter(items);
        _adapter.setOnRecyclerViewItemClickListener(this);

        _recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        _recyclerView.setHasFixedSize(true);
        _recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).drawable(R.drawable.shape_divider_horizontal).build());
        _recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        _recyclerView.setAdapter(_adapter);

        findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

    }

    public static boolean isFileValid(String name) {
        return  Pattern.matches("^.*\\.(txt|TXT|csv|CSV)$", name);
    }

    @Override
    public void onItemClicked(View view, int i) {
        File file = _files[i];

        String input = FileUtils.readFile(file.getPath());
        WipLwkRlpParser parser = (WipLwkRlpParser) ParserFactory.createParser(ParserVersion.WIP_LWK_RLP, input);
        DataSet dataSet;
        try {
            dataSet = parser.parse();
            if (_listener != null) {
                _listener.onParsed(dataSet);
            }
        } catch (ParserException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), getString(R.string.title_import_error), Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    public interface OnDataParsedListener {
        void onParsed(DataSet dataSet);
    }

    public SelectSourceFragment setOnDataParsedListener(OnDataParsedListener l) {
        this._listener = l;
        return this;
    }

    public SelectSourceFragment removeOnDataParsedListener() {
        this._listener = null;
        return this;
    }
}
