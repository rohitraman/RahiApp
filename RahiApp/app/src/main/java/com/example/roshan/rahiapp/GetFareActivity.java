package com.example.roshan.rahiapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
//Format of response
//{"Adult":{"2A":"1,330","3A":"940","GN":"185","SL":"355"},"Adult Tatkal":{"2A":"1,750","3A":"1,255","GN":"-","SL":"455"},"Child":{"2A":"665","3A":"475","GN":"100","SL":"185"},"Child Tatkal":{"2A":"1,095","3A":"770","GN":"-","SL":"295"},"Sen. Female":{"2A":"715","3A":"515","GN":"185","SL":"205"},"Sen. Male":{"2A":"840","3A":"600","GN":"185","SL":"235"}}



public class GetFareActivity extends AppCompatActivity {

    AutoCompleteTextView etGetFrom, etGetTo;
    EditText etGetTrainNo;
    Button button;
    String from,to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_fare);
        etGetFrom = (AutoCompleteTextView) findViewById(R.id.et_from_1);
        etGetTo = (AutoCompleteTextView) findViewById(R.id.et_to_1);
        etGetTrainNo = (EditText) findViewById(R.id.et_train_no_1);
        button = (Button) findViewById(R.id.btn_get_fare);

        CustomAutoTextViewAdapter adapter = new CustomAutoTextViewAdapter(this,android.R.layout.simple_list_item_1,GetStations.stations);
        etGetFrom.setAdapter(adapter);
        etGetTo.setAdapter(adapter);
        etGetFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = String.valueOf(adapterView.getItemAtPosition(i));
                String[] items = item.split(" ");
                from = items[items.length-1];
            }
        });
        etGetTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                JSONObject object = new JSONObject();
                try {
                    object.put("from", from);
                    object.put("to",to);
                    object.put("train_no",Integer.parseInt(etGetTrainNo.getText().toString()));
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://railwayapi.herokuapp.com/getFare", object, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.i("Response",response.toString());
                                JSONObject adult = response.getJSONObject("Adult");
                                JSONObject adultTatkal = response.getJSONObject("Adult Tatkal");
                                JSONObject child = response.getJSONObject("Child");
                                JSONObject childTatkal = response.getJSONObject("Child Tatkal");
                                JSONObject senFemale = response.getJSONObject("Sen. Female");
                                JSONObject senMale = response.getJSONObject("Sen. Male");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Error",error.toString());
                            NetworkResponse response = error.networkResponse;
                            if (error instanceof ServerError && response!=null)
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
                    request.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES*2,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue queue = Volley.newRequestQueue(GetFareActivity.this);
                    queue.add(request);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
