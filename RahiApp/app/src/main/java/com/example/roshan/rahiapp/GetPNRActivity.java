package com.example.roshan.rahiapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
//Format of response
// {"boardingPoint":"CLT","bookingDate":"Jul 1, 2018 12:00:00 AM","bookingFare":540,"chartStatus":"Chart Not Prepared","dateOfJourney":"Jul 22, 2018 12:00:00 AM","destinationStation":"MAQ","generatedTimeStamp":{"day":2,"hour":14,"minute":5,"month":7,"second":4,"year":2018},"informationMessage":["",""],"isWL":"N","journeyClass":"3A","numberOfpassenger":1,"passengerList":[{"bookingBerthCode":"LB","bookingBerthNo":57,"bookingCoachId":"B1","bookingStatus":"CNF","bookingStatusDetails":"CNF\/B1\/57\/LB","bookingStatusIndex":0,"childBerthFlag":false,"concessionOpted":false,"currentBerthNo":0,"currentCoachId":"","currentStatus":"CNF","currentStatusDetails":"CNF","currentStatusIndex":0,"forGoConcessionOpted":false,"passengerCoachPosition":0,"passengerIcardFlag":false,"passengerNationality":"IN","passengerQuota":"PQ","passengerSerialNumber":1,"waitListType":0}],"pnrNumber":"4246494122","quota":"PQ","reasonType":"S","reservationUpto":"MAQ","serverId":"aposnd02:instance3","sourceStation":"CLT","ticketFare":540,"ticketTypeInPrs":"E","timeStamp":"Jul 2, 2018 2:05:04 PM","trainName":"MANGALORE MAIL","trainNumber":"12601","waitListType":0}


public class GetPNRActivity extends AppCompatActivity {
    EditText etPNR;
    Button button;
    TextView tvPNR,tvFrom,tvTo,tvTrainNo,tvTrainName,tv_total_passengers,tv_doj;
    List<Integer> nums = new ArrayList<>();
    List<String> bStatuses = new ArrayList<>();
    List<String> cStatuses = new ArrayList<>();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_pnr);
        etPNR = (EditText) findViewById(R.id.editText6);
        button = (Button) findViewById(R.id.btn_pnr);
        tvPNR = (TextView) findViewById(R.id.tvPNR);
        tvFrom = (TextView) findViewById(R.id.tvFrom);
        tvTo = (TextView) findViewById(R.id.tvTo);
        tvTrainName = (TextView) findViewById(R.id.tvTrainName);
        tvTrainNo = (TextView) findViewById(R.id.tvTrainNo);
        tv_total_passengers = (TextView) findViewById(R.id.tv_total_passengers);
        tv_doj = (TextView) findViewById(R.id.tv_doj);
        listView = (ListView)findViewById(R.id.lv_passengers);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pnr = etPNR.getText().toString();
                JSONObject object = new JSONObject();
                try {
                    object.put("pnr",pnr);
                    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://railwayapi.herokuapp.com/getPNR", object, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                                Log.i("Rsponse",response.toString());
                            try {
                                JSONObject boardingPoint = response.getJSONObject("boarding_point");
                                String doj = response.getString("doj");
                                String fromName = boardingPoint.getString("name");
                                JSONObject reservation_upto = response.getJSONObject("reservation_upto");
                                String toName = reservation_upto.getString("name");
                                String pnrNumber = response.getString("pnr");
                                JSONObject train = response.getJSONObject("train");
                                String trainName = train.getString("name");
                                String trainNumber = train.getString("number");
                                JSONArray array = response.getJSONArray("passengers");
                                for(int i=0;i<array.length();i++)
                                {
                                    JSONObject object1 = array.getJSONObject(i);
                                    String booking_status = object1.getString("booking_status");
                                    String current_status = object1.getString("current_status");
                                    int no = object1.getInt("no");
                                    nums.add(no);
                                    bStatuses.add(booking_status);
                                    cStatuses.add(current_status);

                                }
                                int total_passengers = response.getInt("total_passengers");
                                tv_total_passengers.setText(String.valueOf(total_passengers));
                                tv_doj.setText(doj);
                                tvPNR.setText(pnrNumber);
                                tvFrom.setText(fromName);
                                tvTo.setText(toName);
                                tvTrainName.setText(trainName);
                                tvTrainNo.setText(trainNumber);
                                CustomAdapter adapter = new CustomAdapter();
                                listView.setAdapter(adapter);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error!=null) {
                              try
                              {
                                Log.i("Erroe", error.toString());
                            }catch (Exception e)
                              {
                                  Log.i("Ball",e.getMessage());
                              }
                            }
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

                    request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES*2,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue queue = Volley.newRequestQueue(GetPNRActivity.this);
                    queue.add(request);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    class CustomAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return nums.size();
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
            view = getLayoutInflater().inflate(R.layout.pnr_layout,viewGroup,false);
            TextView tvNo = (TextView) view.findViewById(R.id.tv_no);
            TextView tvBS = (TextView) view.findViewById(R.id.tvBookStat);
            TextView tvCS = (TextView) view.findViewById(R.id.tvCurrStat);

            tvNo.setText(nums.get(i)+".");
            tvBS.setText(bStatuses.get(i));
            tvCS.setText(cStatuses.get(i));

            return view;
        }
    }
}
