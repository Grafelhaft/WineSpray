package de.grafelhaft.winespray.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.view.Menu;
import android.view.MenuItem;

import com.luseen.spacenavigation.SpaceOnClickListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.List;

import de.grafelhaft.winespray.app.dialog.ExitDialog;
import de.grafelhaft.winespray.app.dialog.OnDialogCallbackListener;
import de.grafelhaft.winespray.app.fragment.MapFragment;
import de.grafelhaft.winespray.app.fragment.NoteListFragment;
import de.grafelhaft.winespray.app.fragment.ProfileFragment;
import de.grafelhaft.winespray.app.fragment.SessionListFragment;
import de.grafelhaft.winespray.model.Acre;
import de.grafelhaft.winespray.model.Note;
import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends BaseActivity implements SpaceOnClickListener, OnTabSelectListener {

    private Pair<Fragment, Integer>[] _fragments = new Pair[]{
            new Pair<Fragment, Integer>(new SessionListFragment(), R.string.title_fragment_session_list),
            new Pair<Fragment, Integer>(new MapFragment().enableMyLocation(true), R.string.title_fragment_map),
            new Pair<Fragment, Integer>(new NoteListFragment(), R.string.title_fragment_note_list),
            new Pair<Fragment, Integer>(new ProfileFragment(), R.string.title_fragment_profile),
    };

    private RealmChangeListener<RealmResults<Note>> _noteListener = new RealmChangeListener<RealmResults<Note>>() {
        @Override
        public void onChange(RealmResults<Note> element) {
            BottomBar bottomBar = (BottomBar) findViewById(R.id.bottombar);
            BottomBarTab notesTab = bottomBar.getTabWithId(R.id.tab_notes);
            //notesTab.setBadgeCount(element.where().equalTo("isChecked", false).findAll().size());
        }
    };

    @Override
    public void onBackPressed() {
        ExitDialog dialog = new ExitDialog();
        dialog.setOnDialogCallbackListener(new OnDialogCallbackListener() {
            @Override
            public void onCallback(boolean positive, boolean negative, boolean neutral) {
                if (positive) {
                    MainActivity.super.onBackPressed();
                }
            }
        });
        dialog.show(getSupportFragmentManager(), "EXIT_DIALOG");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottombar);
        bottomBar.setOnTabSelectListener(this);

        inflateFragment(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        RealmResults<Note> notes = Realm.getDefaultInstance().where(Note.class).findAll();
        notes.removeChangeListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        RealmResults<Note> notes = Realm.getDefaultInstance().where(Note.class).findAll();
        notes.addChangeListener(_noteListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCentreButtonClick() {
        //startActivity(new Intent(this, ActiveSessionActivity.class));
    }

    @Override
    public void onItemClick(int itemIndex, String itemName) {
        inflateFragment(itemIndex);
    }

    @Override
    public void onItemReselected(int itemIndex, String itemName) {

    }

    @Override
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId) {
            case R.id.tab_sessions:
                inflateFragment(0);
                break;
            case R.id.tab_map:
                inflateFragment(1);
                break;
            case R.id.tab_notes:
                inflateFragment(2);
                break;
            case R.id.tab_profile:
                inflateFragment(3);
                break;
        }
    }

    private void inflateFragment(int position) {
        if (position == 1) {
            RealmResults<Acre> acres = (RealmResults<Acre>) RealmHelper.findAll(Acre.class);
            MapFragment mapFragment = (MapFragment) _fragments[position].first;

            mapFragment.clear();
            for (Acre a : acres) {
                if (a.getArea() != null && a.getArea().getLocations().size() > 0) {
                    mapFragment.addPolygon(a);
                    mapFragment.addInfo(a.getArea().centroid(), a.getName());
                }
            }
            mapFragment.redraw();
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, _fragments[position].first);
        transaction.commit();

        getSupportActionBar().setTitle(_fragments[position].second);
    }


}
