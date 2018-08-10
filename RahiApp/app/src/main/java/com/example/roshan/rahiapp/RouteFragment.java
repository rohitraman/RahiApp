package com.example.roshan.rahiapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class RouteFragment extends Fragment {

    public RouteFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_route, container, false);
        ListView listView = view.findViewById(R.id.list_view);
        CustomAdapter adapter = new CustomAdapter(getContext());
        listView.setAdapter(adapter);
        return view;
    }

    class CustomAdapter extends BaseAdapter {
        public Context context;

        public CustomAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return GetRouteActivity.arrival.size();
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_route, viewGroup, false);
            TextView codes = view.findViewById(R.id.tv_name);
            TextView arrival = view.findViewById(R.id.tv_arrival);
            TextView departure = view.findViewById(R.id.tv_departure);
            TextView day = view.findViewById(R.id.tv_day);
            TextView platform = view.findViewById(R.id.tv_pf);
            TextView distance = view.findViewById(R.id.tv_dist);
            codes.setText(GetRouteActivity.names.get(i) + "(" + GetRouteActivity.codes.get(i) + ")");
            arrival.setText(GetRouteActivity.arrival.get(i));
            departure.setText(GetRouteActivity.depart.get(i));
            day.setText(GetRouteActivity.day.get(i));
            platform.setText(GetRouteActivity.platform.get(i));
            distance.setText(GetRouteActivity.distance_from_source.get(i));
            return view;
        }
    }


}
