package de.grafelhaft.winespray.app.viewmodel;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by @author Markus Graf (Grafelhaft) on 14.10.2016.
 */

public class ProfileCategory implements Parent<ProfileItem> {

    private String title;
    private List<ProfileItem> items;


    public ProfileCategory() {

    }


    public List<ProfileItem> getItems() {
        return items;
    }

    public ProfileCategory setItems(ProfileItem... items) {
        return setItems(Arrays.asList(items));
    }

    public ProfileCategory setItems(List<ProfileItem> items) {
        this.items = items;
        return this;
    }


    @Override
    public List<ProfileItem> getChildList() {
        return items;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return true;
    }


    public String getTitle() {
        return title;
    }

    public ProfileCategory setTitle(String title) {
        this.title = title;
        return this;
    }
}
