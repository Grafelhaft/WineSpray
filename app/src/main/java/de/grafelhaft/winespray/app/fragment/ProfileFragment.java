package de.grafelhaft.winespray.app.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import de.grafelhaft.grafellib.adapter.OnRecyclerViewItemClickListener;
import de.grafelhaft.grafellib.fragment.BaseFragment;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.adapter.ProfileExpandableRecycleViewAdapter;
import de.grafelhaft.winespray.app.adapter.ProfileRecycleViewAdapter;
import de.grafelhaft.winespray.app.viewmodel.ProfileItem;
import de.grafelhaft.winespray.model.Company;
import io.realm.Realm;

/**
 * Created by Markus on 15.09.2016.
 */
public class ProfileFragment extends BaseFragment implements OnRecyclerViewItemClickListener {

    private RecyclerView _recyclerView;
    private ProfileRecycleViewAdapter _adapter;
    private EditText _editCompanyName, _editCompanyNumber;

    @Override
    protected int inflateLayoutRes() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void init(Bundle bundle) {

        _adapter = new ProfileRecycleViewAdapter(getActivity());
        _adapter.setOnRecyclerViewItemClickListener(this);

        _recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        _recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        _recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .drawable(R.drawable.shape_divider_horizontal)
                .margin(getResources().getDimensionPixelSize(R.dimen.divider_margin), getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin))
                .build());
        _recyclerView.setHasFixedSize(true);
        _recyclerView.setAdapter(_adapter);
    }

    @Override
    public void onItemClicked(View view, int i) {
        ProfileItem item = _adapter.getData(i);
        if (item.getIntent() != null) {
            startActivity(item.getIntent());
        }
    }
}
