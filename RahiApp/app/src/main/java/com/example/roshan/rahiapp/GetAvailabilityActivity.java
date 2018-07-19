package com.example.roshan.rahiapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//Format of response
//{"journey_class": {"name": "THIRD AC", "code": "3A"}, "debit": 3, "response_code": 200, "from_station": {"lng": 80.2755685, "code": "MAS", "name": "CHENNAI CENTRAL", "lat": 13.081674}, "train": {"classes": [{"available": "Y", "name": "SECOND AC", "code": "2A"}, {"available": "N", "name": "3rd AC ECONOMY", "code": "3E"}, {"available": "N", "name": "AC CHAIR CAR", "code": "CC"}, {"available": "N", "name": "SECOND SEATING", "code": "2S"}, {"available": "N", "name": "FIRST AC", "code": "1A"}, {"available": "N", "name": "FIRST CLASS", "code": "FC"}, {"available": "Y", "name": "THIRD AC", "code": "3A"}, {"available": "Y", "name": "SLEEPER CLASS", "code": "SL"}], "days": [{"code": "MON", "runs": "Y"}, {"code": "TUE", "runs": "Y"}, {"code": "WED", "runs": "Y"}, {"code": "THU", "runs": "Y"}, {"code": "FRI", "runs": "Y"}, {"code": "SAT", "runs": "Y"}, {"code": "SUN", "runs": "Y"}], "name": "MANGALORE MAIL", "number": "12601"}, "quota": {"name": "GENERAL QUOTA", "code": "GN"}, "to_station": {"lng": 76.272355, "code": "SRR", "name": "SHORANUR JN", "lat": 10.7637196}, "availability": [{"status": "PQWL2/WL2", "date": "22-7-2018"}, {"status": "PQWL1/WL1", "date": "23-7-2018"}, {"status": "AVAILABLE-0005", "date": "24-7-2018"}, {"status": "AVAILABLE-0004", "date": "25-7-2018"}, {"status": "PQWL5/WL5", "date": "26-7-2018"}, {"status": "PQWL9/WL8", "date": "27-7-2018"}]}


public class GetAvailabilityActivity extends AppCompatActivity {

    AutoCompleteTextView train_no;
    EditText clas, quota, date1;
    AutoCompleteTextView etFrom, etTo;
    Button button;
    String train;
    String day,from,to;
    String month;
    String year;
    String date,date3;
    DatePickerDialog dialog;
    List<String> dates1 = new ArrayList<>();
    List<String> status = new ArrayList<>();
    ListView view1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_availability);
        train_no = (AutoCompleteTextView) findViewById(R.id.editText);
        etFrom = (AutoCompleteTextView) findViewById(R.id.editText2);
        etTo = (AutoCompleteTextView) findViewById(R.id.editText3);
        clas = (EditText) findViewById(R.id.editText4);
        quota = (EditText) findViewById(R.id.editText5);
        date1 = (EditText)findViewById(R.id.editText8);
        button = (Button) findViewById(R.id.button);
        view1 = (ListView) findViewById(R.id.lv_availability);

        CustomAutoTextViewAdapter adapter = new CustomAutoTextViewAdapter(this,android.R.layout.simple_list_item_1,GetStationsAndTrains.stations);
        etFrom.setAdapter(adapter);
        etTo.setAdapter(adapter);
        CustomAutoTextViewAdapter adapter1 = new CustomAutoTextViewAdapter(this,android.R.layout.simple_list_item_1,GetStationsAndTrains.trains);
        train_no.setAdapter(adapter1);
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

        train_no.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = String.valueOf(adapterView.getItemAtPosition(i));
                String[] items = item.split(" ");
                train = items[0];
            }
        });

        date1.setShowSoftInputOnFocus(false);

        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    final Calendar calendar = Calendar.getInstance();
                    int yy = calendar.get(Calendar.YEAR);
                    int mm = calendar.get(Calendar.MONTH);
                    int dd = calendar.get(Calendar.DAY_OF_MONTH);
                    dialog = new DatePickerDialog(GetAvailabilityActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            date = String.valueOf(i2) + "-" + String.valueOf(i1 + 1) + "-" + String.valueOf(i);
                            date3 = date;
                            date1.setText(date);
                        }
                    }, yy, mm, dd);
                    dialog.show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] dates = date.split("-");
                day = dates[0];
                month = dates[1];
                year = dates[2];
                JSONObject object = new JSONObject();
                try {
                    object.put("from", from);
                    object.put("to", to);
                    object.put("cls", clas.getText().toString().toUpperCase());
                    object.put("qt", quota.getText().toString().toUpperCase());
                    object.put("dd", Integer.parseInt(day));
                    object.put("mm", Integer.parseInt(month));
                    object.put("yyyy", Integer.parseInt(year));
                    object.put("date",date3);
                    object.put("train_no", train);
                    Log.i("Hellooo", object.toString());
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://railwayapi.herokuapp.com/getAvailability", object, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                dates1.clear();
                                status.clear();
                                JSONArray array = response.getJSONArray("availability");
                                JSONObject journey_class = response.getJSONObject("journey_class");
                                String name = journey_class.getString("name");
                                for (int i =0;i<array.length();i++ )
                                {
                                    JSONObject object1 = array.getJSONObject(i);
                                    Log.i("obj",object1.toString());
                                    dates1.add(object1.getString("date"));
                                    status.add(object1.getString("status"));
                                }
                                Log.i("size",String.valueOf(dates1.size())+status.size());
                                CustomAdapter adapter2 = new CustomAdapter();
                                view1.setAdapter(adapter2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            NetworkResponse response = error.networkResponse;
                            if (error instanceof ServerError && response != null) {
                                try {
                                    String object1 = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                    Log.i("Error", object1);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES*2,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue requestQueue = Volley.newRequestQueue(GetAvailabilityActivity.this);
                    requestQueue.add(request);

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
            return dates1.size();
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
            view = getLayoutInflater().inflate(R.layout.layout_list_status,viewGroup,false);
            TextView tvDates = (TextView) view.findViewById(R.id.tv_date);
            TextView tvStatus = (TextView) view.findViewById(R.id.tv_status);
            tvDates.setText(dates1.get(i));
            tvStatus.setText(status.get(i));
            return view;
        }
    }
}
