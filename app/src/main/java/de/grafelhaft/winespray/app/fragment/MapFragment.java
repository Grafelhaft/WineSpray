package de.grafelhaft.winespray.app.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.grafelhaft.grafellib.async.AAsyncTask;
import de.grafelhaft.grafellib.async.OnTaskOutputListener;
import de.grafelhaft.grafellib.view.IFragmentTabTitleResProvider;
import de.grafelhaft.winespray.acrecontrol.IPoint;
import de.grafelhaft.winespray.acrecontrol.Polygon;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.controller.RunController;
import de.grafelhaft.winespray.app.service.GpsLocationService;
import de.grafelhaft.winespray.app.util.MathUtils;
import de.grafelhaft.winespray.model.Acre;
import de.grafelhaft.winespray.model.DataPoint;
import de.grafelhaft.winespray.model.Location;
import de.grafelhaft.winespray.model.Run;
import de.grafelhaft.winespray.model.SensorPurpose;
import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.Realm;

/**
 * Created by Markus on 14.09.2016.
 */
public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, DataPoint.OnDataPointAddedListener, IFragmentTabTitleResProvider {

    private static final String DRAW_POLYGON = "DRAW_POLYGON";
    private static final String DRAW_ACTIVE = "DRAW_ACTIVE";
    private static final String DRAW_ROUTE = "DRAW_ROUTE";

    private static final float MAP_ZOOM = 17.5f;

    private boolean _isMapReady;
    private GoogleMap _map;

    private List<List<? extends IPoint>> _polygonList = new ArrayList<>();
    private List<List<? extends IPoint>> _routeList = new ArrayList<>();
    private List<List<DataPoint>> _heatList = new ArrayList<>();
    private List<Pair<Location, String>> _infoList = new ArrayList<>();

    private Polyline _track;

    private boolean _showZoomButtons = true;
    private boolean _enableZoomGesture = true;
    private boolean _enableMyLocation = false;
    private boolean _drawActiveRun = false;
    private int[] _padding = {
            0, 0, 0, 0
    };

    private int[] _heatColors = {
            Color.parseColor("#8BC34A"),
            Color.parseColor("#CDDC39"),
            Color.parseColor("#FF9800")
    };
    private float[] _startPoints = {
            0.2f, 0.6f, 1f
    };
    private Gradient _gradient = new Gradient(_heatColors, _startPoints);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);

        GpsLocationService.getInstance().start(getActivity());
        if (savedInstanceState != null) {
            this._drawActiveRun = savedInstanceState.getBoolean(DRAW_ACTIVE);
        }

        init();

        return view;
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
                    } catch (IllegalStateException e) {
                        //Fuck this shit
                    }
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (_isMapReady) {
            onMapReady(_map);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.getMapAsync(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        GpsLocationService.getInstance().stop();
        RunController.getInstance().removeOnDataPointAddedListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean(DRAW_ACTIVE, this._drawActiveRun);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this._isMapReady = true;
        this._map = googleMap;

        _map.getUiSettings().setZoomControlsEnabled(this._showZoomButtons);
        _map.getUiSettings().setMyLocationButtonEnabled(true);
        _map.getUiSettings().setZoomGesturesEnabled(this._enableZoomGesture);
        _map.setOnMyLocationButtonClickListener(this);
        _map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        LatLng position = new LatLng(
                GpsLocationService.getInstance().getCurrentLocation().getLatitude(),
                GpsLocationService.getInstance().getCurrentLocation().getLongitude()
        );
        moveCamera(position);

        //Draw the heatmaps
        if (_heatList.size() > 0) {
            for (List<DataPoint> list : _heatList) {
                drawHeatMap(_map, list);
            }
        }

        //Draw all the polygons
        if (_polygonList.size() > 0) {
            for (List<? extends IPoint> list : _polygonList) {
                drawPolygon(_map, list);
            }
        }

        // Then add the routes on top
        if (_routeList.size() > 0) {
            for (List<? extends IPoint> list : _routeList) {
                drawRoute(_map, list);
            }
        }

        //Draw Info-Boxes
        if (_infoList.size() > 0) {
            drawInfo(_map, _infoList);
        }

        //Just show map
        else {
            _map.setMyLocationEnabled(this._enableMyLocation);
        }

        _map.setPadding(_padding[0], _padding[1], _padding[2], _padding[3]);
    }


    private void drawPolygon(final GoogleMap map, List<? extends IPoint> locations) {
        PolygonTask polygonTask = new PolygonTask(locations);
        polygonTask.addOnTaskOutputListener(new OnTaskOutputListener<List<LatLng>>() {
            @Override
            public void onTaskOutput(List<LatLng> latLngs) {
                if (latLngs != null) {
                    if (map != null & MapFragment.this.isAdded()) {
                        PolygonOptions polygonOptions = new PolygonOptions()
                                .strokeColor(getResources().getColor(R.color.blue))
                                .fillColor(getResources().getColor(R.color.white_with_alpha))
                                .zIndex(0f)
                                .addAll(latLngs);

                        map.addPolygon(polygonOptions);
                    }
                    LatLng position = latLngs.get(0);
                    moveCamera(position);
                }
            }
        });
        polygonTask.execute();
    }

    private void drawRoute(final GoogleMap map, List<? extends IPoint> locations) {
        RouteTask routeTask = new RouteTask(locations);
        routeTask.addOnTaskOutputListener(new OnTaskOutputListener<List<LatLng>>() {
            @Override
            public void onTaskOutput(List<LatLng> latLngs) {
                if (latLngs != null) {
                    if (map != null & MapFragment.this.isAdded()) {
                        PolylineOptions polylineOptions = new PolylineOptions()
                                .color(getResources().getColor(R.color.orange))
                                .zIndex(2f)
                                .addAll(latLngs);

                        map.addPolyline(polylineOptions);
                    }
                    LatLng position = latLngs.get(0);
                    moveCamera(position);
                }
            }
        });
        routeTask.execute();
    }

    private void drawHeatMap(final GoogleMap map, List<DataPoint> dataPoints) {
        HeatMapTask heatMapTask = new HeatMapTask(dataPoints);
        heatMapTask.addOnTaskOutputListener(new OnTaskOutputListener<List<WeightedLatLng>>() {
            @Override
            public void onTaskOutput(List<WeightedLatLng> weightedLatLngs) {
                if (weightedLatLngs != null) {
                    if (map != null & MapFragment.this.isAdded()) {
                        HeatmapTileProvider heatProvider = new HeatmapTileProvider.Builder()
                                .weightedData(weightedLatLngs)
                                .gradient(_gradient)
                                .build();

                        TileOverlay overlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(heatProvider).zIndex(1f));
                    }
                }
            }
        });
        heatMapTask.execute();
    }

    private void drawInfo(final GoogleMap map, List<Pair<Location, String>> infos) {
        for (Pair<Location, String> info : infos) {
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(info.first.getLatitude(), info.first.getLongitude()))
                    .title(info.second)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            );
            marker.showInfoWindow();
        }
    }


    @Override
    public int getFragmentTabTitle() {
        return R.string.title_fragment_map;
    }


    @Override
    public boolean onMyLocationButtonClick() {
        LatLng position;
        if (_routeList.size() > 0) {
            position = new LatLng(
                    _routeList.get(0).get(0).x(),
                    _routeList.get(0).get(0).y()
            );
        } else if (_polygonList.size() > 0) {
            position = new LatLng(
                    _polygonList.get(0).get(0).x(),
                    _polygonList.get(0).get(0).y()
            );
        } else {
            position = new LatLng(
                    GpsLocationService.getInstance().getCurrentLocation().getLatitude(),
                    GpsLocationService.getInstance().getCurrentLocation().getLongitude()
            );
        }

        _map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, _map.getCameraPosition().zoom));

        return true;
    }

    public MapFragment setPadding(int left, int top, int right, int bottom) {
        _padding[0] = left;
        _padding[1] = top;
        _padding[2] = right;
        _padding[3] = bottom;
        return this;
    }

    public MapFragment showZoomButtons(boolean show) {
        this._showZoomButtons = show;
        return this;
    }

    public MapFragment enableZoomGesture(boolean enable) {
        this._enableZoomGesture = enable;
        return this;
    }

    public MapFragment enableMyLocation(boolean enable) {
        this._enableMyLocation = enable;
        return this;
    }


    public MapFragment addPolygon(Polygon polygon) {
        this._polygonList.add(new ArrayList<>(Arrays.asList(polygon.getPoints())));
        return this;
    }

    public MapFragment addPolygon(List<Location> locations) {
        this._polygonList.add(locations);
        return this;
    }

    public MapFragment addPolygon(Acre acre) {
        if (acre != null && acre.getArea() != null && acre.getArea().getLocations().size() > 0) {
            if (acre.isManaged()) {
                addPolygon(Realm.getDefaultInstance().copyFromRealm(acre.getArea().getLocations()));
            } else {
                addPolygon(acre.getArea().getLocations());
            }
        }
        return this;
    }

    public MapFragment addRoute(List<Location> locations) {
        this._routeList.add(locations);
        return this;
    }

    public MapFragment addHeatMap(List<DataPoint> dataPoints) {
        List<DataPoint> help = new ArrayList<>();
        for (DataPoint d : dataPoints) {
            if (d.isManaged()) {
                help.add(Realm.getDefaultInstance().copyFromRealm(d));
            } else {
                help.add(d);
            }
        }
        this._heatList.add(help);
        return this;
    }

    public MapFragment addPoint(Location location) {
        if (this._isMapReady) {
            if (_track == null) {
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(getResources().getColor(R.color.orange));
                _track = this._map.addPolyline(polylineOptions);
            }

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            List<LatLng> points = _track.getPoints();
            points.add(latLng);
            _track.setPoints(points);
            _map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
        return this;
    }

    public MapFragment addInfo(Location location, String text) {
        _infoList.add(new Pair<Location, String>(location, text));
        return this;
    }

    public MapFragment clear() {
        this._polygonList.clear();
        this._routeList.clear();
        this._track = null;
        if (_map != null) {
            _map.clear();
        }
        return this;
    }

    public MapFragment clearPolygons() {
        _polygonList.clear();
        return this;
    }

    public MapFragment clearRoutes() {
        _routeList.clear();
        return this;
    }

    public MapFragment redraw() {
        if (_isMapReady) {
            _map.clear();
            onMapReady(_map);
        }
        return this;
    }

    public MapFragment drawActiveRun(boolean draw) {
        this._drawActiveRun = draw;
        RunController.getInstance().addOnDataPointAddedListener(this);
        return this;
    }


    private synchronized void moveCamera(LatLng position) {
        if (_map.getCameraPosition().zoom >= _map.getMinZoomLevel()) {
            _map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, MAP_ZOOM));
        } else {
            _map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, _map.getCameraPosition().zoom));
        }
    }


    @Override
    public void onDataPointAdded(final long runId, final DataPoint dataPoint) {
        if (this._isMapReady && this._drawActiveRun) {
            if (_track == null) {
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(getResources().getColor(R.color.orange));
                _track = this._map.addPolyline(polylineOptions);
            }

            if (dataPoint.getLocation() != null) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Run run = (Run) RealmHelper.findWhereId(Run.class, runId);

                        List<LatLng> points = _track.getPoints();
                        points.clear();
                        //points.add(new LatLng(dataPoint.getLocation().getLatitude(), dataPoint.getLocation().getLongitude()));
                        for (Location l : Realm.getDefaultInstance().copyFromRealm(run.getRoute())) {
                            points.add(new LatLng(l.getLatitude(), l.getLongitude()));
                        }
                        _track.setPoints(points);

                        LatLng currentPosition = new LatLng(dataPoint.getLocation().getLatitude(), dataPoint.getLocation().getLongitude());
                        _map.animateCamera(CameraUpdateFactory.newLatLng(currentPosition));
                    }
                });
            }
        }
    }


    private class PolygonTask extends AAsyncTask<Void, Void, List<LatLng>, List<LatLng>> {

        private List<? extends IPoint> __locations;

        public PolygonTask(List<? extends IPoint> locations) {
            this.__locations = locations;
        }

        @Override
        protected List<LatLng> doInBackground(Void... params) {
            if (__locations.size() > 0) {
                List<LatLng> latLngs = new ArrayList<>();
                for (IPoint l : __locations) {
                    latLngs.add(
                            new LatLng(l.x(), l.y())
                    );
                }
                return latLngs;
            }
            return null;
        }

        @Override
        protected List<LatLng> convertResultToOutput(List<LatLng> latLngs) {
            return latLngs;
        }
    }

    private class RouteTask extends AAsyncTask<Void, Void, List<LatLng>, List<LatLng>> {

        private List<? extends IPoint> __locations;

        public RouteTask(List<? extends IPoint> locations) {
            this.__locations = locations;
        }

        @Override
        protected List<LatLng> convertResultToOutput(List<LatLng> latLngs) {
            return latLngs;
        }

        @Override
        protected List<LatLng> doInBackground(Void... params) {
            if (__locations.size() > 0) {
                List<LatLng> points = new ArrayList<>();
                for (IPoint l : __locations) {
                    points.add(
                            new LatLng(l.x(), l.y())
                    );
                }
                return points;
            }
            return null;
        }
    }

    private class HeatMapTask extends AAsyncTask<Void, Void, List<WeightedLatLng>, List<WeightedLatLng>> {

        private List<DataPoint> __dataPoints;

        public HeatMapTask(List<DataPoint> dataPoints) {
            this.__dataPoints = dataPoints;
        }

        @Override
        protected List<WeightedLatLng> convertResultToOutput(List<WeightedLatLng> weightedLatLngs) {
            return weightedLatLngs;
        }

        @Override
        protected List<WeightedLatLng> doInBackground(Void... voids) {
            if (__dataPoints.size() > 0) {
                List<WeightedLatLng> points = new ArrayList<>();
                for (DataPoint d : __dataPoints) {
                    double used = MathUtils.calcUsedVolume(
                            d.getDataByPurpose(SensorPurpose.EJECTION).getValue(),
                            d.getDataByPurpose(SensorPurpose.INJECTION).getValue(),
                            d.getDataByPurpose(SensorPurpose.RETURN).getValue());
                    points.add(
                            new WeightedLatLng(new LatLng(d.getLocation().getLatitude(), d.getLocation().getLongitude()), used)
                    );
                }
                return points;
            }
            return null;
        }
    }

}

