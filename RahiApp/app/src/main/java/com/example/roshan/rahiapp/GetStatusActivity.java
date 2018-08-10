package com.example.roshan.rahiapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.example.roshan.rahiapp.model.TimeLineModel;
import com.example.roshan.rahiapp.model.TrainStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

//Format of response
//{"status":[{"arrdelay":0,"cancelled":false,"date":"18-Jun-2018","depdelay":0,"stations":"SRR","train":"12601"},{"arrdelay":38,"cancelled":false,"date":"17-Jun-2018","depdelay":38,"stations":"SRR","train":"12601"},{"arrdelay":78,"cancelled":false,"date":"16-Jun-2018","depdelay":75,"stations":"SRR","train":"12601"}]}

public class GetStatusActivity extends AppCompatActivity {
    AutoCompleteTextView etTrain;
    EditText etDate;
    Button button;
    String train;
    DatePickerDialog dialog;
    String from;
    String date, date3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_status);
        etTrain = (AutoCompleteTextView) findViewById(R.id.et_train_no1);
        etDate = (EditText) findViewById(R.id.et_date1);
        button = (Button) findViewById(R.id.btn_getDetails);
        CustomAutoTextViewAdapter adapter = new CustomAutoTextViewAdapter(this, android.R.layout.simple_list_item_1, GetStationsAndTrains.trains);
        etTrain.setAdapter(adapter);
//        etTo.setAdapter(adapter);
        etTrain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = String.valueOf(adapterView.getItemAtPosition(i));
                String[] items = item.split(" ");
                train = items[0];
            }
        });

        etDate.setShowSoftInputOnFocus(false);

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                dialog = new DatePickerDialog(GetStatusActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        date = String.valueOf(i2) + "-" + String.valueOf(i1 + 1) + "-" + String.valueOf(i);
                        date3 = date;
                        etDate.setText(date);
                    }
                }, yy, mm, dd);
                dialog.show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final JSONObject object = new JSONObject();
                try {
                    object.put("date", etDate.getText().toString().trim());
                    object.put("train_no", Integer.parseInt(train));
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://railwayapi.herokuapp.com/getStatus", object, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.i("Arrrray", response.toString());
                                JSONObject status = response.getJSONObject("status");
                                JSONObject current_station = status.getJSONObject("current_station");
                                String position = status.getString("position");
                                JSONArray route = status.getJSONArray("route");
                                for (int i = 0; i < route.length(); i++) {
                                    JSONObject place = route.getJSONObject(i);
                                    String actarr = place.getString("actarr");
                                    String actarr_date = place.getString("actarr_date");
                                    String actdep = place.getString("actdep");
                                    String scharr = place.getString("scharr");
                                    String scharr_date = place.getString("scharr_date");
                                    String schdep = place.getString("schdep");
                                    String status1 = place.getString("status");
                                    boolean has_arrived = place.getBoolean("has_arrived");
                                    boolean has_departed = place.getBoolean("has_departed");
                                    JSONObject station = place.getJSONObject("station");
                                    String code = station.getString("code");
                                    double lat = station.getDouble("lat");
                                    double lng = station.getDouble("lng");
                                    String name = station.getString("name");
                                    if (has_arrived && has_departed) {
                                        if (i == 0)
                                            TimeLineActivity.mDataList.add(new TimeLineModel(name, actarr_date + " " + schdep, TrainStatus.DEPARTED));
                                        else
                                            TimeLineActivity.mDataList.add(new TimeLineModel(name, actarr_date + " " + actdep, TrainStatus.DEPARTED));
                                    } else if (!has_arrived && !has_departed) {
                                        TimeLineActivity.mDataList.add(new TimeLineModel(name, actarr_date + " " + actarr, TrainStatus.ARRIVING));
                                    } else if (has_arrived && !has_departed) {
                                        TimeLineActivity.mDataList.add(new TimeLineModel(name, actarr_date + " " + actarr, TrainStatus.AVAILABLE));
                                    }
                                }
                                startActivity(new Intent(GetStatusActivity.this, TimeLineActivity.class));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            Log.i("Errrroooorrrr",error.getMessage());
                            NetworkResponse response = error.networkResponse;
                            if (error instanceof ServerError && response != null) {
                                try {
                                    String string = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                    Log.i("Finally", string);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
                    request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue queue = Volley.newRequestQueue(GetStatusActivity.this);
                    queue.add(request);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }
}
