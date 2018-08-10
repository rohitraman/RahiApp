package com.example.roshan.rahiapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roshan.rahiapp.maps.MapActivity;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;

import java.util.ArrayList;


public class MapFragment extends Fragment {
    private MapView mapView;
    private MapController controller;
    RotationGestureOverlay overlay;
    //    List<Double> latitudes = new ArrayList<>();
//    List<Double> longitudes = new ArrayList<>();
    RoadManager roadManager = new OSRMRoadManager(getContext());
    Polyline roadOverlay;
    Road road;


    public MapFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        // Inflate the layout for this fragment
        Context context = getContext();
        org.osmdroid.config.Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        mapView = (MapView) view.findViewById(R.id.mapView);

        mapView.setMultiTouchControls(true);
        controller = (MapController) mapView.getController();
        controller.setZoom(7.25);
        ArrayList<OverlayItem> items = new ArrayList<>();
        final ArrayList<GeoPoint> points = new ArrayList<>();
        points.clear();
//        Marker marker = new Marker(mapView);
        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_brightness_1_black_24dp);

        for (int i = 0; i < GetRouteActivity.latitudes.size(); i++) {
            Log.i("Blah", String.valueOf(GetRouteActivity.latitudes.get(i)) + GetRouteActivity.longitudes.get(i));
            GeoPoint geoPoint = new GeoPoint(GetRouteActivity.latitudes.get(i), GetRouteActivity.longitudes.get(i));
            points.add(geoPoint);
//            marker.setPosition(geoPoint);
//            marker.setTitle(GetRouteActivity.names.get(i));
            if (i == 0 || i == GetRouteActivity.latitudes.size() - 1) {
                OverlayItem item = new OverlayItem(GetRouteActivity.names.get(i), GetRouteActivity.codes.get(i), geoPoint);
//                item.setMarker(drawable);
                items.add(item);
            } else {
                OverlayItem item = new OverlayItem(GetRouteActivity.names.get(i), GetRouteActivity.codes.get(i), geoPoint);
                item.setMarker(drawable);

                items.add(item);
            }
        }
        controller.setCenter(points.get(points.size() / 2 - 1));

        overlay = new RotationGestureOverlay(mapView);
        overlay.setEnabled(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                roadManager.addRequestOption("routeType=train");
                road = roadManager.getRoad(points);


                        roadOverlay = RoadManager.buildRoadOverlay(road);
                        mapView.getOverlays().add(roadOverlay);
            }
        }).start();




        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<>(items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                return true;
            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                return false;
            }
        }, getContext());

        mOverlay.setFocusItemsOnTap(true);
        mapView.getOverlays().add(this.overlay);
        mapView.getOverlays().add(mOverlay);
        Log.i("Hello", Float.toString(mapView.getMapOrientation()));



        return view;

    }
    public void ml(View view){
        Intent i=new Intent(getContext(),MapActivity.class);
        startActivity(i);
    }

}
