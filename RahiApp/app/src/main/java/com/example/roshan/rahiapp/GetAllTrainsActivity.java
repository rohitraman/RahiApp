package com.example.roshan.rahiapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//Format of response
//{"trains":[{'coach_types': {'CC': '0', '2S': '0', 'GN': '1', 'SL': '1', '2A': '1', '3A': '1', '1A': '0'}, 'region': 'SR', 'train_type': 'Super Fast', 'train_base': {'source_stn_name': 'Chennai Central', 'train_id': '2206', 'source_depart': '20.20', 'from_stn_code': 'MAS', 'from_stn_name': 'Chennai Central', 'type': 'SUPERFAST', 'running_days': '1111111', 'to_time': '07.55', 'to_stn_name': 'Kozhikkode (Calicut)', 'dstn_stn_code': 'MAQ', 'travel_time': '11.35', 'dstn_stn_name': 'Mangalore Central', 'distance_from_to': '668', 'train_name': 'MANGALORE MAIL', 'train_no': '12601', 'source_stn_code': 'MAS', 'from_time': '20.20', 'average_speed': '58', 'notif_coach': 'MAS-CLT-143 berth in SL class. ', 'to_stn_code': 'CLT', 'dstn_reach': '12.25'}, 'coach_arrangement': [{'1': {'coach_id': '', 'tag': '', 'type': 'En'}}, {'2': {'coach_id': '', 'tag': '', 'type': 'VP'}}, {'3': {'coach_id': '', 'tag': '', 'type': 'GS'}}, {'4': {'coach_id': '', 'tag': '', 'type': 'UR'}}, {'5': {'coach_id': '', 'tag': '', 'type': 'UR'}}, {'6': {'coach_id': 'A1', 'tag': 'A', 'type': '2A'}}, {'7': {'coach_id': 'B1', 'tag': 'B', 'type': '3A'}}, {'8': {'coach_id': 'B2', 'tag': 'B', 'type': '3A'}}, {'9': {'coach_id': 'B3', 'tag': 'B', 'type': '3A'}}, {'10': {'coach_id': 'B4', 'tag': 'B', 'type': '3A'}}, {'11': {'coach_id': 'S1', 'tag': 'S', 'type': 'SL'}}, {'12': {'coach_id': 'S2', 'tag': 'S', 'type': 'SL'}}, {'13': {'coach_id': 'S3', 'tag': 'S', 'type': 'SL'}}, {'14': {'coach_id': 'S4', 'tag': 'S', 'type': 'SL'}}, {'15': {'coach_id': 'S5', 'tag': 'S', 'type': 'SL'}}, {'16': {'coach_id': 'S6', 'tag': 'S', 'type': 'SL'}}, {'17': {'coach_id': 'S7', 'tag': 'S', 'type': 'SL'}}, {'18': {'coach_id': 'S8', 'tag': 'S', 'type': 'SL'}}, {'19': {'coach_id': 'S9', 'tag': 'S', 'type': 'SL'}}, {'20': {'coach_id': 'S10', 'tag': 'S', 'type': 'SL'}}, {'21': {'coach_id': 'S11', 'tag': 'S', 'type': 'SL'}}, {'22': {'coach_id': '', 'tag': '', 'type': 'UR'}}, {'23': {'coach_id': '', 'tag': '', 'type': 'UR'}}, {'24': {'coach_id': '', 'tag': '', 'type': 'GS'}}], 'rake_share': ['12601', '22638']}, {'coach_types': {'CC': '0', '2S': '0', 'GN': '1', 'SL': '1', '2A': '1', '3A': '1', '1A': '1'}, 'region': 'SR', 'train_type': 'Super Fast', 'train_base': {'source_stn_name': 'Chennai Central', 'train_id': '2329', 'source_depart': '17.00', 'from_stn_code': 'MAS', 'from_stn_name': 'Chennai Central', 'type': 'SUPERFAST', 'running_days': '1111111', 'to_time': '04.20', 'to_stn_name': 'Kozhikkode (Calicut)', 'dstn_stn_code': 'MAQ', 'travel_time': '11.20', 'dstn_stn_name': 'Mangalore Central', 'distance_from_to': '668', 'train_name': 'MANGALORE EXP', 'train_no': '12685', 'source_stn_code': 'MAS', 'from_time': '17.00', 'average_speed': '59', 'to_stn_code': 'CLT', 'dstn_reach': '09.05'}, 'coach_arrangement': [{'1': {'coach_id': '', 'tag': '', 'type': 'En'}}, {'2': {'coach_id': '', 'tag': '', 'type': 'GC'}}, {'3': {'coach_id': '', 'tag': '', 'type': 'UR'}}, {'4': {'coach_id': 'HA1', 'tag': 'HA', 'type': '1A + 2A'}}, {'5': {'coach_id': 'A1', 'tag': 'A', 'type': '2A'}}, {'6': {'coach_id': 'A2', 'tag': 'A', 'type': '2A'}}, {'7': {'coach_id': 'B1', 'tag': 'B', 'type': '3A'}}, {'8': {'coach_id': 'B2', 'tag': 'B', 'type': '3A'}}, {'9': {'coach_id': 'S1', 'tag': 'S', 'type': 'SL'}}, {'10': {'coach_id': 'S2', 'tag': 'S', 'type': 'SL'}}, {'11': {'coach_id': 'S3', 'tag': 'S', 'type': 'SL'}}, {'12': {'coach_id': 'S4', 'tag': 'S', 'type': 'SL'}}, {'13': {'coach_id': 'S5', 'tag': 'S', 'type': 'SL'}}, {'14': {'coach_id': 'S6', 'tag': 'S', 'type': 'SL'}}, {'15': {'coach_id': 'S7', 'tag': 'S', 'type': 'SL'}}, {'16': {'coach_id': 'S8', 'tag': 'S', 'type': 'SL'}}, {'17': {'coach_id': 'S9', 'tag': 'S', 'type': 'SL'}}, {'18': {'coach_id': 'S10', 'tag': 'S', 'type': 'SL'}}, {'19': {'coach_id': 'S11', 'tag': 'S', 'type': 'SL'}}, {'20': {'coach_id': '', 'tag': '', 'type': 'UR'}}, {'21': {'coach_id': '', 'tag': '', 'type': 'UR'}}, {'22': {'coach_id': '', 'tag': '', 'type': 'GC'}}], 'rake_share': ['12685', '12686']}, {'coach_types': {'CC': '0', '2S': '1', 'GN': '1', 'SL': '1', '2A': '1', '3A': '1', '1A': '1'}, 'train_base': {'source_stn_name': 'Chennai Egmore', 'train_id': '15826', 'source_depart': '23.15', 'from_stn_code': 'MS', 'from_stn_name': 'Chennai Egmore', 'type': 'MAIL_EXPRESS', 'running_days': '1111111', 'to_time': '16.50', 'to_stn_name': 'Kozhikkode (Calicut)', 'dstn_stn_code': 'MAQ', 'travel_time': '17.35', 'dstn_stn_name': 'Mangalore Central', 'distance_from_to': '763', 'train_name': 'MS MANGALORE EXP', 'train_no': '16859', 'source_stn_code': 'MS', 'from_time': '23.15', 'average_speed': '43', 'to_stn_code': 'CLT', 'dstn_reach': '21.45'}, 'coach_arrangement': [{'1': {'coach_id': '', 'tag': '', 'type': 'En'}}, {'2': {'coach_id': '', 'tag': '', 'type': 'GS'}}, {'3': {'coach_id': '', 'tag': '', 'type': 'UR'}}, {'4': {'coach_id': '', 'tag': '', 'type': 'UR'}}, {'5': {'coach_id': 'S11', 'tag': 'S', 'type': 'SL'}}, {'6': {'coach_id': 'S10', 'tag': 'S', 'type': 'SL'}}, {'7': {'coach_id': 'S9', 'tag': 'S', 'type': 'SL'}}, {'8': {'coach_id': 'S8', 'tag': 'S', 'type': 'SL'}}, {'9': {'coach_id': 'S7', 'tag': 'S', 'type': 'SL'}}, {'10': {'coach_id': 'S6', 'tag': 'S', 'type': 'SL'}}, {'11': {'coach_id': 'S5', 'tag': 'S', 'type': 'SL'}}, {'12': {'coach_id': 'S4', 'tag': 'S', 'type': 'SL'}}, {'13': {'coach_id': 'S3', 'tag': 'S', 'type': 'SL'}}, {'14': {'coach_id': 'S2', 'tag': 'S', 'type': 'SL'}}, {'15': {'coach_id': 'S1', 'tag': 'S', 'type': 'SL'}}, {'16': {'coach_id': 'D2', 'tag': 'D', 'type': '2S'}}, {'17': {'coach_id': 'D1', 'tag': 'D', 'type': '2S'}}, {'18': {'coach_id': 'B3', 'tag': 'B', 'type': '3A'}}, {'19': {'coach_id': 'B2', 'tag': 'B', 'type': '3A'}}, {'20': {'coach_id': 'B1', 'tag': 'B', 'type': '3A'}}, {'21': {'coach_id': 'HA1', 'tag': 'HA', 'type': '1A + 2A'}}, {'22': {'coach_id': '', 'tag': '', 'type': 'UR'}}, {'23': {'coach_id': '', 'tag': '', 'type': 'GS'}}], 'train_type': 'Mail & Express', 'region': 'SR'}, {'coach_types': {'CC': '0', '2S': '0', 'GN': '1', 'SL': '1', '2A': '1', '3A': '1', '1A': '0'}, 'notif': 'Premium Tatkal quota in SL Class only. ', 'train_type': 'Super Fast', 'train_base': {'source_stn_name': 'Chennai Central', 'train_id': '16867', 'source_depart': '12.05', 'from_stn_code': 'MAS', 'from_stn_name': 'Chennai Central', 'type': 'SUPERFAST', 'running_days': '1111111', 'to_time': '23.50', 'to_stn_name': 'Kozhikkode (Calicut)', 'dstn_stn_code': 'MAQ', 'travel_time': '11.45', 'dstn_stn_name': 'Mangalore Central', 'distance_from_to': '681', 'train_name': 'WEST COAST EXP', 'train_no': '22637', 'source_stn_code': 'MAS', 'from_time': '12.05', 'average_speed': '58', 'to_stn_code': 'CLT', 'dstn_reach': '04.30'}, 'coach_arrangement': [{'1': {'coach_id': '', 'tag': '', 'type': 'En'}}, {'2': {'coach_id': '', 'tag': '', 'type': 'VP'}}, {'3': {'coach_id': '', 'tag': '', 'type': 'GS'}}, {'4': {'coach_id': '', 'tag': '', 'type': 'UR'}}, {'5': {'coach_id': '', 'tag': '', 'type': 'UR'}}, {'6': {'coach_id': 'A1', 'tag': 'A', 'type': '2A'}}, {'7': {'coach_id': 'B1', 'tag': 'B', 'type': '3A'}}, {'8': {'coach_id': 'B2', 'tag': 'B', 'type': '3A'}}, {'9': {'coach_id': 'B3', 'tag': 'B', 'type': '3A'}}, {'10': {'coach_id': 'B4', 'tag': 'B', 'type': '3A'}}, {'11': {'coach_id': 'S1', 'tag': 'S', 'type': 'SL'}}, {'12': {'coach_id': 'S2', 'tag': 'S', 'type': 'SL'}}, {'13': {'coach_id': 'S3', 'tag': 'S', 'type': 'SL'}}, {'14': {'coach_id': 'S4', 'tag': 'S', 'type': 'SL'}}, {'15': {'coach_id': 'S5', 'tag': 'S', 'type': 'SL'}}, {'16': {'coach_id': 'S6', 'tag': 'S', 'type': 'SL'}}, {'17': {'coach_id': 'S7', 'tag': 'S', 'type': 'SL'}}, {'18': {'coach_id': 'S8', 'tag': 'S', 'type': 'SL'}}, {'19': {'coach_id': 'S9', 'tag': 'S', 'type': 'SL'}}, {'20': {'coach_id': 'S10', 'tag': 'S', 'type': 'SL'}}, {'21': {'coach_id': 'S11', 'tag': 'S', 'type': 'SL'}}, {'22': {'coach_id': '', 'tag': '', 'type': 'UR'}}, {'23': {'coach_id': '', 'tag': '', 'type': 'UR'}}, {'24': {'coach_id': '', 'tag': '', 'type': 'GS'}}], 'rake_share': ['12601', '12602'], 'region': 'SR'}]


public class GetAllTrainsActivity extends AppCompatActivity {
    AutoCompleteTextView etFrom, etTo;
    Button button;
    ListView listView;
    List<Integer> trainNos = new ArrayList<>();
    List<String> trainNames = new ArrayList<>();
    String from,to;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_trains);
        etFrom = (AutoCompleteTextView) findViewById(R.id.et_from);
        etTo = (AutoCompleteTextView) findViewById(R.id.et_to);
        button = (Button) findViewById(R.id.btn_get_trains);
        listView = (ListView) findViewById(R.id.lv_trains);


        CustomAutoTextViewAdapter adapter = new CustomAutoTextViewAdapter(this,android.R.layout.simple_list_item_1,GetStations.stations);
        etFrom.setAdapter(adapter);
        etTo.setAdapter(adapter);
        etFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = String.valueOf(adapterView.getItemAtPosition(i));
                String[] items = item.split(" ");
                from = items[items.length-1];
            }
        });
        etTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = String.valueOf(adapterView.getItemAtPosition(i));
                String[] items = item.split(" ");
//                Log.i("CBjk", String.valueOf(items.length));
                to = items[items.length-1];
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trainNames.clear();
                trainNos.clear();
                DatabaseAccess access = DatabaseAccess.getInstance(GetAllTrainsActivity.this);
                access.open();
                Map<Integer, String> trains = access.getTrains(from, to);
                access.close();
                for (Map.Entry<Integer, String> entry : trains.entrySet()) {
                    trainNos.add(entry.getKey());
                    trainNames.add(entry.getValue());
                }

                CustomAdapter adapter = new CustomAdapter();
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(GetAllTrainsActivity.this, RouteViewActivity.class);
                        intent.putExtra("trainNo", trainNos.get(i));
                        startActivity(intent);
                    }
                });
                //                JSONObject object = new JSONObject();
//                try {
//                    object.put("from", etFrom.getText().toString().toUpperCase());
//                    object.put("to", etTo.getText().toString().toUpperCase());
//                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://railwayapi.herokuapp.com/getAllTrains", object, new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            try {
//                                Log.i("Great",response.toString());
//                                JSONArray array = response.getJSONArray("trains");
//                                for (int i = 0; i < array.length(); i++) {
//                                    JSONObject train = array.getJSONObject(i);
//                                    JSONObject train_base = train.getJSONObject("train_base");
//                                    String source_stn_name = train_base.getString("source_stn_name");
//                                    String source_depart = train_base.getString("source_depart");
//                                    String from_stn_name = train_base.getString("from_stn_name");
//                                    String dstn_stn_name = train_base.getString("dstn_stn_name");
//                                    String dstn_stn_code = train_base.getString("dstn_stn_code");
//                                    String train_name = train_base.getString("train_name");
//                                    String train_no = train_base.getString("train_no");
//                                    String from_stn_code = train_base.getString("from_stn_code");
//                                    String to_stn_code = train_base.getString("to_stn_code");
//                                    String to_stn_name = train_base.getString("to_stn_name");
//                                    String to_time = train_base.getString("to_time");
//                                    String from_time = train_base.getString("from_time");
//
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.i("Errrroooorrrr", error.getMessage());
//                            NetworkResponse response = error.networkResponse;
//                            if (error instanceof ServerError && response != null) {
//                                try {
//                                    String string = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//                                    Log.i("Finally", string);
//                                } catch (UnsupportedEncodingException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
            }
        });
//                    request.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES*2,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//                    RequestQueue queue = Volley.newRequestQueue(GetAllTrainsActivity.this);
//                    queue.add(request);

//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return trainNames.size();
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
            view = getLayoutInflater().inflate(R.layout.listview_layout, null);
            TextView tvTrainNos = (TextView) view.findViewById(R.id.tv_trainNo);
            TextView tvTrainNames = (TextView) view.findViewById(R.id.tv_trainName);

            tvTrainNames.setText(trainNames.get(i));
            tvTrainNos.setText(String.valueOf(trainNos.get(i)));
            return view;
        }

    }
}

