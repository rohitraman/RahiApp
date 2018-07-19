package com.example.roshan.rahiapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
//Format of response
//{"status":[{"arrdelay":0,"cancelled":false,"date":"18-Jun-2018","depdelay":0,"stations":"SRR","train":"12601"},{"arrdelay":38,"cancelled":false,"date":"17-Jun-2018","depdelay":38,"stations":"SRR","train":"12601"},{"arrdelay":78,"cancelled":false,"date":"16-Jun-2018","depdelay":75,"stations":"SRR","train":"12601"}]}

public class GetStatusActivity extends AppCompatActivity {
    AutoCompleteTextView etStation;
    AutoCompleteTextView etTrainNo;
    Button button;
    String station,train;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_status);
        etStation = (AutoCompleteTextView) findViewById(R.id.et_station);
        etTrainNo = (AutoCompleteTextView) findViewById(R.id.et_train_no_3);
        button = (Button) findViewById(R.id.btn_getDetails);
        CustomAutoTextViewAdapter adapter = new CustomAutoTextViewAdapter(this,android.R.layout.simple_list_item_1,GetStationsAndTrains.stations);
        etStation.setAdapter(adapter);
        CustomAutoTextViewAdapter adapter1 = new CustomAutoTextViewAdapter(this,android.R.layout.simple_list_item_1,GetStationsAndTrains.trains);
        etTrainNo.setAdapter(adapter1);
//        etTo.setAdapter(adapter);
        etStation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = String.valueOf(adapterView.getItemAtPosition(i));
                String[] items = item.split(" ");
                station = items[items.length-1];
            }
        });

        etTrainNo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = String.valueOf(adapterView.getItemAtPosition(i));
                String[] items = item.split(" ");
                train = items[0];
            }
        });
//        etTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String item = String.valueOf(adapterView.getItemAtPosition(i));
//                String[] items = item.split(" ");
////                Log.i("CBjk", String.valueOf(items.length));
//                to = items[items.length-1];
//            }
//        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final JSONObject object = new JSONObject();
                try {
                    object.put("station",station);
                    object.put("train_no",Integer.parseInt(train));
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://railwayapi.herokuapp.com/getStatus", object, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.i("Arrrray",response.toString());
                                JSONArray array = response.getJSONArray("status");
                                for (int i = 0;i<array.length();i++)
                                {
                                    JSONObject object1 = array.getJSONObject(i);
                                    int arrdelay = object1.getInt("arrdelay");
                                    boolean cancelled = object1.getBoolean("cancelled");
                                    String date = object1.getString("date");
                                    int depdelay = object1.getInt("depdelay");
                                    String stations = object1.getString("stations");
                                    String trainNo = object1.getString("train");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Errrroooorrrr",error.getMessage());
                            NetworkResponse response = error.networkResponse;
                            if(error instanceof ServerError && response!=null)
                            {
                                try {
                                    String string = new String(response.data, HttpHeaderParser.parseCharset(response.headers,"utf-8"));
                                    Log.i("Finally",string);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
                    request.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue queue = Volley.newRequestQueue(GetStatusActivity.this);
                    queue.add(request);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }
}
