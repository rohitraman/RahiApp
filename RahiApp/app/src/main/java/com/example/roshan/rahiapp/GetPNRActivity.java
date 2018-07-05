package com.example.roshan.rahiapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
// {"boardingPoint":"CLT","bookingDate":"Jul 1, 2018 12:00:00 AM","bookingFare":540,"chartStatus":"Chart Not Prepared","dateOfJourney":"Jul 22, 2018 12:00:00 AM","destinationStation":"MAQ","generatedTimeStamp":{"day":2,"hour":14,"minute":5,"month":7,"second":4,"year":2018},"informationMessage":["",""],"isWL":"N","journeyClass":"3A","numberOfpassenger":1,"passengerList":[{"bookingBerthCode":"LB","bookingBerthNo":57,"bookingCoachId":"B1","bookingStatus":"CNF","bookingStatusDetails":"CNF\/B1\/57\/LB","bookingStatusIndex":0,"childBerthFlag":false,"concessionOpted":false,"currentBerthNo":0,"currentCoachId":"","currentStatus":"CNF","currentStatusDetails":"CNF","currentStatusIndex":0,"forGoConcessionOpted":false,"passengerCoachPosition":0,"passengerIcardFlag":false,"passengerNationality":"IN","passengerQuota":"PQ","passengerSerialNumber":1,"waitListType":0}],"pnrNumber":"4246494122","quota":"PQ","reasonType":"S","reservationUpto":"MAQ","serverId":"aposnd02:instance3","sourceStation":"CLT","ticketFare":540,"ticketTypeInPrs":"E","timeStamp":"Jul 2, 2018 2:05:04 PM","trainName":"MANGALORE MAIL","trainNumber":"12601","waitListType":0}


public class GetPNRActivity extends AppCompatActivity {
    EditText etPNR;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_pnr);
        etPNR = (EditText) findViewById(R.id.editText6);
        button = (Button) findViewById(R.id.btn_pnr);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pnr = etPNR.getText().toString();
                JSONObject object = new JSONObject();
                try {
                    object.put("pnr",pnr);
                    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://192.168.0.105:8080/getPNR", object, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String boardingPoint = response.getString("boardingPoint");
                                int bookingFare = response.getInt("bookingFare");
                                String destinationStation = response.getString("destinationStation");
                                String journeyClass = response.getString("journeyClass");
                                String isWL = response.getString("isWL");
                                String pnrNumber = response.getString("pnrNumber");
                                int numberOfpassenger = response.getInt("numberOfpassenger");
                                String quota = response.getString("quota");
                                String trainName = response.getString("trainName");
                                String trainNumber = response.getString("trainNumber");
                                JSONArray array = response.getJSONArray("passengerList");
                                for (int i =0; i<array.length();i++)
                                {
                                    JSONObject object1 =array.getJSONObject(i);
                                    String bookingBerthCode = object1.getString("bookingBerthCode");
                                    int bookingBerthNo = object1.getInt("bookingBerthNo");
                                    String bookingCoachId = object1.getString("bookingCoachId");
                                    String bookingStatus = object1.getString("bookingStatus");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            NetworkResponse response = error.networkResponse;
                            if(error instanceof ServerError && response!=null)
                            {
                                String response1 = null;
                                try {
                                    response1 = new String(response.data, HttpHeaderParser.parseCharset(response.headers,"utf-8"));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                Log.i("Erroorr",response1);
                            }
                        }
                    });
                    RequestQueue queue = Volley.newRequestQueue(GetPNRActivity.this);
                    queue.add(request);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
