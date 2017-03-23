package de.grafelhaft.winespray.app.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.model.Location;

/**
 * Created by @author Markus Graf (Grafelhaft) on 22.10.2016.
 */

public class MapDialog extends ADialogFragment {

    private LatLng _position;

    public static MapDialog create(Location location) {
        return create(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    public static MapDialog create(LatLng position) {
        MapDialog dialog = new MapDialog();
        dialog._position = position;
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_map, null);
        builder.setView(view)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (_listener != null) {
                            _listener.onCallback(true, false, false);
                        }
                        dismiss();
                    }
                });
        onViewCreated(view, savedInstanceState);
        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.remove(mapFragment);
        transaction.commit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.addMarker(new MarkerOptions()
                .position(_position));

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(_position, 15f));
            }
        });
    }
}
