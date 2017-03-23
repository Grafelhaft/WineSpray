package de.grafelhaft.winespray.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.grafelhaft.grafellib.adapter.ARecyclerViewAdapter;
import de.grafelhaft.winespray.app.AboutActivity;
import de.grafelhaft.winespray.app.AcreListActivity;
import de.grafelhaft.winespray.app.DistrictListActivity;
import de.grafelhaft.winespray.app.GrapeListActivity;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.viewmodel.ProfileItem;
import de.grafelhaft.winespray.app.viewholder.ProfileViewHolder;

/**
 * Created by Markus on 18.09.2016.
 */
public class ProfileRecycleViewAdapter extends ARecyclerViewAdapter<ProfileItem, ProfileViewHolder> {

    public ProfileRecycleViewAdapter(Context context) {
        getData().add(new ProfileItem(context.getString(R.string.profile_acres), new Intent(context, AcreListActivity.class)).setIconRes(R.drawable.ic_terrain_white_48dp));
        //getData().add(new ProfileItem(context.getString(R.string.profile_districts), new Intent(context, DistrictListActivity.class)).setIconRes(R.drawable.ic_place_white_24dp));
        getData().add(new ProfileItem(context.getString(R.string.profile_grapes), new Intent(context, GrapeListActivity.class)).setIconRes(R.drawable.ic_grape_white_24dp));
        //getData().add(new ProfileItem(context.getString(R.string.profile_sprays), null).setIconRes(R.drawable.ic_cup_water_white_48dp));
        getData().add(new ProfileItem(context.getString(R.string.profile_about), new Intent(context, AboutActivity.class)).setIconRes(R.drawable.ic_info_white_24dp));
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_profile, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProfileViewHolder holder, int position) {
        ProfileItem item = getData().get(position);
        holder.bind(item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemClicked(v, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }

    public ProfileItem getData(int position) {
        return getData().get(position);
    }
}
