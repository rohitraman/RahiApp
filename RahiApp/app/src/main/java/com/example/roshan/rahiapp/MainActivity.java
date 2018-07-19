package com.example.roshan.rahiapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    GridView gridView, gridview2;
    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;

    private Session session;

    private Button LogoutBtn;


    private void logout() {
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();

    }

    static final String[] GRID_DATA = new String[]{
            "Train Between Stations",
            "Train Schedule",
            "PNR Status",
            "Seat Availability",
            "Live Train Status",
            "Trains On A Day",
            "Seat Map",
            "Fare Enquiry",
            "Book/Cancel Tickets",
            "Station Alarm",
            "Cancelled Trains",
            "Rescheduled Trains",
            "Diverted Trains"
    };
    static final String[] GRID_DATA2 = new String[]{
            "Train Between Stations",
            "Train Schedule",
            "PNR Status",

    };

    static final String[] activities = new String[]
            {
                    "GetAllTrainsActivity",
                    "GetRouteActivity",
                    "GetPNRActivity"
            };
    static final String[] activities1 = new String[]
            {
                    "GetAllTrainsActivity",
                    "GetRouteActivity",
                    "GetPNRActivity",
                    "GetAvailabilityActivity",
                    "GetStatusActivity",
                    "GetTrainsOnActivity",
                    "",
                    "GetFareActivity",
                    "",
                    "",
                    "",
                    "",
                    ""
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.grid);
        gridview2 = (GridView) findViewById(R.id.gride);
        gridView.setAdapter(new ImageAdapterGridView(this, GRID_DATA));
        gridview2.setAdapter(new ImageAdapterGridView2(this, GRID_DATA2));
        LogoutBtn = (Button) findViewById(R.id.btn_logout);

        DatabaseAccess access = DatabaseAccess.getInstance(this);
        access.open();
        if (GetStationsAndTrains.stations.size() == 0) {
            GetStationsAndTrains.stations = access.getStations();
        }
        if (GetStationsAndTrains.trains.size() == 0) {
            GetStationsAndTrains.trains = access.getTrains1();
        }
        access.close();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("Position ", String.valueOf(i));
                try {
                    Class myClass = Class.forName("com.example.roshan.rahiapp." + activities1[i]);
                    startActivity(new Intent(MainActivity.this, myClass));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        gridview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Class myClass = Class.forName("com.example.roshan.rahiapp." + activities[i]);
                    Intent intent = new Intent(MainActivity.this, myClass);
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        mAuth = FirebaseAuth.getInstance();

        session = new Session(this);

        mProgress = new ProgressDialog(MainActivity.this);

        if (!session.loggedIn()) {
            logout();
        }

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgress.setTitle("Logging Out...");
                mProgress.setMessage("Please wait while we logout...");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();

                mAuth.signOut();

                mProgress.dismiss();

                session.setLoggedIn(false);
                logout();
            }
        });


    }

    public class ImageAdapterGridView extends BaseAdapter {
        private Context context;
        private final String[] gridValues;

        public ImageAdapterGridView(Context context, String[] gridValues) {

            this.context = context;
            this.gridValues = gridValues;
        }

        public int getCount() {
            return gridValues.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View gridView;
            if (convertView == null) {
                gridView = inflater.inflate(R.layout.item, null);
                TextView textView = (TextView) gridView
                        .findViewById(R.id.grid_item_label);

                textView.setText(gridValues[position]);
                ImageView imageView = (ImageView) gridView
                        .findViewById(R.id.grid_item_image);

                String arrLabel = gridValues[position];
                if (arrLabel.equals("Train Between Stations")) {

                    imageView.setImageResource(R.drawable.seach);

                } else if (arrLabel.equals("Train Schedule")) {

                    imageView.setImageResource(R.drawable.calender);

                } else if (arrLabel.equals("PNR Status")) {

                    imageView.setImageResource(R.drawable.trainstatus);

                } else if (arrLabel.equals("Seat Availability")) {

                    imageView.setImageResource(R.drawable.seat);
                } else if (arrLabel.equals("Live Train Status")) {

                    imageView.setImageResource(R.drawable.trainstatus);
                } else if (arrLabel.equals("Trains On A Day")) {

                    imageView.setImageResource(R.drawable.pnr);
                } else if (arrLabel.equals("Seat Map")) {

                    imageView.setImageResource(R.drawable.seat);
                } else if (arrLabel.equals("Fare Enquiry")) {

                    imageView.setImageResource(R.drawable.fare);
                } else if (arrLabel.equals("Book/Cancel Tickets")) {

                    imageView.setImageResource(R.drawable.ticket);
                } else if (arrLabel.equals("Station Alarm")) {

                    imageView.setImageResource(R.drawable.alar);
                } else if (arrLabel.equals("Cancelled Trains")) {

                    imageView.setImageResource(R.drawable.cac);
                } else if (arrLabel.equals("Rescheduled Trains")) {

                    imageView.setImageResource(R.drawable.restrain);
                } else if (arrLabel.equals("Diverted Trains")) {

                    imageView.setImageResource(R.drawable.diverted);
                }

            } else {

                gridView = (View) convertView;
            }

            return gridView;
        }
    }

    public class ImageAdapterGridView2 extends BaseAdapter {
        private Context context;
        private final String[] gridValues;

        public ImageAdapterGridView2(Context context, String[] gridValues) {

            this.context = context;
            this.gridValues = gridValues;
        }

        public int getCount() {
            return gridValues.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View gridView;
            if (convertView == null) {
                gridView = inflater.inflate(R.layout.item, null);
                TextView textView = (TextView) gridView
                        .findViewById(R.id.grid_item_label);

                textView.setText(gridValues[position]);
                ImageView imageView = (ImageView) gridView
                        .findViewById(R.id.grid_item_image);

                String arrLabel = gridValues[position];
                if (arrLabel.equals("search")) {

                    imageView.setImageResource(R.drawable.seach);

                } else if (arrLabel.equals("schedule")) {

                    imageView.setImageResource(R.drawable.calender);

                } else if (arrLabel.equals("PNR Status")) {

                    imageView.setImageResource(R.drawable.trainstatus);

                }
            } else {

                gridView = (View) convertView;
            }

            return gridView;
        }

    }

    @Override
    public void onBackPressed() {

    }
}


