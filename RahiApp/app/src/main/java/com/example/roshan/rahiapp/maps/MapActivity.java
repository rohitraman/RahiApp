package com.example.roshan.rahiapp.maps;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.example.roshan.rahiapp.GetRouteActivity;
import com.example.roshan.rahiapp.R;

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

public class MapActivity extends AppCompatActivity {
    private MapView mapView;
    private MapController controller;
    RotationGestureOverlay overlay;
//    List<Double> latitudes = new ArrayList<>();
//    List<Double> longitudes = new ArrayList<>();
    RoadManager roadManager = new OSRMRoadManager(this);
    Polyline roadOverlay;
    Road road;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Context context = getApplicationContext();
        org.osmdroid.config.Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        mapView = (MapView) findViewById(R.id.mapView);

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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        roadOverlay = RoadManager.buildRoadOverlay(road);
                        mapView.getOverlays().add(roadOverlay);

                    }
                });

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
        }, this);

        mOverlay.setFocusItemsOnTap(true);
        mapView.getOverlays().add(this.overlay);
        mapView.getOverlays().add(mOverlay);
        Log.i("Hello", Float.toString(mapView.getMapOrientation()));



    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mapView == null)
            return super.onKeyUp(keyCode, event);
        switch (keyCode) {
            case KeyEvent.KEYCODE_PAGE_DOWN:
                mapView.getController().zoomIn();
                return true;
            case KeyEvent.KEYCODE_PAGE_UP:
                mapView.getController().zoomOut();
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }




}

