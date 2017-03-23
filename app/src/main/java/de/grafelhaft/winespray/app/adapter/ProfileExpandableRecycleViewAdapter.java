package de.grafelhaft.winespray.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import de.grafelhaft.winespray.app.AcreListActivity;
import de.grafelhaft.winespray.app.GrapeListActivity;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.viewholder.ProfileParentViewHolder;
import de.grafelhaft.winespray.app.viewholder.ProfileViewHolder;
import de.grafelhaft.winespray.app.viewmodel.ProfileCategory;
import de.grafelhaft.winespray.app.viewmodel.ProfileItem;

/**
 * Created by Markus on 18.09.2016.
 */
public class ProfileExpandableRecycleViewAdapter extends AExpandableRecyclerViewAdapter<ProfileCategory, ProfileItem, ProfileParentViewHolder, ProfileViewHolder> {

    private Context context;

    public ProfileExpandableRecycleViewAdapter(Context context) {
        super(Arrays.asList(
                new ProfileCategory().setItems(
                        new ProfileItem(context.getString(R.string.profile_acres), new Intent(context, AcreListActivity.class)).setIconRes(R.drawable.ic_terrain_white_48dp),
                        new ProfileItem(context.getString(R.string.profile_districts), null).setIconRes(R.drawable.ic_place_white_24dp),
                        new ProfileItem(context.getString(R.string.profile_grapes), new Intent(context, GrapeListActivity.class)).setIconRes(R.drawable.ic_grape_white_24dp),
                        new ProfileItem(context.getString(R.string.profile_sprays), null).setIconRes(R.drawable.ic_cup_water_white_48dp)
                ).setTitle("1")
        )
        );
        this.context = context;
    }

    @NonNull
    @Override
    public ProfileParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View view = LayoutInflater.from(parentViewGroup.getContext()).inflate(R.layout.list_header_profile, parentViewGroup, false);
        return new ProfileParentViewHolder(view);
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View view = LayoutInflater.from(childViewGroup.getContext()).inflate(R.layout.list_item_profile, childViewGroup, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(@NonNull ProfileParentViewHolder parentViewHolder, int parentPosition, @NonNull ProfileCategory parent) {
        parentViewHolder.bind(parent);
        parentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onBindChildViewHolder(@NonNull final ProfileViewHolder childViewHolder, final int parentPosition, int childPosition, @NonNull final ProfileItem child) {
        childViewHolder.bind(child);
        childViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (child.getIntent() != null) {
                    context.startActivity(child.getIntent());
                }
            }
        });
    }

}
