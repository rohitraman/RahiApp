package com.example.roshan.rahiapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Map;

public class RouteViewActivity extends AppCompatActivity {

    ListItems trains;
    ListView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_view);

        Intent intent = getIntent();
        DatabaseAccess access = DatabaseAccess.getInstance(this);
        int trainNo = intent.getIntExtra("trainNo",0);
        access.open();
        trains = access.getStaionAndArrival(trainNo);
        access.close();
        view = (ListView)findViewById(R.id.lv_train_view);
        CustomAdapter adapter = new CustomAdapter();
        view.setAdapter(adapter);
    }
    class CustomAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return trains.distances.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.custom_list_view,null,false);
            TextView stationNames = (TextView)view.findViewById(R.id.station_name);
            TextView stationArrivals = (TextView)view.findViewById(R.id.station_arrival);
            TextView stationHalts = (TextView)view.findViewById(R.id.station_halt);
            TextView stationDistances = (TextView)view.findViewById(R.id.station_distance);
            stationNames.setText(trains.stationNames.get(i));
            stationArrivals.setText(trains.arrivals.get(i));
            stationHalts.setText(Integer.toString(trains.halts.get(i)));
            stationDistances.setText(trains.distances.get(i));
            return view;
        }
    }
}
