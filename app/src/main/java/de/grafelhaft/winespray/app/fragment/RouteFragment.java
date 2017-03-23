package de.grafelhaft.winespray.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import de.grafelhaft.grafellib.view.IFragmentTabTitleResProvider;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.controller.RunController;
import de.grafelhaft.winespray.app.service.GpsLocationService;
import de.grafelhaft.winespray.model.DataPoint;
import de.grafelhaft.winespray.model.Run;

/**
 * Created by @author Markus Graf.
 */
public class RouteFragment extends SupportMapFragment implements OnMapReadyCallback, DataPoint.OnDataPointAddedListener, IFragmentTabTitleResProvider {

    private boolean _isMapReady;
    private GoogleMap _map;

    private Run _run;

    public static RouteFragment create(Run run) {
        RouteFragment fragment = new RouteFragment();
        if (run != null && run.getDataPoints().size() > 0) {
            fragment._run = run;
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        init();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        GpsLocationService.getInstance().stop();
        RunController.getInstance().removeOnDataPointAddedListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.getMapAsync(this);
    }

    private void init() {
        GpsLocationService.getInstance().start(getActivity());

        RunController.getInstance().addOnDataPointAddedListener(this);

        //TODO
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkRequest.Builder request = new NetworkRequest.Builder();
            request.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);

            connectivityManager.registerNetworkCallback(request.build(), new ConnectivityManager.NetworkCallback() {

                @Override
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    try {
                        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                            connectivityManager.setProcessDefaultNetwork(network);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            connectivityManager.bindProcessToNetwork(network);
                        }
                    }
                    catch (IllegalStateException e) {
                        //Fuck this shit
                    }
                }
            });
        }*/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this._isMapReady = true;
        this._map = googleMap;

        this._map.getUiSettings().setMyLocationButtonEnabled(true);
        this._map.getUiSettings().setZoomControlsEnabled(true);
        this._map.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        if (_run != null) {
            if (_track == null) {
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(getResources().getColor(R.color.colorAccent));
                _track = this._map.addPolyline(polylineOptions);
            }

            List<LatLng> points = _track.getPoints();

            List<DataPoint> dataPoints = _run.getDataPoints();
            for (DataPoint d : dataPoints) {
                if (d.getLocation() != null) {
                    points.add(
                            new LatLng(d.getLocation().getLatitude(),
                                    d.getLocation().getLongitude())
                    );
                }
            }
            _track.setPoints(points);

            if (_run.getLastAddedDataPoint() != null && _run.getLastAddedDataPoint().getLocation() != null) {
                LatLng currentPosition = new LatLng(_run.getLastAddedDataPoint().getLocation().getLatitude(), _run.getLastAddedDataPoint().getLocation().getLongitude());
                this._map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 20.0f));
            }

        } else {
            this._map.setMyLocationEnabled(true);

            LatLng currentPosition = new LatLng(
                    GpsLocationService.getInstance().getCurrentLocation().getLatitude(),
                    GpsLocationService.getInstance().getCurrentLocation().getLongitude()
            );

            this._map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 20.0f));
        }
    }

    private Polyline _track;

    @Override
    public void onDataPointAdded(long runId, DataPoint data) {
        if (this._isMapReady) {
            if (_track == null) {
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(getResources().getColor(R.color.colorAccent));
                _track = this._map.addPolyline(polylineOptions);
            }

            if (data.getLocation() != null) {
                List<LatLng> points = _track.getPoints();
                points.add(new LatLng(data.getLocation().getLatitude(), data.getLocation().getLongitude()));
                _track.setPoints(points);

                LatLng currentPosition = new LatLng(data.getLocation().getLatitude(), data.getLocation().getLongitude());
                this._map.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
            }
        }
    }

    @Override
    public int getFragmentTabTitle() {
        return R.string.title_fragment_map;
    }
}
