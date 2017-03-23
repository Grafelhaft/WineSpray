package de.grafelhaft.winespray.app.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemClickListener;
import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemLongClickListener;
import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemSelectedListener;
import de.grafelhaft.grafellib.fragment.BaseFragment;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.adapter.realm.NoteRealmRecyclerViewAdapter;
import de.grafelhaft.winespray.app.dialog.DeleteRealmObjectDialog;
import de.grafelhaft.winespray.app.dialog.MapDialog;
import de.grafelhaft.winespray.app.dialog.NoteDialog;
import de.grafelhaft.winespray.app.dialog.OnDialogCallbackListener;
import de.grafelhaft.winespray.model.Note;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Markus on 14.09.2016.
 */
public class NoteListFragment extends BaseFragment implements OnRecyclerViewItemClickListener, OnRecyclerViewItemSelectedListener, OnRecyclerViewItemLongClickListener, View.OnClickListener {


    private RecyclerView _recyclerView;
    private NoteRealmRecyclerViewAdapter _adapter;

    @Override
    protected int inflateLayoutRes() {
        return R.layout.fragment_note_list;
    }

    @Override
    protected void init(Bundle bundle) {
        setHasOptionsMenu(true);

        _adapter = new NoteRealmRecyclerViewAdapter(getActivity(), Realm.getDefaultInstance().where(Note.class).findAllAsync(), true);
        _adapter.setOnRecyclerViewItemClickListener(this);
        _adapter.addOnRecyclerViewItemSelectedListener(this);
        _adapter.setOnRecyclerViewItemLongClickListener(this);

        _recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        _recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        _recyclerView.setHasFixedSize(true);
        _recyclerView.setAdapter(_adapter);
        _recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .drawable(R.drawable.shape_divider_horizontal)
                .margin(getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin), getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin))
                .build());

        findViewById(R.id.fab).setOnClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_note_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            NoteDialog dialog = new NoteDialog();
            dialog.show(getActivity().getSupportFragmentManager(), "ADD_DIALOG");
        }
        if (item.getItemId() == R.id.action_clear) {
            Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Realm.getDefaultInstance().where(Note.class).equalTo("isChecked", true).findAll().deleteAllFromRealm();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(View view, int i) {
        final Note note = _adapter.getData().get(i);

        NoteDialog dialog = NoteDialog.create(note);
        dialog.setOnDialogCallbackListener(new OnDialogCallbackListener() {
            @Override
            public void onCallback(boolean positive, boolean negative, boolean neutral) {
                if (neutral) {
                    MapDialog mapDialog = MapDialog.create(note.getLocation());
                    mapDialog.show(getActivity().getSupportFragmentManager(), "MAP_DIALOG");
                }
            }
        });
        dialog.show(getActivity().getSupportFragmentManager(), "NOTE_DIALOG");
    }

    @Override
    public void onItemSelected(int position, final boolean selected) {
        final Note note = _adapter.getData().get(position);
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                note.setChecked(selected);
            }
        });
    }

    @Override
    public boolean onItemLongClicked(View view, int i) {
        Note note = _adapter.getData().get(i);
        DeleteRealmObjectDialog dialog = DeleteRealmObjectDialog.create(note);
        dialog.show(getActivity().getSupportFragmentManager(), "DELETE_DIALOG");
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                NoteDialog dialog = new NoteDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "ADD_DIALOG");
                break;
        }
    }
}
